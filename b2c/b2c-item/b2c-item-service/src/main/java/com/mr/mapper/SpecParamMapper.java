package com.mr.mapper;

import com.mr.repository.SpecParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpecParamMapper extends tk.mybatis.mapper.common.Mapper<SpecParam>{

    @Select("select * from tb_spec_param where cid=#{cid}")
    List<SpecParam> queryBuCid(@Param("cid") Long cid);
}
