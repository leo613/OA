package com.taotao.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cart.bean.Item;
import com.taotao.common.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemService {
    @Autowired
    private ApiService apiService;
    ObjectMapper objectMapper=new ObjectMapper();
    public Item queryItemByItemId(Long itemId) {
        try {
            String jsonString = apiService.doGet("http://manage.taotao.com/rest/api/item/" + itemId);
            return objectMapper.readValue(jsonString,Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
