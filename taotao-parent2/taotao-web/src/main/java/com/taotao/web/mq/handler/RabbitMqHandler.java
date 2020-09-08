package com.taotao.web.mq.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class RabbitMqHandler {
    @Autowired
    private RedisService redisService;

   private   ObjectMapper objectMapper=new ObjectMapper();

    /**
     * 删除redis中的商品缓存数据
     * @param rabbitMqMessage
     */
    public void hand(String rabbitMqMessage){
        try {
            JsonNode jsonNode = this.objectMapper.readTree(rabbitMqMessage);
            long itemId = jsonNode.get("id").asLong();
            String key= ItemService.REDIS_KEY + "_" + itemId;
            this.redisService.del(key);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
