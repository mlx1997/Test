package com.mr.bo;

import lombok.Data;

import java.util.List;

@Data
public class OrderBo {

    // sku信息 id num
    private List<CartBo> cartList;
    // 邮寄地址 根据id查看详情
    private Long addressId;
    // 发票类型
    private Integer invoiceType;
    // 留言
    private String buyerMessage;
    // 支付方式
    private Integer paymentType;

}
