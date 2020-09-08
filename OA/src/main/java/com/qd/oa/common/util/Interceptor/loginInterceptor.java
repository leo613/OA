package com.qd.oa.common.util.Interceptor;

import com.qd.oa.common.constant.ContstanUtils;
import com.qd.oa.indentity.bean.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * todo 登录拦截器
 * todo 拦截未登录就访问该网页
 */
public class loginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user =(User) ContstanUtils.getsession().getAttribute(ContstanUtils.SESSION_USER);
        if (user!=null){
            return  true;
        }else{
            HttpSession session = ContstanUtils.setSeeesion();
            session.setAttribute("message","您尚未登录,请登录后再进行相关操作!");
            //todo 跳转至登录页面
            response.sendRedirect(request.getContextPath()+"/login/login.jspx");
            return  false;
        }

    }
}
