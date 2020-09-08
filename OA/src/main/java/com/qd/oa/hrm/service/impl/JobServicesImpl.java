package com.qd.oa.hrm.service.impl;


import com.qd.oa.hrm.dao.JobDao;
import com.qd.oa.hrm.service.JobServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("jobService")
@Transactional
public class JobServicesImpl implements JobServices {
    @Resource(name = "jobDao")
    private JobDao jobDao;

}
