<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>
<c:url var="APIurl" value="/api-admin-new" />
<c:url var="NewURL" value="/admin-new" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<meta name="stylesheet"
	content='
		<link rel="stylesheet"
			href="<c:url value='/template/css/style/quan_li_attribute_style.css' />" />
			
			<link rel="stylesheet"
			href="<c:url value='/template/css/style/show_PN_details.css' />" />
	    ' />
<meta name="scripts"
	content='
	        	<script src="<c:url value='/template/js/quan_li_phieunhap.js' />"></script>
	    ' />
</head>

<body>


	<div class="main-content">

		<%@ include file="show_phieunhap.jsp"%>

		<form action="<c:url value='/quan-tri/phieunhap/danh-sach'/>"
			id="formSubmit" method="get">
			<section id="wrapper">

			<div class="p-4">
				<button type="button" class="bt-trash"
					style="background-color: #ADD8E6;">
					<a style="color: white;"
						href="<c:url value='/quan-tri/phieunhap/trash'/>"> <i
						class="fa-solid fa-floppy-disk"></i> <span class="trash-count">
							<c:if test="${not empty model.num_trash}">
																				${model.num_trash}
															</c:if>
					</span>
					</a>
				</button>
				<div class="table" style="margin-top: 50px;">
					<div class="table-header">
						<p>Danh sách phiếu nhập</p>

						<div class="filter-container">
							<p>Thời gian:</p>
							<select id="timeFilter" name="timeFilter"
								onchange="submitFormWithFilter()">
								<option value="">Bỏ lọc</option>
								<option value="today"
									${param.timeFilter == 'today' ? 'selected' : ''}>Hôm nay</option>
								<option value="yesterday"
									${param.timeFilter == 'yesterday' ? 'selected' : ''}>Hôm
									qua</option>
								<option value="week"
									${param.timeFilter == 'week' ? 'selected' : ''}>Tuần này</option>
								<option value="month"
									${param.timeFilter == 'month' ? 'selected' : ''}>Tháng
									này</option>
							</select>

						</div>


					</div>
					<div id="table">
						<div class="table-section">
							<table>
								<thread>
								<tr>
									<th style="width: 60px">STT</th>
									<th>Mã phiếu nhập</th>
									<th>Nhà cung cấp</th>
									<th>Nhân viên nhập</th>
									<th>Tổng tiền</th>
									<th>Thời gian nhập</th>
									<th>Hành động</th>
								</tr>
								</thread>
								<tbody id="ds">

									<c:choose>
										<c:when test="${not empty model.listResult}">


											<c:forEach var="item" items="${model.listResult}"
												varStatus="i">

												<tr style="color: black">
													<td>${(model.page - 1) * model.limit + i.index + 1}</td>

													<td>${item.id}</td>

													<td style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>${item.nhaCungCapName}</td>

													<td>${item.nguoiTaoPhieuName}</td>

													<td><fmt:formatNumber value="${item.tongTien}"
															type="number" pattern="#,##0" /> đ</td>

													<td><fmt:formatDate value="${item.thoiGian}"
															pattern="dd-MM-yyyy" /></td>

													<td>
														<button type="button" onclick='show(${item.id});'>
															<a style='color: white'> <i class='fa-solid fa-eye'></i>
															</a>
														</button>
														<button type="button" onclick='Delete(${item.id});'
															style='background-color: blue'>
															<i class='fa-solid fa-cloud'></i>
														</button>
														<button type="button" style='background-color: orange'>
															<a style='color: white'
																href='http://localhost:8080/Spring-mvc/quan-tri/phieunhap/export/${item.id}'>
																<i class='fa-solid fa-file-export'></i>
															</a>
														</button>
													</td>

												</tr>
											</c:forEach>

										</c:when>
										<c:otherwise>
											<tr style='border: 0; height: 300px'>
												<td colspan='7' class='text-center'><img
													style='border: 0; width: 500px; height: 250px !important;'
													src='https://i.ibb.co/1fjWK3sC/images.jpg'></td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>

							<ul class="pagination" id="pagination"></ul>
							<input type="hidden" value="" id="page" name="page" /> <input
								type="hidden" value="" id="limit" name="limit" />

						</div>

					</div>
				</div>
			</div>
	</div>
	</section>
	</form>
	</div>

	<script type="text/javascript">
		var totalPages = ${model.totalPage};
		var currentPage = ${model.page};
		var limit = 7;
		$(function () {
			window.pagObj = $('#pagination').twbsPagination({
				totalPages: totalPages,
				visiblePages: 7,
				startPage: currentPage,
				onPageClick: function (event, page) {
					if (currentPage != page) {
						$('#limit').val(limit);
						$('#page').val(page);
						/* $('#sortName').val('title');
						$('#sortBy').val('desc');
						$('#type').val('list');  */
						$('#formSubmit').submit();
					}
				},
			   paginationClass: 'pagination',
			   nextClass: 'page-item next',
			   prevClass: 'page-item prev',
			   lastClass: 'page-item last',
			   firstClass: 'page-item first',
			   pageClass: 'page-item',
			   activeClass: 'active',
			   disabledClass: 'disabled',
			   anchorClass: 'page-link'  
			});
		});

		function submitFormWithFilter() {
			document.getElementById("page").value = 1; // reset về trang 1 khi lọc
			document.getElementById("formSubmit").submit();
		}
	</script>

</body>

</html>