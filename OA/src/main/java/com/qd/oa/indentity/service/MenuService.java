package com.qd.oa.indentity.service;

import com.qd.oa.common.util.pageTage.PageModel;
import com.qd.oa.menu.bean.Module;

import java.util.List;

public interface MenuService {
    List<Module> selectMenu(PageModel pageModel);




    List<Module> getModulesByPcode(String code, PageModel pageModel);

    Boolean addModule(Module module);

    Module showUpdateMenu(String code);

    Boolean updateModule(Module module);

    String deleterMenuByIds(String[] codes,PageModel pageModel);

    List<Module> selectModuleBycode(String code);
}
