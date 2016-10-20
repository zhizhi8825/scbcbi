<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>转移客户</title>
    <jsp:include page="/pages/include.jsp"/>
    <script type="text/javascript">
    	$(function(){
    		//客户列表
    		$("#datagrid").datagrid({
    			url:"<%=path%>/clientAction/queryDatagrid.action",
    			fit:true,  
    			pagination:true,
    			pageSize:20,
    			pageList:[20,50,100],
    			sortName:"createTime",
    			sortOrder:"desc",
    			striped:true,
    			rownumbers:true,
    			columns:[[   
    				{field:'ck',checkbox:true},        
    				{title:'公司名称',field:'name',width:100,sortable:true},
    				{title:'联系人',field:'linkman',width:100},
    				{title:'电话',field:'phone',width:100},        
    				{title:'QQ',field:'qq',width:80}, 
    				{title:'邮箱',field:'email',width:100}, 
    				{title:'地址',field:'address',width:100},        
    				{title:'意向级别',field:'intentLevel',width:100,sortable:true},        
    				{title:'项目名称',field:'projectName',width:100},        
    				{title:'备注',field:'remark',width:100},        
    				{title:'所属业务员',field:'showName',width:100},        
    				{title:'修改时间',field:'updateTime',width:130,sortable:true},     
    				{title:'创建时间',field:'createTime',width:130,sortable:true}     
    			]],
    			onClickRow:function(index,row){
    				var url = $("#datagrid2").datagrid("options").url;
    				if(!url){
    					$("#datagrid2").datagrid("options").url="<%=path%>/changeRecordAction/queryDatagrid.action";
    				}
    				$("#datagrid2").datagrid("load",{"paramEntity.clientId":row.id});
    			}
    		});
    		
    		//查询客户按钮
    		$("#searchClient").click(function(){
    			$("#dialog").dialog("open");
    		});
    		
    		//查询窗口
    		$("#dialog").dialog({
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
    					searchClient();
    				}
    			},{
    				text:"取消",
    				iconCls:"icon-cancel",
    				handler:function(){
    					$("#dialog").dialog("close");
    				}
    			}]
    		});
    		
    		//查询方法
    		function searchClient () {
    			var data = $("#form").form("getData");
				data = addStrToBeforeKey(data,"paramEntity.");
				$("#datagrid").datagrid("load",data);
				$("#dialog").dialog("close");
    		}
    		
    		//意向级别下拉框(查询)
    		$("#intentLevel").combobox({
    			url:"<%=path%>/dictionaryAction/queryByType.action?paramEntity.type=YIXIANG",
    			width:155,
    			panelWidth:200,
    			panelHeight:"auto",
    			editable:false,
    			valueField:"code",
    			textField:"name",
    			loadFilter:function(data){
    				data.unshift({code:"",name:"全部"});
    				return data;
    			}
    		});
    		
    		//项目下拉框(查询)
    		$("#project").combobox({
    			url:"<%=path%>/dictionaryAction/queryByType.action?paramEntity.type=XIANGMU",
    			width:155,
    			panelWidth:200,
    			panelHeight:"auto",
    			editable:false,
    			valueField:"code",
    			textField:"name",
    			loadFilter:function(data){
    				data.unshift({code:"",name:"全部"});
    				return data;
    			}
    		});
    		
    		//业务员下拉框
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
    			}
    		});
    		
    		//业务员下拉框
    		$("#userId2").combobox({
    			url:"<%=path%>/userAction/queryUserCombobox.action",
    			width:155,
    			panelWidth:200,
    			editable:false,
    			//multiple:true,
    			valueField:"id",
    			textField:"showName"
    		});
    		
    		//给查询窗口里面的输入框都加个回车事件
    		$("#form").keypress(function(e){
    			if(e.which == 13) {
    				searchClient();				
    			}
    		});
    		
    		//设置公司名称的查询，输入东西后，延时执行查询
    		var timer1;
    		$("#clientName").keydown(function(e){
    			if(timer1) {
    				clearTimeout(timer1);
    			}
    			
    			timer1 = setTimeout(queryByClientName,1000);
    		});
    		
    		function queryByClientName(){
    			if($("#clientName").val() != ""){
    				$("#datagrid").datagrid("load",{"paramEntity.name":$("#clientName").val()});
    			} else {
    				searchClient();
    			}
    		}
    		
    		//转移按钮
    		$("#changeBtn").click(function(){
    			var rows = $("#datagrid").datagrid("getSelections");
    			
    			if(rows.length == 0){
    				toastr.warning("请选择要转移的记录");
    				return;
    			}
    			
    			$("#dialog2").dialog("open");
    		});
    		
    		//转移窗口
    		$("#dialog2").dialog({
    			title:"转移目标",
    			iconCls:"icon-save",
    			width:400,
    			//height:300,
    			closed:true,
    			modal:true,
    			buttons:[{
    				text:"确定",
    				iconCls:"icon-save",
    				handler:function(){
    					var rows = $("#datagrid").datagrid("getSelections");
    	    			
    	    			if(rows.length == 0){
    	    				toastr.warning("请选择要转移的记录");
    	    				return;
    	    			}
    	    			
    	    			var id = $("#userId2").combobox("getValue");
    	    			
    	    			if(!id) {
    	    				toastr.warning("请选择要转给谁");
    	    				return;
    	    			}
    	    			
    	    			$.messager.confirm("提示","确定转移？",function(rs){
    	    				if(rs){
    			    			var ids = getUrlDataFromObjects(rows,"id","paramEntity.ids");
    			    			$.ajax({
    			    				url:"<%=path%>/clientAction/saveChange.action",
    			    				type:"POST",
    			    				data:ids+"&paramEntity.id="+id,
    			    				dataType:"json",
    			    				success:function(r){
    			    					if(r.result) {
    			    						$("#dialog2").dialog("close");
    			    						$("#datagrid").datagrid("reload");
    			    					} else {
    			    						$.messager.alert("提示",r.error);
    			    					}
    			    				}
    			    			});
    	    				}
    	    			});
    				}
    			},{
    				text:"取消",
    				iconCls:"icon-cancel",
    				handler:function(){
    					$("#dialog2").dialog("close");
    				}
    			}]
    		});
    		
    		//转移明细列表
    		$("#datagrid2").datagrid({
    			fit:true,  
    			pagination:true,
    			pageSize:20,
    			pageList:[20,50,100],
    			sortName:"createTime",
    			sortOrder:"desc",
    			striped:true,
    			rownumbers:true,
    			columns:[[   
    				{field:'ck',checkbox:true},        
    				{title:'原来业务员',field:'sourceShowName',width:100},
    				{title:'转给业务员',field:'targetShowName',width:100},
    				{title:'操作人',field:'updateName',width:100},        
    				{title:'创建时间',field:'createTime',width:130}     
    			]]
    		});
    	});
    </script>
  </head>
  
  <body class="easyui-layout">
  	<div region="center" title="客户信息">
  		<div class="easyui-layout" fit="true">
  			<div region="north" style="height:30px;" border="false">
  				<div class="datagrid-toolbar">
		           <table cellspacing="0" cellpadding="0">
		              <tbody>
		                  <tr>
		                     <td><a id="searchClient" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="changeBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">转移</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td>公司名称(可简拼)：<input type="text" id="clientName"/></td>
		                  </tr>
		              </tbody>
		           </table>
		        </div>
  			</div>
  			<div region="center" border="false">
		  		<div id="datagrid"></div>
  			</div>
  		</div>
  	</div>
  	<div region="east" style="width:450px;" title="转移记录"  split="true">
  		<div class="easyui-layout" fit="true">
  			<div region="north" style="height:30px;" border="false">
  				<div class="datagrid-toolbar">
		           <table cellspacing="0" cellpadding="0">
		              <tbody>
		                  <tr>
		                     <td><div class=" datagrid-btn-separator"></td>
		                  </tr>
		              </tbody>
		           </table>
		        </div>
  			</div>
  			<div region="center" border="false">
		  		<div id="datagrid2"></div>
  			</div>
  		</div>
  	</div>
  	<div id="dialog">
  		<form id="form">
	  		<table class="myTable">
	  			<tr>
	  				<td>业务员：</td>
	  				<td><input type="text" id="userId" name="userId" data-options=""/></td>
	  			</tr>
	  			<tr>
	  				<td>公司地名称：</td>
	  				<td><input type="text" name="name" class="easyui-validatebox" /></td>
	  			</tr>
	  			<tr>
	  				<td>联系人：</td>
	  				<td><input type="text" name="linkman" class="easyui-validatebox"/></td>
	  			</tr>
	  			<tr>
	  				<td>电话：</td>
	  				<td><input type="text" name="phone" class="easyui-validatebox"/></td>
	  			</tr>
	  			<tr>
	  				<td>QQ：</td>
	  				<td><input type="text" name="qq" class="easyui-validatebox"/></td>
	  			</tr>
	  			<tr>
	  				<td>邮箱：</td>
	  				<td><input type="text" name="email" class="easyui-validatebox" data-options=""/></td>
	  			</tr>
	  			<tr>
	  				<td>地址：</td>
	  				<td><input type="text" name="address" class="easyui-validatebox" data-options=""/></td>
	  			</tr>
	  			<tr>
	  				<td>意向级别：</td>
	  				<td><input type="text" id="intentLevel" name="intentLevel"/></td>
	  			</tr>
	  			<tr>
	  				<td>项目：</td>
	  				<td>
	  					<input type="text" id="project" name="project"/>
	  				</td>
	  			</tr>
	  			<tr>
	  				<td>备注：</td>
	  				<td><input type="text" name="remark" data-options=""/></td>
	  			</tr>
	  			<tr>
	  				<td>开始时间：</td>
	  				<td><input type="text" name="beginDate" class="easyui-datebox" data-options="editable:false"/></td>
	  			</tr>
	  			<tr>
	  				<td>结束时间：</td>
	  				<td><input type="text" name="endDate" class="easyui-datebox" data-options="editable:false"/></td>
	  			</tr>
	  		</table>
  		</form>
  	</div>
  	<div id="dialog2">
  		<form id="form2">
	  		<table class="myTable">
	  			<tr>
	  				<td>业务员：</td>
	  				<td><input type="text" id="userId2" name="userId" data-options=""/></td>
	  			</tr>
	  		</table>
  		</form>
  	</div>
  </body>
</html>
