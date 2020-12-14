package com.mr;

import com.mr.client.GoodsClient;
import com.mr.repository.SpuBo;
import com.mr.service.FileStaticService;
import com.mr.utils.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GoodsPageApplication.class})
public class test {

    @Autowired
    private FileStaticService fileStaticService;

    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void test(){
        PageResult<SpuBo> aTrue = goodsClient.querySpu(0, 300, "0", null);
        aTrue.getItems().forEach(spu -> {
            fileStaticService.createStaticPage(spu.getId());
        });
    }
}
