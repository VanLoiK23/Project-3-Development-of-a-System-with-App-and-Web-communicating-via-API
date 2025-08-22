<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Reset Password</title>


<meta name="stylesheet"
	content='
		 <link rel="stylesheet"
				href="<c:url value='/template/css/style/reset-password.css' />" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/water.css@2/out/water.css">

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
			<h1>Đặt lại mật khẩu</h1>
			<form method="POST"
				action="reset-new-password">

				<input type="hidden" name="token" value="${token}"> <label
					for="password">Mật khẩu mới</label> <input type="password"
					id="password" name="password" required> <label
					for="password_confirmation">Xác nhận lại mật khẩu</label> <input
					type="password" id="password_confirmation"
					name="password_confirmation" required> <input type="submit"
					name="submit" ${empty token ? 'disabled' : ''} value="Đổi mật khẩu">
			</form>
		</div>
	</div>
</body>

</html>
