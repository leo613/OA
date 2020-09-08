package com.taotao.cart.controller.api;

import com.taotao.cart.pojo.Cart;
import com.taotao.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/cart")
public class ApiCartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("/query/list/{userId}")
    public ResponseEntity<List<Cart>> queryCartByUserId(@PathVariable(value = "userId")Long userId){
        try {
            List<Cart> list = this.cartService.queryCartList(userId);
            if (list==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
          return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
