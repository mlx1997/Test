package com.mr.mapper;

import com.mr.repository.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends tk.mybatis.mapper.common.Mapper<Category>, SelectByIdListMapper<Category,Long> {

    @Select("select * from tb_category c where id in (select cb.category_id from tb_category_brand cb where cb.brand_id=#{bid})")
    List<Category> oldCategoriesFromBrand(@Param("bid") Long bid);
}
