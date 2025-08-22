<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<title>Cập nhật sản phẩm</title>

<meta name="stylesheet"
	content='
	
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/quan_li_sp_style.css' />" />
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/edit_sp.css' />" />
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/comment.css' />" />
    ' />
<meta name="scripts"
	content='
        	<script src="<c:url value='/template/js/quan_li_sp.js' />"></script>
    ' />

</head>
<body style="background: #D3D3D3; color: black">

	<div class="main-content">

		<section id="wrapper">

		<div class="p-4" id="edit_sp">
			<div class="table" style="padding: 0 !important;">
				<form
					action="http://localhost:8080/Spring-mvc/quan-tri/Quanlicomment_controller/show_content/${dto.id}"
					method="POST">
					<div class="table-header" style="background-color: transparent">
						<p>
							<button type="button"
								style="background-color: orange; scale: calc(0.8)">
								<i class="fa-solid fa-marker"></i>
							</button>
							Chi tiết bình luận
						</p>
						<p>
							<button type="button"
								style="background-color: red; scale: calc(0.8); width: 180px; margin-right: -30px; color: white; text-decoration: none;">
								<a style="color: white; text-decoration: none;"
									href="http://localhost:8080/Spring-mvc/quan-tri/Quanlicomment_controller/danh-sach"><i
									style="margin-right: 10px" class="fa-solid fa-x"></i>Quay lại</a>
							</button>
						</p>
					</div>
					<div class="form">
						<div class="leftt-form">
							<div class="form-group">
								<input type="hidden" id="id" name="id" value="${dto.id}">
								<div>
									<label for="name">Tên sản phẩm</label> <input
										style="width: 200px !important" type="text"
										class="form-control" id="name" disabled value="${dto.tensp }"
										placeholder="Tên sản phẩm">
								</div>
								<div>
									<label for="name">Khách hàng</label> <input
										style="width: 200px !important" type="text"
										class="form-control" id="name" disabled value="${dto.hoten }"
										placeholder="Tên khách hàng">
								</div>
							</div>

							<div class="form-group">
								<div>
									<label for="name">Số sao đánh giá</label>

									<c:forEach var="j" begin="1" end="5">
										<c:choose>
											<c:when test="${j <= dto.rate}">
												<i style="color: #FFD700;" class="fa-solid fa-star"></i>
											</c:when>
											<c:when test="${j - 0.5 <= dto.rate&& j > dto.rate}">
												<i style="color: #FFD700;"
													class="fa-solid fa-star-half-stroke"></i>
											</c:when>
											<c:otherwise>
												<i style="color: #FFD700;" class="fa-regular fa-star"></i>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</div>
							</div>

							<div class="form-group">
								<div>
									<label for="name">Nội dung đánh giá</label> <input type="text"
										class="form-control"
										style="width: 530px !important; height: 100px;" id="name"
										disabled name="name" value="${dto.content }" disabled>
								</div>
							</div>
							<div class="form-group">
								<c:if test="${not empty dto.img }">
									<label for="name">Ảnh đánh giá</label>
									<img id="preview" src="${dto.img }"
										style="width: 330px !important; height: 200px;" alt="Preview">
								</c:if>

							</div>
							<div class="form-group">
								<div>
									<label for="">Phản hồi</label>

									<textarea style="width: 530px !important; height: 100px;"
										type="text" class="form-control" id="feeback_content"
										name="feeback_content" required>${dto.feedbackContent}</textarea>

									<button type="submit" name="submit"
										style="background-color: blue; scale: calc(0.8); width: 180px; margin-top: 25px; margin-left: -10px;">
										<i style="margin-right: 10px" class="fa-solid fa-comments"></i>
										<c:if test="${dto.feeback==1 }">
                                            Cập nhật phản hồi
                                            </c:if>
										<c:if test="${dto.feeback==0 }">
                                            Gửi phản hồi
                                            </c:if>
									</button>
								</div>
							</div>
						</div>

					</div>
				</form>
			</div>
		</div>
	</div>
	</section>

	</div>


</body>

</html>