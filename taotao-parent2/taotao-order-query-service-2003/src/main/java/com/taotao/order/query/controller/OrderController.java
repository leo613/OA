package com.taotao.order.query.controller;


import com.taotao.order.query.api.OrderApiService;
import com.taotao.order.query.bean.Order;
import com.taotao.order.query.bean.TaotaoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/order")
@Controller
public class OrderController {

	@Autowired
	private OrderApiService orderApiService;
	
	/**
	 * 创建订单
	 * @param json
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create" , method = RequestMethod.POST)
	public TaotaoResult createOrder(@RequestBody String json) {
		return orderApiService.createOrder(json);
	}
	
	
	/**
	 * 根据订单ID查询订单
	 * @param orderId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/query/{orderId}" ,method = RequestMethod.GET)
	public Order queryOrderById(@PathVariable("orderId") String orderId) {
		return orderApiService.queryOrderById(orderId);
	}

}
