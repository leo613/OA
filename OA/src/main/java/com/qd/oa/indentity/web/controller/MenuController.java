package com.qd.oa.indentity.web.controller;

import com.qd.oa.common.util.pageTage.PageModel;
import com.qd.oa.indentity.bean.Role;
import com.qd.oa.indentity.service.MenuService;
import com.qd.oa.indentity.service.PopedomService;
import com.qd.oa.indentity.service.RoleService;
import com.qd.oa.menu.bean.Module;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {
    @Resource(name = "menuService")
    private MenuService menuService;

    @Resource(name = "popedomService")
    private PopedomService popedomService;

    @Resource(name = "roleService")
    private RoleService roleService;

    /**
     * todo 跳转至权限管理页面
     * @return
     */
    @RequestMapping(value = "mgrModule",produces = "application/text;charset=UTF-8")
    private String mgrModule(Role role, Model model){
        model.addAttribute("role",role);
        return "module/mgrModule";
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
     * todo 分页查询下级菜单信息并跳转至module页面
     * @return
     */
    @RequestMapping(value = "/getModulesByPcode.jspx",produces = "application/text;charset=UTF-8")

    private String getModulesByPcode(@RequestParam("code")String code,Model model,PageModel pageModel){
        List<Module> moduleList=null;
        if (code==null||code.equals("")){
//            如果code为空则为一级模块
            moduleList =  this.menuService.selectMenu(pageModel);
            model.addAttribute("showCode","0001");
        }else {
            //根据二级模块查询三级模块
             moduleList =this.menuService.getModulesByPcode(code,pageModel);
            //关闭添加模块是 可跳转至此
            model.addAttribute("showCode",code);
        }
        model.addAttribute("modules",moduleList);
        return "module/module";
    }



    /**
     *todo  分页查询并跳转至菜单管理
     * @param model
     * @param pageModel
     * @return
     */
    @RequestMapping(value = "/selectMenu")
    private String selectMenu(Model model, PageModel pageModel){
        List<Module> moduleList=this.menuService.selectMenu(pageModel);
        model.addAttribute("modules",moduleList);
        return "module/module";
    }

    /**
     * TODO  展示添加页面
     * @param codes
     * @param model
     * @return
     */
    @RequestMapping(value = "/showAddMenu")
    private String showAddMenu(@RequestParam("codes")String[] codes, Model model){
        //自定义添加module数据code
        int s=0;
        String prefix="000";
         for (int i=0;i<codes.length;i++){
             int code= Integer.parseInt(codes[i]);
             if (s<code){
                 s=code;
             }
         }
         int result=s+1;
         String moduleCode=prefix+result;

        System.out.println(moduleCode);
        model.addAttribute("moduleCode",moduleCode);
        return "module/addModule";
    }

    /**
     * TODO  添加module信息
     * @return
     */
    @RequestMapping(value = "/addModule",method = RequestMethod.POST,produces = "application/text;charset=UTF-8")
    private String addModule(Module module,Model model){
        Boolean flag= this.menuService.addModule(module);
        if (flag){
            model.addAttribute("message","添加成功");
            model.addAttribute("module",module);
        }else {
            model.addAttribute("message","添加失败");
            model.addAttribute("module",module);
        }
        return "module/addModule";
    }

    /**
     * 展示更新页面
     * @param code
     * @param model
     * @return
     */
    @RequestMapping(value = "/showUpdateMenu",method = RequestMethod.GET,produces = "application/text;charset=UTF-8")
    private String showUpdateMenu(@RequestParam("code")String code,Model model){
           Module  module= this.menuService.showUpdateMenu(code);
            model.addAttribute("module",module);
        return "module/updateModule";
    }

    /**
     * 更新菜单信息
     * @param module
     * @param model
     * @return
     */
    @RequestMapping(value = "/updateModule",method = RequestMethod.POST,produces = "application/text;charset=UTF-8")
    private String updateModule(Module module,Model model){
        Boolean flag= this.menuService.updateModule(module);
        if (flag){
            model.addAttribute("message","更新成功");
            model.addAttribute("module",module);
        }else {
            model.addAttribute("message","更新失败");
            model.addAttribute("module",module);
        }
        return "module/updateModule";
    }

    /**
     * 删除菜单
     * @param codes
     * @param model
     * @return
     */

    @RequestMapping(value = "/deleterMenuByIds",method = RequestMethod.GET,produces = "application/text;charset=UTF-8")
    private String deleterMenuByIds(String[] codes, Model model,PageModel pageModel){
            String  result=  this.menuService.deleterMenuByIds(codes,pageModel);
            model.addAttribute("message",result);

       //根据模块级数跳转至module主菜单
        for (int i=0;i<codes.length;i++) {
              List<Module> moduleList= this.menuService.selectModuleBycode(codes[0]);
             model.addAttribute("modules",moduleList);

         }
        return "module/module";
    }
}
