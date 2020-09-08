package com.qd.oa.menu.bean;

import com.qd.oa.indentity.bean.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="OA_ID_MODULE")
public class Module implements Serializable {
    private static final long serialVersionUID = 5139796285142133024L;
    /**
     * CODE	VARCHAR2(100)	代码	PK主键
     * (0001...0002)四位为模块;
     * (00010001..)八位为操作
     */
    @Id
    @Column(name = "moudle_code",length = 100)
    private String code;

    /** 名称 VARCHAR2(50) */
    @Column(name = "moudle_name",length = 50)
    private String name;

    /**	操作链接  VARCHAR2(100)**/
    @Column(name = "moudle_url",length = 100)
    private String url;

    /** 备注	VARCHAR2(500)*/
    @Column(name = "moudle_remark",length = 500)
    private String remark;

    /** 修改时间 */
    @Column(name="moudle_modifyDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    /** 创建时间 */
    @Column(name="moudle_createDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /** 模块创建人与用户存在多对一关联(FK(OA_ID_USER)) */
    @ManyToOne(fetch=FetchType.EAGER, targetEntity= User.class)
    @JoinColumn(name="user_creater", referencedColumnName="user_id",foreignKey=@ForeignKey(name="FK_MODULE_MOUDLE_CREATER")) // 更改外键约束名
    private User creater;

    /** 模块修改人与用户存在多对一关联(FK(OA_ID_USER)) */
    @ManyToOne(fetch=FetchType.EAGER, targetEntity=User.class)
    @JoinColumn(name="user_modifier", referencedColumnName="user_id",foreignKey=@ForeignKey(name="FK_MODULE_MOUDLE_MODIFIER")) // 更改外键约束名
    private User modifier;


    //定义字段用于记录删除的状态      delFlag:1 正常     delFlag：0 已删除
    @Column(name="moudle_delFlag")
    private String delFlag = "1";

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
