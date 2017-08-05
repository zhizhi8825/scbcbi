<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.gzwanhong.domain.User"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
User user = (User)request.getAttribute("user");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>文件备份管理客户端</title>
    <jsp:include page="/pages/include.jsp"/>
    <script type="text/javascript">
    	$(function(){
    		var isOn = ${isOn};
    		
    		//文件列表
    		$("#datagrid").datagrid({
    			url:"<%=path%>/synFileOptionAction/queryDatagrid.action",
    			fit:true,  
    			pagination:true,
    			pageSize:50,
    			pageList:[20,50,100],
    			sortName:"createTime",
    			sortOrder:"desc",
    			striped:true,
    			rownumbers:true,
    			queryParams:{"paramEntity.isIntention":"NULL"},
    			columns:[[   
    				{field:'ck',checkbox:true},        
    				{title:'文件名',field:'fileName',width:140},
    				{title:'原文件名',field:'oldName',width:140},
    				{title:'操作类型',field:'changeType',width:80,formatter: function(value,row,index){
    					if(value == 1) {
    						return "创建";
    					} else if(value == 2){
    						return "修改";
    					} else if(value == 3){
    						return "删除";
    					} else if(value == 4){
    						return "更名";
    					} else {
    						return "";
    					}
    				}},
    				{title:'文件/目录',field:'fileOrDir',width:80,formatter: function(value,row,index){
    					if(value == 1) {
    						return "文件";
    					} else if(value == 2){
    						return "目录";
    					} else{
    						return "";
    					}
    				}},
    				{title:'文件路径',field:'filePath',width:200},
    				{title:'目标路径',field:'targetPath',width:200},
    				{title:'发生时间',field:'createTime',width:150} 
    			]]
    		});
    		
    		//查询客户按钮
    		$("#optionBtn").click(function(){
    			$("#dialog").dialog("open");
    		});
    		
    		//增加窗口
    		$("#dialog").dialog({
    			title:" ",
    			iconCls:"icon-set",
    			width:600,
    			resizable:true,
    			closed:true,
    			modal:true,
    			buttons:[{
    				text:"保存",
    				iconCls:"icon-save",
    				handler:function(){
    					saveOrUpdate();
    				}
    			},{
    				text:"取消",
    				iconCls:"icon-cancel",
    				handler:function(){
    					$("#dialog").dialog("close");
    				}
    			}]
    		});
    		
    		//保存方法
    		function saveOrUpdate (isInsert){
    			if($("#form1").form("validate")){
					var data = $("#form1").form("getData");
					data = {"paramEntity.jsonStr":$.toJSON(data)}
					
					showLoading();
					$.ajax({
						url:"<%=path%>/configurationAction/saveOrUpdate.action",
						type:"POST",
						data:data,
						dataType:"json",
						success:function(r){
							hideLoading();
							if(r.result) {
								toastr.success("保存成功！");
								$("#dialog").dialog("close");
								
								//如果是已开启了监控器的话，那就重启它
								if($("#onOffBtn").linkbutton("options").text == "关闭"){
									onOff(1);
								}
							} else {
								$.messager.alert("提示",r.error);
							}
						}
					});
				}
    		}
    		
    		//加载配置信息
    		function loadConfig () {
    			$.ajax({
					url:"<%=path%>/configurationAction/queryConfig.action",
					type:"POST",
					dataType:"json",
					success:function(r){
						if(r.result) {
							$("#form1").form("setData",r.obj);
						} else {
							$.messager.alert("配置信息加载失败",r.error);
						}
					}
				});
    		}
    		loadConfig();
    		
    		//开户关闭按钮
    		$("#onOffBtn").click(function () {
    			if($("#onOffBtn").linkbutton("options").text == "开启"){
    				//开启
    				$.messager.confirm("提示","确定开启？",function(isOk){
	    				if(isOk) {
	    					onOff(1);
    					}
    				});
    			} else {
    				//关闭
    				$.messager.confirm("提示","确定关闭？",function(isOk){
	    				if(isOk) {
	    					onOff(0);
    					}
    				});
    			}
    		});
    		
    		//改变按钮状态
    		function btnStatus (status) {
    			if(status == 1) {
    				//开启
    				$("#onOffBtn").linkbutton({text:"开启",iconCls:"icon-ok"});
    			} else {
    				//关闭
    				$("#onOffBtn").linkbutton({text:"关闭",iconCls:"icon-cancel"});
    			}
    		}
    		
    		//启动关闭监控器
    		function onOff (status) {
    			if(status == 1) {
    				//开启
   					$.ajax({
   						url:"<%=path%>/synFileOptionAction/onOff.action",
   						type:"POST",
   						data:{"paramEntity.status":1},
   						dataType:"json",
   						success:function(r){
   							if(r.result) {
   								btnStatus(0);
   								toastr.success("开启成功!");
   							} else {
   								$.messager.alert("异常",r.error);
   							}
   						}
   					});
    			} else {
    				//关闭
  					$.ajax({
   						url:"<%=path%>/synFileOptionAction/onOff.action",
   						type:"POST",
   						data:{"paramEntity.status":0},
   						dataType:"json",
   						success:function(r){
   							if(r.result) {
   								btnStatus(1);
   								toastr.success("已关闭!");
   							} else {
   								$.messager.alert("异常",r.error);
   							}
   						}
   					});
    			}
    		}
    		
    		//一键整站备份
    		$("#backupAllBtn").click(function(){
    			//先查下看数据库是否还有在备份的数据
    			$.ajax({
					url:"<%=path%>/synFileOptionAction/hasDownTask.action",
					type:"POST",
					dataType:"json",
					success:function(r){
						if(r.result) {
							if(r.code == 1) {
								//还有整站备份的任务没有完成，问是否继续
								$("#dialog2").dialog("open");
							} else {
								//没有整站任务在进行，问是否要开始新的备份
								$.messager.confirm("提示","确定开始整站备份？",function(isOk){
				    				if(isOk) {
				    					getAllFilePath();
				    					startDownFile();
			    					}
			    				});
							}
						} else {
							$.messager.alert("异常",r.error);
						}
					}
				});
    		});
    		
    		$("#dialog2").dialog({
    			title:"提示",
    			width:300,
    			height:200,
    			closed:true, 
    			modal:true,
    			buttons:[{
    				text:"重新开始",
    				handler:function(){
    					getAllFilePath();
    					startDownFile();
    					$("#dialog2").dialog("close");
    				}
    			},{
    				text:"继续备份",
    				handler:function(){
    					startDownFile();
    					$("#dialog2").dialog("close");
    				}
    			}]
    		});
    		
    		//整站备份，获取所有文件路径
    		function getAllFilePath () {
    			$("#downStatus").html("正在获取文件信息，请耐心等候...");
    			//先获取所有的文件路径
				$.ajax({
					url:"<%=path%>/synFileOptionAction/getAllFile.action",
					type:"POST",
					dataType:"json",
					success:function(r){
						if(r.result) {
							//startDownFile();
						} else {
							$.messager.alert("异常",r.error);
						}
					}
				});
    		}
    		
    		//整站备份，开始备份
    		function startDownFile () {
    			//先获取所有的文件路径
				$.ajax({
					url:"<%=path%>/synFileOptionAction/startDownFile.action",
					type:"POST",
					dataType:"json",
					success:function(r){
						if(r.result) {
							toastr.success("开始整站备份");
						} else {
							$.messager.alert("异常",r.error);
						}
					}
				});
    		}
    		
    		//暂停一键整站备份
    		$("#cancelBackupAllBtn").click(function(){
    			$.messager.confirm("提示","确定暂停整站备份？",function(isOk){
    				if(isOk) {  
		    			$.ajax({
							url:"<%=path%>/synFileOptionAction/stopDownFile.action",
							type:"POST",
							dataType:"json",
							success:function(r){
								if(r.result) {
									toastr.success("已暂停备份");
								} else {
									$.messager.alert("异常",r.error);
								}
							}
						});
    				}
    			});
    		});
    		
    		
    		//设置当前监控器是否已开启状态
    		if(isOn) { 
    			btnStatus(0);
    		} else {
    			btnStatus(1);
    		}
    		
    		//定时刷新列表
    		//$("#datagrid").datagrid("reload");
    		//var vartimer = setInterval(function(){$("#datagrid").datagrid("reload");},2000);
    		
    		//不停的刷新整站备份进度
    		var vartimer2 = setInterval(function(){
    			$.ajax({
    				url:"<%=path%>/synFileOptionAction/getDownloadStatus.action",
    				type:"POST",
    				dataType:"json",
    				success:function(r){
    					if(r.result) {
    						$("#downStatus").html(r.msg);
    					}
    				}
    			});
    		},3000);
    	});
    </script>
  </head>
  
  <body class="easyui-layout">
  	<div region="center" title="文件备份管理">
  		<div class="easyui-layout" fit="true">
  			<div region="north" style="height:31px;" border="false">
  				<div class="datagrid-toolbar">
		           <table cellspacing="0" cellpadding="0">
		              <tbody>
		                  <tr>
		                     <td><a id="optionBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-set',plain:true">配置</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="backupAllBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-down',plain:true">一键整站备份</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="cancelBackupAllBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-no',plain:true">暂停整站备份</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><a id="onOffBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true">开启</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
		                     <td><span id = "downStatus"></span></td>
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
  	<div id="dialog">
  		<form id="form1">
  		<table class="myTable">
  			<tr>
  				<td>远程服务器地址：</td>
  				<td><input type="text" name="webserviceUrl" class="easyui-validatebox" required="true" style="width:300px"/></td>
  			</tr>
  			<tr>
  				<td>本机标识：</td>
  				<td><input type="text" name="flag" class="easyui-validatebox" required="true" style="width:300px"/></td>
  			</tr>
  			<tr>
  				<td>本机名字：</td>
  				<td><input type="text" name="clientName" class="easyui-validatebox" required="true" style="width:300px"/></td>
  			</tr>
  			<tr>
  				<td>备份到哪个目录：</td>
  				<td><input type="text" name="backupDir" class="easyui-validatebox" required="true" style="width:300px"/></td>
  			</tr>
  			<tr>
  				<td>备份几小时内变动的文件：</td>
  				<td><input type="text" name="hours" class="easyui-validatebox" required="true"/>小时内</td>
  			</tr>
  			<tr>
  				<td>整站备份线程数：</td>
  				<td><input type="text" name="theadCount" class="easyui-validatebox" required="true"/>小时内</td>
  			</tr>
  			<tr>
  				<td>文件传输时间段：<br>(时:分)示例<br>00:00-03:15<br>12:30-13:00</td>
  				<td><textarea name="workTime" rows="10" cols="40"></textarea></td>
  			</tr>
  		</table>
  		</form>
  	</div>
  	<div id="dialog2">还有未下载完的整站备份任务，是否要重新整站备份？还是继续上次未完成的备份？</div>
  </body>
</html>
