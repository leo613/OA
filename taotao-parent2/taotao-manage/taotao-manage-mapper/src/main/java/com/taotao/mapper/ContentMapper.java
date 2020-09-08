package com.taotao.mapper;

import com.github.abel533.mapper.Mapper;
import com.taotao.pojo.Content;

import java.util.List;

public interface ContentMapper extends Mapper<Content> {
    List<Content> queryContentById(Long categoryId);
}
