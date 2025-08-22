
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>


<div class="overlay-background1" onclick="closeForm1()"></div>

<div class="form-container1" id="customer-form1">
	<button class="close-btn1" title="close" onclick="closeForm1()">
		<i class="fa-solid fa-x"></i>
	</button>
	<h5>
		<button class="add">
			<i class="fa-solid fa-circle-plus"></i>
		</button>
		Thêm nhà cung cấp
	</h5>
	<form method="POST" action="http://localhost:8080/Spring-mvc/quan-tri/supplier/add_NCC">
		<div class="form-group1">
			<label for="value">Tên nhà cung cấp</label> <input type="text"
				class="form-control" id="new_name" name="name" required>
		</div>
		<div class="form-group1">
			<label>Địa chỉ</label> <input type="text" class="form-control"
				id="addres1" name="address" required>
		</div>
		<div class="form-group1">
			<label>Email</label> <input type="email" class="form-control"
				id="email1" name="email" required>
		</div>
		<div class="form-group1">
			<label>Số điện thoại</label> <input type="text" class="form-control"
				id="sdt1" name="sdt" required>
		</div>
		<div class="form-footer">
			<button type="submit" name="submit"
				style="scale: calc(0.9); background: green" id="add"
				class="btn btn-primary">
				<i class="fa-solid fa-calendar-plus"></i> Thêm nhà cung cấp
			</button>
			<a href="#" id="close1" style="scale: calc(0.9)"
				onclick="closeForm1()" class="btn btn-secondary"><i
				class="fa-solid fa-rotate-left"></i> Quay Lại</a>
		</div>
	</form>
</div>

