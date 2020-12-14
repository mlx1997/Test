package com.mr.bo;

import com.mr.repository.Sku;
import lombok.Data;

@Data
public class CartBo {
    private Long skuId;// 商品id
    private Integer num;// 购买数量

    private Sku sku;// sku其他信息

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }
}