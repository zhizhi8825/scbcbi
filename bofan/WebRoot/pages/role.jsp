<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Object user = request.getAttribute("user");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>角色管理</title>
    <jsp:include page="/pages/include.jsp"/>
    <script type="text/javascript">
    	$(function(){
    		$("#datagrid").datagrid({
    			url:"<%=path%>/roleAction/queryDatagrid.action",
    			fit:true,  
    			singleSelect:true,
    			pagination:true,
    			pageSize:50,
    			pageList:[10,50,100],
    			columns:[[   
    				{title:'名称',field:'name',width:180},        
    				{title:'编码',field:'code',width:180},        
    				{title:'备注',field:'remark',width:180},        
    				{title:'创建日期',field:'createTime',width:180}     
    			]],
    			toolbar:[{
    				text:"增加",
    				iconCls: 'icon-add',
					handler: function(){
						$("#dialog").dialog("open");
						$("#form1").form("setData",{});
					}
    			},"-",{
    				text:"删除",
    				iconCls: 'icon-remove',
					handler: function(){
						var rows = $("#datagrid").datagrid("getSelections");
						if(rows.length>0) {
 							$.messager.confirm("提示","确定删除？",function(rr){
								if(rr) {
									var ids = getUrlDataFromObjects(rows,"id","ids");
									showLoading();
									$.ajax({
    									url:"<%=path%>/roleAction/deleteRole.action",
   										type:"POST",
   										data:ids,
   										dataType:"json",
   										success:function(r){
   											hideLoading();
   											if(r.result) {
	    										$("#datagrid").datagrid("reload");
   												toastr.success("删除成功!");
   											} else {
   												$.messager.alert(r.error);
    										}
	    								}
   									});
   								}
   							});
						} else {
							toastr.warning("请选择要删除的数据");
						}
					}
    			},"-",{
    				text:"修改",
    				iconCls: 'icon-edit',
					handler: function(){
						var row = $("#datagrid").datagrid("getSelected");
						if(row) {
							$("#dialog").dialog("open");
							$("#form1").form("setData",row);
						} else {
							toastr.warning("请选择要修改的数据");
						}
					}
    			}],
    			onSelect:function(index,row){
    				$.ajax({
    					url:"<%=path%>/relationRoleMenuAction/queryByRoleId.action",
    					type:"POST",
    					data:{roleId:row.id},
    					dataType:"json",
    					success:function(r){
    						if(r.result) {
    							$("#tree2").tree("uncheckAll");
    							$("#tree2").tree("checkNodes",r.obj);
    						} else {
    							$.messager.alert("提示",r.error);
    						}
    					}
    				});
    			}
    		});
    		
    		//菜单树
    		$("#tree2").tree({
    			url:"<%=path%>/menuAction/initUserMenuTree.action",
    			checkbox:true
    		});
    		
    		//增加窗口
    		$("#dialog").dialog({
    			title:" ",
    			iconCls:"icon-save",
    			width:400,
    			//height:250,
    			closed:true,
    			modal:true,
    			buttons:[{
    				text:"保存",
    				iconCls:"icon-save",
    				handler:function(){
    					if($("#form1").form("validate")){
    						var data = $("#form1").form("getData");
    						data = addStrToBeforeKey(data,"role.");
    						data["paramMap.myName"]="luozhi";
    						showLoading();
    						$.ajax({
    							url:"<%=path%>/roleAction/saveOrUpdateRole.action",
    							type:"POST",
    							data:data,
    							dataType:"json",
    							success:function(r){
    								hideLoading();
    								if(r.result) {
    									$("#datagrid").datagrid("reload");
    									toastr.success("增加成功!");
    									$("#dialog").dialog("close");
    								} else {
    									$.messager.alert(r.error);
    								}
    							}
    						});
    					}
    				}
    			},{
    				text:"取消",
    				iconCls:"icon-cancel",
    				handler:function(){
    					$("#dialog").dialog("close");
    				}
    			}]
    		});
    		
    		//保存菜单按钮
    		$("#saveMenu").click(function(){
    			var role = $("#datagrid").datagrid("getSelected");
    			if(role) {
    				$.messager.confirm("提示","确定保存？",function(rr){
    					if(rr) {
		    				var nodes = $("#tree2").tree("getChecked",["checked","indeterminate"]);
    						var ids = getUrlDataFromObjects(nodes,"id","ids");
    						
		    				showLoading();
    						$.ajax({
    							url:"<%=path%>/relationRoleMenuAction/saveRelationRoleMenu.action",
    							type:"POST",
		    					data:ids+"&roleId="+role.id,
    							dataType:"json",
    							success:function(r){
    								hideLoading();
		    						if(r.result) {
    									toastr.success("保存成功");
    								} else {
    									$.messager.alert("提示",r.error);
		    						}
    							}
    						});
    					}
    				});
    			} else {
    				toastr.warning("请选择角色");
    			}
    		});
    	});
    </script>
  </head>
  
  <body class="easyui-layout">
  	<div region="center" title="角色">
  		<div id="datagrid"></div>
  	</div>
  	<div region="east" title="菜单" style="width:300px;">
  		<div class="easyui-layout" fit="true">
  			<div region="north" style="height:35px;border:0px;">
  				<div class="datagrid-toolbar">
					<table cellspacing="0" cellpadding="0">
						<tbody>
							<tr>
								<td><a id="saveMenu" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a></td>
								<td><div class="datagrid-btn-separator"></td>
							</tr>
						</tbody>
					</table>
				</div>
  			</div>
  			<div region="center">
  				<div id="tree2"></div>
  			</div>
  		</div>
  	</div>
  	<div id="dialog">
  		<form id="form1">
  		<table class="myTable">
  			<tr>
  				<td>名称：</td>
  				<td><input type="text" name="name" class="easyui-validatebox" data-options="required:true"/></td>
  			</tr>
  			<tr>
  				<td>编码：</td>
  				<td><input type="text" name="code"/></td>
  			</tr>
  			<tr>
  				<td>备注：</td>
  				<td><input type="text" name="remark"/></td>
  			</tr>
  		</table>
  		</form>
  	</div>
  </body>
</html>
