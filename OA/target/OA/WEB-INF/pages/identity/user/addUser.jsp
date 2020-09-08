<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"  isELIgnored="false" %>
<%@include file="../../common/taglib.jsp"%>
<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>办公管理系统-添加用户</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta name="Keywords" content="keyword1,keyword2,keyword3" />
<meta name="Description" content="网页信息的描述" />
<meta name="Author" content="fkjava.org" />
<meta name="Copyright" content="All Rights Reserved." />
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
<script type="text/javascript">

	$(function() {
        if("${message}"){
            $.messager.alert('提示信息',"${message}",'warning');
        }

        //todo 1.发送异步请求加载部门以及职位信息
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

     //todo 2.异步查询userId是否被占用并确认其格式
		$("#userId").blur(function () {
			var userId=$("#userId").val();

            var  datas={"userId":userId};
            //判断登录名称格式userId
            if (!/^\w{5,20}$/.test(userId)) {
                $.messager.alert('提示信息',"用户登录名长度必须在5-20之间!",'warning');
            }else {
                //异步处理登录名称是否被占用
			$.ajax({
				type:"POST",
				url:"${ctx}/user/onlyUserId.jspx",
                dataType:"text",
				data:datas,
				success:function (msg) {
				    if (msg){
                        $.messager.alert('提示信息',msg,'warning');
					}
                },error:function () {
                    $.messager.alert('提示信息',"网络异常！",'warning');
                }
			});
            }
        });
        //todo 3.确认name格式
        $("#name").blur(function () {
            //获取姓名
            var name = this.value;
            if ($.trim(name) == "") {
                msg = "姓名不能为空!";
                $.messager.alert('提示信息',msg,'warning');
            }
        });

        //todo 4.确认密码格式
        $("#password").blur(function () {
            //获取密码
            var password = this.value;
            if ($.trim(password) == "") {
                msg = "密码不能为空!";
                $.messager.alert('提示信息',msg,'warning');
            }else if (!/^\w{6,20}$/.test(password)) {
                msg = "密码长度必须为6-20之间!";
                $.messager.alert('提示信息',msg,'warning');
            }
        });


        //todo 5.密码double check
        $("#repwd").blur(function () {
            //获取密码
            var repwd = this.value;
            var password=$("#password").val();
            if (repwd != password) {
                msg = "两次输入的密码不一致!";
                $.messager.alert('提示信息',msg,'warning');
            }
        });

        //todo 6.email 格式确认
        $("#email").blur(function () {
            //获取邮箱
            var email = this.value;

            if ($.trim(email) == "") {
                msg = "邮箱不能为空!";
                $.messager.alert('提示信息',msg,'warning');
            }else if (!/^\w+@\w{2,}\.\w{2,}$/.test(email)) {
                msg = "邮箱格式不正确!";
                $.messager.alert('提示信息',msg,'warning');
            }
        });

        //todo 7.电话格式确认
        $("#tel").blur(function () {
            //获取电话
            var tel = this.value;

            if ($.trim(tel) == "") {
                msg = "电话号码不能为空!";
                $.messager.alert('提示信息',msg,'warning');
            }else if (!/^0\d{2,3}-?\d{7,8}$/.test(tel)) {
                msg = "电话号码格式不正确!";
                $.messager.alert('提示信息',msg,'warning');
            }
        });
        //todo 8.手机格式确认
        $("#phone").blur(function () {
            //获取手机
            var phone = this.value;

            if ($.trim(phone) == "") {
                msg = "手机号码不能为空!";
                $.messager.alert('提示信息',msg,'warning');
            }else if (!/^1[3|4|5|8]\d{9}$/.test(phone)) {
                msg = "手机号码格式不正确!";
                $.messager.alert('提示信息',msg,'warning');
            }
        });
        //todo 9.QQ格式确认
        $("#qqNum").blur(function () {
            //获取QQ
            var qqNum = this.value;
            if ($.trim(qqNum) == "") {
                msg = "QQ号码不能为空!";
                $.messager.alert('提示信息',msg,'warning');
            }else if (!/^\d{5,12}$/.test(qqNum)) {
                msg = "QQ号码长度必须在5-12之间!";
                $.messager.alert('提示信息',msg,'warning');
            }
        });
        //todo 10.答案格式确认
        $("#answer").blur(function () {
            //获取答案
            var answer = this.value;
            if ($.trim(answer) == "") {
                msg = "密保问题不能为空!";
                $.messager.alert('提示信息',msg,'warning');
            }
        });

// todo 11.添加用户，提交表单函数
    $("#btn_submit").click(function () {
// 对表单中所有字段做校验
        var userId = $("#userId");
        var name = $("#name");
        var passWord = $("#password");
        var repwd = $("#repwd");
        var email = $("#email");
        var tel = $("#tel");
        var phone = $("#phone");
        var qqNum = $("#qqNum");
        var answer = $("#answer");
        var msg = "";
        if ($.trim(userId.val()) == "") {
            msg += "用户登录名不能为空!";
            userId.focus();
        } else if (!/^\w{5,20}$/.test(userId.val())) {
            msg += "用户登录名长度必须在5-20之间!";
            userId.focus();
        }else if ($.trim(name.val()) == "") {
            msg += "姓名不能为空!";
            name.focus();
        } else if ($.trim(passWord.val()) == "") {
            msg += "密码不能为空!";
            passWord.focus();
        } else if (!/^\w{6,20}$/.test(passWord.val())) {
            msg += "密码长度必须为6-20之间!";
            passWord.focus();
        } else if (repwd.val() != passWord.val()) {
            msg += "两次输入的密码不一致!";
            repwd.focus();
        } else if ($.trim(email.val()) == "") {
            msg += "邮箱不能为空!";
            email.focus();
        } else if (!/^\w+@\w{2,}\.\w{2,}$/.test(email.val())) {
            msg += "邮箱格式不正确!";
            email.focus();
        } else if ($.trim(tel.val()) == "") {
            msg += "电话号码不能为空!";
            tel.focus();
            // 020-38216920 02034432323  0755
        } else if (!/^0\d{2,3}-?\d{7,8}$/.test(tel.val())) {
            msg += "电话号码格式不正确!";
            tel.focus();
        } else if ($.trim(phone.val()) == "") {
            msg += "手机号码不能为空!";
            phone.focus();
        } else if (!/^1[3|4|5|8]\d{9}$/.test(phone.val())) {
            msg += "手机号码格式不正确!";
            phone.focus();
        } else if ($.trim(qqNum.val()) == "") {
            msg += "QQ号码不能为空!";
            qqNum.focus();
        } else if (!/^\d{5,12}$/.test(qqNum.val())) {
            msg += "QQ号码长度必须在5-12之间!";
            qqNum.focus();
        } else if ($.trim(answer.val()) == "") {
            msg += "密保问题不能为空!";
            answer.focus();
        }
        if (msg){
            $.messager.alert('提示信息',msg,'warning');
        }else {
            //提交表单
            $("#addUserForm").submit();
        }
    });

	});
</script>
</head>
<body style="background: #F5FAFA;">
	<center>
		<form id="addUserForm" action="${ctx}/user/addUser.jspx"
			method="post">
			<table class="table-condensed">
				<tbody>
					<tr>
						<td><label>登陆名称:</label></td>
						<td><input type="text" id="userId" name="userId"
							value="${user.userId}" class="form-control" placeholder="请输入您的登录名"></td>
						<td><label>用户姓名:</label></td>
						<td><input type="text" id="name" name="name"
							value="${user.name}" class="form-control" placeholder="请输入您的电子邮件"></td>
					</tr>
					<tr>
						<td><label>密码:</label></td>
						<td><input type="password" id="password" name="passWord"
							value="${user.passWord}" class="form-control" placeholder="请输入您的密码"></td>
						<td><label>确认密码:</label></td>
						<td><input type="password" id="repwd" name="repwd"
							value="${user.passWord}" class="form-control" placeholder="请输入您的确认密码"></td>
					</tr>
					<tr>
						<td><label>性别:</label></td>
						<td><select name="sex" id="sex" class="btn btn-default">
								<option value="男">男</option>
								<option value="女">女</option>
						</select></td>
						<td><label>部门:</label></td>
						<td><select id="deptSelect" name="dept.id" class="btn btn-default"></select>
						</td>
					</tr>
					<tr>
						<td><label>职位:</label></td>
						<td><select id="jobSelect" name="job.code" class="btn btn-default"></select>
						</td>
						<td><label>邮箱:</label></td>
						<td><input id="email" name="email" type="text" value="${user.email}" class="form-control" placeholder="请输入您的电子邮件"></td>
					</tr>

					<tr>
						<td><label>电话:</label></td>
						<td><input id="tel" name="tel" type="text" value="${user.tel}" class="form-control" placeholder="请输入您的电话">
						</td>
						<td><label>手机:</label></td>
						<td><input id="phone" name="phone" type="text" value="${user.phone}" class="form-control" placeholder="请输入您的手机">
						</td>
					</tr>

					<tr>
						<td><label>问题:</label></td>
						<td><select name="question" class="btn btn-default"
							id="question">
								<option value="1">您的生日</option>
								<option value="2">您的出生地</option>
								<option value="3">您母亲的名字</option>
						</select></td>
						<td><label>答案:</label></td>
						<td><input id="answer" name="answer" value="${user.answer}" type="text" class="form-control" placeholder="请输入您的答案"></td>
					</tr>
					<tr>
						<td><label>qq号码:</label></td>
						<td><input id="qqNum" name="qqNum" value="${user.qqNum}" type="text" class="form-control" placeholder="请输入您的qq号码">
						</td>
					</tr>

				</tbody>
			</table>
			<div align="center" style="margin-top: 20px;">
				<a id="btn_submit" class="btn btn-info">
					<span class="glyphicon glyphicon-plus"></span>&nbsp;添加</a>
				<button type="reset" class="btn btn-danger">
					<span class="glyphicon glyphicon-remove"></span>&nbsp;重置
				</button>
			</div>
		</form>

	</center>
</body>
</html>
