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
		Sửa Slide
	</h5>
	<form method="POST" enctype="multipart/form-data"
		action="<c:url value='/quan-tri/slide/add_Slide'/>">
		<div class="form-group">
			<label for="img">Thay đổi ảnh</label> <input type="file" name="img"
				id="img" accept="image/*">
			<div
				style="display: flex; justify-content: center; align-items: center">
				<img style="width: 200px;" id="preview" alt="Preview">
			</div>
			<input type="hidden" id="id" name="id" required>
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

