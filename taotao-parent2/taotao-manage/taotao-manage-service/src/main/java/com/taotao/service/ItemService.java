package com.taotao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.mapper.ItemMapper;
import com.taotao.pojo.Item;
import com.taotao.pojo.ItemCat;
import com.taotao.pojo.ItemDesc;

import com.taotao.pojo.ItemParamItem;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService extends  BaseService<Item> {

    @Autowired
    private ApiService apiService;

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private ItemMapper itemMapper;

    @Value("${TAOTAO_WEB_URL}")
    private String TAOTAO_WEB_URL;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private ObjectMapper objectMapper=new ObjectMapper();

    /***
     * 保存商品信息
     * @param item
     * @param desc
     * @param paramData
     * @return
     */
    public Boolean saveItem(Item item, String desc,String paramData) {
        //添加商品信息
        item.setStatus(1);
        item.setId(null);
        int itemCount = this.save(item);
        //添加商品描述
        ItemDesc itemDesc=new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        int itemDescCount = itemDescService.save(itemDesc);
        //添加商品规格参数
        ItemParamItem itemParamItem=new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(paramData);
        int itemParamItemCount = itemParamItemService.save(itemParamItem);
        rabbitMq(item.getId(),"update");
        return itemCount==1 && itemDescCount==1 && itemParamItemCount==1;
    }

    /**
     * 根据分页的页码和分页大小查询分页数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    public  EasyUIResult queryPageList(Integer pageNum, Integer pageSize){
        //设置分页参数
        PageHelper.startPage(pageNum,pageSize);
        Example example=new Example(Item.class);

        //根据updated 修改时间进行排序
        example.setOrderByClause("updated desc");

        List<Item> items = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo=new PageInfo<Item>(items);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }

    /**
     * 修改商品信息
     * @param item
     * @param desc
     * @return
     */
    public Boolean updateItem(Item item, String desc,String paramData) {
        //修改商品信息
        item.setStatus(null);
        item.setCreated(null);
        int itemCount = this.updateSelective(item);
        //修改商品描述
        ItemDesc itemDesc=new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        int itemDescCount = itemDescService.updateSelective(itemDesc);
        //修改商品规格
        int itemParamItemCount = itemParamItemService.updateItemParamItem(item.getId(),paramData);

          //通知其他系统,该商品已经更新
//        try {
//            apiService.doPost(TAOTAO_WEB_URL+"item/cache/"+item.getId()+".html");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
           rabbitMq(item.getId(),"update");
        return itemCount==1 || itemDescCount==1 || itemParamItemCount==1;
    }


    public  void rabbitMq(Long id,String type){

        try {
            Map<String,Object> hashMap=new HashMap<>();
            hashMap.put("id",id);
            hashMap.put("type",type);
            hashMap.put("time",System.currentTimeMillis());
            this.rabbitTemplate.convertAndSend("item."+type,objectMapper.writeValueAsString(hashMap));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
