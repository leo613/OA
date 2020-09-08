package com.taotao.cart.threadlocal;


import com.taotao.sso.query.bean.User;

public class UserThreadlocal {
	
	private UserThreadlocal(){}
	public static final ThreadLocal<User> local = new ThreadLocal<User>();
	/**
	 * 存储用户信息
	 * @param user
	 */
	public static void set(User user){
		local.set(user);
	}
	/**
	 * 获取用户身份呢信息
	 * @return
	 */
	public static User get(){
		return local.get();
	}
}
