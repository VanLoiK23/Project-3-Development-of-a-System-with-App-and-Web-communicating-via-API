<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><dec:title default="Thế giới điện thoại" /></title>
<!-- icon shortcut -->
<link rel="shortcut icon"
	href="<c:url value='/template/images/favicon.ico' />" />

<!-- Load font awesome icons -->
<link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css'
	rel='stylesheet'>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
	crossorigin="anonymous">

<!-- owl carousel libraries -->
<link rel="stylesheet"
	href="<c:url value='/template/js/owlcarousel/owl.carousel.min.css' />" />

<link rel="stylesheet"
	href="<c:url value='/template/js/owlcarousel/owl.theme.default.min.css' />" />

<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

<!-- our files -->
<!-- css -->
<link rel="stylesheet"
	href="<c:url value='/template/css/style1/style.css' />" />

<link rel="stylesheet"
	href="<c:url value='/template/css/style1/topnav.css' />" />

<link rel="stylesheet"
	href="<c:url value='/template/css/style1/header.css' />" />

<link rel="stylesheet"
	href="<c:url value='/template/css/style1/banner.css' />" />

<link rel="stylesheet"
	href="<c:url value='/template/css/style1/footerFPT.css' />" />

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
	rel="stylesheet">

<dec:getProperty property="meta.stylesheet" />

</head>
<body>
	<!-- header -->
	<%@ include file="/common/web/header.jsp"%>
	<!-- header -->

	<div class="container1">
		<dec:body />
	</div>

	<!-- footer -->
	<%@ include file="/common/web/footer.jsp"%>
	<!-- footer -->

	<script type="text/javascript"
		src="<c:url value='/template/web/jquery/jquery.min.js' />"></script>
	<script type="text/javascript"
		src="<c:url value='/template/web/bootstrap/js/bootstrap.bundle.min.js' />"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.jshttps://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"></script>
	<script type="text/javascript"
		src="<c:url value='/template/js/owlcarousel/owl.carousel.min.js' />"></script>
	<script
		src="<c:url value='/template/admin/paging/jquery.twbsPagination.js' />"></script>
	<script
		src="<c:url value='/template/admin/paging/jquery.twbsPagination.min.js' />"></script>

	<dec:getProperty property="meta.scripts" />


</body>
</html>