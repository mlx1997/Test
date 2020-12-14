package com.mr.api;

import com.mr.repository.Brand;
import com.mr.utils.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public interface BrandApi {

    //  查询
    @GetMapping("queryBrand")      //    required  必须填写的属性  false 非必填
    public PageResult queryBrand(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc
            );

    @PostMapping("addBrand")
    public String addBrand(Brand brand ,@RequestParam("cids") List<Long> cids);

    @PutMapping("updateBrand")
    public String updateBrand(Brand brand ,@RequestParam("cids") List<Long> cids);

    @DeleteMapping("deleteBrand/{bid}")
    public String deleteBrand(@PathVariable("bid")Long bid);

    @GetMapping("cid/{cid}")
    public List<Brand> queryBrandByCid(@PathVariable("cid")Long cid);

    @GetMapping("bid/{bid}")
    public Brand queryBrandBybId(@PathVariable("bid")Long bid);
}
