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
					action="/Spring-mvc/quan-tri/Quanlisanpham_controller/update_sp"
					role="form" enctype="multipart/form-data" method="POST"
					modelAttribute="model">

					<div class="table-header" style="background-color: transparent">
						<p>
							<button type="button" id="add-product-btn"
								style="background-color: orange; scale: calc(0.8)">
								<i class="fa-solid fa-marker"></i>
							</button>
							Chỉnh sửa sản phẩm
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
								<form:input path="id" type="hidden" id="id" />
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
										<%
										Object pageObj = request.getAttribute("imgUrl"); // Lấy từ request scope

										String imageUrls = (pageObj != null) ? (String) pageObj : "";
										String[] imageUrlsArray = imageUrls.split(",");
										%>
										<img id="preview" src="<%=imageUrlsArray[0]%>" alt="Preview">
									</div>
								</div>
								<div id="select" style="margin-left: 30px">
									<div>
										<label for="">Xuất xứ</label><br>
										<form:select path="xuatXuId" id="xx" required="true">
											<form:options items="${model.xuatxu_List}" itemValue="id"
												itemLabel="tenXuatXu" />
										</form:select>



									</div>
									<div>
										<label for="name">Hệ điều hành</label><br>
										<form:select path="heDieuHanhId" id="hdh">
											<form:options items="${model.hedieuhanh_List}" itemValue="id"
												itemLabel="tenHeDieuHanh" />
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
											name="check" type="checkbox" value="tragop" id="check"
											${not empty tragop ? "checked" : ""} />
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

												<%
												int i = 1;
												%>
												<c:if test="${not empty pbsp }">
													<c:forEach var="item" items="${pbsp}">
														<tr>
															<td><%=i++%></td>
															<input type="hidden" id="id_pbsp" name="id_pbsp[]"
																value="${item.maPhienbansanpham}">
															<c:choose>
																<c:when test="${not empty item.ram}">
																	<td><input type="hidden" name="ram[]"
																		value="${item.ram}">${item.ram} GB</td>
																</c:when>
																<c:otherwise>
																	<td><input type="hidden" name="ram[]" value="">NULL</td>
																</c:otherwise>
															</c:choose>
															<c:choose>
																<c:when test="${not empty item.rom}">
																	<td><input type="hidden" name="rom[]"
																		value="${item.rom}">${item.rom} GB</td>
																</c:when>
																<c:otherwise>
																	<td><input type="hidden" name="rom[]" value="">NULL</td>
																</c:otherwise>
															</c:choose>

															<td><input type="hidden" name="color[]"
																value="${item.color}">${item.color}</td>

															<td><input type="hidden" name="price_n[]"
																value="${item.giaNhap}" /> <fmt:formatNumber
																	value="${item.giaNhap}" type="number" pattern="#,##0" />
																đ</td>
															<td><input type="hidden" name="price_x[]"
																value="${item.giaXuat}" /> <fmt:formatNumber
																	value="${item.giaXuat}" type="number" pattern="#,##0" />
																đ</td>
													</c:forEach>
												</c:if>
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
									required="true"
									value="${not empty giareonline ? giareonline : 0}" />

							</div>
							<div>
								<label for="">Giảm giá khi mua trực tiếp(%)</label> <input
									name="bigsale" type="number" min="0" class="form-control"
									id="bigsale" required="true"
									value="${not empty giamgia ? giamgia : 0}" />
							</div>


							<div>
								<label for="" style="margin-top: 15px;">Số lượng tồn kho</label>
								<form:input type="number" class="form-control" id="sl_ton"
									path="soLuongCon" style="background-color: rgb(212, 207, 207)"
									readonly="true" />

							</div>
							<div>
								<label for="" style="margin-top: 15px;">Số lượng đã bán</label>
								<form:input type="number" class="form-control" id="sl_ban"
									path="soLuongBan" style="background-color: rgb(212, 207, 207)"
									readonly="true" />
							</div>
							<div>
								<label for="" style="margin-top: 15px;">Trạng thái</label> <select
									name="state" style="margin-left: 10px" id="state"
									class="form-control">

									<c:choose>
										<c:when test="${model.status eq 1}">
											<option value="1" selected>Đang kinh doanh</option>
											<option value="0">Ngừng kinh doanh</option>
										</c:when>
										<c:otherwise>
											<option value="1">Đang kinh doanh</option>
											<option value="0" selected>Ngừng kinh doanh</option>
										</c:otherwise>
									</c:choose>
								</select>
							</div>


							<div id="contain-im">
								<div class="image-container">
									<img id="displayedImage"
										src="<%= (imageUrlsArray != null && imageUrlsArray.length > 1) ? imageUrlsArray[1] : "https://i.ibb.co/hFQYvzZG/download.png" %>"
										alt="Selectable Image"> <input name="img-upload1"
										type="file" id="imageInput" accept="image/*"
										style="display: none;" />
								</div>

								<div class="image-container">
									<img id="displayedImage1"
										src="<%= (imageUrlsArray != null && imageUrlsArray.length > 2) ? imageUrlsArray[2] : "https://i.ibb.co/hFQYvzZG/download.png" %>"
										alt="Selectable Image"> <input name="img-upload2"
										type="file" id="imageInput1" accept="image/*"
										style="display: none;" />
								</div>

								<div class="image-container">
									<img id="displayedImage2"
										src="<%= (imageUrlsArray != null && imageUrlsArray.length > 3) ? imageUrlsArray[3] : "https://i.ibb.co/hFQYvzZG/download.png" %>"
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
	<script>
		$(document)
				.ready(
						function() {
							$('#summernote')
									.summernote(
											{
												height : 400,
												minHeight : 200,
												maxHeight : 1000,
												toolbar : [
														[ 'style', [ 'style' ] ],
														[
																'font',
																[
																		'bold',
																		'italic',
																		'underline',
																		'clear' ] ],
														[ 'fontname',
																[ 'fontname' ] ],
														[ 'fontsize',
																[ 'fontsize' ] ],
														[ 'color', [ 'color' ] ],
														[
																'para',
																[ 'ul', 'ol',
																		'paragraph' ] ],
														[ 'height',
																[ 'height' ] ],
														[ 'table', [ 'table' ] ],
														[
																'insert',
																[
																		'link',
																		'picture',
																		'video' ] ],
														[
																'view',
																[ 'codeview',
																		'help' ] ],
														[
																'misc',
																[ 'undo',
																		'redo' ] ] ],
												callbacks : {
													onChange : function(
															contents, $editable) {
														$('#htmlContent').val(
																contents);
													}
												}
											});

							// Lấy nội dung từ model để hiển thị trong Summernote
							const htmlContent = `<c:out value="${model.detail}" escapeXml="false" />`;
							$('#summernote').summernote('code', htmlContent);
						});
	</script>



</body>

</html>