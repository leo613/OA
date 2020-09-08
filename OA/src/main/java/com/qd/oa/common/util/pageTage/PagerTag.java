package com.qd.oa.common.util.pageTage;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Todo 标签处理类,分页相关业务逻辑都是再标签处理类中完成
 */
public class PagerTag extends TagSupport {
    private int pageIndex;//Todo 当前页码
    private  int pageSize;//Todo 每页显示行数
    private  int totalNum;//Todo 总记录数
    private String submitUrl;//Todo 提交路径地址
    private String pageStyle;//todo pageStyle:页码样式


    @Override
    public int doStartTag() throws JspException {
        JspWriter jspWriter = this.pageContext.getOut();
        StringBuffer sbf=new StringBuffer();
        StringBuffer paper=new StringBuffer();
        String jumpUrl;
    try {
    if (this.totalNum>0){
           //todo 计算总页数
            int totalPageSum=this.totalNum%this.pageSize==0?this.totalNum/this.pageSize:(this.totalNum/this.pageSize)+1;
          //todo 如果当前页为首页
      if(this.getPageIndex()==1){
            //TODO 上一页补不能点击
                paper.append("<span class='disabled'>上一页</span>");
            //TODO 计算中间页
                  calcMiddlePage(paper,totalPageSum);
            //TODO 如果总共只有一页
                if (totalPageSum==1){
                    //todo 下一页不能点击
                    paper.append("<span class='disabled'>下一页</span>");
                }else{
                    //todo 计算下一页
                    jumpUrl= this.submitUrl.replace("{0}",String.valueOf(this.pageIndex+1));
                    paper.append("<a id='nextPage' href='"+jumpUrl+"'>下一页</a>");
                }
        //todo 如果当前页未尾页
       }else if(this.getPageIndex()==totalPageSum){
             //todo 计算上一页
          jumpUrl=this.submitUrl.replace("{0}",String.valueOf(this.pageIndex-1));
          paper.append("<a href='"+jumpUrl+"'>上一页</a>");
            //todo 计算中间页
          calcMiddlePage(paper,totalPageSum);
            //todo 下一页不能点击
          paper.append("<span class='disabled'>下一页</span>");

          //todo 计算中间页码
      }else{
          //todo 计算上一页
          jumpUrl=this.submitUrl.replace("{0}",String.valueOf(this.pageIndex-1));
          paper.append("<a  id='nextPage' href='"+jumpUrl+"'>上一页</a>");

          //todo 计算中间页
          calcMiddlePage(paper,totalPageSum);

          //todo 计算下一页
          jumpUrl= this.submitUrl.replace("{0}",String.valueOf(this.pageIndex+1));
          paper.append("<a id='nextPage' href='"+jumpUrl+"'>下一页</a>");
      }
        //todo 計算開始行號及結束行號
        int StartSize = (this.pageIndex-1) * this.pageSize+1;
        int EndSize=this.pageIndex==this.pageSize?totalPageSum:this.pageIndex*this.pageSize;

        sbf.append("<table class='"+pageStyle+"'style='align:center;width:100%'><tr><td>"+paper.toString()+"</td></tr>");
        sbf.append("<tr><td>跳转到<input type='text' id='jupm_num' size='4'/>页&nbsp;<input type='button' value='跳转' id='jupm_but'/></td></tr>");
        sbf.append("<tr><td>总共<font color='red'>"+this.totalNum+"</font>条记录,当前显示"+StartSize+"-"+EndSize+"条记录</td></tr>");

        sbf.append("<script type='text/javascript'>");
        sbf.append("document.getElementById('jupm_but').onclick = function(){");
        sbf.append("var value = document.getElementById('jupm_num').value;");
        sbf.append("if(!/^[1-9]\\d*$/.test(value)||value > "+this.totalNum+"){");
        sbf.append("alert('请输入[1-"+totalPageSum+"]之间的页码值！');");
        sbf.append("}else{");
        // index.action?pageIndex = {0}
        sbf.append("var submiturl = '"+this.submitUrl+"';");
        sbf.append("submiturl = submiturl.replace('{0}',value);");
        sbf.append("window.location = submiturl;");


        sbf.append("}");
        sbf.append("}");
        sbf.append("</script>");
    }else{
            sbf.append("<table class='"+pageStyle+"' style='align:center;width:100%;'><tr><td>总共<font color='red'>0</font>条记录,当前显示0-0条记录</td></tr></table>");
    }

            jspWriter.write(sbf.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.doEndTag();
    }

    /**
     * TODO 计算中间页
     * @param paper
     * @param totalPageSum
     */
    private void calcMiddlePage(StringBuffer paper, int totalPageSum) {
        //定義跳轉地址
        String jumpUrl;
        //TODO  如果总页码小于等于10 则全部显示不需要“...”
        if (totalPageSum<=10){
            //todo 循环展示各个页码
            for (int i = 1; i < totalPageSum; i++) {
               //todo 如果当前页不可点击
                if(this.getPageIndex()==i){
                    paper.append("<span class='current'>"+i+"</span>");
                }else{
                 //todo 其他页面可点击
                    jumpUrl=this.submitUrl.replace("{0}",String.valueOf(i));
                    paper.append("<a href='"+jumpUrl+"'>"+i+"</a>");
                }
            }
            //todo 点击页码小于8 样式为 1 2 3 4 5 6 7 8 9 ... 100
        }else if(this.getPageIndex()<=8){
              //todo 循环展示页码
              for (int i=1;i<=9;i++){
                  //todo 如果当前页不可点击
                  if (this.getPageIndex()==i){
                      paper.append("<span class='current'>"+i+"</span>");
                  }else{
                      //todo 其他页面可点击

                      jumpUrl=this.submitUrl.replace("{0}",String.valueOf(i));

                      paper.append("<a href='"+jumpUrl+"'>"+i+"</a>");
                  }

              }
               //todo 拼装 ... 100
               paper.append("...");
               jumpUrl=this.submitUrl.replace("{0}",String.valueOf(totalPageSum));
               paper.append("<a href='"+jumpUrl+"'>"+totalPageSum+"</a>");

            //todo 点击页码靠近尾页 样式为1... 91 92 93 94 95 96 97 98 100
        }else if (this.getPageIndex()+7>=totalPageSum){
            //todo 拼装 1...
            jumpUrl=this.submitUrl.replace("{0}",String.valueOf(1));
            paper.append("<a href='"+jumpUrl+"'>"+1+"</a>");
            paper.append("...");

              for(int i=totalPageSum-8;i<=totalPageSum;i++){
                  //todo 如果当前页不可点击
                  if (this.getPageIndex()==i){
                      paper.append("<span class='current'>"+i+"</span>");
                  }else{
                      //todo 其他页面可点击
                      jumpUrl=this.submitUrl.replace("{0}",String.valueOf(i));
                      paper.append("<a href='"+jumpUrl+"'>"+i+"</a>");
                  }
              }
            // TODO: 点击中间页码样式为  1 ... 5 6 7 6 8 9 10 11 12 13 ... 100
        }else {
            //todo 拼装 1...
            jumpUrl=this.submitUrl.replace("{0}",String.valueOf(1));
            paper.append("<a href='"+jumpUrl+"'>"+1+"</a>");
            paper.append("...");

              for (int i=this.getPageIndex()-4;i<=this.getPageIndex()+4;i++){
                  //todo 如果当前页不可点击
                  if (this.getPageIndex()==i){
                      paper.append("<span class='current'>"+i+"</span>");
                  }else{
                      //todo 其他页面可点击
                      jumpUrl=this.submitUrl.replace("{0}",String.valueOf(i));
                      paper.append("<a href='"+jumpUrl+"'>"+i+"</a>");
                  }
              }

            //todo 拼装 ... 100
            paper.append("...");
            jumpUrl=this.submitUrl.replace("{0}",String.valueOf(totalPageSum));
            paper.append("<a href='"+jumpUrl+"'>"+totalPageSum+"</a>");

        }

    }


    public int getPageIndex() {

        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        if (pageIndex==0 || "".equals(pageIndex)){
            pageIndex=1;
        }
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }

    public String getPageStyle() {
        return pageStyle;
    }

    public void setPageStyle(String pageStyle) {
        this.pageStyle = pageStyle;
    }




}
