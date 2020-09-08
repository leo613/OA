package com.qd.oa.indentity.dao.impl;

import com.qd.oa.common.dao.BaseDao;
import com.qd.oa.common.dao.impl.BaseDaoImpl;
import com.qd.oa.indentity.bean.User;
import com.qd.oa.indentity.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl extends BaseDaoImpl implements UserDao {

}
