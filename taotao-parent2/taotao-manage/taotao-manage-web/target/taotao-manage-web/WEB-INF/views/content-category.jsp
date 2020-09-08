<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">
    </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
$(function(){
	$("#contentCategory").tree({
		url : '/rest/content/category',
		animate: true,
		method : "GET",
		onContextMenu: function(e,node){ //右键菜单时间
            e.preventDefault(); //阻止默认事件的执行
            $(this).tree('select',node.target); //选中当前的节点
            $('#contentCategoryMenu').menu('show',{ //显示菜单,并且指定显示位置
                left: e.pageX,
                top: e.pageY
            });
        },
        onAfterEdit : function(node){//按下回车,完成编辑事件
        	var _tree = $(this);
        	if(node.id == 0){
        		// 新增节点
        		$.post("/rest/content/category",{parentId:node.parentId,name:node.text},function(data){
        			_tree.tree("update",{
        				target : node.target,
        				id : data.id
        			});
        		});
        	}else{
        		$.ajax({
        			   type: "PUT",
        			   url: "/rest/content/category",
        			   data: {id:node.id,name:node.text},
        			   success: function(msg){
        				   //$.messager.alert('提示','新增商品成功!');
        			   },
        			   error: function(){
        				   $.messager.alert('提示','重命名失败!');
        			   }
        			});
        	}
        }
	});
});
function menuHandler(item){
	var tree = $("#contentCategory");
	var node = tree.tree("getSelected");
	if(item.name === "add"){
		tree.tree('append', {
            parent: (node?node.target:null),
            data: [{
                text: '新建分类',  //name
                id : 0,
                parentId : node.id //parentId
            }]
        }); 
		var _node = tree.tree('find',0);  //查找到新添加的节点
		tree.tree("select",_node.target).tree('beginEdit',_node.target); //选中该节点并开始编辑
	}else if(item.name === "rename"){
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){
				$.ajax({
     			   type: "POST",
     			   url: "/rest/content/category",
     			   data : {parentId:node.parentId,id:node.id,"_method":"DELETE"},
                    statusCode:{
                        204 :function(){
                            $.messager.alert('提示','删除成功!');
                            tree.tree("remove",node.target);
                        },

                        500:function(){
                            $.messager.alert('提示','删除失败!');
                        }
                    },
     			});
			}
		});
	}
}
</script>