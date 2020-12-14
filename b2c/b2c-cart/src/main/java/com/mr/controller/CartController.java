package com.mr.controller;

import com.mr.bo.Cart;
import com.mr.bo.UserInfo;
import com.mr.config.JwtConfig;
import com.mr.service.CartService;
import com.mr.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtConfig config;

    /**
     * 增加商品信息至redis
     * @param cart
     * @param token
     * @return
     */
    @PostMapping("addCart")
    public ResponseEntity<Void> addCart(@RequestBody Cart cart , @CookieValue("B2C_TOKEN") String token){

        try {
            // 用户是否登录
            UserInfo info = JwtUtils.getInfoFromToken(token, config.getPublicKey());
            cartService.addCart(cart,info);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(null);
    }

    /**
     * 查询 当前用户下所有购物车商品
     * @param token
     * @return
     */
    @GetMapping("queryCart")
    public ResponseEntity<List<Cart>> queryCartList(@CookieValue("B2C_TOKEN") String token){

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, config.getPublicKey());

            List<Cart> cartList=cartService.queryCartList(userInfo);

            if(cartList == null || cartList.size() == 0){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(cartList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PutMapping("updateNum")
    public ResponseEntity<Void> updateNum(
            @RequestParam(value="skuId") Long skuId,
            @RequestParam(value="num") Integer num,
            @CookieValue("B2C_TOKEN") String token){
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, config.getPublicKey());

            cartService.updateNum(skuId,num, userInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("deleteCart/{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId,@CookieValue("B2C_TOKEN") String token){

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, config.getPublicKey());

            cartService.deleteCart(skuId, userInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(null);
    }
}
