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
		
	    ' />
<meta name="scripts"
	content='
	        	<script src="<c:url value='/template/js/quan_li_attribute.js' />"></script>
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
							href="<c:url value='/quan-tri/slide/danh-sach'/>"><i
							class="fa-solid fa-backward"></i></a>
					</button>
					<p style="margin-right: 30px">Thùng rác</p>
					<p></p>
				</div>
				<div class="table-section">
					<table>
						<thread>
						<tr>
							<th style="width: 200px">STT</th>
							<th style="width: 450px">Hình ảnh</th>
							<th>Hành động</th>

						</tr>
						</thread>
						<tbody id="trash_list">

							<c:choose>
								<c:when test="${not empty model.listResult}">

									<c:forEach var="item" items="${model.listResult}" varStatus="i">
										<tr style='color: black'>
											<td>${i.index + 1}</td>

											<td><img src='${item.image }' alt='anh'
												style='width: 300px; scale: calc(0.8)'></td>

											<td>
												<button>
													<a style='color: white'
														href='http://localhost:8080/Spring-mvc/quan-tri/slide/reset/${item.id}'>
														<i class='fa-solid fa-arrows-rotate'></i>
													</a>
												</button>
											</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr style='border: 0'>
										<td colspan='5' class='text-center'><span
											style='color: gray; display: flex; justify-content: center'>Thùng
												rác rỗng.</span></td>
									</tr>

								</c:otherwise>
							</c:choose>
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