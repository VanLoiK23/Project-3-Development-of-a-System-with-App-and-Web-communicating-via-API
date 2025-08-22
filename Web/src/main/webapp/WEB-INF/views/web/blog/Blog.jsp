<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>Bài viết</title>

<meta name="stylesheet"
	content='
	 <link rel="stylesheet"
			href="<c:url value='/template/css/style/style1.css' />" />
	 <link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/vendor/linearicons.min.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/vendor/font.awesome.min.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/blog.css' />" />
	
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link
        href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
        rel="stylesheet">
    
    <link
        href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&family=Mulish:ital,wght@0,200..1000;1,200..1000&family=Pacifico&display=swap"
        rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Great+Vibes&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Josefin+Slab:ital,wght@0,100..700;1,100..700&display=swap"
        rel="stylesheet">
    

	    ' />

<meta name="scripts"
	content='
	        	<script src="<c:url value='/template/js/user/blog_comment.js' />"></script>
	        	
	    ' />



</head>

<body>

	<div class="main-content">

		<div class="breadcrumbs-area position-relative" id="b">

			<div class="image-left">
				<!-- Ảnh bên trái bao phủ toàn bộ chiều cao -->
				<img
					src="https://i.ibb.co/N26GqgqN/dang-ky-truyen-hinh-fpt-nhan-ngay-uu-dai-khung-khi-mua-smart-tv-samsung-1602500823.png"
					alt="Image Left">
			</div>
			<div class="container">
				<div class="row">
					<div class="col-12 text-center">
						<div class="breadcrumb-content position-relative section-content"
							id="re">
							<h3 class="title-3" id="tt1">Bài báo về công nghệ</h3>
							<ul>
								<li><a id="tt2"
									href="http://localhost:8080/Spring-mvc/trang-chu">Trang chủ</a></li>
								<li id="tt3">Bài đăng</li>
							</ul>
						</div>
					</div>
				</div>
			</div>

			<div class="image-right">
				<!-- Ảnh bên phải bao phủ toàn bộ chiều cao -->
				<img
					src="https://i.ibb.co/0V89M78b/gioi-thieu-ban-be-su-dung-internet-truyen-hinh-fpt-nhan-qua-laptop-sieu-mong-1602476284.png"
					alt="Image Right">
			</div>
		</div>




		<div class="blog-main-area" id="bl"
			<c:if test="${not empty isDetail}">
			style="display: none "
			</c:if>>
			<div class="container container-default custom-area">
				<div class="row">
					<div class="col-lg-12 col-12 col-custom widget-mt">
						<div class="row" id="r">

							<c:if test="${not empty  articles.listResult}">
								<c:forEach var="item" items="${articles.listResult }">


									<div class="col-12 col-md-6 col-lg-4 col-custom mb-30">
										<div class="blog-lst">
											<div class="single-blog">
												<div class="blog-image">
													<a class="d-block" href="#"> <img id="im"
														src="${item.image }" style="height: 150px"
														alt="Blog Image" class="w-100">
													</a>
												</div>
												<div class="blog-content">
													<div class="blog-text">
														<h5
															style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>
															<a
																href="http://localhost:8080/Spring-mvc/Blog/${item.id }">
																${item.title } </a>
														</h5>
														<div class="blog-post-info">
															<span><a href="#">By admin</a></span> <span>
																<td><fmt:formatDate value="${item.createdDate}"
																		pattern="dd-MM-yyyy" /></td>
															</span>
														</div>
														<p
															style='overflow: hidden; text-overflow: ellipsis; height: 100px'>
															${item.sortDesc }</p>
													</div>
												</div>
											</div>
										</div>
									</div>

								</c:forEach>
							</c:if>
						</div>

					</div>
				</div>
			</div>
		</div>


		<c:if test="${not empty isDetail}">
			<div class="blog-main-area" id="main">
				<div class="container container-default custom-area">
					<div class="row">
						<div class="col-12 col-custom widget-mt">
							<div class="blog-post-details">
								<input type="hidden" id="idBlog" value="${articles.id}">

								<figure class="blog-post-thumb mb-5"> <img
									src="${articles.image}" style="width: 100%;" alt="Blog Image"
									id="mn"> </figure>
								<section class="blog-post-wrapper product-summery">
								<div class="section-content section-title">
									<h6 class="section-title-2 mb-3" style="font-size: 23px">
										${articles.title}</h6>
									<p class="mb-4">${articles.sortDesc}</p>
									<blockquote class="blockquote mb-4">
										<p>${articles.details}</p>
									</blockquote>
								</div>
								<div class="share-article">
									<span class="left-side"> <a
										href="http://localhost:8080/Spring-mvc/Blog/${preId}"> <i
											class="fa fa-long-arrow-left"></i> Trang trước
									</a>
									</span>
									<h6 class="text-uppercase">Chia sẻ bài viết này</h6>
									<span class="right-side"> <a
										href="http://localhost:8080/Spring-mvc/Blog/${nextId}">Trang
											sau <i class="fa fa-long-arrow-right"></i>
									</a>
									</span>
								</div>
								<div class="social-share">
									<a href="#"><i class="fa fa-facebook-square facebook-color"></i></a>
									<a href="#"><i class="fa fa-twitter-square twitter-color"></i></a>
									<a href="#"><i class="fa fa-linkedin-square linkedin-color"></i></a>
									<a href="#"><i
										class="fa fa-pinterest-square pinterest-color"></i></a>
								</div>

								</section>
							</div>
						</div>
					</div>
				</div>


				<button class="rate-product-btn"
					style="margin-left: 50px; border-radius: 5px; color: white; background-color: blue; outline: 0; border: 0; padding: 7px"
					onclick="openReviewForm()">Xem thêm đánh giá</button>
				<div class="comment-container" id="comment-container">
					<!-- Comment 1 -->

					<c:if test="${not empty comments.listResult }">

						<c:forEach var="item" items="${comments.listResult }"
							varStatus="status">
							<c:if test="${status.index < 5}">

								<div class="comment-item">
									<div class="comment-author">
										<span class="author-name"> ${item.nameClient } </span> <span
											class="comment-date"> ${item.ngayDanhGiaString } </span>
									</div>
									<p class="comment-content">${item.content }</p>

								</div>

								<c:if test="${item.feedback eq 1 }">
									<div class="feedback-container"
										style="border-left: 6px solid blue">
										<div class="feedback-header">
											<div class="employee-info">
												<span class="employee-name"> ${item.nhanVien } </span> <span
													class="feedback-time">Phản hồi lúc:
													${item.ngayPhanHoiString }</span>
											</div>
										</div>
										<div class="feedback-content">${item.contentFeedback }</div>
									</div>

								</c:if>
							</c:if>
						</c:forEach>
					</c:if>



				</div>





				<c:if test="${not empty nameUser}">
					<div class="comment-form">
						<c:set var="id" value="${articles.id}" />

						<h3>Để lại đánh giá</h3>
						<form id="commentForm">
							<!-- Đánh giá sao -->
							<div class="star-rating">
								<span data-value="1" class="star">&#9733;</span> <span
									data-value="2" class="star">&#9733;</span> <span data-value="3"
									class="star">&#9733;</span> <span data-value="4" class="star">&#9733;</span>
								<span data-value="5" class="star">&#9733;</span>
							</div>
							<input type="hidden" id="id" name="id"
								value="${id}">
							<!-- Tên người dùng -->
							<div class="form-group">
								<label for="name">Tên của bạn</label> <input type="text"
									id="name" name="name" placeholder="Nhập tên của bạn"
									value="${nameUser}" readonly required>
							</div>
							<!-- Nội dung bình luận -->
							<div class="form-group">
								<label for="comment">Bình luận</label>
								<textarea id="comment" name="comment"
									placeholder="Viết bình luận của bạn..." required></textarea>
							</div>
							<!-- Nút gửi -->
							<button type="button" class="btn-submit" id="btn-submit">Gửi
								bình luận</button>
						</form>
					</div>
			</div>
	</div>
	</c:if>


	<div class="blog-main-area" id="bl">
		<div class="container container-default custom-area">
			<div class="row">
				<h2 class="section-title-2 mb-3" id="hh"
					style="display: flex; justify-content: center; align-items: center;">Các
					bài viết khác</h2>
				<div class="col-lg-12 col-12 col-custom widget-mt">
					<div class="row" id="r">
						<c:forEach var="item" items="${articles.listResult }">


							<div class="col-12 col-md-6 col-lg-4 col-custom mb-30">
								<div class="blog-lst">
									<div class="single-blog">
										<div class="blog-image">
											<a class="d-block" href="#"> <img src="${item.image }"
												style="height: 150px" alt="Blog Image" id="im" class="w-100">
											</a>
										</div>
										<div class="blog-content">
											<div class="blog-text">
												<h5
													style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>
													<a
														href="http://localhost:8080/Spring-mvc/Blog/${item.id }">
														${item.title } </a>
												</h5>
												<div class="blog-post-info">
													<span><a href="#">By admin</a></span> <span> <fmt:formatDate
															value="${item.createdDate}" pattern="dd-MM-yyyy" />
													</span>
												</div>
												<p
													style='overflow: hidden; text-overflow: ellipsis; height: 120px'>
													${item.sortDesc }</p>
											</div>
										</div>
									</div>
								</div>
							</div>

						</c:forEach>

					</div>

				</div>
			</div>
		</div>
	</div>
	</c:if>


	<div class="review-form" id="reviewForm" style="display: none">
		<button class="close-btn" id="closeBtn">×</button>
		<div class="review-header" style="display: flex">

			<div>Xem Đánh Giá</div>

			<div class="filter-container" style="margin-left: 180px">
				<p>Lọc theo sao:</p>
				<select id="timeFilter" style="color: yellow;">
					<option value="1">★</option>
					<option value="2">★★</option>
					<option value="3">★★★</option>
					<option value="4">★★★★</option>
					<option value="5">★★★★★</option>
					<option value="all">Bỏ lọc</option>
				</select>
			</div>
		</div>
		<div class="review-content" id="reviewContent">
			<c:if test="${not empty comments.listResult }">

				<c:forEach var="item" items="${comments.listResult }"
					varStatus="status">
					<div class="review-item">
						<div class="review-name">${item.nameClient }</div>
						<div class="review-rating">
							<c:set var="countRate" value="5" />
							<c:set var="rate" value="${item.rating }" />

							<c:forEach var="i" begin="1" end="${countRate}">
								<c:choose>
									<c:when test="${rate >= i}">
										<i class="fa fa-star" style="color: orange"></i>
									</c:when>
									<c:when test="${rate >= i - 0.5}">
										<i class="fa-solid fa-star-half-stroke" style="color: orange"></i>
									</c:when>
									<c:otherwise>
										<i class="fa fa-star-o" style="color: orange"></i>
									</c:otherwise>
								</c:choose>
							</c:forEach>

						</div>
						<div class="review-text">${item.content }</div>

						<c:if test="${item.feedback eq 1 }">
							<div class="feedback-container"
								style="border-left: 6px solid blue; width: 400px">
								<div class="feedback-header">
									<div class="employee-info">
										<span class="employee-name">${item.nhanVien } </span> <span
											class="feedback-time">Phản hồi lúc:
											${item.ngayPhanHoiString }</span>
									</div>
								</div>
								<div class="feedback-content">${item.contentFeedback }</div>
							</div>
						</c:if>
					</div>


				</c:forEach>
			</c:if>
		</div>
	</div>

	</div>



	<script>
		function openReviewForm() {
			document.getElementById("reviewForm").style.display = "block";
		}

		// Close the form
		document.getElementById("closeBtn").onclick = function() {
			document.getElementById("reviewForm").style.display = "none";
		};

		document.getElementById("timeFilter").addEventListener("change",
				function() {
					const selectedValue = this.value;

					// Đảm bảo PHP giá trị được chèn chính xác vào JavaScript
					const idsp = document.getElementById("idBlog").value;

					// Gửi dữ liệu đã chọn đến server
					  fetch("http://localhost/DACS2/Blog/filter", {
					         method: "POST",
					         headers: {
					             "Content-Type": "application/x-www-form-urlencoded",
					         },
					         body: `filter=` + encodeURIComponent(selectedValue) + `&id_blog=` + encodeURIComponent(idsp), 
					     })
					     .then((response) => {
					         if (!response.ok) {
					             throw new Error(`HTTP error! status: ${response.status}`);
					         }
					         return response.text(); // Dự đoán trả về HTML
					     })
					     .then((html) => {
					         const tableElement = document.getElementById("reviewContent");
					         if (tableElement) {
					             tableElement.innerHTML = html; // Cập nhật nội dung bảng
					         }
					     })
					     .catch((error) => {
					         console.error("Request failed:", error);
					         alert("Đã xảy ra lỗi khi tải dữ liệu. Vui lòng thử lại.");
					     }); 
				});
	</script>

</body>

</html>