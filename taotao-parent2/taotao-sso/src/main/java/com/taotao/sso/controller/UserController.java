package com.taotao.sso.controller;

import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.pojo.User;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    private static String TAOTAO_COOKIE = "taotao_cookie";



    /**
     * 跳转到注册页面
     * @return
     */
         @RequestMapping(value = "register",method = RequestMethod.GET)
        public String register(){
             return "register";
         }

    /**
     * 注册时数据监测接口（判断用户名/手机号 是否已存在）
     * @param param
     * @param type
     * @return
     */
         @RequestMapping(value = "/check/{param}/{type}",method = RequestMethod.GET)
         public ResponseEntity<Boolean> check(@PathVariable("param")String param,
                                              @PathVariable("type")int type
                                              ){
             try {
                 Boolean flag= this.userService.checkInfo(param,type);
                 if (flag==null){
                     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(flag);
                 }
                 return ResponseEntity.ok(flag);
             } catch (Exception e) {
                 e.printStackTrace();
             }
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
         }


    /**
     *  注册用户信息
     * @param user
     * @return
     */
    @RequestMapping("doRegister")
    @ResponseBody
         public Map<String,Object> doRegister(@Valid  User user, BindingResult bindingResult){
          Map<String,Object> map=new HashMap<>();
            if (bindingResult.hasErrors()){
                  List<String> strings=new ArrayList<>();
                  List<ObjectError> allErrors = bindingResult.getAllErrors();
                 if (allErrors!=null){
                     for (ObjectError allError : allErrors) {
                         String message = allError.getDefaultMessage();
                         strings.add(message);
                     }
                     map.put("status",400);
                     map.put("data","数据校验失败，错误信息是"+StringUtils.join(strings, "|"));
                    return map;
                 }
              }

        try {
            Boolean flag= this.userService.doRegister(user);
            if (flag){
                map.put("status",200);
            }else {
                map.put("status",500);
                map.put("data","辛苦你了");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status",500);
            map.put("data","辛苦你了");
        }
        return map;
    }


    /**
     * 跳转登录页面
     */
    @RequestMapping(value = "login",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    /**
     * 用户登录
     * @param user
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "doLogin",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> doLogin(User user, HttpServletRequest request, HttpServletResponse response){
         Map<String,Object> map=new HashMap<>();
        //redis   （token,用户信息）
        try {
            String  token= this.userService.doLogin(user.getUsername(),user.getPassword());
            if (token==null){
                map.put("status", 400);
                map.put("data", "登录失败，请重试");
                return map;
            }
            //需要将token保存到cookie中，以便在访问其他的资源时会将cookie中的token携带过来。
            CookieUtils.setCookie(request,response,TAOTAO_COOKIE,token);
            map.put("status", 200);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 根据token查询用户身份信息
     * @return
     */
    @RequestMapping(value = "{token}",method = RequestMethod.GET)
    public ResponseEntity<User> queryUserByToken(@PathVariable("token")String token){
//        try {
//            User user=this.userService.queryUserByToken(token);
//            if (user==null){
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//            return ResponseEntity.ok(user);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        User user=new User();
        user.setUsername("该服务已经停止调用");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
    }

}
