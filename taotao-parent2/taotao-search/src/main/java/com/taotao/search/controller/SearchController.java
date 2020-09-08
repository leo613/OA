package com.taotao.search.controller;

import com.taotao.search.bean.Item;
import com.taotao.search.bean.SearchResult;
import com.taotao.search.service.SearchService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;

@Controller
public class SearchController {

@Autowired
private SearchService searchService;
    private static final Integer pageSize = 32;


    @RequestMapping(value = "/search")
    public ModelAndView search(@RequestParam("q")String keyWord,@RequestParam(value="page",defaultValue="1")Integer page){
         ModelAndView mv=new ModelAndView("search");
        try {
            keyWord = new String(keyWord.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            keyWord="";
        }
        mv.addObject("query",keyWord);
        mv.addObject("page",page);
        SearchResult result = null;
        try {
            result= this.searchService.queryItemBykeyWord(keyWord,page,pageSize);
        } catch (SolrServerException e) {
            e.printStackTrace();
            result = new SearchResult();
            mv.addObject("itemList", null);
            //根据数据的总条数除以每页的分页大小获取总页面
            mv.addObject("pages",0);
            return mv;
        }
        mv.addObject("itemList", result.getRows());
        //根据数据的总条数除以每页的分页大小获取总页面
        mv.addObject("pages",result.getTotal()%pageSize==0?result.getTotal()/pageSize:result.getTotal()/pageSize+1);
        return mv;
    }
}
