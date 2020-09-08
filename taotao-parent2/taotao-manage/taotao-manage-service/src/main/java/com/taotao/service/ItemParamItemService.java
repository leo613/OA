package com.taotao.service;

import com.github.abel533.entity.Example;
import com.taotao.mapper.ItemParamItemMapper;
import com.taotao.pojo.ItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ItemParamItemService  extends BaseService<ItemParamItem>{
    @Autowired
    private ItemParamItemMapper itemParamItemMapper;

    public Integer updateItemParamItem(Long id, String paramData) {
        //更新数据
        ItemParamItem itemParamItem=new ItemParamItem();
        itemParamItem.setParamData(paramData);
        itemParamItem.setUpdated(new Date());

        Example example=new Example(ItemParamItem.class);
        example.createCriteria().andEqualTo("itemId",id);

        return this.itemParamItemMapper.updateByExampleSelective(itemParamItem,example);
    }
}
