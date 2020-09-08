<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../../common/taglib.jsp"%>
<%@taglib uri="/oa/pager" prefix="oa" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
<link href="${ctx}/css/pager.css" rel="stylesheet">
<html>
<head>
<title>办公管理系统-菜单管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta name="Keywords" content="keyword1,keyword2,keyword3" />
<meta name="Description" content="网页信息的描述" />
<meta name="Author" content="fkjava.org" />
<meta name="Copyright" content="All Rights Reserved." />
<link href="${ctx}/fkjava.ico" rel="shortcut icon" type="image/x-icon" />

<link rel="stylesheet" href="${ctx}/resources/easyUI/easyui.css">
<script type="text/javascript" src="${ctx }/script/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${ctx }/script/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/easyUI/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/resources/dtree/dtree.css"/>
<script type="text/javascript" src="${ctx}/resources/dtree/dtree.js"></script>
<script type="text/javascript" src="${ctx}/JS/operaPageCss.JS"></script>
<script type="text/javascript">
	$(function () {
         d=new dTree('d',"${ctx}");
         d.add("0",-1,"系统模块");
         d.add("1","0","全部","javascript:void(0)",'全部');

         $.ajax({
			 type:"post",//指定路劲
			 dataType:'json',
			 url:"${ctx}/popedom/loadAllModule.jspx",//指定请求的地址
             success:function (result) {
                 $.each(result,function () {
                     if (this.id.length!=12){
                         //d.add("节点ID","父级节点ID","节点名称","跳转的地址","标题","跳转目的地显示的位置")
                         d.add(this.id,this.pid,this.name,this.id.length==4?"javascript:viod(0)":"${ctx}/popedom/loadThirdModule.jspx?id=${role.id}&name="+this.name+"&code="+this.id,this.name,"rightFrame");
                     }
                 });
                 $("#tree").html(d.toString());
             },error:function () {
                 $.messager.alert("提示信息","网络异常","warning");
             }
		 })

    })
</script>
</head>
    <body class="easyui-layout" style="width:100%;height:100%;">
			<div id="tree" data-options="region:'west'" title="菜单模块树" style="width:20%;padding:10px">
				 <!-- 展示所有的模块树  -->
			</div>
			
			<div data-options="region:'center'" title="模块菜单"  >
			     <!-- 展示当前模块下的子模块  -->
			     <iframe scrolling="auto"  frameborder="0" id="sonModules"  name="rightFrame" width="100%" height="100%" ></iframe>
			</div>
	</body>
</html>
