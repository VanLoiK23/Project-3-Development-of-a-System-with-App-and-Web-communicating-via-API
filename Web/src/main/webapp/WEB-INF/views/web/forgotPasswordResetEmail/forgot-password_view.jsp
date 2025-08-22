<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<title>Forgot Password</title>

<meta name="stylesheet"
	content='
		 <link rel="stylesheet"
				href="<c:url value='/template/css/style/forgot-password_style.css' />" />

		    ' />
</head>

<body>
	<div class="main-content">
	
	<c:if test="${not empty message}">
			<script>
				alert('${message}');
			</script>
		</c:if>
		<div class="form">
			<h1>Tìm tài khoản của bạn</h1>
			<hr>
			<form action="http://localhost:8080/Spring-mvc/send-Password-Reset"
				method="POST">
				<label for="email"> Vui lòng nhập email để tìm kiếm tài
					khoản của bạn. </label><br> <input placeholder="Điền email của bạn "
					type="email" name="email" id="email" required>
				<div class="button">
					<a href="http://localhost:8080/Spring-mvc/dang-nhap">Hủy</a>
					<button>Tìm kiếm</button>
				</div>
				<br>
			</form>
		</div>
	</div>
</body>

</html>