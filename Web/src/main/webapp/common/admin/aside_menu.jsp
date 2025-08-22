<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<aside class="sidebar position-fixed top-0 left-0 overflow-auto h-100 float-left" id="show-side-navigation1">
  <i class="uil-bars close-aside d-md-none d-lg-none" data-close="show-side-navigation1"></i>
  <div class="sidebar-header d-flex justify-content-center align-items-center px-3 py-4">
    <img class="rounded-pill img-fluid" width="65"
      src="https://i.ibb.co/d0PjC4nh/download.jpg"
      alt="User Avatar">
    <div class="ms-2">
      <h5 class="fs-6 mb-0">
        <a class="text-decoration-none" href="#">${sessionScope.name}</a>
      </h5>
      <p class="mt-1 mb-0">Trang quản lý website bán điện thoại.</p>
    </div>
  </div>

  <ul class="categories list-unstyled">
    <li id="homepage">
      <i class="uil-estate fa-fw"></i><a href='<c:url value="/quan-tri/Homepage_admin"/>' class="side-menu-header">Trang chủ</a>
    </li>
    <hr style="color:bisque">
    <li style="color:aliceblue">Quản lý cửa hàng</li>
    <li id="sanpham">
      <i class="fa-solid fa-mobile-screen-button"></i><a 
	  
	  href='<c:url value="/quan-tri/Quanlisanpham_controller/danh-sach?page=1&limit=7"/>'
	  
        class="side-menu-header">Sản phẩm</a>
    </li>
    <li class="has-dropdown">
      <i class="fa-solid fa-calendar-plus"></i><a href="#" class="side-menu-header">Thuộc tính sản phẩm</a>
      <ul class="sidebar-dropdown list-unstyled">
        <li><a 
			href='<c:url value="/quan-tri/attribute-sp/danh-sach?page=1&limit=7&type=xuatxu"/>'>Xuất xứ</a></li>
        <li><a 
			href='<c:url value="/quan-tri/attribute-sp/danh-sach?page=1&limit=7&type=hedieuhanh"/>'
			>Hệ điều hành</a></li>
        <li><a 
			href='<c:url value="/quan-tri/attribute-sp/danh-sach?page=1&limit=7&type=thuonghieu"/>'
			>Thương hiệu</a></li>
        <li><a 
			href='<c:url value="/quan-tri/attribute-sp/danh-sach?page=1&limit=7&type=mausac"/>'
			>Màu sắc</a></li>
        <li><a 
			href='<c:url value="/quan-tri/attribute-sp/danh-sach?page=1&limit=7&type=dungluongram"/>'
			>Ram</a></li>
        <li><a 
			href='<c:url value="/quan-tri/attribute-sp/danh-sach?page=1&limit=7&type=dungluongrom"/>'
			>Rom</a></li>
      </ul>
    </li>
    <li id="kvkho">
      <i class="fa-solid fa-warehouse"></i><a 
	  href='<c:url value="/quan-tri/khuvuckho/danh-sach?page=1&limit=7"/>'
	 class="side-menu-header">Khu vực kho</a>
    </li>
    <li id="supplier">
      <i class="fa-solid fa-boxes-packing"></i><a 
	  href='<c:url value="/quan-tri/supplier/danh-sach?page=1&limit=7"/>'
	  class="side-menu-header">Nhà cung cấp</a>
    </li>
    <li id="slide">
      <i class="fa-brands fa-slideshare"></i><a 
	  
	  href='<c:url value="/quan-tri/slide/danh-sach?page=1&limit=7"/>' class="side-menu-header">Slide</a>
    </li>
    <li id="baiviet">
      <i class="fa-solid fa-newspaper"></i><a 
	  href='<c:url value="/quan-tri/Articles/danh-sach?page=1&limit=7"/>'
	 class="side-menu-header">Bài viết</a>
    </li>
    <li id="comment_articles">
      <i class="fa-solid fa-comment-dollar"></i><a 
	  href='<c:url value="/quan-tri/QuanlicommentArticles_controller/danh-sach?page=1&limit=7"/>'
        class="side-menu-header">Bình luận bài viết</a>
    </li> 
    <li id="phieunhap">
      <i class="fa-solid fa-file-import"></i><a 		href='<c:url value="/quan-tri/phieunhap/danh-sach?page=1&limit=7"/>'
 class="side-menu-header">Phiếu nhập</a>
    </li>
    <hr style="color:bisque">
    <li style="color:aliceblue">Quản lý bán hàng</li>
    <li id="discount">
      <i class="fa-brands fa-salesforce"></i><a 
	  href='<c:url value="/quan-tri/discount/danh-sach?page=1&limit=7"/>'

	 class="side-menu-header">Mã giảm giá</a>
    </li>
     <li id="contact">
      <i class="fa-solid fa-envelope"></i><a 
	  href='<c:url value="/quan-tri/contact/danh-sach?page=1&limit=7"/>'

	  class="side-menu-header">Liên hệ</a>
    </li>
    <li id="order">
      <i class="uil-shopping-cart-alt"></i><a 
	  href='<c:url value="/quan-tri/donhang/danh-sach?page=1&limit=7"/>'

	 class="side-menu-header">Đơn hàng</a>
    </li>
    <li id="customer">
      <i class="fa-solid fa-person-breastfeeding"></i><a 
	  href='<c:url value="/quan-tri/Quanlikhachhang_controller/danh-sach?page=1&limit=7"/>'
	 
        class="side-menu-header">Khách hàng</a>
    </li>
    <li id="comment">
      <i class="fa-solid fa-comments"></i><a 
	  href='<c:url value="/quan-tri/Quanlicomment_controller/danh-sach?page=1&limit=7"/>'
        class="side-menu-header">Bình luận sản phẩm</a>
    </li>
  </ul>
</aside>
