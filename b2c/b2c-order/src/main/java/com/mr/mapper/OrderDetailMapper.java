package com.mr.mapper;

 import com.mr.pojo.OrderDetail;
 import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;


@Mapper
public interface OrderDetailMapper extends tk.mybatis.mapper.common.Mapper<OrderDetail>, InsertListMapper<OrderDetail> {
}
