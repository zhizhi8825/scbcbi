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
    <title>文件备份管理</title>
    <jsp:include page="/pages/include.jsp"/>
    <script type="text/javascript">
    	$(function(){
    		//文件列表
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
    			queryParams:{"paramEntity.isIntention":"NULL"},
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
    		
    		//查询客户按钮
    		$("#optionBtn").click(function(){
    			$("#dialog").dialog("open");
    		});
    		
    		//增加窗口
    		$("#dialog").dialog({
    			title:" ",
    			iconCls:"icon-set",
    			width:500,
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
					data = addStrToBeforeKey(data,"client.");
					
					if(isInsert) {
						data["paramEntity.isInsert"] = true;
					}
					
					showLoading();
					$.ajax({
						url:"<%=path%>/clientAction/saveOrUpdateClient.action",
						type:"POST",
						data:data,
						dataType:"json",
						success:function(r){
							hideLoading();
							if(r.result) {
								if(r.code && r.code == 1) {
									//如果是有相似客户信息的话，就显示出来确认下
									var msgStr = "发现该项目中有相似的公司，请认真核对是否有撞单。如果确认没撞单，请按“确定”，否则按“取消”。</br><div style='color:red'>注意：如果在后面发现有撞单情况，将判先录入的人为有效。</div>";
									
									if(r.obj && r.obj.length>0){
										$.each(r.obj,function(i,o){
											msgStr += "</br>"+o.name;
										});
									}
									
									$("#errorDiv").html(msgStr);
    								$("#dialog6").dialog("open");
								} else {
									$("#datagrid").datagrid("reload");
									toastr.success("保存成功!");
									$("#dialog").dialog("close");
								}
							} else {
								$.messager.alert("提示",r.error);
							}
						}
					});
				}
    		}
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
		                     <td><a id="onOffBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true">开启</a></td>
		                     <td><div class=" datagrid-btn-separator"></td>
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
  				<td>要备份的目录<br>(回车分隔)：</td>
  				<td><textarea name="dirs" rows="10" cols="40"></textarea></td>
  			</tr>
  		</table>
  		</form>
  	</div>
  </body>
</html>
