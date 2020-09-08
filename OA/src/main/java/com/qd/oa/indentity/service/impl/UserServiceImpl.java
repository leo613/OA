package com.qd.oa.indentity.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.qd.oa.common.constant.ContstanUtils;
import com.qd.oa.common.util.md5.MD5;
import com.qd.oa.common.util.pageTage.PageModel;
import com.qd.oa.hrm.bean.Dept;
import com.qd.oa.hrm.bean.Job;
import com.qd.oa.hrm.dao.DeptDao;
import com.qd.oa.hrm.dao.JobDao;
import com.qd.oa.indentity.bean.User;
import com.qd.oa.indentity.dao.UserDao;
import com.qd.oa.indentity.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;

import static com.qd.oa.common.constant.ContstanUtils.SESSION_USER;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Resource(name = "userDao")
    private UserDao userDao;

    @Resource(name = "deptDao")
    private DeptDao deptDao;

    @Resource(name = "jobDao")
    private JobDao jobDao;


    /**
     * 登录
     * @param userId
     * @param passWord
     * @param vcode
     * @return
     */
    @Override
    public String userLogin(String userId, String passWord, String vcode) throws Exception {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = requestAttributes.getRequest().getSession();
        String sessionCode = (String) session.getAttribute("virify_code");
        if (!sessionCode.equals(vcode)){
            return "您验证码输入错误!";
        }else{
            User user = userDao.get(User.class, userId);

            if (user.getDelFlag().equals("0")){
                return "您的账号已过期!";
            }

            if (user.getStatus().equals(0)||user.getStatus()==0){
                return "您的账号未激活,请找管理员激活账号!";
            }

            if (user==null|| !user.getUserId().equals(userId)){
                return "您的账号输入错误!";
            }else if (!user.getPassWord().equals(MD5.getMd5(passWord))){
                System.out.println(MD5.getMd5(passWord));
                return "您的密码输入错误!";
            }
            session.setAttribute("session_user",user);
        }
           return "";
    }

    /**
     * 更新个人信息展示
     * @param user
     * @return
     */
    @Override
    public Boolean updateSelf(User user) {
        //转换为数组
        Object[]userArray={user.getName(),user.getSex(),user.getEmail(),user.getTel(),user.getPhone(),user.getQuestion(),user.getAnswer(),user.getQqNum(),user.getUserId()};
        String sql="update User set name=?,sex=?,email=?,tel=?,phone=?,question=?,answer=?,qqNum=? where userId=?";
        int i = userDao.bulkUpdate(sql, userArray);
        if (i>0){
            HttpSession session=ContstanUtils.getsession();
            session.setAttribute("session_user",user);
            return true;
        }
        return false;
    }

    /**
     * 模糊查询 User 和 分页查询
     * @param user
     * @param pageModel
     * @return
     */
    @Override
    public List<User> selectUser(User user, PageModel pageModel) {
        // 定义StringBuffer 用于封装SQL语句
        StringBuffer hql=new StringBuffer();
        // 定义List用于放置参数
        List<Object> list=new ArrayList<>();
        // Hql 语句
        hql.append("From User where delFlag=1");

        if (!StringUtils.isEmpty(user.getName())){
           hql.append(" and name like ? ");
           list.add("%"+user.getName()+"%");
        }

        if (!StringUtils.isEmpty(user.getUserId())){
            hql.append(" and user_id like ? ");
            list.add("%"+user.getUserId()+"%");
        }

        if (!StringUtils.isEmpty(user.getPhone())){
            hql.append(" and phone like ? ");
            list.add("%"+user.getPhone()+"%");
        }

        if (user.getDept()!=null&&user.getDept().getId()!=null&&user.getDept().getId()!=0){
            hql.append(" and dept.id = ? ");
            list.add(user.getDept().getId());
        }

        if (user.getJob()!=null&&!StringUtils.isEmpty(user.getJob().getCode())&&!user.getJob().getCode().equals("0")){
            hql.append(" and job.code = ? ");
            list.add(user.getJob().getCode());
        }

        return userDao.findByPage(hql.toString(),pageModel,list);

    }

    @Override
    public String ajaxLoadDeptAndJob() {
        Map<String,String> deptsMap=new HashMap<>();
        Map<String,String> jobsMap=new HashMap<>();
        try {
            List<Dept> deptList=deptDao.find(Dept.class);
            for (Dept depts : deptList) {
             deptsMap.put(depts.getId().toString(),depts.getName());
            }

            List<Job> jobList=jobDao.find(Job.class);
            for (Job jobs : jobList) {
             jobsMap.put(jobs.getCode(),jobs.getName());
            }
            JSONObject obj=new JSONObject();
            obj.put("depts",deptsMap);
            obj.put("jobs",jobsMap);
            return obj.toJSONString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String onlyUserId(User user,PageModel pageModel) {

        if (user.getUserId()==null||user.getUserId().equals("")){
            return "账号不能为空!";
        }

        List<User> userList = selectUser(user, pageModel);

        if(!userList.equals("") && userList!=null && userList.size()!=0){
            return "账号已被占用,请重新填写!";
        }
        return null;
    }

    @Override
    public Boolean addUser(User user) {
        HttpSession session=ContstanUtils.getsession();

        try {
            if(user!=null&&!user.equals("")){
                //创建时间
                user.setCreateDate(new Date());
                //创建人
                user.setCreater((User) session.getAttribute(ContstanUtils.SESSION_USER));
                //添加用户
                 userDao.save(user);
                 return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return null;
    }

    @Override
    public Boolean deleteUserByIds(String[] userIds) {

        try {
         if (userIds!=null&&userIds.length!=0&&!userIds.equals("")){
             for (String userId : userIds) {
                 User user = this.userDao.get(User.class, userId);
                 user.setDelFlag("0");
                 this.userDao.update(user);
             }
             return true;
         }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return null;
    }


    @Override
    public String AccountChange(User user) {
        if(user!=null&&!user.equals("")){
            User u = this.userDao.get(User.class, user.getUserId());
           if (u.getStatus()==1){
               try {
                   short s=0;
                   u.setStatus(s);
                   this.userDao.update(u);
                   return "账户冻结成功!";
               } catch (Exception e) {
                   e.printStackTrace();
                   return "账户冻结失败!";
               }
           }else {
               try {
                   short s=1;
                   u.setStatus(s);
                   this.userDao.update(u);
                   return "账户激活成功!";
               } catch (Exception e) {
                   e.printStackTrace();
                   return "账户激活失败!";
               }
           }

        }
        return null;
    }

    @Override
    public User selectUserById(User user) {
        try {
            if(user!=null&&!user.equals("")){
              return this.userDao.get(User.class,user.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
             return null;
    }

    @Override
    public Boolean updateUserSave(User user) {
        HttpSession session=ContstanUtils.getsession();
        //创建时间
        user.setModifyDate(new Date());
        //创建人
        user.setModifier((User) session.getAttribute(ContstanUtils.SESSION_USER));
        //转换为数组
        Object[]userArray={user.getName(),user.getPassWord(),user.getSex(),user.getEmail(),user.getTel(),user.getPhone(),user.getQuestion(),user.getAnswer(),user.getQqNum(),user.getDept().getId(),user.getJob().getCode(),user.getModifyDate(),user.getModifier(),user.getUserId()};
        String sql="update User set name=?,passWord=?,sex=?,email=?,tel=?,phone=?,question=?,answer=?,qqNum=?,dept.id=?,job.code=?,modifyDate=?,modifier=? where userId=?";
        int i = userDao.bulkUpdate(sql, userArray);
        if (i>0){
            return true;
        }
        return false;
    }
}
