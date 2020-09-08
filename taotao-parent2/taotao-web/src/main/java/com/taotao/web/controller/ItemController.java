package com.taotao.web.controller;


import com.taotao.pojo.ItemDesc;
import com.taotao.web.bean.ItemVo;
import com.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 商品基本数据显示
 */
@Controller
@RequestMapping("item")
public class ItemController {

      @Autowired
      private ItemService itemService;

     @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
      public ModelAndView queryItemById(@PathVariable("itemId")Long itemId){
         ModelAndView mv=new ModelAndView("item");
         ItemVo itemVo= this.itemService.queryItemByItemId(itemId);
         mv.addObject("item", itemVo);

         //查询商品的描述信息
         ItemDesc itemDesc=this.itemService.queryItemDescById(itemId);
         mv.addObject("itemDesc", itemDesc);

         //商品Id查询商品规格
         String itemParam =  this.itemService.queryItemParamItemByItemId(itemId);
         mv.addObject("itemParam", itemParam);
         return mv;
     }
}
