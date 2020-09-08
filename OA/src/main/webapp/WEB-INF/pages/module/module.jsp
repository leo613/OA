 <%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%@include file="../common/taglib.jsp"%>

 <c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
 <%@taglib uri="/oa/pager" prefix="oa" %>
 <link href="${ctx}/css/pager.css" rel="stylesheet">
 <html>
<head>
<title>OA办公管理系统-用户管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
<meta http-equiv="description" content="This is my page" />
<link rel="stylesheet" href="${ctx }/resources/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript" src="${ctx }/script/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${ctx }/script/jquery-migrate-1.2.1.min.js"></script>
<!-- 导入bootStrap的库 -->
<script type="text/javascript" src="${ctx}/resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/easyUI/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/easyUI/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" href="${ctx}/resources/easyUI/easyui.css">
	<script type="text/javascript" src="${ctx}/JS/operaPageCss.JS"></script>
<script type="text/javascript">
	$(function () {

		if("${message}"){
			$.messager.alert('提示信息',"${message}",'warning');
		}

		//TODO 如果是一级模块则隐藏返回按钮
		var code=new Array();
		<c:forEach items="${modules}" var="module" varStatus="stat">
		  var object=new Object();
		  object.value="${module.code}";
	   	code.push(object);
		</c:forEach>

		var codes=new Array();
		$.each(code,function () {
			codes.push(this.value);
			if (this.value.length==4){
				$("#back").hide();//隐藏标签
			}
		});


		//TODO 返回
		$("#back").click(function(){
					window.history.go(-1);
		});

		//TODO 添加菜单
		$("#addModule").click(function(){
			$("#divDialog").dialog({
				title:"添加菜单信息",//标题
				cls:"easyui-dialog",//class
				width:680,//宽度
				height:400,//高度
				maximizable:true,//最大化
				minimizable:false,//最小化
				collapsible:true,//可收缩
				modal:true,//模拟窗口
				onClose:function () {//关闭窗口
					window.location="${ctx}/menu/getModulesByPcode.jspx?code=${showCode}";
				}
			});
			//通过iframe加载修改用户信息页面
			$("#iframe").prop("src","${ctx}/menu/showAddMenu.jspx?codes="+codes);
		});

		//TODO 删除菜单
		$("#deleteModule").click(function () {
			var boxes = $("[id^='box_']").filter(":checked");
			if (boxes.length==null|| boxes.length==0){
				$.messager.alert('提示信息',"请选择需要删除的菜单信息！",'warning');
			}else {
				$.messager.confirm('提示信息','是否确认删除该菜单信息?',function (r) {
				         if (r){
							 //获取需要删除的用户信息
							 var array=new Array();
							 for(var i=0;i<boxes.length;i++){
								 array.push(boxes[i].value);
							 }
							 window.location="${ctx}/menu/deleterMenuByIds.jspx?codes="+array;
						 }
				})
			}

		})
	});

	function updateModule(code) {
		$("#divDialog").dialog({
			title:"添加菜单信息",//标题
			cls:"easyui-dialog",//class
			width:680,//宽度
			height:400,//高度
			maximizable:true,//最大化
			minimizable:false,//最小化
			collapsible:true,//可收缩
			modal:true,//模拟窗口
			onClose:function () {//关闭窗口
				window.location="${ctx}/menu/getModulesByPcode.jspx?code=${showCode}";
			}
		});
		//通过iframe加载修改用户信息页面
		$("#iframe").prop("src","${ctx}/menu/showUpdateMenu.jspx?code="+code);
	}

</script>

</head>
<body style="overflow: hidden; width: 98%; height: 100%;">
	<div>
	
		<div class="panel panel-primary">
			<!-- 工具按钮区 -->
			<div style="padding: 5px;">
				 <a  id="back" class="btn btn-primary"><span class="glyphicon glyphicon-hand-left"></span>&nbsp;返回</a>
				<a  id="addModule" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span>&nbsp;添加</a>
				<a  id="deleteModule" class="btn btn-danger"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除</a>
			</div>
			
			<div class="panel-body">
				<table class="table table-bordered" style="float: right;">
					<thead>
						<tr style="font-size: 12px;" align="center">
							<th style="text-align: center;"><input type="checkbox" id="checkAll"/></th>
							<th style="text-align: center;">编号</th>
							<th style="text-align: center;">名称</th>
							<th>备注</th>
							<th style="text-align: center;">链接</th>
							<th style="text-align: center;">操作</th>
							<th style="text-align: center;">创建日期</th>
							<th style="text-align: center;">创建人</th>
							<th>修改日期</th>
							<th style="text-align: center;">修改人</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					  <c:forEach items="${modules}" var="module" varStatus="stat">
				        <tr align="center" id="dataTr_${stat.index}">
							<td><input type="checkbox" name="box" id="box_${stat.index}" value="${module.code}"/></td>
							<td>${module.code}</td>
							<td>${module.name.replaceAll("-","")}</td>
 							<td>${module.remark}</td>
							<td>${module.url}</td>
							<td>
								<c:if test="${fn:length(module.code)!=12}">
								<span class="label label-success"><a href="${ctx}/menu/getModulesByPcode.jspx?code=${module.code}" style="color: white;">查看下级</a></span></td>
							    </c:if>
 							<td><fmt:formatDate value="${module.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
 							<td>${module.creater.name }</td>
 							<td><fmt:formatDate value="${module.modifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>${module.modifier.name }</td>
							<td><span class="label label-info"><a href="javascript:updateModule('${module.code}');" style="color: white;">修改</a></span></td>
						</tr>
				    </c:forEach>
				</table>


			</div>
			
		</div>
	</div>
	<!-- 分页标签区 -->
	<oa:pager pageIndex="${pageModel.pageIndex}" pageSize="${pageModel.pageSize}" totalNum="${pageModel.totalNum}" pageStyle="megas512" submitUrl="${ctx}/menu/selectMenu.jspx?pageIndex={0}"/>
		<div id="divDialog" style="display: none;" >
			 <!-- 放置一个添加用户的界面  -->
			 <iframe id="iframe" frameborder="0" style="width: 100%;height: 100%;"></iframe>
		</div>
	
</body>
</html>