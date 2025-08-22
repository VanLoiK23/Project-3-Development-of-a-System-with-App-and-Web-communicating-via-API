<%@include file="/common/taglib.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm bài viết</title>
</head>
<body>
	<div class="main-content">
		<h2>Thêm bài viết mới</h2>
		<form:form method="POST" action="uploadFile"
			enctype="multipart/form-data" modelAttribute="myFile">
    File: <input type="file" name="multipartFile" />
			<br />
			<br />
    Description: <form:input path="description" />
			<br />
			<input type="submit" value="Submit" />
		</form:form>
	</div>
</body>
</html>
