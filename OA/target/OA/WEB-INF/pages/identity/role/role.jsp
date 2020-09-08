<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="../../common/taglib.jsp"%>
<%@taglib uri="/oa/pager" prefix="oa" %>
<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>

<link href="${ctx}/css/pager.css" rel="stylesheet">
<html>
<head>
	<title>OA办公管理系统-角色管理</title>
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
	<link rel="stylesheet" href="${ctx}/resources/easyUI/easyui.css" />
	<script type="text/javascript" src="${ctx}/resources/blockUI/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${ctx}/JS/operaPageCss.JS"></script>
	<script type="text/javascript">
	$(function () {
        if("${message}"){
            $.messager.alert('提示信息',"${message}",'warning');
        }

        //todo 1.增加角色
        $("#addRole").click(function () {
            $("#divDialog").dialog({
                title: '添加角色', //标题
                width: 580,  //宽度
                height: 400,  //高度
                maximizable: true,// 最大化
                minimizable:false,//最小化
                collapsible:true,//可伸缩
                modal:true,//模态窗口
                onClose: function () {//关闭窗口
                    window.location="${ctx}/role/selectRole.jspx?pageIndex=${pageModel.pageIndex}";
                }
            });
            //通过iframe加载添加用户信息页面
            $("#iframe").prop("src","${ctx}/role/showAddRole.jspx")
        });



    //Todo 1.为删除绑定按钮绑定点击事件
        $("#deleteRole").click(function () {
            var boxes = $("[id^='box_']").filter(":checked");
            if (boxes.length==null || boxes.length==0){
                $.messager.alert('提示信息',"请选择需要删除的用户信息！",'warning');
            } else {
                //提示用户  是否争取删除
                $.messager.confirm('提示信息','是否确认删除该用户信息?',function (r) {
                    if (r){
                        //获取需要删除的用户信息
                        var array=new Array();
                        for(var i=0;i<boxes.length;i++){
                            array.push(boxes[i].value);
                        }
                        window.location="${ctx}/role/deleteroleByIds.jspx?roleIds="+array;
                    }
                });
            }
        });
	});
      function updateRole(roleId) {
          $("#divDialog").dialog({

              title : "修改角色", // 标题
              cls : "easyui-dialog", // class
              width : 480, // 宽度
              height : 255, // 高度
              maximizable : true, // 最大化
              minimizable : false, // 最小化
              collapsible : true, // 可伸缩
              modal : true, // 模态窗口
              onClose : function(){ // 关闭窗口
                  //重新进行分页查询
                  window.location="${ctx}/role/selectRole.jspx?pageIndex=${pageModel.pageIndex}";

              }
          });
          //通过iframe加载修改角色信息页面
          $("#iframe").prop("src","${ctx}/role/showUpdateRole.jspx?id="+roleId);
      }


	</script>
</head>
<body style="overflow: hidden;width: 100%;height: 100%;padding: 5px;">
	<div>
		<div class="panel panel-primary">
			<!-- 工具按钮区 -->
			<div style="padding-top: 4px;padding-bottom: 4px;">
				<oa:hasPremiss value="role:addRole"><a  id="addRole" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span>&nbsp;添加</a></oa:hasPremiss>
				<oa:hasPremiss value="role:deleteRole"><a  id="deleteRole" class="btn btn-danger"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除</a></oa:hasPremiss>
			</div>
			
			<div class="panel-body">
				<table class="table table-bordered" style="float: right;">
					<thead>
					    <tr>
						<th style="text-align: center;"><input type="checkbox" id="checkAll"/></th>
						<th style="text-align: center;">名称</th>
						<th style="text-align: center;">备注</th>
						<th style="text-align: center;">操作</th>
						<th style="text-align: center;">创建日期</th>
						<th style="text-align: center;">创建人</th>
						<th style="text-align: center;">修改日期</th>
						<th style="text-align: center;">修改人</th>
						<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					<c:forEach items="${roles}" var="role" varStatus="stat">
				         <tr align="center" id="dataTr_${stat.index}">
							<td><input type="checkbox" name="box" id="box_${stat.index}" value="${role.id}"/></td>
							<td>${role.name}</td>
							<td>${role.remark}</td>
							 <td><oa:hasPremiss value="role:bandUser"><span class="label label-success"><a href="${ctx}/role/selectRoleUser.jspx?roleId=${role.id}&pageIndex=${pageModel.pageIndex}" style="color: white;">绑定用户</a></span></oa:hasPremiss>&nbsp;
								<oa:hasPremiss value="role:bandmgr"><span class="label label-info"><a href="${ctx}/popedom/mgrPopedom.jspx?id=${role.id}" style="color: white;">绑定操作</a></span></oa:hasPremiss></td>
							<td><fmt:formatDate value="${role.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>${role.creater.name}</td>
							<td><fmt:formatDate value="${role.modifyDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>${role.modifier.name}</td>
							 <td><oa:hasPremiss value="role:updateRole">  <span class="label label-info"><a href="javascript:updateRole('${role.id}')">修改</a></span></oa:hasPremiss></td>
						</tr>
		   			 </c:forEach>
				</table>


			</div>
			
		</div>
	</div>
    <!-- div作为弹出窗口 -->
    <%--<div id="divDialog" style="overflow: hidden;">--%>
		<%--<iframe id="iframe" scrolling="no" frameborder="0" width="100%" height="100%" style="display:none;"></iframe>--%>
	<%--</div>--%>
	<%----%>
	<!-- 分页标签区 -->
	<oa:pager pageIndex="${pageModel.pageIndex}" pageSize="${pageModel.pageSize}" totalNum="${pageModel.totalNum}" pageStyle="megas512" submitUrl="${ctx}/role/selectRole.jspx?pageIndex={0}"/>
	<div id="divDialog" style="display: none;" >
		<!-- 放置一个添加用户的界面  -->
		<iframe id="iframe" frameborder="0" style="width: 100%;height: 100%;"></iframe>
</body>
</html>