package com.taotao.datasource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;

/**
 * 切面
 * todo 定义数据源的AOP切面，通过该Service的方法名判断是应该走读库还是写库
 */
public class DataSourceAspect {

    /**
     *  在进入Service方法之前执行
     *
     * @param point 切入点 将通知用在目标对象上的哪些位置（细粒度控制到哪些包的哪些类的哪些方法上）
     */
    public void before(JoinPoint point){
        //获取当前对象执行的方法名
        String methodName = point.getSignature().getName();
        if (isSlave(methodName)){
            //标记为读库
            DynamicDataSourceHolder.markSlave();
        }else{
            //标记为写库
            DynamicDataSourceHolder.markMaster();
        }
    }

    /**
     * 判断是否为读库
     *
     * @param methodName
     * @return
     */
    private Boolean isSlave(String methodName) {
        // 方法名以query、find、get开头的方法名走从库
        return StringUtils.startsWithAny(methodName, "query", "find", "get");
    }

}
