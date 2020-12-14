package com.mr.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mr.bo.GoodsBo;
import com.mr.client.BrandClient;
import com.mr.client.CategoryClient;
import com.mr.client.GoodsClient;
import com.mr.client.SpecClient;
import com.mr.repository.*;
import com.mr.utils.HighLightUtil;
import com.mr.utils.JsonUtils;
import com.mr.utils.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodIndexService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecClient specClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsReponsitory goodsReponsitory;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    public void querySpu(Long id){
        Spu spu = goodsClient.querySpuById(id);

        Goods goodsByspu = this.getGoodsByspu(spu);

        goodsReponsitory.save(goodsByspu);
    }
    

    public SearchResult<Goods> loadSearch(GoodsBo goodsBo) {
        // 创建 builder
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 查询条件


        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("all",goodsBo.getKey()));

        Map<String, String> filter = goodsBo.getFilter();
        if(filter.size() > 0){
            Set<String> filterSet = filter.keySet();

            for (String key : filterSet) {
                MatchQueryBuilder matchQueryBuilder=null;

                if(key.equals("cid3") || key.equals("brandId")){
                    matchQueryBuilder=QueryBuilders.matchQuery(key,filter.get(key));
                }else{
                    matchQueryBuilder=QueryBuilders.matchQuery("specs."+key+".keyword",filter.get(key));
                }
                boolQuery.must(matchQueryBuilder);

            }
        }
        builder.withQuery(boolQuery);

//        if(goodsBo!=null && StringUtils.isNotBlank(goodsBo.getKey())){
//            builder.withQuery(
//                    QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("all",goodsBo.getKey()))
//            );
//        }

        // 分页
        builder.withPageable(PageRequest.of(goodsBo.getPage()-1,goodsBo.getRows()));
        // 增加分组
        builder.addAggregation(AggregationBuilders.terms("brandGro").field("brandId"));
        builder.addAggregation(AggregationBuilders.terms("cid3Gro").field("cid3"));
        // 查询
        Page<Goods> goodsPage = goodsReponsitory.search(builder.build());
        // 用分组page 接
        AggregatedPage<Goods> page= (AggregatedPage<Goods>) goodsPage;
        // 根据别名 获得分组字段
        LongTerms brandTerms = (LongTerms) page.getAggregation("brandGro");
        LongTerms cid3Terms = (LongTerms) page.getAggregation("cid3Gro");
        // 获取 数据 stream.map 遍历
        List<LongTerms.Bucket> brandBuckets = brandTerms.getBuckets();
        List<Brand> brandList = brandBuckets.stream().map(bucket -> brandClient.queryBrandBybId( bucket.getKeyAsNumber().longValue())).collect(Collectors.toList());

        List<LongTerms.Bucket> cid3Buckets = cid3Terms.getBuckets();
        List<Category> categoryList = cid3Buckets.stream().map(bucket -> categoryClient.queryCategoryById(bucket.getKeyAsNumber().longValue())).collect(Collectors.toList());

        final List<Long> maxDocCount=new ArrayList<>(1);
        maxDocCount.add(1L);
        final List<Long> maxDocCateId=new ArrayList<>(1);
        maxDocCateId.add(1L);

        List<Long> cateIds = cid3Buckets.stream().map(bucket -> {
            if (maxDocCount.get(0) < bucket.getDocCount()) {
                maxDocCount.set(0, bucket.getDocCount());
                maxDocCateId.set(0, bucket.getKeyAsNumber().longValue());
            }
            return bucket.getKeyAsNumber().longValue();
        }).collect(Collectors.toList());

        List<Map<String, Object>> specMap = this.getSpecMap(maxDocCateId.get(0), goodsBo);

        // 高亮
        builder.withHighlightFields(new HighlightBuilder.Field("all").preTags("<font color='red'>").postTags("</font>"));
        Map<Long, String> all = HighLightUtil.getHignLigntMap(elasticsearchTemplate, builder.build(), Goods.class, "all");
        goodsPage.getContent().forEach(good -> good.setAll(all.get(good.getId())));
        // 计算 多少页数
        double a= (double) goodsPage.getTotalElements() / goodsBo.getRows();
        long ceil = (long) Math.ceil(a);

        return new SearchResult<Goods>(goodsPage.getTotalElements(),ceil,goodsPage.getContent(),categoryList,brandList,specMap);

    }

    // specMap 聚合
    private List<Map<String, Object>> getSpecMap(Long cid, GoodsBo goodsBo){
        // 根据 cid 查询出要展示的筛选规格
        List<SpecParam> specParamList = specClient.querySpecParam(null, cid, true,false);

        List<Map<String, Object>> specsMap = new ArrayList<>();

        // 从 es 索引库 查询数据
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        builder.withPageable(PageRequest.of(0,1));

        if(StringUtils.isNotEmpty(goodsBo.getKey())){
            builder.withQuery(QueryBuilders.matchQuery("all",goodsBo.getKey()));
        }

        specParamList.forEach(list->{
            String name = list.getName();
            builder.addAggregation(AggregationBuilders.terms(name).field("specs."+name+".keyword"));
        });
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        Map<String, String> filter = goodsBo.getFilter();
        if(filter.size() > 0){
            Set<String> filterSet = filter.keySet();

            for (String key : filterSet) {
                MatchQueryBuilder matchQueryBuilder=null;

                if(key.equals("cid3") || key.equals("brandId")){
                    matchQueryBuilder=QueryBuilders.matchQuery(key,filter.get(key));
                }else{
                    matchQueryBuilder=QueryBuilders.matchQuery("specs."+key+".keyword",filter.get(key));
                }
                boolQuery.must(matchQueryBuilder);

            }
        }
        builder.withQuery(boolQuery);

        AggregatedPage<Goods> page = (AggregatedPage<Goods>) goodsReponsitory.search(builder.build());

        specParamList.forEach(list -> {
            Map<String, Object> map=new HashMap<>();
            map.put("key",list.getName());

            StringTerms terms = (StringTerms) page.getAggregation(list.getName());

            List<StringTerms.Bucket> buckets = terms.getBuckets();

            List<String> values = buckets.stream().map(bucket -> {
                return bucket.getKeyAsString();
            }).collect(Collectors.toList());

            map.put("values",values);
            specsMap.add(map);
        });

        return specsMap;
    }


    // 导入数据
    public Goods getGoodsByspu(Spu spu){

        Long id=spu.getId();
        // 准备 sku集合
        List<Sku> skus = goodsClient.list(id);
        // detail
        SpuDetail spuDetail = goodsClient.querySpuDetailById(id);
        // 商品的三级分类
        List<String> names=categoryClient.queryCateNamesByIds(
                Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
        // 查询spu所属分类的规格
        List<SpecParam> params = specClient.querySpecParam(null, spu.getCid3(), true,null);
        // 查询brand名称
        Brand brand = brandClient.queryBrandBybId(spu.getBrandId());


        // 价格集合
        List<Long> prices=new ArrayList<>();
        // 填充sku属性 sku用map不用实体类 装入价格 图片等页面需要的元素
        List<Map<String,Object>> skuList=new ArrayList<>();
        for (Sku sku:skus){
            prices.add(sku.getPrice());
            Map<String, Object> map=new HashMap<>();

            map.put("id",sku.getId());
            map.put("title",sku.getTitle());

            // 取第一张图片即可
            map.put("image", StringUtils.isBlank(sku.getImages())?"":sku.getImages().split(",")[0]);
            map.put("price",sku.getPrice());

            skuList.add(map);
        }

        Map<Long ,String> genericMap = JsonUtils.parseMap(
                spuDetail.getGenericSpec(),Long.class,String.class);
        // TypeReference 对泛型的反序列化
        Map<Long ,List<String>> specialMap = JsonUtils.nativeRead(
                spuDetail.getSpecialSpec(), new TypeReference<Map<Long,List<String>>>() {
                });

        Map<String, Object> specs=new HashMap<>();

        for (SpecParam param:params){
            if(param.getGeneric()){
                String value = genericMap.get(param.getId());
                if(param.getNumeric()){
                    // 数值类型,区间
                    value = this.chooseSegment(value, param);
                }

                specs.put(param.getName(), value);
            }else{
                // 特有参数
                specs.put(param.getName(),specialMap.get(param.getId()));
            }
        }

        Goods goods = new Goods();

        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(id);
        goods.setSubTitle(spu.getSubTitle());
        // 搜索条件 拼接 标题、分类、品牌
        goods.setAll(spu.getTitle()+","+brand.getName()+","+StringUtils.join(names,","));
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specs);

        return goods;
    }


    private String chooseSegment(String value, SpecParam p){

        double val = NumberUtils.toDouble(value);
        String result="其它";

        for (String segment:p.getSegments().split(",")){
            String[] segs = segment.split("-");

            double begin=NumberUtils.toDouble(segs[0]);
            double end=Double.MAX_VALUE;

            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }

        }

        return result;
    }


}
