<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>
<c:url var="APIurl" value="/api-admin-new" />
<c:url var="NewURL" value="/admin-new" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>Liên hệ</title>

<meta name="stylesheet"
	content='
		
		<link rel="stylesheet"
			href="<c:url value='/template/css/style/quan_li_attribute_style.css' />" />
			
			<link rel="stylesheet"
			href="<c:url value='/template/css/style/add_attribute_style.css' />" />
			
			<link rel="stylesheet"
			href="<c:url value='/template/css/style/edit_KH_style.css' />" />
			' />
<meta name="scripts"
	content='
	        	<script src="<c:url value='/template/js/quan_li_lienhe.js' />"></script>
	    ' />
</head>

<body>

	<div class="main-content">
		<%@ include file="show_content_LH.jsp"%>
		<%@ include file="feeback.jsp"%>

		<c:if test="${not empty message}">
			<script>
				alert('${message}');
			</script>
		</c:if>

		<form action="<c:url value='/quan-tri/contact/danh-sach'/>"
			id="formSubmit" method="get">



			<section id="wrapper">

			<div class="p-4">
				<div class="table" style="margin-top: 50px;">
					<div class="table-header">
						<p>Danh sách liên hệ</p>
					</div>
					<div id="table">

						<div class="table-section">
							<table>
								<thread>
								<tr>
									<th>STT</th>
									<th>Tên khách hàng</th>
									<th>Tiêu đề</th>
									<th>Email</th>
									<th>Hành động</th>
								</tr>
								</thread>
								<tbody id="ds">
									<c:forEach var="item" items="${model.listResult}" varStatus="i">

										<tr style="color: black">

											<td>${(model.page - 1) * model.limit + i.index + 1}</td>
											<td
												style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
												${item.tenKhachHang }</td>
											<td
												style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
												${item.title }</td>
											<td
												style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
												${item.email }</td>
											<td>
												<button type="button" onclick='show(${item.id})'>
													<a style='color: white'> <i class='fa-solid fa-eye'></i>
													</a>
												</button>
												<button type="button" onclick='Delete(${item.id});'>
													<i class='fa-solid fa-trash'></i>
												</button> <c:if test="${item.status eq 0 }">

													<button type="button" style='background-color: green'
														onclick="feedback('${item.email}', '${item.tenKhachHang}', ${item.id })">
														<a style='color: white'> <i
															class='fa-solid fa-comment-dollar'></i>
														</a>
													</button>

												</c:if>
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