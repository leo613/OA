package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.pojo.ItemDesc;
import com.taotao.web.bean.ItemVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ItemService {
    @Autowired
    private ApiService apiService;

    @Autowired
    private RedisService redisService;

    @Value("${MANAGER_TAOTAO_URL}")
    private String MANAGER_TAOTAO_URL;

    ObjectMapper objectMapper=new ObjectMapper();

    public static final String REDIS_KEY="TAOTAO_WEB_ITEM_DETAIL";  //redis缓存名
    private static final int SECOND = 60*60*24; //有效时间

    public ItemVo queryItemByItemId(Long itemId) {
        try {
            String item = redisService.get(REDIS_KEY + "_" + itemId);
            if (StringUtils.isNotBlank(item)){
                return objectMapper.readValue(item,ItemVo.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String itemstr=this.apiService.doGet(MANAGER_TAOTAO_URL+"/rest/api/item/"+itemId);
            ItemVo itemVo = objectMapper.readValue(itemstr, ItemVo.class);
            try {
                //添加至redis 缓存中
                redisService.set(REDIS_KEY+"_"+itemId,itemstr,SECOND);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return itemVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemDesc queryItemDescById(Long itemId) {
        try {
            String itemstr=this.apiService.doGet(MANAGER_TAOTAO_URL+"/rest/api/item/desc/"+itemId);
            if (StringUtils.isNotBlank(itemstr)){
                return objectMapper.readValue(itemstr,ItemDesc.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
     return null;
    }

    /**
     * 通过商品ID查询商品规格查询
     * @param itemId
     * @return
     */
    public String queryItemParamItemByItemId(Long itemId) {
        try {
            String itemstr=this.apiService.doGet(MANAGER_TAOTAO_URL+"/rest/api/item/param/item/"+itemId);
            JsonNode jsonNode = objectMapper.readTree(itemstr);
            String paramData = jsonNode.get("paramData").asText();
            ArrayNode  arr= (ArrayNode) objectMapper.readTree(paramData);
            StringBuilder str=new StringBuilder();
            str.append("<table  cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");
            for (JsonNode node : arr) {
                str.append("<tr><th class=\"tdTitle\" colspan=\"2\">"+node.get("group").asText()+"</th></tr>");
                ArrayNode params =  (ArrayNode) node.get("params");
                for (JsonNode param : params) {
                    str.append("<tr><td class=\"tdTitle\">"+param.get("k").asText()+"</td><td>"+param.get("v").asText()+"</td></tr>");
                }
            }
            str.append("</tbody></table>");
            return str.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
