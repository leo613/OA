<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  isELIgnored="false"%>
<!-- 引入c标签 -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="${pageContext.request.contextPath}" var="ctx"></c:set>
<!DOCTYPE html> 
<head> 
    <meta name="viewport" content="width=device-width, initial-scale=1"> 
    <title>智能办公</title> 
<link href="${ctx}/resources/css/base.css" rel="stylesheet">
<link href="${ctx}/resources/css/platform.css" rel="stylesheet">
<link rel="stylesheet" href="${ctx}/resources/easyUI/easyui.css">
<script type="text/javascript" src="${ctx }/script/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${ctx }/script/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="${ctx}/resources/easyUI/jquery.easyui.min.js"></script>
 <!-- <script type="text/javascript" src="js/menu.js"></script> -->
<script type="text/javascript" src="${ctx}/resources/main.js"></script>
<script type="text/javascript">
    $(function () {
        $("#tt").tabs({
            taHeight:40, //标签的高度
            onSelect:function (title,index) {
                //todo getSelected:获取选择的选项卡面板
                var currentTab=$('#tt').tabs("getSelected");
                if(currentTab.find("iframe")&&currentTab.find("iframe").size()){
                    currentTab.find("iframe").attr("src",currentTab.find("iframe").attr("src"));
                }
            }
        })
    });

        // 写一个方法往easyUI中添加面板
        function addPanel(id,url,name){
            name = name.replace(/-/g,"");
            // 判断之前是否已经存在该面板存在就不创建新的面板
            //todo tabs:返回所有选项卡面板  exists:表明指定的面板是否存在
            var exist = $('#tt').tabs('exists',name);
            if(exist){
                // 已经存在就将该面板选中  exist
                //todo select:选择一个选项卡面板
                $('#tt').tabs('select',name);
                //todo getTab:获取指定选项卡面板
                var currentTab =  $('#tt').tabs('getTab',name);
                // 刷新一下界面
                if(currentTab.find("iframe") && currentTab.find("iframe").size()){
                    currentTab.find("iframe").attr("src",currentTab.find("iframe").attr("src"));
                }
            }else{
                $('#tt').tabs('add',{
                    id:id,
                    title: name,
                    content: '<div style="width:100%;height:100%;"><iframe class="page-iframe" src="'+url+'" frameborder="no"   border="no" height="100%" width="100%" scrolling="auto"></iframe></div>',
                    closable: true //对话框是否能被关闭
                });
            }
        }

        $(window).resize(function(){
            $('.tabs-panels').height($("#pf-page").height()-46);
            $('.panel-body').height($("#pf-page").height()-76)
        }).resize();

        var page = 0,
            pages = ($('.pf-nav').height() / 70) - 1;

        if(pages === 0){
            $('.pf-nav-prev,.pf-nav-next').hide();
        }
        $(document).on('click', '.pf-nav-prev,.pf-nav-next', function(){

            if($(this).hasClass('disabled')) return;
            if($(this).hasClass('pf-nav-next')){
                page++;
                $('.pf-nav').stop().animate({'margin-top': -70*page}, 200);
                if(page == pages){
                    $(this).addClass('disabled');
                    $('.pf-nav-prev').removeClass('disabled');
                }else{
                    $('.pf-nav-prev').removeClass('disabled');
                }
            }else{
                page--;
                $('.pf-nav').stop().animate({'margin-top': -70*page}, 200);
                if(page == 0){
                    $(this).addClass('disabled');
                    $('.pf-nav-next').removeClass('disabled');
                }else{
                    $('.pf-nav-next').removeClass('disabled');
                }

            }
        });

        //用户退出
        function userExit() {
            //发送请求 进行信息删除退出操作
            window.location = "${pageContext.request.contextPath}/user/logout.jspx";
        }




</script>




</head> 
<body>

    <div class="container">
        <div id="pf-hd">
           <div class="pf-logo">
                <img  src="${ctx}/images/main/login_logo.png" alt="logo">
           </div>
            
            <div class="pf-nav-wrap">
              <div class="pf-nav-ww">
                <ul class="pf-nav">
                 
	                  <li class="pf-nav-item home" data-menu="sys-manage">
	                      <a href="javascript:;">
	                          <span class="iconfont">&#xe63f;</span>
	                          <span class="pf-nav-title">欢迎进入OA管理系统</span>
	                      </a>
	                  </li>
                </ul>
              </div>

<%--              <a href="javascript:;" class="pf-nav-prev disabled iconfont">&#xe606;</a>--%>
<%--              <a href="javascript:;" class="pf-nav-next iconfont">&#xe607;</a>--%>
            </div>
            


            <div class="pf-user">
                <div class="pf-user-photo">
                    <img style="width: 40px;height: 40px;" src="${ctx}/images/user.png" alt="">
                </div>
                <h4 class="pf-user-name ellipsis">${session_user.name}</h4>
                <i class="iconfont xiala">&#xe607;</i>

                <div class="pf-user-panel">
                    <ul class="pf-user-opt">
                        <li>
                            <a href="javascript:;">
                                <i class="iconfont">&#xe60d;</i>
                                <span class="pf-opt-name">用户信息</span>
                            </a>
                        </li>
                        <li  id="exit">
                            <a href="javascript:userExit();">
                                <i class="iconfont">&#xe60e;</i>
                                <span class="pf-opt-name">退出</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>

        </div>

        <div id="pf-bd">
            <div id="pf-sider">
                <h2 class="pf-model-name">
                    <span class="iconfont">&#xe64a;</span>
                    <span class="pf-name">信息系统</span>
                    <span class="toggle-icon"></span>
                </h2>

				<!--todo 展示系统左侧权限树  -->
                <ul class="sider-nav" id="sider-nav">
                  	  
                  	  	        <c:forEach items="${menuOperas}" var="map">
                  	  	               <li>
				                  	  	    <a href="javascript:" >
					                            <span class="iconfont sider-nav-icon">&#xe611;</span>
					                            <span class="sider-nav-title">${map.key.name}</span>
					                            <i class="iconfont">&#xe642;</i>
					                        </a>
                                           <ul class="sider-nav-s">
                                               <c:forEach items="${map.value}" var="module">
                                                   <li><a href="javascript:addPanel('${module.code}' ,'${ctx}${module.url}','${module.name}');">${module.name}</a></li>
                                               </c:forEach>
                                           </ul>
                  	  	        </li>
                  	  	        
                  	  	        </c:forEach>
                  	  	    	
	                      
                        
                 </ul> 
            </div>

           <!-- 面板 -->
            <div id="pf-page">
                <div class="easyui-tabs" id="tt" style="width:100%;height:100%;">
                  <div title="当前用户" id="user" style="padding:10px 5px 5px 10px;">
                    	<iframe class="page-iframe" src="${ctx}/user/showSelf.jspx" frameborder="no"   border="no" height="100%" width="100%" scrolling="auto"></iframe>
                  </div>
                </div>
            </div>
        </div>

        <div id="pf-ft">
            <div class="system-name">
              <i class="iconfont">&#xe6fe;</i>
              <span>智能办公平台&nbsp;v1.0</span>
            </div>
            <div class="copyright-name">
              <span>技术成就未来</span>
              <i class="iconfont" >&#xe6ff;</i>
            </div>
        </div>
    </div>
</body> 
</html>
    