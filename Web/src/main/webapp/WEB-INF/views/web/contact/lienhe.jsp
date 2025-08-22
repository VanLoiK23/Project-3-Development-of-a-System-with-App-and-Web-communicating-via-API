<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<title>Liên hệ</title>

<meta name="stylesheet"
	content='
	 <link rel="stylesheet"
			href="<c:url value='/template/css/style/style1.css' />" />
	 <link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/vendor/linearicons.min.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/vendor/font.awesome.min.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/contact.css' />" />
			
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


</head>

<body>

	<c:if test="${not empty message}">
		<script>
			alert('${message}');
		</script>
	</c:if>

	<div class="main-content">
		<div class="breadcrumbs-area position-relative" id="b">

			<div class="image-left">
				<!-- Ảnh bên trái bao phủ toàn bộ chiều cao -->
				<img
					src="https://i.ibb.co/ZR8j4Sc2/8e30a177c1a04ef33305d532299d245c.jpg"
					alt="Image Left">
			</div>
			<div class="container">
				<div class="row">
					<div class="col-12 text-center">
						<div class="breadcrumb-content position-relative section-content"
							id="re">
							<h3 class="title-3" id="tt1">Liên hệ</h3>
							<ul>
								<li><a id="tt2"
									href="http://localhost:8008/Spring-mvc/trang-chu">Trang chủ</a></li>
								<li id="tt3">Liên hệ</li>
							</ul>
						</div>
					</div>
				</div>
			</div>

			<div class="image-right">
				<!-- Ảnh bên phải bao phủ toàn bộ chiều cao -->
				<img
					src="https://i.ibb.co/ycqfYdP4/27a7279289f8a11b18869bf77b5fb1f6.jpg"
					alt="Image Right">
			</div>
		</div>
		<div class="contact-us-area mt-no-text">
			<div class="container custom-area">
				<div class="row">
					<div class="col-lg-4 col-md-6 col-custom">
						<div class="contact-info-item" id="w1">
							<div class="con-info-icon">
								<a href="https://maps.app.goo.gl/PL6wAdGsQi4bnnY58"> <i
									class="lnr lnr-map-marker"></i>
								</a>
							</div>
							<div class="con-info-txt">
								<h4 class="h1">Vị trí của chúng tôi</h4>
								<p class="h2">54 Điện Biên Phủ, Chính Gián, Thanh Khê, Đà
									Nẵng 550000</p>
							</div>
						</div>
					</div>
					<div class="col-lg-4 col-md-6 col-custom">
						<div class="contact-info-item" id="w2">
							<div class="con-info-icon">
								<i class="lnr lnr-smartphone"></i>
							</div>
							<div class="con-info-txt">
								<h4 class="h1">Liên hệ 24/7</h4>
								<p class="h2">
									Sđt: 012 345 678<br>Fax: 123 456 789
								</p>
							</div>
						</div>
					</div>
					<div class="col-lg-4 col-md-12 col-custom text-align-center">
						<div class="contact-info-item" id="w3">
							<div class="con-info-icon">
								<a href=" https://mail.google.com/mail/u/0/#inbox "> <i
									class="lnr lnr-envelope"></i>
								</a>
							</div>
							<div class="con-info-txt">
								<h4 class="h1">Hỗ trợ mọi lúc</h4>
								<p class="h2">
									Support24/7@example.com <br> loihv.23ite@vku.udn.vn
								</p>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12 col-custom">
						<form method="POST"
							action="http://localhost:8080/Spring-mvc/contact"
							id="contact-form" accept-charset="UTF-8" class="contact-form">
							<div class="comment-box mt-5">
								<h3 class="text-uppercase">Viết cho chúng tôi</h3>
								<h6 class="h6">Gửi một lưu ý cho chúng tôi và chúng tôi sẽ
									trả lời bạn nhanh nhất có thể.</h6>

								<div class="row mt-3">
									<div class="col-md-6 col-custom">
										<div class="input-item mb-4">
											<input
												class="border-0 rounded-0 w-100 input-area name gray-bg"
												style="border: 0" type="text" name="con_name" id="con_name"
												placeholder="Tên" required>
										</div>
									</div>
									<div class="col-md-6 col-custom">
										<div class="input-item mb-4">
											<input
												class="border-0 rounded-0 w-100 input-area email gray-bg"
												type="email" name="con_email" id="con_email"
												placeholder="Email" required>
										</div>
									</div>
									<div class="col-12 col-custom">
										<div class="input-item mb-4">
											<input
												class="border-0 rounded-0 w-100 input-area email gray-bg"
												type="text" name="con_content" id="con_content"
												placeholder="Chủ đề bạn muốn bàn đến" required>
										</div>
									</div>
									<div class="col-12 col-custom">
										<div class="input-item mb-4">
											<textarea cols="30" rows="5"
												class="border-0 rounded-0 w-100 custom-textarea input-area gray-bg"
												name="con_message" id="con_message"
												placeholder="Bạn đang nghĩ gì?" required></textarea>
										</div>
									</div>
									<div class="col-12 col-custom mt-40">
										<div class="carousel-caption fade-in">
											<button type="submit" class="btn btn-default" id="tb">Gửi
												tin nhắn</button>
										</div>
									</div>
									<p class="col-8 col-custom form-message mb-0"></p>
								</div>

							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>

</html>