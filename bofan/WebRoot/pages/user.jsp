<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Object user = request.getAttribute("user");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>用户管理</title>
    <jsp:include page="/pages/include.jsp"/>
    <script type="text/javascript">
    	$(function(){
    		$("#datagrid").datagrid({
    			url:"<%=path%>/userAction/queryDatagrid.action",
    			fit:true,  
    			pagination:true,
    			pageSize:50,
    			pageList:[10,50,100],
    			columns:[[   
    				{field:'ck',checkbox:true},        
    				{title:'账号',field:'userName',width:100},        
    				{title:'显示名称',field:'showName',width:180},        
    				{title:'部门',field:'departmentName',width:180},        
    				{title:'角色',field:'roleName',width:100},        
    				{title:'权限范围',field:'limitsLevel',width:100,
    					formatter: function(value,row,index){
    						if(value == 1){
    							return "可查本部门";
    						} else if(value == 2){
    							return "可查下级部门";
    						} else if(value == 3){
    							return "可查本部门与下级部门";
    						} else {
    							return "只能查本人";
    						}
    					}
    				},        
    				{title:'备注',field:'remark',width:100},        
    				{title:'创建日期',field:'createTime',width:90}     
    			]],
    			toolbar:[{
    				text:"增加",
    				iconCls: 'icon-add',
					handler: function(){
						$("#dialog").dialog("open");
						$("#form1").form("setData",{});
						//$("#password").removeAttr("readonly");
						//$("#ppassword").removeAttr("readonly");
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
    									url:"<%=path%>/userAction/deleteUser.action",
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
							row.ppassword = row.password;
							$("#form1").form("setData",row);
							//$("#password").attr("readonly","readonly");
							//$("#ppassword").attr("readonly","readonly");
						} else {
							toastr.warning("请选择要修改的数据");
						}
					}
    			},"-",{
    				text:"查询",
    				iconCls: 'icon-search',
					handler: function(){
						$("#dialog2").dialog("open");
					}
    			}]
    		});
    		
    		//增加窗口
    		$("#dialog").dialog({
    			title:" ",
    			iconCls:"icon-save",
    			width:400,
    			//height:300,
    			closed:true,
    			modal:true,
    			buttons:[{
    				text:"保存",
    				iconCls:"icon-save",
    				handler:function(){
    					if($("#form1").form("validate")){
    						var data = $("#form1").form("getData");
    						data = addStrToBeforeKey(data,"user.");
    						showLoading();
    						$.ajax({
    							url:"<%=path%>/userAction/saveOrUpdateUser.action",
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
    		
    		//查询窗口
    		$("#dialog2").dialog({
    			title:" ",
    			iconCls:"icon-save",
    			width:400,
    			//height:300,
    			closed:true,
    			modal:true,
    			buttons:[{
    				text:"查询",
    				iconCls:"icon-search",
    				handler:function(){
   						var data = $("#form2").form("getData");
   						data = addStrToBeforeKey(data,"user.");
   						$("#datagrid").datagrid("load",data);
   						$("#dialog2").dialog("close");
    				}
    			},{
    				text:"取消",
    				iconCls:"icon-cancel",
    				handler:function(){
    					$("#dialog2").dialog("close");
    				}
    			}]
    		});
    		
    		$("#departmentId").combobox({
    			url:"<%=path%>/departmentAction/queryComboboxByUser.action",
    			width:155,
    			panelWidth:200,
    			editable:false,
    			valueField:"id",
    			textField:"name",
    		});
    		
    		$("#roleId").combobox({
    			url:"<%=path%>/roleAction/queryCombobox.action",
    			width:155,
    			panelWidth:200,
    			editable:false,
    			valueField:"id",
    			textField:"name",
    		});
    		
    		$("#departmentId2").combobox({
    			url:"<%=path%>/departmentAction/queryComboboxByUser.action",
    			width:155,
    			panelWidth:200,
    			editable:false,
    			valueField:"id",
    			textField:"name",
    		});
    		
    		$("#roleId2").combobox({
    			url:"<%=path%>/roleAction/queryCombobox.action",
    			width:155,
    			panelWidth:200,
    			editable:false,
    			valueField:"id",
    			textField:"name",
    		});
    	});
    </script>
  </head>
  
  <body class="easyui-layout">
  	<div region="center" title="用户">
  		<div id="datagrid"></div>
  	</div>
  	<div id="dialog">
  		<form id="form1">
  		<table class="myTable">
  			<tr>
  				<td>账号：</td>
  				<td><input type="text" name="userName" class="easyui-validatebox" data-options="required:true"/></td>
  			</tr>
  			<tr>
  				<td>密码：</td>
  				<td><input type="password" id="password" name="password" class="easyui-validatebox" data-options="required:true"/></td>
  			</tr>
  			<tr>
  				<td>再次输入：</td>
  				<td><input type="password" id="ppassword" name="ppassword" class="easyui-validatebox" data-options="required:true" validType="compare['password']"/></td>
  			</tr>
  			<tr>
  				<td>显示名称：</td>
  				<td><input type="text" name="showName"/></td>
  			</tr>
  			<tr>
  				<td>部门：</td>
  				<td><input type="text" id="departmentId" name="departmentId"/></td>
  			</tr>
  			<tr>
  				<td>角色：</td>
  				<td><input type="text" id="roleId" name="roleId"/></td>
  			</tr>
  			<tr>
  				<td>权限：</td>
  				<td>
  					<select class="easyui-combobox" name="limitsLevel" style="width:155px;" editable="false" panelHeight="auto">
  						<option value="0">只能查自己</option>
  						<option value="1">可查本部门</option>
  						<option value="2">可查下级部门</option>
  						<option value="3">可查本部门与下级部门</option>
  					</select>
  				</td>
  			</tr>
  			<tr>
  				<td>备注：</td>
  				<td><input type="text" name="remark"/></td>
  			</tr>
  		</table>
  		</form>
  	</div>
  	<div id="dialog2">
  		<form id="form2">
  		<table class="myTable">
  			<tr>
  				<td>账号：</td>
  				<td><input type="text" name="userName"/></td>
  			</tr>
  			<tr>
  				<td>显示名称：</td>
  				<td><input type="text" name="showName"/></td>
  			</tr>
  			<tr>
  				<td>部门：</td>
  				<td><input type="text" id="departmentId2" name="departmentId"/></td>
  			</tr>
  			<tr>
  				<td>角色：</td>
  				<td><input type="text" id="roleId2" name="roleId"/></td>
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
