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
		Thêm Slide
	</h5>
	<form method="POST" enctype="multipart/form-data"
		action="<c:url value='/quan-tri/slide/add_Slide'/>">
		<div class="form-group1">
			<label for="img">Chọn ảnh</label> <input type="file" name="img"
				id="img1" accept="image/*">
			<div
				style="display: flex; justify-content: center; align-items: center">
				<img style="width: 200px; display: none;" id="preview1"
					alt="Preview">
			</div>
		</div>
		<div class="form-footer">
			<button type="submit" name="submit"
				style="scale: calc(0.9); background: green" id="add"
				class="btn btn-primary">
				<i class="fa-solid fa-calendar-plus"></i> Thêm Slide
			</button>
			<a href="#" id="close1" style="scale: calc(0.9)"
				onclick="closeForm1()" class="btn btn-secondary"><i
				class="fa-solid fa-rotate-left"></i> Quay Lại</a>
		</div>
	</form>
</div>
