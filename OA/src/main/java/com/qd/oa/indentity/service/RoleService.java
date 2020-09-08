package com.qd.oa.indentity.service;

import com.qd.oa.common.util.pageTage.PageModel;
import com.qd.oa.indentity.bean.Role;
import com.qd.oa.indentity.bean.User;

import java.util.List;

public interface RoleService {
    List<Role> selectRole(Role role, PageModel pageModel);

    Boolean addRole(Role role);

    String onlyRoleName(Role role);

    Boolean deleteroleByIds(long [] roleIds);

    Role showUpdateRole(Role role);

    Boolean updateRole(Role role);

    List<User> selectRoleUser(Long roleId, PageModel pageModel);

    Role selectRoleById(Long roleId);

    List<User> findUnBindUser(Long roleId, PageModel pageModel);

    String bindUser(Long roleId, String[] userIds);

    String unBindUser(Long roleId, String[] userIds);
}
