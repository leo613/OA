package com.taotao.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.taotao.common.service.ApiService;
import com.taotao.sso.query.api.UserQueryService;

import com.taotao.sso.query.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {
    @Autowired
    private ApiService apiService;
    @Value("${TAOTAO_SSO_URL}")
    private String TAOTAO_SSO_URL;
    private   ObjectMapper objectMapper=new ObjectMapper();


    @Autowired
    private UserQueryService userQueryService;

    public User queryUserByToken(String token) {
         User user= this.userQueryService.queryUserByToken(token);
        return user;

//        try {
//            String userStr = this.apiService.doGet(TAOTAO_SSO_URL + "service/user/" + token);
//            if (StringUtils.isNotBlank(userStr)){
//                User user = objectMapper.readValue(userStr, User.class);
//                return user;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }
}
