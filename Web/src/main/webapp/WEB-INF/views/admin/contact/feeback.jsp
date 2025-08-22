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
			<i class="fa-solid fa-comment-medical"></i>
		</button>
		Phản hồi khách hàng
	</h5>
	<form method="POST"
		action="http://localhost:8080/Spring-mvc/quan-tri/contact/sendEmail">
		<input type="hidden" class="form-control" name="id" id="id" required>
		<input type="hidden" class="form-control" name="email" id="email"
			required> <input type="hidden" class="form-control"
			name="khachhang" id="khachhang" required>
		<div class="form-group1">
			<label for="value">Tên người gửi</label> <input type="text"
				class="form-control" name="name" required>
		</div>
		<div class="form-group1">
			<label>Nội dung</label>
			<textarea name="ct" class="form-control" id="" required
				style="min-height: 150px"></textarea>
		</div>
		<div class="form-footer">
			<button type="submit" name="submit"
				style="scale: calc(0.9); background: green" id="add"
				class="btn btn-primary">
				<i class="fa-regular fa-comment"></i>Gửi phản hồi
			</button>
			<a href="#" id="close1" style="scale: calc(0.9)"
				onclick="closeForm1()" class="btn btn-secondary"><i
				class="fa-solid fa-rotate-left"></i> Quay Lại</a>
		</div>
	</form>
</div>
