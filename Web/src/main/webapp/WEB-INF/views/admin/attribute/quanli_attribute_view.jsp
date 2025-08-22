<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>
<c:url var="APIurl" value="/api-admin-new" />
<c:url var="NewURL" value="/admin-new" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>\
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
	        	<script src="<c:url value='/template/js/quan_li_attribute.js' />"></script>
	    ' />

</head>

<body>

	<div class="main-content">
		
		<%@ include file="add_Attribute.jsp"%>
		<%@ include file="edit_Attribute.jsp"%>
		
		<c:if test="${not empty message}">
			<script>
        alert('${message}');
    </script>
		</c:if>
		
		<form
			action="<c:url value='/quan-tri/attribute-sp/danh-sach?type=${type}'/>"
			id="formSubmit" method="get">

			<section id="wrapper">
			<div class="p-4">
				<button type="button" class="bt-trash">
					<a style="color: white;"
						href="<c:url value='/quan-tri/attribute-sp/trash?type=${type}'/>">
						<i class="fa-solid fa-trash-can"></i> <span class="trash-count">
							<c:if test="${not empty model.num_trash}">
														${model.num_trash}
									</c:if>
					</span>
					</a>
				</button>
				<div class="table" style="margin-top: 50px;">
					<div class="table-header">
						<p>
							Danh sách

							<%
						Map<String, String> map = new HashMap<>();
						map.put("xuatxu", "xuất xứ");
						map.put("thuonghieu", "thương hiệu");
						map.put("hedieuhanh", "hệ điều hành");
						map.put("mausac", "màu sắc");
						map.put("dungluongram", "dung lượng ram");
						map.put("dungluongrom", "dung lượng rom");

						Object pageObj = request.getAttribute("type"); // Lấy từ request scope
						String value = (pageObj != null) ? (String) pageObj : "";

						String label = map.get(value);
						if (label != null) {
							out.print(label);
							request.setAttribute("label", label);
						} else {
							out.print("màu sắc");
						}

						Map<String, String> map1 = new HashMap<>();
						map1.put("xuatxu", "tenXuatXu");
						map1.put("thuonghieu", "tenThuongHieu");
						map1.put("hedieuhanh", "tenHeDieuHanh");
						map1.put("mausac", "tenMauSac");
						map1.put("dungluongram", "kichThuocRam");
						map1.put("dungluongrom", "kichThuocRom");

						String label1 = map1.get(value);
						if (label1 != null) {
							request.setAttribute("label1", label1);
						}

						Object pageObj1 = request.getAttribute("page"); // Lấy từ request scope
						System.out.println("Page: " + pageObj1);
						int page1 = (pageObj != null) ? (Integer) pageObj1 : 1;
						int limit = 7;
						int i = (page1 - 1) * limit;
						%>


						</p>
						<button type="button" class="btn-add" onclick="add()">
							<i class="fa-solid fa-square-plus"></i>Thêm mới
						</button>
						<%-- <div>
							<input name-table="<?php echo $value ?>" type="text" id="search"
								placeholder="Tìm kiếm">
						</div> --%>
					</div>
					<div id="table">
						<c:if test="${type == 'thuonghieu'}">
							<div class="table-section">
								<table>
									<thead>
										<tr>
											<th style="width: 60px">STT</th>
											<th>${tenAttri}</th>
											<th style="width: 260px">Hình ảnh</th>
											<th>Trạng thái</th>
											<th>Hành động</th>
										</tr>
									</thead>
									<tbody id="ds">
										</c:if>
										<c:if test="${type != 'thuonghieu'}">
											<div class="table-section">
												<table>
													<thread>
													<tr>
														<th>STT</th>
														<th>${tenAttri}</th>
														<th>Trạng thái</th>
														<th>Hành động</th>

													</tr>
													</thread>
													<tbody id="ds">
														</c:if>

														<c:forEach var="item" items="${model.listResult}">
															<%
															i++;
															%>
															<tr style="color: black">
																<td><%=i%></td>

																<c:choose>
																	<c:when
																		test="${type == 'dungluongram' || type == 'dungluongrom'}">
																		<c:choose>
																			<c:when test="${type == 'dungluongram'}">
																				<c:choose>
																					<c:when test="${item.kichThuocRam == 1000}">
																						<td>1 TB</td>
																					</c:when>
																					<c:otherwise>
																						<td>${item.kichThuocRam}GB</td>
																					</c:otherwise>
																				</c:choose>
																			</c:when>

																			<c:when test="${type == 'dungluongrom'}">
																				<c:choose>
																					<c:when test="${item.kichThuocRom == 1000}">
																						<td>1 TB</td>
																					</c:when>
																					<c:otherwise>
																						<td>${item.kichThuocRom}GB</td>
																					</c:otherwise>
																				</c:choose>
																			</c:when>
																		</c:choose>
																	</c:when>

																	<c:otherwise>
																		<c:choose>
																			<c:when test="${type == 'xuatxu'}">
																				<td>${item.tenXuatXu}</td>
																			</c:when>
																			<c:when test="${type == 'thuonghieu'}">
																				<td>${item.tenThuongHieu}</td>
																			</c:when>
																			<c:when test="${type == 'hedieuhanh'}">
																				<td>${item.tenHeDieuHanh}</td>
																			</c:when>
																			<c:when test="${type == 'mausac'}">
																				<td>${item.tenMauSac}</td>
																			</c:when>
																			<c:otherwise>
																				<td>${item.tenMauSac}</td>
																			</c:otherwise>
																		</c:choose>

																	</c:otherwise>
																</c:choose>

																<c:if test="${type == 'thuonghieu'}">
																	<td><img src="${item.image}" alt="anh"
																		style="width: 170px; scale: calc(0.75)"></td>
																</c:if>

																<c:url var="disable"
																	value="/quan-tri/attribute-sp/status">
																	<c:param name="type" value="${type}" />
																	<c:param name="action" value="disable" />
																	<c:param name="id" value="${item.id}" />
																	<c:param name="page" value="${model.page}" />
																</c:url>

																<c:url var="active"
																	value="/quan-tri/attribute-sp/status">
																	<c:param name="type" value="${type}" />
																	<c:param name="action" value="active" />
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
																	<button type="button" onclick="show('${type}', ${item.id})">
																		<a style="color: white"> <i
																			class="fa-solid fa-pen-to-square"></i>
																		</a>
																	</button>
																	<button type="button" onclick="Delete('${type}', ${item.id})">
																		<i class="fa-solid fa-trash"></i>
																	</button>
																</td>
															</tr>
														</c:forEach>


													</tbody>
												</table>

												<ul class="pagination" id="pagination"></ul>
												<input type="hidden" value="" id="page" name="page" /> <input
													type="hidden" value="" id="limit" name="limit" /> <input
													type="hidden" value="" id="type" name="type" />
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
	var type = "${type}";
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
					$('#type').val(type);
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