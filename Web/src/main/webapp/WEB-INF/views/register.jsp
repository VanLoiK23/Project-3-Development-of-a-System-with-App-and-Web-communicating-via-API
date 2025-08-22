<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>
<c:url var="APIurl" value="/api-admin-new" />
<c:url var="NewURL" value="/admin-new" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>\
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
    <div class="container">
        <form action="dang-ki" method="POST">
            <h2>Tạo tài khoản mới</h2>
            <div class="h">
                Nhanh chóng và dễ dàng
            </div>
            <div class="name">
                <input type="text" autocomplete="off" required placeholder="Họ" name="last_name" id="last_name">
                <input type="text" autocomplete="off" required placeholder="Tên" name="first_name" id="first_name">
                <i class='bx bxs-user'></i>
            </div>
            <div class="sdt">

                <input id="phone" autocomplete="off" type="text" placeholder="Số di động" pattern="\d{10}" title="Số điện thoại phải có 10 chữ số" required name="phone">

                <i style="color:white" class='bx bxs-phone'></i>
            </div>
            <div class="sdt">

                <input id="email" autocomplete="off" type="email" placeholder="Email" required name="email">

                <i class='bx bxs-envelope'></i>
            </div>
            <div class="pass">
                <input type="password" autocomplete="off" required placeholder="Mật khẩu mới" name="password" id="password">
                <i class='bx bxs-lock-alt' id="showPassword" onclick="togglePassword()"></i>
            </div>
           <!--  <div class="pass">
                <input type="tex" autocomplete="off" required placeholder="Địa chỉ" name="address" id="address">
                <i class='bx bxs-user-badge'></i>
            </div> -->
            <div class="date">
                <label class="day" style="position:relative; top:-15px">Ngày sinh</label><br>
                <input type="text" id="datepicker" name="ngaysinh" placeholder="Chọn ngày" required>
                <!-- <i class='bx bxs-calendar-alt'></i> -->
            </div>
            <div class="sex">
                <label style="position:relative;bottom:10px;top:10px;" class="day">Giới tính</label><br><br>
                <div class="female">
                    <p style="line-height:35px ">Nữ</p>
                    <input type="radio" name="gender" value="female" style="color:white; position:relative;top:-35px;left:140px">
                </div>
                <div class="female" style="position:relative;top:-30px;left:180px">
                    <p style="line-height: 35px;">Nam</p>
                    <input type="radio" name="gender" value="male" style="color:white; position:relative;top:-35px;left:140px">
                </div>
                <div class="female" style="position:relative;top:-70px;left:370px">
                    <p style="line-height: 35px;">Tùy chỉnh</p>
                    <input type="radio" name="gender" value="unspecified" style="color:white; position:relative;top:-35px;left:140px">
                </div>
            </div>
            <div class="bt"> <button class="btn" name="submit" type="submit">Đăng ký</button></div>
            <div style="position:relative;top:30px;left:200px"><a href="dang-nhap">Bạn đã có tài khoản ư?</a></div>
        </form>
    </div>
    </div>
   


</body>

</html>