package com.taotao.controller;
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
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemDescService itemDescService;

    // error - warn - info - debug  级别依次递减
    public static Logger logger = LoggerFactory.getLogger(ItemController.class);


    /**
     *分页查询列表
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value = "page",defaultValue = "0")Integer pageNum,
            @RequestParam(value = "rows",defaultValue = "0")Integer pageSize){
        try {
            EasyUIResult easyUIResult= itemService.queryPageList(pageNum,pageSize);
            logger.info("商品查询结果为：{}",easyUIResult);
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    /**
     * 添加商品信息
     * @param item
     * @param desc
     * @param paramData  商品规格参数
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc")String desc,@RequestParam("itemParams") String paramData){
        try {
            if(logger.isDebugEnabled()){
                logger.info("接受参数的信息 item={}，desc={}",item,desc); //{} 相当于占位符
            }
            // 判断item 是否为空
            if (item==null||StringUtils.isEmpty(item.getTitle())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            // 添加产品信息
            Boolean flag=itemService.saveItem(item,desc,paramData);
            // 判断添加是否成功
            if (!flag){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            logger.debug("保存商品成功,商品Id为:{},商品名称为:{}",item.getId(),item.getTitle());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("服务器出错item="+item,e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 查询商品描述
     * @param itemId
     * @return
     */

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

    /**
     * 修改商品信息
     * @param item
     * @param desc
     * @param paramData
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItme(Item item,@RequestParam("desc") String desc,@RequestParam("itemParams") String paramData){
        try {
            if(logger.isDebugEnabled()){
                logger.info("接受参数的信息 item={}，desc={}",item,desc); //{} 相当于占位符
            }
            // 判断item 是否为空
            if (item==null||StringUtils.isEmpty(item.getTitle())||item.getId()==null ){
                if(logger.isDebugEnabled()){
                    logger.info("接受参数不合法, item={}，desc={}",item,desc); //{} 相当于占位符
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            //修改商品
            Boolean flag = this.itemService.updateItem(item,desc,paramData);
            if (flag){
                if(logger.isDebugEnabled()){
                    logger.info("添加商品成功： item={}，desc={}",item,desc); //{} 相当于占位符
                }
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
        } catch (Exception e) {
            logger.error("添加商品错误 item="+item+"  desc:"+desc,e);
            e.printStackTrace();
        }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
