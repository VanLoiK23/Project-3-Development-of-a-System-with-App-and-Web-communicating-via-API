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
	        	<script src="<c:url value='/template/js/quan_li_comment.js' />"></script>
	    ' />

</head>
<body>



	<div class="main-content">

		<c:if test="${not empty message}">
			<script>
        alert('${message}');
    </script>
		</c:if>

		<form
			action="<c:url value='/quan-tri/Quanlicomment_controller/danh-sach'/>"
			id="formSubmit" method="get">

			<section id="wrapper">


			<div class="p-4">
				<div class="table" style="margin-top: 50px;">
					<div class="table-header">
						<p>Danh sách bình luận</p>

						<div class="filter-container">
							<p>Số sao:</p>
							<select id="timeFilter" name="rateFilter">
								<option value="" ${empty param.rateFilter ? 'selected' : ''}>Bỏ
									lọc</option>
								<option value="1" ${ param.rateFilter ==1 ? 'selected' : ''}>★☆☆☆☆</option>
								<option value="2" ${ param.rateFilter  == 2? 'selected' : ''}>★★☆☆☆</option>
								<option value="3" ${ param.rateFilter == 3 ? 'selected' : ''}>★★★☆☆</option>
								<option value="4" ${ param.rateFilter  == 4? 'selected' : ''}>★★★★☆</option>
								<option value="5" ${ param.rateFilter  == 5? 'selected' : ''}>★★★★★</option>
							</select>
						</div>

						<div class="filter-container">
							<p>Trạng thái:</p>
							<select id="statusFilter" name="statusFilter">
								<option value="" ${empty statusFilter ? 'selected' : ''}>Bỏ
									lọc</option>
								<option value="1" ${ statusFilter==1 ? 'selected' : ''}>Đã
									phản hồi</option>
								<option value="0" ${ statusFilter==0 ? 'selected' : ''}>Chưa
									phản hồi</option>
							</select>
						</div>

						<button id="applyFilters" type="button"
							onclick="submitFormWithFilter()">Lọc</button>
					</div>
					<div id="table">

						<div class="table-section">
							<table>
								<thread>
								<tr>
									<th style='width: 70px'>STT</th>
									<th style='width: 200px'>Tên khách hàng</th>
									<th>Nội dung</th>
									<th style='width: 150px'>Số sao ĐG</th>
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

													<td
														style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>
														${item.hoten }</td>

													<td
														style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>${item.content}</td>



													<td><c:forEach var="j" begin="1" end="5">
															<c:choose>
																<c:when test="${j <= item.rate}">
																	<i style="color: #FFD700;" class="fa-solid fa-star"></i>
																</c:when>
																<c:when test="${j - 0.5 <= item.rate&& j > item.rate}">
																	<i style="color: #FFD700;"
																		class="fa-solid fa-star-half-stroke"></i>
																</c:when>
																<c:otherwise>
																	<i style="color: #FFD700;" class="fa-regular fa-star"></i>
																</c:otherwise>
															</c:choose>
														</c:forEach></td>

													<td>
														<button onclick='show(${item.id})' type="button">
															<a style='color: white'
																href='http://localhost:8080/Spring-mvc/quan-tri/Quanlicomment_controller/show_content/${item.id }'>
																<i class='fa-solid fa-eye'></i>
															</a>
														</button>
														<button onclick='Delete(${item.id});' type="button">
															<i class='fa-solid fa-trash'></i>
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