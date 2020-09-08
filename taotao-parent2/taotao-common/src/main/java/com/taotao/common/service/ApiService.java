package com.taotao.common.service;


import com.taotao.common.httpclient.HttpResult;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;


/**
 * 封装HTTPClient Geg和Post请求
 */
@Service
public class ApiService implements BeanFactoryAware {
      @Autowired(required = false)
      private RequestConfig requestConfig;

      private BeanFactory beanFactory;

    /**
     * 支持多并发 (@Autowired 方法无法支持)
     * @return
     */
    public CloseableHttpClient getHttpClient() {
        return beanFactory.getBean(CloseableHttpClient.class);
    }

    @Override
    public void setBeanFactory(BeanFactory arg0) throws BeansException {
            beanFactory=arg0;
    }

    /**
     *无参的get请求
     */
    public String doGet(String url) throws IOException {
        // 创建http GET请求
        HttpGet httpGet=new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response=null;
        try {
            // 执行请求
            response = getHttpClient().execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                return content;
            }
            return null;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     *带有参数的GET请求，返回:null,请求失败，String数据，请求成功  url进行拼接
     */
    public String doGet(String url, Map<String,String> params) throws URISyntaxException, IOException {
        URIBuilder uriBuilder=new URIBuilder(url);
        if (params!=null){
            for (Map.Entry<String, String> en : params.entrySet()) {
                uriBuilder.setParameter(en.getKey(),en.getValue());
            }
        }
        URI uri=uriBuilder.build();
        return doGet(uri.toString());
    }

    /**
     * 带有参数的post请求
     */
    public HttpResult doPost(String url, Map<String,String> params) throws IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        if (params!=null){
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(0);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
            // 构造一个form表单式的实体
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
        }
        CloseableHttpResponse response=null;
        try {
            response= getHttpClient().execute(httpPost);
            // response.getStatusLine().getStatusCode() 判断返回状态是否为200
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), "UTF-8"));
        } finally {
            if (response!=null){
                response.close();
            }
        }

    }


    /**
     * 带有参数的post请求 json
     */
    public HttpResult doPostJson(String url, String josnStr) throws IOException {

        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
            // 构造一个form表单式的实体
            StringEntity formEntity = new StringEntity(josnStr, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(formEntity);
          CloseableHttpResponse response=null;
        try {
            response= getHttpClient().execute(httpPost);
            // response.getStatusLine().getStatusCode() 判断返回状态是否为200
            return new HttpResult(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity(), "UTF-8"));
        } finally {
            if (response!=null){
                response.close();
            }
        }

    }

    /**
     * 无参的post请求
     */
    public  HttpResult doPost(String url) throws IOException {

      return doPost(url,null);
    }


}
