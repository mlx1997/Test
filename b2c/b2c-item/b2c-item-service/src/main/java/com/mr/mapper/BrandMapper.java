package com.mr.mapper;

import com.mr.repository.Brand;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BrandMapper extends tk.mybatis.mapper.common.Mapper<Brand>{

    //新增 category 、 brand 中间表 方法
    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{bid})")  //Insert mybatis注解 ,tkmapper无关
    public int insertCategoryBrand(@Param("bid") Long bid, @Param("cid") Long cid);

    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    public int deleteCategoryBrand(@Param("bid") Long bid);

    @Select("SELECT brand_id FROM tb_category_brand WHERE category_id = #{cid}")
    public List<Long> queryCateBrandByCid(Long cid);
}
