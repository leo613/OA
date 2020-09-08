package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.httpclient.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.order.query.api.OrderApiService;
import com.taotao.order.query.bean.TaotaoResult;
import com.taotao.pojo.Item;
import com.taotao.sso.query.bean.User;
import com.taotao.web.bean.Order;

import com.taotao.web.threadLocal.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class OrderService {
    @Autowired
    private   ApiService  apiService;

    @Autowired
    private OrderApiService orderApiService;

    @Value("${ORDER_TAOTAO_URL}")
    private String ORDER_TAOTAO_URL;


    ObjectMapper objectMapper=new ObjectMapper();

    /**
     * status: 200
     * msg: "OK"
     * data: "31431919252344" //订单号
     * ok: true
     * @param order
     * @return
     */
    public String submitOrder(Order order) {
        try {
            //往order中添加用户的参数信息   userId,nikname 添加订单必须要用到这两个字串
//        User user = this.userService.queryUserByToken(token);
            User user = UserThreadLocal.get();
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());

            String str = objectMapper.writeValueAsString(order);

            TaotaoResult re = orderApiService.createOrder(str);
            if (re.getStatus()==200){
               String  orderId= (String) re.getData();
                return orderId;
            }

//            HttpResult httpResult = this.apiService.doPostJson(ORDER_TAOTAO_URL +"/order/create", str);
//             if (httpResult.getCode()==200){
//                 String content = httpResult.getContent();
//                 JsonNode jsonNode = objectMapper.readTree(content);
//                 if (jsonNode.get("status").intValue()==200){
//                     return  jsonNode.get("data").textValue(); //返回的就是订单Id
//                 }
//             }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Order queryOrderByOrderId(String orderId) {
        try {
            String str = this.apiService.doGet(ORDER_TAOTAO_URL + "/order/query/" + orderId);
            Order order = objectMapper.readValue(str, Order.class);
            if (order!=null){
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
