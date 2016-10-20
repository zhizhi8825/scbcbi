<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <title>页面跳转</title>
    <script type="text/javascript">
    	window.top.location = "<%=path%>/userAction/admin.action";
    </script>
  </head>
  
  <body>
  </body>
</html>
