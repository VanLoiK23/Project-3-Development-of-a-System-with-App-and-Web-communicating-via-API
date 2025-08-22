<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<div class="overlay-background" onclick="closeForm()"></div>

<div class="form-container" id="details-form">
	<button class="close-btn" title="close" onclick="closeForm()">
		<i class="fa-solid fa-x"></i>
	</button>
	<h5>
		<button style="background-color: #0ffbff;" class="show">
			<i class="fa-solid fa-circle-info"></i>
		</button>
		Chi tiết thông tin sản phẩm
	</h5>
	<form>
		<div class="right-form">
			<div class="form-group">
				<label for="img" class="img">Hình ảnh sản phẩm</label> <img
					style="width: 260px; aspect-ratio: 4/3"
					src="https://img.upanh.tv/2024/10/18/download.jpg" alt="" id="img">
			</div>
		</div>
		<div class="left-form">
			<div class="form-group">
				<input type="hidden" id="id" name="id" required>
				<div>
					<label for="name">Tên sản phẩm</label> <input type="text"
						class="form-control" id="name" name="name" required>
				</div>
				<div>
					<label for="">Xuất xứ</label> <input type="text"
						class="form-control" id="xx" name="xx" required>
				</div>
				<div>
					<label for="">Dung lượng pin</label> <input type="number"
						class="form-control" id="dlp" name="dlp" required>
				</div>
			</div>
			<div class="form-group">
				<div>
					<label for="name">Màn hình</label> <input type="text"
						class="form-control" id="size" name="size" required>
				</div>
				<div>
					<label for="phone">Camera sau</label> <input type="text"
						class="form-control" id="cr_s" name="cr_s" required>
				</div>
				<div>
					<label for="phone">Camera trước</label> <input type="text"
						class="form-control" id="cr_tr" name="cr_tr" required>
				</div>
			</div>
			<div class="form-group child">
				<div>
					<label for="name">Hệ điều hành</label> <input type="text"
						class="form-control" id="hdh" name="hdh" required>
				</div>
				<div>
					<label for="phone">Thương hiệu</label> <input type="text"
						class="form-control" id="th" name="th" required>
				</div>
				<div>
					<label for="phone">Phiên bản HĐH</label> <input type="text"
						class="form-control" id="version" name="version" required>
				</div>

				<div>
					<label for="phone">Khu vực kho</label> <input type="text"
						class="form-control" id="kho" name="kho" required>
				</div>
			</div>
			<div>
				<button type="button"
					style="background-color: #007BFF; width: 70px; color: white; border: none; cursor: pointer;"
					onclick="show_config()">
					<i class="fa-solid fa-gears"></i>
				</button>
			</div>
		</div>
	</form>
</div>

