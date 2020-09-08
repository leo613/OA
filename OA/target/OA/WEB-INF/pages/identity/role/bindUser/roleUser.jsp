<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../../common/taglib.jsp"%>
<%@taglib uri="/oa/pager" prefix="oa" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
<link href="${ctx}/css/pager.css" rel="stylesheet" />
<html>
<head>
<title>OA办公管理系统-用户管理</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
	<meta http-equiv="description" content="This is my page" />
	<link href="${ctx}/fkjava.ico" rel="shortcut icon" type="image/x-icon" />
	<link rel="stylesheet"
		  href="${ctx }/resources/bootstrap/css/bootstrap.min.css" />
	<script type="text/javascript"
			src="${ctx }/script/jquery-1.11.0.min.js"></script>
	<script type="text/javascript"
			src="${ctx }/script/jquery-migrate-1.2.1.min.js"></script>
	<!-- 导入bootStrap的库 -->
	<script type="text/javascript"
			src="${ctx}/resources/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript"
			src="${ctx}/resources/easyUI/jquery.easyui.min.js"></script>
	<script type="text/javascript"
			src="${ctx}/resources/easyUI/easyui-lang-zh_CN.js"></script>
	<link rel="stylesheet" href="${ctx}/resources/easyUI/easyui.css">
	<script type="text/javascript" src="${ctx}/resources/blockUI/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${ctx}/JS/operaPageCss.JS"></script>
	<script type="text/javascript">

        $(function(){
            if("${message}"){
                $.messager.show({
                    title:'提示信息',
                    msg:"<font color='red'>${message}</font>",
                    showType:'show'
                });
            }
            //为解绑按钮绑定点击事件
            $("#unBindUser").click(function(){
                //获取选中的用户信息
                var boxes = $("input[id^='box_']:checked");
                if(boxes.length == 0){
                    $.messager.alert("提示信息","请选择需要解绑的用户！","warning");
                }else{
                    //定义数组用于封装用户的账号
                    var arr = new Array();
                    $.each(boxes,function(){
                        arr.push(this.value);
                    });
                    window.location="${ctx}/role/unBindUser.jspx?roleId=${role.id}&userIds="+arr;
                }
            });

            $("#bindUser").click(function () {
                $("#divDialog").dialog({
                    title: '绑定用户', //标题
                    width: 780,  //宽度
                    height: 500,  //高度
                    maximizable: true,// 最大化
                    minimizable:false,//最小化
                    collapsible:true,//可伸缩
                    modal:true,//模态窗口
                    onClose: function () {//关闭窗口
                        window.location="${ctx}/role/selectRoleUser.jspx?roleId=${role.id}";
                    }
                });
                //通过iframe加载添加用户信息页面
                $("#iframe").prop("src","${ctx}/role/findUnBindUser.jspx?roleId=${role.id}");
            });

            //返回
            $("#back").click(function(){
				window.location="${ctx}/identity/role/selectRole.jspx";
            });
        });
        function backRoles() {
            //发送请求 返回至角色查询页面
            window.location = "${pageContext.request.contextPath}/role/selectRole.jspx";
        }
	</script>


</head>
<body style="overflow: hidden; width: 98%; height: 100%;" >
		<!-- 工具按钮区 -->
		
 		<div class="panel panel-primary" style="padding-left: 5px;">
 			<div style="padding-top: 4px;padding-bottom: 4px;">
				<a  id="back" href="javascript:backRoles();" class="btn btn-primary"><span class="glyphicon glyphicon-hand-left"></span>&nbsp;返回</a>
				<a  id="bindUser" class="btn btn-success"><span class="glyphicon glyphicon-copy"></span>&nbsp;绑定用户</a>
				<a  id="unBindUser" class="btn btn-danger"><span class="glyphicon glyphicon-paste"></span>&nbsp;解绑用户</a>
			</div>
 			<div class="panel-heading" style="background-color: #11a9e2;">
				<h3 class="panel-title"><label style="color: white;font-size: 15px;"><span class="glyphicon glyphicon-user"></span>&nbsp;${role.name}</label></h3>
			</div>
			<div class="panel-body" >
				<table class="table table-bordered">
					<thead>
						<tr style="font-size: 12px;" align="center">
							<th style="text-align: center;"><input id="checkAll"
								type="checkbox" /></th>
							<th style="text-align: center;">账户</th>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">性别</th>
							<th style="text-align: center;">部门</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">手机号码</th>
							<th style="text-align: center;">邮箱</th>
							<th style="text-align: center;">审核人</th>
						</tr>
					</thead>
					<c:forEach items="${requestScope.users}" var="user"
						varStatus="stat">
						<tr id="dataTr_${stat.index}" align="center">
							<td><input type="checkbox" name="box" id="box_${stat.index}"
								value="${user.userId}" /></td>
							<td>${user.userId}</td>
							<td>${user.name}</td>
							<td>${user.sex}</td>
							<td>${ user.dept.name}</td>
							<td>${ user.job.name}</td>
							<td>${user.phone}</td>
							<td>${user.email}</td>
							<td>${user.checker.name}</td>
						</tr>
					</c:forEach>
				</table>

		</div>

			<!-- 分页标签区 -->
			<oa:pager pageIndex="${pageModel.pageIndex}" pageSize="${pageModel.pageSize}" totalNum="${pageModel.totalNum}" pageStyle="megas512" submitUrl="${ctx}/role/selectRoleUser.jspx?roleId=${role.id}&pageIndex={0}"/>
		<div id="divDialog" style="display: none;" >
			 <!-- 放置一个添加用户的界面  -->
			 <iframe id="iframe" frameborder="0" style="width: 100%;height: 100%;"></iframe>
		</div>

</body>
</html>