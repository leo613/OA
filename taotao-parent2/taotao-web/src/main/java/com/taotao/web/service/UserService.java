package com.taotao.web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.sso.query.api.UserQueryService;
import com.taotao.sso.query.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private ApiService apiService;

    @Value("${SSO_TAOTAO_URL}")
    private String SSO_TAOTAO_URL;

    @Autowired
    private UserQueryService userQueryService;

    ObjectMapper objectMapper=new ObjectMapper();

    public User queryUserByToken(String token) {

       User user= this.userQueryService.queryUserByToken(token);
       return user;
//
//        try {
//            String str = this.apiService.doGet(SSO_TAOTAO_URL+"/service/user/"+token);
//            if (StringUtils.isNotBlank(str)){
//                User user= objectMapper.readValue(str,User.class);
//                if (user!=null){
//                    return user;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }
}
