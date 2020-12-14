package com.mr.listener;

import com.mr.service.GoodIndexService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchListener {

    @Autowired
    private GoodIndexService goodIndexService;

    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(value="search_save_update", durable = "true"),
            exchange = @Exchange(
                    value="item_spu_change",
                    ignoreDeclarationExceptions = "true",
                    type= ExchangeTypes.TOPIC
            ),key = {"spu.save","spu.update"}
    ))
    public void searchListener(Long id){
        System.out.println("search接收到:" +id);
        try {
            goodIndexService.querySpu(id);
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("错误信息id: " + id);
        }
    }
}
