function show(id) {
	const xhr = new XMLHttpRequest();
	const url = `http://localhost:8080/Spring-mvc/quan-tri/don-hang/showdetails?id=${id}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			const orderData = JSON.parse(xhr.responseText);
			console.log(orderData);

			const tableBody = document.getElementById("table-body");
			const tableBody1 = document.getElementById("tableBody");
			const uniqueMaphienbansp = new Set();

			tableBody.innerHTML = "";
			tableBody1.innerHTML = "";

			document.getElementById("voucherCode").value = "PX" + orderData.id;
			document.getElementById("employee").value = orderData.khachHangName || "";
			document.getElementById("createTime").value = convertTimestampToDateString(orderData.thoiGian);


			orderData.product_info.forEach((product, index) => {
				const [maphienbansp, tensp, kichthuocram, kichthuocrom, tenmausac, gianhap] = product.info;

				if (!uniqueMaphienbansp.has(maphienbansp)) {
					uniqueMaphienbansp.add(maphienbansp);

					const row = document.createElement("tr");
					row.innerHTML = `
						            <td>${tableBody.rows.length + 1}</td>
						            <td>${maphienbansp}</td>
						            <td>${tensp}</td>
						            <td>${kichthuocram}GB - ${kichthuocrom}GB - ${tenmausac}</td>
						            <td>${formatCurrency(gianhap)}</td>
						            <td>${product.imeis.length}</td>
						        `;
					tableBody.appendChild(row);

					// Bắt sự kiện click để hiển thị danh sách IMEI tương ứng
					row.addEventListener("click", () => {
						tableBody1.innerHTML = "";
						let imeiIndex = 1;

						product.imeis.forEach((imei) => {
							const row1 = document.createElement("tr");
							row1.innerHTML = `
						                    <td>${imeiIndex}</td>
						                    <td>${imei}</td>
						                `;
							tableBody1.appendChild(row1);
							imeiIndex++;
						});
					});
				}
			});

			document.querySelector(".modal-container").style.display = "block";
			document.querySelector(".overplay-background").style.display = "block";

			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/don-hang/showdetails?id=${id}`;
			history.pushState(
				{
					path: newUrl,
				},
				"",
				newUrl
			);
		}
	};

	xhr.onerror = function() {
		alert("Không thể kết nối tới server.");
	};

	// Gửi yêu cầu
	xhr.send();
}

function cancle(maphieuxuat, content) {
	// Hiển thị popup để nhập lý do hủy
	document.getElementById("cancelReason").innerHTML = content;
	document.getElementById("cancelPopup").style.display = "flex";
}
function closePopup() {
	document.getElementById("cancelPopup").style.display = "none";
}


function convertTimestampToDateString(timestamp) {
	const date = new Date(timestamp);
	const day = String(date.getDate()).padStart(2, '0');
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const year = date.getFullYear();
	const hour = String(date.getHours()).padStart(2, '0');
	const minute = String(date.getMinutes()).padStart(2, '0');
	return `${day}/${month}/${year} ${hour}:${minute}`;
}
// Hàm định dạng tiền VND
function formatCurrency(amount) {
	return amount.toLocaleString("vi-VN", { style: "currency", currency: "VND" });
}

function closeForm() {
	document.querySelector(".modal-container").style.display = "none";
	document.querySelector(".overplay-background").style.display = "none";
}

function goBack() {
	window.history.back();
}

function Delete(id) {
	if (confirm("Bạn có chắc chắn muốn lưu trữ không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/don-hang?id=${id}`;

		xhr.open("DELETE", url, true);

		// Khi có phản hồi từ server
		xhr.onload = function() {
			if (xhr.status === 200) {
				alert("Xóa thành công .");


				setTimeout(function() {
					window.location.href = "http://localhost:8080/Spring-mvc/quan-tri/donhang/danh-sach";
				}, 1000);
			}
		};

		// Xử lý lỗi trong quá trình gửi yêu cầu
		xhr.onerror = function() {
			alert("Không thể kết nối tới server.");
		};

		// Gửi yêu cầu
		xhr.send();
	}
}

function cancel($id, $status) {
	if (confirm("Bạn có chắc chắn muốn hủy đơn hàng không?")) {
		window.location.href =
			"http://localhost:8080/Spring-mvc/quan-tri/don-hang/status/" + $id + "/" + $status;
	}
}

function change_status($id, $status) {
	var status = $status;
	var id = $id;
	let obj = {
		1: "duyệt đơn hàng",
		2: "lấy hàng",
		3: "giao hàng",
		4: "xác nhận giao hàng",
		"-1": "hủy đơn hàng",
		"-3": "đơn hủy của khách hàng",
	};

	if (status == -1) {
		status = status.toString();
	}
	if (status == -3) {
		status = status.toString();
	}

	if (confirm("Bạn có chắc chắn muốn " + obj[status] + " không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/don-hang/status/${id}/${parseInt(status, 10)}`;

		xhr.open("GET", url, true);

		xhr.onload = function() {
			if (xhr.status === 200) {
				alert("Trạng thái đã được cập nhật");
				var maphieuxuat = id;
				// Cập nhật lại màu và nội dung của các nút
				let statusButton = $("#status_" + maphieuxuat); // Lấy nút với ID tương ứng
				let process_list = $("#process_" + maphieuxuat); //xu li an hang neu cancle
				let status_list = $("#status1_" + maphieuxuat); // chuyen doi text status

				// Xóa các lớp cũ
				statusButton.removeClass("duyet pending shipped delivered canceled");

				// Thêm lớp mới theo trạng thái
				if (status == 1) {
					statusButton.addClass("pending");
					statusButton.html("Lấy hàng");
					statusButton.attr("onclick", "change_status(" + maphieuxuat + ", 2)");
					status_list.html("Đang lấy hàng");
				} else if (status == 2) {
					statusButton.addClass("shipped");
					statusButton.html("Giao hàng");
					statusButton.attr("onclick", "change_status(" + maphieuxuat + ", 3)");
					status_list.html("Đang chờ giao hàng");
				} else if (status == 3) {
					statusButton.addClass("delivered");
					statusButton.html("Xác nhận giao hàng ");
					statusButton.attr("onclick", "change_status(" + maphieuxuat + ", 4)");
					status_list.html("Đang giao hàng");
				} else if (status == 4) {
					process_list.html("");
					status_list.html("Đã giao");
				} else if (status == -1) {
					process_list.html("");
					status_list.html("Nhân viên đã hủy");
				} else if (status == -3) {
					process_list.html("");
					status_list.html("Khách hàng đã hủy");
				}
			}
		};

		xhr.onerror = function() {
			alert("Không thể kết nối tới server.");
		};

		xhr.send();
	}
}
