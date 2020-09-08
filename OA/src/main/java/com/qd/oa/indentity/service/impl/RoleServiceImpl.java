package com.qd.oa.indentity.service.impl;

import com.qd.oa.common.constant.ContstanUtils;
import com.qd.oa.common.util.pageTage.PageModel;
import com.qd.oa.indentity.bean.Role;
import com.qd.oa.indentity.bean.User;
import com.qd.oa.indentity.dao.RoleDao;
import com.qd.oa.indentity.dao.UserDao;
import com.qd.oa.indentity.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service(value = "roleService")
@Transactional
public class RoleServiceImpl implements RoleService {
    @Resource(name = "roleDao")
    private RoleDao roleDao;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Override
    public List<Role> selectRole(Role role, PageModel pageModel) {
        try {
                // 定义StringBuffer 用于封装SQL语句
                StringBuffer hql=new StringBuffer();
                // Hql 语句
                 hql.append("select r from Role r where delFlag='1' ");
                return this.roleDao.findByPage(hql.toString(),pageModel,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Boolean addRole(Role role) {
        HttpSession session = ContstanUtils.getsession();
        try {
            if (role!=null&&!role.equals("")) {
                //创建时间
                role.setCreateDate(new Date());
                //创建人
                role.setCreater((User) session.getAttribute(ContstanUtils.SESSION_USER));
                this.roleDao.save(role);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return null;
    }

    @Override
    public String onlyRoleName(Role role) {
        if (role!=null&&!role.equals("")){
            // 定义StringBuffer 用于封装SQL语句

            StringBuffer hql=new StringBuffer();

            hql.append("FROM Role where delFlag='1' and name  = ?");
            String[] param={role.getName()};

            List<Role> roleList = (List<Role>) this.roleDao.find(hql.toString(), param);

            if(!roleList.equals("") && roleList!=null && roleList.size()!=0){
                return "角色名已被占用,请重新填写!";
            }
        }
        return null;
    }

    @Override
    public Boolean deleteroleByIds(long[] roleIds) {
        try {
            if (roleIds!=null&&roleIds.length!=0&&!roleIds.equals("")){
                for (long roleId : roleIds) {
                    Role role = this.roleDao.get(Role.class, roleId);
                    role.setDelFlag("0");
                    this.roleDao.update(role);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public Role showUpdateRole(Role role) {
        try {
            if (role!=null&&!role.equals("")) {
                return this.roleDao.get(Role.class, role.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean updateRole(Role role) {
        try {
            if (role!=null&&!role.equals("")) {
                //修改人
                role.setModifier((User) ContstanUtils.getsession().getAttribute(ContstanUtils.SESSION_USER));
                //修改时间
                role.setModifyDate(new Date());
                //更新数据
                this.roleDao.update(role);
                //返回值
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public List<User> selectRoleUser(Long roleId, PageModel pageModel) {
        if (roleId!=null&&!roleId.equals("")){
            // 定义StringBuffer 用于封装SQL语句
            StringBuffer hql=new StringBuffer();

            hql.append("select u from User u where delFlag='1' and  u.userId in (");
            hql.append("select u.userId from User u inner join u.roles r where r.id="+roleId+")");
            return this.roleDao.findByPage(hql.toString(),pageModel,null);
        }
        return null;
    }

    @Override
    public List<User> findUnBindUser(Long roleId, PageModel pageModel) {
        if (roleId!=null&&!roleId.equals("")){
            // 定义StringBuffer 用于封装SQL语句
            StringBuffer hql=new StringBuffer();

            hql.append("select u from User u where delFlag='1' and  u.userId not in (");
            hql.append("select u.userId from User u inner join u.roles r where r.id="+roleId+")");
            return this.roleDao.findByPage(hql.toString(),pageModel,null);
        }
        return null;
    }

    @Override
    public String bindUser(Long roleId, String[] userIds) {
        if (roleId!=null&&!roleId.equals("")&&!userIds.equals("")&&userIds!=null&&userIds.length!=0){
            try {
                //更具ID获取角色信息
                Role role = this.roleDao.get(Role.class, roleId);
                //获取角色绑定的用户信息
                Set<User> setUser=	role.getUsers();

                for (String userId : userIds) {
                    //根据用户的账号获取用户的信息
                    User user = this.userDao.get(User.class, userId);
                    ///建立关系
                    setUser.add(user);
                }
                return "用户绑定成功!";
            } catch (Exception e) {
                e.printStackTrace();
                return "用户绑定失败!";
            }

        }
        return "用户绑定失败!";
    }

    @Override
    public String unBindUser(Long roleId, String[] userIds) {
        if (roleId!=null&&!roleId.equals("")&&!userIds.equals("")&&userIds!=null&&userIds.length!=0){
            try {
                //根据ID获取角色信息
                Role role = this.roleDao.get(Role.class, roleId);
                //获取角色绑定的用户信息
                Set<User> setUser=	role.getUsers();

                for (String userId : userIds) {
                    User user = this.userDao.get(User.class, userId);
                    if (setUser.contains(user)){
                        setUser.remove(user);
                    }
                }
                return "用户移除成功!";
            } catch (Exception e) {
                e.printStackTrace();
                return "用户移除失败!";
            }
        }


        return "用户移除失败!";
    }


    @Override
    public Role selectRoleById(Long roleId) {
        try {
            if (roleId!=null&&!roleId.equals("")){
             return this.roleDao.get(Role.class,roleId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    }
