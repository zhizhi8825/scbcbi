<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>意向客户管理</title>
    <jsp:include page="/pages/include.jsp"/>
    <script type="text/javascript">
    	$(function(){
    		var clickedRow;
    		
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
    			queryParams:{"paramEntity.isIntention":"NOT NULL"},
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
    				$("#datagrid2").datagrid("load",{"paramEntity.clientId":row.id});
    				clickedRow = row;
    			},
    			onLoadSuccess:function(data){
    				clickedRow = undefined;
    			}
    		});
    		
    		//增加客户按钮
    		$("#addClient").click(function(){
    			$("#dialog").dialog("open");
				$("#form1").form("setData",{});
    		});
    		
    		//删除客户按钮
    		$("#removeClient").click(function(){
    			var rows = $("#datagrid").datagrid("getSelections");
				if(rows.length>0) {
						$.messager.confirm("提示","确定删除？",function(rr){
						if(rr) {
							var ids = getUrlDataFromObjects(rows,"id","paramEntity.ids");
							showLoading();
							$.ajax({
								url:"<%=path%>/clientAction/deleteClient.action",
									type:"POST",
									data:ids,
									dataType:"json",
									success:function(r){
										hideLoading();
										if(r.result) {
										$("#datagrid").datagrid("reload");
											toastr.success("删除成功!");
										} else {
											$.messager.alert("提示",r.error);
									}
								}
								});
							}
						});
				} else {
					toastr.warning("请选择要删除的数据");
				}
    		});
    		
    		//修改客户按钮
    		$("#editClient").click(function(){
    			var row = $("#datagrid").datagrid("getSelected");
				if(row) {
					$("#dialog").dialog("open");
					$("#form1").form("setData",row);
				} else {
					toastr.warning("请选择要修改的数据");
				}
    		});
    		
    		//查询客户按钮
    		$("#searchClient").click(function(){
    			$("#dialog2").dialog("open");
    		});
    		
    		//增加窗口
    		$("#dialog").dialog({
    			title:" ",
    			iconCls:"icon-save",
    			width:400,
    			//height:500,
    			resizable:true,
    			closed:true,
    			modal:true,
    			buttons:[{
    				text:"保存",
    				iconCls:"icon-save",
    				handler:function(){
    					if($("#form1").form("validate")){
    						var data = $("#form1").form("getData");
    						data = addStrToBeforeKey(data,"client.");
    						showLoading();
    						$.ajax({
    							url:"<%=path%>/clientAction/saveOrUpdateClient.action",
    							type:"POST",
    							data:data,
    							dataType:"json",
    							success:function(r){
    								hideLoading();
    								if(r.result) {
    									$("#datagrid").datagrid("reload");
    									toastr.success("保存成功!");
    									$("#dialog").dialog("close");
    								} else {
    									$.messager.alert("提示",r.error);
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
    					searchClient();
    				}
    			},{
    				text:"取消",
    				iconCls:"icon-cancel",
    				handler:function(){
    					$("#dialog2").dialog("close");
    				}
    			}]
    		});
    		
    		//查询方法
    		function searchClient () {
    			var data = $("#form2").form("getData");
    			data.isIntention = "NOT NULL";
				data = addStrToBeforeKey(data,"paramEntity.");
				$("#datagrid").datagrid("load",data);
				$("#dialog2").dialog("close");
    		}
    		
    		//意向级别下拉框
    		$("#intentLevel").combobox({
    			url:"<%=path%>/dictionaryAction/queryByType.action?paramEntity.type=YIXIANG",
    			width:155,
    			panelWidth:200,
    			panelHeight:"auto",
    			editable:false,
    			valueField:"code",
    			textField:"name"
    		});
    		
    		//项目下拉框
    		$("#project").combobox({
    			url:"<%=path%>/dictionaryAction/queryByType.action?paramEntity.type=XIANGMU",
    			width:155,
    			panelWidth:200,
    			panelHeight:"auto",
    			editable:false,
    			valueField:"code",
    			textField:"name"
    		});
    		
    		//意向级别下拉框(查询)
    		$("#intentLevel2").combobox({
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
    		$("#project2").combobox({
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
    		
    		//给查询窗口里面的输入框都加个回车事件
    		$("#form2").keypress(function(e){
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
    				$("#datagrid").datagrid("load",{
    					"paramEntity.fastQuery":$("#clientName").val(),
    					"paramEntity.isIntention":"NOT NULL"
    				});
    			} else {
    				searchClient();
    			}
    		}
    		
    		//跟踪明细列表
    		$("#datagrid2").datagrid({
    			url:"<%=path%>/trackRecordAction/queryDatagrid.action",
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
    				{title:'公司名称',field:'clientName',width:100},
    				{title:'业务员',field:'showName',width:80},
    				{title:'跟踪记录',field:'record',width:200},        
    				{title:'创建时间',field:'createTime',width:130}     
    			]],
    			onClickRow:function(index,row){
    				$("#form3").form("setData",row);
    			},
    			onLoadSuccess:function(data){
    				$("#form3").form("setData",{});
    			}
    		});
    		
    		//跟踪明细查询的业务员下拉框
    		$("#userId2").combobox({
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
    		
    		//增加跟踪记录
    		$("#addTrack").click(function(){
    			if(!clickedRow) {
    				toastr.warning("请先点击一条客户信息");
    				return;
    			}
    			
    			var record = $("#record").val();
    			
    			if(!record){
    				toastr.warning("请填写跟踪内容");
    				return;
    			}
    			
    			var trackRecord = {};
    			trackRecord.clientId = clickedRow.id;
    			trackRecord.record = record;
    			trackRecord = addStrToBeforeKey(trackRecord,"trackRecord.");
    			
    			$.ajax({
    				url:"<%=path%>/trackRecordAction/saveOrUpdate.action",
    				type:"POST",
    				data:trackRecord,
    				dataType:"json",
    				success:function(r){
    					if(r.result) {
    						$("#record").val("");
    						$("#datagrid2").datagrid("reload");
    					} else {
    						$.messager.alert("提示",r.error);
    					}
    				}
    			});
    		});
    		
    		//修改跟踪记录
    		$("#editTrack").click(function(){
    			var trackRecord = $("#form3").form("getData");
    			
    			if(!trackRecord || !trackRecord.id) {
    				toastr.warning("请先点击一条跟踪记录");
    				return;
    			}
    			
    			if(!trackRecord.record){
    				toastr.warning("请填写跟踪内容");
    				return;
    			}
    			
    			trackRecord = addStrToBeforeKey(trackRecord,"trackRecord.");
    			
    			$.ajax({
    				url:"<%=path%>/trackRecordAction/saveOrUpdate.action",
    				type:"POST",
    				data:trackRecord,
    				dataType:"json",
    				success:function(r){
    					if(r.result) {
    						$("#record").val("");
    						$("#datagrid2").datagrid("reload");
    					} else {
    						$.messager.alert("提示",r.error);
    					}
    				}
    			});
    		});
    		
    		//删除跟踪记录
    		$("#removeTrack").click(function(){
    			var rows = $("#datagrid2").datagrid("getSelections");
    			
    			if(rows.length == 0){
    				toastr.warning("请选择要删除的跟踪记录");
    				return;
    			}
    			
    			$.messager.confirm("提示","确定删除？",function(rs){
    				if(rs){
		    			var ids = getUrlDataFromObjects(rows,"id","paramEntity.ids");
		    			$.ajax({
		    				url:"<%=path%>/trackRecordAction/deleteTrackRecord.action",
		    				type:"POST",
		    				data:ids,
		    				dataType:"json",
		    				success:function(r){
		    					if(r.result) {
		    						$("#datagrid2").datagrid("reload");
		    					} else {
		    						$.messager.alert("提示",r.error);
		    					}
		    				}
		    			});
    				}
    			});
    		});
    		
    		//查询跟踪记录
    		$("#searchTrack").click(function(){
				$("#dialog4").dialog("open");
    		});
    		
    		//跟踪的查询窗口
    		$("#dialog4").dialog({
    			title:" ",
    			iconCls:"icon-search",
    			width:400,
    			//height:300,
    			closed:true,
    			modal:true,
    			buttons:[{
    				text:"查询",
    				iconCls:"icon-search",
    				handler:function(){
    					var data = $("#form4").form("getData");
    					data = addStrToBeforeKey(data,"paramEntity.");
    					$("#datagrid2").datagrid("load",data);
    					$("#dialog4").dialog("close");
    				}
    			},{
    				text:"取消",
    				iconCls:"icon-cancel",
    				handler:function(){
    					$("#dialog4").dialog("close");
    				}
    			}]
    		});
    		
    		//上传文件后的错误提示
    		$("#dialog5").dialog({
    			title:"提示",
    			iconCls:"icon-search",
    			width:400,
    			height:500,
    			resizable:true,
    			maximizable:true,
    			closed:true,
    			modal:true,
    			buttons:[{
    				text:"确定",
    				iconCls:"icon-ok",
    				handler:function(){
    					$("#dialog5").dialog("close");
    				}
    			}]
    		});
    		
    		//文件标签的单击事件
    		$("#file1").click(function(){
    			$("#file1").val("");
    		});
    		
    		$("#file1").change(function(){
    			var filePath = $("#file1").val();
    			if(filePath){
    				showLoading();
    				$("#fileForm").form("submit",{
    					url:"<%=path%>/clientAction/importClientIntention.action",
    					success:function(r){
    						hideLoading();
    						var r = $.evalJSON(r);
    						if(r.result) {
    							$("#datagrid").datagrid("reload");
    							
    							if(r.code == 2) {
    								//$.messager.alert("提示",r.msg);
    								$("#errorDiv").html(r.msg);
    								$("#dialog5").dialog("open");
    							} else {
	    							toastr.success(r.msg);
    							}
    						} else {
    							$.messager.alert("提示",r.error);
    						}
    					}
    				});
    			}
    		});
    	});
    </script>
  </head>
  
  <body class="easyui-layout">
  	<div region="center" title="客户信息">
  		<div class="easyui-layout" fit="true">
  			<div region="north" style="height:31px;" border="false">
  				<div class="datagrid-toolbar">
		           <table cellspacing="0" cellpadding="0">
		              <tbody>
		                  <tr>
		                  	<td>
		                     	<form id="fileForm" method="post" enctype="multipart/form-data">
			                     	<a id="importBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-excel',plain:true">导入
			                     	<input type="file" id="file1" name="file1" style="position:absolute;right:0px;top:0px;z-index:1;font-size: 30px;filter:alpha(opacity=0);-moz-opacity:0;opacity:0;"/>
		                     		</a>
              					</form>
		                     </td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a href="<%=path %>/template/yiXiangLeiJi.xls" class="easyui-linkbutton" data-options="iconCls:'icon-excel',plain:true">模板</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="addClient" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">增加</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="removeClient" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="editClient" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="searchClient" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td>搜索：<input type="text" id="clientName"/></td>
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
  	<div region="east" style="width:310px;" title="跟踪明细"  split="true">
  		<div class="easyui-layout" fit="true">
  			<div region="north" style="height:120px;" border="false" split="true">
  				<div class="datagrid-toolbar">
		           <table cellspacing="0" cellpadding="0">
		              <tbody>
		                  <tr>
		                     <td><a id="addTrack" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">增</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="removeTrack" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="editTrack" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">改</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="searchTrack" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查</a></td>
		                  </tr>
		              </tbody>
		           </table>
		           <form id="form3"><textarea id="record" name="record" rows="4" cols="38"></textarea></form>
		        </div>
  			</div>
  			<div region="center" border="false">
		  		<div id="datagrid2"></div>
  			</div>
  		</div>
  	</div>
  	<div id="dialog">
  		<form id="form1">
  		<table class="myTable">
  			<tr>
  				<td>公司名称：</td>
  				<td><input type="text" name="name" class="easyui-validatebox" data-options="required:true"/></td>
  			</tr>
  			<tr>
  				<td>联系人：</td>
  				<td><input type="text" name="linkman" class="easyui-validatebox" data-options="required:true"/></td>
  			</tr>
  			<tr>
  				<td>电话：</td>
  				<td><input type="text" name="phone" class="easyui-validatebox" data-options="required:true"/></td>
  			</tr>
  			<tr>
  				<td>QQ：</td>
  				<td><input type="text" name="qq" class="easyui-validatebox" data-options=""/></td>
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
  				<td><textarea name="remark" rows="3" cols="20"></textarea></td>
  			</tr>
  		</table>
  		</form>
  	</div>
  	<div id="dialog2">
  		<form id="form2">
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
	  				<td><input type="text" id="intentLevel2" name="intentLevel"/></td>
	  			</tr>
	  			<tr>
	  				<td>项目：</td>
	  				<td>
	  					<input type="text" id="project2" name="project"/>
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
  	<div id="dialog4">
  		<form id="form4">
	  		<table class="myTable">
	  			<tr>
	  				<td>业务员：</td>
	  				<td><input type="text" id="userId2" name="userId" data-options=""/></td>
	  			</tr>
	  			<tr>
	  				<td>公司地名称：</td>
	  				<td><input type="text" name="clientName" class="easyui-validatebox" /></td>
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
  	<div id="dialog5">
  		<div style="height:100%;width:100%;" id="errorDiv"></div>
  	</div>
  </body>
</html>
