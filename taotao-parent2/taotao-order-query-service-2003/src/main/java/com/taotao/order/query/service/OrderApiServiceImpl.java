package com.taotao.order.query.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.order.query.api.OrderApiService;
import com.taotao.order.query.bean.Order;
import com.taotao.order.query.bean.TaotaoResult;
import com.taotao.order.query.dao.IOrder;
import com.taotao.order.query.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderApiServiceImpl implements OrderApiService {


    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IOrder orderDao;

    @Override
    public TaotaoResult createOrder(String json) {

        System.out.println("order--------2003");
        Order order = null;
        try {
            order = objectMapper.readValue(json, Order.class);
            // 校验Order对象
            ValidateUtil.validate(order);
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(400, "请求参数有误!");
        }

        try {
            // 生成订单ID，规则为：userid+当前时间戳
            String orderId = order.getUserId() + "" + System.currentTimeMillis();
            order.setOrderId(orderId);

            // 设置订单的初始状态为未付款
            order.setStatus(1);

            // 设置订单的创建时间
            order.setCreateTime(new Date());
            order.setUpdateTime(order.getCreateTime());

            // 设置买家评价状态，初始为未评价
            order.setBuyerRate(0);

            // 持久化order对象
            orderDao.createOrder(order);

            //发送消息到RabbitMQ
//            Map<String, Object> msg = new HashMap<String, Object>(3);
//            msg.put("userId", order.getUserId());
//            msg.put("orderId", order.getOrderId());
//            List<Long> itemIds = new ArrayList<Long>(order.getOrderItems().size());
//            for (OrderItem orderItem : order.getOrderItems()) {
//                itemIds.add(orderItem.getItemId());
//            }
//            msg.put("itemIds", itemIds);
//            this.rabbitTemplate.convertAndSend(objectMapper.writeValueAsString(msg));

            return TaotaoResult.ok(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.build(400, "保存订单失败!");
    }

    @Override
    public Order queryOrderById(String orderId) {
        Order order = orderDao.queryOrderById(orderId);
        return order;
    }
}
