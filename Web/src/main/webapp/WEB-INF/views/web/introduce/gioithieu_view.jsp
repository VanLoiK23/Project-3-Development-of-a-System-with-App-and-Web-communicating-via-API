<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

    <title>Smartphone Store - About Us</title>
	
	<meta name="stylesheet"
		content='
		 <link rel="stylesheet"
				href="<c:url value='/template/css/style/gioithieu.css' />" />

		    ' />
</head>

<body>

	<div class="main-content">
	    <!-- Hero Image -->
    <section class="hero">
        <div class="container">
            <img src="http://localhost:8080/Spring-mvc/template/images/gioithieujpg.jpg" alt="Smartphone Store" class="hero-img">
        </div>
    </section>

    <!-- About Section -->
    <section class="about1">
        <div class="container">
			<h2>Giới Thiệu</h2>
			<p>
			    <strong>FPT Shop</strong> là hệ thống bán lẻ hàng đầu tại Việt Nam chuyên cung cấp các sản phẩm công nghệ chính hãng, bao gồm điện thoại, laptop, máy tính bảng, phụ kiện và nhiều thiết bị thông minh khác từ các thương hiệu uy tín như Apple, Samsung, OPPO, Xiaomi, ASUS, HP, Lenovo...
			</p>
			<p>
			    Với phương châm <em>"Tận tâm phục vụ khách hàng"</em>, FPT Shop không ngừng nâng cao chất lượng dịch vụ, mang đến trải nghiệm mua sắm tiện lợi, an toàn và nhanh chóng. Tất cả sản phẩm đều có nguồn gốc rõ ràng, bảo hành chính hãng cùng chính sách đổi trả minh bạch.
			</p>
			<p>
			    Hệ thống FPT Shop có mặt tại hầu hết các tỉnh thành trên toàn quốc, kết hợp giữa bán hàng trực tiếp và trực tuyến nhằm đáp ứng tối đa nhu cầu của khách hàng trong thời đại số.
			</p>
			<p>
			    FPT Shop cam kết mang lại sự hài lòng cao nhất cho khách hàng, không chỉ ở chất lượng sản phẩm mà còn ở sự chuyên nghiệp và tận tâm trong từng dịch vụ.
			</p>
        </div>
    </section>

    <section class="why-choose-us">
        <div class="container">
            <h2>Tại Sao Chọn Chúng Tôi?</h2>
            <div class="reasons-grid">
                <div class="reason-item">
                    <div class="icon">
                        <i class="fas fa-box-open"></i>
                    </div>
                    <h3>Sản phẩm chính hãng</h3>
                    <p>Cam kết mang đến các sản phẩm 100% chính hãng với đầy đủ giấy tờ và bảo hành.</p>
                </div>
                <div class="reason-item">
                    <div class="icon">
                        <i class="fas fa-tags"></i>
                    </div>
                    <h3>Giá cả cạnh tranh</h3>
                    <p>Luôn đảm bảo mức giá tốt nhất trên thị trường cùng nhiều ưu đãi hấp dẫn.</p>
                </div>
                <div class="reason-item">
                    <div class="icon">
                        <i class="fas fa-headset"></i>
                    </div>
                    <h3>Dịch vụ tận tâm</h3>
                    <p>Đội ngũ nhân viên chuyên nghiệp, hỗ trợ tư vấn và giải đáp mọi thắc mắc của bạn.</p>
                </div>

            </div>
        </div>
    </section>


    <!-- Mission Section -->
    <section class="mission">
        <div class="container">
            <h2>Sứ Mệnh & Tầm Nhìn</h2>
            <p>
                Chúng tôi không chỉ bán điện thoại mà còn mang đến những giá trị công nghệ, giúp khách hàng tiếp cận
                những sản phẩm tiên tiến nhất một cách dễ dàng.
                FPT shop luôn nỗ lực để trở thành thương hiệu công nghệ hàng đầu tại Việt Nam, đáp ứng mọi nhu
                cầu của người tiêu dùng.
            </p>
            <blockquote>
                "Công nghệ là chìa khóa mở ra tương lai. FPT shop đồng hành cùng bạn trên hành trình ấy."
            </blockquote>
        </div>
    </section>

    <!-- Team Section -->
    <section class="team">
        <div class="container">
            <h2>Đội Ngũ Của Chúng Tôi</h2>
            <div class="team-grid">
                <div class="team-member">
                    <img src="https://i.ibb.co/1GB3wTsX/images.jpg"
                        style="width:300px;aspect-ratio:4/3" alt="Huỳnh Văn Lợi">
                    <h3>Nguyễn Văn Khoa</h3>
                    <p>Giám đốc điều hành</p>
                </div>
                <div class="team-member">
                    <img src="https://i.ibb.co/0yTrKzQw/mau-ao-polo-dong-phuc-fpt-telecom.jpg"
                        style="width:300px;aspect-ratio:4/3" alt="Phạm Thị Khánh Đoan">
                    <h3>Phạm Thị Khánh Đoan</h3>
                    <p>Quản lý kinh doanh</p>
                </div>
                <div class="team-member">
                    <img src="https://i.ibb.co/HLnFsj4z/anh-1-1.jpg"
                        style="width:300px;aspect-ratio:4/3" alt="Lê Văn Gia Huy">
                    <h3>Lê Văn Gia Huy</h3>
                    <p>Kỹ thuật viên</p>
                </div>
            </div>
        </div>
    </section>
	</div>


</body>

</html>