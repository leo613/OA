package com.taotao.web.service;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.pojo.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndexService {
    private ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    private ApiService apiService;

    @Value("${MANAGER_TAOTAO_URL}")
    private String MANAGER_TAOTAO_URL;
    @Value("${INDEX_AD1_URL}")
    private String INDEX_AD1_URL;
    @Value("${INDEX_AD2_URL}")
    private String INDEX_AD2_URL;

    /**
     * 查询大广告位的数据
     * @return
     */
    public String queryIndexAd1() {
//        try {
//         String url=MANAGER_TAOTAO_URL+INDEX_AD1_URL;
//        String jsonData = this.apiService.doGet(url);
//       if (null==jsonData){
//           return null;
//       }
//       //解析JSON数据
//            JsonNode jsonNode = objectMapper.readTree(jsonData);
//            ArrayNode rows=(ArrayNode) jsonNode.get("rows");
//            List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
//            for (JsonNode row : rows) {
//                Map<String,Object> map=new LinkedHashMap<>();
//                map.put("srcB",row.get("pic").asText());
//                map.put("height",240);
//                map.put("alt",row.get("title").asText());
//                map.put("width",670);
//                map.put("src",row.get("pic").asText());
//                map.put("widthB",550);
//                map.put("href",row.get("url").asText());
//                map.put("heightB",240);
//                result.add(map);
//            }
//
//            return objectMapper.writeValueAsString(result);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//       return null;

        //对上面代码进行优化
        try {
            String str = apiService.doGet(MANAGER_TAOTAO_URL + INDEX_AD1_URL);
            EasyUIResult result = EasyUIResult.formatToList(str, Content.class);
            List<Content> rows = (List<Content>) result.getRows();
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            if (null!=rows && rows.size()>0){
                for (Content content : rows) {
                    Map<String,Object> map = new LinkedHashMap<String, Object>();
                    map.put("srcB", content.getPic());
                    map.put("height", 240);
                    map.put("alt", content.getTitle());
                    map.put("width", 670);
                    map.put("src", content.getPic());
                    map.put("widthB", 550);
                    map.put("href", content.getUrl());
                    map.put("heightB", 240);
                    list.add(map);
                }
            }
            return objectMapper.writeValueAsString(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String queryIndexAd2() {
//        try {
//            String url=MANAGER_TAOTAO_URL+INDEX_AD2_URL;
//            String jsonData = this.apiService.doGet(url);
//            if (null==jsonData){
//                return null;
//            }
//            //解析JSON数据
//
//            JsonNode jsonNode = objectMapper.readTree(jsonData);
//            ArrayNode rows=(ArrayNode) jsonNode.get("rows");
//            List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
//            for (JsonNode row : rows) {
//                Map<String,Object> map=new LinkedHashMap<>();
//                map.put("srcB",row.get("pic").asText());
//                map.put("height",70);
//                map.put("alt",row.get("title").asText());
//                map.put("width",310);
//                map.put("src",row.get("pic").asText());
//                map.put("widthB",210);
//                map.put("href",row.get("url").asText());
//                map.put("heightB",70);
//                result.add(map);
//            }
//
//            return objectMapper.writeValueAsString(result);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;

        //对上面代码进行优化
        try {
            String str = apiService.doGet(MANAGER_TAOTAO_URL + INDEX_AD2_URL);
            EasyUIResult result = EasyUIResult.formatToList(str, Content.class);
            List<Content> rows = (List<Content>) result.getRows();
            List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
            if (null!=rows && rows.size()>0){
                for (Content content : rows) {
                    Map<String,Object> map = new LinkedHashMap<String, Object>();
                    map.put("srcB", content.getPic());
                    map.put("height", 240);
                    map.put("alt", content.getTitle());
                    map.put("width", 670);
                    map.put("src", content.getPic());
                    map.put("widthB", 550);
                    map.put("href", content.getUrl());
                    map.put("heightB", 240);
                    list.add(map);
                }
            }
            return objectMapper.writeValueAsString(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
