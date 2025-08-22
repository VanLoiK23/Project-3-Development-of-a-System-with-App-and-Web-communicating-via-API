<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Navigation -->
<script>
    function toggleMenu() {
        const menu = document.querySelector('.top-nav-quicklink');
        menu.classList.toggle('show');
    }
</script>

<div class="top-nav group">
    <section>
        <div class="social-top-nav">
            <a class="fa fa-facebook"></a>
            <a class="fa fa-twitter"></a>
            <a class="fa fa-google"></a>
            <a class="fa fa-youtube"></a>
        </div> <!-- End Social Topnav -->

        <!-- Icon mở/đóng menu -->
        <div class="menu-icon" onclick="toggleMenu()">
            <i class="fa fa-bars"></i>
        </div>

        <!-- Menu chính -->
        <ul class="top-nav-quicklink flexContain">
            <li><a href="http://localhost:8080/Spring-mvc/trang-chu"><i class="fa fa-home"></i> Trang chủ</a></li>
            <li><a href="http://localhost:8080/Spring-mvc/Blog"><i class="fa fa-newspaper-o"></i> Tin tức</a></li>
            <li><a href="http://localhost:8080/Spring-mvc/introduce"><i class="fa fa-info-circle"></i> Giới thiệu</a></li>
            <li><a href="http://localhost:8080/Spring-mvc/contact"><i class="fa fa-phone"></i> Liên hệ</a></li>
        </ul> <!-- End Quick link -->
    </section><!-- End Section -->
</div>