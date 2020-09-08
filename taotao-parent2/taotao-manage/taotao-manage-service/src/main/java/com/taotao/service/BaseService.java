package com.taotao.service;

import com.github.abel533.entity.Example;
import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.pojo.BasePojo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public abstract class BaseService<T extends BasePojo> {
    @Autowired
    public Mapper<T> mapper;

    /**
     * 根据主键id查询一条数据
     * @param id
     * @return
     */
    public T queryById(Object id){
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询表中所有的数据
     * @return
     */

    public List<T> queryAll(){
        return mapper.select(null);
    }

    /**
     * 根据查询条件查询一条数据
     * @param record
     * @return
     */
    public T queryOne(T record){
       return mapper.selectOne(record);
    }

    /**
     * 根据条件查询集合数据
     * @return
     */
    public List<T> queryListByWhere(T record){
        return mapper.select(record);
    }

    /**
     * 按照条件进行分页查询
     * @param record
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T record,int pageNum,int pageSize){
        //开启分页
        PageHelper.startPage(pageNum,pageSize);
        List<T> select = mapper.select(record);
        return  new PageInfo<T> (select);
    }

    /**
     * 保存一条数据
     * @param record
     * @return
     */
    public int save(T record){
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return mapper.insert(record);
    }

    /**
     * 保存非空字段数据
     * @param record
     * @return
     */
    public int saveSelective(T record){
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return mapper.insertSelective(record);
    }

    /**
     * 根据条件更新不为空的字段
     * @param record
     * @return
     */
    public int updateSelective(T record){
        record.setUpdated(new Date());
        record.setCreated(null);
        return  mapper.updateByPrimaryKeySelective(record);
    }
    /**
     *
     * @param record
     * @return 更新一条数据信息
     */
    public int update(T record){
        record.setUpdated(new Date());
        return  mapper.updateByPrimaryKey(record);
    }
    /**
     * 根据主键删除一条数据（物理删除）
     * @param id
     * @return
     */
    public int deleteById(Object id){
        return mapper.deleteByPrimaryKey(id);
    }
    /**
     * 根据主键的id删除多条数据
     * @param ids
     * @param clazz
     * @param id
     * @return
     */
    public int deleteByIds(List<Object> ids,Class clazz,String id){
        Example example = new Example(clazz);
        example.createCriteria().andIn(id, ids);
        return mapper.deleteByExample(example);
    }

    public int deleteByWhere(T record){
        return mapper.delete(record);
    }



}
