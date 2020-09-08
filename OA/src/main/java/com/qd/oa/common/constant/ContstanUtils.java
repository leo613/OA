package com.qd.oa.common.constant;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class ContstanUtils {
    public static final String SESSION_USER="session_user";
    public static HttpSession getsession(){
        //获取session
        HttpSession session= ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
      return session;
    }

    public static HttpSession setSeeesion() {
        //设置session
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = requestAttributes.getRequest().getSession();
        return session;
    }

    public  static void sessionRemove (String message){
        getsession().removeAttribute(message);
    }


}
