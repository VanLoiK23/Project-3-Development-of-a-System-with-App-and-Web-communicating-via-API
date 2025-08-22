<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>


<div class="overlay-background2" onclick="closeForm2()"></div>

<div class="form-container2">
	<button class="close-btn" title="close" onclick="closeForm2()">
		<i class="fa-solid fa-x"></i>
	</button>
	<h5>
		<button style="background-color: pink;" class="show">
			<i class="fa-brands fa-product-hunt"></i>
		</button>
		Nhập thêm sản phẩm
	</h5>
	<form action="http://localhost:8080/Spring-mvc/quan-tri/Quanlisanpham_controller/import"
		method="POST">
		<div class="right-form">
			<div class="form-group">
				<label for="img" class="img">Hình ảnh sản phẩm</label> <img
					style="width: 260px; aspect-ratio: 4/3"
					src="https://img.upanh.tv/2024/10/18/download.jpg" alt="" id="img1">
			</div>
		</div>
		<div class="left-form">
			<div class="form-group">
				<input type="hidden" id="id1" name="id">
				<div>
					<label for="name">Tên sản phẩm</label> <input type="text"
						class="form-control" id="name1" name="name">
				</div>
			</div>
			<div class="form-group">
				<div>
					<label for="">Dung lượng pin</label> <input type="number"
						class="form-control" id="dlp1" name="dlp">
				</div>
			</div>
			<div class="form-group">
				<div>
					<label for="name">Hệ điều hành</label> <input type="text"
						class="form-control" id="hdh1" name="hdh">
				</div>
			</div>
			<div class="form-group">
				<div>
					<label for="phone">Khu vực kho</label> <input type="text"
						class="form-control" id="kho1" name="kho">
				</div>
			</div>
		</div>
		<div class="flex-end">
			<div class="form-group" style="display: flex; gap: 10px">

				<div>
					<label style="font-size: 13px">Lựa chọn phiên bản sản phẩm</label>
					<select style="background-color: aliceblue" class="form-control"
						id="import" name="import">

					</select>
				</div>
				<div>
					<label style="font-size: 13px">Nhập số lượng nhập</label> <input
						min="1" max="40" type="number"
						style="pointer-events: visible; background-color: aliceblue; width: 100px;"
						class="form-control" id="number">
				</div>
				<div>
					<button type="button" onclick="addRow()"
						style="margin-top: 35px; background-color: #83fc02; width: 50px; height: 30px; display: flex; align-items: center; justify-content: center;">
						<i class="fa-solid fa-square-plus"></i>
					</button>
				</div>
			</div>
			<div class="table">
				<div class="table-header">
					<p>
						<button type="button" style="background-color: #bde8f8">
							<i class="fa-solid fa-sliders"></i>
						</button>
						Các phiên bản sản phẩm
					</p>
				</div>
				<div>
					<div class="table-section">
						<table>
							<thread>
							<tr>
								<th style="width: 35px">STT</th>
								<th>Ram</th>
								<th>Rom</th>
								<th style="width: 100px">Màu sắc</th>
								<th style="width: 120px">Giá nhập</th>
								<th style="width: 80px">Số lượng</th>
							</tr>
							</thread>
							<tbody id="tb">
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div style="display: flex">
				<div id="imeiInputsContainer"></div>
				<select style="background-color: aliceblue; width: 250px"
					class="form-control" id="supplier" name="supplier" required>
					<option value="" disabled selected>Chọn nhà cung cấp</option>

					<c:forEach var="item" items="${nhacungcapModel}">
						<option value="${item.id}">${item.tenNhaCungCap}</option>
					</c:forEach>

					
				</select>
				<button style="background-color: aquamarine; margin-left: 50px"
					type="button" id="addIMEI" onclick="generateIMEIInputs()">Thêm
					IMEI</button>
			</div>
		</div>
	</form>
</div>

