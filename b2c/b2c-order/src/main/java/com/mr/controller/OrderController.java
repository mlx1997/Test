package com.mr.controller;

import com.mr.bo.OrderBo;
import com.mr.bo.UserInfo;
import com.mr.config.JwtConfig;
import com.mr.enums.ExceptionEnums;
import com.mr.exception.MrException;
import com.mr.utils.JwtUtils;
import com.mr.service.OrdersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("订单")
public class OrderController {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private OrdersService ordersService;
    /**
     * 增加订单
     * 返回订单号
     * @param orderBo
     * @return
     */
    @PostMapping("createOrder")
    @ApiOperation(value="创建订单接口,返回订单编号",notes="创建订单")
    @ApiImplicitParam(name="createOrder",required = true,value="订单的json对象,包含订单地址，sku，支付方式等信息")
    public ResponseEntity<String> createOrder(
            @RequestBody OrderBo orderBo, @CookieValue("B2C_TOKEN")String token){
        Long order=0L;
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
           order  = ordersService.createOrder(orderBo, userInfo);
        } catch (Exception e) {
            e.printStackTrace();

        }
        if(order==1L){
            throw new MrException(ExceptionEnums.SKU_IS_OFF_THE_SHELF);
        }else if(order==2L){
            throw new MrException(ExceptionEnums.STOCK_IS_NOT_ENOUGH);
        }else if(order==3L){
            throw new MrException(ExceptionEnums.STOCK_IS_NOT_ENOUGH);
        }else{
            return ResponseEntity.ok(order.toString());
        }

    }
}
