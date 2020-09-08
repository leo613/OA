package com.qd.oa.hrm.bean;

import com.qd.oa.indentity.bean.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="OA_ID_DEPT")
public class Dept implements Serializable {
    private static final long serialVersionUID = 678100638005497362L;

    /** ID	NUMBER	编号	PK主键自增长*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dept_id")
    private  Long id;

    /** NAME	VARCHAR2(50) 部门名称*/
    @Column(name = "dept_name",length = 50)
    private String name;

    /** REMARK	VARCHAR2(500)	备注	*/
    @Column(name="dept_remark", length=500)
    private String remark;

    /** MODIFY_DATE	DATE	修改时间*/
    @Column(name="dept_modifyDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    /** CREATE_DATE	DATE	创建时间*/
    @Column(name="dept_createDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /** MODIFIER VARCHAR2(50) 修改人	FK(OA_ID_USER) N-1*/
    @ManyToOne(fetch=FetchType.LAZY, targetEntity= User.class)
    @JoinColumn(name="user_modifier", referencedColumnName="user_id",foreignKey=@ForeignKey(name="FK_DEPT_DEPT_MODIFIER")) // 更改外键约束名
    private User modifier;

    /** CREATER	VARCHAR2(50) 创建人	FK(OA_ID_USER) N-1*/
    @ManyToOne(fetch=FetchType.LAZY, targetEntity=User.class)
    @JoinColumn(name="user_creater", referencedColumnName="user_id",foreignKey=@ForeignKey(name="FK_DEPT_DEPT_CREATER")) // 更改外键约束名
    private User creater;

    /** setter and getter method */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getModifier() {
        return modifier;
    }

    public void setModifier(User modifier) {
        this.modifier = modifier;
    }

    public User getCreater() {
        return creater;
    }

    public void setCreater(User creater) {
        this.creater = creater;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
