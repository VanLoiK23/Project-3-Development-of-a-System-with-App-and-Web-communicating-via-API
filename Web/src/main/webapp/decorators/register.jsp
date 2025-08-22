<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title><dec:title default="Đăng ký" /></title>


 <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
<link rel="shortcut icon"
	href="<c:url value='/template/images/favicon.ico' />" />
	    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
	
<link rel="stylesheet"
	href="<c:url value='/template/css/style/register_style.css' />" />


</head>
<body>
	
    <div class="container1">
    	<dec:body/>
    </div>
	
	<script type="text/javascript" src="<c:url value='/template/web/jquery/jquery.min.js' />"></script>
	    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
		<script type="text/javascript" src="<c:url value='/template/js/dangki.js' />"></script>
	
</body>
</html>