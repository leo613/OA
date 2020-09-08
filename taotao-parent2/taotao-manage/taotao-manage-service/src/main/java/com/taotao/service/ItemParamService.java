package com.taotao.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.mapper.ItemParamMapper;
import com.taotao.pojo.ItemParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemParamService extends BaseService<ItemParam> {
    @Autowired
    private ItemParamMapper itemParamMapper;

    /**
     * 新增规模参数模板
     */
    public Boolean saveItemParam(Long itemCatId, String paramData) {
        ItemParam itemParam=new ItemParam();
        itemParam.setItemCatId(itemCatId);
        itemParam.setParamData(paramData);
        itemParam.setId(null);
        int i = saveSelective(itemParam);
        return i==1;
    }

    public EasyUIResult queryPageList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Example example=new Example(ItemParam.class);
        example.setOrderByClause("updated desc");
        List<ItemParam> itemParams = this.itemParamMapper.selectByExample(example);
        PageInfo<ItemParam> pageInfo=new PageInfo<>(itemParams);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }
}
