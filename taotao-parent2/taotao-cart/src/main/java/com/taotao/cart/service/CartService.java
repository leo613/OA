package com.taotao.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.taotao.cart.bean.Item;
import com.taotao.cart.mapper.CartMapper;
import com.taotao.cart.pojo.Cart;
import com.taotao.cart.threadlocal.UserThreadlocal;
import com.taotao.common.service.ApiService;
import com.taotao.sso.query.bean.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private ApiService apiService;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ItemService itemService;


    @Value("${MANAGER_TAOTAO_URL}")
    private String MANAGER_TAOTAO_URL;
    private ObjectMapper objectMapper=new ObjectMapper();

    /**
     * 根据商品Id 与 用户名添加至购物车
     * @param itemId
     */
    public void addItemToCart(Long itemId) {
       if (!itemId.equals("")){
           try {
               User user = UserThreadlocal.get();
               Cart record=new Cart();
               record.setItemId(itemId);
               record.setUserId(user.getId());
               //todo 通过itemId、UserId 查询Cart 在数据库是否有数据
               Cart cart = this.cartMapper.selectOne(record);

                // todo 判断如果有数据 商品数量加1,否则添加数据
               if (cart==null){
                   cart=new Cart();
                   cart.setItemId(itemId);
                   cart.setUserId(user.getId());
                   cart.setNum(1);
                   cart.setUpdated(cart.getUpdated());
                   cart.setCreated(new Date());
                   //标题，图片，价格
                   Item item=this.itemService.queryItemByItemId(itemId);
                   String[] img = StringUtils.split(item.getImage(), ",");
                   cart.setItemImage(img[0]);//获取主要的图片即可
                   cart.setItemPrice(item.getPrice());
                   cart.setItemTitle(item.getTitle());
                    //添加至数据库
                   this.cartMapper.insert(cart);
               }else{
                   cart.setNum(cart.getNum()+1);
                   cart.setUpdated(new Date());
                   //更新数据
                   this.cartMapper.updateByPrimaryKey(cart);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    }


    /**
     * 根据用户名 查询购物车信息
     * @return
     */
    public List<Cart> queryCartList(Long userId) {
        Example example=new Example(Cart.class);
        example.setOrderByClause("created desc");
        example.createCriteria().andEqualTo("userId",userId);
        List<Cart> list = this.cartMapper.selectByExample(example);
        return list;
    }

    /**
     * 更改购物车中的商品数量
     * @param itemId
     * @param num
     * @param userId
     */
    public void updateCartNum(Long itemId, Integer num, Long userId) {
        User user = UserThreadlocal.get();
        Cart cart = new Cart();
        cart.setNum(num);
        cart.setUpdated(new Date());
        Example example = new Example(Cart.class);
        example.createCriteria().andEqualTo("userId", user.getId()).andEqualTo("itemId", itemId);
        cartMapper.updateByExampleSelective(cart, example);

    }

    /**
     * 删除购物车中商品信息
     * @param userId
     * @param itemId
     */
    public void deleteCartByItemId(Long itemId,Long userId) {
        Cart cart=new Cart();
        cart.setUserId(userId);
        cart.setItemId(itemId);
        this.cartMapper.delete(cart);
    }
}
