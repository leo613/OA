package com.taotao.order.query.api;

import com.taotao.order.query.bean.Order;
import com.taotao.order.query.bean.TaotaoResult;

public interface OrderApiService {

    /**
     * 创建订单
     * @param json
     * @return
     */
    public TaotaoResult createOrder(String json);

    /**
     * 查询订单
     * @param orderId
     * @return
     */
    public Order queryOrderById(String orderId);
}
