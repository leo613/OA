package com.qd.oa.indentity.web.controller;


import com.qd.oa.indentity.bean.Role;
import com.qd.oa.indentity.service.PopedomService;
import com.qd.oa.indentity.service.RoleService;
import com.qd.oa.menu.bean.Module;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/popedom")
public class PopedomController {
    @Resource(name = "popedomService")
    private PopedomService popedomService;

    @Resource(name = "roleService")
    private RoleService roleService;

    /**
     * todo 跳转至权限管理页面
     * @return
     */
    @RequestMapping(value = "mgrPopedom",produces = "application/text;charset=UTF-8")
    private String mgrPopedom(Role role, Model model){
        model.addAttribute("role",role);
        return "identity/role/bindPopedom/mgrPopedom";
    }

    /**
     * todo 异步查询Model 菜单(权限菜单页面)
     * @return
     */
    @RequestMapping(value = "/loadAllModule.jspx",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    private String loadAllModule(){
        String result=this.popedomService.loadAllModule();
        return result;
    }

    /**
     * todo 跳转至operas页面
     * @return
     */
    @RequestMapping(value = "/loadThirdModule.jspx",produces = "application/text;charset=UTF-8")

    private String loadThirdModule(@RequestParam("id")Long roleId,@RequestParam("name")String name,@RequestParam("code")String code,Model model){
       //根据二级模块查询三级模块
       List<Module> moduleList =this.popedomService.loadThirdModule(code);
       model.addAttribute("modules",moduleList);

       Module  module=new Module();
       module.setCode(code);
       module.setName(name);
       model.addAttribute("module",module);

        //根据角色的id获取
        Role role=this.roleService.selectRoleById(roleId);
        model.addAttribute("role",role);

        //根据角色id以及二级模块的code查询权限表获取  已绑定的操作的code（操作指的是第三级模块）
        List<String>operas=this.popedomService.findOperasByRoleIdAndCode(code,roleId);
        model.addAttribute("operas",operas);
        return "identity/role/bindPopedom/operas";
    }


    /**
     * todo 绑定
     * @return
     */
    @RequestMapping(value = "/bindOpera.jspx")
    private String bindOpera(@RequestParam("id")Long id,@RequestParam("code")String code,@RequestParam("name")String name,String []codes,Model model){
      String result= this.popedomService.bindOpera(id,code,name,codes);
      model.addAttribute("message",result);
      return "forward:/popedom/loadThirdModule.jspx";
    }
}