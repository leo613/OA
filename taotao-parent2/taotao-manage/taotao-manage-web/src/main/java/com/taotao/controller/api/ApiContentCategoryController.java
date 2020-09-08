package com.taotao.controller.api;

import com.taotao.controller.ItemController;
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
@RequestMapping("api/content/category")
public class ApiContentCategoryController {
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

}
