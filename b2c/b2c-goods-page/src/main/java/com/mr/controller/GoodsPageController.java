package com.mr.controller;

import com.mr.service.GoodsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("item")
public class GoodsPageController {

    @Autowired
    private GoodsPageService goodsPageService;

    @GetMapping("{id}.html")
    public String page(@PathVariable("id") Long spuId, ModelMap map){
        Map<String, Object> pageInfo = goodsPageService.getPageInfo(spuId);
        map.putAll(pageInfo);
        return "item";
    }
}
