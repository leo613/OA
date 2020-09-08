package com.taotao.order.query.dao;


import com.taotao.order.query.bean.Order;

/**
 * 订单DAO接口
 */
public interface IOrder {

    /**
     * 创建订单
     * 
     * @param order
     */
    public void createOrder(Order order);

    /**
     * 根据订单ID查询订单
     * 
     * @param orderId
     * @return
     */
    public Order queryOrderById(String orderId);

}
