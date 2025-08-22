<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>
<c:url var="APIurl" value="/api-admin-new" />
<c:url var="NewURL" value="/admin-new" />
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

</head>
<body>

	<div class="main-content">

		<c:if test="${not empty message}">
			<script>
				alert('${message}');
			</script>
		</c:if>
		<c:if test="${not empty sessionScope.FLASH_MESSAGE}">
			<script>
				alert('${sessionScope.FLASH_MESSAGE}');
			</script>
			<c:remove var="FLASH_MESSAGE" scope="session" />
		</c:if>
		<section id="wrapper">

		<div class="p-4">
			<div class="welcome">
				<div class="content rounded-3 p-3">
					<h1 class="fs-3">Chào mừng đến với trang chủ</h1>
					<p class="mb-0">Hello Gia Huy!</p>
				</div>
			</div>

			<!-- <section class="statistics mt-4">
        <div class="row">
          <div class="col-lg-4">
            <div class="box d-flex rounded-2 align-items-center mb-4 mb-lg-0 p-3">
              <i class="uil-envelope-shield fs-2 text-center bg-primary rounded-circle"></i>
              <div class="ms-3">
                <div class="d-flex align-items-center">
                  <h3 class="mb-0">1,245</h3> <span class="d-block ms-2">Emails</span>
                </div>
                <p class="fs-normal mb-0">Lorem ipsum dolor sit amet</p>
              </div>
            </div>
          </div>
          <div class="col-lg-4">
            <div class="box d-flex rounded-2 align-items-center mb-4 mb-lg-0 p-3">
              <i class="uil-file fs-2 text-center bg-danger rounded-circle"></i>
              <div class="ms-3">
                <div class="d-flex align-items-center">
                  <h3 class="mb-0">34</h3> <span class="d-block ms-2">Projects</span>
                </div>
                <p class="fs-normal mb-0">Lorem ipsum dolor sit amet</p>
              </div>
            </div>
          </div>
          <div class="col-lg-4">
            <div class="box d-flex rounded-2 align-items-center p-3">
              <i class="uil-users-alt fs-2 text-center bg-success rounded-circle"></i>
              <div class="ms-3">
                <div class="d-flex align-items-center">
                  <h3 class="mb-0">5,245</h3> <span class="d-block ms-2">Users</span>
                </div>
                <p class="fs-normal mb-0">Lorem ipsum dolor sit amet</p>
              </div>
            </div>
          </div>
        </div>
      </section> -->

			<section class="statis mt-4 text-center">
			<div class="row">
				<div class="col-md-6 col-lg-3 mb-4 mb-lg-0">
					<div class="box bg-primary p-3">
						<i class="fa-solid fa-warehouse"></i>
						<h3>${dto.quantityWareHouse}</h3>
						<p class="lead">Kho hoạt động</p>
					</div>
				</div>
				<div class="col-md-6 col-lg-3 mb-4 mb-lg-0">
					<div class="box bg-danger p-3">
						<i class="uil-user"></i>
						<h3>${dto.quantityUser}</h3>
						<p class="lead">Khách hàng đăng ký</p>
					</div>
				</div>
				<div class="col-md-6 col-lg-3 mb-4 mb-md-0">
					<div class="box bg-warning p-3">
						<i class="uil-shopping-cart"></i>
						<h3>${dto.quantityProduct}</h3>
						<p class="lead">Sản phẩm</p>
					</div>
				</div>
				<div class="col-md-6 col-lg-3">
					<div class="box bg-success p-3">
						<i class="uil-feedback"></i>
						<h3>${dto.quantityPurchase}</h3>
						<p class="lead">Số lượng thanh toán</p>
					</div>
				</div>
			</div>
			</section>

			<section class="charts mt-4">
			<div class="row">
				<div class="col-lg-6">
					<div class="chart-container rounded-2 p-3">
						<h3 class="fs-6 mb-3">Biểu đồ thống kê số đơn hàng theo tuần</h3>
						<canvas id="myChart11"></canvas>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="chart-container rounded-2 p-3">
						<h3 class="fs-6 mb-3">Biểu đồ thống kê doanh thu bán hàng
							theo tuần</h3>
						<canvas id="myChart12"></canvas>
					</div>
				</div>
			</div>
			</section>

			<section class="charts mt-4">
			<div class="row">
				<div class="col-lg-6">
					<div class="chart-container rounded-2 p-3">
						<h3 class="fs-6 mb-3">Biểu đồ thống kê số đơn hàng theo tháng</h3>
						<canvas id="myChart13"></canvas>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="chart-container rounded-2 p-3">
						<h3 class="fs-6 mb-3">Biểu đồ thống kê doanh thu bán hàng
							theo tháng</h3>
						<canvas id="myChart14"></canvas>
					</div>
				</div>
			</div>
			</section>


			<section class="charts mt-4">
			<div class="row">
				<div class="col-lg-6">
					<div class="chart-container rounded-2 p-3">
						<h3 class="fs-6 mb-3">Biểu đồ thống kê nhập, xuất hàng theo
							năm</h3>
						<canvas id="myChart"></canvas>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="chart-container rounded-2 p-3">
						<h3 class="fs-6 mb-3">Biểu đồ thống kê nhập, xuất hàng theo
							quý</h3>
						<canvas id="myChart2"></canvas>
					</div>
				</div>
			</div>
			</section>

			<!-- <section class="admins mt-4">
        <div class="row">
          <div class="col-md-6">
            <div class="box">
              <div class="admin d-flex align-items-center rounded-2 p-3 mb-4">
                <div class="img">
                  <img class="img-fluid rounded-pill" width="75" height="75"
                    src="https://uniim1.shutterfly.com/ng/services/mediarender/THISLIFE/021036514417/media/23148906966/small/1501685402/enhance"
                    alt="admin">
                </div>
                <div class="ms-3">
                  <h3 class="fs-5 mb-1">Joge Lucky</h3>
                  <p class="mb-0">Lorem ipsum dolor sit amet consectetur elit.</p>
                </div>
              </div>
              <div class="admin d-flex align-items-center rounded-2 p-3 mb-4">
                <div class="img">
                  <img class="img-fluid rounded-pill" width="75" height="75"
                    src="https://uniim1.shutterfly.com/ng/services/mediarender/THISLIFE/021036514417/media/23148907137/small/1501685404/enhance"
                    alt="admin">
                </div>
                <div class="ms-3">
                  <h3 class="fs-5 mb-1">Joge Lucky</h3>
                  <p class="mb-0">Lorem ipsum dolor sit amet consectetur elit.</p>
                </div>
              </div>
              <div class="admin d-flex align-items-center rounded-2 p-3">
                <div class="img">
                  <img class="img-fluid rounded-pill" width="75" height="75"
                    src="https://uniim1.shutterfly.com/ng/services/mediarender/THISLIFE/021036514417/media/23148907019/small/1501685403/enhance"
                    alt="admin">
                </div>
                <div class="ms-3">
                  <h3 class="fs-5 mb-1">Joge Lucky</h3>
                  <p class="mb-0">Lorem ipsum dolor sit amet consectetur elit.</p>
                </div>
              </div>
            </div>
          </div>
          <div class="col-md-6">
            <div class="box">
              <div class="admin d-flex align-items-center rounded-2 p-3 mb-4">
                <div class="img">
                  <img class="img-fluid rounded-pill" width="75" height="75"
                    src="https://uniim1.shutterfly.com/ng/services/mediarender/THISLIFE/021036514417/media/23148907114/small/1501685404/enhance"
                    alt="admin">
                </div>
                <div class="ms-3">
                  <h3 class="fs-5 mb-1">Joge Lucky</h3>
                  <p class="mb-0">Lorem ipsum dolor sit amet consectetur elit.</p>
                </div>
              </div>
              <div class="admin d-flex align-items-center rounded-2 p-3 mb-4">
                <div class="img">
                  <img class="img-fluid rounded-pill" width="75" height="75"
                    src="https://uniim1.shutterfly.com/ng/services/mediarender/THISLIFE/021036514417/media/23148907086/small/1501685404/enhance"
                    alt="admin">
                </div>
                <div class="ms-3">
                  <h3 class="fs-5 mb-1">Joge Lucky</h3>
                  <p class="mb-0">Lorem ipsum dolor sit amet consectetur elit.</p>
                </div>
              </div>
              <div class="admin d-flex align-items-center rounded-2 p-3">
                <div class="img">
                  <img class="img-fluid rounded-pill" width="75" height="75"
                    src="https://uniim1.shutterfly.com/ng/services/mediarender/THISLIFE/021036514417/media/23148907008/medium/1501685726/enhance"
                    alt="admin">
                </div>
                <div class="ms-3">
                  <h3 class="fs-5 mb-1">Joge Lucky</h3>
                  <p class="mb-0">Lorem ipsum dolor sit amet consectetur elit.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section> -->



			<section class="charts mt-4">
			<div class="chart-container p-3">
				<h3 class="fs-6 mb-3">Biểu đồ thống kê sản phẩm được ưa chuộng</h3>
				<div style="height: 300px">
					<canvas id="myPieChart" width="100%"></canvas>
				</div>
			</div>
			</section>
		</div>
		</section>
	</div>


</body>

</html>