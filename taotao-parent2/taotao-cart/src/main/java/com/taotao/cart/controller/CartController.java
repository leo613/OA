package com.taotao.cart.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartCookieService;
import com.taotao.cart.service.CartService;
import com.taotao.cart.threadlocal.UserThreadlocal;
import com.taotao.sso.query.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private CartCookieService cartCookieService;

    /**
     * 跳转至购物车页面
     * @param request
     * @return
     */
    @RequestMapping(value = "list",method = RequestMethod.GET)
    public ModelAndView cartList(HttpServletRequest request){
        ModelAndView mv=new ModelAndView("cart");
        User user = UserThreadlocal.get();
        List<Cart> list=new ArrayList<>();
        if (user!=null){
            //已登录
           list= this.cartService.queryCartList(user.getId());
        }else{
           //未登录
           list= this.cartCookieService.queryCartCookieList(request);
        }
        mv.addObject("cartList",list);
        return mv;
    }

    /**
     * 加入商品到购物车
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public String addItemToCart(@PathVariable(value = "itemId")Long itemId, HttpServletRequest request, HttpServletResponse response){
        User user = UserThreadlocal.get();
        if (user==null){
            //未登录状态
            try {
                this.cartCookieService.addItemToCart(itemId,request,response);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else{
            //登录状态 购物车添加商品
            this.cartService.addItemToCart(itemId);
        }
        return "redirect:/cart/list.html";
    }

    /**
     * 修改购物车中的商品数量
     */
    @RequestMapping(value = "/update/num/{itemId}/{num}")
    public ResponseEntity<Void> update(@PathVariable(value = "itemId")Long itemId, @PathVariable(value = "num")Integer num, HttpServletRequest request, HttpServletResponse response ){
        try {
            User user = UserThreadlocal.get();

            if (user!=null){
                //登录状态
                this.cartService.updateCartNum(itemId,num,user.getId());
            }else{
                //未登录状态
                this.cartCookieService.updateCartNum(num,itemId,request,response);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

     @RequestMapping(value = "/delete/{itemId}")
     public String deleteCartByItemId(@PathVariable("itemId")Long itemId,HttpServletRequest request, HttpServletResponse response){
         try {
             User user = UserThreadlocal.get();
             if(user!=null){
                 //登录状态
                 this.cartService.deleteCartByItemId(itemId,user.getId());
             }else {
                 //未登录状态
                 this.cartCookieService.deleteCartByItemId(itemId,request,response);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         return "redirect:/cart/list.html";
     }

}
