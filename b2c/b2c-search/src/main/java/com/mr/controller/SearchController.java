package com.mr.controller;

import com.mr.bo.GoodsBo;
import com.mr.repository.Goods;
import com.mr.service.GoodIndexService;
import com.mr.utils.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("goods")
public class SearchController {

    @Autowired
    private GoodIndexService goodIndexService;

    @PostMapping("loadSearch")
    public ResponseEntity<SearchResult<Goods>> loadSearch(@RequestBody GoodsBo goodsBo){

        System.out.println(goodsBo);

        SearchResult<Goods> page= goodIndexService.loadSearch(goodsBo);

        return ResponseEntity.ok(page);
    }
}
