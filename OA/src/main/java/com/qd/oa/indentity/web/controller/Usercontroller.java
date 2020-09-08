package com.qd.oa.indentity.web.controller;

import com.qd.oa.common.constant.ContstanUtils;
import com.qd.oa.common.util.md5.MD5;
import com.qd.oa.common.util.pageTage.PageModel;
import com.qd.oa.hrm.service.DeptService;
import com.qd.oa.hrm.service.JobServices;
import com.qd.oa.indentity.bean.User;
import com.qd.oa.indentity.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class Usercontroller {
    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "deptService")
    private DeptService deptService;

    @Resource(name = "jobService")
    private JobServices jobServices;

    /**
     * todo 登录页面
     * @param request
     * @param userId
     * @param passWord
     * @param vcode
     * @return
     */
    @RequestMapping(value = "/ajaxLogin.jspx",method = RequestMethod.POST,produces = "application/text; charset=UTF-8")
    @ResponseBody
    private String login(HttpServletRequest request,@RequestParam String userId,@RequestParam String passWord,@RequestParam String vcode){
         String result=null;
        try {
            result = userService.userLogin(userId, passWord, vcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 展示个人登录信息
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value ="/showSelf.jspx",method = RequestMethod.GET,produces = "application/text;charset=UTF-8")
    private String showSelf(HttpServletRequest request){
        User session_user = (User) request.getSession().getAttribute("session_user");
        return "home";
    }

    /**
     * 更新个人信息展示
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value ="/updateSelf.jspx",method = RequestMethod.POST,produces = "application/text;charset=UTF-8")
    private String updateSelf(User user, Model model){
        try {
            user.setPassWord(MD5.getMd5(user.getPassWord()));
            Boolean flag= userService.updateSelf(user);
            if (flag){
                model.addAttribute("message","更新成功");
            }else {
                model.addAttribute("message","更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "home";
    }

    /**
     * todo 用户页面:模糊查询 User 和 分页查询
     * @param model
     * @return
     */
    @RequestMapping(value ="/selectUser.jspx",produces = "application/text;charset=UTF-8")
    private  String selectUser(User user, PageModel pageModel, Model model){
         List<User> userList= userService.selectUser(user,pageModel);
         model.addAttribute("users",userList);
        return "/identity/user/user";
    }


    /**
     * todo 用户页面:异步加载部门与职位信息
     * @return
     */
    @RequestMapping(value ="/ajaxLoadDeptAndJob.jspx",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    private String ajaxLoadDeptAndJob(){
      String result=userService.ajaxLoadDeptAndJob();
        return result;
    }

    /**
     *todo 跳转addUser页面
     * @return
     */
    @RequestMapping(value = "/showAddUser.jspx")
    private String showAddUser(){
        return "/identity/user/addUser";
    }

    /**
     * todo 异步查询userId是否被占用
     * @return
     */
    @RequestMapping(value = "/onlyUserId.jspx",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    private String onlyUserId(User user,PageModel pageModel,Model model){
         String result= this.userService.onlyUserId(user,pageModel);
        return result;
    }

    /**
     * todo 添加用户
     * @return
     */
    @RequestMapping(value = "/addUser.jspx",method = RequestMethod.POST,produces = "application/text;charset=UTF-8")
    private String addUser(User user,Model model){
        try {
            user.setPassWord(MD5.getMd5(user.getPassWord()));
            Boolean flag = this.userService.addUser(user);
            if (flag){
                model.addAttribute("message","添加成功");
            }else {
                model.addAttribute("message","添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "forward:/user/showAddUser.jspx";
    }


    /**
     * todo 删除用户
     * @return
     */
    @RequestMapping(value = "/deleteUserByIds.jspx")
    private String deleteUserByIds(@RequestParam(value = "userIds") String [] userIds,Model model){

        Boolean flag = this.userService.deleteUserByIds(userIds);
        if (flag){
            model.addAttribute("message","删除成功");
        }else {
            model.addAttribute("message","删除失败");
        }
        return "forward:/user/selectUser.jspx";
    }

    /**
     * 更改用户状态
     * @param user
     * @return
     */
    @RequestMapping(value = "/AccountChange.jspx")
    private String AccountChange(User user){
       String result = this.userService.AccountChange(user);
        HttpSession session = ContstanUtils.setSeeesion();
        session.setAttribute("message",result);
        return "redirect:/user/selectUser.jspx";
   }

    /**
     *  预览信息页面
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value = "/showOtherInfo.jspx")
    private String showOtherInfo(User user,Model model){
      User users=  this.userService.selectUserById(user);
       model.addAttribute("user",users);
        return "/identity/user/preUser";
    }

    /**
     *  跳转修改页面
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value = "/showUpdateUser.jspx")
    private String showUpdateUser(User user,Model model){
        User users=  this.userService.selectUserById(user);
        model.addAttribute("user",users);
        return "/identity/user/updateUser";
    }

    /**
     * 修改用户
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value ="/updateUserSave.jspx",method = RequestMethod.POST,produces = "application/text;charset=UTF-8")
    private String updateUserSave(User user, Model model){
        try {
            user.setPassWord(MD5.getMd5(user.getPassWord()));
            Boolean flag= userService.updateUserSave(user);
            if (flag){
                model.addAttribute("message","更新成功");
                model.addAttribute("user",user);
            }else {
                model.addAttribute("message","更新失败");
                model.addAttribute("user",user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "forward:/user/showUpdateUser.jspx";
    }

    /**
     *  退出
     * @return
     */
    @RequestMapping(value = "/logout.jspx")
    private String logout(HttpSession session){
       session.removeAttribute("session_user");
        return "/login/login";
    }
}
