<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<link rel="stylesheet" type="text/css" href="<%=path%>/css/my.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/script/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/script/easyui/themes/metro-blue/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/script/toastr/toastr.min.css">
<script type="text/javascript" src="<%=path%>/script/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="<%=path%>/script/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=path%>/script/easyui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=path%>/script/easyui/easyui-my-extend.js"></script>
<script type="text/javascript" src="<%=path%>/script/myUtil.js"></script>
<script type="text/javascript" src="<%=path%>/script/easyui/jquery.easyui.rules.js"></script>
<script type="text/javascript" src="<%=path%>/script/jquery.json-2.3.js"></script>
<script type="text/javascript" src="<%=path%>/script/lodop/LodopFuncs.js"></script>
<script type="text/javascript" src="<%=path%>/script/toastr/toastr.min.js"></script>
<script type="text/javascript" src="<%=path%>/script/DateUtil.js"></script>