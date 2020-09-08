package com.taotao.sso.query.api;

import com.taotao.sso.query.bean.User;

public interface UserQueryService {

    /**
     *  根据token查询用户信息
     * @param token
     * @return
     */
    public User queryUserByToken(String token);
}
