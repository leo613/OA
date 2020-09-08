package com.taotao.controller;

import com.taotao.pojo.ItemParamItem;
import com.taotao.service.ItemParamItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 最终数据表
 */

@Controller
@RequestMapping("item")
public class ItemParamItemController {
    @Autowired
    private ItemParamItemService itemParamItemService;

    // error - warn - info - debug  级别依次递减
    public static Logger logger = LoggerFactory.getLogger(ItemController.class);

    /**
     * 根据产品id 查询商品规格参数数据
     * @param itemId
     * @return
     */
    @RequestMapping(value = "/param/item/{itemId}",method = RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryByItemId(@PathVariable("itemId") Long itemId){
        try {
            ItemParamItem itemParamItem=new ItemParamItem();
            itemParamItem.setItemId(itemId);
            ItemParamItem itemParamItemOne = itemParamItemService.queryOne(itemParamItem);
            if (itemParamItemOne==null){
                logger.debug("查询商品失败,商品Id为: {}",itemId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(itemParamItemOne);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

}
