package com.mr.service;

import com.mr.bo.AddressBo;
import com.mr.bo.CartBo;
import com.mr.bo.OrderBo;
import com.mr.bo.UserInfo;
import com.mr.client.AddressClient;
import com.mr.client.GoodsClient;
import com.mr.mapper.OrderDetailMapper;
import com.mr.mapper.OrderMapper;
import com.mr.mapper.OrderStatusMapper;
import com.mr.pojo.Order;
import com.mr.pojo.OrderDetail;
import com.mr.pojo.OrderStatus;
import com.mr.repository.Sku;
import com.mr.repository.Stock;
import com.mr.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrdersService {

    @Autowired
    private AddressClient addressClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;


//    @Transactional
    public Long createOrder(OrderBo orderBo, UserInfo userInfo) {
        long orderId = new IdWorker().nextId();

        Order order = new Order();

        order.setOrderId(orderId); // id 订单号
        order.setInvoiceType(orderBo.getInvoiceType()); // 发票类型
        order.setBuyerMessage(orderBo.getBuyerMessage()); // 买家留言
        order.setPaymentType(orderBo.getPaymentType()); // 支付方式
        order.setCreateTime(new Date()); // 订单创建日期
        order.setUserId(userInfo.getId()); // 用户id
        order.setBuyerNick(userInfo.getUsername()); // 用户昵称
        order.setBuyerRate(false); // 是否已评价
        order.setSourceType(2); // 订单来源 (没写呢)
        order.setPostFee(0L); // 邮费 (没写呢)
        // sku信息 id num
////        private List<CartBo> cartList;
        Long totalPay=0l;
        Long actualPay=0l;
        for (CartBo cartBo : orderBo.getCartList()){
            Sku sku = goodsClient.querySkuBySkuId(cartBo.getSkuId());
            if(!sku.getEnable()){
                return 1L;
            }
            if(cartBo.getNum() > sku.getStock()){
                return 2L;
            }else{

                orderStatusMapper.update(sku.getId(), sku.getStock(), cartBo.getNum());
                Stock stock = goodsClient.queryStockBySkuId(sku.getId());
                if(stock.getStock() < 0){
                    return 3L;
                }
            }
            cartBo.setSku(sku);
            totalPay+= sku.getPrice()*cartBo.getNum();
        }
        order.setTotalPay(totalPay); // 总金额
        order.setActualPay(actualPay); // 实付金额

        // 收货人信息
        AddressBo addressBo = addressClient.addressClient(orderBo.getAddressId());
        order.setReceiver(addressBo.getName()); // 收货人
        order.setReceiverState(addressBo.getState()); // 省
        order.setReceiverCity(addressBo.getCity()); // 市
        order.setReceiverDistrict(addressBo.getDistrict()); // 区/县
        order.setReceiverAddress(addressBo.getAddress()); // 街道、住址等详细地址
        order.setReceiverMobile(addressBo.getPhone()); // 手机
        order.setReceiverZip(addressBo.getZipCode()); // 邮编

        orderMapper.insertSelective(order);

        OrderDetail orderDetail = new OrderDetail();

        for (CartBo cartBo : orderBo.getCartList()) {
            orderDetail.setOrderId(orderId); // 订单id
            orderDetail.setSkuId(cartBo.getSkuId()); // sku商品id
            orderDetail.setNum(cartBo.getNum()); // 购买数量
            orderDetail.setTitle(cartBo.getSku().getTitle()); // 商品标题
            orderDetail.setOwnSpec(cartBo.getSku().getOwnSpec()); // 商品动态属性键值集
            orderDetail.setPrice(cartBo.getSku().getPrice()); // 价格
            orderDetail.setImage(cartBo.getSku().getImages().split(",")[0]); // 商品图片
            if(orderDetail.getId()!=null){
                orderDetail.setId(orderDetail.getId()+1);
            }
            orderDetailMapper.insertSelective(orderDetail);
        }
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId); // 订单id
        orderStatus.setStatus(1); // 状态 1 未付款
        orderStatus.setCreateTime(new Date()); // 订单创建时间
//        orderStatus.setPaymentTime(null); // 付款时间
//        orderStatus.setConsignTime(null); // 发货时间
//        orderStatus.setEndTime(null); // 交易完成时间
//        orderStatus.setCloseTime(null); // 交易关闭时间
//        orderStatus.setCommentTime(null); // 评价时间

        orderStatusMapper.insertSelective(orderStatus);


        return orderId;
    }
}
