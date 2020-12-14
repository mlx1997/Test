package com.mr.api;

import com.mr.repository.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/category")
public interface CategoryApi {

    @GetMapping("cateNames")
    public List<String> queryCateNamesByIds(@RequestParam("cid") List<Long> cid);

    @GetMapping("query")
    public Category queryCategoryById(@RequestParam("cid") Long id);

    @GetMapping("queryCateByIds")
    public List<Category> queryCateByIds(@RequestParam("cid") List<Long> ids);
}
