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
			href="<c:url value='/template/css/style/add_attribute_style.css' />" />
			<link rel="stylesheet"
			href="<c:url value='/template/css/style/edit_KH_style.css' />" />
			<link rel="stylesheet"
			href="<c:url value='/template/css/style/quan_li_KH_style.css' />" />
			
		
	    ' />
<meta name="scripts"
	content='
	        	<script src="<c:url value='/template/js/quan_li_KH.js' />"></script>
	    ' />

</head>
<body>

	<div class="main-content">

		<%@ include file="edit_KH.jsp"%>

		<c:if test="${not empty message}">
			<script>
		    alert('${message}');
		</script>
		</c:if>

		<form
			action="<c:url value='/quan-tri/Quanlikhachhang_controller/danh-sach'/>"
			id="formSubmit" method="get">

			<section id="wrapper">


			<div class="p-4">
				<button type="button" class="bt-trash">
					<a style="color: white;"
						href="<c:url value='/quan-tri/Quanlikhachhang_controller/trash'/>">
						<i class="fa-solid fa-trash-can"></i> <span class="trash-count">
							<c:if test="${not empty model.num_trash}">
																										${model.num_trash}
																					</c:if>
					</span>
					</a>
				</button>
				<div class="table" style="margin-top: 50px;">
					<div class="table-header">
						<p>Danh sách khách hàng</p>
						<!-- <div>
							<input type="text" id="search" placeholder="Tìm kiếm">
						</div> -->
					</div>
					<div id="table">
						<div class="table-section">
							<table>
								<thread>
								<tr>
									<th>STT</th>
									<th>Tên</th>
									<th>Email</th>
									<th>Số điện thoại</th>
									<th>Hành động</th>

								</tr>
								</thread>
								<tbody id="dskh">

									<c:forEach var="item" items="${model.listResult}" varStatus="i">

										<tr style="color: black">
											<td>${(model.page - 1) * model.limit + i.index + 1}</td>

											<td>${item.name}</td>
											<td
												style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>${item.email}</td>
											<td
												style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>${item.phone}</td>


											<td>
												<button type="button" onclick="show(${item.id})">
													<a style="color: white"> <i
														class="fa-solid fa-pen-to-square"></i>
													</a>
												</button> <c:choose>

													<c:when test="${item.statusString == 'lock'}">
														<button type="button" style='background-color: green;'
															onclick='active(${item.id});'>
															<i class='fa-solid fa-lock-open'></i>
														</button>

													</c:when>
													<c:otherwise>
														<button type="button" style='background-color: orange;'
															onclick='lock(${item.id});'>
															<i class='fa-solid fa-lock'></i>
														</button>
													</c:otherwise>
												</c:choose>
												<button type="button" style="background-color: red"
													onclick="Delete(${item.id})">
													<i class="fa-solid fa-trash"></i>
												</button>
											</td>
										</tr>
									</c:forEach>

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

		</script>


</body>

</html>