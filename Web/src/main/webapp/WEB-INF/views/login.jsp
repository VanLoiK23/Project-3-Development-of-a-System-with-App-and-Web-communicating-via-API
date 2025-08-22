<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>
<c:url var="APIurl" value="/api-admin-new" />
<c:url var="NewURL" value="/admin-new" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>


</head>

<body>

	<div class="main-content">
		<c:if test="${not empty message}">
			<script>
				alert('${message}');
			</script>
		</c:if>

		<c:if test="${param.error != null}">
			<script>
				alert('${param.error}');
			</script>
		</c:if>

		<%
		String message = (String) session.getAttribute("FLASH_MESSAGE");
		if (message != null) {
		%>
		<script>
		alert('<%=message%>');
		</script>
		<%
		session.removeAttribute("FLASH_MESSAGE"); // Xóa sau khi hiển thị
		}
		%>

	<%-- 	<c:if test="${not empty sessionScope.FLASH_MESSAGE}">
			<div class="alert">${sessionScope.FLASH_MESSAGE}</div>
			<c:remove var="FLASH_MESSAGE" scope="session" />
		</c:if> --%>


		<div class="wrapper">
			<form action="<c:url value='/process-login'/>" method="POST">
				<h1>Đăng nhập</h1>
				<div class="input-box">
					<input type="text" autocomplete="off" placeholder="Tên đăng nhập"
						name="username" required> <i class='bx bxs-user'></i>
				</div>
				<div class="input-box">
					<input type="password" autocomplete="off" placeholder="Mật khẩu"
						name="password" required> <i class='bx bxs-lock-alt'></i>
				</div>

				<div class="remember-forgot">
					<label> <input type="checkbox" name="check">Nhớ
						đăng nhập
					</label> <a href="send-Password-Reset">Quên mật khẩu</a>
				</div>

				<div class="bt">
					<button class="btn" name="login" type="submit">Đăng nhập</button>
				</div>

				<div class="register-link">
					<p>
						Bạn chưa có tài khoản? <a href="dang-ki">Ấn vào đây</a>
					</p>
				</div>
			</form>
		</div>
	</div>
</body>

</html>