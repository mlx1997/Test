package com.mr.service;

import com.mr.client.BrandClient;
import com.mr.client.CategoryClient;
import com.mr.client.GoodsClient;
import com.mr.client.SpecClient;
import com.mr.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsPageService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecClient specClient;

    public Map<String,Object> getPageInfo(Long spuId) {

//        - SPU
        Spu spu = goodsClient.querySpuById(spuId);
//        - SpuDetail
        SpuDetail spuDetail = goodsClient.querySpuDetailById(spuId);
//        - SKU集合
        List<Sku> skuList = goodsClient.list(spuId);
//        - 商品分类三级
        List<Category> categoryList = categoryClient.queryCateByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
//        - 品牌
        Brand brand = brandClient.queryBrandBybId(spu.getBrandId());
//        - 规格组（中含有该组下详细的规格参数）
        List<SpecGroup> specGroups = specClient.querySpecGroup(spu.getCid3());
        specGroups.forEach(specGroup -> {
            specGroup.setSpecParamList(specClient.querySpecParam(specGroup.getId(),null,null,null));
        });
//        - 分类下特有规格参数（页面需要展示）
        List<SpecParam> specParamList = specClient.querySpecParam(null, spu.getCid3(), null, false);

        Map<Long,String> specParamMap=new HashMap<>();
        specParamList.forEach(specParam -> {
            specParamMap.put(specParam.getId(),specParam.getName());
        });

        Map<String, Object> map=new HashMap<>();

        map.put("spu",spu);
        map.put("spuDetail",spuDetail);
        map.put("skuList",skuList);
        map.put("categoryList",categoryList);
        map.put("brand",brand);
        map.put("specGroups",specGroups);
        map.put("specParamMap",specParamMap);
        return map;
    }
}
