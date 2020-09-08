package com.qd.oa.indentity.bean;

import com.qd.oa.hrm.bean.Dept;
import com.qd.oa.hrm.bean.Job;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="OA_ID_USER",
     //创建索引
        indexes = {@Index(columnList = "user_name",name = "IDX_USER_NAME")}
)
public class User implements Serializable {
    private static final long serialVersionUID = -3417930882448168081L;

    /** 用户ID	PK，大小写英文和数字 */
    @Id
    @Column(name = "user_id",length = 50)
    private String userId;

    /** 密码	MD5加密 */
    @Column(name = "user_passWord",length = 50)
    private String passWord;

    /** 姓名 */
    @Column(name = "user_name",length = 50)
    private String name;

    /** 性别	1:男 2:女 */
    @Column(name = "user_sex",length = 20)
    private String sex ;

    /** 邮箱 */
    @Column(name = "user_email",length = 50)
    private String email;

    /** 电话号码 */
    @Column(name = "user_tel",length =50 )
    private String tel;

    /** 手机号码 */
    @Column(name = "user_phone",length = 50)
    private String phone;

    /** QQ号码 */
    @Column(name = "user_qqNum",length = 50)
    private String qqNum;

    /** 问题编号 */
    @Column(name = "user_question")
    private Short question;

    /** 回答结果 */
    @Column(name = "user_answer",length = 200)
    private String answer;

    /** 状态
     *  1：状态正常
     *  0：账户冻结
     *  新建的用户默认是冻结的，需要管理员进行激活，激活成功之后才能登陆，激活就像审核一样
     *   */
    @Column(name = "user_status")
    private Short status = 0;

    /** 创建时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_createDate")
    private Date createDate;

    /** 修改时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_modifyDate")
    private Date modifyDate;

    /** 审核时间 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_checkDate")
    private Date checkDate;

    /** 用户创建人与用户存在多对一关联(FK(OA_ID_USER)) */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = User.class)
    @JoinColumn(name = "user_creater",referencedColumnName = "user_id",foreignKey = @ForeignKey(name = "FK_USER_CREATER"))//更改外键名称
    private User creater;

    /** 用户修改人与用户存在多对一关联(FK(OA_ID_USER)) */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = User.class)
    @JoinColumn(name = "user_modifier",referencedColumnName = "user_id",foreignKey = @ForeignKey(name = "FK_USER_MODIFER"))//更改外键名称
    private User modifier;

    /** 部门审核人与用户存在多对一关联(FK(OA_ID_USER)) */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = User.class)
    @JoinColumn(name = "user_checker",referencedColumnName = "user_id",foreignKey = @ForeignKey(name = "FK_USER_CHCKER"))//更改外键名称
    private User checker;

    /** 用户与部门存在多对一关联    部门	FK(OA_ID_DEPT) */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Dept.class)
    @JoinColumn(name = "dept_id",referencedColumnName = "dept_id",foreignKey = @ForeignKey(name = "FK_USER_DEPT"))
    private Dept dept;

    /** 用户与职位存在多对一关联    职位	FK(OA_ID_JOB) */
    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Job.class)
    @JoinColumn(name = "job_code",referencedColumnName = "job_code",foreignKey = @ForeignKey(name = "FK_USER_JOB"))
    private Job job;

    /** 用户与角色存在N-N关联 */
    @ManyToMany(fetch = FetchType.EAGER,targetEntity = Role.class,mappedBy = "users")
    private Set<Role> roles = new HashSet<>();

    //定义字段用于记录删除的状态      delFlag:1 正常     delFlag：0 已删除
    @Column(name = "user_delFlag")
    private String delFlag = "1";

    /** setter and getter method */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQqNum() {
        return qqNum;
    }

    public void setQqNum(String qqNum) {
        this.qqNum = qqNum;
    }

    public Short getQuestion() {
        return question;
    }

    public void setQuestion(Short question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
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

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public User getCreater() {
        return creater;
    }

    public void setCreater(User creater) {
        this.creater = creater;
    }

    public User getModifier() {
        return modifier;
    }

    public void setModifier(User modifier) {
        this.modifier = modifier;
    }

    public User getChecker() {
        return checker;
    }

    public void setChecker(User checker) {
        this.checker = checker;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", passWord='" + passWord + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
