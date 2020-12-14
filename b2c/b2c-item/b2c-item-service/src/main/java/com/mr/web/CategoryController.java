package com.mr.web;

import com.mr.repository.Category;
import com.mr.enums.ExceptionEnums;
import com.mr.exception.MrException;
import com.mr.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryByPid(Long pid){
        List<Category> list = categoryService.queryCategoryByPid(pid);
        if(list == null || list.size() == 0){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_IS_NULL);
        }
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam("id") Long id){
        categoryService.deleteById(id);
    }

    @PostMapping("addCategory")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){

        categoryService.addCategory(category);
        System.out.println( category.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping("oldCategories/{bid}")
    public ResponseEntity<List<Category>> oldCategoriesFromBrand(@PathVariable("bid") Long bid){
        List<Category> categories = categoryService.oldCategoriesFromBrand(bid);
        if(categories.size()==0){
            return null;
        }
        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    @GetMapping("cateNames")
    public ResponseEntity<List<String>> queryCateNamesByIds(@RequestParam("cid") List<Long> cid){

        List<String> strings = categoryService.queryCateNamesByIds(cid);

        return ResponseEntity.ok(strings);
    }

    @GetMapping("query")
    public ResponseEntity<Category> queryCategoryById(@RequestParam("cid") Long id){

        return ResponseEntity.ok(categoryService.queryCategoryById(id));
    }

    @GetMapping("queryCateByIds")
    public ResponseEntity<List<Category>> queryCateByIds(@RequestParam("cid") List<Long> ids){
        return ResponseEntity.ok(categoryService.queryCateByIds(ids));
    }
}
