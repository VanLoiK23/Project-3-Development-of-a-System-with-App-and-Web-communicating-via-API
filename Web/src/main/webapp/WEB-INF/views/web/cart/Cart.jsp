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
					src="https://i.ibb.co/1JKzwTdk/cc6acb54f0ea39ed943a2e64d73d7cfb.jpg"
					alt="Image Left">
			</div>
			<div class="container">
				<div class="row">
					<div class="col-12 text-center">
						<div class="breadcrumb-content position-relative section-content"
							id="re">
							<h3 class="title-3">Giỏ hàng</h3>
							<ul>
								<li><a href="http://localhost:8080/Spring-mvc/trang-chu">Trang
										chủ</a></li>
								<li id="t">Giỏ hàng</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="image-right">
				<!-- Ảnh bên phải bao phủ toàn bộ chiều cao -->
				<img
					src="https://i.ibb.co/GQtkk8bq/997375d6ac84565a9b189218e6ae101e.jpg"
					alt="Image Right">
			</div>
		</div>

		<div class="cart-main-wrapper mt-no-text" id="card">
			<div class="container custom-area">
				<div class="row" id="row">
					<div class="col-lg-12 col-custom">
						<div class="cart-table table-responsive">
							<table class="table table-bordered" id="cartTable">
								<thead>
									<tr>
										<th class="pro-thumbnail">Hình ảnh</th>
										<th class="pro-title">Sản phẩm</th>
										<th class="pro-price">Giá tiền</th>
										<th class="pro-quantity">Số lượng</th>
										<th class="pro-subtotal">Tổng tiền</th>
										<th class="pro-remove">Hành động</th>
									</tr>
								</thead>
								<c:set var="checkExistProductInCar"
									value="${not empty cartModel.listResult }" />
								<tbody id="cart-items">
									<c:choose>

										<c:when test="${checkExistProductInCar}">
											<c:set var="totalFinal" value="0" />

											<c:forEach var="item" items="${cartModel.listResult }">

												<tr data-maphienbansp="${item.maphienbansp }" data-idkh="1">
													<td class="pro-thumbnail"><a href="${item.img }"><img
															class="img-fluid" src="${item.img }" alt="Product"></a></td>
													<td class="pro-title"><a href="#"> ${item.tensp }
													</a></td>
													<td class="pro-price"><span id="p"
														data-price="${item.priceSale }" style="font-size: 17px">
															<fmt:formatNumber value="${item.priceSale}" type="number"
																pattern="#,##0" /> đ
													</span></td>
													<td class="pro-quantity">
														<div class="quantity">
															<div class="cart-plus-minus" style="margin-left: 40px">
																<button
																	onclick="updateCartItem(1,${item.maphienbansp},'decrease')"
																	style="font-weight: 900; font-size: 20px">-</button>
																<input type="text" id="input" value="${item.soluong }"
																	style="height: 30px; width: 100px; text-align: center; border-width: 3px"
																	readonly min="0" class="quantity-input">
																<button
																	onclick="updateCartItem(1,${item.maphienbansp},'crease')"
																	style="font-weight: 900; font-size: 20px">+</button>
															</div>
														</div>
													</td>
													<c:set var="total" value="${item.priceSale*item.soluong }" />
													<td class="pro-subtotal"><span style="font-size: 17px"
														id="price" data-price="${total}"> <fmt:formatNumber
																value="${total}" type="number" pattern="#,##0" /> đ
													</span></td>
													<c:set var="totalFinal"
														value="${totalFinal+item.priceSale*item.soluong }" />

													<td class="pro-remove"><button
															onclick="removeFromCart(1,${item.maphienbansp})">Xóa</button>
													</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<h1 id="ch">Bạn chưa thêm sản phẩm nào vào giỏ hàng!!</h1>
										</c:otherwise>
									</c:choose>

									<h1 id="emptyCartMessage" style="display: none;">Bạn chưa
										thêm sản phẩm nào vào giỏ hàng!!</h1>

								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="row" id="total">
					<div class="col-lg-5 ml-auto col-custom">
						<!-- Cart Calculation Area -->
						<div class="cart-calculator-wrapper">
							<div class="cart-calculate-items">
								<h3>Tổng giỏ hàng</h3>
								<div class="table-responsive">
									<table class="table">
										<tr>
											<td>Tổng sản phẩm</td>
											<td id="subtotal"><c:if
													test="${checkExistProductInCar }">
											${lenghtCart}
											</c:if></td>
										</tr>
										<tr>
											<c:set var="fee" value="0" />
											<td>Vận chuyển</td>
											<td id="free"><c:if test="${checkExistProductInCar }">
													<c:choose>
														<c:when test="${totalFinal ge 5000000 }">
															<c:set var="fee" value="0" />
													0
													</c:when>
														<c:otherwise>
															<c:set var="fee" value="200000" />

															<fmt:formatNumber value="200000" type="number"
																pattern="#,##0" /> đ
													</c:otherwise>
													</c:choose>
												</c:if></td>
										</tr>
										<tr>
											<c:set var="tax" value="${totalFinal/10 }" />
											<td>Thuế</td>
											<td id="fee"><c:if test="${checkExistProductInCar }">
													<fmt:formatNumber value="${tax}" type="number"
														pattern="#,##0" /> đ
														</c:if></td>
										</tr>
										<tr class="total">
											<td>Tổng cộng</td>
											<td id="total-amount" style="color: red"><c:if
													test="${checkExistProductInCar }">
													<fmt:formatNumber value="${tax+fee+totalFinal}" type="number"
														pattern="#,##0" /> đ
											</c:if></td>
										</tr>
									</table>
								</div>
							</div>

							<a href="http://localhost:8080/Spring-mvc/Cart/checkout"
								id="checkout-btn"
								class="btn flosun-button primary-btn rounded-0 black-btn w-100"
								onclick="return checkCartStatus();"> Tiến hành thanh toán </a>
						</div>
					</div>
				</div>
			</div>
		</div>


	</div>


	<script>

        (function () {
        	  var user = "${user}";
        	if (user==null) {
               
                $('#cart-items tr').remove();

                $('#ch').show();

                $('.table-responsive table tr td').each(function () {
                    $(this).text(''); // Xóa nội dung mỗi ô
                });
            }
        })();


        function removeFromCart(id_kh, maphienbansp, num) {
            var confirmDelete = confirm('Bạn có muốn xóa sản phẩm này khỏi giỏ hàng.');

            if (confirmDelete) {
                $.ajax({
                    url: 'http://localhost:8080/Spring-mvc/Cart?maphienbansp='+maphienbansp,
                    type: 'DELETE',
                    success: function (deleteResponse) {
                        if (deleteResponse.result === true) {
                            alert('Sản phẩm đã được xóa khỏi giỏ hàng.');

                            $('tr[data-maphienbansp="' + maphienbansp + '"]').remove();

                            var $row = $('tr[data-maphienbansp="' + maphienbansp + '"]');
                            var currentValue = parseInt($row.find('#input').val());
                            var newValue = currentValue + num;

                            $row.find('#input').val(newValue);

                            var price = $row.find('#p').data('price');

                            // Tính giá trị mới cho sản phẩm
                            var updatedPrice = parseInt(newValue) * parseInt(price);
                            var formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(updatedPrice);
                            $row.find('#price').text(formattedPrice);

                            // Tính lại tổng số lượng sản phẩm
                            $('#subtotal').text($('#cartTable tbody tr').length);

                            // Tính tổng tiền của tất cả sản phẩm trong giỏ
                            var totalPrice = 0;
                            $('#cartTable tbody tr').each(function () {
                                var quantity = parseInt($(this).find('#input').val());
                                var unitPrice = parseFloat($(this).find('#p').data('price'));
                                totalPrice += unitPrice * quantity;
                            });

                            // Kiểm tra để xác định chi phí giao hàng
                            var freeShipping = totalPrice > 5000000 ? 0 : 200000;
                            $('#free').text(new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(freeShipping));

                            // Cập nhật thuế
                            var fee = totalPrice / 10;
                            $('#fee').text(new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(fee));

                            // Cập nhật tổng tiền cuối cùng
                            var finalTotal = totalPrice + fee + freeShipping;
                            $('#total-amount').text(new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(finalTotal));

                            if ($('#cartTable tbody tr').length === 0) {
                                $('#emptyCartMessage').show(); // Hiển thị thông báo giỏ hàng trống
                                $('#subtotal').html('');
                                $('#free').html('');
                                $('#fee').html('');
                                $('#total-amount').html('');

                            }
                        } else {
                            alert('Không thể xóa sản phẩm khỏi giỏ hàng.');
                        }
                    },
                    error: function (xhr, status, error) {
                        console.log('Lỗi yêu cầu xóa:', error);
                    }
                });
            }
        }

        function updateCartItem(id_kh, maphienbansp, type) {
            var num = 0;
            if (type == 'decrease') {
                num = -1;
            }
            else {
                num = 1;
            }
            console.log(maphienbansp)
            $.ajax({
                url: 'http://localhost:8080/Spring-mvc/Cart',
                type: 'POST',
                data: {
                    idkh: id_kh,
                    maphienbansp: maphienbansp,
                    new_quantity: parseInt(num)
                },
                dataType: 'json',
                success: function (response) {
                    console.log(response.result);
                    if (response.result == "Sản phẩm không đủ số lượng để mua hàng.") {
                        alert(response.result);
                    }
                    else if (response.result == 'Bạn có muốn xóa sản phẩm này khỏi giỏ hàng.') {
                        removeFromCart(id_kh, maphienbansp, num);
                    }
                    else if (response.result == 'Sản phẩm đã ngừng kinh doanh') {
                        alert(response.result);

                        if (response.resultReplace == 'Bạn có muốn xóa sản phẩm này khỏi giỏ hàng.') {
                            removeFromCart(id_kh, maphienbansp, num);
                        }
                    }
                    else {
                        var $row = $('tr[data-maphienbansp="' + maphienbansp + '"]');
                        var currentValue = parseInt($row.find('#input').val());
                        var newValue = currentValue + num;

                        $row.find('#input').val(newValue);

                        var price = $row.find('#p').data('price');

                        // Tính giá trị mới cho sản phẩm
                        var updatedPrice = parseInt(newValue) * parseInt(price);
                        var formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(updatedPrice);
                        $row.find('#price').text(formattedPrice);

                        // Tính lại tổng số lượng sản phẩm
                        $('#subtotal').text($('#cartTable tbody tr').length);

                        // Tính tổng tiền của tất cả sản phẩm trong giỏ
                        var totalPrice = 0;
                        $('#cartTable tbody tr').each(function () {
                            var quantity = parseInt($(this).find('#input').val());
                            var unitPrice = parseFloat($(this).find('#p').data('price'));
                            totalPrice += unitPrice * quantity;
                        });

                        // Kiểm tra để xác định chi phí giao hàng
                        var freeShipping = totalPrice > 5000000 ? 0 : 200000;
                        $('#free').text(new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(freeShipping));

                        // Cập nhật thuế
                        var fee = totalPrice / 10;
                        $('#fee').text(new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(fee));

                        // Cập nhật tổng tiền cuối cùng
                        var finalTotal = totalPrice + fee + freeShipping;
                        $('#total-amount').text(new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(finalTotal));
                    }

                },
                error: function (xhr, status, error) {
                    console.error('Lỗi AJAX:', error);
                    alert('Có lỗi xảy ra khi cập nhật giỏ hàng.');
                }
            });
        }

        // function checkCartStatus() {
        //     if ($('#cartTable tbody tr').length === 0) {
        //         alert('Giỏ hàng trống. Vui lòng thêm sản phẩm để thanh toán.');
        //         return false;
        //     }
        //     // Kiểm tra nếu giỏ hàng trống hoặc không có tổng hợp lệ
        //     <?php if (empty($data['cart_list'])): ?>
            //         alert('Giỏ hàng trống. Vui lòng thêm sản phẩm để thanh toán.');
            //         return false; // Ngừng chuyển hướng
            //     <?php endif; ?>
        //     return true; // Cho phép chuyển hướng
        // }

        function checkCartStatus() {
            if ($('#cartTable tbody tr').length === 0) {
                alert('Giỏ hàng trống. Vui lòng thêm sản phẩm để thanh toán.');
                return false;
            }

            // Lấy danh sách sản phẩm trong giỏ hàng
            const cartItems = [];
            $('#cartTable tbody tr').each(function () {
                const productId = $(this).data('maphienbansp'); // Giả định mỗi dòng có `data-product-id`
                const quantity = $(this).find('.quantity-input').val(); // Input chứa số lượng
                cartItems.push({ productId, quantity });
            });



            // Gửi yêu cầu AJAX đến máy chủ để kiểm tra trạng thái sản phẩm
            $.ajax({
                url: 'http://localhost:8080/Spring-mvc/Cart/checkValid', // Đường dẫn API kiểm tra trạng thái
                method: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                data: JSON.stringify({ cart: cartItems }),
                success: function (response) {
                    console.log(response.success);
                    console.log(response.message);
                    if (response.success) {
                        window.location.href = 'http://localhost:8080/Spring-mvc/Cart/checkout'; // Chuyển hướng trước khi kết thúc hàm
                        return true;

                    } else {
                        alert(response.message);


                        console.log(response.message);
                       /*  for (const key in response.message) {
                            if (response.message.hasOwnProperty(key)) {
                                if (key === "out") {
                                    alert("Lỗi: " + response.message[key]); // Thông báo lỗi
                                } else if (key === "not") {
                                    alert("Cảnh báo: " + response.message[key]); // Thông báo cảnh báo
                                }
                            }
                        } */

                    }
                },
                error: function (xhr, status, error) {
                    // Kiểm tra xem phản hồi có phải là JSON hợp lệ không
                    try {
                        var jsonResponse = JSON.parse(xhr.responseText);
                    } catch (e) {
                        console.error("Lỗi: Phản hồi không phải JSON hợp lệ", xhr.responseText);
                    }
                    console.error("Mã lỗi: " + xhr.status);
                    console.error("Lỗi chi tiết: " + error);
                    console.error("Trạng thái: " + status);
                }

            });

            return false;
        }


    </script>
</body>

</html>