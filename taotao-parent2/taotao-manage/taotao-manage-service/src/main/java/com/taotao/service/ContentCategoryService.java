package com.taotao.service;

import com.taotao.pojo.ContentCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentCategoryService extends BaseService<ContentCategory> {

    /**
     * 新增分类管理(子节点)
     *
     */
    public Boolean saveContentCategory(ContentCategory contentCategory) {
        if (contentCategory!=null){
            //保存子节点信息
            contentCategory.setStatus(1);
            contentCategory.setIsParent(false);
            contentCategory.setSortOrder(1);
            contentCategory.setId(null);
            int i = saveSelective(contentCategory);

            //判断父节点isParent是否为true,不是需要改为true
            ContentCategory parent = queryById(contentCategory.getParentId());
            if (!parent.getIsParent()){
                parent.setIsParent(true);
                updateSelective(parent);
            }

            return i==1;
        }
        return false;
    }
    /**
     * 第一种情况
     *  删除子节点 并更改父节点isparent为false
     *
     *  第二中情况
     *  删除中间节点 子节点删除 更改父节点isParent 为false
     */
    public Boolean deleteContentCategory(ContentCategory contentCategory) {
        List<Object> ids=new ArrayList<>();
        ids.add(contentCategory.getId());
        //根据id删除当前节点以及其底下的子节点和孙子几点
        ids.add(contentCategory.getId());
        findAllSubNode(ids,contentCategory.getId());
        int id = deleteByIds(ids, ContentCategory.class, "id");
        //判断父节点上是否还有子节点，如果没有，则将父节点的isparent设置为false
        ContentCategory record = new ContentCategory();
        record.setParentId(contentCategory.getParentId());
        List<ContentCategory> list = queryListByWhere(record);
        if(null == list || list.size() == 0){
            ContentCategory category = new ContentCategory();
            category.setId(contentCategory.getParentId());
            category.setIsParent(false);
             updateSelective(category);
        }
         return id>=1;
    }

    private void findAllSubNode(List<Object> ids, Long id) {
        ContentCategory record=new ContentCategory();
        record.setParentId(id);
        //查询所有的子节点
        List<ContentCategory> list = super.queryListByWhere(record);
        for (ContentCategory contentCategory : list) {
            ids.add(contentCategory.getId());
            // 判断该节点是否为父节点，如果是，继续调用该方法查找子节点
            if (contentCategory.getIsParent()){
                findAllSubNode(ids,contentCategory.getId());
            }
        }
    }
}
