<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/common/taglib.jsp"%>
<div class="overlay-background1" onclick="closeForm1()"></div>

<div class="form-container1">
	<button class="close-btn" title="close" onclick="closeForm1()">
		<i class="fa-solid fa-x"></i>
	</button>
	<div class="table">
		<button class="close-btn" title="close" onclick="closeForm1()">
			<i class="fa-solid fa-x"></i>
		</button>
		<div class="table-header">
			<p>
				<button>
					<i class="fa-solid fa-sliders"></i>
				</button>
				Phiên bản sản phẩm
			</p>
		</div>
		<div>
			<div class="table-section">
				<table>
					<thread>
					<tr>
						<th>STT</th>
						<th>Ram</th>
						<th>Rom</th>
						<th>Màu sắc</th>
						<th>Giá nhập</th>
						<th>Giá bán</th>
					</tr>
					</thread>
					<tbody id="list_config">
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>