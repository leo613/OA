package com.taotao.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_content_category")
public class ContentCategory extends BasePojo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;//父类目ID=0时，代表的是一级的类目

    private String name;//分类名称

    private Integer status;//状态。可选值:1(正常),2(删除)

    @Column(name = "sort_order")
    private Integer sortOrder; //排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数

    @Column(name = "is_parent")
    private Boolean isParent; //该类目是否为父类目，1为true，0为false

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    // 扩展字段，用于EasyUI中tree结构
    public String getText() {
        return getName();
    }

    public String getState() {
        return getIsParent() ? "closed" : "open";
    }

    @Override
    public String toString() {
        return "ContentCategory{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", sortOrder=" + sortOrder +
                ", isParent=" + isParent +
                '}';
    }
}
