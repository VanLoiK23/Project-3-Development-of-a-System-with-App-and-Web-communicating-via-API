<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>


<div class="overlay-background" onclick="closeForm()"></div>

<div class="form-container" id="customer-form">
	<button class="close-btn" title="close" onclick="closeForm()">
		<i class="fa-solid fa-x"></i>
	</button>
	<h5>
		<button class="edit">
			<i class="fa-solid fa-user-edit"></i>
		</button>
		Sửa Thông Tin Khu Vực Kho
	</h5>
	<form method="POST"
		action="http://localhost:8080/Spring-mvc/quan-tri/khuvuckho/add_KVKho?page=${page}">
		<div class="form-group">
			<label for="value">Tên Khu Vực Kho</label> <input type="text"
				class="form-control" id="name" name="name" required> <input
				type="hidden" id="id" name="id" required>
		</div>
		<div class="form-group">
			<label>Ghi chú</label>
			<textarea name="ghichu" class="form-control" id="ghichu"
				style="height: 150px; padding: 10px"></textarea>
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
