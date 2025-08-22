<%@include file="/common/taglib.jsp"%>
<c:url var="APIurl" value="/api-admin-new" />
<c:url var="NewURL" value="/admin-new" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>


<title>Danh sách sản phẩm</title>


<meta name="stylesheet"
	content='
	<link rel="stylesheet"
		href="<c:url value='/template/css/style/quan_li_sp_style.css' />" />
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/show_details_sp.css' />" />
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/config_sp.css' />" />
		<link rel="stylesheet"
		href="<c:url value='/template/css/style/import_sp_style.css' />" />
    ' />
<meta name="scripts"
	content='
        	<script src="<c:url value='/template/js/quan_li_sp.js' />"></script>
    ' />

<%-- <%@ include file="show_details_sp.php.jsp"%>
 --%>
</head>

<body>

	<%
	// Lấy giá trị từ session
	String message = (String) session.getAttribute("message");
	String check1 = (String) session.getAttribute("check1");
	String check = (String) session.getAttribute("check");

	if (message != null) {
		if ("success".equals(message)) {
	%>
	<script>
		alert("Cập nhật thông tin sản phẩm thành công");
	</script>
	<%
	} else if ("error".equals(message)) {
	%>
	<script>
		alert("Cập nhật thông tin sản phẩm không thành công");
	</script>
	<%
	}
	session.removeAttribute("message");
	}

	if (check1 != null) {
	if ("success".equals(check1)) {
	%>
	<script>
		alert("Thêm sản phẩm thành công");
	</script>
	<%
	} else if ("error".equals(check1)) {
	%>
	<script>
		alert("Thêm sản phẩm không thành công");
	</script>
	<%
	}
	session.removeAttribute("check1");
	}

	if (check != null) {
	if ("true".equals(check)) {
	%>
	<script>
		alert("Xóa thành công");
	</script>
	<%
	} else if ("false".equals(check)) {
	%>
	<script>
		alert("Xóa không thành công");
	</script>
	<%
	}
	session.removeAttribute("check");
	}
	%>

	<%-- <%@ include file="show_details_sp.jsp"%>
	<%@ include file="show_config_sp.jsp"%>
	<%@ include file="import_sp.jsp"%> --%>

	<%-- <jsp:include page="aside_menu.jsp" />
<jsp:include page="show_details_sp.jsp" />
<jsp:include page="show_config_sp.jsp" />
<jsp:include page="import_sp.jsp" /> --%>

	<div class="main-content">
		<%@ include file="show_details_sp.jsp"%>
		<%@ include file="show_config_sp.jsp"%>
		<%@ include file="import_sp.jsp"%>

		<c:if test="${not empty message}">
			<script>
        alert('${message}');
    </script>
		</c:if>

		<form
			action="<c:url value='/quan-tri/Quanlisanpham_controller/danh-sach'/>"
			id="formSubmit" method="get">

			<section id="wrapper">

			<div class="p-4">
				<button class="bt-trash" type="button">
					<a style="color: white;"
						href='<c:url value="/quan-tri/Quanlisanpham_controller/trash"/>'>
						<i class="fa-solid fa-trash-can"></i> <span class="trash-count">
							<c:if test="${not empty model.num_trash}">
								${model.num_trash}
							</c:if>
					</span>
					</a>
				</button>

				<div class="table" style="margin-top: 50px;">
					<div class="table-header">
						<p>Danh sách sản phẩm</p>
						<button class="btn-add" type="button"
							style="display: block; border-radius: 20px !important">
							<a style="color: #ffffff"
								href='<c:url value="/quan-tri/Quanlisanpham_controller/add_sp"/>'>
								<i class="fa-solid fa-square-plus"></i>
							</a>Thêm mới
						</button>
						<div>
							<input type="text" id="search" placeholder="Tìm kiếm">
						</div>
					</div>
					<div id="table">
						<div class="table-section">
							<table id="productTable">
								<thread>
								<tr>
									<th style="width: 70px">STT</th>
									<th>Hình ảnh</th>
									<th style="width: 170px !important">Tên sản phẩm</th>
									<th>Số lượng<br>trong kho
									</th>
									<th>Trạng thái</th>
									<th>Nhập hàng</th>
									<th style="width: 170px !important">Hành động</th>

								</tr>
								</thread>
								<tbody id="dssp">


									<%
									Object pageObj = request.getAttribute("page"); // Lấy từ request scope
									System.out.println("Page: " + pageObj);
									int page1 = (pageObj != null) ? (Integer) pageObj : 1;
									int limit = 7;
									int i = (page1 - 1) * limit;
									/* pageContext.setAttribute("i", i); ngu canh page */
									%>



									<%-- <c:set var="page1"
										value="${not empty model.page ? model.page : 1}" />
									<c:set var="limit" value="7" />
									<c:set var="i" value="${(page1 - 1) * limit}" /> --%>


									<c:forEach var="item" items="${model.listResult}">
										<%
										i++;
										%>
										<tr style='color: black'>
											<td><%=i%></td>
											<%-- <td>${i}</td>
											<c:set var="i" value="${i + 1}" /> --%>
											<td><img src='${item.hinhAnh.split(",")[0]}' /></td>
											<td
												style='white-space: nowrap; overflow: hidden; text-overflow: ellipsis;'>${item.tenSanPham}</td>
											<td>${item.soLuongCon}</td>
											<c:url var="disable"
												value="/quan-tri/Quanlisanpham_controller/status">
												<c:param name="type" value="disable" />
												<c:param name="id" value="${item.id}" />
												<c:param name="page" value="${model.page}" />
											</c:url>

											<c:url var="active"
												value="/quan-tri/Quanlisanpham_controller/status">
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
												<button type="button" style='background-color: #C71585'
													onclick='importt(${item.id})'>
													<a style='color: white'> <i
														class='fa-solid fa-cart-shopping'></i></a>
												</button>
											</td>
											<td>
												<button type="button" onclick='show_details_sp(${item.id})'>
													<a style='color: white'> <i class='fa-solid fa-eye'></i></a>
												</button>
												<button type="button">
													<a style='color: white'
														href='<c:url value="/quan-tri/Quanlisanpham_controller/update_sp"> <c:param name="id_sp" value="${item.id}"/> </c:url>'>
														<i class='fa-solid fa-pen-to-square'></i>
													</a>
												</button>
												<button type="button" onclick='Delete(${item.id})'>
													<i class='fa-solid fa-trash'></i>
												</button>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>

							<ul class="pagination" id="pagination"></ul>
							<input type="hidden" value="" id="page" name="page" /> <input
								type="hidden" value="" id="limit" name="limit" />
							<!-- <input type="hidden" value="" id="sortName" name="sortName"/>
											<input type="hidden" value="" id="sortBy" name="sortBy"/>
											<input type="hidden" value="" id="type" name="type"/> -->
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