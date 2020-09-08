package com.qd.oa.menu.service;

import com.qd.oa.menu.bean.Module;

import java.util.List;
import java.util.Map;

public interface ModuleService {
    Map<Module,List<Module>> findLeftMenuOperas();

    List<String> findModuleOperasByUserId();
}
