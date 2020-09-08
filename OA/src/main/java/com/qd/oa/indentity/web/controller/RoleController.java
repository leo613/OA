package com.qd.oa.indentity.web.controller;

import com.qd.oa.common.constant.ContstanUtils;
import com.qd.oa.common.util.pageTage.PageModel;
import com.qd.oa.indentity.bean.Role;
import com.qd.oa.indentity.bean.User;
import com.qd.oa.indentity.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Resource(name = "roleService")
    private RoleService roleService;

    /**
     * todo 跳转角色页面
     * @param role
     * @param model
     * @param pageModel
     * @return
     */
     @RequestMapping(value = "selectRole",produces = "application/text;charset=UTF-8")
    private String selectRole(Role role, Model model, PageModel pageModel){
         List<Role> roleList= this.roleService.selectRole(role,pageModel);

           model.addAttribute("roles",roleList);
          return "identity/role/role";
      }

    /**
     *todo 跳转增加角色页面
     * @param model
     * @return
     */
    @RequestMapping(value = "showAddRole",produces = "application/text;charset=UTF-8")
      private String showAddRole(Model model){
         return "identity/role/addRole";
      }


    /**
     * todo 异步查询role Name是否被占用
     * @return
     */
    @RequestMapping(value = "/onlyRoleName.jspx",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    private String onlyRoleName(Role role, PageModel pageModel, Model model){
        String result= this.roleService.onlyRoleName(role);
        return result;
    }

    /**
     * todo 增加角色信息
     * @param role
     * @return
     */
    @RequestMapping(value = "addRole",method = RequestMethod.POST,produces = "application/text; charset=UTF-8")
    private String addRole(Role role,Model model){
        Boolean flag= this.roleService.addRole(role);
        if (flag){
            model.addAttribute("message","添加成功");
        }else {
            model.addAttribute("message","添加失败");
        }
        return "forward:/role/showAddRole.jspx";
    }

    /**
     * todo 删除角色信息
     * @return
     */
    @RequestMapping(value = "/deleteroleByIds.jspx")
    private String deleteroleByIds(@RequestParam(value = "roleIds") long [] roleIds, Model model){
        Boolean flag = this.roleService.deleteroleByIds(roleIds);
        if (flag){
            model.addAttribute("message","删除成功");
        }else {
            model.addAttribute("message","删除失败");
        }
        return "forward:/role/selectRole.jspx";
    }


    /**
     * todo 显示需要修改的角色信息
     * @return
     */
    @RequestMapping(value = "/showUpdateRole.jspx")
    private String showUpdateRole(Role role, Model model){
         Role roles= this.roleService.showUpdateRole(role);
         model.addAttribute("role",roles);
        return "identity/role/updateRole";
    }


    /**
     * todo 修改角色信息
     * @return
     */
    @RequestMapping(value = "/updateRole.jspx")
    private String updateRole(Role role, Model model){
        Boolean flag= this.roleService.updateRole(role);
        if (flag){
            model.addAttribute("message","更新成功");
            model.addAttribute("role",role);
        }else {
            model.addAttribute("message","更新失败");
            model.addAttribute("role",role);
        }
        return "identity/role/updateRole";
    }

    /**
     * todo 查询已绑定该角色用户信息
     * @param roleId
     * @param pageModel
     * @param model
     * @return
     */
    @RequestMapping(value = "selectRoleUser",method = RequestMethod.GET)
    private String selectRoleUser(@RequestParam (value = "roleId")Long  roleId,PageModel pageModel,Model model){
         // 通过RoleId查询未绑定该角色的用户
         List<User> userList=this.roleService.selectRoleUser(roleId,pageModel);
         model.addAttribute("users",userList);
         //通过RoleId查询该角色信息
         Role role=this.roleService.selectRoleById(roleId);
         model.addAttribute("role",role);
        return "identity/role/bindUser/roleUser";
    }

    /**
     * todo 查询未绑定该角色用户信息
     * @param roleId
     * @param pageModel
     * @param model
     * @return
     */
    @RequestMapping(value = "findUnBindUser",method = RequestMethod.GET)
    private String findUnBindUser(@RequestParam (value = "roleId")Long  roleId,PageModel pageModel,Model model){
        // 通过RoleId查询未绑定该角色的用户
        List<User> userList=this.roleService.findUnBindUser(roleId,pageModel);
        model.addAttribute("users",userList);
        //通过RoleId查询该角色信息
        Role role=this.roleService.selectRoleById(roleId);
        model.addAttribute("role",role);
        return "identity/role/bindUser/bindUser";
    }

    /**
     * todo 将已选定的用户绑定于该角色内
     * @param roleId
     * @param userIds
     * @param model
     * @return
     */
    @RequestMapping(value = "bindUser",method = RequestMethod.GET)
    public String bindUser(@RequestParam(value = "roleId")Long roleId, @RequestParam(value = "userIds")  String[] userIds,Model model) {
         String result= this.roleService.bindUser(roleId,userIds);
        model.addAttribute("message",result);
        return "forward:/role/findUnBindUser.jspx";
    }

    /**
     * todo 将已选定的用户绑定于该角色内
     * @param roleId
     * @param userIds
     * @param model
     * @return
     */
    @RequestMapping(value = "unBindUser",method = RequestMethod.GET)
    public String unBindUser(@RequestParam(value = "roleId")Long roleId, @RequestParam(value = "userIds")  String[] userIds,Model model) {
        String result= this.roleService.unBindUser(roleId,userIds);
        model.addAttribute("message",result);
        return "forward:/role/selectRoleUser.jspx";
    }

}
