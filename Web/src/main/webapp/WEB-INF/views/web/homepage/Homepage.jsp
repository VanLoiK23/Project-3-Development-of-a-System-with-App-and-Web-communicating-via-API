<%@page import="java.util.Arrays"%>
<%@page import="com.thuongmaidientu.dto.PromoDTO"%>
<%@page import="com.thuongmaidientu.dto.ProductDTO"%>
<%@page import="java.util.Map"%>
<%@page import="org.springframework.util.comparator.BooleanComparator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.thuongmaidientu.dto.PhienBanSanPhamDTO"%>

<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Thế giới điện thoại</title>


<meta name="stylesheet"
	content='
		<link rel="stylesheet"
			href="<c:url value='/template/css/style1/taikhoan.css' />" />
			<link rel="stylesheet"
			href="<c:url value='/template/css/style1/trangchu.css' />" />
			<link rel="stylesheet"
			href="<c:url value='/template/css/style1/home_products.css' />" />
			<link rel="stylesheet"
			href="<c:url value='/template/css/style1/pagination_phantrang.css' />" />
		
	    ' />
<meta name="scripts"
	content='
	        	<script src="<c:url value='/template/js/user/trangchu.js' />"></script>
	    ' />

</head>

<body>
	<div class="main-content">
		<c:if test="${not empty sessionScope.FLASH_MESSAGE}">
			<script>
				alert('${sessionScope.FLASH_MESSAGE}');
			</script>
			<c:remove var="FLASH_MESSAGE" scope="session" />
		</c:if>
		<section>
		<div class="header group">
			<div class="logo">

				<a href="http://localhost:8080/Spring-mvc/trang-chu"> <img
					src="https://i.ibb.co/HpRWyCDC/download.png"
					alt="Trang chủ Smartphone Store"
					title="Trang chủ Smartphone
					Store">
				</a>
			</div>

			<div class="content">
				<div class="search-header"
					style="position: relative; left: 162px; top: 1px;">
					<form class="input-search" method="get"
						action="http://localhost:8080/Spring-mvc/filter">
						<div class="autocomplete">
							<input id="search-box" name="search" autocomplete="off"
								type="text" placeholder="Nhập từ khóa tìm kiếm...">
							<button type="submit">
								<i class="fa fa-search"></i> Tìm kiếm
							</button>
							<div id="search-boxautocomplete-list" class="autocomplete-items">
							</div>
						</div>
					</form>
					<div class="tags">
						<strong>Từ khóa: </strong> <a
							href="http://localhost:8080/Spring-mvc/filter?search=Samsung">Samsung</a>
						<a href="http://localhost:8080/Spring-mvc/filter?search=iPhone">iPhone</a>
						<a href="http://localhost:8080/Spring-mvc/filter?search=Huawei">Huawei</a>
						<a href="http://localhost:8080/Spring-mvc/filter?search=Oppo">Oppo</a>
						<a href="http://localhost:8080/Spring-mvc/filter?search=Mobi">Mobi</a>
					</div>
				</div>

				<div class="tools-member">
					<div class="member">
						<a
							href="<c:out value='${empty sessionScope.name ? "http://localhost:8080/Spring-mvc/dang-nhap" : ""}'/>">
							<i class="fa fa-user"></i> Tài khoản
						</a>

						<c:if test="${not empty sessionScope.name }">
							<div class="menuMember">
								<a href="http://localhost:8080/Spring-mvc/account">Trang
									người dùng</a> <a
									onclick="if(window.confirm('Xác nhận đăng xuất ?')) logOut();">Đăng
									xuất</a>
							</div>
						</c:if>

					</div>
					<!-- End Member -->

					<div class="cart">
						<a href="http://localhost:8080/Spring-mvc/Cart"> <i
							class="fa fa-shopping-cart"></i> <span>Giỏ hàng</span> <span
							class="cart-number"></span>
						</a>
					</div>
					<!-- End Cart -->

					<!--<div class="check-order">
                        <a>
                            <i class="fa fa-truck"></i>
                            <span>Đơn hàng</span>
                        </a>
                    </div> -->
				</div>
				<!-- End Tools Member -->
			</div>
			<!-- End Content -->
		</div>
		<div class="banner">
			<div class="owl-carousel owl-theme owl-loaded owl-drag">

				<div class="owl-stage-outer">
					<div class="owl-stage"
						style="transition: 0.45s; width: 17235px; transform: translate3d(-10303px, 0px, 0px);">


						<div class="owl-item"
							style="width: 649.333px; margin-right: 100px;">
							<div class="item">
								<a target="_blank"
									href="https://res.cloudinary.com/deynh1vvv/image/upload/v1753843699/b7c7d55a5d2fe0c2b78e29e11064d7c2_xz9hvc.jpg">
									<img style="height: 290px"
									src="https://res.cloudinary.com/deynh1vvv/image/upload/v1753843699/b7c7d55a5d2fe0c2b78e29e11064d7c2_xz9hvc.jpg">
								</a>
							</div>
						</div>
						
					
						<div class="owl-item"
							style="width: 649.333px; margin-right: 100px;">
							<div class="item">
								<a target="_blank"
									href="https://i.ibb.co/DgsxHP4Q/a838bf7d72a1a6f111de79b45c5c9718.jpg">
									<img style="height: 290px"
									src="https://i.ibb.co/DgsxHP4Q/a838bf7d72a1a6f111de79b45c5c9718.jpg">
								</a>
							</div>
						</div>
						
						
						 <c:forEach var="item" items="${slides.listResult}">

							<div class="owl-item"
								style="width: 649.333px; margin-right: 100px;">
								<div class="item">
									<a target="_blank" href="${item.image}"> <img
										src="${item.image}">
									</a>
								</div>
							</div>

						</c:forEach> 
					</div>
				</div>
			</div>
		</div>
		<!-- End Banner --> <img
			src="http://localhost:8080/Spring-mvc/template/images/banners/blackFriday.gif"
			alt="" style="width: 100%;"> <br>
		<div class="companyMenu group flexContain">
			<c:forEach var="item" items="${brands.listResult}">
				<a
					href="http://localhost:8080/Spring-mvc/filter?company=${item.tenThuongHieu}">
					<img src="${item.image}">
				</a>
			</c:forEach>
			<!-- <a href="index.html?company=Samsung">
                <img src="http://localhost/DACS2/public/images/company/Samsung.jpg"></a>
                <a href="index.html?company=Oppo">
                <img src="http://localhost/DACS2/public/images/company/Oppo.jpg"></a>
                <a href="index.html?company=Nokia">
                <img src="http://localhost/DACS2/public/images/company/Nokia.jpg"></a>
                <a href="index.html?company=Huawei">
                <img src="http://localhost/DACS2/public/images/company/Huawei.jpg"></a>
                <a href="index.html?company=Xiaomi">
                <img src="http://localhost/DACS2/public/images/company/Xiaomi.png"></a>
                <a href="index.html?company=Realme">
                <img src="http://localhost/DACS2/public/images/company/Realme.png"></a>
                <a href="index.html?company=Vivo">
                <img src="http://localhost/DACS2/public/images/company/Vivo.jpg"></a>
                <a href="index.html?company=Philips">
                <img src="http://localhost/DACS2/public/images/company/Philips.jpg"></a>
                <a href="index.html?company=Mobell">
                <img src="http://localhost/DACS2/public/images/company/Mobell.jpg"></a>
                <a href="index.html?company=Mobiistar">
                <img src="http://localhost/DACS2/public/images/company/Mobiistar.jpg"></a>
                <a href="index.html?company=Itel">
                <img src="http://localhost/DACS2/public/images/company/Itel.jpg"></a>
                <a href="index.html?company=Coolpad">
                <img src="http://localhost/DACS2/public/images/company/Coolpad.png"></a>
                <a href="index.html?company=HTC">
                <img src="http://localhost/DACS2/public/images/company/HTC.jpg"></a>
                <a href="index.html?company=Motorola">
                <img src="http://localhost/DACS2/public/images/company/Motorola.jpg"></a> -->
		</div>

		<div class="flexContain">

			<div class="pricesRangeFilter dropdown">
				<c:set var="baseUrl" value="http://localhost:8080/Spring-mvc" />
				<button class="dropbtn">Giá tiền</button>
				<div class="dropdown-content">
					<c:choose>
						<c:when test="${not empty urlWithoutPrice}">
							<%-- Loại bỏ dấu ? hoặc & ở đầu bằng Java --%>
							<c:set var="cleanUrl"
								value="${fn:replace(urlWithoutPrice, '?', '')}" />
							<a href="${baseUrl}/filter?price=0-2000000&${cleanUrl}">Dưới
								2 triệu</a>
							<a href="${baseUrl}/filter?price=2000000-4000000&${cleanUrl}">Từ
								2 - 4 triệu</a>
							<a href="${baseUrl}/filter?price=4000000-7000000&${cleanUrl}">Từ
								4 - 7 triệu</a>
							<a href="${baseUrl}/filter?price=7000000-13000000&${cleanUrl}">Từ
								7 - 13 triệu</a>
							<a href="${baseUrl}/filter?price=13000000-100000000&${cleanUrl}">Trên
								13 triệu</a>
						</c:when>
						<c:otherwise>
							<a href="${baseUrl}/filter?price=0-2000000">Dưới 2 triệu</a>
							<a href="${baseUrl}/filter?price=2000000-4000000">Từ 2 - 4
								triệu</a>
							<a href="${baseUrl}/filter?price=4000000-7000000">Từ 4 - 7
								triệu</a>
							<a href="${baseUrl}/filter?price=7000000-13000000">Từ 7 - 13
								triệu</a>
							<a href="${baseUrl}/filter?price=13000000-100000000">Trên 13
								triệu</a>
						</c:otherwise>
					</c:choose>

					<%--	<?php
                    if (isset($data['link_price'])) {
                        echo '<a href="http://localhost/DACS2/Homepage/filter?price=0-2000000&' . $data['link_price'] . '">Dưới 2 triệu</a>
                    <a href="http://localhost/DACS2/Homepage/filter?price=2000000-4000000&' . $data['link_price'] . '">Từ 2 - 4 triệu</a>
                    <a href="http://localhost/DACS2/Homepage/filter?price=4000000-7000000&' . $data['link_price'] . '">Từ 4 - 7 triệu</a>
                    <a href="http://localhost/DACS2/Homepage/filter?price=7000000-13000000&' . $data['link_price'] . '">Từ 7 - 13 triệu</a>
                    <a href="http://localhost/DACS2/Homepage/filter?price=13000000-100000000&' . $data['link_price'] . '">Trên 13 triệu</a>';
                    } else {
                        echo '<a href="http://localhost/DACS2/Homepage/filter?price=0-2000000">Dưới 2 triệu</a>
                    <a href="http://localhost/DACS2/Homepage/filter?price=2000000-4000000">Từ 2 - 4 triệu</a>
                    <a href="http://localhost/DACS2/Homepage/filter?price=4000000-7000000">Từ 4 - 7 triệu</a>
                    <a href="http://localhost/DACS2/Homepage/filter?price=7000000-13000000">Từ 7 - 13 triệu</a>
                    <a href="http://localhost/DACS2/Homepage/filter?price=13000000-100000000">Trên 13 triệu</a>';
                    } ?> --%>
				</div>
			</div>

			<div class="promosFilter dropdown">
				<button class="dropbtn">Khuyến mãi</button>
				<div class="dropdown-content">
					<%--	<?php
                    if (isset($data['link_promo'])) {
                        echo '<a href="http://localhost/DACS2/Homepage/filter?promo=giamgia&' . $data['link_promo'] . '">Giảm giá</a>
                    <a href="http://localhost/DACS2/Homepage/filter?promo=tragop&' . $data['link_promo'] . '">Trả góp</a>
                    <a href="http://localhost/DACS2/Homepage/filter?promo=moiramat&' . $data['link_promo'] . '">Mới ra mắt</a>
                    <a href="http://localhost/DACS2/Homepage/filter?promo=giareonline&' . $data['link_promo'] . '">Giá rẻ online</a>';
                    } else {
                        echo '<a href="http://localhost/DACS2/Homepage/filter?promo=giamgia">Giảm giá</a>
                        <a href="http://localhost/DACS2/Homepage/filter?promo=tragop">Trả góp</a>
                        <a href="http://localhost/DACS2/Homepage/filter?promo=moiramat">Mới ra mắt</a>
                        <a href="http://localhost/DACS2/Homepage/filter?promo=giareonline">Giá rẻ online</a>';
                    } ?> --%>

					<c:choose>
						<c:when test="${not empty urlWithoutPromo}">
							<%-- Loại bỏ dấu ? hoặc & ở đầu bằng Java --%>
							<c:set var="cleanUrl"
								value="${fn:replace(urlWithoutPromo, '?', '')}" />

							<a href="${baseUrl}/filter?promo=giamgia&${cleanUrl}">Giảm
								giá</a>
							<a href="${baseUrl}/filter?promo=tragop&${cleanUrl}">Trả góp</a>
							<a href="${baseUrl}/filter?promo=moiramat&${cleanUrl}">Mới ra
								mắt</a>
							<a href="${baseUrl}/filter?promo=giareonline&${cleanUrl}">Giá
								rẻ online</a>
						</c:when>
						<c:otherwise>
							<a href="${baseUrl}/filter?promo=giamgia">Giảm giá</a>
							<a href="${baseUrl}/filter?promo=tragop">Trả góp</a>
							<a href="${baseUrl}/filter?promo=moiramat">Mới ra mắt</a>
							<a href="${baseUrl}/filter?promo=giareonline">Giá rẻ online</a>
						</c:otherwise>
					</c:choose>

				</div>
			</div>

			<div class="starFilter dropdown">
				<button class="dropbtn">Số lượng sao</button>
				<div class="dropdown-content">
					<%--	<?php
                    if (isset($data['link_star'])) {
                        echo ' <a href="http://localhost/DACS2/Homepage/filter?star=2&' . $data['link_star'] . '">Trên 2 sao</a>
                    <a href="http://localhost/DACS2/Homepage/filter?star=3&' . $data['link_star'] . '">Trên 3 sao</a>
                    <a href="http://localhost/DACS2/Homepage/filter?star=4&' . $data['link_star'] . '">Trên 4 sao</a>';
                    } else {
                        echo ' <a href="http://localhost/DACS2/Homepage/filter?star=2">Trên 2 sao</a>
                    <a href="http://localhost/DACS2/Homepage/filter?star=3">Trên 3 sao</a>
                    <a href="http://localhost/DACS2/Homepage/filter?star=4">Trên 4 sao</a>';
                    } ?> --%>

					<c:choose>
						<c:when test="${not empty urlWithoutStar}">
							<%-- Loại bỏ dấu ? hoặc & ở đầu bằng Java --%>
							<c:set var="cleanUrl"
								value="${fn:replace(urlWithoutStar, '?', '')}" />
							<a href="${baseUrl}/filter?star=2&${cleanUrl}">Trên 2 sao</a>
							<a href="${baseUrl}/filter?star=3&${cleanUrl}">Trên 3 sao</a>
							<a href="${baseUrl}/filter?star=4&${cleanUrl}">Trên 4 sao</a>
						</c:when>
						<c:otherwise>
							<a href="${baseUrl}/filter?star=2">Trên 2 sao</a>
							<a href="${baseUrl}/filter?star=3">Trên 3 sao</a>
							<a href="${baseUrl}/filter?star=4">Trên 4 sao</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>

			<div class="sortFilter dropdown">
				<button class="dropbtn">Sắp xếp</button>
				<div class="dropdown-content">
					<%-- <?php
                    if (isset($data['link_sort'])) {
                        echo '<a href="http://localhost/DACS2/Homepage/filter?sort=price-ascending&' . $data['link_sort'] . '">Giá tăng dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=price-decrease&' . $data['link_sort'] . '">Giá giảm dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=star-ascending&' . $data['link_sort'] . '">Sao tăng dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=star-decrease&' . $data['link_sort'] . '">Sao giảm dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=rateCount-ascending&' . $data['link_sort'] . '">Đánh giá tăng dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=rateCount-decrease&' . $data['link_sort'] . '">Đánh giá giảm dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=name-ascending&' . $data['link_sort'] . '">Tên A-Z</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=name-decrease&' . $data['link_sort'] . '">Tên Z-A</a>';
                    } else {
                        echo '<a href="http://localhost/DACS2/Homepage/filter?sort=price-ascending">Giá tăng dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=price-decrease">Giá giảm dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=star-ascending">Sao tăng dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=star-decrease">Sao giảm dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=rateCount-ascending">Đánh giá tăng dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=rateCount-decrease">Đánh giá giảm dần</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=name-ascending">Tên A-Z</a>
                    <a href="http://localhost/DACS2/Homepage/filter?sort=name-decrease">Tên Z-A</a>';
                    } ?> --%>

					<c:choose>
						<c:when test="${not empty urlWithoutSort}">
							<%-- Loại bỏ dấu ? hoặc & ở đầu bằng Java --%>
							<c:set var="cleanUrl"
								value="${fn:replace(urlWithoutSort, '?', '')}" />

							<a href="${baseUrl}/filter?sort=price-ascending&${cleanUrl}">Giá
								tăng dần</a>
							<a href="${baseUrl}/filter?sort=price-decrease&${cleanUrl}">Giá
								giảm dần</a>
							<a href="${baseUrl}/filter?sort=star-ascending&${cleanUrl}">Sao
								tăng dần</a>
							<a href="${baseUrl}/filter?sort=star-decrease&${cleanUrl}">Sao
								giảm dần</a>
							<a href="${baseUrl}/filter?sort=rateCount-ascending&${cleanUrl}">Đánh
								giá tăng dần</a>
							<a href="${baseUrl}/filter?sort=rateCount-decrease&${cleanUrl}">Đánh
								giá giảm dần</a>
							<a href="${baseUrl}/filter?sort=name-ascending&${cleanUrl}">Tên
								A-Z</a>
							<a href="${baseUrl}/filter?sort=name-decrease&${cleanUrl}">Tên
								Z-A</a>
						</c:when>
						<c:otherwise>
							<a href="${baseUrl}/filter?sort=price-ascending">Giá tăng dần</a>
							<a href="${baseUrl}/filter?sort=price-decrease">Giá giảm dần</a>
							<a href="${baseUrl}/filter?sort=star-ascending">Sao tăng dần</a>
							<a href="${baseUrl}/filter?sort=star-decrease">Sao giảm dần</a>
							<a href="${baseUrl}/filter?sort=rateCount-ascending">Đánh giá
								tăng dần</a>
							<a href="${baseUrl}/filter?sort=rateCount-decrease">Đánh giá
								giảm dần</a>
							<a href="${baseUrl}/filter?sort=name-ascending">Tên A-Z</a>
							<a href="${baseUrl}/filter?sort=name-decrease">Tên Z-A</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>

		</div>

		<div class="choosedFilter flexContain"
			style="   
                   <c:choose>
						<c:when test="${not empty priceMap}">
							display:flex;
						</c:when>
						<c:otherwise>
							display:none;
						</c:otherwise>
					</c:choose>
			">
			<a id="deleteAllFilter" style="display: block;"
				href="http://localhost:8080/Spring-mvc/trang-chu">
				<h3>Xóa bộ lọc</h3>
			</a>

			<c:set var="checkIsOneFilter" value="${checkIsOneFilter}" />
			<c:if test="${not empty param.company}">
				<a
					href="
				  <c:choose>
						<c:when test="${not empty checkIsOneFilter}">
							${checkIsOneFilter}
						</c:when>
						<c:otherwise>
							${urlWithoutCompany}
						</c:otherwise>
					</c:choose>
				">
					<h3>
						${param.company } <i class="fa fa-close"></i>
					</h3>
				</a>
			</c:if>

			<c:if test="${not empty param.search}">
				<a
					href="
				<c:choose>
						<c:when test="${not empty checkIsOneFilter}">
							${checkIsOneFilter}
						</c:when>
						<c:otherwise>
							${urlWithoutSearch}
						</c:otherwise>
					</c:choose>
				">
					<h3>
						${param.search } <i class="fa fa-close"></i>
					</h3>
				</a>
			</c:if>

			<c:if test="${not empty param.price}">
				<c:set var="priceLabel" value="${priceMap[param.price]}" />
				<a
					href="
				<c:choose>
						<c:when test="${not empty checkIsOneFilter}">
							${checkIsOneFilter}
						</c:when>
						<c:otherwise>
							${urlWithoutPrice}
						</c:otherwise>
					</c:choose>
				">
					<h3>${priceLabel}
						<i class="fa fa-close"></i>
					</h3>
				</a>
			</c:if>

			<c:if test="${not empty param.promo}">
				<c:set var="promoLabel" value="${promoMap[param.promo]}" />
				<a
					href="
				<c:choose>
						<c:when test="${not empty checkIsOneFilter}">
							${checkIsOneFilter}
						</c:when>
						<c:otherwise>
							${urlWithoutPromo}
						</c:otherwise>
					</c:choose>
				
				">
					<h3>${promoLabel}
						<i class="fa fa-close"></i>
					</h3>
				</a>
			</c:if>

			<c:if test="${not empty param.star}">
				<c:set var="starLabel" value="${starMap[param.star]}" />
				<a
					href="
				<c:choose>
						<c:when test="${not empty checkIsOneFilter}">
							${checkIsOneFilter}
						</c:when>
						<c:otherwise>
							${urlWithoutStar}
						</c:otherwise>
					</c:choose>
				
				"><h3>${starLabel}
						<i class="fa fa-close"></i>
					</h3></a>
			</c:if>

			<c:if test="${not empty param.sort}">
				<c:set var="sortLabel" value="${sortMap[param.sort]}" />
				<a
					href="
				<c:choose>
						<c:when test="${not empty checkIsOneFilter}">
							${checkIsOneFilter}
						</c:when>
						<c:otherwise>
							${urlWithoutSort}
						</c:otherwise>
					</c:choose>
				
				"><h3>${sortLabel}
						<i class="fa fa-close"></i>
					</h3></a>
			</c:if>
			<%--	<?php
            if (!empty($data['href_company'])) {
                echo ' <a href="' . $data['href_company'] . '">
                <h3>' . $_GET['company'] . '<i class="fa fa-close"></i> </h3>
            </a>';
            }
            if (!empty($data['href_search'])) {
                echo ' <a href="' . $data['href_search'] . '">
                <h3>' . $_GET['search'] . '<i class="fa fa-close"></i> </h3>
            </a>';
            }
            if (!empty($data['href_price'])) {
                $array = array(
                    '0-2000000' => 'Dưới 2 triệu',
                    '2000000-4000000' => 'Từ 2 - 4 triệu',
                    '4000000-7000000' => 'Từ 4 - 7 triệu',
                    '7000000-13000000' => 'Từ 7 - 13 triệu',
                    '13000000-100000000' => 'Trên 13 triệu'
                );
                echo ' <a href="' . $data['href_price'] . '">
                <h3>' . $array[$_GET['price']] . '<i class="fa fa-close"></i> </h3>
            </a>';
            }
            if (!empty($data['href_promo'])) {
                $array1 = array(
                    'giamgia' => 'Giảm giá',
                    'tragop' => 'Trả góp',
                    'moiramat' => 'Mới ra mắt',
                    'giareonline' => 'Giá rẻ online'
                );
                echo ' <a href="' . $data['href_promo'] . '">
                <h3>' . $array1[$_GET['promo']] . '<i class="fa fa-close"></i> </h3>
            </a>';
            }
            if (!empty($data['href_star'])) {
                $array2 = array(
                    '2' => 'Trên 2 sao',
                    '3' => 'Trên 3 sao',
                    '4' => 'Trên 4 sao'
                );
                echo ' <a href="' . $data['href_star'] . '">
                <h3>' . $array2[$_GET['star']] . '<i class="fa fa-close"></i> </h3>
            </a>';
            }
            if (!empty($data['href_sort'])) {
                $array3 = array(
                    'price-ascending' => 'Giá tăng dần',
                    'price-decrease' => 'Giá giảm dần',
                    'star-ascending' => 'Sao tăng dần',
                    'star-decrease' => 'Sao giảm dần',
                    'rateCount-ascending' => 'Đánh giá tăng dần',
                    'rateCount-decrease' => 'Đánh giá giảm dần',
                    'name-ascending' => 'Tên A-Z',
                    'name-decrease' => 'Tên Z-A'
                );
                echo ' <a href="' . $data['href_sort'] . '">
                <h3>' . $array3[$_GET['sort']] . '<i class="fa fa-close"></i> </h3>
            </a>';
            }

            ?>  --%>
		</div>
		<hr>

		<!-- Mặc định mới vào trang sẽ ẩn đi, nế có filter thì mới hiện lên -->
		<div class="contain-products"
			style="
		  <c:choose>
						<c:when test="${not empty priceMap}">
							display:block;
						</c:when>
						<c:otherwise>
							display:none;
						</c:otherwise>
					</c:choose>
		">
			<div class="filterName">
				<input type="text" placeholder="Lọc trong trang theo tên..."
					onkeyup="filterProductsName(this)">
			</div>
			<!-- End FilterName -->
			<form action="<c:url value='/filter'/>" id="formSubmit" method="get">

				<c:if test="${not empty param.company}">
					<input type="hidden" name="company" value="${param.company}" />
				</c:if>

				<c:if test="${not empty param.search}">
					<input type="hidden" name="search" value="${param.search}" />

				</c:if>

				<c:if test="${not empty param.price}">
					<input type="hidden" name="price" value="${param.price}" />

				</c:if>

				<c:if test="${not empty param.promo}">
					<input type="hidden" name="promo" value="${param.promo}" />

				</c:if>

				<c:if test="${not empty param.star}">
					<input type="hidden" name="star" value="${param.star}" />

				</c:if>

				<c:if test="${not empty param.sort}">
					<input type="hidden" name="sort" value="${param.sort}" />
				</c:if>

				<ul id="products" class="homeproduct group flexContain">
					<div id="khongCoSanPham"
						style="width: auto; margin: auto; transition-duration: 1s; 
					  <c:choose>
						<c:when test="${not empty model.listResult}">
							display:none; opacity: 0;
						</c:when>
						<c:otherwise>
							display:block; opacity: 1;
						</c:otherwise>
					</c:choose>
					">
						<i class="fa fa-times-circle"></i> Không có sản phẩm nào
					</div>


					<%
					Object productObjectFilter = request.getAttribute("model"); // Lấy từ request scope
					ProductDTO productListResultFilter = (productObjectFilter != null) ? (ProductDTO) productObjectFilter : null;

					if (productListResultFilter != null) {
						for (ProductDTO topRateProduct : productListResultFilter.getListResult()) {
							String[] images = topRateProduct.getHinhAnh().split(",");
							String firstImage = images.length > 0 ? images[0] : "";
					%>
					<li class="sanPham"
						style="opacity: 1; width: 239px; border-width: 1px;"
						id_sp="<%=topRateProduct.getId().intValue()%>"><a
						href="Detail?p=<%=topRateProduct.getAlias()%>"> <img
							src="<%=firstImage%>" alt="">
							<h3>
								<%=topRateProduct.getTenSanPham()%>
							</h3>

							<div class="prods-group">
								<ul>
									<%
									java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));

									Map<String, String> romMap = new HashMap<>();
									romMap.put("16", "16 GB");
									romMap.put("32", "32 GB");
									romMap.put("64", "64 GB");
									romMap.put("128", "128 GB");
									romMap.put("256", "256 GB");
									romMap.put("512", "512 GB");
									romMap.put("1000", "1 TB");

									Set<String> kichThuocRomDaXuatHien = new HashSet<>();
									boolean isFirstItem = true;

									// Lấy danh sách phiên bản sản phẩm
									List<PhienBanSanPhamDTO> pbList = topRateProduct.getPbspList();
									double priceSaleFirst = (pbList != null) ? pbList.get(0).getPriceSale() : 0.0;
									double giaXuatFirst = (pbList != null) ? pbList.get(0).getGiaXuat() : 0.0;

									for (PhienBanSanPhamDTO pb : pbList) {
										String kichthuocrom = pb.getRom();
										if (kichthuocrom != null && !kichThuocRomDaXuatHien.contains(kichthuocrom)) {
											kichThuocRomDaXuatHien.add(kichthuocrom);
											String url = "Detail?p=" + topRateProduct.getAlias() + "&id_pbsp=" + pb.getMaPhienbansanpham();
											String label = romMap.getOrDefault(kichthuocrom, kichthuocrom + " GB");

											if (isFirstItem) {
									%>
									<li data-url="<%=url%>" class="merge__item item act"><%=label%></li>
									<%
									isFirstItem = false;
									} else {
									%>
									<li data-url="<%=url%>" class="merge__item item"><%=label%></li>
									<%
									}
									}
									}
									%>
								</ul>
							</div>

							<div class="price">

								<strong><%=formatter.format(priceSaleFirst)%> đ</strong>

								<%
								if (giaXuatFirst != priceSaleFirst) {
								%>
								<span><%=formatter.format(giaXuatFirst)%> đ</span>
								<%
								}
								%>
							</div>
							<div class="ratingresult">

								<%
								int count_rate = 5;
								double rating = topRateProduct.getSoLuongDanhGia() == 0
										? 0
										: Math.round((double) topRateProduct.getTongSao() / topRateProduct.getSoLuongDanhGia() * 10.0) / 10.0;

								for (int i = 1; i <= count_rate; i++) {
									if (rating >= i) {
								%>
								<i class="fa fa-star"></i>
								<%
								} else if (rating >= i - 0.5) {
								%>
								<i class="fa-solid fa-star-half-stroke"></i>
								<%
								} else {
								%>
								<i class="fa fa-star-o"></i>
								<%
								}

								}
								%>
								<span> <%=topRateProduct.getSoLuongDanhGia()%> đánh giá
								</span>
							</div>
							<div style="display: flex;" class="label-container">
								<%
								int key = 0;
								int space = 1;
								for (PromoDTO promo : topRateProduct.getPromoList()) {
									space = key * 85;
									if ("moiramat".equals(promo.getName())) {
								%>
								<label style="left: <%=space%>px;" class="moiramat">Mới
									ra mắt</label>
								<%
								}
								if ("giamgia".equals(promo.getName())) {
								double priceDecrease = (Double.parseDouble(promo.getValue()) / 100) * giaXuatFirst;

								String formattedPriceDecrease = formatter.format(priceDecrease);
								%>
								<label style="left: <%=space%>px;" class="giamgia"> <i
									class="fa fa-bolt"></i> Giảm <%=formattedPriceDecrease%> đ
								</label>
								<%
								}
								if ("tragop".equals(promo.getName())) {
								%>
								<label style="left: <%=space%>px;" class="tragop">Trả
									góp 0%</label>
								<%
								}
								if ("giareonline".equals(promo.getName())) {
								%>
								<label style="left:<%=space%>px;" class="giareonline">Giá
									rẻ online</label>
								<%
								}
								key++;
								}
								%>
								<!-- <label class="tragop" style="margin-left:85px !important">Trả góp 0%</label> -->
							</div>
							<div class="tooltip">
								<button class="themvaogio"
									onclick="themVaoGioHang(event, '<%=topRateProduct.getId()%>',1)">
									<span class="tooltiptext" style="font-size: 15px;">Thêm
										vào giỏ</span> +
								</button>
							</div>
					</a></li>
					<%
					}
					}
					%>
				</ul>
				<!-- End products -->

				<!-- 			<div class="pagination"></div>
 -->
				<ul class="pagination" id="pagination"></ul>
				<input type="hidden" value="" id="page" name="page" /> <input
					type="hidden" value="" id="limit" name="limit" />
			</form>
		</div>
		<!-- Div hiển thị khung sp hot, khuyến mãi, mới ra mắt ... -->

		<div class="contain-khungSanPham"
			style=" 
         <c:choose>
						<c:when test="${not empty model.listResult}">
							display:none;
						</c:when>
						<c:otherwise>
							display:block;
						</c:otherwise>
					</c:choose>
        ">

			<%
			class PromoStyleDTO {
				String borderColor;
				String backgroundImage;
				String buttonColor;
				String attribute;
				String urlWatchAll;

				PromoStyleDTO(String borderColor, String backgroundImage, String buttonColor, String attribute,
				String urlWatchAll) {
					this.borderColor = borderColor;
					this.backgroundImage = backgroundImage;
					this.buttonColor = buttonColor;
					this.attribute = attribute;
					this.urlWatchAll = urlWatchAll;
				}
			}

			Map<String, PromoStyleDTO> promoStyleMap = new HashMap<>();
			promoStyleMap.put("NỔI BẬT NHẤT",
					new PromoStyleDTO("#ff9c00", "linear-gradient(120deg, #ff9c00 0%, #ec1f1f 50%, #ff9c00 100%);",
					"2px solid #ff9c00", "topRateProduct",
					"http://localhost:8080/Spring-mvc/filter?star=3&sort=rateCount-decrease"));
			promoStyleMap.put("SẢN PHẨM MỚI",
					new PromoStyleDTO("#42bcf4", "linear-gradient(120deg, #42bcf4 0%, #004c70 50%, #42bcf4 100%);",
					"2px solid #42bcf4", "newReleaseProduct",
					"http://localhost:8080/Spring-mvc/filter?promo=moiramat&sort=rateCount-decrease"));
			promoStyleMap.put("TRẢ GÓP 0%",
					new PromoStyleDTO("#ff9c00", "linear-gradient(120deg, #ff9c00 0%, #ec1f1f 50%, #ff9c00 100%);",
					"2px solid #ff9c00", "installmentProduct",
					"http://localhost:8080/Spring-mvc/filter?promo=tragop&sort=rateCount-decrease"));
			promoStyleMap.put("GIÁ SỐC ONLINE",
					new PromoStyleDTO("#5de272", "linear-gradient(120deg, #5de272 0%, #007012 50%, #5de272 100%);",
					"2px solid #5de272", "cheapmentProduct",
					"http://localhost:8080/Spring-mvc/filter?promo=giareonline&sort=rateCount-decrease"));
			promoStyleMap.put("GIẢM GIÁ LỚN",
					new PromoStyleDTO("#ff9c00", "linear-gradient(120deg, #ff9c00 0%, #ec1f1f 50%, #ff9c00 100%);",
					"2px solid #ff9c00", "discountProduct",
					"http://localhost:8080/Spring-mvc/filter?promo=giamgia&sort=rateCount-decrease"));
			promoStyleMap.put("GIÁ RẺ CHO MỌI NHÀ",
					new PromoStyleDTO("#5de272", "linear-gradient(120deg, #5de272 0%, #007012 50%, #5de272 100%);",
					"2px solid #5de272", "cheapestProduct",
					"http://localhost:8080/Spring-mvc/filter?price=0-2000000&sort=price-ascending"));

			List<String> promoList = Arrays.asList("NỔI BẬT NHẤT", "SẢN PHẨM MỚI", "TRẢ GÓP 0%", "GIÁ SỐC ONLINE", "GIẢM GIÁ LỚN",
					"GIÁ RẺ CHO MỌI NHÀ");
			for (String item : promoList) {

				PromoStyleDTO style = promoStyleMap.get(item);

				Object productObject = request.getAttribute(style.attribute); // Lấy từ request scope
				ProductDTO productListResult = (productObject != null) ? (ProductDTO) productObject : null;

				if (productListResult != null) {
			%>

			<div class="khungSanPham"
				style="border-color: <%=style.borderColor%>;">
				<h3 class="tenKhung"
					style="background-image: <%=style.backgroundImage%>;">
					*
					<%=item%>
					*
				</h3>
				<div class="listSpTrongKhung flexContain">
					<%
					for (ProductDTO topRateProduct : productListResult.getListResult()) {
						String[] images = topRateProduct.getHinhAnh().split(",");
						String firstImage = images.length > 0 ? images[0] : "";
					%>
					<li class="sanPham"><a
						href="Detail?p=<%=topRateProduct.getAlias()%>"> <img
							src="<%=firstImage%>" alt="">
							<h3>
								<%=topRateProduct.getTenSanPham()%>
							</h3>

							<div class="prods-group">
								<ul>
									<%
									java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));

									Map<String, String> romMap = new HashMap<>();
									romMap.put("16", "16 GB");
									romMap.put("32", "32 GB");
									romMap.put("64", "64 GB");
									romMap.put("128", "128 GB");
									romMap.put("256", "256 GB");
									romMap.put("512", "512 GB");
									romMap.put("1000", "1 TB");

									Set<String> kichThuocRomDaXuatHien = new HashSet<>();
									boolean isFirstItem = true;

									// Lấy danh sách phiên bản sản phẩm
									List<PhienBanSanPhamDTO> pbList = topRateProduct.getPbspList();
									double priceSaleFirst = (pbList != null) ? pbList.get(0).getPriceSale() : 0.0;
									double giaXuatFirst = (pbList != null) ? pbList.get(0).getGiaXuat() : 0.0;

									for (PhienBanSanPhamDTO pb : pbList) {
										String kichthuocrom = pb.getRom();
										if (kichthuocrom != null && !kichThuocRomDaXuatHien.contains(kichthuocrom)) {
											kichThuocRomDaXuatHien.add(kichthuocrom);
											String url = "Detail?p=" + topRateProduct.getAlias() + "&id_pbsp=" + pb.getMaPhienbansanpham();
											String label = romMap.getOrDefault(kichthuocrom, kichthuocrom + " GB");

											if (isFirstItem) {
									%>
									<li data-url="<%=url%>" class="merge__item item act"><%=label%></li>
									<%
									isFirstItem = false;
									} else {
									%>
									<li data-url="<%=url%>" class="merge__item item"><%=label%></li>
									<%
									}
									}
									}
									%>
								</ul>
							</div>

							<div class="price">

								<strong><%=formatter.format(priceSaleFirst)%> đ</strong>

								<%
								if (giaXuatFirst != priceSaleFirst) {
								%>
								<span><%=formatter.format(giaXuatFirst)%> đ</span>
								<%
								}
								%>
							</div>
							<div class="ratingresult">

								<%
								int count_rate = 5;
								double rating = topRateProduct.getSoLuongDanhGia() == 0
										? 0
										: Math.round((double) topRateProduct.getTongSao() / topRateProduct.getSoLuongDanhGia() * 10.0) / 10.0;

								for (int i = 1; i <= count_rate; i++) {
									if (rating >= i) {
								%>
								<i class="fa fa-star"></i>
								<%
								} else if (rating >= i - 0.5) {
								%>
								<i class="fa-solid fa-star-half-stroke"></i>
								<%
								} else {
								%>
								<i class="fa fa-star-o"></i>
								<%
								}

								}
								%>
								<span> <%=topRateProduct.getSoLuongDanhGia()%> đánh giá
								</span>
							</div>
							<div style="display: flex;" class="label-container">
								<%
								int key = 0;
								int space = 1;
								for (PromoDTO promo : topRateProduct.getPromoList()) {
									space = key * 85;
									if ("moiramat".equals(promo.getName())) {
								%>
								<label style="left: <%=space%>px;" class="moiramat">Mới
									ra mắt</label>
								<%
								}
								if ("giamgia".equals(promo.getName())) {
								double priceDecrease = (Double.parseDouble(promo.getValue()) / 100) * giaXuatFirst;

								String formattedPriceDecrease = formatter.format(priceDecrease);
								%>
								<label style="left: <%=space%>px;" class="giamgia"> <i
									class="fa fa-bolt"></i> Giảm <%=formattedPriceDecrease%> đ
								</label>
								<%
								}
								if ("tragop".equals(promo.getName())) {
								%>
								<label style="left: <%=space%>px;" class="tragop">Trả
									góp 0%</label>
								<%
								}
								if ("giareonline".equals(promo.getName())) {
								%>
								<label style="left:<%=space%>px;" class="giareonline">Giá
									rẻ online</label>
								<%
								}
								key++;
								}
								%>
								<!-- <label class="tragop" style="margin-left:85px !important">Trả góp 0%</label> -->
							</div>
							<div class="tooltip">
								<button class="themvaogio"
									onclick="themVaoGioHang(event, '<%=topRateProduct.getId()%>',1)">
									<span class="tooltiptext" style="font-size: 15px;">Thêm
										vào giỏ</span> +
								</button>
							</div>
					</a></li>
					<%
					}
					%>
				</div>
				<%
				//Total highlight
				Object topRateProductCountObj = request.getAttribute(style.attribute + "Count");
				Integer topRateProductCount = (topRateProductCountObj != null) ? (Integer) topRateProductCountObj : 0;
				%>
				<a class="xemTatCa" href="<%=style.urlWatchAll%>"
					style="border-left:  <%=style.buttonColor%>; border-right: <%=style.buttonColor%>;">
					Xem tất cả <%=topRateProductCount%> sản phẩm
				</a>
			</div>
			<hr>

			<%
			}
			}
			%>


		</div>
		</section>
		<!-- End Section -->



		<div class="plc">
			<section>
			<ul class="flexContain" style="justify-content: space-around;"
				id="product-list">
				<li class="centered-item">
					<div class="circle-icon">
						<i class='bx bxs-truck'></i>
					</div>
					<div class="text-pro">
						<h3>Miễn phí giao hàng</h3>
						<p class="color-gray">Áp dụng với hóa đơn từ 1.000.000 đ</p>
					</div>
				</li>
				<li class="centered-item">
					<div class="circle-icon">
						<i class="fa-solid fa-mobile-button"></i>
					</div>
					<div class="text-pro">
						<h3>30 ngày đổi sản phẩm</h3>
						<p class="color-gray">Áp dụng với sản phẩm nguyên giá</p>
					</div>
				</li>
				<li class="centered-item">
					<div class="circle-icon">
						<i class='bx bxs-package'></i>
					</div>
					<div class="text-pro">
						<h3>Ưu đãi hấp dẫn</h3>
						<p class="color-gray">Quyền lợi sinh nhật đặc biệt</p>
					</div>
				</li>
				<li class="centered-item">
					<div class="circle-icon">
						<i class='bx bxs-phone-call'></i>
					</div>
					<div class="text-pro">
						<h3>Hotline</h3>
						<a href="tel:12345678" class="color-gray">Mua hàng Online:
							0924.888.888đ</a>
					</div>
				</li>
			</ul>
			</section>
		</div>
	</div>



	<i class="fa fa-arrow-up" id="goto-top-page" onclick="gotoTop()"></i>

	<script type="text/javascript">
		var totalPages = ${model.totalPage};
		var currentPage = ${model.page};
		var limit = 10;
		$(function() {
			window.pagObj = $('#pagination').twbsPagination({
				totalPages : totalPages,
				visiblePages : limit,
				startPage : currentPage,
				onPageClick : function(event, page) {
					if (currentPage != page) {
						$('#limit').val(limit);
						$('#page').val(page);
						/* $('#sortName').val('title');
						$('#sortBy').val('desc');
						$('#type').val('list');  */
						$('#formSubmit').submit();
					}
				},
				paginationClass : 'pagination',
				nextClass : 'page-item next',
				prevClass : 'page-item prev',
				lastClass : 'page-item last',
				firstClass : 'page-item first',
				pageClass : 'page-item',
				activeClass : 'active',
				disabledClass : 'disabled',
				anchorClass : 'page-link'
			});
		});
	</script>

</body>

</html>