package com.qd.oa.menu.dao;

import com.qd.oa.common.dao.BaseDao;

import java.util.List;

public interface ModuleDao extends BaseDao {
    List<String> findLeftMenuOperas(String userId);

    List<String> findModuleOperasByUserId(String userId);
}
