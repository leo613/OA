package com.taotao.web.controller;

import com.taotao.pojo.Item;
import com.taotao.web.bean.Cart;
import com.taotao.web.bean.Order;
import com.taotao.web.bean.User;
import com.taotao.web.service.CartService;
import com.taotao.web.service.ItemService;
import com.taotao.web.service.OrderService;
import com.taotao.web.service.UserService;
import com.taotao.web.threadLocal.UserThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController {
     @Autowired
     private OrderService orderService;

     @Autowired
     private ItemService itemService;

     @Autowired
     private UserService userService;

     @Autowired
     private CartService cartService;


    /**
     *  根据商品Id  跳转至订单页面
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ModelAndView toOrder(@PathVariable(value = "itemId")Long itemId){
        Item item= this.itemService.queryItemByItemId(itemId);
        ModelAndView mv=new ModelAndView("order");
        mv.addObject("item",item);
        return mv;
    }

    /**
     * 提交订单信息
     */
    @RequestMapping(value = "submit",method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> submitOrder(Order order,@CookieValue(value ="taotao_cookie")String token){
        Map<String,Object> map=new HashMap<>();
        try {
            String order_id=  orderService.submitOrder(order);
            if(StringUtils.isNotBlank(order_id)){
                map.put("status",200);
                map.put("data",order_id);
            }else {
                map.put("stauts",500);
            }
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @RequestMapping(value = "success",method = RequestMethod.GET)
    public ModelAndView orderSuccess(@RequestParam("id")String orderId){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("success");
        Order order=orderService.queryOrderByOrderId(orderId);
         modelAndView.addObject("order",order);
         modelAndView.addObject("date",new DateTime().plusDays(2).toString("MM月dd天"));
        return modelAndView;
    }

    /**
     *根据用户信息 登录到订单确认页面
     */
    @RequestMapping(value = "/create",method = RequestMethod.GET)
    public ModelAndView toOrderConfig(){
        ModelAndView mv=new ModelAndView("order-cart");
        List<Cart> carts = this.cartService.queryCarts();
        mv.addObject("carts",carts);
        return mv;

    }

}
