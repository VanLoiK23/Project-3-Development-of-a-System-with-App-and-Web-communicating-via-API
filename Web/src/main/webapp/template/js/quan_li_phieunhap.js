function show(id) {
	const xhr = new XMLHttpRequest();
	const url = `http://localhost:8080/Spring-mvc/quan-tri/phieu-nhap/showdetails?id=${id}`;

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

			// Thông tin phiếu
			document.getElementById("voucherCode").value = "PN" + orderData.id;
			document.getElementById("employee").value = orderData.nguoiTaoPhieuName || "";
			document.getElementById("supplier").value = orderData.nhaCungCapName || "";
			document.getElementById("createTime").value = convertTimestampToDateString(orderData.thoiGian);

			// Hiển thị danh sách sản phẩm (không trùng mã phiên bản)
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


			
			// Hiển thị modal
			document.querySelector(".modal-container").style.display = "block";
			document.querySelector(".overplay-background").style.display = "block";

			// Cập nhật URL hiện tại
			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/phieu-nhap/showdetails?id=${id}`;
			history.pushState({ path: newUrl }, "", newUrl);
		}
	};

	xhr.onerror = function() {
		alert("Không thể kết nối tới server.");
	};

	xhr.send();
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
		const url = `http://localhost:8080/Spring-mvc/quan-tri/phieu-nhap?id=${id}`;

		xhr.open("DELETE", url, true);

		// Khi có phản hồi từ server
		xhr.onload = function() {
			if (xhr.status === 200) {

				alert("Lưu thành công .");


				setTimeout(function() {
					window.location.href = "http://localhost:8080/Spring-mvc/quan-tri/phieunhap/danh-sach";
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
