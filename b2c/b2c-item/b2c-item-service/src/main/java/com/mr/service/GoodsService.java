    package com.mr.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.mapper.*;
import com.mr.repository.*;
import com.mr.utils.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<SpuBo> querySpu(Integer page, Integer rows, String saleable, String key){
        PageHelper.startPage(page, rows);

        Example example = new Example(Spu.class);

        Example.Criteria criteria = example.createCriteria();

        if(saleable != null && !saleable.equals("0")) {
            criteria.andEqualTo("saleable", saleable.equals("true"));
        }

        if(key!=null && !key.equals("")){
            criteria.andLike("title","%"+key.trim()+"%");
        }

        Page<Spu> pageInfo= (Page<Spu>) goodsMapper.selectByExample(example);

        List<SpuBo> list=pageInfo.getResult().stream().map(spu -> {
            // 把spu变为spuBo
            SpuBo spuBo = new SpuBo();
            // 属性拷贝
            BeanUtils.copyProperties(spu, spuBo);

            //查询spu的品牌名
            List<String> names=categoryService.queryNameByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
            // 将分类名称拼接后存入
            spuBo.setCategoryName(StringUtils.join(names,"/"));

            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());

            spuBo.setBrandName(brand.getName());
            return spuBo;
        }).collect(Collectors.toList());

        return new PageResult<>(pageInfo.getTotal(),list);
    }

    public void saveGoods(SpuBo spuBo) {
        Spu spu = new Spu();
        spu.setBrandId(spuBo.getBrandId());
        spu.setCid1(spuBo.getCid1());
        spu.setCid2(spuBo.getCid2());
        spu.setCid3(spuBo.getCid3());
        spu.setSubTitle(spuBo.getSubTitle());
        spu.setTitle(spuBo.getTitle());
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(new Date());

        goodsMapper.insertSelective(spu);

        spuBo.getSpuDetail().setSpuId(spu.getId());
        spuDetailMapper.insertSelective(spuBo.getSpuDetail());

        for (Sku sku:spuBo.getSkus()){
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(new Date());
            skuMapper.insertSelective(sku);

            skuMapper.insertStock(sku.getId(),sku.getStock());
        }

        // mq
        amqpTemplate.convertAndSend("item_spu_change","spu.save",spu.getId());
        System.out.println("发送新增成功: " + spu.getId());
    }
    public SpuDetail querySpuDetailById(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);
    }

    public List<Sku> querySkuBySpuId(Long spuId) {
        return skuMapper.querySkuBySpuId(spuId);
    }

    public void updateGoods(SpuBo spuBo) {
        // 修改spu
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuBo, spu);
        goodsMapper.updateByPrimaryKeySelective(spu);
        // 修改spuDetail
        SpuDetail spuDetail= spuBo.getSpuDetail();
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);
        // 修改sku
        // 修改stock
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());

        List<Sku> skuList = skuMapper.select(sku);
        List<Long> skuId= skuList.stream().map(Sku::getId).collect(Collectors.toList());

        stockMapper.deleteByIdList(skuId);
        skuMapper.delete(sku);
        for (Sku skus:spuBo.getSkus()){
            skus.setSpuId(spu.getId());
            skus.setCreateTime(new Date());
            skus.setLastUpdateTime(new Date());
            skuMapper.insertSelective(skus);

            skuMapper.insertStock(skus.getId(),skus.getStock());
        }

        // mq
        amqpTemplate.convertAndSend("item_spu_change","spu.update",spu.getId());
        System.out.println("发送修改成功: " + spu.getId());
    }

    public void updateSaletable(Spu spu) {
        spu.setSaleable(!spu.getSaleable());
        goodsMapper.updateByPrimaryKeySelective(spu);

        // mq
        amqpTemplate.convertAndSend("item_spu_change","spu.update",spu.getId());
        System.out.println("发送修改成功: " + spu.getId());

    }

    public Spu querySpuById(Long spuId) {
        return goodsMapper.selectByPrimaryKey(spuId);
    }

    public Sku querySkuBySkuId(Long skuId) {

        Sku sku = skuMapper.selectByPrimaryKey(skuId);

        Stock stock = stockMapper.selectByPrimaryKey(skuId);
        sku.setStock(stock.getStock());
        return sku;
    }

    public Stock queryStockBySkuId(Long skuId) {
        return stockMapper.selectByPrimaryKey(skuId);
    }
}
