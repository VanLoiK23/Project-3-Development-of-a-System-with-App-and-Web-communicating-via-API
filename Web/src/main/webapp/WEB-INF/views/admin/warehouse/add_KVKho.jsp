<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>


<div class="overlay-background1" onclick="closeForm1()"></div>

<div class="form-container1" id="customer-form1">
	<button class="close-btn1" title="close" onclick="closeForm1()">
		<i class="fa-solid fa-x"></i>
	</button>
	<h5>
		<button class="add">
			<i class="fa-solid fa-circle-plus"></i>
		</button>
		Thêm khu vực kho
	</h5>
	<form method="POST" action="http://localhost:8080/Spring-mvc/quan-tri/khuvuckho/add_KVKho?page=${page}">
		<div class="form-group1">
			<label for="value">Tên Khu Vực Kho</label> <input type="text"
				class="form-control" id="new_name" name="name" required>
		</div>
		<div class="form-group1">
			<label>Ghi chú</label>
			<textarea name="ghichu" class="form-control" id="ghichu1"
				style="height: 150px; padding: 10px"></textarea>
		</div>
		<div class="form-footer">
			<button type="submit" name="submit"
				style="scale: calc(0.9); background: green" id="add"
				class="btn btn-primary">
				<i class="fa-solid fa-calendar-plus"></i> Thêm khu vực kho
			</button>
			<a href="#" id="close1" style="scale: calc(0.9)"
				onclick="closeForm1()" class="btn btn-secondary"><i
				class="fa-solid fa-rotate-left"></i> Quay Lại</a>
		</div>
	</form>
</div>
