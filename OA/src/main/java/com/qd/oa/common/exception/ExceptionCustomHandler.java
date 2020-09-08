package com.qd.oa.common.exception;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExceptionCustomHandler implements HandlerExceptionResolver {
    String erroMessage="程序出错";
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
           ModelAndView modelAndView=new ModelAndView();
           if (ex!=null){
               if (ex instanceof ExceptionCustom){
                   ExceptionCustom exceptionCustom=(ExceptionCustom)ex;
                   erroMessage=exceptionCustom.getMessage();
               }else if (ex instanceof AuthorizationException){
                   AuthorizationException authorizationException = (AuthorizationException) ex;
                   erroMessage=authorizationException.getMessage();
               }else {
                   ex.printStackTrace();
               }
           }
           modelAndView.addObject("message",erroMessage);
           modelAndView.setViewName("common/error");
        return modelAndView;
    }
}
