package com.taotao.cart.interceptors;


import com.taotao.cart.service.UserService;
import com.taotao.cart.threadlocal.UserThreadlocal;
import com.taotao.common.service.ApiService;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.query.bean.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginHandlerInterceptor implements HandlerInterceptor {
    public static String TAOTAO_COOKIE = "taotao_cookie";

    @Autowired
    private ApiService apiService;

    @Autowired
    private UserService userServic;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request,TAOTAO_COOKIE);
          //查询是否有token(确认是否登录过)
        if (!StringUtils.isNotBlank(token)){
            return true;
        }
          //如果有token 确认是否已经超时
        User user= this.userServic.queryUserByToken(token);
        if (user==null){
            //登录超时
            return true;
        }
        //储存用户信息
        UserThreadlocal.set(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadlocal.set(null);
    }
}
