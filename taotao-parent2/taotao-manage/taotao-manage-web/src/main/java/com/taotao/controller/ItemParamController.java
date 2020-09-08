package com.taotao.controller;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.pojo.ItemParam;
import com.taotao.service.ItemParamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("item")
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    // error - warn - info - debug  级别依次递减
    public static Logger logger = LoggerFactory.getLogger(ItemController.class);


    /**
     *分页查询列表
     * @return
     */
    @RequestMapping(value = "/param/list",method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value = "page",defaultValue = "0")Integer pageNum,
            @RequestParam(value = "rows",defaultValue = "0")Integer pageSize){
        try {
            EasyUIResult easyUIResult= this.itemParamService.queryPageList(pageNum,pageSize);
            logger.info("商品规格结果为：{}",easyUIResult);
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }



    /**
     * 根据商品ID 查询规格
     * @param itemCatId
     * @return
     */
    @RequestMapping(value = "/param/{itemCatId}",method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryItemParamByItmeCatId(@PathVariable("itemCatId") long itemCatId){
        try {
            ItemParam itemParam=new ItemParam();
            itemParam.setItemCatId(itemCatId);
            ItemParam oneItemParam = itemParamService.queryOne(itemParam);
            if (oneItemParam==null){
                if(logger.isDebugEnabled()){
                    logger.info("此商品未查到相关规格:{}",itemCatId); //{} 相当于占位符
                }
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(oneItemParam);
        } catch (Exception e) {
            e.printStackTrace();
            if(logger.isDebugEnabled()){
                logger.info("商品规格查询错误:"+e); //{} 相当于占位符
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 新增规格参数模板
     */
      @RequestMapping(value = "/param/{itemCatId}",method = RequestMethod.POST)
      public ResponseEntity<Void> saveItemParam(@PathVariable("itemCatId")Long itemCatId,
                                                @RequestParam("paramData") String paramData
                                               ){
          try {
              Boolean flag = this.itemParamService.saveItemParam(itemCatId,paramData);
              if (!flag){
                  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
              }
              logger.debug("新增规格模板成功：{}",itemCatId);
              return ResponseEntity.status(HttpStatus.CREATED).build();
          } catch (Exception e) {
              e.printStackTrace();
              logger.error("新增规格模板出错：{}",itemCatId,e);
          }
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
}
