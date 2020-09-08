package com.taotao.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatData;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.common.service.RedisService;
import com.taotao.pojo.ItemCat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ItemCatService extends BaseService<ItemCat> {
//    @Autowired
//    public ItemCatMapper itemCatMapper;
//
//    /**
//     * 查询商品类目信息W
//     * @param parentId
//     * @return
//     */
//
//    public List<ItemCat> queryItemCatList(Long parentId) {
//        ItemCat itemCat=new ItemCat();
//        itemCat.setParentId(parentId);
//        return itemCatMapper.select(itemCat);
//    }
     @Autowired
     private RedisService redisService;

     private static final String ITEM_CAT_KEY="TAOTAO_MANAGE_ITEM_CAT_ALL";// key 命名规则： 项目名_模块名_方法名（业务名称）
     private static final int ITEM_CAT_KEY_TIME=60 * 60 * 24 * 30 * 3; //连接超时时间
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 全部查询，并且生成树状结构
     *
     * @return
     */
    public ItemCatResult queryAllToTree() {
        // 直接从缓存中取数据
        try {
            String itemCatResultStr = redisService.get(ITEM_CAT_KEY);
            if (StringUtils.isNotBlank(itemCatResultStr)){

                    return mapper.readValue(itemCatResultStr,ItemCatResult.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ItemCatResult result = new ItemCatResult();
        // 全部查出，并且在内存中生成树形结构
        List<ItemCat> itemCats = super.queryAll();

        //创建map集合
        Map<Long, List<ItemCat>> itemCatMap = new HashMap<Long, List<ItemCat>>();

        // 转为map存储，key为父节点ID，value为数据集合
        for (ItemCat itemCat : itemCats) {
            // 父节点Id
            Long parentId = itemCat.getParentId();
             //判断itemCatMap有没有相同父节点,若没有则添加否则跳过直接添加value数据集合
            if (!itemCatMap.containsKey(parentId)){
                //添加父节点key
                itemCatMap.put(itemCat.getParentId(),new ArrayList<ItemCat>());
            }
             //添加value 数据集合
             itemCatMap.get(parentId).add(itemCat);
        }
        // 封装一级对象
        List<ItemCat> itemCatList1 = itemCatMap.get(0L);
        for (ItemCat itemCat : itemCatList1) {
            ItemCatData itemCatData=new ItemCatData();
            itemCatData.setUrl("/products/" + itemCat.getId() + ".html");
            itemCatData.setName("<a href='" + itemCatData.getUrl() + "'>" + itemCat.getName() + "</a>");

            result.getItemCats().add(itemCatData);
            //判断是否为父节点
            if (!itemCat.getIsParent()){
                continue;
            }
            // 封装二级对象
            Long  secondId = itemCat.getId();
            List<ItemCat> itemCatList2 = itemCatMap.get(secondId);
            List<ItemCatData> itemCatDataList2=new ArrayList<ItemCatData>();
            itemCatData.setItems(itemCatDataList2);

            for (ItemCat itemCat2 : itemCatList2) {
                //封装成ItemCatData 对象
                ItemCatData itemCatData2=new ItemCatData();

                itemCatData2.setUrl("/products/"+itemCat2.getId()+".html");
                itemCatData2.setName("<a href='"+itemCatData2.getUrl()+"'>"+itemCat2.getName()+"</a>");
                itemCatDataList2.add(itemCatData2);
                 
                  //判断二级对象是否有三级对象
                if (itemCat2.getIsParent()){
                    // 封装三级对象
                    Long thirdId = itemCat2.getId();
                    List<ItemCat> itemCatList3 = itemCatMap.get(thirdId);

                     List<String> itemCatDataList3=new ArrayList<>();
                     itemCatData2.setItems(itemCatDataList3);

                    for (ItemCat itemCat3 : itemCatList3) {
                        itemCatDataList3.add("/products/" + itemCat3.getId() + ".html|" + itemCat3.getName());
                    }
                }
            }
            if (result.getItemCats().size()>=14){
                break;
            }

        }
        // 将result存储到redis中
        try {
            redisService.set(ITEM_CAT_KEY,mapper.writeValueAsString(result),ITEM_CAT_KEY_TIME);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }



}
