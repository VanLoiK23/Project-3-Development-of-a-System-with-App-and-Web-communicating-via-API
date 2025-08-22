
function show(customerId) {
	const xhr = new XMLHttpRequest();
	const url = `http://localhost:8080/Spring-mvc/quan-tri/khach-hang/edit_KH?id=${customerId}`;

	// Cấu hình yêu cầu với phương thức GET và URL
	xhr.open("GET", url, true);

	// Khi có phản hồi từ server
	xhr.onload = function() {
		if (xhr.status === 200) {
			// Chuyển đổi dữ liệu JSON thành đối tượng
			const customer = JSON.parse(xhr.responseText).exists;

			// Gán các giá trị vào các trường trong form
			document.getElementById("id").value = customer.id;
			document.getElementById("name").value = customer.hoTen;
			document.getElementById("phone").value = customer.soDienThoai;
			document.getElementById("email").value = customer.email;
			
			const timestamp = customer.ngaySinh;
			const date = new Date(timestamp);

			// Lấy ngày/tháng/năm theo giờ địa phương
			const year = date.getFullYear();
			const month = String(date.getMonth() + 1).padStart(2, '0'); // tháng bắt đầu từ 0
			const day = String(date.getDate()).padStart(2, '0');

			const formattedDate = `${year}-${month}-${day}`;
			document.getElementById("ngaysinh").value = formattedDate;


			const genderSelect = document.getElementById("gender");
			const genderValue = customer.gioiTinh === "male" ? "male" : "female";

			// Duyệt qua các option và chọn giá trị khớp
			for (let i = 0; i < genderSelect.options.length; i++) {
				if (genderSelect.options[i].value === genderValue) {
					genderSelect.selectedIndex = i;
					break;
				}
			}

			// Hiển thị form
			document.querySelector(".form-container").style.display = "block";
			document.querySelector(".overlay-background").style.display = "block";

			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/khach-hang/edit_KH?id=${customerId}`;
			history.pushState(
				{
					path: newUrl,
				},
				"",
				newUrl
			);
		} else {
			alert("Có lỗi xảy ra khi lấy thông tin khách hàng.");
		}
	};

	// Xử lý lỗi trong quá trình gửi yêu cầu
	xhr.onerror = function() {
		alert("Không thể kết nối tới server.");
	};

	// Gửi yêu cầu
	xhr.send();
}


function Delete(customerId) {
	if (confirm("Bạn có chắc chắn muốn xóa không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/khach-hang?id=${customerId}&status=deleted`;

		// Cấu hình yêu cầu với phương thức GET và URL
		xhr.open("DELETE", url, true);

		// Khi có phản hồi từ server
		xhr.onload = function() {
			if (xhr.status === 200) {
				alert("Xóa thành công .");


				setTimeout(function() {
					window.location.href = "http://localhost:8080/Spring-mvc/quan-tri/Quanlikhachhang_controller/danh-sach";
				}, 1000);
			} else {
				alert("Có lỗi xảy ra khi lấy thông tin khách hàng.");
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

function lock(customerId) {
	if (confirm("Bạn có chắc chắn muốn khóa tài khoản này không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/khach-hang?id=${customerId}&status=lock`;

		// Cấu hình yêu cầu với phương thức GET và URL
		xhr.open("DELETE", url, true);

		// Khi có phản hồi từ server
		xhr.onload = function() {
			if (xhr.status === 200) {
				alert("Khóa thành công .");


				setTimeout(function() {
					window.location.href = "http://localhost:8080/Spring-mvc/quan-tri/Quanlikhachhang_controller/danh-sach";
				}, 1000);
			} else {
				alert("Có lỗi xảy ra khi lấy thông tin khách hàng.");
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

function active(customerId) {
	if (confirm("Bạn có chắc chắn muốn mở khóa tài khoản này không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/khach-hang?id=${customerId}&status=active`;

		// Cấu hình yêu cầu với phương thức GET và URL
		xhr.open("DELETE", url, true);

		// Khi có phản hồi từ server
		xhr.onload = function() {
			if (xhr.status === 200) {
				alert("Mở khóa thành công .");


				setTimeout(function() {
					window.location.href = "http://localhost:8080/Spring-mvc/quan-tri/Quanlikhachhang_controller/danh-sach";
				}, 1000);
			} else {
				alert("Có lỗi xảy ra khi lấy thông tin khách hàng.");
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

document.querySelector("#close").addEventListener("click", function() {
	document.querySelector(".form-container").style.display = "none";
	document.querySelector(".overlay-background").style.display = "none";
});
document.querySelector(".close-btn").addEventListener("click", function() {
	document.querySelector(".form-container").style.display = "none";
	document.querySelector(".overlay-background").style.display = "none";
});

document
	.querySelector(".overlay-background")
	.addEventListener("click", function() {
		document.querySelector(".form-container").style.display = "none";
		document.querySelector(".overlay-background").style.display = "none";
	});

function goBack() {
	window.history.back();
}

document.addEventListener("DOMContentLoaded", function() {
	const searchInput = document.getElementById("search");

	searchInput.addEventListener("keyup", function() {
		const search = searchInput.value;

		if (search == "") {
			fetch(
				"http://localhost/DACS2/Quanlikhachhang_controller/handle_empty_data_input",
				{
					method: "POST",
					headers: {
						"Content-Type": "application/x-www-form-urlencoded",
					},
					body: "empty_Input=" + encodeURIComponent(search),
				}
			)
				.then((response) => response.text())
				.then((data) => {
					document.getElementById("dskh").innerHTML = data;
				})
				.catch((error) => {
					console.error("Có lỗi xảy ra:", error);
				});
		} else {
			fetch("http://localhost/DACS2/Quanlikhachhang_controller/search", {
				method: "POST",
				headers: {
					"Content-Type": "application/x-www-form-urlencoded",
				},
				body: "data=" + encodeURIComponent(search),
			})
				.then((response) => response.text())
				.then((data) => {
					document.getElementById("dskh").innerHTML = data;
				})
				.catch((error) => {
					console.error("Có lỗi xảy ra:", error);
				});
		}
	});
});
