package com.qd.oa.menu.dao.impl;

import com.qd.oa.common.dao.impl.BaseDaoImpl;
import com.qd.oa.menu.dao.ModuleDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("moduleDao")
public class ModuleDaoImpl extends BaseDaoImpl implements ModuleDao {
    @Override
    public List<String> findLeftMenuOperas(String userId) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("select distinct p.module.code  from Popedom p where p.role in (");
        buffer.append("select r.id from Role r inner join r.users u where u.userId='"+userId+"') order by p.module.code  asc ");
        return find(buffer.toString());
    }

    @Override
    public List<String> findModuleOperasByUserId(String userId) {
        StringBuffer buffer=new StringBuffer();
        buffer.append("select distinct p.opera.code  from Popedom p where p.role in (");
        buffer.append("select r.id from Role r inner join r.users u where u.userId='"+userId+"') order by p.opera.code  asc ");
        return find(buffer.toString());
    }
}
