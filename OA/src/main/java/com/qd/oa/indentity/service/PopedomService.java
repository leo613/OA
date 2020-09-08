package com.qd.oa.indentity.service;

import com.qd.oa.menu.bean.Module;

import java.util.List;

public interface PopedomService {

    String loadAllModule();

    List<Module> loadThirdModule(String code);

    List<String> findOperasByRoleIdAndCode(String code, Long roleId);

    String bindOpera(Long id, String code, String name, String[] codes);

    void deleteOperas(String code, Long id);
}
