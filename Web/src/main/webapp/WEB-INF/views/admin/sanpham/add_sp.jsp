<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<title>Thêm sản phẩm</title>

<meta name="stylesheet"
	content='
	
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/quan_li_sp_style.css' />" />
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/edit_sp.css' />" />
    ' />
<meta name="scripts"
	content='
        	<script src="<c:url value='/template/js/quan_li_sp.js' />"></script>
    ' />

</head>

<body style="background: #D3D3D3">

	<div class="main-content">
		<section id="wrapper">

		<div class="p-4" id="edit_sp">
			<div class="table" style="padding: 0 !important;">
				<form:form
					action="/Spring-mvc/quan-tri/Quanlisanpham_controller/add_sp"
					role="form" enctype="multipart/form-data" method="POST"
					modelAttribute="model">

					<div class="table-header" style="background-color: transparent">
						<p>
							<button type="button" id="add-product-btn"
								style="background-color: #A0F820; scale: calc(0.9)">
								<i style="scale: calc(1.4)"
									class="fa-brands fa-creative-commons-sampling-plus"></i>
							</button>
							Thêm sản phẩm
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
									href='<c:url value="/quan-tri/Quanlisanpham_controller/danh-sach"/>'>
									<i style="margin-right: 10px" class="fa-solid fa-x"></i>Quay
									lại
								</a>
							</button>
						</p>
					</div>
					<div class="form">
						<div class="leftt-form">
							<div class="form-group">
								<form:input path="detail" type="hidden" id="htmlContent" />
								<div>
									<label for="name">Tên sản phẩm</label>
									<form:input path="tenSanPham" type="text" class="form-control"
										id="name" placeholder="Tên sản phẩm" required="true" />
										
										<div id="error-message" style="color: red; display: none;"></div>
										
										
								</div>
							</div>
							<div class="form-group" id="form_img" style="margin: 30px 0;">
								<div class="custom-file-upload" style="margin-right: 20px;">
									<label for="img">Thêm ảnh</label> <input type="file" name="img"
										id="img" accept="image/*" />

									<div id="preview-container">
										<img id="preview" style="display: none" src="" alt="Preview">
									</div>
								</div>
								<div id="select" style="margin-left: 270px">
									<div>
										<label for="">Xuất xứ</label><br>
										<form:select path="xuatXuId" id="xx" required="true">
											<form:options items="${model.xuatxu_List}" itemValue="id"
												itemLabel="tenXuatXu" />
										</form:select>

								

									</div>
									<div>
										<label for="name">Hệ điều hành</label><br>
										<form:select path="heDieuHanhId" id="hdh" required="true">
											<form:options items="${model.hedieuhanh_List}" itemValue="id"
												itemLabel="tenHeDieuHanh" />
											<form:option value="">Không</form:option>
										</form:select>
									</div>
									<div>
										<label for="name">Thương hiệu</label><br>


										<form:select path="thuongHieuId" id="th" required="true">
											<form:options items="${model.category_List}" itemValue="id"
												itemLabel="tenThuongHieu" />
										</form:select>
									</div>
									<div>
										<label for="name">Trả góp 0%</label><br> <input
											name="check" type="checkbox" value="tragop" id="check" />
									</div>
								</div>
							</div>
							<div class="form-group">
								<div>
									<label for="">Dung lượng pin</label>
									<form:input path="dungLuongPin" type="number"
										class="form-control" id="dlp" required="true" />
								</div>
								<div>
									<label for="name" style="margin-left: 150px">Màn hình</label>
									<form:input path="manHinh" type="text" class="form-control"
										id="size" required="true" />
								</div>
							</div>
							<div class="form-group">
								<div>
									<label for="phone" style="margin-top: 15px;">Camera sau</label>
									<form:input path="cameraSau" type="text" class="form-control"
										id="cr_s" required="true" />
								</div>
								<div>
									<label for="phone"
										style="margin-left: 150px; margin-top: 15px;">Camera
										trước</label>
									<form:input path="cameraTruoc" type="text" class="form-control"
										id="cr_tr" required="true" />
								</div>
							</div>
							<div class="form-group">
								<div>
									<label for="phone" style="margin-top: 15px;">Phiên bản
										HĐH</label>
									<form:input path="phienBanHDH" type="number"
										class="form-control" id="version" required="true" />
								</div>

								<div>
									<label for="name" style="margin-left: 150px; margin-top: 15px">Khu
										vực kho</label><br>



									<form:select path="khuVucKhoId" id="kho" required="true">
										<form:options items="${model.kho_List}" itemValue="id"
											itemLabel="tenKhuVuc" />
									</form:select>
								</div>
							</div>
							<div id="pbsp" style="margin-top: 25px;">
								<div>
									<label for="">Ram</label><br> <select name="ram" id="ram">

										<c:forEach var="item" items="${model.ram_List}">

											<option value="${item.kichThuocRam} GB">${item.kichThuocRam}
												GB</option>

										</c:forEach>
										<option value="">Không</option>
									</select>
								</div>
								<div>
									<label for="">Rom</label><br> <select name="rom" id="rom">
										<c:forEach var="item" items="${model.rom_List}">

											<option value="${item.kichThuocRom} GB">${item.kichThuocRom}
												GB</option>

										</c:forEach>
										<option value="">Không</option>
									</select>
								</div>
								<div>
									<label for="">Màu sắc</label><br> <select name="color"
										id="color" text-align="center">
										<c:forEach var="item" items="${model.color_List}">

											<option value="${item.tenMauSac}">${item.tenMauSac}</option>

										</c:forEach>
									</select>
								</div>
								<div>
									<label for="">Giá nhập</label> <input type="text"
										class="form-control" min="1" id="price_n" name="price_n">
								</div>
								<div>
									<label for="phone">Giá xuất</label> <input type="text"
										class="form-control" min="1" id="price_x" name="price_x">
								</div>
							</div>
							<div class="form-group" id="attri"
								style="max-width: 700px; margin-top: 20px">
								<div class="tb">
									<div class="table-section">
										<table>
											<thread>
											<tr>
												<th style="width: 5px">STT</th>
												<th style="width: 7px">Ram</th>
												<th style="width: 7px">Rom</th>
												<th style="width: 14px">Màu sắc</th>
												<th style="width: 10px">Giá nhập</th>
												<th style="width: 10px">Giá xuất</th>
											</tr>
											</thread>
											<tbody id="tb">
											</tbody>
										</table>
									</div>
								</div>
								<div style="max-width: 100px">
									<button type="button">
										<i class="fa-solid fa-plus"></i>
									</button>
									<br>
									<button type="button">
										<i class="fa-solid fa-file-pen"></i>
									</button>
									<br>
									<button type="button">
										<i class="fa-solid fa-trash-can"></i>
									</button>
								</div>
							</div>
							<div id="sortDesc" class="form-group" style="margin: 20px 0">
								<div>
									<label for="">Mô tả ngắn sản phẩm:</label><br>
									<form:textarea path="sortDesc" id="description" rows="2"
										cols="85" placeholder="Nhập mô tả ngắn sản phẩm"></form:textarea>
								</div>
							</div>
							<div id="Desc">
								<div>
									<label for="">Mô tả chi tiết sản phẩm:</label>
									<div id="summernote"></div>
								</div>
							</div>
						</div>
						<div class="rightt-form">
							<div>
								<label for="">Khuyến mãi(%)</label> <input name="sale"
									type="number" min="0" class="form-control" id="sale"
									required="true" />
							</div>
							<div>
								<label for="">Giảm giá khi mua trực tiếp(%)</label> <input 
									name="bigsale" type="number" min="0" class="form-control"
									id="bigsale" required="true" />
							</div>

							<div id="contain-im">
								<div class="image-container">
									<img id="displayedImage"
										src="https://i.ibb.co/hFQYvzZG/download.png"
										alt="Selectable Image"> <input name="img-upload1"
										type="file" id="imageInput" accept="image/*" 
										style="display: none;" />
								</div>
								<div class="image-container">
									<img id="displayedImage1"
										src="https://i.ibb.co/hFQYvzZG/download.png"
										alt="Selectable Image"> <input name="img-upload2"
										type="file" id="imageInput1" accept="image/*" 
										style="display: none;" />
								</div>
								<div class="image-container">
									<img id="displayedImage2"
										src="https://i.ibb.co/hFQYvzZG/download.png"
										alt="Selectable Image"> <input name="img-upload3"
										type="file" id="imageInput2" accept="image/*" 
										style="display: none;" />
								</div>
							</div>
						</div>
					</div>
				</form:form>

			</div>
		</div>
		</section>

	</div>



</body>

</html>