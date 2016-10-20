<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Object user = request.getAttribute("user");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>客户关系管理系统之报表管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="<%=path%>/images/login/title_ico.ico" />
    
    <jsp:include page="/pages/include.jsp"/>
    <script type="text/javascript">
    	$(function(){
    		$("#menuTree").tree({
    		    url:"<%=path%>/menuAction/initUserMenuTree.action",
    		    animate:true,
    		    lines:true,
    		    onClick:function(node){
    		    	if(node.attributes && node.attributes.url){
    		    		addTabs(node.text,node.attributes.url);
    		    	}else{
    		    		if(node.state == "closed")
    		    			$("#menuTree").tree("expand",node.target);
    		    		else
    		    			$("#menuTree").tree("collapse",node.target);
    		    	}
    		    }
    		});
    		
    		//修改密码
    		$("#showName").click(function(){
    			$("#updatePasswordDialog").dialog("open");
    		});
    		
    		//修改密码窗口
    		$("#updatePasswordDialog").dialog({
    			title:"修改密码",
    			iconCls:"icon-edit",
    			width:400,
    			closed:true,
    			modal:true,
    			buttons:[{
    				text:"确定",
    				iconCls:"icon-save",
    				handler:function(){
    					var param = $("#form1").form("getData");
    					
    					$.ajax({
    						url:"<%=path%>/userAction/updatePassword.action",
    						type:"POST",
    						data:param,
    						dataType:"json",
    						success:function(r){
    							if(r.result) {
    								$("#updatePasswordDialog").dialog("close");
    								toastr.success("修改成功");
    							} else {
    								$.messager.alert("提示",r.error);
    							}
    						}
    					});
    				}
    			},{
    				text:"取消",
    				iconCls:"icon-cancel",
    				handler:function(){
    					$("#updatePasswordDialog").dialog("close");
    				}
    			}]
    		});
    	});
    	
    	//子页面添加tabs方法
		var addTabs = function(title,url,closable){
			var tab = $("#centerTabs").tabs("getTab",title);
			var tabCount = $("#centerTabs").tabs("tabs").length;//统计打开窗口个数(不包含首页)
			url = "<%=path%>/"+url;
			if(tab){
				//如果已经打开该tab页，显示出来
				$("#centerTabs").tabs("select",title);
			}else{
				if (tabCount <= 8) {//Tab 数量限制
					var content = "<iframe scrolling='auto' frameborder='0' style='width:100%;height:100%' src='"+url+"'></iframe>";
					$("#centerTabs").tabs("add",{
						title:title,
						closable:true,
						fit:true,
						content:content
					});
				}else{
					alert("您打开窗口太多，请关闭没用窗口!");
				}
			}
    	};
    	
    </script>
  </head>
  
  <body class="easyui-layout">
  	<div region="north" style="height:80px;background-color:#99CCCC; ">
  		<div style="position:absolute;left:20px;top:7px;">
  			<img src="<%=path%>/images/main/logo.jpg" height="70px;"/>
  		</div>
  		<div style="position:absolute;right:280px;top:50px;font-size:large;">
  			您好！<a href="#" id="showName" style="text-decoration: none;">${user.showName }</a>
  		</div>
  		<div style="position:absolute;right:20px;">
  			<a href="<%=path%>/userAction/logout.action">
  				<img src="<%=path%>/images/main/logout.png" height="70px;"/>
  			</a>
  		</div>
  	</div>
  	<div region="west" style="width:150px" title="导航菜单" split="true">
  		<ul id="menuTree"></ul>
  	</div>
  	<div region="center">
  		<div class="easyui-tabs" id="centerTabs" fit="true">
  		</div>
  	</div>
  	
  	<div id="updatePasswordDialog">
  		<form id="form1">
	  		<table class="myTable">
	  			<tr>
	  				<td>旧密码：</td>
	  				<td><input type="text" class="easyui-validatebox" name="oldPassword" data-options="required:true"/></td>
	  			</tr>
	  			<tr>
	  				<td>新密码：</td>
	  				<td><input type="text" class="easyui-validatebox" id="password" name="password" data-options="required:true"/></td>
	  			</tr>
	  			<tr>
	  				<td>再次输入密码：</td>
	  				<td><input type="text" class="easyui-validatebox" name="password2" validType="compare['password']" data-options="required:true"/></td>
	  			</tr>
	  		</table>
  		</form>
  	</div>
  </body>
</html>
