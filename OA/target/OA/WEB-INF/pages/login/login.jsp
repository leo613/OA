<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>

<%@include file="../common/taglib.jsp"%>
<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
<!DOCTYPE html> 
<html lang="en"> 
<head> 
    <meta charset="utf-8"> 
    <meta name="viewport" content="width=device-width, initial-scale=1"> 
    <title>智能办公</title> 
    <link href="${ctx}/resources/css/base.css" rel="stylesheet">
    <link href="${ctx}/resources/css/login.css" rel="stylesheet">
    <link href="${ctx}/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <script type="text/javascript" src="${ctx }/script/jquery-1.11.0.min.js"></script>
	<script type="text/javascript" src="${ctx }/script/jquery-migrate-1.2.1.min.js"></script>
	<script type="text/javascript" src="${ctx }/resources/bootstrap/js/bootstrap.min.js"></script>
     <script type="text/javascript" src="${ctx}/resources/easyUI/jquery.easyui.min.js"></script>
     <script type="text/javascript" src="${ctx}/resources/easyUI/easyui-lang-zh_CN.js"></script>
     <link rel="stylesheet" href="${ctx}/resources/easyUI/easyui.css">
	 <script type="text/javascript">
        $(function () {

            if("${message}"){
                $.messager.alert('提示信息',"${message}",'warning');
            }
            //todo 如果当前窗口不是  最顶级窗口，将当前窗口作为 顶级|最外层窗口
            if(window.location != top.window.location){
                top.window.location = window.location;
            }
            //todo 为验证码点击绑定事件
            $("#vimg").click(function () {
                $("#vimg").prop("src","${ctx}/createCode.jspx?data="+new Date());
            });


            //todo 为登录点击绑定事件
            $("#login_id").click(function () {
                //获取账号
                var userId=$("#userId").val();
                //获取密码
                var passWord=$("#passWord").val();
                //获取验证码
                var vcode=$("#vcode").val();

                //todo 通过正则表达式进行数据的确认
                //\W === [^A-z0-9_] // 除了字母 数字 下划线以外的
                if (/^[\W]{5,16}/.test(userId)){
                    $.messager.alert('提示信息',"您输入的账号格式不正确，请核实！",'warning');
                }else if(/^[\W]{5,16}/.test(passWord)){
                    $.messager.alert('提示信息',"您输入的密码格式不正确，请核实！",'warning');
                }else if (/^[\W]{4}/.test(vcode)) {
                    $.messager.alert('提示信息',"您输入的验证码格式不正确，请核实！",'warning');
                }else {
                    var  loginSon={"userId":userId,"passWord":passWord,"vcode":vcode};

                    $.ajax({
                        type:"POST",
                        url:"${ctx}/user/ajaxLogin.jspx",//请求地址
                        data:loginSon,
                        dataType:"text",//预期服务器返回的数据格式
                        success:function (data) {
                            if (!data){
                                // 跳转至首页
                                window.location="${ctx}/main/main.jspx";
                            }else {
                                $.messager.alert('提示信息',data,'warning');
                                // 刷新验证码 方式1
                                <%--$("#vimg").prop("src","${ctx}/createCode.jspx?data="+new Date());--%>
                                //刷新验证码 方式2
                                $("#vimg").trigger("click");
                            }
                        },error:function () {
                            $.messager.alert('提示信息',"网络异常！",'warning');
                        }
                    });
                }
            })
        })
	 </script>
</head> 
<body>
	<div class="login-hd">
		<div class="left-bg"></div>
		<div class="right-bg"></div>
		<div class="hd-inner">
			<span class="logo"></span>
			<span class="sys-name">智能办公平台</span>
		</div>
	</div>
	<div class="login-bd">
		<div class="bd-inner">
			<div class="inner-wrap">
				<div class="lg-zone">
					<div class="lg-box">
						<div class="panel-heading" style="background-color: #ADC5CC;">
							<h3 class="panel-title" style="color: #FFFFFF;font-style: italic;">用户登陆</h3>
						</div>
						<form id="loginForm">
						   <div class="form-horizontal" style="padding-top: 20px;padding-bottom: 30px; padding-left: 20px;">
								
								<div class="form-group" style="padding: 5px;">
									<div class="col-md-11">
										<input class="form-control" id="userId" name="userId" type="text" placeholder="账号/邮箱">
									</div>
								</div>
				
								<div class="form-group" style="padding: 5px;">
									<div class="col-md-11">
										<input  class="form-control"  id="passWord" name="passWord" type="password" placeholder="请输入密码">
									</div>
								</div>
				
								<div class="form-group" style="padding: 5px;">
									<div class="col-md-11">
										<div class="input-group">
										<input class="form-control " id="vcode" name="vcode" type="text" placeholder="验证码">
										<span class="input-group-addon" id="basic-addon2"><img style="cursor:pointer" class="check-code" id="vimg" alt="" src="${ctx}/createCode.jspx"></span>
										</div>
									</div>
								</div>
				
						</div>
							
							<div class="enter">
								<a href="javascript:;" id="login_id" class="purchaser" >登录</a>
								<a href="javascript:;" class="supplier" onClick="javascript:location.reload()">重 置</a>
							</div>
						</form>
					</div>
				</div>
				<div class="lg-poster"></div>
			</div>
	</div>
	</div>
	<div class="login-ft">
		<div class="ft-inner">
			<div class="about-us">
				<a href="javascript:;">关于我们</a>
				<a href="javascript:;">法律声明</a>
				<a href="javascript:;">服务条款</a>
				<a href="javascript:;">联系方式</a>
			</div>
			<div class="address">
			地址：江苏省苏州市
			&nbsp;邮编：215200 &nbsp;&nbsp;
			技术成就未来</div>
			<div class="other-info">
			建议使用火狐、谷歌浏览器，不建议使用IE浏览器！</div>
		</div>
	</div>
</body> 
</html>
