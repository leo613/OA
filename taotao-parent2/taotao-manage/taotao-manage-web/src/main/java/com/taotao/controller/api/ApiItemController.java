package com.taotao.controller.api;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.pojo.Item;
import com.taotao.pojo.ItemDesc;
import com.taotao.service.ItemDescService;
import com.taotao.service.ItemService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/api/item")
public class ApiItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemDescService itemDescService;

    // error - warn - info - debug  级别依次递减
    public static Logger logger = LoggerFactory.getLogger(ApiItemController.class);


    @RequestMapping(value = "/{itemId}",method = RequestMethod.GET)
    public ResponseEntity<Item> queryItemById(@PathVariable("itemId")Long itemId){
        try {
            Item item = itemService.queryById(itemId);
            if (item==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            logger.debug("查询商品成功,商品Id为:{},商品描述为:{}",itemId,item);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器出错itemDesc="+itemId,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }



    @RequestMapping(value = "/desc/{itemId}",method = RequestMethod.GET)
    public ResponseEntity<ItemDesc> queryDescById(@PathVariable("itemId")Long itemId){

        try {
            ItemDesc itemDesc = this.itemDescService.queryById(itemId);
            if (itemDesc==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            logger.debug("查询商品成功,商品Id为:{},商品描述为:{}",itemId,itemDesc);
            return ResponseEntity.ok(itemDesc);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器出错itemDesc="+itemId,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }


}
