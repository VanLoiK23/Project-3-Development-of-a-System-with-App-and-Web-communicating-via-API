<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>


<div class="overlay-background"></div>

<div class="form-container" id="customer-form">
	<button class="close-btn" title="close" onclick="closeForm()">
		<i class="fa-solid fa-x"></i>
	</button>
	<h5>
		<button class="edit">
			<i class="fa-solid fa-user-edit"></i>
		</button>
		Sửa Thông Tin Khách Hàng
	</h5>
	<form method="POST"
		action="<c:url value='/quan-tri/Quanlikhachhang_controller/edit_KH'/>">
		<div class="form-group">
			<label for="name">Tên Khách Hàng</label> <input type="text"
				class="form-control" id="name" name="name" required> <input
				type="hidden" id="id" name="id" required>
		</div>
		<div class="form-group">
			<label for="phone">Số Điện Thoại</label> <input type="text"
				class="form-control" id="phone" name="soDienThoai" required>
		</div>
		<div class="form-group">
			<label for="email">Email</label> <input type="email"
				class="form-control" id="email" name="email" required>
		</div>
		<div class="form-group">
			<label for="ngaysinh">Ngày Sinh</label> <input type="date"
				class="form-control" id="ngaysinh" name="ngaysinh" required>
		</div>
		<div class="form-group">
			<label for="gender">Giới Tính</label> <select class="form-control"
				id="gender" name="gender" required>
				<option value="male" selected>Nam</option>
				<option value="female">Nữ</option>
			</select>
		</div>

		<div class="form-footer">
			<button type="submit" name="submit" class="btn btn-primary">
				<i class="fa-solid fa-floppy-disk"></i> Lưu Thay Đổi
			</button>
			<a href="#" id="close" class="btn btn-secondary"><i
				class="fa-solid fa-rotate-left"></i> Quay Lại</a>
		</div>
	</form>
</div>