package com.taotao.controller;

import com.taotao.pojo.ItemCat;

import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类目查询
 */
@Controller
@RequestMapping("item/cat")
public class ItemCatController {

    @Autowired
    public ItemCatService itemCatService;

    //localhost:8081/rest/item/cat
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<List<ItemCat>> queryItemCatList(@RequestParam(value="id",defaultValue="0",required=false)Long id){
        try {
            ItemCat record=new ItemCat();
             record.setParentId(id);
            List<ItemCat> itemCats = itemCatService.queryListByWhere(record);

            if (itemCats.size()==0||itemCats==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            ResponseEntity<List<ItemCat>> body = ResponseEntity.status(HttpStatus.OK).body(itemCats);
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
