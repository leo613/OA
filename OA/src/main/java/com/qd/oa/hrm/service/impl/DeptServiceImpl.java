package com.qd.oa.hrm.service.impl;


import com.qd.oa.hrm.dao.DeptDao;
import com.qd.oa.hrm.service.DeptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("deptService")
@Transactional
public class DeptServiceImpl implements DeptService {

    @Resource(name = "deptDao")
    private DeptDao deptDao;
}
