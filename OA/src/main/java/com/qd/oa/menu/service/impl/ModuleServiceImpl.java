package com.qd.oa.menu.service.impl;

import com.qd.oa.common.constant.ContstanUtils;
import com.qd.oa.indentity.bean.User;
import com.qd.oa.menu.bean.Module;
import com.qd.oa.menu.dao.ModuleDao;
import com.qd.oa.menu.service.ModuleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("moduleService")
@Transactional
public class ModuleServiceImpl implements ModuleService {
    @Resource(name="moduleDao")
    private ModuleDao moduleDao;

    @Override
    public Map<Module, List<Module>> findLeftMenuOperas() {
        try {
            List<String> secondCodes= null;
            //获取当前用户的id
            User user=(User) ContstanUtils.getsession().getAttribute(ContstanUtils.SESSION_USER);
            //00010001,00010002,00020001,00020002 获取二级模块
              secondCodes = moduleDao.findLeftMenuOperas(user.getUserId());


            Map<Module, List<Module>> maps=new LinkedHashMap<>();

              if (secondCodes!=null&&secondCodes.size()>0){
                  List<Module> secondCodeList=null;
                  for (String secondCode : secondCodes) {
                      //获得一级模块
                      String firstCodes = secondCode.substring(0, 4);
                      //获取一级模块信息
                      Module firstModule = moduleDao.get(Module.class, firstCodes);
                      if(!maps.containsKey(firstModule)){
                          secondCodeList=new ArrayList<>();
                          maps.put(firstModule,secondCodeList);
                      }
                      //获取二级模块信息
                      Module secondModule = moduleDao.get(Module.class, secondCode);
                      secondCodeList.add(secondModule);
                  }
              }
            return maps;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> findModuleOperasByUserId() {
        List<String> thirdCodes= null;
        //获取当前用户的id
        User user = (User) ContstanUtils.getsession().getAttribute(ContstanUtils.SESSION_USER);
        //000100010001   000100010002   000100010003   == 》user:addUser    user:deleteUser
        thirdCodes=moduleDao.findModuleOperasByUserId(user.getUserId());
        List<String>opera=new ArrayList<>();
        if (thirdCodes!=null&&thirdCodes.size()>0){
            for (String thirdCode : thirdCodes) {
                Module module = moduleDao.get(Module.class, thirdCode);
                opera.add(module.getUrl());
            }
        }
        return opera;
    }
}
