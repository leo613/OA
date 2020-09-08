package com.taotao.cart.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Item;
import com.taotao.cart.pojo.Cart;
import com.taotao.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class CartCookieService {

    private static String TAOTAO_CART_KEY = "taotao_cart";

    private  int COOKIE_MAX_AGE = 60*60*24*7;

    private ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    private ItemService itemService;
    /**
     *  向cookie中添加商品数据,如果当前的商品存在则把数量加1如果不存在则创建商品，存到cookie中
     * @param itemId
     * @param request
     * @param response
     */
    public void addItemToCart(Long itemId, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        List<Cart> list=queryCartCookieList(request);
        Cart cardDesc=null;
    if (list!=null&&list.size()>0){
        for (Cart cart : list) {
            if(itemId.equals(cart.getItemId())){
                cardDesc=cart;
                break;
            }
        }
      }
      //判断当前购物车是否有商品
       if (cardDesc!=null){
         cardDesc.setNum(cardDesc.getNum()+1);
         cardDesc.setUpdated(new Date());
       }else {
        //购车内没有任何商品
        Cart cart=new Cart();
           cart.setCreated(new Date());
           cart.setItemId(itemId);
           cart.setNum(1);
           cart.setUpdated(cart.getCreated());

           //标题，图片，价格
           Item item = this.itemService.queryItemByItemId(itemId);
           cart.setItemImage(StringUtils.split(item.getImage(),",")[0]);
           cart.setItemPrice(item.getPrice());
           cart.setItemTitle(item.getTitle());
           list.add(cart);

       }
        //保存至Cookie当中
      CookieUtils.setCookie(request,response,TAOTAO_CART_KEY,objectMapper.writeValueAsString(list),COOKIE_MAX_AGE,true);
    }

    // 查询购物车信息 获取Cookie值
    public List<Cart> queryCartCookieList(HttpServletRequest request) {
        List<Cart> cartList = null;
        String cookieValue = CookieUtils.getCookieValue(request, TAOTAO_CART_KEY, true);
        if (StringUtils.isBlank(cookieValue)){
            cartList=new ArrayList<>();
        }else{
            try {
                cartList=objectMapper.readValue(cookieValue,objectMapper.getTypeFactory().constructCollectionType(List.class,Cart.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cartList;
    }

    public void updateCartNum(Integer num, Long itemId, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        List<Cart> list = queryCartCookieList(request);
        if (list!=null&&list.size()>0){
            for (Cart cart : list) {
                if (cart.getItemId().equals(itemId)){
                    cart.setNum(num);
                    cart.setUpdated(new Date());
                    break;
                }
            }

        }
        //保存至Cookie当中
        CookieUtils.setCookie(request,response,TAOTAO_CART_KEY,objectMapper.writeValueAsString(list),COOKIE_MAX_AGE,true);
    }

    public void deleteCartByItemId(Long itemId, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        List<Cart> list = queryCartCookieList(request);
        if (list!=null&&list.size()>0){
            Iterator<Cart> iterator = list.iterator();
         while (iterator.hasNext()){
             Cart cart = iterator.next();
             if (cart.getItemId().equals(itemId)){
                 iterator.remove();
                 break;
             }
          }
        }
        //保存至Cookie当中
        CookieUtils.setCookie(request,response,TAOTAO_CART_KEY,objectMapper.writeValueAsString(list),COOKIE_MAX_AGE,true);
    }
}
