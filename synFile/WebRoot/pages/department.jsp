<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Object user = request.getAttribute("user");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>部门管理</title>
    <jsp:include page="/pages/include.jsp"/>
    <script type="text/javascript">
    	$(function(){
    		$("#treegrid").treegrid({
    			url:"<%=path%>/departmentAction/queryTreegrid.action",
    			fit:true,
    			idField:'id',    
    			treeField:'name',    
    			columns:[[   
    				{title:'名称',field:'name',width:180},        
    				{title:'电话',field:'tel',width:180},        
    				{title:'地址',field:'address',width:180},        
    				{title:'联系人',field:'linkman',width:180},        
    				{title:'创建日期',field:'createTime',width:180}     
    			]],
    			toolbar:[{
    				text:"增加",
    				iconCls: 'icon-add',
					handler: function(){
						var row = $("#treegrid").treegrid("getSelected");
						if(row) {
							$("#dialog").dialog("open");
							$("#form1").form("setData",{
								"parentName":row.name,
								"parentId":row.id
							});
						} else {
							$.messager.confirm("提示","没有选择上级菜单，将添加为根菜单？",function(r){
								if(r) {
									$("#dialog").dialog("open");
									$("#form1").form("setData",{
										"parentName":"无",
										"parentId":-1
									});
								}
							});
						}
					}
    			},"-",{
    				text:"删除",
    				iconCls: 'icon-remove',
					handler: function(){
						var row = $("#treegrid").treegrid("getSelected");
						if(row) {
							if(!row.children) {
								$.messager.confirm("提示","确定删除？",function(rr){
									if(rr) {
										showLoading();
										$.ajax({
	    									url:"<%=path%>/departmentAction/deleteDepartment.action",
    										type:"POST",
    										data:{id:row.id},
    										dataType:"json",
    										success:function(r){
    											hideLoading();
    											if(r.result) {
		    										$("#treegrid").treegrid("reload");
    												toastr.success("删除成功!");
    											} else {
    												$.messager.alert(r.error);
	    										}
		    								}
    									});
    								}
    							});
    						} else {
    							toastr.warning("请先删完子菜单");
    						}
						} else {
							toastr.warning("请选择要删除的数据");
						}
					}
    			},"-",{
    				text:"修改",
    				iconCls: 'icon-edit',
					handler: function(){
						var row = $("#treegrid").treegrid("getSelected");
						if(row) {
							$("#dialog").dialog("open");
							row.parentName = "";
							$("#form1").form("setData",row);
						} else {
							toastr.warning("请选择要修改的数据");
						}
					}
    			}]
    		});
    		
    		//增加窗口
    		$("#dialog").dialog({
    			title:" ",
    			iconCls:"icon-save",
    			width:400,
    			height:250,
    			closed:true,
    			modal:true,
    			buttons:[{
    				text:"保存",
    				iconCls:"icon-save",
    				handler:function(){
    					if($("#form1").form("validate")){
    						var data = $("#form1").form("getData");
    						data = addStrToBeforeKey(data,"department.");
    						showLoading();
    						$.ajax({
    							url:"<%=path%>/departmentAction/saveOrUpdateDepartment.action",
    							type:"POST",
    							data:data,
    							dataType:"json",
    							success:function(r){
    								hideLoading();
    								if(r.result) {
    									$("#treegrid").treegrid("reload");
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
    	});
    </script>
  </head>
  
  <body>
  	<div id="treegrid"></div>
  	<div id="dialog">
  		<form id="form1">
  		<table class="myTable">
  			<tr>
  				<td>上级菜单：</td>
  				<td><span name="parentName"></span></td>
  			</tr>
  			<tr>
  				<td>名称：</td>
  				<td><input type="text" name="name" class="easyui-validatebox" data-options="required:true"/></td>
  			</tr>
  			<tr>
  				<td>电话：</td>
  				<td><input type="text" name="tel"/></td>
  			</tr>
  			<tr>
  				<td>地址：</td>
  				<td><input type="text" name="address"/></td>
  			</tr>
  			<tr>
  				<td>联系人：</td>
  				<td><input type="text" name="linkman"/></td>
  			</tr>
  		</table>
  		</form>
  	</div>
  </body>
</html>
