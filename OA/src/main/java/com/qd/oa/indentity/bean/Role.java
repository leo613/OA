package com.qd.oa.indentity.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="OA_ID_ROLE")
public class Role implements Serializable {
    private static final long serialVersionUID = 6837526111700641932L;

    /** 编号	PK主键自增长 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long id;

    /** 角色名字 VARCHAR2(50)*/
    @Column(name="role_name", length=50)
    private String name;

    /** 备注	VARCHAR2(500)*/
    @Column(name="role_remark", length=500)
    private String remark;

    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "role_createDate")
    private Date createDate;

    /** 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "role_modifyDate")
    private Date modifyDate;

    /** 角色创建人与用户存在多对一关联(FK(OA_ID_USER)) */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = User.class)
    @JoinColumn(name = "user_create",referencedColumnName = "user_id",foreignKey = @ForeignKey(name = "FK_ROLE_USER_CREATER"))// 更改外键约束名
    private User creater;

    /** 角色修改人与用户存在多对一关联(FK(OA_ID_USER)) */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = User.class)
    @JoinColumn(name = "user_modifier",referencedColumnName = "user_id",foreignKey = @ForeignKey(name = "FK_ROLE_USER_MODIFIER"))// 更改外键约束名
    private User modifier;

    /** 角色与用户存在N-N关联 */
    @ManyToMany(fetch = FetchType.EAGER,targetEntity = User.class)
    @JoinTable(name ="OA_ID_USER_ROLE",joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "role_id"),
               inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "user_id"))
    private Set<User> users=new HashSet<>();

    //1：正常    0：已删除
    @Column(name="role_delFlag")
    private String delFlag = "1";

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreater() {
        return creater;
    }

    public void setCreater(User creater) {
        this.creater = creater;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public User getModifier() {
        return modifier;
    }

    public void setModifier(User modifier) {
        this.modifier = modifier;
    }
}
