package com.qd.oa.indentity.service;

import com.qd.oa.common.util.pageTage.PageModel;
import com.qd.oa.indentity.bean.User;

import java.util.List;

public interface UserService {
    String userLogin(String userId, String passWord, String vcode) throws Exception;

    Boolean updateSelf(User user);

    List<User> selectUser(User user, PageModel pageModel);

    String ajaxLoadDeptAndJob();

    String onlyUserId(User userId, PageModel pageModel);

    Boolean addUser(User user);

    Boolean deleteUserByIds(String[] userIds);

    String AccountChange(User user);

    User selectUserById(User user);

    Boolean updateUserSave(User user);
}
