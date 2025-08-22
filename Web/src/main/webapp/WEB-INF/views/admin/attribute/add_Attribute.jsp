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
		Thêm

		<%
		Map<String, String> map2 = new HashMap<>();
		map2.put("xuatxu", "xuất xứ");
		map2.put("thuonghieu", "thương hiệu");
		map2.put("hedieuhanh", "hệ điều hành");
		map2.put("mausac", "màu sắc");
		map2.put("dungluongram", "dung lượng ram");
		map2.put("dungluongrom", "dung lượng rom");

		Object pageObj2 = request.getAttribute("type"); // Lấy từ request scope
		String value2 = (pageObj2 != null) ? (String) pageObj2 : "";

		String label2 = map2.get(value2);
		if (label2 != null) {
			out.print(label2);
			request.setAttribute("label2", label2);
		} else {
			out.print("màu sắc");
		}
		%>
	</h5>
	<form method="POST" enctype="multipart/form-data"
		action="http://localhost:8080/Spring-mvc/quan-tri/attribute-sp/add_Attribute?type=${type}&page=${page}">
		<div class="form-group1">
			<label for="value"> ${label2} </label> <input type="text"
				category="${type}" class="form-control" id="new_name" name="value"
				required>
			<c:if test="${type=='thuonghieu'}">
				<label for="img">Chọn ảnh</label>
				<input type="file" name="img" id="img1" accept="image/*">
				<div
					style="display: flex; justify-content: center; align-items: center">
					<img style="width: 200px; display: none;" id="preview1"
						alt="Preview">
				</div>

			</c:if>
		</div>
		<div class="form-footer">
			<button type="submit" name="submit"
				style="scale: calc(0.9); background: green" id="add"
				class="btn btn-primary">
				<i class="fa-solid fa-calendar-plus"></i> Thêm ${label2}
			</button>
			<a href="#" id="close1" style="scale: calc(0.9)"
				onclick="closeForm1()" class="btn btn-secondary"><i
				class="fa-solid fa-rotate-left"></i> Quay Lại</a>
		</div>
	</form>
</div>