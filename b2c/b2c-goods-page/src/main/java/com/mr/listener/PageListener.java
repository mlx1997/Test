package com.mr.listener;

import com.mr.service.FileStaticService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageListener {

    @Autowired
    private FileStaticService fileStaticService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "page_save_update", durable = "true"),
            exchange = @Exchange(
                    value = "item_spu_change",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),key = {"spu.save","spu.update"}
    ))
    public void pageListener(Long id){
        System.out.println("page接收到id: " + id);

        try {
            fileStaticService.createStaticPage(id);
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("错误信息id: " + id);
        }
    }
}
