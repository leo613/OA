package com.qd.oa.hrm.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "OA_ID_JOB")
public class Job implements Serializable {

    private static final long serialVersionUID = 459497377750274376L;

    /**
     * CODE	VARCHAR2(100) 代码 PK主键
     */
    @Id
    @Column(name = "job_code",length = 100)
    private String code;

    /** NAME VARCHAR2(50) 名称 */
    @Column(name = "job_name",length = 100)
    private String name;

    /** REMARK	VARCHAR2(300) 职位说明*/
    @Column(name = "job_remark",length = 300)
    private String remark;

    /** setter and getter method */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
