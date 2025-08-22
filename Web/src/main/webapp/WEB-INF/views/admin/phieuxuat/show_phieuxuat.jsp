<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>

<!-- Overlay background -->
<div class="overplay-background" onclick="closeForm()"></div>

<!-- Modal Container (Form Thông Tin Phiếu Xuất) -->
<div class="modal-container">
	<!-- Nút đóng form -->
	<div class="modal-header" onclick="closeForm()"">X</div>

	<!-- Form Thông Tin Phiếu Xuất -->
	<div class=" header">
		<h3>Thông Tin Phiếu Xuất</h3>
	</div>

	<!-- Dòng thông tin phiếu xuất -->
	<div class="info-row">
		<div class="form-group">
			<label for="voucherCode">Mã Phiếu:</label> <input type="text"
				class="form-control" id="voucherCode" readonly>
		</div>
		<div class="form-group">
			<label for="employee">Khách hàng:</label> <input type="text"
				class="form-control" id="employee" value="Nguyễn Văn A" readonly>
		</div>
		<div class="form-group">
			<label for="createTime">Thời gian tạo:</label> <input type="text"
				class="form-control" id="createTime" readonly>
		</div>
	</div>

	<!-- Các bảng chi tiết -->
	<div class="table-container">
		<!-- Thông tin phiếu xuất -->
		<div class="info-table">
			<table class="table table-striped" id="table1">
				<thead>
					<tr>
						<th
							style="min-width: 10px !important; width: 11px !important; padding-right: 5px !important; padding-left: 0px !important">
							STT</th>
						<th
							style="min-width: 10px !important; width: 11px !important; padding-right: 5px !important; padding-left: 0px !important">
							Mã phiên bản</th>
						<th>Tên sp</th>
						<th>Cấu hình</th>
						<th style="padding-right: 10px !important;">Đơn giá</th>
						<th
							style="min-width: 10px !important; width: 11px !important; padding-right: 0px !important; padding-left: 0px !important">
							Số lượng</th>
					</tr>
				</thead>
				<tbody id="table-body">

				</tbody>
			</table>
		</div>

		<!-- Chi tiết sản phẩm -->
		<div class="product-table">
			<table class="table table-striped" id="table2">
				<thead>
					<tr>
						<th
							style="min-width: 5px !important; width: 6px !important; padding-right: 0px !important">STT
						</th>
						<th>Mã IMEI</th>
					</tr>
				</thead>
				<tbody id="tableBody">

				</tbody>
			</table>
		</div>
	</div>
</div>
