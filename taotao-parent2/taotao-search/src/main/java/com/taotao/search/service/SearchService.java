package com.taotao.search.service;

import com.taotao.search.bean.Item;
import com.taotao.search.bean.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private HttpSolrServer httpSolrServer;


    public SearchResult queryItemBykeyWord(String keyWord, Integer page, Integer pageSize) throws SolrServerException {
        SolrQuery solrQuery=new SolrQuery();//构造搜索条件
        solrQuery.setQuery("title:"+keyWord+ " AND  status:1"); //搜索关键词
        // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了
        solrQuery.setStart((Math.max(page,1)-1)*pageSize);
        solrQuery.setRows(pageSize);
        //非空开启高亮
        boolean isHighlighting = !StringUtils.equals("*", keyWord) && StringUtils.isNotBlank(keyWord);
        if (isHighlighting){
            //设置高亮
            solrQuery.setHighlight(true);//开启高亮组件
            solrQuery.addHighlightField("title");//设置高亮字段
            solrQuery.setHighlightSimplePre("<em>");//标记,高亮关键字前缀
            solrQuery.setHighlightSimplePost("</em>"); //后缀
        }
        //执行查询
        QueryResponse queryResponse = this.httpSolrServer.query(solrQuery);

        long total = queryResponse.getResults().getNumFound();//总记录数

        List<Item> items = queryResponse.getBeans(Item.class);
        // 将高亮的标题数据写回到数据对象中
        if (isHighlighting){
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            for (Map.Entry<String, Map<String, List<String>>> stringMapEntry : highlighting.entrySet()) {
                for (Item item : items) {
                    if (!stringMapEntry.getKey().equals(item.getId().toString())) {  //判断是否需要高亮
                        continue;
                }
                item.setTitle(StringUtils.join(stringMapEntry.getValue().get("title"),"")); //替换
                    break;
            }

        }

    }
        return new SearchResult(total,items);
   }
}