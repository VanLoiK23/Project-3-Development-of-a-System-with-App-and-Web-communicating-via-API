<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<meta name="stylesheet"
	content='
	 <link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/style.css' />" />
	 <link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/vendor/linearicons.min.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/flosun/assets/css/vendor/font.awesome.min.css' />" />
	<link rel="stylesheet"
			href="<c:url value='/template/css/style/style_for_card.css' />" />
				<link rel="stylesheet"
			href="<c:url value='/template/css/style/checkout.css' />" />
	
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link
        href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
        rel="stylesheet">
    
    <link
        href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@700&family=Mulish:ital,wght@0,200..1000;1,200..1000&family=Pacifico&display=swap"
        rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Great+Vibes&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Josefin+Slab:ital,wght@0,100..700;1,100..700&display=swap"
        rel="stylesheet">
    

	    ' />

</head>


<body>
	<div class="main-content">
		<div class="breadcrumbs-area position-relative">
			<div class="image-left">
				<!-- Ảnh bên trái bao phủ toàn bộ chiều cao -->
				<img
					src="https://i.ibb.co/rKQbSHwB/aafae976a38bd341fa0d1d6f57a52d6e.jpg"
					alt="Image Left">
			</div>
			<div class="container">
				<div class="row">
					<div class="col-12 text-center">
						<div class="breadcrumb-content position-relative section-content"
							id="re">
							<h3 class="title-3">Thanh toán</h3>
							<ul>
								<li><a href="http://localhost:8080/Spring-mvc/trang-chu">Trang
										chủ</a></li>
								<li id="t">Thanh toán</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="image-right">
				<!-- Ảnh bên phải bao phủ toàn bộ chiều cao -->
				<img
					src="https://i.ibb.co/j9qgsj2v/c118343fa67de1a1f8058e95fb06ad24.jpg"
					alt="Image Right">
			</div>
		</div>

		<div class="checkout-area mt-no-text">
			<div class="container custom-container">

				<div class="row" id="tr">
					<div class="col-lg-6 col-12 col-custom">
						<form action="#">
							<div class="checkbox-form">
								<h3>Chi tiết thanh toán</h3>
								<div class="row">
									<div class="col-md-12 col-custom">
										<div class="country-select clearfix">
											<input type="hidden" id="idShipping"
												value='<c:if test="${not empty infoShipping.id }">${infoShipping.id }</c:if>'>
											<label>Thành phố <span class="required">*</span>
											</label> <select id="city"
												class="myniceselect nice-select wide rounded-0">

												<c:if test="${not empty infoShipping.city }">
													<option value='${infoShipping.city }' selected>${infoShipping.city }</option>
												</c:if>
												<option value="Hà Nội">Hà Nội</option>
												<option value="Huế">Huế</option>
												<option value="Lai Châu">Lai Châu</option>
												<option value="Điện Biên">Điện Biên</option>
												<option value="Sơn La">Sơn La</option>
												<option value="Lạng Sơn">Lạng Sơn</option>
												<option value="Quảng Ninh">Quảng Ninh</option>
												<option value="Thanh Hoá">Thanh Hoá</option>
												<option value="Nghệ An">Nghệ An</option>
												<option value="Hà Tĩnh">Hà Tĩnh</option>
												<option value="Cao Bằng">Cao Bằng</option>
												<option value="Tuyên Quang">Tuyên Quang</option>
												<!-- hợp với Hà Giang -->
												<option value="Lào Cai">Lào Cai</option>
												<!-- hợp với Yên Bái -->
												<option value="Thái Nguyên">Thái Nguyên</option>
												<!-- hợp với Bắc Kạn -->
												<option value="Phú Thọ">Phú Thọ</option>
												<!-- hợp với Vĩnh Phúc, Hòa Bình -->
												<option value="Bắc Ninh">Bắc Ninh</option>
												<!-- hợp với Bắc Giang -->
												<option value="Hưng Yên">Hưng Yên</option>
												<!-- hợp với Thái Bình -->
												<option value="Hải Phòng">Hải Phòng</option>
												<!-- hợp với Hải Dương -->
												<option value="Ninh Bình">Ninh Bình</option>
												<!-- hợp với Hà Nam, Nam Định -->
												<option value="Quảng Trị">Quảng Trị</option>
												<!-- hợp với Quảng Bình -->
												<option value="Đà Nẵng">Đà Nẵng</option>
												<!-- hợp với Quảng Nam -->
												<option value="Quảng Ngãi">Quảng Ngãi</option>
												<!-- hợp với Kon Tum -->
												<option value="Gia Lai">Gia Lai</option>
												<!-- hợp với Bình Định -->
												<option value="Khánh Hòa">Khánh Hòa</option>
												<!-- hợp với Ninh Thuận -->
												<option value="Lâm Đồng">Lâm Đồng</option>
												<!-- hợp với Đắk Nông, Bình Thuận -->
												<option value="Đắk Lắk">Đắk Lắk</option>
												<!-- hợp với Phú Yên -->
												<option value="Hồ Chí Minh">Hồ Chí Minh</option>
												<!-- hợp với Bình Dương, Vũng Tàu -->
												<option value="Đồng Nai">Đồng Nai</option>
												<!-- hợp với Bình Phước -->
												<option value="Tây Ninh">Tây Ninh</option>
												<!-- hợp với Long An -->
												<option value="Cần Thơ">Cần Thơ</option>
												<!-- hợp với Sóc Trăng, Hậu Giang -->
												<option value="Vĩnh Long">Vĩnh Long</option>
												<!-- hợp với Bến Tre, Trà Vinh -->
												<option value="Đồng Tháp">Đồng Tháp</option>
												<!-- hợp với Tiền Giang -->
												<option value="Cà Mau">Cà Mau</option>
												<!-- hợp với Bạc Liêu -->
												<option value="An Giang">An Giang</option>
												<!-- hợp với Kiên Giang -->
											</select>


										</div>
									</div>


									<div class="col-md-6 col-custom">
										<div class="checkout-form-list">
											<label>Tên người nhận<span class="required">*</span></label>
											<input placeholder="" id="firstName" name="firstname"
												value="${infoShipping.firstName}" required type="text">
										</div>
									</div>
									<div class="col-md-6 col-custom">
										<div class="checkout-form-list">
											<label>Họ <span class="required">*</span></label> <input
												placeholder="" id="lastName" name="lastname"
												value="${infoShipping.lastName}" required type="text">
										</div>
									</div>
									<div class="col-md-12 col-custom">
										<div class="checkout-form-list">
											<label>Xã, Phường<span class="required">*</span></label> <input
												placeholder="Bao gồm quận, huyện" name="district"
												value="${infoShipping.district }" id="district" required
												type="text">
										</div>
									</div>
									<div class="col-md-12 col-custom">
										<div class="checkout-form-list">
											<label>Địa chỉ <span class="required">*</span></label> <input
												placeholder="Bao gồm số và tên đường"
												value="${infoShipping.streetName }" name="street"
												id="street" required type="text">
										</div>
									</div>
									<div class="col-md-6 col-custom">
										<div class="checkout-form-list">
											<label>Email<span class="required">*</span></label> <input
												required placeholder="" value="${infoShipping.email }"
												id="email" type="email">
										</div>
									</div>
									<div class="col-md-6 col-custom">
										<div class="checkout-form-list">
											<label>Số điện thoại nhận hàng<span class="required">*</span></label>
											<input type="text" id="phone"
												value="${infoShipping.soDienThoai }" required name="phone">
										</div>
									</div>
									<div class="order-notes mt-3">
										<div class="checkout-form-list checkout-form-list-2">
											<label id="note">Ghi chú đơn hàng</label>
											<textarea id="checkout-mess" cols="30" rows="10" name="note"
												placeholder="Những lưu ý cho đơn hàng của bạn">${infoShipping.note }</textarea>
										</div>
									</div>
								</div>

							</div>
						</form>
					</div>
					<div class="col-lg-6 col-12 col-custom" id="check">
						<div class="your-order">
							<h3>Đơn hàng của bạn</h3>
							<div class="your-order-table table-responsive">
								<table class="table">
									<tfoot>
										<tr class="cart-subtotal">
											<th>Phí ship</th>
											<td class="text-center"><span class="amount" id="ship">
													<fmt:formatNumber value="${feeTransport}" type="number"
														pattern="#,##0" /> đ
											</span></td>
										</tr>
										<tr class="order-total">
											<th>Tổng tiền thanh toán</th>
											<td class="text-center" id="amount"><strong><span
													class="amount" id="total" style="color: red"> <fmt:formatNumber
															value="${totalFinal}" type="number" pattern="#,##0" /> đ
												</span></strong> <span style="font-size: 13px">(đã tính thuế 10%)</span></td>
										</tr>
									</tfoot>
								</table>
							</div>
							<div class="discount-container">
								<label for="discount" class="discount-label">Chọn Mã
									Giảm Giá</label> <select id="discount" class="discount-select">
									<option value="" disabled selected hidden>Chọn mã giảm
										giá...</option>

									<c:choose>
										<c:when test="${not empty discounts.listResult}">
											<c:forEach var="item" items="${discounts.listResult}">
												<option value="${item.id}"
													${idDiscount == item.id ? 'selected' : ''}>Giảm
													<fmt:formatNumber value="${item.discountAmount}"
														type="number" pattern="#,##0" /> đ - ${item.code }
												</option>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<option value="" disabled selected hidden>Chưa
												có mã giảm giá nào</option>
										</c:otherwise>
									</c:choose>


								</select>
							</div>
							<div class="payment-method">
								<div class="payment-accordion">
									<div id="accordion">
										<div class="card">
											<h4>Chọn hình thức thanh toán</h4>
											<form id="paymentForm" method="POST" target="_blank"
												enctype="application/x-www-form-urlencoded">

												<input type="hidden" name="total" id="totalFinal"
													value="${totalFinal}"> <input type="hidden"
													name="feeTransport" value="${feeTransport}"> <input
													type="hidden" name="tax" value="${tax}">

												<!-- 	<div class="form-group">
													<button type="button" onclick="submitPayment('momo')"
														class="btn flosun-button secondary-btn black-color rounded-0 w-100">
														Thanh toán bằng Momo <i class="fa-solid fa-qrcode"
															style="color: white"></i>
													</button>
												</div> -->
												<div class="form-group mt-3">
													<button type="button" onclick="submitPayment('atm')"
														class="btn flosun-button secondary-btn black-color rounded-0 w-100">
														Thanh toán bằng thẻ ngân hàng <i style="color: white"
															class="fa-solid fa-money-check-dollar"></i>
													</button>
												</div>

												<div class="form-group mt-3">
													<button type="button" onclick="submitPayment('visa')"
														class="btn flosun-button secondary-btn black-color rounded-0 w-100">
														Thanh toán bằng thẻ VISA <i style="color: white"
															class="fa-brands fa-cc-visa"></i>
													</button>
												</div>
											</form>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>


	<script>
		(function() {

			var user = "${user}";
			if (user == null) {

				window.location.href = "http://localhost:8080/Spring-mvc/trang-chu";

			}
		})();

		function addHiddenInput(form, name, value) {
			var input = document.createElement("input");
			input.type = "hidden";
			input.name = name;
			input.value = value;
			form.appendChild(input);
		}

		function submitPayment(method) {
			// Lấy giá trị từ các trường
			var city = document.getElementById('city').value;
			var firstName = document.getElementById('firstName').value;
			var lastName = document.getElementById('lastName').value;
			var district = document.getElementById('district').value;
			var street = document.getElementById('street').value;
			var email = document.getElementById('email').value;
			var phone = document.getElementById('phone').value;
			var note = document.getElementById('checkout-mess').value;
			var total = document.getElementById('totalFinal').value;
			var discount = ((document.getElementById('discount').value) ? document
					.getElementById('discount').value
					: null);

			var ship = ((document.getElementById('idShipping').value) ? document
					.getElementById('idShipping').value
					: null);
			var urlReturnByMomo = null;

			// Tạo một form ẩn để gửi dữ liệu
			var form = document.getElementById('paymentForm');
			/*   if (method === 'momo') {
			      form.action = "http://localhost:8080/Spring-mvc/Cart/handlePay?type=momo";
			  } else  */
			/* if (method === 'atm') {
			form.action = "http://localhost:8080/Spring-mvc/Cart/handlePay?type=atm";
			} else {
			form.action = "http://localhost:8080/Spring-mvc/Cart/handlePay?type=visa";
			}  */
			form.action = "http://localhost:8080/Spring-mvc/Cart/handlePay";

			$.ajax({
				url : 'http://localhost:8080/Spring-mvc/quan-tri/don-hang/'
						+ method + '/' + total,
				type : 'POST',
				success : function(response) {
					urlReturnByMomo = response.payUrl;

					// Thêm các trường ẩn với dữ liệu thu thập được giống cú pháp jstl nên rỗng 
					/*  form.innerHTML += `<input type="hidden" name="city" value="${city}">
					                <input type="hidden" name="firstName" value="${firstName}">
					                <input type="hidden" name="lastName" value="${lastName}">
					                <input type="hidden" name="district" value="${district}">
					                <input type="hidden" name="street" value="${street}">
					                <input type="hidden" name="email" value="${email}">
					                <input type="hidden" name="phone" value="${phone}">
					                <input type="hidden" name="note" value="${note}">
					                <input type="hidden" name="ship" value="${ship}">
					                <input type="hidden" name="select" value="${discount}">
					                <input type="hidden" name="urlReturnByMomo" value="${response.payUrl}">
					                `; */

					// Thêm input ẩn bằng cách an toàn
					addHiddenInput(form, "city", city);
					addHiddenInput(form, "firstName", firstName);
					addHiddenInput(form, "lastName", lastName);
					addHiddenInput(form, "district", district);
					addHiddenInput(form, "street", street);
					addHiddenInput(form, "email", email);
					addHiddenInput(form, "phone", phone);
					addHiddenInput(form, "note", note);
					addHiddenInput(form, "ship", ship);
					addHiddenInput(form, "select", discount);
					addHiddenInput(form, "urlReturnByMomo", urlReturnByMomo);

					// Gửi form
					console.log("Redirecting to:", urlReturnByMomo);

					// Gửi form
					console.log("${response.payUrl}");
					form.submit();
				}
			});

		}

		$(document).ready(function() {
			$('#discount').on('change', function() {
				var discountId = $(this).val();

				    sessionStorage.setItem("city", $('#city').val());
			        sessionStorage.setItem("firstName", $('#firstName').val());
			        sessionStorage.setItem("lastName", $('#lastName').val());
			        sessionStorage.setItem("district", $('#district').val());
			        sessionStorage.setItem("street", $('#street').val());
			        sessionStorage.setItem("email", $('#email').val());
			        sessionStorage.setItem("phone", $('#phone').val());
			        sessionStorage.setItem("note", $('#checkout-mess').val());

				$.ajax({
		            url: 'http://localhost:8080/Spring-mvc/Cart/save-discount', // endpoint lưu session
		            type: 'POST',
		            data: { discountId: discountId },
		            success: function () {
		                // Sau khi lưu xong, reload lại trang
		                window.location.href = 'http://localhost:8080/Spring-mvc/Cart/checkout';
		            }
		        });

			

				/* const url='http://localhost:8080/Spring-mvc/Cart/checkout?discount='+discountId;

				window.location.href=url; */

				// Gửi AJAX đến PHP để cập nhật total
				/* $.ajax({
					url : 'http://localhost:8080/Spring-mvc/Cart/checkout',
					type : 'GET',
					data : {
						discount : discountAmount
					},
					success : function(response) {
						/* $('#total').text(response); // Cập nhật total hiển thị 
					}
				}); */
			});

			if (sessionStorage.getItem("city")) {
		        $('#city').val(sessionStorage.getItem("city"));
		        $('#firstName').val(sessionStorage.getItem("firstName"));
		        $('#lastName').val(sessionStorage.getItem("lastName"));
		        $('#district').val(sessionStorage.getItem("district"));
		        $('#street').val(sessionStorage.getItem("street"));
		        $('#email').val(sessionStorage.getItem("email"));
		        $('#phone').val(sessionStorage.getItem("phone"));
		        $('#checkout-mess').val(sessionStorage.getItem("note"));

		        // Sau khi khôi phục xong thì xoá luôn để tránh lưu mãi
		        sessionStorage.clear();
		    }
		});
	</script>
</body>

</html>