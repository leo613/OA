package com.taotao.controller;

import com.taotao.pojo.ContentCategory;
import com.taotao.service.ContentCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
    @Autowired
   private ContentCategoryService contentCategoryService;

    // error - warn - info - debug  级别依次递减
    public static Logger logger = LoggerFactory.getLogger(ItemController.class);

    /**
     * 根据父节点id查询内容分类列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ContentCategory>> queryContentCategoryList(@RequestParam(value = "id",defaultValue = "0") Long pid){
        try {
            ContentCategory contentCategory=new ContentCategory();
            contentCategory.setParentId(pid);

            List<ContentCategory> categoryList = this.contentCategoryService.queryListByWhere(contentCategory);
            logger.info("根据父节点id查询内容分类列表：{}",categoryList);

            if (categoryList==null || categoryList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(categoryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    /**
     * 新增分类管理(子节点)
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory ){
        if(logger.isDebugEnabled()){
            logger.info("接受参数的信息 ContentCategory={}",contentCategory); //{} 相当于占位符
        }
        try {
            Boolean flag=  this.contentCategoryService.saveContentCategory(contentCategory);
            if (flag){
                return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 更新分类管理(重命名)
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateContentCategory(ContentCategory contentCategory){
        if(logger.isDebugEnabled()){
            logger.info("接受参数的信息 ContentCategory={}",contentCategory); //{} 相当于占位符
        }
        try {
            this.contentCategoryService.updateSelective(contentCategory);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    }

    /**
     * 删除分类管理
     */
     @RequestMapping(method = RequestMethod.DELETE)
     public ResponseEntity<Void> deleteContentCategory(ContentCategory contentCategory){
         /**
          * 第一种情况
          *  删除子节点 并更改父节点isparent为false
          *
          *  第二中情况
          *  删除中间节点 子节点删除 更改父节点isParent 为false
          */
         try {
             Boolean flag= this.contentCategoryService.deleteContentCategory(contentCategory);
             if (flag){
                 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
     }
}
