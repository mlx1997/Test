package com.mr.repository;

import java.util.List;

/**
 * 返回结果
 */
public class SpuBo extends Spu {

    String categoryName;// 商品分类名称
    
    String brandName;// 品牌名称

    SpuDetail spuDetail;

    List<Sku> skus;

    public SpuBo() {
    }

    public SpuBo(String categoryName, String brandName) {
        this.categoryName = categoryName;
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}