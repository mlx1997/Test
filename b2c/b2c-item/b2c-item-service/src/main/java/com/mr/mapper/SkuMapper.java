package com.mr.mapper;

import com.mr.repository.Sku;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SkuMapper extends tk.mybatis.mapper.common.Mapper<Sku>{
    @Insert("insert into tb_stock(sku_id,stock) values(#{id},#{stock})")
    public void insertStock(Long id, Integer stock);

    @Select("select * from tb_sku t1 left join tb_stock t2 on t1.id=t2.sku_id where spu_id=#{spuId}")
    List<Sku> querySkuBySpuId(Long spuId);
}
