<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>403 Don't permitssion to access</title>

<%-- <meta name="stylesheet"
	content='
	  	<link rel="stylesheet"
			href="<c:url value='/template/css/style/404.css' />" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/css/bootstrap.min.css">
<link
	href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans&display=swap"
	rel="stylesheet">

	    ' />
 --%>
<link rel="stylesheet"
	href="<c:url value='/template/css/style/404.css' />" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/css/bootstrap.min.css">
<link
	href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans&display=swap"
	rel="stylesheet">
</head>

<body>

	<div class="main-content">

		<div class="container">
			<h1>403</h1>
			<p class="motto">Bạn không có quyền truy cập vào trang này!</p>
			<p class="info-text">Vui lòng liên hệ quản trị viên nếu bạn cho
				rằng đây là lỗi.</p>
			<a href="http://localhost:8080/Spring-mvc/trang-chu" class="btn-home">Về
				trang chủ</a>
		</div>
	</div>
</body>

</html>