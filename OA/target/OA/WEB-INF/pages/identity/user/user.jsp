<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" %>
<%@include file="../../common/taglib.jsp"%>

<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
<%@taglib uri="/oa/pager" prefix="oa" %>
<link href="${ctx}/css/pager.css" rel="stylesheet">
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title>OA办公管理系统-用户管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
<meta http-equiv="description" content="This is my page" />
<link rel="stylesheet"
	href="${ctx }/resources/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="${ctx}/resources/easyUI/easyui.css" />
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
	<script type="text/javascript" src="${ctx}/resources/blockUI/jquery.blockUI.js"></script>
	<script type="text/javascript" src="${ctx}/JS/operaPageCss.JS"></script>
<script type="text/javascript">
	$(function () {
	    //todo 1.提示信息
        if("${message}"){
            $.messager.alert('提示信息',"${message}",'warning');
        }

        // //todo 2.为全选绑定点击事件
		// $("#checkAll").click(function () {
		//     //获取checkAll属性
		// 	var checked=this.checked;
		// 	$("input[name='box']").each(function () {
		// 	   this.checked=checked;
        //     });
        // });

        //todo 3.发送异步请求加载部门以及职位信息
        $.ajax({
            type:"POST",
            url:"${ctx}/user/ajaxLoadDeptAndJob.jspx",//请求地址
            dataType:"json",//预期服务器返回的数据格式
            success:function (data) {

               var depts=data.depts;
               var jobs=data.jobs;
               $.each(depts,function (key,value) {
                   <!--$("<option>").val(key).text(value).prop("selected",key=="${user.dept.id}").appendTo("#deptSelect");-->
                   $("#deptSelect").append("<option value='"+key+"'>"+value+"</option>");
               });

               $.each(jobs,function (key,value) {
				   <%--$("<option>").val(key).text(value).prop("sselected",key=="${user.job.code}").appendTo("#jobSelect");--%>
                   $("#jobSelect").append("<option value='"+key+"'>"+value+"</option>");
			   });

            },error:function () {
                $.messager.alert('提示信息',"网络异常！",'warning');
            }
        });

        //todo 4.添加查询按钮功能
		  $("#selectUser").click(function () {
			  ${pageContext.session.removeAttribute("message")}
              var name=$("#name").val();
              var phone=$("#phone").val();
              var deptId=$("#deptSelect option:selected").val();
              var jobCode=$("#jobSelect option:selected").val();
              var  findInfo={"name":name,"phone":phone,"job.code":jobCode,"dept.id":deptId};//传递至后台的参数
              $.ajax({
                 type:"POST",//请求方式
				  url:"${ctx}/user/selectUser.jspx",//请求地址
				  data:findInfo,
                  dataType:"text",//预期服务器返回的数据的格式
				  success:function (msg) {//后台响应成功时候的回调函数
                      <%--if(msg){--%>
                          <%--alert(msg);--%>
                          <%--window.location="${ctx}/identity/user/user?user=${users}";--%>
					  <%--}--%>
                  },error:function () {
                      $.messager.alert('提示信息',"网络异常！",'warning');
                  }
			  });
          });

        //Todo 5.为添加按钮添加绑定点击事件
		$("#addUser").click(function () {
            ${pageContext.session.removeAttribute("message")}

			$("#divDialog").dialog({
				title:"添加用户",//标题
				width:580,//宽度
				height:400,//高度
                maximizable:true,//最大化
                minimizable:false,//最小化
				collapsible:true,//可收缩
				modal:true,//模拟窗口
				onClose:function () {//关闭窗口
				    window.location="${ctx}/user/selectUser.jspx";
                }

			});
            //通过iframe加载添加用户信息页面
            $("#iframe").prop("src","${ctx}/user/showAddUser.jspx")
        });

        //Todo 5.为删除按钮添加绑定点击事件
		$("#deleteUser").click(function () {
            ${pageContext.session.removeAttribute("message")}
		     //todo 方法一
            // $("input[name='box']").each(function () {
            //    var checked =this.checked;
            //    if (checked){
            //       var value= this.value;
			 //   }
              //  });

			var boxes=$("[id^='box_']").filter(":checked");
               if (boxes==null||boxes.length==0){
                   $.messager.alert('提示信息',"请选择需要删除的用户信息！",'warning');
			   } else{
                   //提示用户
                   $.messager.confirm('提示信息',"请确认是否真的要删除该用户信息？",function (r) {
					   if (r){
					       var array=new Array();
					       //获取用户信息
						   boxes.each(function () {
							   array.push(this.value);
                           });
                           window.location="${ctx}/user/deleteUserByIds.jspx?userIds="+array;
					   }
                   });
			   }
        });

//todo 6.激活按钮
 var t= $("[id^='checkUser_']");
        t.each(function () {
            var userId="checkUser_"+this.value;
            $("#"+userId).switchbutton({
				onChange:function (checked) {
                   var userId= this.value;
					if (checked){
                        $.messager.confirm('提示信息',"请确认是否激活账户？",function (r) {
                            window.location="${ctx}/user/AccountChange.jspx?userId="+userId;
                        });
                    } else {
                        $.messager.confirm('提示信息',"请确认是否冻结账户？",function (r) {
                            window.location="${ctx}/user/AccountChange.jspx?userId="+userId;
                        });
					}
                }
            });
	   });

    });

	//TODO 7.修改用户信息
	function updateUser(userId) {
		$("#divDialog").dialog({
			title:"修改用户信息",//标题
            cls:"easyui-dialog",//class
			width:680,//宽度
			height:400,//高度
			maximizable:true,//最大化
			minimizable:false,//最小化
			collapsible:true,//可收缩
			modal:true,//模拟窗口
			onClose:function () {//关闭窗口
				window.location="${ctx}/user/selectUser.jspx";
			}

		});

		//通过iframe加载修改用户信息页面
		$("#iframe").prop("src","${ctx}/user/showUpdateUser.jspx?userId="+userId);
	}

	//TODO 8.浏览用户信息
	function preUser(userId) {
		$("#divDialog").dialog({
			title:"浏览用户信息",//标题
			cls:"easyui-dialog",//class
			width:900,//宽度
			height:600,//高度
			maximizable:true,//最大化
			minimizable:false,//最小化
			collapsible:true,//可收缩
			modal:true,//模拟窗口
			onClose:function () {//关闭窗口
				window.location="${ctx}/user/selectUser.jspx";
			}

		});
		//通过iframe加载修改用户信息页面
		$("#iframe").prop("src","${ctx}/user/showOtherInfo.jspx?userId="+userId);
	}



</script>


</head>
<body style="overflow: hidden; width: 98%; height: 100%;" >
   	<!-- 工具按钮区 -->
	<form class="form-horizontal" 
			action="${ctx }/user/selectUser.jspx" method="post" style="padding-left: 5px;" >
			<table class="table-condensed">
					<tbody>
					<tr>
					   <td>
						<input name="name" id="name" type="text" class="form-control" placeholder="姓名" value="${user.name}" >
						</td>
						<td>	
						<input type="text" name="phone" id="phone" class="form-control" placeholder="手机号码" value="${user.phone}" >
						</td>
<!-- 						<td>	 -->
<!-- 						   <input type="text" class="form-control" placeholder="状态"> -->
<!-- 						</td> -->
						<td>	
						<select class="btn btn-default" placeholder="部门" id="deptSelect" name="dept.id">
							<option value="0">==请选择部门==</option>
						</select>
						</td>
						<td>	
						<select class="btn btn-default" placeholder="职位" id="jobSelect" name="job.code">
							<option value="0">==请选择职位==</option>
						</select>
						</td>
						<td>
						<button  type="submit" id="selectUser"  class="btn btn-info"><span class="glyphicon glyphicon-search"></span>&nbsp;查询</button>
							<oa:hasPremiss value="user:addUser"><a  id="addUser" class="btn btn-success"><span class="glyphicon glyphicon-plus"></span>&nbsp;添加</a></oa:hasPremiss>
					        <oa:hasPremiss value="user:deleteUser"><a  id="deleteUser" class="btn btn-danger"><span class="glyphicon glyphicon-remove"></span>&nbsp;删除</a></oa:hasPremiss>
					 </td>
					</tr>
					</tbody>
				</table>
		</form>
 		<div class="panel panel-primary" style="padding-left: 10px;">
 			<div class="panel-heading" style="background-color: #11a9e2;">
				<h3 class="panel-title">用户信息列表</h3>
			</div>
			<div class="panel-body" >
				<table class="table table-bordered">
					<thead>
						<tr style="font-size: 12px;" align="center">
							<th style="text-align: center;"><input id="checkAll" type="checkbox" /></th>
							<th style="text-align: center;">账户</th>
							<th style="text-align: center;">姓名</th>
							<th style="text-align: center;">性别</th>
							<th style="text-align: center;">部门</th>
							<th style="text-align: center;">职位</th>
							<th style="text-align: center;">手机号码</th>
							<th style="text-align: center;">邮箱</th>
							<th style="text-align: center;">激活状态</th>
							<th style="text-align: center;">创建人</th>
							<th style="text-align: center;">操作</th>
						</tr>
					</thead>
					  <c:forEach items="${users}" var="user" varStatus="start">
						  <tr style="font-size: 14px;" align="center" id="dataTr_${stat.index}">
							  <th style="text-align: center;"><input type="checkbox" name="box" value="${user.userId}" id="box_${start.index}"/></th>
							  <th style="text-align: center;">${user.userId}</th>
							  <th style="text-align: center;">${user.name}</th>
							  <th style="text-align: center;">${user.sex}</th>
							  <th style="text-align: center;">${user.dept.name}</th>
							  <th style="text-align: center;">${user.job.name}</th>
							  <th style="text-align: center;">${user.phone}</th>
							  <th style="text-align: center;">${user.email}</th>
							  <th style="text-align: center;">
								  <c:if test="${user.status==1}">
									  <input id="checkUser_${user.userId }" value="${user.userId }" name="status" class="easyui-switchbutton" data-options="onText:'激活',offText:'冻结'"  onchange="checkState()" checked>
								  </c:if>

								  <c:if test="${user.status!=1}">
									  <input id="checkUser_${user.userId }" value="${user.userId }" name="status"  class="easyui-switchbutton" data-options="onText:'激活',offText:'冻结'" onchange="checkState()">

								  </c:if>
							  </th>
							  <th style="text-align: center;">${user.creater.name}</th>
							  <th style="text-align: center;">
								  <oa:hasPremiss value="user:selectUser"><span id="preUser_${stat.index}" class="label label-success"><a href="javascript:preUser('${user.userId}')"  style="color: white;">预览</a></span></oa:hasPremiss>
								  <oa:hasPremiss value="user:updateUser">	<span id="updateUser_${stat.index}"  class="label label-info"><a href="javascript:updateUser('${user.userId}')" style="color: white;">修改</a></span></oa:hasPremiss>
							  </th>
						  </tr>
					  </c:forEach>

				</table>


		</div>
			<!-- 分页标签区 -->
			<oa:pager pageIndex="${pageModel.pageIndex}" pageSize="${pageModel.pageSize}" totalNum="${pageModel.totalNum}" pageStyle="megas512" submitUrl="${ctx}/user/selectUser.jspx?pageIndex={0}"/>

		<div id="divDialog" style="display: none;" >
			 <!-- 放置一个添加用户的界面  -->
			 <iframe id="iframe" frameborder="0" style="width: 100%;height: 100%;"></iframe>

		</div>
</body>
</html>