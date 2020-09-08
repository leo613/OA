package com.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.sso.query.bean.User;
import com.taotao.web.bean.Cart;

import com.taotao.web.threadLocal.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private ApiService apiService;
    @Value("${CART_TAOTAO_URL}")
    private String CART_TAOTAO_URL;

    private ObjectMapper objectMapper=new ObjectMapper();

    /**
     * 获取userId 查询商品信息
     * @return
     */
    public List<Cart> queryCarts() {
        try {
            User user = UserThreadLocal.get();
            if (user!=null){
                String cartStr = this.apiService.doGet(CART_TAOTAO_URL + "/service/api/cart/query/list/" + user.getId());
                List<Cart> carts= objectMapper.readValue(cartStr, objectMapper.getTypeFactory().constructCollectionType(List.class, Cart.class));
                 return carts;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
