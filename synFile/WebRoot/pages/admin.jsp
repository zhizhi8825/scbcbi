<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Object user = request.getSession().getAttribute("user");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>客户关系管理系统之报表管理</title>
    <link rel="shortcut icon" type="image/x-icon" href="<%=path%>/images/login/title_ico.ico" />
    
	<jsp:include page="/pages/include.jsp"/>
	<style>
		#login:hover{
			background-image: url('<%=path%>/images/login/btn_login.png');
			background-repeat:no-repeat;
			width:300px;
			height:36px;
			cursor:pointer;
		}
		
		#login{
			background: url('<%=path%>/images/login/btn_login_pre.png') no-repeat;
			width:300px;
			height:36px;
			position:absolute;
			top:445px;
			left:163px;
			cursor:pointer;
		}
		
		.input{
			background: url('<%=path%>/images/login/login_input.png') no-repeat;
			width:300px;
			height:36px;
		}
	</style>
	<script>
		<% if(user != null) { %>
		location.href = "<%=path%>/userAction/main.action";
		<%}%>
		
		$(function(){
			//登陆按钮
			$("#login").click(function(){
				$("#form1").submit();
			});
			
			//给用户和密码加上回车事件
			$("#userName,#password").keypress(function(data){
    			if(data.keyCode==13){
    				$("#login").click();
    			}
    		});
    		
    		
    		$("#userName").keyup(function(e){
    		 		var userName =  $("#userName").val();
		    		if(userName!=null&&userName!=""){
		    			$("#lable_u").hide();
		    		}else{
		    			$("#lable_u").show();
		    		}
    		 });
    		 
    		$("#password").keyup(function(e){
    		 		var password =  $("#password").val();
		    		if(password!=null&&password!=""){
		    			$("#lable_p").hide();
		    		}else{
		    			$("#lable_p").show();
		    		}
    		 });
    		 
    		 $("#userName").focus(function(){
		    		$("#lable_u").hide();
    		 });
    		 $("#userName").blur(function(){
		    		var userName =  $("#userName").val();
		    		if(userName!=null&&userName!=""){
		    			$("#lable_u").hide();
		    		}else{
		    			$("#lable_u").show();
		    		}
    		 });
    		 
    		  $("#password").focus(function(e){
    		 		$("#lable_p").hide();
    		 });
    		  $("#password").blur(function(){
		    		var password =  $("#password").val();
		    		if(password!=null&&password!=""){
		    			$("#lable_p").hide();
		    		}else{
		    			$("#lable_p").show();
		    		}
    		 });
		});
	</script>
  </head>
  
  <body style="padding:0px;margin:0px;">
  	<form id="form1" action="<%=path%>/userAction/login.action" method="post">
	<img src="<%=path%>/images/login/bg.png" style="position:absolute;top:0px;left:0px;width:100%;height:100%"/>
	<div id="login_bg"  style="position:absolute;width:840px;height:510px;top:15%;left:31%;">
		<img src="<%=path%>/images/login/login_bg.png" style="position:absolute;top:0px;left:0px;width:649px;"/>
		<div  style="position:absolute;top:294px;left:195px;">
			<input style="border:0 hidden;width:270px;height:26px;margin-top:5px;margin-left:1px;background:rgba(0, 0, 0, 0);" type="text"id="userName" name="userName" />
		</div>
		
		<div style="position:absolute;top:349px;left:195px">
			<input  style="border:0 hidden;width:270px;height:26px;margin-top:5px;margin-left:1px;background:rgba(0, 0, 0, 0);" type="password" id="password" name="password" />
		</div>
		<div id="login" ></div>
 		<div style="position: absolute ; top:405px;left:193px;color:red;">${error}</div>
	</div>
  	</form>
  </body>
</html>
