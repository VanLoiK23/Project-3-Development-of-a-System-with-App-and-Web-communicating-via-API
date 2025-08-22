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
			
		
	    ' />
<meta name="scripts"
	content='
	        	<script src="<c:url value='/template/js/quan_li_slide.js' />"></script>
	    ' />

</head>

<body>

	<div class="main-content">

		<%@ include file="add_Slide.jsp"%>
		<%@ include file="edit_Slide.jsp"%>

		<c:if test="${not empty message}">
			<script>
				alert('${message}');
			</script>
		</c:if>

		<form action="<c:url value='/quan-tri/slide/danh-sach'/>"
			id="formSubmit" method="get">


			<section id="wrapper">
			<div class="p-4">
				<button type="button" class="bt-trash">
					<a style="color: white;"
						href="<c:url value='/quan-tri/slide/trash'/>"> <i
						class="fa-solid fa-trash-can"></i> <span class="trash-count">
							<c:if test="${not empty model.num_trash}">
																				${model.num_trash}
															</c:if>
					</span>
					</a>
				</button>
				<div class="table" style="margin-top: 50px;">
					<div class="table-header">
						<p>Danh sách Slide</p>
						<button type="button" class="btn-add" onclick="add()">
							<i class="fa-solid fa-square-plus"></i>Thêm mới
						</button>
					</div>
					<div id="table">

						<div class="table-section">
							<table>
								<thread>
								<tr>
									<th style="width: 100px">STT</th>
									<th style="width: 300px">Hình ảnh</th>
									<th>Trạng thái</th>
									<th>Hành động</th>

								</tr>
								</thread>
								<tbody id="ds">

									<c:forEach var="item" items="${model.listResult}" varStatus="i">

										<tr style="color: black">
											<td>${(model.page - 1) * model.limit + i.index + 1}</td>

											<td><img src='${item.image }' alt='anh'
												style='width: 300px; scale: calc(0.8)'></td>

											<c:url var="disable" value="/quan-tri/slide/status">
												<c:param name="type" value="disable" />
												<c:param name="id" value="${item.id}" />
												<c:param name="page" value="${model.page}" />
											</c:url>

											<c:url var="active" value="/quan-tri/slide/status">
												<c:param name="type" value="active" />
												<c:param name="id" value="${item.id}" />
												<c:param name="page" value="${model.page}" />
											</c:url>


											<td><c:choose>

													<c:when test="${item.status == 1}">
														<a href='${disable}'><i
															class='fa-regular fa-circle-check'></i></a>
													</c:when>
													<c:otherwise>
														<a href='${active}'><i
															class='fa-regular fa-circle-xmark'></i></a>
													</c:otherwise>
												</c:choose></td>

											<td>
												<button type="button" onclick="show(${item.id})">
													<a style="color: white"> <i
														class="fa-solid fa-pen-to-square"></i>
													</a>
												</button>
												<button type="button" onclick="Delete(${item.id})">
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