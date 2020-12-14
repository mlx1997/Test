package com.mr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Component
public class FileStaticService {

    @Autowired
    private GoodsPageService goodsPageService;

    // 静态化模板
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${b2c.thymeleaf.destPath}")
    private String destPath;

    public void createStaticPage(Long id){
        // 创建上下文 context
        Context context=new Context();
        // 把数据加入上下文 中
        context.setVariables(goodsPageService.getPageInfo(id));
        // 创建最终文件输出流 规定 文件夹目录 文件名后缀 等
        File file = new File(destPath,id+".html");
        // 创建输出流 指定格式
        try {
            PrintWriter printWriter=new PrintWriter(file);
            templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
