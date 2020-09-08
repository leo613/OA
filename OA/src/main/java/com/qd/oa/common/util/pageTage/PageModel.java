package com.qd.oa.common.util.pageTage;

/**
 * 分页实体类
 */
public class PageModel  {
    /**
     * 总记录数
     */
    private Integer totalNum;
    /** 当前页面 */
    private int pageIndex=1 ;

    /** 每页分多少条数据   */
    private int pageSize = 8;

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getFirstLimitParam() {
        return (this.getPageIndex()-1)*this.getPageSize() ;
    }

}
