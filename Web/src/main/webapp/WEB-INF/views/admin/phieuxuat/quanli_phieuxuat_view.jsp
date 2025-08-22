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
			href="<c:url value='/template/css/style/phieuxuat.css' />" />
	    ' />

<meta name="scripts"
	content='
	        	<script src="<c:url value='/template/js/quan_li_phieuxuat.js' />"></script>
	    ' />
</head>

<body>

	<div class="main-content">

			<%@ include file="show_phieuxuat.jsp"%>

		<form action="<c:url value='/quan-tri/donhang/danh-sach'/>"
			id="formSubmit" method="get">

			<section id="wrapper">

			<div class="p-4">
				<button type="button" class="bt-trash" style="background-color: #ADD8E6;">
					<a style="color: white;"
						href="<c:url value='/quan-tri/donhang/save'/>"> <i
						class="fa-solid fa-floppy-disk"></i> <span class="trash-count">
							<c:if test="${not empty model.num_trash}">
																				${model.num_trash}
															</c:if>
					</span>
					</a>
				</button>
				<div class="table" style="margin-top: 50px;">
					<div class="table-header">
						<p>Danh sách đơn hàng</p>

						<div class="filter-container">
							<p>Thời gian:</p>

							<select id="timeFilter" name="timeFilter">
<!-- 								onchange="submitFormWithFilter()"
 -->								
 
								<option value="" ${empty param.timeFilter ? 'selected' : ''} >Bỏ lọc</option>
								<option value="today"
									${param.timeFilter == 'today' ? 'selected' : ''}>Hôm
									nay</option>
								<option value="yesterday"
									${param.timeFilter == 'yesterday' ? 'selected' : ''}>Hôm
									qua</option>
								<option value="week"
									${param.timeFilter == 'week' ? 'selected' : ''}>Tuần
									này</option>
								<option value="month"
									${param.timeFilter == 'month' ? 'selected' : ''}>Tháng
									này</option>
							</select>

						</div>

						<div class="filter-container">
							<p>Trạng thái:</p>
							<select id="statusFilter" name="statusFilter">
<!-- 								onchange="submitFormWithFilter()">
 -->								<option value="" ${empty param.statusFilter ? 'selected' : ''} >Bỏ lọc</option>
								<option value="0" ${statusFilter == 0 ? 'selected' : ''}>Chưa
									duyệt</option>
								<option value="1" ${statusFilter == 1 ? 'selected' : ''}>Chưa
									lấy hàng</option>
								<option value="2" ${statusFilter == 2 ? 'selected' : ''}>Chưa
									giao</option>
								<option value="3" ${statusFilter == 3 ? 'selected' : ''}>Chưa
									xác nhận giao</option>
								<option value="4" ${statusFilter == 4 ? 'selected' : ''}>Đã
									giao</option>
								<option value="-1" ${statusFilter == -1 ? 'selected' : ''}>Nhân
									viên hủy</option>
								<option value="-2" ${statusFilter == -2 ? 'selected' : ''}>Khách
									hàng gửi đơn hủy</option>
								<option value="-3" ${statusFilter == -3 ? 'selected' : ''}>Khách
									hàng hủy</option>
							</select>
						</div>

						<button id="applyFilters" type="button" onclick="submitFormWithFilter()">Lọc</button>

					</div>
					<div id="table">
						<div class="table-section">
							<table>
								<thread>
								<tr>
									<th style="width: 75px">Mã PX</th>
									<th style="width: 150px">Khách hàng</th>
									<th style="width: 130px">Điện thoại</th>
									<th style="width: 145px">Tổng tiền</th>
									<th style="width: 170px">Ngày tạo đơn hàng</th>
									<th style="width: 185px">Trạng thái</th>
									<th style="width: 385px">Xử lý đơn</th>
									<th>Hành động</th>
								</tr>
								</thread>
								<tbody id="ds">

									<c:choose>
										<c:when test="${not empty model.listResult}">


											<c:forEach var="item" items="${model.listResult}"
												varStatus="i">

												<tr style="color: black">

													<td>${item.id}</td>

													<td>${item.khachHangName}</td>

													<td>${item.sdt}</td>

													<td><fmt:formatNumber value="${item.tongTien}"
															type="number" pattern="#,##0" /> đ</td>

													<td><fmt:formatDate value="${item.thoiGian}"
															pattern="dd-MM-yyyy" /></td>


													<c:choose>
														<c:when test="${item.status == 0}">
															<td id='status1_${item.id}'
																style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>Đang
																chờ duyệt</td>

															<td id="process_${item.id}"><button type="button"
																	class="status duyet" id="status_${item.id}"
																	onclick="change_status(${item.id},1)">
																	<a style="color: white"> Duyệt đơn hàng </a>
																</button>
																<button type="button" class="status canceled" id="status_${item.id}"
																	onclick="change_status(${item.id},-1)">
																	<a style="color: white"> Hủy đơn </a>
																</button></td>
														</c:when>
														<c:when test="${item.status == 1}">
															<td id='status1_${item.id}'
																style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>Đang
																lấy hàng</td>

															<td id="process_${item.id}">
																<button type="button" class="status pending" id="status_${item.id}"
																	onclick="change_status(${item.id},2)">
																	<a style="color: white"> Lấy hàng </a>
																</button>
																<button type="button" class="status canceled" id="status_${item.id}"
																	onclick="change_status(${item.id},-1)">
																	<a style="color: white"> Hủy đơn </a>
																</button>
															</td>
														</c:when>
														<c:when test="${item.status == 2}">
															<td id='status1_${item.id}'
																style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>Đang
																chờ giao hàng</td>

															<td id="process_${item.id}">

																<button type="button" class="status shipped" id="status_${item.id}"
																	onclick="change_status(${item.id},3)">
																	<a style="color: white"> Giao hàng </a>
																</button>
																<button type="button" class="status canceled" id="status_${item.id}"
																	onclick="change_status(${item.id},-1)">
																	<a style="color: white"> Hủy đơn </a>
																</button>
															</td>


														</c:when>
														<c:when test="${item.status == 3}">
															<td id='status1_${item.id}'
																style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>Đang
																giao hàng</td>


															<td id="process_${item.id}">
																<button type="button" class="status delivered" id="status_${item.id}"
																	onclick="change_status(${item.id},4)">
																	<a style="color: white"> Xác nhận giao hàng </a>
																</button>
																<button type="button" class="status canceled" id="status_${item.id}"
																	onclick="change_status(${item.id},-1)">
																	<a style="color: white"> Hủy đơn </a>
																</button>
															</td>

														</c:when>
														<c:when test="${item.status == 4}">
															<td id='status1_${item.id}'
																style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>Đã
																giao</td>

															<td></td>
														</c:when>
														<c:when test="${item.status == -1}">
															<td id='status1_${item.id}'
																style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>Nhân
																viên đã hủy</td>

															<td></td>
														</c:when>
														<c:when test="${item.status == -2}">
															<td id='status1_${item.id}'
																style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>Khách
																hàng gửi đơn hủy</td>

															<td id="process_${item.id}">
																<button type="button" class="status delivered" id="status_${item.id}"
																	onclick="cancle(${item.id}, '${fn:escapeXml(item.feeback)}')">
																	<a style="color: white"> Xem lý do </a>
																</button>
																<button type="button" class="status canceled" id="status_${item.id}"
																	onclick="change_status(${item.id},-3)">
																	<a style="color: white"> Xác nhận đơn hủy </a>
																</button>
															</td>

														</c:when>
														<c:otherwise>
															<td id='status1_${item.id}'
																style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>Khách
																hàng đã hủy</td>
															<td></td>
														</c:otherwise>
													</c:choose>


													<td>
														<button  type="button" onclick='show(${item.id});'>
															<a style='color: white'> <i class='fa-solid fa-eye'></i>
															</a>
														</button>
														<button type="button" onclick='Delete(${item.id});'
															style='background-color: blue'>
															<i class='fa-solid fa-cloud'></i>
														</button>
														<button type="button" style='background-color: orange'>
															<a style='color: white'
																href='http://localhost:8080/Spring-mvc/quan-tri/donhang/export/${item.id}'>
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

	<div id="cancelPopup" class="popup" style="display: none;"
		onclick="closePopup()">
		<div class="popup-content" onclick="event.stopPropagation()">
			<button type="button" class="close-btn" onclick="closePopup()">X</button>
			<h3>Lý do hủy đơn hàng</h3>
			<textarea id="cancelReason" placeholder="Nhập lý do hủy..."></textarea>
		</div>
	</div>
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