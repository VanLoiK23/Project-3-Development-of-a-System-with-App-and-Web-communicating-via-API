<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Thêm bài viết</title>


<meta name="stylesheet"
	content='
	
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/quan_li_sp_style.css' />" />
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/edit_sp.css' />" />
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/add_article.css' />" />
    ' />
<meta name="scripts"
	content='
        	<script src="<c:url value='/template/js/quan_li_articles.js' />"></script>
    ' />

</head>

<body style="background: #D3D3D3;">

	<div class="main-content">

		<section id="wrapper">

		<div class="p-4" id="edit_sp" style="height: 500px !important">
			<div class="table" style="padding: 0 !important;">
				<form:form
					action="http://localhost:8080/Spring-mvc/quan-tri/Articles/add_Articles"
					role="form" enctype="multipart/form-data" method="POST"
					modelAttribute="model">
					<div class="table-header" style="background-color: transparent">
						<p>
							<button type="button"
								style="background-color: #A0F820; scale: calc(0.9)">
								<i style="scale: calc(1.4)"
									class="fa-brands fa-creative-commons-sampling-plus"></i>
							</button>
							Thêm bài viết
						</p>
						<p>
							<button type="submit" id="getHtml" name="submit"
								style="background-color: blue; scale: calc(0.8); width: 180px; margin-right: -30px">
								<i style="margin-right: 10px" class="fa-solid fa-floppy-disk"></i>Lưu
								thay đổi
							</button>
							<button type="button"
								style="background-color: red; scale: calc(0.8); width: 180px; margin-right: -30px; color: white; text-decoration: none;">
								<a style="color: white; text-decoration: none;"
									href='<c:url value="/quan-tri/Articles/danh-sach"/>'><i
									style="margin-right: 10px" class="fa-solid fa-x"></i>Quay lại</a>
							</button>
						</p>
					</div>
					<div class="form">
						<div class="form-group">
							<div>
								<label for="name">Tiêu đề bài viết</label>
								<div>
									<%-- <form:textarea path="title" id="description" rows="2" cols="85"
										style="margin: 10px 0; padding: 2px 10px" required
										placeholder="Tiêu đề bài viết"></form:textarea> --%>
										
										<textarea name="title" id="description" rows="2" cols="85"
										style="margin: 10px 0; padding: 2px 10px" required
										placeholder="Tiêu đề bài viết"></textarea>
								</div>
							</div>
						</div>
						<div class="form-group">
							<input type="hidden" id="htmlContent" name="details" />
							<div>
								<label for="name">Mô tả ngắn</label>
								<div>
									<%-- <form:textarea path="sortDesc" id="description" rows="2"
										cols="85" style="margin: 10px 0;" required
										placeholder="Mô tả ngắn bài viết"></form:textarea> --%>
										<textarea name="sortDesc" id="description" rows="2"
										cols="85" style="margin: 10px 0;" required
										placeholder="Mô tả ngắn bài viết"></textarea>
								</div>
							</div>
						</div>
						<div class="form-group" id="form_img" style="margin: 30px 0;">
							<div class="custom-file-upload">
								<label for="img">Thay đổi ảnh</label> <input type="file"
									name="img" id="img" accept="image/*">

								<div id="preview-container">
									<img id="preview" alt="Preview"
										style="width: 300px; aspect-ratio: 4/3">
								</div>
							</div>
						</div>
						<div id="Desc">
							<div>
								<label for="">Nội dung bài viết:</label>
								<div id="summernote"></div>
							</div>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	</section>
	</div>


</body>

</html>