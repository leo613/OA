package com.qd.oa.menu.web.controller;

import com.qd.oa.common.constant.ContstanUtils;
import com.qd.oa.indentity.bean.User;
import com.qd.oa.menu.bean.Module;
import com.qd.oa.menu.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/main")
public class Maincontroller {
    @Resource(name = "moduleService")
    private ModuleService moduleService;
    /*
     * 需求,控制主页面左侧菜单栏以及相关模块信息的显示与隐藏
     *  1. 根据用户账号查询用户与角色的中间表获取该用户有哪些角色
     *  2. 根据角色id 查询权限表获取用户拥有哪些二级模块的操作权限，那么这些对应的二级模块应该展示给用户看
     *  3. 再根据角色id查询权限表获取用户拥有哪些操作(第三级权限)
     *
     */
    @RequestMapping(value = "main.jspx",method = RequestMethod.GET)
    private String main(WebRequest request){
        request.removeAttribute("message",1);

        //根据用户账号查询用户与角色的中间表获取该用户有哪些角色
        Map<Module,List<Module>>maps=moduleService.findLeftMenuOperas();
        request.setAttribute("menuOperas",maps, WebRequest.SCOPE_REQUEST);

        //再根据角色id查询权限表获取用户拥有哪些操作(第三级权限)
        List<String> moduleOperas=moduleService.findModuleOperasByUserId();
        request.setAttribute("moduleOperas",moduleOperas,WebRequest.SCOPE_SESSION);
        return "main";
    }
    
    

}
