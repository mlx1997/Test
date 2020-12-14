package com.mr.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 枚举   定义常量对象   final static
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ExceptionEnums {
    PRICE_CANNOT_IS_NULL(400,"价格不能为空"),
    NAME_CANNOT_IS_NULL(500,"名字不能为空"),
    CATEGORY_CANNOT_IS_NULL(500,"名字不能为空"),
    BRAND_CANNOT_IS_NULL(500,"没有品牌数据"),
    IMAGE_CANNOT_IS_NULL(500,"图片不存在"),
    CHABUCHULAI_IS_NULL(500,"查不出来,抱歉"),
    STOCK_IS_NOT_ENOUGH(500,"库存不够,请重试"),
    SKU_IS_OFF_THE_SHELF(500,"商品已下架,你咋进来的"),
    ;
    private int code;
    private String message;
}
