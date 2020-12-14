package com.mr.web;

import com.mr.enums.ExceptionEnums;
import com.mr.exception.MrException;
import com.mr.repository.Brand;
import com.mr.service.BrandService;
import com.mr.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    //  查询
    @GetMapping("queryBrand")      //    required  必须填写的属性  false 非必填
    public ResponseEntity<PageResult> queryBrand(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc
            ){
        PageResult<Brand> result = brandService.queryBrand(page, rows, key, sortBy, desc);

        if(result==null || result.getItems().size() == 0){
            //  没有数据 ,抛异常
            throw new MrException(ExceptionEnums.BRAND_CANNOT_IS_NULL);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("addBrand")
    public ResponseEntity<String> addBrand(Brand brand ,@RequestParam("cids") List<Long> cids){
        brandService.addBrand(brand,cids);
        return ResponseEntity.ok(null);
    }

    @PutMapping("updateBrand")
    public ResponseEntity<String> updateBrand(Brand brand ,@RequestParam("cids") List<Long> cids){
        brandService.updateBrand(brand,cids);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("deleteBrand/{bid}")
    public ResponseEntity<String> deleteBrand(@PathVariable("bid")Long bid){
        brandService.deleteBrand(bid);
        return ResponseEntity.ok(null);
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid")Long cid){
        List<Brand> list=brandService.queryBrandByCid(cid);
        if(list.size() == 0 ){
            throw new MrException(ExceptionEnums.CHABUCHULAI_IS_NULL);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("bid/{bid}")
    public ResponseEntity<Brand> queryBrandBybId(@PathVariable("bid")Long bid){
        Brand brand=brandService.queryBrandBybId(bid);
        return ResponseEntity.ok(brand);
    }
}
