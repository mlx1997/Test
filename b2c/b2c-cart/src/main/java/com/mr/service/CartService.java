package com.mr.service;

import com.mr.bo.Cart;
import com.mr.bo.UserInfo;
import com.mr.client.GoodsClient;
import com.mr.repository.Sku;
import com.mr.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    public static void main(String[] args) {
        HashMap map = new HashMap();
        map.put("name","xuan");

        System.out.println("name".hashCode());

        int h;

        System.out.println((h = "name".hashCode()) ^ (h >>> 16));
        System.out.println(h);
        System.out.println(map);

        System.out.println(1 << 30);

        System.out.println(15 & 3373752);
    }

    @Autowired
    private GoodsClient goodsClient;

//    @Autowired
//    private RedisTemplate redisTemplate;  // 可以放java对象

    @Autowired
    private StringRedisTemplate redisTemplate;  // 可以放string 类型对象
    // 购物车商品
    private static final String KEY_PRE="b2c-cart-uid";
    // 数据库商品信息 enable stock newPrice ... 减少数据库查询
    private static final String KEY_SKU="b2c-sku-id";

    public void addCart(Cart cart, UserInfo info) {
        // cart 现在有 skuId num商品数量
        // info 有 用户id name(没用)
        // 这个数据是什么功能的数据  例 b2c-cart-uid
        String key=KEY_PRE+info.getId();

        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);

        String skuId=cart.getSkuId().toString();

        // 判断 redis是否已有(是否已买过)
        Boolean hasKey = ops.hasKey(skuId);

        Integer num = cart.getNum();
        if(hasKey){
            // 已买过  增加购买数量
            String cartJson = ops.get(skuId).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);

            cart.setNum(cart.getNum()+num);

        }else {
            // 未买过

            // 根据 skuId 查询出 该sku信息
            Sku sku = goodsClient.querySkuBySkuId(cart.getSkuId());

            // 放入数据
            cart.setTitle(sku.getTitle());
            // 图片只存入第一张
            cart.setImage(sku.getImages()==null?"":sku.getImages().split(",")[0]);
            cart.setPrice(sku.getPrice());
            // 规格应该将id 变为 汉字
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setUserId(info.getId());
        }

        ops.put(skuId, JsonUtils.serialize(cart));

    }

    public Sku addSku(Long skuId){
        String key=KEY_SKU+skuId;

        BoundValueOperations<String, String> ops = redisTemplate.boundValueOps(key);

        String s = ops.get();

        Sku sku=null;
        // 缓存中没有的话 就加进去
        if(s == null){
            sku = goodsClient.querySkuBySkuId(skuId);
            ops.set(JsonUtils.serialize(sku));
        }else{
            // 有的话直接取出来
            sku = JsonUtils.parse(s,Sku.class);
        }
        System.out.println(ops);
        return sku;
    }

    public List<Cart> queryCartList(UserInfo userInfo) {

        String key=KEY_PRE+userInfo.getId();
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);

        List<Object> values = ops.values();

        if(CollectionUtils.isEmpty(values)){
            return null;
        }
        List<Cart> cartList = values.stream().map(cart -> JsonUtils.parse(cart.toString(), Cart.class)).collect(Collectors.toList());

        cartList.forEach(cart -> {
            //            Sku sku = goodsClient.querySkuBySkuId(cart.getSkuId());
            Sku sku = this.addSku(cart.getSkuId());
            // 如果新价格(数据库) 与 老价格(redis)不相等  就set
            if(!cart.getPrice().equals(sku.getPrice())){
                cart.setNewPrice(sku.getPrice());
            }
            // 放入库存
            cart.setStock(sku.getStock());
            // 放入上下架 下架为false
            cart.setEnable(sku.getEnable());

        });

        return cartList;
    }

    public void updateNum(Long skuId, Integer num, UserInfo userInfo) {
        // cart 里 skuid num  userinfo userid

        String key = KEY_PRE + userInfo.getId();
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);
        Boolean aBoolean = ops.hasKey(skuId.toString());

        if(aBoolean){
            Cart cart  = JsonUtils.parse(ops.get(skuId.toString()).toString(), Cart.class);
            System.out.println(cart);

            cart.setNum(num);

            ops.put(skuId.toString(), JsonUtils.serialize(cart));

        }

    }

    public void deleteCart(Long skuId, UserInfo userInfo) {

        String key= KEY_PRE+userInfo.getId();
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);

        if(ops.hasKey(skuId.toString())){

            ops.delete(skuId.toString());

        }else{
            System.out.println("没有这玩意"+skuId);
        }

    }
}
