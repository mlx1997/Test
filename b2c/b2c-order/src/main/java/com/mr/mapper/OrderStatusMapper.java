package com.mr.mapper;

 import com.mr.pojo.OrderStatus;
 import org.apache.ibatis.annotations.Mapper;
 import org.apache.ibatis.annotations.Param;
 import org.apache.ibatis.annotations.Update;


@Mapper
public interface OrderStatusMapper extends tk.mybatis.mapper.common.Mapper<OrderStatus>{

  @Update("update tb_stock set stock= #{stock}-#{num} where sku_id=#{skuId} and stock >0")
  public Integer update(@Param("skuId") Long skuId,@Param("stock") Integer stock,@Param("num") Integer num);
// @Delete("delete from tb_category_brand where brand_id = #{bid}")
// public int deleteCategoryBrand(@Param("bid") Long bid);
}
