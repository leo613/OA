package com.qd.oa.common.util.pageTage;

import com.qd.oa.common.constant.ContstanUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;

public class ShiroTag  extends TagSupport {
     private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int doStartTag() throws JspException {

        List<String> operas=(List<String>)ContstanUtils.getsession().getAttribute("moduleOperas");

       //判断是否有值
        if (operas.indexOf(value)!=-1){
            //显示标签
            return EVAL_PAGE;
        }else{
            //不显示
            return SKIP_BODY;
        }

    }
}
