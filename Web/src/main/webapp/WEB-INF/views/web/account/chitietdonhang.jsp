<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>Chi tiết đơn hàng</title>
<meta name="stylesheet"
	content='
	 <link rel="stylesheet"
			href="<c:url value='/template/css/style/order_details.css' />" />
	    ' />

</head>

<body>

	<h2 class="header" id="header">Chi tiết đơn hàng</h2>
	
	<div class="frame-19522" id="frame-19522">
		<div class="frame-195211">
			<div class="frame-19520">
				<div class="frame-19519">
					<div class="frame-19518">
						<p class="nhng">Đơn hàng:</p>
						<p class="_-22010124125521352">#84-${user.id}${order.id}</p>
					</div>
					<div class="frame-19551">
						<p class="content-left">
							<fmt:formatDate value="${order.thoiGian}" pattern="yyyy-MM-dd HH:mm:ss" />
						</p>

					</div>
				</div>
				
				<c:set var="status" value="${order.status}"/>

				<c:choose>
					<c:when test="${status eq 0}">
						<c:set var="statusString" value="Đang chờ duyệt" />
						<c:set var="statusStyleCss"
							value="background-color: #f0ad4e; color: #fff;" />

					</c:when>
					<c:when test="${status eq 1}">
						<c:set var="statusString" value="Đang lấy hàng" />
						<c:set var="statusStyleCss"
							value="background-color: #5bc0de; color: #fff;" />
					</c:when>
					<c:when test="${status eq 2}">
						<c:set var="statusString" value="Đang chờ giao hàng" />
						<c:set var="statusStyleCss"
							value="background-color: #5cb85c; color: #fff;" />
					</c:when>
					<c:when test="${status eq 3}">
						<c:set var="statusString" value="Đang giao hàng" />
						<c:set var="statusStyleCss"
							value="background-color: #0275d8; color: #fff;" />
					</c:when>
					<c:when test="${status eq 4}">
						<c:set var="statusString" value="Đã giao" />
						<c:set var="statusStyleCss"
							value="background-color: rgba(48, 205, 96, 1); color: #fff;" />
					</c:when>
					<c:when test="${status eq -1}">
						<c:set var="statusString" value="Nhân viên đã hủy" />
						<c:set var="statusStyleCss"
							value="background-color: #d9534f; color: #fff;" />
					</c:when>
					<c:when test="${status eq -2}">
						<c:set var="statusString" value="Đang chờ nhân viên duyệt đơn hủy" />
						<c:set var="statusStyleCss"
							value="background-color: #6c757d; color: #fff;" />
					</c:when>
					<c:otherwise>
						<c:set var="statusString" value="Bạn đã hủy" />
						<c:set var="statusStyleCss"
							value="background-color: #6c757d; color: #fff;" />
					</c:otherwise>
				</c:choose>

				<div class="product-status" style="${statusStyleCss}">
					<p class=" tag-content">${statusString}</p>
				</div>
			</div>
			<div class="frame-19548">
				<div class="frame-19545 clip-contents">
					<div class="frame-852">
						<p class="kh-ch-hng">KHÁCH HÀNG</p>
					</div>
					<div class="frame-849">
						<div class="frame-19498">
							<p class="nguy-nminh-ho-ng-ph-ng">${user.name }</p>
						</div>
						<p class="_-0985123245">${user.phone}</p>
					</div>
				</div>
				<div class="frame-19546 clip-contents">
					<div class="frame-8521">
						<p class="ng-inh-n">NGƯỜI NHẬN</p>
					</div>
					<div class="frame-8491">
						<div class="frame-194981">
							<p class="nguy-nminh-ho-ng-ph-ng1">${shipping.hoVaTen }</p>
						</div>
						<p class="_-09851232451">${shipping.soDienThoai }</p>
						<p class="tng-12-tapeakview-s-36-ho">${shipping.streetName }-
							${shipping.district } - ${shipping.city } - ${shipping.country }
						</p>
					</div>
				</div>
			</div>
			<div class="frame-19506 clip-contents">
				<div class="frame-19521">
					<div class="table-content">
						<p class="th-ng-24-chai-tr-long-vcha" style="display: none">
							Nước Mắm Sá Sùng Vân Đồn chai 700...</p>
					</div>

				</div>
				<div class="frame-19504">
					<div class="header-text">
						<p class="title-left">Tên sản phẩm</p>
					</div>
					<div class="header-text-1">
						<p class="title-left-1">Số lượng</p>
					</div>
					<div class="header-text-2">
						<p class="title-left-2">Đơn giá</p>
					</div>
					<div class="header-text-3">
						<p class="title-left-3">Tổng tiền</p>
					</div>
				</div>

				<c:if test="${not empty order.listProductDTOsForDisplay }">
					<c:forEach var="item" items="${order.listProductDTOsForDisplay}"
						varStatus="i">
						<div class="frame-19516">
							<div class="table-content-1">
								<p class="th-ng-24-chai-tr-long-vcha-1">

									${item.tenSanPham}
									<c:if test="${not empty  item.kichThuocRom}">
								 - ${item.kichThuocRam } GB - ${item.kichThuocRom } GB
								</c:if>
									${item.color }

								</p>
							</div>
							<c:set var="quantityNumber" value="0" />
							<div class="content-text-1-line-3">
								<div class="frame-593">
									<p class="content-left-4">
										<c:forEach var="ctpx" items="${order.listctpx}"
											varStatus="status">
											<c:if test="${status.index == i.index}">
												<!-- Hiển thị thuộc tính -->
												${ctpx.soLuong}
												<c:set var="quantityNumber" value="${ctpx.soLuong}" />

											</c:if>
										</c:forEach>
									</p>
								</div>
							</div>
							<div class="content-text-1-line-4">
								<div class="frame-594">

									<c:if test="${item.giaXuat ne item.priceSale }">
										<p class="sub-content" style="white-space: nowrap">

											<fmt:formatNumber value="${item.giaXuat}" type="number"
												pattern="#,##0" />
											đ
										</p>

									</c:if>

									<p class="content-left-5" style="white-space: nowrap">
										<fmt:formatNumber value="${item.priceSale}" type="number"
											pattern="#,##0" />
										đ
									</p>
								</div>
							</div>
							<div class="content-text-1-line-5">
								<div class="frame-595">
									<p class="content-left-6">

										<fmt:formatNumber value="${item.priceSale*quantityNumber}"
											type="number" pattern="#,##0" />
										đ
									</p>
								</div>
							</div>
						</div>
					</c:forEach>
				</c:if>


			</div>
		</div>
		<div class="frame-19538">
			<div class="frame-19549 clip-contents">
				<div class="frame-8522">
					<p class="ph-ng-th-cthanh-to-n">PHƯƠNG THỨC THANH TOÁN</p>
				</div>
				<div class="frame-660">
					<div class="frame-19542">
						<!-- <div class="frame-837">
                  <p class="ti-nmt">Tiền mặt</p>
                  <p class="_-10000000">10.000.000 đ</p>
                </div> -->
						<div class="frame-838">
							<c:choose>
								<c:when test="${not empty order.payment }">
									<p class="vnpayqr">${order.payment }</p>
								</c:when>
								<c:otherwise>
									<p class="ti-nmt">Tiền mặt</p>
								</c:otherwise>
							</c:choose>

							<p class="_-6000000">
								<fmt:formatNumber value="${order.tongTien}" type="number"
									pattern="#,##0" />
								đ
							</p>
						</div>
					</div>
				</div>
			</div>
			<div class="block-summary clip-contents">
				<div class="frame-792">
					<div class="frame-6601">
						<p class="tmtnh">Tạm tính</p>
						<c:set var="total" value="${order.tongTien}" />
						<c:set var="discount" value="0" />
						<c:set var="transport" value="0" />
						<c:set var="discountRaw" value="Không" />

						<c:if test="${not empty order.discountCode }">
							<c:set var="discount" value="${-order.amountDiscount}" />
							<c:set var="discountRaw" value="${order.discountCodeRaw}" />
							<c:set var="total" value="${order.tongTien+order.amountDiscount}" />
						</c:if>
						<c:if test="${order.feeTransport eq 1 }">
							<c:set var="transport" value="200000" />
							<c:set var="total" value="${order.tongTien-transport}" />
						</c:if>
						<p class="_-15500000">
							<fmt:formatNumber value="${total}" type="number" pattern="#,##0" />
							đ
						</p>
					</div>
					<!-- <div class="frame-661">
            <p class="khuy-nmi">Khuyến mãi</p>
            <p class="_-30000">-30.000 đ</p>
          </div> -->
					<div class="frame-662">

						<p class="ph-vnchuy-n">Phí vận chuyển</p>

						<c:if test="${transport eq 200000 }">
							<p class="mi-nph">
								<fmt:formatNumber value="${transport}" type="number"
									pattern="#,##0" />
								đ
							</p>
						</c:if>
						<c:if test="${transport ne 200000 }">
							<p class="mi-nph">Miễn phí</p>
						</c:if>

					</div>
					<div class="frame-778">
						<div class="frame-777" style="white-space: nowrap">
							<p class="mgi-mgi">Mã giảm giá</p>
							<div class="product-status-1">
								<p class="tag-content-1">${discountRaw }</p>
							</div>
						</div>
						<p class="_-10000" style="white-space: nowrap">
							<fmt:formatNumber value="${discount}" type="number"
								pattern="#,##0" />
							đ
						</p>
					</div>
					<div class="frame-794">
						<div class="frame-779">
							<p class="th-nh-ti-n">Thành tiền</p>
							<p class="_-16600000">
								<fmt:formatNumber value="${order.tongTien}" type="number"
									pattern="#,##0" />
								đ
							</p>
						</div>

					</div>
				</div>
				<div class="frame-404">

					<div class="frame-6602">
						<div class="frame-836">
							<c:choose>
								<c:when test="${not empty order.payment }">
									<p class="cnthanh-to-n">Đã thanh toán</p>
								</c:when>
								<c:otherwise>
									<p class="cnthanh-to-n">Vui lòng thanh toán khi nhận hàng</p>
								</c:otherwise>
							</c:choose>

							<p class="_-6-sa-nph-m">( ${quantity} sản phẩm)</p>
						</div>
						<div class="frame-835">
							<p class="_-16280000" style="white-space: nowrap;">
								<fmt:formatNumber value="${order.tongTien}" type="number"
									pattern="#,##0" />
								đ
							</p>
							<p class="abao-gmvat">(Đã bao gồm VAT)</p>
						</div>
					</div>
				</div>
			</div>
			<div class="frame-19554">
				<div class="frame-19539">
					<div class="button">
						<div class="kh-ng-sdng clip-contents" style="cursor: pointer;"
							onclick="openForm()">
							<div class="content clip-contents">
								<p class="text">Ghi chú</p>
								<div class="upload-16px">
									<p class="_-1">1</p>
								</div>
							</div>
						</div>
					</div>

					<c:if test="${order.status eq 0}">
						<div class="button-2">
							<div class="kh-ng-sdng-2 clip-contents" style="cursor: pointer;"
								onclick="cancle(${order.id})">
								<div class="content-50 clip-contents">
									<p class="text-24">Hủy đơn</p>
								</div>
							</div>
						</div>
					</c:if>
					
				</div>
				<div class="frame-19550">
					<div class="button-3">
						<div class="kh-ng-sdng-3 clip-contents" style="cursor: pointer;"
							onclick="close1()">
							<div class="content-51 clip-contents">
								<p class="text-25">Đóng</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<?php
    }
  }
  ?>

	<div class="overlay" id="popup" style="display: none;"
		onclick="closeForm()">
		<form class="form-popup" id="updateDataForm"
			onclick="event.stopPropagation()" method="POST">
			<button type="button" class="close-btn" onclick="closeForm()">X</button>
			<input type="hidden" value="${shipping.id}"
				id="id_shipping" name="id_shipping">
			<textarea placeholder="Nhập ghi chú..." id="note" name="note">${shipping.note}</textarea>
			<button type="submit">Xác nhận</button>
		</form>
	</div>

	<div id="cancelPopup" class="popup" style="display: none;"
		onclick="closePopup()">
		<div class="popup-content" onclick="event.stopPropagation()">
			<button type="button" class="close-btn" onclick="closePopup()">X</button>
			<h3>Nhập lý do hủy đơn hàng</h3>
			<textarea id="cancelReason" placeholder="Nhập lý do hủy..."></textarea>
			<button onclick="submitCancel()">Xác nhận hủy</button>
		</div>
	</div>

	<script>
    function openForm() {
      document.getElementById("popup").style.display = "flex";
    }

    // Ẩn form
    function closeForm() {
      document.getElementById("popup").style.display = "none";
    }

    function cancle(maphieuxuat) {
      // Sử dụng confirm để hỏi người dùng
      var isConfirmed = confirm("Bạn có chắc chắn muốn hủy đơn hàng?");

      // Nếu người dùng xác nhận
      if (isConfirmed) {
        // Hiển thị popup để nhập lý do hủy
        document.getElementById('cancelPopup').style.display = 'flex';

        // Lưu id đơn hàng vào window để sử dụng sau này
        window.maphieuxuat = maphieuxuat;
      }
    }

    // Đóng popup
    function closePopup() {

      document.getElementById('cancelPopup').style.display = 'none';
    }

    function close1() {
/*       document.getElementById('frame-19522').style.display = 'none';
      document.getElementById('header').style.display = 'none'; */

      window.location.href = "http://localhost:8080/Spring-mvc/account";

    }
    
    function submitCancel() {
      var cancelReason = document.getElementById('cancelReason').value.trim();

      // Kiểm tra xem người dùng đã nhập lý do chưa
      if (cancelReason === "") {
        alert("Vui lòng nhập lý do hủy đơn hàng.");
        return;
      }
      window.location.href =
  		"http://localhost:8080/Spring-mvc/account/huydonhang/" +
  		window.maphieuxuat +
  		"?reason=" +
  		encodeURIComponent(cancelReason);
      // Nếu có lý do hủy, chuyển hướng với lý do hủy đã nhập
/*       window.location.href = "http://localhost/DACS2/accountt/huydonhang/" + window.maphieuxuat + "?reason=" + encodeURIComponent(cancelReason);
 */    }

    document.getElementById('updateDataForm').addEventListener('submit', function (e) {
      e.preventDefault(); // Ngừng hành động mặc định của form

      const id = document.getElementById('id_shipping').value;
      const noteValue = document.getElementById('note').value;

      // Gửi dữ liệu qua AJAX
      fetch('http://localhost:8080/Spring-mvc/Info-Individual/updateNote?id_shipping='+id+'&note='+noteValue, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
/*         body: JSON.stringify({ note: noteValue, id_shipping: id }),
 */      })
        .then((response) => response.json())
        .then((data) => {
          if (data.success) {
            alert('Cập nhật thành công!');
            closeForm();
          } else {
            alert('Cập nhật thất bại! Lỗi: ' + data.error);
          }
        })
        .catch((error) => {
          console.error('Lỗi:', error);
          alert('Đã xảy ra lỗi khi gửi dữ liệu!');
        });
    });
  </script>
</body>

</html>