<%@page import="java.util.Arrays"%>
<%@page import="com.thuongmaidientu.dto.ProductDTO"%>
<%@page import="com.thuongmaidientu.dto.PromoDTO"%>

<%@page import="java.util.stream.Collectors"%>


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
<meta charset="UTF-8">
<%@include file="/common/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<meta charset="UTF-8">
<meta name="stylesheet"
	content='
	 <link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/vendor/linearicons.min.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/vendor/font.awesome.min.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/plugins/swiper-bundle.min.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/style.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/detailProduct.css' />" />
	
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
	        	<script src="<c:url value='/template/css/flosun/assets/js/vendor/jquery-3.6.0.min.js' />"></script>
	        	<script src="<c:url value='/template/css/flosun/assets/js/plugins/swiper-bundle.min.js' />"></script>
	        	<script src="<c:url value='/template/css/flosun/assets/js/main.js' />"></script>
	        	<script src="<c:url value='/template/js/user/details_product.js' />"></script>
	        	
	    ' />


</head>

<body>
	<div class="main-content">


		<div class="breadcrumbs-area position-relative">
			<div class="image-left">
				<!-- Ảnh bên trái bao phủ toàn bộ chiều cao -->
				<img
					src="https://i.ibb.co/vCwZzCQK/6f4ae82fd9ae97dc1b7f876c85870c9e.jpg"
					alt="Image Left">
			</div>

			<div class="container">
				<div class="row">
					<div
						class="col-12 d-flex align-items-center justify-content-between">
						<!-- Nội dung ở giữa -->
						<div
							class="breadcrumb-content position-relative section-content text-center"
							id="re">
							<h3 class="title-3">Chi tiết sản phẩm</h3>
							<ul>
								<li><a href="http://localhost:8080/Spring-mvc/trang-chu">Trang chủ</a></li>
								<li id="t">Chi tiết sản phẩm</li>
							</ul>
						</div>
					</div>
				</div>
			</div>

			<div class="image-right">
				<!-- Ảnh bên phải bao phủ toàn bộ chiều cao -->
				<img
					src="https://i.ibb.co/PXCRd1K/d89b16196de7911ea69f25b14df775f2.jpg"
					alt="Image Right">
			</div>
		</div>

		<c:if test="${not empty model }">


			<%
			Object productObject = request.getAttribute("model"); // Lấy từ request scope
			ProductDTO product = (productObject != null) ? (ProductDTO) productObject : null;

			String[] images = product.getHinhAnh().split(",");
			%>


			<div class="single-product-main-area" id="product-details1">
				<div class="container container-default custom-area">
					<div class="row">
						<div class="col-lg-5 offset-lg-0 col-md-8 offset-md-2 col-custom">
							<div class="product-details-img">
								<div
									class="single-product-img swiper-container gallery-top popup-gallery"
									id="main-img">
									<div class="swiper-wrapper">
										<%
										for (String image : images) {
										%>
										<div class="swiper-slide">
											<a class="w-100" href="${image}"> <img class="w-100"
												src="${image}" alt="Product">
											</a>
										</div>
										<%
										}
										%>

									</div>
								</div>
								<div
									class="single-product-thumb swiper-container gallery-thumbs">
									<%
									if (images.length != 1) {
									%>
									<div class="swiper-wrapper">
										<%
										for (String image : images) {
										%>
										<div class="swiper-slide">
											<img id="im" src="${image}" alt="Product">
										</div>
										<%
										}
										%>
									</div>
									<div class="swiper-button-next swiper-button-white">
										<i class="lnr lnr-arrow-right"></i>
									</div>
									<div class="swiper-button-prev swiper-button-white">
										<i class="lnr lnr-arrow-left"></i>
									</div>
									<%
									}
									%>
								</div>
							</div>
						</div>
						<div class="col-lg-7 col-custom">
							<div class="product-summery position-relative">
								<div class="product-head mb-3">
									<input type="hidden" id="masp" value="${model.id }">
									<h1 style="font-size: 30px; line-height: 1.4; font-weight: 700"
										class="product-title">
										${model.tenSanPham}


										<%
										if (product.getKichThuocRom() != null) {
										%>
										<%=product.getKichThuocRom()%>
										GB
										<%
										}
										%>

									</h1>
									<span class="quantity-sale">Đã bán ${model.soLuongBan}</span>
								</div>
								<div class="price-box mb-2">

									<%
									java.text.NumberFormat formatter = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));

									if (product.getPriceSale() != null) {
									%>

									<strong style='color: red; font-size: 20px'> <%=formatter.format(product.getPriceSale())%>
										đ <%
 if (!formatter.format(product.getPriceSale()).equals(formatter.format(product.getGiaXuat()))) {
 %>
									</strong> <span class="old-price"
										style="text-decoration: line-through; position: relative; margin-left: 20px !important"><%=formatter.format(product.getGiaXuat())%>
										đ</span>
									<%
									}
									} else {
									%>

									<strong style='color: red; font-size: 20px'> <%=formatter.format(product.getPbspList().get(0).getPriceSale().doubleValue())%>
										đ <%
 if (!formatter.format(product.getPbspList().get(0).getPriceSale())
 		.equals(formatter.format(product.getPbspList().get(0).getGiaXuat()))) {
 %>
									</strong> <span class="old-price"
										style="text-decoration: line-through; position: relative; margin-left: 20px !important">
										<%=formatter.format(product.getPbspList().get(0).getGiaXuat().doubleValue())%>
										đ
									</span>
									<%
									}
									}
									%>
								</div>
								<div class="product-rating mb-3" id="rate">

									<%
									int count_rate = 5;
									double rating = product.getSoLuongDanhGia() == 0
											? 0
											: Math.round((double) product.getTongSao() / product.getSoLuongDanhGia() * 10.0) / 10.0;

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
								</div>
								<p class="desc-content mb-5">${model.sortDesc}</p>
								<div class="scrolling_inner">
									<div class="box03 group desk">

										<%
										Set<String> displayedRoms = new HashSet<>();

										for (PhienBanSanPhamDTO phienBanSanPhamDTO : product.getPbspList()) {
											if (displayedRoms.contains(phienBanSanPhamDTO.getRom()))
												continue;
											displayedRoms.add(phienBanSanPhamDTO.getRom());
											if (product.getKichThuocRom() != null && phienBanSanPhamDTO.getRom().equals(product.getKichThuocRom().toString())) {
										%>
										<a
											href="Detail?p=<%=product.getAlias()%>-<%=product.getKichThuocRam()%>gb-<%=product.getKichThuocRom()%>gb-<%=product.getColor()%>"
											class="box03__item item act"><i
											style="background-color: #A29F9B"></i><%=product.getKichThuocRom()%></a>
										<%
										} else {
										if (phienBanSanPhamDTO.getRom() != null) {
										%>
										<a
											href="Detail?p=<%=product.getAlias()%>-<%=phienBanSanPhamDTO.getRam()%>gb-<%=phienBanSanPhamDTO.getRom()%>gb-<%=phienBanSanPhamDTO.getColor()%>"
											class="box03__item item"><i
											style="background-color: #A29F9B"></i><%=phienBanSanPhamDTO.getRom()%></a>
										<%
										}
										}
										System.out.print(displayedRoms + "Rom: " + product);
										}
										%>


									</div>
								</div>
								<div class="scrolling_inner" style="margin-bottom: 30px">

									<div class="box03 color group desk">
										<%
										Map<String, String> colorMap = new HashMap<>();
										colorMap.put("Màu đỏ", "#FF0000");
										colorMap.put("Màu trắng", "#FFFFFF");
										colorMap.put("Màu đen", "#000000");
										colorMap.put("Màu hồng", "pink");
										colorMap.put("Màu Titan Sa Mạc", "#C5B49B");
										colorMap.put("Màu xám", "gray");
										colorMap.put("Màu trắng bạc", "#C0C0C0");
										colorMap.put("Màu tím", "#6A0DAD");
										colorMap.put("Màu vàng", "yellow");
										colorMap.put("Màu xanh", "#0000FF");

										Set<String> displayedColors = new HashSet<>();

										for (PhienBanSanPhamDTO pb : product.getPbspList()) {
											if (product.getKichThuocRom() != null && pb.getRom().equals(product.getKichThuocRom().toString())) {

												String color = pb.getColor();

												if (displayedColors.contains(color))
											continue;
												displayedColors.add(color);

												boolean isActive = product.getColor() != null && color.trim().equalsIgnoreCase(product.getColor().trim());;

												String ram = pb.getRam();
												String rom = pb.getRom();
										%>

										<a
											href="Detail?p=<%=product.getAlias()%>-<%=ram%>gb-<%=rom%>gb-<%=color%>"
											class="box03__item item <%=isActive ? "act" : ""%>"> <i
											style="background-color:<%=colorMap.getOrDefault(color, "#ccc")%>"></i>
											<%=color%>
										</a>

										<%
										} else if (product.getPbspList().size() == 1 && product.getKichThuocRom() == null) {
										%>

										<a
											href="Detail?p=<%=product.getAlias()%>-<%=pb.getRam()%>gb-<%=pb.getRom()%>gb-<%=pb.getColor()%>"
											class="box03__item item act"> <i
											style="background-color:<%=colorMap.getOrDefault(pb.getColor(), "#ccc")%>"></i>
											<%=pb.getColor()%>
										</a>

										<%
										}
										}
										%>


									</div>
								</div>

								<c:if test="${model.status eq 1 }">


									<form action="http://localhost:8080/Spring-mvc/Cart"
										method="POST">
										<div class="quantity-with_btn mb-5">
											<div class="quantity">
												<div class="cart-plus-minus">
													<input class="cart-plus-minus-box" id="number"
														name="number" value="1" min="1" max="100" type="text">
													<div class="dec qtybutton">-</div>
													<div class="inc qtybutton">+</div>
												</div>
											</div>
											<div class="add-to_cart">

												<input type="hidden" name="id" value="${model.id }">
												<input type="hidden" name="id_pbsp" value="${model.id }">

												<button
													class="btn product-cart button-icon flosun-button dark-btn"
													style="color: white"
													onclick="checkSL(event, 
         '<%= product.getId()%>', 
        parseInt(document.getElementById('number').value))">
													Mua trực tiếp</button>

												<button
													class="btn product-cart button-icon flosun-button dark-btn"
													style="color: white"
													onclick="themVaoGioHang(event, 
        '<%= product.getId()%>', 
        parseInt(document.getElementById('number').value) )">
													Thêm vào giỏ hàng</button>


											</div>
										</div>
									</form>
								</c:if>
								<c:if test="${model.status ne 1 }">

									<img
										src="https://img.upanh.tv/2024/11/25/Thong-bao-tam-ngung-kinh-doanh-min.png"
										style="width: 200px; aspect-ratio: 4/3"
										alt="Thong-bao-tam-ngung-kinh-doanh-min.png" border="0">';
                               
                                								</c:if>

								<div class="social-share mb-4">
									<span>Share :</span> <a href="#"><i
										class="fa fa-facebook-square facebook-color"></i></a> <a href="#"><i
										class="fa fa-twitter-square twitter-color"></i></a> <a href="#"><i
										class="fa fa-linkedin-square linkedin-color"></i></a> <a href="#"><i
										class="fa fa-pinterest-square pinterest-color"></i></a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>


			<div class="product-detail">
				<!-- Phần mô tả sản phẩm -->

				<h2 style="text-align: center"
					style=" font-size: 24px;
  margin-bottom: 15px;
  color: #333;">Thông
					số kỹ thuật</h2>
				<div class="info_product"
					style="display: flex; justify-content: center; align-items: center">
					<table class="info">
						<tr>
							<th>Màn hình</th>
							<td>${model.manHinh }</td>
						</tr>
						<tr>
							<th>Hệ điều hành</th>
							<td><c:choose>
									<c:when test="${not empty model.heDieuHanh }">
							${model.heDieuHanh }
							</c:when>
									<c:otherwise>
							Không
							</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<th>Camara sau</th>
							<td><c:choose>
									<c:when test="${not empty model.cameraSau }">
							${model.cameraSau }
							</c:when>
									<c:otherwise>
							Không
							</c:otherwise>
								</c:choose></td>
						</tr>
						<tr>
							<th>Camara trước</th>
							<td><c:choose>
									<c:when test="${not empty model.cameraTruoc }">
							${model.cameraTruoc }
							</c:when>
									<c:otherwise>
							Không
							</c:otherwise>
								</c:choose></td>
						</tr>
						<%
						String ram = null;
						String rom = null;

						if (product.getKichThuocRom() != null && product.getKichThuocRam() != null) {
							rom = product.getKichThuocRom() + " GB";
							ram = product.getKichThuocRam() + " GB";
						} else if (product.getPbspList() != null && !product.getPbspList().isEmpty()) {
							PhienBanSanPhamDTO phienBan = product.getPbspList().get(0);

							if (phienBan.getRom() != null && phienBan.getRam() != null) {
								rom = phienBan.getRom() + " GB";
								ram = phienBan.getRam() + " GB";
							}
						}
						%>

						<tr>
							<th>RAM</th>
							<td>
								<%
								if (ram != null) {
								%> <%=ram%> <%
 } else {
 %> Không <%
 }
 %>

							</td>
						</tr>
						<tr>
							<th>Bộ nhớ trong</th>
							<td>
								<%
								if (rom != null) {
								%> <%=rom%> <%
 } else {
 %> Không <%
 }
 %>
							</td>
						</tr>
						<tr>
							<th>Dung lượng pin</th>
							<td>${model.dungLuongPin }mAh</td>
						</tr>
					</table>
				</div>

				<div class="product-description">
					<h2>Mô tả sản phẩm</h2>
					<p>${model.detail }</p>
					<br>
				</div>

				<!-- Phần đánh giá sản phẩm -->
				<div class="product-rating">
					<h3>Đánh giá sản phẩm</h3>
					<div class="rating-summary">
						<span class="average-rating"> <%=rating%>
						</span>
						<div class="stars">


							<%
							for (int i = 1; i <= count_rate; i++) {
								if (rating >= i) {
							%>
							<i style="color: orange" class="fa fa-star"></i>
							<%
							} else if (rating >= i - 0.5) {
							%>
							<i style="color: orange" class="fa-solid fa-star-half-stroke"></i>
							<%
							} else {
							%>
							<i style="color: orange" class="fa fa-star-o"></i>
							<%
							}

							}
							%>
						</div>
						<p>Trung bình dựa trên ${model.soLuongDanhGia } đánh giá</p>
					</div>
					<c:set var="Iscomment" value="${not empty comments.listResult }" />

					<c:if test="${ Iscomment}">
						<button class="rate-product-btn" onclick="openReviewForm()">Xem
							thêm đánh giá</button>
					</c:if>
				</div>

				<c:if test="${ Iscomment}">

					<!-- Phần bình luận sản phẩm -->
					<div class="comment-container">
						<!-- Comment 1 -->

						<%-- <?php
                    if (isset($data['listcomment'])) {

                        if (!empty($data['listcomment'])) {
                            $i = 0;
                            foreach ($data['listcomment'] as $it) {
                                if ($i < 5) {
                                    ?>
					<div class="comment-item">
						<div class="comment-author">
							<span class="author-name"> <?php echo $it['user_name'] ?>
							</span> <span class="comment-date"> <?php echo $it['review_date'] ?>
							</span>
						</div>
						<p class="comment-content">
							<?php echo $it['comment_content'] ?>
						</p>

						<?php
                                        if (!empty($it['comment_image'])) {
                                            ?>
						<div class="review-images">
							<!-- Image related to the review -->
							<img src="<?php echo $it['comment_image'] ?>" alt="Image 1"
								class="review-image">
						</div>

						<?php
                                        } ?>
					</div>

					<?php
                                    if ($it['feeback'] == 1) {
                                        ?>
					<div class="feedback-container" style="border-left: 6px solid blue">
						<div class="feedback-header">
							<div class="employee-info">
								<span class="employee-name"> <?php echo $it['nhanvien'] ?>
								</span> <span class="feedback-time">Phản hồi lúc: <?php echo $it['formatted_date'] ?></span>
							</div>
						</div>
						<div class="feedback-content">
							<?php echo $it['feeback_content'] ?>
						</div>
					</div>

					<?php
                                    }
                                    ?>

					<?php
                                    $i++;
                                }
                            }
                        }
                    } ?>
 --%>
						<c:forEach var="item" items="${comments.listResult }"
							varStatus="status">
							<c:if test="${status.index < 5}">

								<div class="comment-item">
									<div class="comment-author">
										<span class="author-name"> ${item.hoten} </span> <span
											class="comment-date"> ${item.ngay_đanhgia} </span>
									</div>
									<p class="comment-content">${item.content}</p>


									<c:if test="${not empty item.img }">

										<div class="review-images">
											<!-- Image related to the review -->
											<img src=" ${item.img}" alt="Image 1" class="review-image">
										</div>

									</c:if>
								</div>

								<c:if test="${item.feeback eq 1 }">

									<div class="feedback-container"
										style="border-left: 6px solid blue">
										<div class="feedback-header">
											<div class="employee-info">
												<span class="employee-name"> ${item.nhanvien} </span> <span
													class="feedback-time">Phản hồi lúc:
													${item.ngayphanhoi}</span>
											</div>
										</div>
										<div class="feedback-content">${item.feedbackContent}</div>
									</div>
								</c:if>

							</c:if>
						</c:forEach>
					</div>
				</c:if>


			</div>



		</c:if>



		<c:if test="${not empty sampleProduct.listResult }">
			<div class="product-area mt-text-3" id="product-details2">
				<div class="container custom-area-2 overflow-hidden">
					<div class="row">
						<div class="col-12 col-custom">
							<div class="section-title text-center mb-30">
								<span class="section-title-1" style="color: black">Các
									sản phẩm tương tự</span>
							</div>
						</div>
					</div>
					<div class="row product-row" style="width: 1050px">
						<div class="col-12 col-custom">
							<div class="product-slider swiper-container anime-element-multi">
								<div class="swiper-wrapper">
									<%
									Object productSampleObject = request.getAttribute("sampleProduct"); // Lấy từ request scope
									ProductDTO productListResult = (productSampleObject != null) ? (ProductDTO) productSampleObject : null;

									Object productObject = request.getAttribute("model"); // Lấy từ request scope
									ProductDTO product = (productObject != null) ? (ProductDTO) productObject : null;

									if (productListResult != null) {
										List<ProductDTO> filteredList = productListResult.getListResult().stream().filter(p -> p.getId() != product.getId())
										.collect(Collectors.toList());
										for (ProductDTO topRateProduct : filteredList) {

											String[] images = topRateProduct.getHinhAnh().split(",");
											String firstImage = images.length > 0 ? images[0] : "";
									%>
									<div class="single-item swiper-slide">
										<div class="single-product position-relative mb-30">
											<div class="product-image">
												<a class="d-block"
													href="Detail?p=<%=topRateProduct.getAlias()%>"> <img
													src="<%=firstImage%>" alt="" class="product-image-1 w-100"
													style="height: 200px">
												</a>
												<%
												for (PromoDTO promoDTO : topRateProduct.getPromoList()) {
													if (promoDTO.getName().equals("giamgia") || promoDTO.getName().equals("giareonline")) {
												%>
												<span class="onsale">Sale!</span>
												<%
												break;
												}
												}
												%>

											</div>
											<div class="product-content">
												<div class="product-title">
													<h4 class="title-2"
														style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis !important;">
														<a href="Detail?p=<%=topRateProduct.getAlias()%>"> <%=topRateProduct.getTenSanPham()%>
														</a>
													</h4>
												</div>
												<div class=" product-rating">
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
												</div>
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
												<div class="price-box">

													<div class="price">

														<strong style='color: red; font-size: 15px'><%=formatter.format(priceSaleFirst)%>
															đ</strong>

														<%
														if (giaXuatFirst != priceSaleFirst) {
														%>
														<span class="old-price"
															style="text-decoration: line-through; font-size: 11px"><%=formatter.format(giaXuatFirst)%>
															đ</span>
														<%
														}
														%>
													</div>
												</div>
												<a href="cart.html" class="btn product-cart"
													style="white-space: nowrap">Thêm vào giỏ hàng</a>
											</div>
										</div>
									</div>
									<?php
                                }
                            } ?>
									<%
									}
									}
									%>
								</div>
								<div class="swiper-pagination default-pagination"></div>
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
				<c:if test="${ Iscomment}">
					<c:forEach var="item" items="${comments.listResult }">

						<div class="review-item">
							<div class="review-name">${item.hoten}</div>
							<div class="review-rating">

								<c:set var="countRate" value="5" />
								<c:set var="rate" value="${item.rate }" />

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
							<c:if test="${not empty item.img }">

								<div class="review-images">
									<img src="${item.img }" style="max-width: 400px; height: 200px"
										alt="Review Image">
								</div>

							</c:if>

							<c:if test="${item.feeback eq 1 }">

								<div class="feedback-container"
									style="border-left: 6px solid blue; width: 400px">
									<div class="feedback-header">
										<div class="employee-info">
											<span class="employee-name"> ${item.nhanvien } </span> <span
												class="feedback-time">Phản hồi lúc:
												${item.ngayphanhoi }</span>
										</div>
									</div>
									<div class="feedback-content">${item.feedbackContent }</div>
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
        document.getElementById("closeBtn").onclick = function () {
            document.getElementById("reviewForm").style.display = "none";
        };

        document.getElementById("timeFilter").addEventListener("change", function () {
            const selectedValue = this.value;
            const idsp = document.getElementById("masp").value;
            


            // Gửi dữ liệu đã chọn đến server ko đc do cứ trả về toàn bộ web
           /*  fetch("http://localhost:8080/Spring-mvc/product/filterComment", {
                method: "POST",
                headers: {
                	"Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
                }, */
                fetch("http://localhost/DACS2/Detail/filter", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                body: `filter=` + encodeURIComponent(selectedValue) + `&idsp=` + encodeURIComponent(idsp),  // Sửa dấu ',' thành '&' để tách các tham số trong body
            })
                .then((response) => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    console.log(response)
                    return response.text();  // Dự đoán trả về HTML
                })
                .then((html) => {
                /* 	console.log("html:"+html);
                	 const parser = new DOMParser();
                	    const doc = parser.parseFromString(html, "text/html");


                    // Lấy phần tử container
                    const container = doc.querySelector(".container");

                    if (container) {
                        document.getElementById("reviewContent").innerHTML = container.innerHTML;
                    } else {
                        console.warn("Không tìm thấy .container trong response");
                    } */
                                	
                    const tableElement = document.getElementById("reviewContent");
                    if (tableElement) {
                        tableElement.innerHTML = html;  // Cập nhật nội dung bảng
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