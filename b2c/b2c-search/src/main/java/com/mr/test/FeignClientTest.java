package com.mr.test;

import com.mr.SearchApplication;
import com.mr.client.GoodsClient;
import com.mr.repository.Goods;
import com.mr.repository.GoodsReponsitory;
import com.mr.repository.SpuBo;
import com.mr.service.GoodIndexService;
import com.mr.utils.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class FeignClientTest {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsReponsitory goodsReponsitory;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodIndexService goodIndexService;


    @Test
    public void createIndex(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void testGoodsClient(){
        System.out.println(0);
        PageResult<SpuBo> pageResult = goodsClient.querySpu(0, 10, "0", "");

        System.out.println(1);

        System.out.println(pageResult);
        Objects.requireNonNull(pageResult).getItems().forEach(page->{
            System.out.println(page.getId()+"     "+page.getTitle());

        });

    }

    @Test
    public void loadGoodsData(){
        PageResult<SpuBo> pageResult = goodsClient.querySpu(0, 300, "0", "");

        List<SpuBo> spuBoList=pageResult.getItems();

        List<Goods> list=spuBoList.stream().map(
                spu-> goodIndexService.getGoodsByspu(spu)).collect(Collectors.toList());

        goodsReponsitory.saveAll(list);
    }

}
