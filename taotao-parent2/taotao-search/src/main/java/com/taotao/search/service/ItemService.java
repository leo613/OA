package com.taotao.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.search.bean.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemService {
    @Autowired
    private ApiService apiService;
    ObjectMapper objectMapper=new ObjectMapper();
    public Item queryItemByItemId(long itemId) {
        try {
            String jsonString = apiService.doGet("http://manage.taotao.com/rest/api/item/" + itemId);
           return objectMapper.readValue(jsonString,Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
