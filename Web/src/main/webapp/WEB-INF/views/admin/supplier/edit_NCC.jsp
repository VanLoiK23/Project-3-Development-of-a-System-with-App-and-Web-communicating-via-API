<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>

<div class="overlay-background" onclick="closeForm()"></div>

<div class="form-container" id="customer-form">
	<button class="close-btn" title="close" onclick="closeForm()">
		<i class="fa-solid fa-x"></i>
	</button>
	<h5>
		<button class="edit">
			<i class="fa-solid fa-user-edit"></i>
		</button>
		Sửa Thông Tin Nhà Cung Cấp
	</h5>
	<form method="POST" action="http://localhost:8080/Spring-mvc/quan-tri/supplier/add_NCC">
		<div class="form-group">
			<label for="value">Tên Nhà Cung Cấp</label> <input type="text"
				class="form-control" id="name" name="name" required> <input
				type="hidden" id="id" name="id" required>
		</div>
		<div class="form-group">
			<label>Địa chỉ</label> <input type="text" class="form-control"
				id="address" name="address" required>
		</div>
		<div class="form-group">
			<label>Email</label> <input type="email" class="form-control"
				id="email" name="email" required>
		</div>
		<div class="form-group">
			<label>Số điện thoại</label> <input type="text" class="form-control"
				id="sdt" name="sdt" required>
		</div>
		<div class="form-footer">
			<button type="submit" name="submit" class="btn btn-primary">
				<i class="fa-solid fa-floppy-disk"></i> Lưu Thay Đổi
			</button>
			<a href="#" id="close" onclick="closeForm()"
				class="btn btn-secondary"><i class="fa-solid fa-rotate-left"></i>
				Quay Lại</a>
		</div>
	</form>
</div>

