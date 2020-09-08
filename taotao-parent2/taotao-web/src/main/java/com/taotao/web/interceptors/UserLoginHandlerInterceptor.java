package com.taotao.web.interceptors;

import com.taotao.common.service.ApiService;
import com.taotao.common.utils.CookieUtils;
import com.taotao.sso.query.bean.User;
import com.taotao.web.service.UserService;
import com.taotao.web.threadLocal.UserThreadLocal;
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
   private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = CookieUtils.getCookieValue(request,TAOTAO_COOKIE);
        if (token==null){
            //跳转到登录页面
            response.sendRedirect("http://sso.taotao.com/user/login.html");
            return false;
        }
        //通过service实现跨域请求user信息
        User user = this.userService.queryUserByToken(token);
        if (user!=null){
            UserThreadLocal.set(user);
            return true;
        }
        //跳转到登录页面
        response.sendRedirect("http://sso.taotao.com/user/login.html");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
         UserThreadLocal.set(null);
    }
}
