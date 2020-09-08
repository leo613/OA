package com.taotao.web.bean;

import com.taotao.pojo.Item;
import org.apache.commons.lang3.StringUtils;

public class ItemVo extends Item {
    public String[]getImages(){
        return StringUtils.split(super.getImage(),"," );
    }
}
