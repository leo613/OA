package com.qd.oa.common.converter;

import com.github.pagehelper.StringUtil;
import com.qd.oa.common.exception.ExceptionCustom;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.util.Date;

public class StringToDate implements Converter<String,Date> {
    private static final String[]DEFAULT_PATTERNS=new String[]{"yyyy-MM-dd"};
    private String[]patterns=DEFAULT_PATTERNS;

    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }

    @Override
    public Date convert(String source) {
         Date date=null;
        try {
            if(StringUtils.isNotBlank(source)){
               date=DateUtils.parseDate(source,this.patterns);
            }else{
                throw new ExceptionCustom("空日期");
            }
        } catch (ParseException e) {
            throw new ExceptionCustom("日期格式轉換錯誤");
        }
        return date;
    }
}
