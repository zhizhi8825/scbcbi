<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>客户管理</title>
<jsp:include page="/pages/include.jsp" />
<script type="text/javascript">
	$(function() {
		$("#datagrid").datagrid({
			fit:true,  
			//pagination:true,
			pageSize:20,
			pageList:[20,50,100],
			sortName:"createTime",
			sortOrder:"desc",
			striped:true,
			rownumbers:true,
			columns:[[   
				{title:'销售员',field:'showName',width:100},
				{title:'电话量',field:'phoneCount',width:80},
				{title:'客户名称',field:'name',width:100,sortable:true},
				{title:'沟通情况',field:'record',width:100},        
				{title:'意向级别',field:'intentLevel',width:80}, 
				{title:'邮箱',field:'email',width:100}, 
				{title:'备注',field:'remark',width:100}    
			]],
			onLoadSuccess:function(data){
				var colMsg = [{
					colName:"showName",
					subMerge:true,
					followCol:[
						{
							colName:"phoneCount",
							isCount:true,
							subMerge:true,
							notMerge:true
						}           
					]
				}];
				mergeCells($("#datagrid"),colMsg);
			}
		});
		
		//初始化业务员下拉框
		$("#userId").combobox({
			url:"<%=path%>/userAction/queryUserCombobox.action",
			width:155,
			panelWidth:200,
			editable:false,
			//multiple:true,
			valueField:"id",
			textField:"showName",
			loadFilter:function(data){
				data.unshift({id:"",showName:"全部"});
				return data;
			},
			onChange:function(newValue, oldValue){
				search();
			}
		});
		
		$("#beginDate,#endDate").datebox({
			editable:false,
			onSelect:function(date){
				search();
			}
		});
		
		//设置日期为今天
		$("#beginDate,#endDate").datebox("setDateNow");
		
		//查询方法
		function search(){
			var url = $("#datagrid").datagrid("options").url;
			if(!url){
				$("#datagrid").datagrid("options").url = "<%=path%>/clientAction/queryDayReport.action";
			}
			
			var data = $("#form1").form("getData");
			data = addStrToBeforeKey(data,"paramEntity.");
			$("#datagrid").datagrid("load",data);
		}
		
		search();
	});
</script>
</head>

<body class="easyui-layout">
	<div region="north" style="height:30px;" border="false">
		<div class="datagrid-toolbar">
		   	<form id="form1">
	           <table cellspacing="0" cellpadding="0">
	              <tbody>
	                  <tr>
	                  	<td>业务员：<input type="text" id="userId" name="userId" data-options=""/></td>
	                    <td><div class=" datagrid-btn-separator"></td>
	                    <td>开始时间：</td>
		  				<td><input type="text"  id="beginDate" name="beginDate" class="easyui-datebox" data-options=""/></td>
		  				<td>结束时间：</td>
		  				<td><input type="text"  id="endDate" name="endDate" class="easyui-datebox" data-options=""/></td>
	              </tbody>
	           </table>
           </form>
        </div>
	</div>
	<div region="center" border="false">
		<div id="datagrid"></div>
	</div>
</body>
</html>
