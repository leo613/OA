package com.taotao.controller;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.pojo.Content;
import com.taotao.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("content")
public class ContentController {
    @Autowired
    private ContentService contentService;

    // error - warn - info - debug  级别依次递减
    public static Logger logger = LoggerFactory.getLogger(ItemController.class);

    /**
     * 新增内容
     * @param content
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveContent(Content content){
        try {
            content.setId(null);
            this.contentService.save(content);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 根据分类ID 查询内容列表,并且按照更新时间顺序排列
     */

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryContentById(@RequestParam(value = "categoryId") Long categoryId,
                                                    @RequestParam(value = "page",defaultValue = "0")Integer pageNum,
                                                    @RequestParam(value = "rows",defaultValue = "0")Integer pageSize
                                                    ){
        EasyUIResult easyUIResult= null;
        try {
            easyUIResult = this.contentService.queryContentById(categoryId,pageNum,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(easyUIResult);
    }
}
