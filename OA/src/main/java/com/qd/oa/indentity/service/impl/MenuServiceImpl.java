package com.qd.oa.indentity.service.impl;

import com.qd.oa.common.constant.ContstanUtils;
import com.qd.oa.common.util.pageTage.PageModel;
import com.qd.oa.indentity.bean.Popedom;
import com.qd.oa.indentity.bean.User;
import com.qd.oa.indentity.dao.MenuDao;
import com.qd.oa.indentity.dao.PopedomDao;
import com.qd.oa.indentity.service.MenuService;
import com.qd.oa.menu.bean.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "menuService")
@Transactional
public class MenuServiceImpl implements MenuService {
    @Resource(name = "menuDao")
    private MenuDao menuDao;

    @Resource(name = "popedomDao")
    private PopedomDao popedomDao;

    @Override
    public List<Module> selectModuleBycode(String code) {
        //获取二级权限信息
        StringBuffer hql=new StringBuffer();
        List<Object> params=new ArrayList<>();

        if (code.length()==4){
            hql.append("From Module where delFlag=1 and length(code)=4" );
            return this.menuDao.find(hql.toString());
        }else if (code.length()==8){
            String  firstCode= code.substring(0,4);
            hql.append("From Module where delFlag=1 and length(code)=8 and code like ?" );
            params.add(firstCode +"%");
            return (List<Module>) this.menuDao.find(hql.toString(), params.toArray());
        }else {
            String  firstCode= code.substring(0,8);
            hql.append("From Module where delFlag=1 and length(code)=12 and code like ?" );
            params.add(firstCode + "%");
           return  (List<Module>) this.menuDao.find(hql.toString(), params.toArray());
        }

    }


    @Override
    public List<Module> selectMenu(PageModel pageModel) {
        try {
            //获取二级权限信息
            StringBuffer hql=new StringBuffer();
            hql.append("From Module where delFlag=1 and length(code)=4" );
            return  this.menuDao.findByPage(hql.toString(),pageModel,null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Module> getModulesByPcode(String code,PageModel pageModel) {
        try {
            if (StringUtils.isNotBlank(code)){
                StringBuffer hql=new StringBuffer();
                List<Object> params=new ArrayList<>();

                hql.append("From Module where delFlag=1 and code like ? and length(code)=?");
                params.add(code +"%");
                int length=code.length();
                params.add(length+4);
                return  this.menuDao.findByPage(hql.toString(),pageModel,params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean addModule(Module module) {
        HttpSession session=ContstanUtils.getsession();
        try {
            if (!module.equals("")||module!=null){
                //创建时间
                module.setCreateDate(new Date());
                //创建人
                module.setCreater((User) session.getAttribute(ContstanUtils.SESSION_USER));
                this.menuDao.save(module);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Module showUpdateMenu(String code) {
        try {
            if (StringUtils.isNoneBlank(code)){
                Module module = this.menuDao.get(Module.class, code);
                return module;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
         return null;
    }

    @Override
    public Boolean updateModule(Module module) {
        try {
            HttpSession session=ContstanUtils.getsession();
            if(module!=null&&!module.equals("")){
                //修改时间
                module.setModifyDate(new Date());
                //修改人
                module.setModifier((User) session.getAttribute(ContstanUtils.SESSION_USER));
                this.menuDao.saveOrUpdate(module);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public String deleterMenuByIds(String[] codes,PageModel pageModel) {
        String message=null;
        if (codes!=null&&!codes.equals("")){

            for (String code : codes) {
                //若是一级模块,则查询三级模块是否用绑定角色
                if (code.length()==4){
                    StringBuffer hql=new StringBuffer();
                    List<Object> params=new ArrayList<>();
                    hql.append("From Module where delFlag=1 and code like ? and length(code)=?");
                    params.add(code +"%");
                    int length=code.length();
                    params.add(length+8);
                    //获取一级模块下面三级模块
                    List<Module> thriedModule =(List<Module>) this.menuDao.find(hql.toString(), params.toArray());
                    if (thriedModuleTest(code, thriedModule)) {
                        return "请解除所绑定角色！";
                    }else{
                     message= "删除成功";
                    }

                    //判断是否为二级模块
                }else if(code.length()==8){
                    //根据二级模块查询三级模块
                    List<Module> thriedModule = this.getModulesByPcode(code,pageModel);
                    if (thriedModuleTest(code, thriedModule)) {
                        return "请解除所绑定角色！";
                    }else{
                        message= "删除成功";
                    }
                }else{
                    //根据三级模块判断是否存在绑定相关角色
                    for (String s : codes) {
                        StringBuffer  popedomHql=new StringBuffer();
                        popedomHql.append("select  p.role.id from Popedom p where p.opera.code= ");
                        //获取三级模块相关的角色
                        popedomHql.append(s);
                        List<Popedom> popedomList = this.popedomDao.find(popedomHql.toString());
                        //判断是否存在相关角色
                        if (popedomList!=null && !popedomList.equals("") && popedomList.size()!=0){
                          return  "请解除所绑定角色！";
                        }else{
                            //删除相关菜单
                            Module module= this.menuDao.get(Module.class,code);
                            module.setDelFlag("0");
                            this.menuDao.update(module);
                            message= "删除成功";
                        }
                    }

                }
            }

            return message;
        }
        return null;
    }


    private boolean thriedModuleTest(String code, List<Module> thriedModule) {
        //三级模块是否为空
        if (thriedModule!=null&&!thriedModule.equals("")&&thriedModule.size()!=0){
            //遍历Module 并获取三级模块的code
            for (Module modules : thriedModule) {
               String thriedCode= modules.getCode();
                StringBuffer  popedomHql=new StringBuffer();
                popedomHql.append("select  p.role.id from Popedom p where p.opera.code= ");
                //获取三级模块相关的角色
                popedomHql.append(thriedCode);
                List<Popedom> popedomList = this.popedomDao.find(popedomHql.toString());
                //判断是否存在相关角色
                if (popedomList!=null && !popedomList.equals("")&& popedomList.size()!=0){
                    return true;
                }else{
                    //删除相关菜单
                    Module module= this.menuDao.get(Module.class,code);
                    module.setDelFlag("0");
                    this.menuDao.update(module);
                }
            }
        }else {
            //删除相关菜单
            Module module= this.menuDao.get(Module.class,code);
            module.setDelFlag("0");
            this.menuDao.update(module);
        }
        return false;
    }
}
