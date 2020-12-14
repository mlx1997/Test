package com.mr.service;

import com.mr.repository.Category;
import com.mr.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryByPid(Long pid){
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    public void deleteById(Long id) {

        Category selectDangqian = categoryMapper.selectByPrimaryKey(id);

        Category category4 = new Category();
        category4.setParentId(selectDangqian.getParentId());

        List<Category> selectBydangqianPid = categoryMapper.select(category4);
        if(selectBydangqianPid.size()==1){

            Category selectParent = categoryMapper.selectByPrimaryKey(selectDangqian.getParentId());

            if(selectParent.getIsParent()){
                selectParent.setIsParent(false);
                categoryMapper.updateByPrimaryKey(selectParent);
            }
        }
        categoryMapper.deleteByPrimaryKey(id);

    }


    public void addCategory(Category category) {
        if(null!=category){
            if(category.getId() == null){
                categoryMapper.insert(category);
            }else{
                categoryMapper.updateByPrimaryKeySelective(category);
            }
        }
    }

    public List<Category> oldCategoriesFromBrand(Long bid){
        return categoryMapper.oldCategoriesFromBrand(bid);
    }

    public List<String> queryNameByIds(List<Long> asList) {

//        for (Long id : asList) {
//            Category category = new Category();
//            category.setId(id);
//            List<Category> select = categoryMapper.select(category);
//            List<String> names = new ArrayList<>();
//            names.add(select.get(0).getName());
//            return names;
//        }

        List<Category> list = categoryMapper.selectByIdList(asList);

        List<String> names = new ArrayList<>();

        for (Category category : list) {
            names.add(category.getName());
        }
        return names;
    }

    public List<String> queryCateNamesByIds(List<Long> cid) {

        List<Category> list = categoryMapper.selectByIdList(cid);

        List<String> names=list.stream().map(name->{
            return name.getName();
        }).collect(Collectors.toList());

        return names;
    }

    public Category queryCategoryById(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    public List<Category> queryCateByIds(List<Long> ids) {
        return categoryMapper.selectByIdList(ids);
    }
}
