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
		Sửa Thông Tin
		<%
		Map<String, String> map3 = new HashMap<>();
		map3.put("xuatxu", "xuất xứ");
		map3.put("thuonghieu", "thương hiệu");
		map3.put("hedieuhanh", "hệ điều hành");
		map3.put("mausac", "màu sắc");
		map3.put("dungluongram", "dung lượng ram");
		map3.put("dungluongrom", "dung lượng rom");

		Object pageObj3 = request.getAttribute("type"); // Lấy từ request scope
		String value3 = (pageObj3 != null) ? (String) pageObj3 : "";

		String label3 = map3.get(value3);
		if (label3 != null) {
			out.print(label3);
			request.setAttribute("label3", label3);
		} else {
			out.print("màu sắc");
		}
		%>
	</h5>
	<form method="POST" enctype="multipart/form-data"
		action="http://localhost:8080/Spring-mvc/quan-tri/attribute-sp/add_Attribute?type=${type}&page=${page}">
		<div class="form-group">
			<label for="value"> ${label3} </label> <input type="text"
				class="form-control" id="value" name="value" required>
			<c:if test="${type=='thuonghieu'}">
				<label for="img">Thay đổi ảnh</label>
				<input type="file" name="img" id="img" accept="image/*">
				<div
					style="display: flex; justify-content: center; align-items: center">
					<img style="width: 200px;" id="preview" alt="Preview">
				</div>
			</c:if>
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

