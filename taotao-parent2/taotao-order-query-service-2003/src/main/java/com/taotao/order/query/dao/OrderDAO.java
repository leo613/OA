package com.taotao.order.query.dao;


import com.taotao.order.query.bean.Order;
import com.taotao.order.query.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * mysql版本的实现
 * 
 */
public class OrderDAO implements IOrder{
	
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public void createOrder(Order order) {
		this.orderMapper.save(order);
	}

	@Override
	public Order queryOrderById(String orderId) {
		return this.orderMapper.queryByID(orderId);
	}





}
