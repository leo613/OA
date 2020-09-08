package com.taotao.sso.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.sso.mapper.UserMapper;
import com.taotao.sso.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisService redisService;
     ObjectMapper objectMapper=new ObjectMapper();

    /**
     * 校验数据
     * @param param 要校验的数据
     * @param type 数据类型
     * @return
     */
    public Boolean checkInfo(String param, int type) {
        User user=new User();
             switch (type){
                 case 1:
                     user.setUsername(param);
                     break;
                 case 2:
                     user.setPhone(param);
                     break;
                 case 3:
                     user.setEmail(param);
                 break;
                 default:
                     return null;
             }
              return userMapper.selectOne(user)==null;
    }

    /**
     *
     * @param user
     * @return
     */
    public Boolean doRegister(User user) {
        int insert =0;
        if (user!=null){
            user.setCreated(new Date());
            user.setUpdated(user.getCreated());
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            insert = this.userMapper.insert(user);
        }
        return insert==1;
    }

    /**
     * 用户登录并返回登录信息
     * @param username
     * @param password
     * @return
     */
    public String  doLogin(String username, String password) throws JsonProcessingException {
        User record=new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
         if (user==null){
             return null;
         }
        boolean b = user.getPassword().endsWith(DigestUtils.md5Hex(password));
         if (!b){
             return null;
         }

        String   token = "TAOTAO_"+DigestUtils.md5Hex(username+System.currentTimeMillis());
        String str = objectMapper.writeValueAsString(user);
        redisService.set(token,str,60*30);
        return token;
    }

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    public User queryUserByToken(String token) {
         if (token==null){
             return null;
         }
        String str = redisService.get(token);
         if (str==null){
             //表示用户不存在
             return null;
         }
         // 把用户信息重新再延迟30分钟,(相对时间)
         redisService.expire(token,60*30);
        try {
            User user = objectMapper.readValue(str, User.class);
            return user;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
