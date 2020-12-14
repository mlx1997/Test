package com.mr.utils;

import com.mr.repository.Brand;
import com.mr.repository.Category;

import java.util.List;
import java.util.Map;

public class SearchResult<T> extends PageResult{

    private List<Category> categoryList;
    private List<Brand> brandList;
    private List<Map<String,Object>> specMap; // 规格参数过滤条件

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public List<Map<String, Object>> getSpecMap() {
        return specMap;
    }

    public void setSpecMap(List<Map<String, Object>> specMap) {
        this.specMap = specMap;
    }

    public SearchResult(Long total, Long totalPage, List items, List<Category> categoryList, List<Brand> brandList, List<Map<String, Object>> specMap) {
        super(total, totalPage, items);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.specMap = specMap;
    }
}
