package com.taotao.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.mapper.ContentMapper;
import com.taotao.pojo.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService extends BaseService<Content> {

    @Autowired
    private ContentMapper contentMapper;
    public EasyUIResult queryContentById(Long categoryId, Integer pageNum, Integer pageSize) {
        //设置分页参数
        PageHelper.startPage(pageNum,pageSize);

        List<Content> contents = this.contentMapper.queryContentById(categoryId);

        PageInfo<Content> pageInfo=new PageInfo<Content>(contents);

        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }
}
