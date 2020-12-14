package com.mr.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("订单服务")
public class TestController {

    @ApiOperation("查询用户")
    @ApiImplicitParam("需id")
    @GetMapping("queryById/{id}")
    public ResponseEntity<String> getById(@PathVariable("id") Integer id){

        System.out.println(id);

        return ResponseEntity.ok("啊哈");
    }
}
