<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<%@ page import="java.util.*"%>

<!-- Overlay Background with Form -->
<div class="overlay-background" id="overlay-background"
	onclick="closeForm()">
	<div class="form-container" onclick="event.stopPropagation()">
		<h2>Thêm mã giảm giá mới</h2>
		<form method="POST"
			action="<c:url value='/quan-tri/discount/add_Discount'/>">
			<div class="form-row">
				<div class="form-group">
					<label for="discount_code">Mã giảm giá</label> <input type="text"
						id="discount_code" name="discount_code" required>
				</div>

				<div class="form-group">
					<label for="discount_amount">Số tiền giảm giá</label> <input
						type="number" id="discount_amount" name="discount_amount" required>
				</div>
			</div>

			<div class="form-row">
				<div class="form-group">
					<label for="limit_number">Số lần giới hạn nhập</label> <input
						type="number" id="limit_number" name="limit_number" required>
				</div>

				<div class="form-group">
					<label for="expiration_date">Ngày hết hạn</label> <input
						type="date" id="expiration_date" name="expiration_date" required>
				</div>
			</div>

			<div class="form-row">
				<div class="form-group" style="width: 100%;">
					<label for="description">Mô tả ngắn</label>
					<textarea id="description" name="description" required></textarea>
				</div>
			</div>

			<div class="form-row">
				<div class="form-group">
					<label for="payment_limit">Số tiền đơn hàng tối thiểu được
						áp dụng</label> <input type="number" id="payment_limit"
						name="payment_limit" required>
				</div>

				<div class="form-group">
					<label for="status">Trạng thái</label> <select id="status"
						name="status" required>
						<option value="1">Có hiệu lực</option>
						<option value="0">Không hiệu lực</option>
					</select>
				</div>
			</div>

			<div class="form-footer">
				<button type="submit" name="submit" id="add" class="btn btn-primary">
					<i class="fa-solid fa-calendar-plus"></i> Lưu
				</button>
				<button type="button" onclick="closeForm()"
					class="btn btn-secondary">
					<i class="fa-solid fa-rotate-left"></i> Quay Lại
				</button>
			</div>
		</form>
	</div>
</div>