package com.taotao.web.controller;

import com.taotao.web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping
public class IndexController {

    @Autowired
    private IndexService indexService;

    @RequestMapping(value = "index",method = RequestMethod.GET)
    public ModelAndView Index(){
        ModelAndView mv=new ModelAndView("index");
        //大广告位查询
        String indexAd1=this.indexService.queryIndexAd1();

        //右上角小广告
        String indexAd2=this.indexService.queryIndexAd2();
        mv.addObject("indexAd1",indexAd1);
        mv.addObject("indexAd2",indexAd2);
        return mv;
    }
}
