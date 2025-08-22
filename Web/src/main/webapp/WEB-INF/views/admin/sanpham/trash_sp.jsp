<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<title>Trash product</title>
<meta name="stylesheet"
	content='
	
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/quan_li_sp_style.css' />" />
    ' />


</head>

<body>

	<div class="main-content">
		<c:if test="${not empty message}">
			<script>
				alert('${message}');
			</script>
		</c:if>

		<section id="wrapper">


		<div class="p-4">
			<div class="table" style="margin-top: 50px;">
				<div class="table-header">
					<button style="background: orange; scale: calc(0.9);">
						<a style="color: white"
							href="http://localhost:8080/Spring-mvc/quan-tri/Quanlisanpham_controller/danh-sach"><i
							class="fa-solid fa-backward"></i></a>
					</button>
					<p style="margin-right: 30px">Thùng rác</p>
					<p></p>
				</div>
				<div class="table-section">
					<table>
						<thread>
						<tr>
							<th style="width: 60px">STT</th>
							<th style="width: 100px">Hình ảnh</th>
							<th
								style="width: 170px !important; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">Tên
								sản phẩm</th>
							<th style="width: 170px !important">Số lượng trong kho</th>
							<th style="width: 170px !important">Hành động</th>

						</tr>
						</thread>
						<tbody id="trash_list">
							<c:if test="${not empty model}">
								<c:forEach var="item" items="${model}" varStatus="status">
									<c:set var="soluongton"
										value="${item.soLuongNhap - item.soLuongBan}" />
									<c:set var="existingImages"
										value="${fn:split(item.hinhAnh, ',')}" />

									<tr style='color: black'>
										<td>${status.index + 1}</td>
										<td><img src="${existingImages[0]}" /></td>
										<td>${item.tenSanPham}</td>
										<td>${soluongton}</td>
										<td>
											<button style='background-color: blue'>
												<a style='color: white'
													href='<c:url value="/quan-tri/Quanlisanpham_controller/reset/${item.id}" />'>
													<i class='fa-solid fa-arrows-rotate'></i>
												</a>
											</button>
											<button alt='Xóa vĩnh viễn' style='background-color: red'>
												<a style='color: white'
													href='<c:url value="/quan-tri/Quanlisanpham_controller/Delete_permanent/${item.id}" />'>
													<i class='fa-regular fa-trash-can'></i>
												</a>
											</button>
										</td>
									</tr>
								</c:forEach>
							</c:if>

							<c:if test="${empty model}">
								<tr style='border: 0'>
									<td colspan='5' class='text-center'><span
										style='color: gray; display: flex; justify-content: center'>Thùng
											rác rỗng.</span></td>
								</tr>
							</c:if>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	</section>
	</div>



</body>

</html>