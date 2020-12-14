package com.mr.bo;

import lombok.Data;

import java.util.Map;

@Data
public class GoodsBo {
    private String key;

    private Integer page=1;
    private Integer rows=10;

    private Map<String,String> filter;
}
