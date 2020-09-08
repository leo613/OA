package com.qd.oa.indentity.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qd.oa.common.constant.ContstanUtils;
import com.qd.oa.indentity.bean.Popedom;
import com.qd.oa.indentity.bean.Role;
import com.qd.oa.indentity.bean.User;
import com.qd.oa.indentity.dao.PopedomDao;
import com.qd.oa.indentity.service.PopedomService;
import com.qd.oa.menu.bean.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "popedomService")
@Transactional
public class PopedomServiceImpl implements PopedomService {
    @Resource(name = "popedomDao")
    private PopedomDao popedomDao;


    @Override
    public String loadAllModule() {
        JSONArray jsonArray=new JSONArray();
        List<Module> modules = this.popedomDao.find(Module.class);
        for (Module module : modules) {
            JSONObject jsonObject=new JSONObject();
            String code = module.getCode();
            //模块的编号
            jsonObject.put("id",code);
            //获取父级的模块编号
            jsonObject.put("pid",code.length()==4?"1":code.substring(0,code.length()-4));
            //模块的名称
            jsonObject.put("name",module.getName());
            jsonArray.add(jsonObject);
        }
        return jsonArray.toString();
    }

    @Override
    public List<Module> loadThirdModule(String code) {
        try {
            if (StringUtils.isNotBlank(code)){
                StringBuffer hql=new StringBuffer();
                List<Object> params=new ArrayList<>();

                hql.append("From Module where delFlag=1 and code like ? and length(code)=?");
                params.add(code +"%");
                int length=code.length();
                params.add(length+4);
                List<Module> moduleList = (List<Module>) this.popedomDao.find(hql.toString(), params.toArray());
                return moduleList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> findOperasByRoleIdAndCode(String code, Long roleId) {
          if (StringUtils.isNotBlank(code)||!roleId.equals("")||roleId!=null){
              StringBuffer hql=new StringBuffer();
              hql.append("select  p.opera.code from Popedom p where p.role.id="+roleId+" and p.module.code="+code);
              return this.popedomDao.find(hql.toString());
          }

        return null;
    }

    @Override
    public void deleteOperas(String code, Long id) {
        // TODO Auto-generated method stub
        String hql = "delete from Popedom p where p.module.code = '"+code+"' and p.role.id = "+id;
        this.popedomDao.bulkUpdate(hql, null);
    }


    @Override
    public String bindOpera(Long id, String code, String name, String[] codes) {
            //先查询绑定的信息
            List<String> operasByRoleIdAndCode = this.findOperasByRoleIdAndCode(code, id);
            if (operasByRoleIdAndCode!=null&&!operasByRoleIdAndCode.equals("")){
                //将之前绑定好的权限信息删除
                this.deleteOperas(code,id);
            }
            //指定角色的id
            Role role=new Role();
            role.setId(id);
            //指定二级模块的code
            Module module=new Module();
            module.setCode(code);
            //2、根据前台传递的参数信息 重新进行绑定
            if (codes!=null&&!code.equals("")){
                for (String thirdCode : codes) {
                    Popedom popedom=new Popedom();
                    //将角色ID添加至Popedom表中
                    popedom.setRole(role);
                    //设置创建时间
                    popedom.setCreateDate(new Date());
                    //设置创建人
                    popedom.setCreater((User) ContstanUtils.getsession().getAttribute(ContstanUtils.SESSION_USER));
                     //设置二级模块
                    popedom.setModule(module);
                    //设置三级模块
                    Module m=new Module();
                    m.setCode(thirdCode);
                    popedom.setOpera(m);
                    this.popedomDao.save(popedom);
                }
                return "绑定成功";
            }
             return "绑定失败";
    }


}
