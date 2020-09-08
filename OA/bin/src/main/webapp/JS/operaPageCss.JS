(function () {
   $(function () {
       //$(document).ajaxStart($.blockUI({ css: { backgroundColor: '#11a9e2', color: '#fff' } , //message: '<h6>正在加载..</h6>'})).ajaxStop($.unblockUI);
       /** 激活用户操作*/
       $("input[id^='checkUser_']").switchbutton({
           onChange: function(checked){
               var status = checked?"1":"0";
               window.location = "${ctx}/identity/user/activeUser.jspx?userId="+this.value+"&status="+status
                   +"&pageIndex=${pageModel.pageIndex}&name=${user.name}&phone=${user.phone}&dept.id=${user.dept.id}&job.code=${user.job.code}";
           }
       });

       //获取所有的子checkbox
       var boxes =$("input[id^='box_']");

       // Todo 为全选绑定点击事件
       $("#checkAll").click(function () {
           //若是没有被选择则全选否则全部取消
           boxes.prop("checked",this.checked);

           //触发数据行的 mouseover或者mouseout事件
           $("tr[id^='dataTr_']").trigger(this.checked?"mouseover":"mouseout");
       });

       // 为所有的子checkBox绑定点击事件
       boxes.click(function () {
           //获取选中的checkbox的个数
           // var len=boxes.filter(":checked").length;
           var len=$("input[id^='box']:checked").length;
           //如果所有的子checkbox是选中的,那么全选应该被选中
           $("#checkAll").prop("checked",boxes.length==len);

       });

       //给数据行绑定 mouseover以及mouseout事件
       $("tr[id^='dataTr_']").mouseover(function () {
           $(this).css("background","#eeccff");
       }).mouseout(function () {
           //如果当前行中的checkbox 是选中的则鼠标离开的时候不去掉背景色
           //获取tr的id
           var trId=this.id;
           //获取当前行中的CheckBox的ID
           var boxId=trId.replace("dataTr","box");
           //获取checkbox的选中状态
           var  flag=$("#"+boxId).prop("checked");
           if (!flag){
               $(this).css("background","");
           }

       });
   })

})()