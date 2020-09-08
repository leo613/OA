package com.taotao.web.threadLocal;


import com.taotao.sso.query.bean.User;

public class UserThreadLocal {

    private UserThreadLocal(){}
    public static final ThreadLocal<User> threadLocal=new ThreadLocal<User>();

    /**
     * 储存用户信息
     * @param user
     */
      public static void set(User user){
          threadLocal.set(user);
      }

    /**
     *  获取用户身份信息
     */
     public static  User get(){
         return threadLocal.get();
     }
}
