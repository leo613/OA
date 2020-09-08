package com.taotao.search.mq.rabbitMq;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;

import com.taotao.search.bean.Item;
import com.taotao.search.service.ItemService;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class RabbitMqHandler {
    @Autowired
    private HttpSolrServer httpSolrServer;

    @Autowired
    private ItemService itemService;

   private   ObjectMapper objectMapper=new ObjectMapper();

    /**
     * 新增，更改，删除
     * @param rabbitMqMessage
     */
    public void hand(String rabbitMqMessage){
        try {
            JsonNode jsonNode = this.objectMapper.readTree(rabbitMqMessage);
            long itemId = jsonNode.get("id").asLong();
            String type = jsonNode.get("type").textValue();
            if (type.equals("update")||type.equals("insert")){
                Item item= this.itemService.queryItemByItemId(itemId);
                 this.httpSolrServer.addBean(item);
                 this.httpSolrServer.commit();
            }else if (type.equals("delete")){
                this.httpSolrServer.deleteById(String.valueOf(itemId));
                this.httpSolrServer.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
