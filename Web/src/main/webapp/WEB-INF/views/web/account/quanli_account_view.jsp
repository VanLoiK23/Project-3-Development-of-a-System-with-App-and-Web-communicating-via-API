<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<title>Quản lý thông tin cá nhân</title>

<meta name="stylesheet"
	content='
	 <link rel="stylesheet"
			href="<c:url value='/template/js/owlcarousel/owl.carousel.min.css' />" />
	 <link rel="stylesheet"
			href="<c:url value='/template/js/owlcarousel/owl.theme.default.min.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/style1/taikhoan.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/style1/trangchu.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/style1/home_products.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/style1/pagination_phantrang.css' />" />
    <link rel="stylesheet"
			href="<c:url value='/template/css/style1/account.css' />" />
			
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
			

	    ' />

<meta name="scripts"
	content='
	            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	        	<script src="<c:url value='/template/js/owlcarousel/owl.carousel.min.js' />"></script>
	        	<script src="<c:url value='/template/js/user/trangchu.js' />"></script>
	        	<script src="<c:url value='/template/js/user/account.js' />"></script>
	        	
	    ' />
</head>

<body>
	<div class="main-content">

		<c:if test="${not empty message}">
			<script>
				alert('${message}');
			</script>
		</c:if>


		<div class="container">
			<!-- Sidebar bên trái -->
			<aside class="sidebar">
			<ul>
				<li><a href="#" onclick="showSection('profile')">Hồ sơ cá
						nhân</a></li>
				<li><a href="#" onclick="showSection('orders')">Đơn hàng
						chưa giao</a></li>
				<li><a href="#" onclick="showSection('order_giao')">Đơn
						hàng đã giao</a></li>
				<li><a href="#" onclick="showSection('new-password')">Đổi
						mật khẩu</a></li>
				<li><a href="#" onclick="showSection('logout')">Logout</a></li>
			</ul>
			</aside>

			<!-- Nội dung bên phải -->
			<main class="content"> <!-- Section: Orders  onclick="showSection('details_order')" -->
			<div id="order_giao" class="section">
				<h2>Đơn hàng đã giao</h2>
				<table class="order-table">
					<thead>
						<tr>
							<th>Order</th>
							<th>Date</th>
							<th>Trạng thái</th>
							<th>Tổng tiền</th>
							<th>Hành động</th>
						</tr>
					</thead>
					<tbody id="d">

						<c:set var="hasBeeenOrderDeliver"
							value="${not empty orderHasDeliver }" />

						<c:if test="${hasBeeenOrderDeliver}">
							<c:forEach var="item" items="${orderHasDeliver}">
								<tr>
									<td>#84-${user.id}${item.id}</td>
									<td><fmt:formatDate value="${item.thoiGian}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>Đơn đã giao</td>
									<td><fmt:formatNumber value="${item.tongTien}"
											type="number" pattern="#,##0" /> đ</td>
									<td>
										<button class="view-btn" style="background-color: #007bff"
											onclick="window.location.href='http://localhost:8080/Spring-mvc/account/donhang/${item.id}';">View</button>
										<button class="view-btn" style="background-color: orange"
											onclick="showSection('details_order_${item.id}')">Review</button>
									</td>
								</tr>
							</c:forEach>
						</c:if>
						<c:if test="${not hasBeeenOrderDeliver}">

							<tr style='border: 0; height: 300px'>
								<td colspan='6' class='text-center'><img
									style='border: 0; width: 100%; height: 250px !important;'
									src='https://i.ibb.co/1fjWK3sC/images.jpg'></td>
							</tr>
						</c:if>

					</tbody>
				</table>
			</div>

			<c:if test="${hasBeeenOrderDeliver}">

				<c:forEach var="order" items="${orderHasDeliver}">
					<div class="order-list mt-3 section" id="details_order_${order.id}">
						<c:forEach var="item" items="${order.listProductDTOsForComment}">

							<div
								class="order-item d-flex justify-content-between align-items-start p-3 border-bottom">
								<img src='${item.hinhAnh.split(",")[0]}' alt="Product"
									class="img-fluid rounded" style="width: 80px; height: 80px;">
								<div class="ms-3 flex-grow-1">
									<h6 class="mb-1">${item.tenSanPham}</h6>
									<p class="mb-1 text-muted"
										style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis; width: 400px;'>
										${item.sortDesc}</p>
								</div>
								<c:if test="${item.isCommentWithOrder}">
									<button class="btn btn-outline-danger"
										onclick="update_cmmt(${item.map}, ${order.id})">
										Chỉnh sửa đánh giá</button>
								</c:if>

								<c:if test="${not item.isCommentWithOrder}">
									<button class="btn btn-outline-danger"
										onclick="open_cmmt(${item.map}, ${order.id})">Đánh
										giá</button>
								</c:if>

							</div>

						</c:forEach>
					</div>
				</c:forEach>
			</c:if>


			<div id="orders" class="section active">
				<div
					style="display: flex; justify-content: space-between; /* Space out the items */ align-items: center; /* Center items vertically */ padding: 10px;">
					<h2>Lịch sử đơn hàng</h2>
					<div class="filter-container">
						<p>Thời gian:</p>
						<select id="timeFilter">
							<option value="today">Hôm nay</option>
							<option value="yesterday">Hôm qua</option>
							<option value="week">Tuần này</option>
							<option value="month">Tháng này</option>
							<option value="all">Bỏ lọc</option>
						</select>
					</div>
				</div>
				<table class="order-table">
					<thead>
						<tr>
							<th>Order</th>
							<th>Date</th>
							<th>Trạng thái</th>
							<th>Tổng tiền</th>
							<th>Hành động</th>
						</tr>
					</thead>
					<tbody id="ds">
						<c:set var="hasOrderNotDeliverYet"
							value="${not empty orderNotDeliverYet }" />

						<c:if test="${hasOrderNotDeliverYet}">
							<c:forEach var="item" items="${orderNotDeliverYet}">
								<tr>
									<td>#84-${user.id}${item.id}</td>
									<td><fmt:formatDate value="${item.thoiGian}"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<c:set var="status" value="${item.status}" />
									<td><c:choose>
											<c:when test="${status eq 0}">Đang chờ duyệt</c:when>
											<c:when test="${status eq 1}">Đang lấy hàng</c:when>
											<c:when test="${status eq 2}">Đang chờ giao hàng</c:when>
											<c:when test="${status eq 3}">Đang giao hàng</c:when>
											<c:when test="${status eq -1}">Nhân viên đã hủy</c:when>
											<c:when test="${status eq -2}">Đang chờ nhân viên duyệt đơn hủy</c:when>
											<c:otherwise>Bạn đã hủy</c:otherwise>
										</c:choose></td>
									<td><fmt:formatNumber value="${item.tongTien}"
											type="number" pattern="#,##0" /> đ</td>
									<td><button class="view-btn"
											style="background-color: #007bff"
											onclick="window.location.href='http://localhost:8080/Spring-mvc/account/donhang/${item.id}';">View</button>

										<c:if test="${status eq 0}">
											<button class="view-btn" onclick="cancle(${item.id})">Hủy</button>
										</c:if></td>
								</tr>
							</c:forEach>
						</c:if>

						<c:if test="${not hasOrderNotDeliverYet}">

							<tr style='border: 0; height: 300px'>
								<td colspan='6' class='text-center'><img
									style='border: 0; width: 100%; height: 250px !important;'
									src='https://i.ibb.co/1fjWK3sC/images.jpg'></td>
							</tr>
						</c:if>

					</tbody>
				</table>

			</div>

			<div id="profile" class="section">
				<h2>Hồ sơ tài khoản</h2>
				<form
					action="http://localhost:8080/Spring-mvc/account/updateIn4Account"
					method="POST">
					<input type="hidden" id="id" name="id" value="${user.id}">
					<c:if test="${not empty user }">
						<label for="username">Full name:</label>
						<input type="text" id="username" name="fullname"
							value="${user.name}">
						<br>

						<!-- Trường Số điện thoại -->
						<label for="phone">Phone Number:</label>
						<input type="text" id="phone" name="phone" value="${user.phone}"
							placeholder="Enter your phone number" required>
						<br>

						<!-- Trường Email -->
						<label for="email">Email:</label>
						<input type="email" id="email" name="email" value="${user.email}"
							placeholder="Enter your email" required>
						<br>

						<%-- <!-- Trường Địa chỉ -->
						<label for="address">Address:</label>
						<input type="text" id="address" name="address"
							value="<?php echo $profile['diachi']; ?>"
							placeholder="Enter your address" required>
						<br> --%>

						<!-- Trường Ngày sinh -->
						<label for="datepicker">Date of Birth:</label>
						<input type="date" id="datepicker" name="ngaysinh"
							value="${user.ngaySinh}" required>
						<br>

						<!-- Trường Giới tính -->
						<label for="gender">Gender:</label>
						<div>
							<label> <input type="radio" name="gender" value="female"
								${user.gender eq 'female' ? 'checked' : ''}> Female
							</label> <label> <input type="radio" name="gender" value="male"
								${user.gender eq 'male' ? 'checked' : ''}> Male
							</label>
						</div>


						<!-- Nút lưu -->
						<button type="submit">Save</button>
					</c:if>

				</form>
			</div>

			<div id="new-password" class="section">
				<h2>Password</h2>
				<form
					action="http://localhost:8080/Spring-mvc/account/updateIn4Account"
					method="POST">
					<input type="hidden" id="id" name="id" value="${user.id}">
					<label for="new-password">New Password:</label> <input
						type="password" name="newpassword" id="new-password"><br>

					<button type="submit">Update Password</button>
				</form>
			</div>

			</main>
		</div>

		<div id="cancelPopup" class="popup" style="display: none;"
			onclick="closePopup()">
			<div class="popup-content" onclick="event.stopPropagation()">
				<button type="button" class="close-btn" onclick="closePopup()">X</button>
				<h3>Nhập lý do hủy đơn hàng</h3>
				<textarea id="cancelReason" placeholder="Nhập lý do hủy..."></textarea>
				<button onclick="submitCancel()">Xác nhận hủy</button>
			</div>
		</div>

		<div class="review-form-container" style="display: none">
			<div class="review-form">
				<button class="close-btn" onclick="close_cmmt()">&times;</button>
				<h3>Đánh Giá Sản Phẩm</h3>
				<div class="star-rating">
					<span data-value="1" class="star">&#9733;</span> <span
						data-value="2" class="star">&#9733;</span> <span data-value="3"
						class="star">&#9733;</span> <span data-value="4" class="star">&#9733;</span>
					<span data-value="5" class="star">&#9733;</span>
				</div>
				<textarea placeholder="Nhập đánh giá của bạn..." rows="4"
					id="reviewText"></textarea>
				<div class="image-container">
					<img id="displayedImage"
						src="https://i.ibb.co/VsmP3QP/illustration-upload-498740-5719.jpg"
						alt="Selectable Image"> <input type="file" id="imageInput"
						name="img-upload" accept="image/*" required style="display: none;">
				</div>
				<button class="submit-btn" id="submit-btn">Gửi Đánh Giá</button>
				<button class="submit-btn" id="update-btn" style="display: none">Cập
					nhật Đánh Giá</button>
			</div>
		</div>
	</div>






</body>

</html>