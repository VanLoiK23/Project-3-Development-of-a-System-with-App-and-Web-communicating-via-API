function Delete(ma) {
	if (confirm("Bạn có chắc chắn muốn xóa không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/danh-gia?id=${ma}`;

		// Cấu hình yêu cầu với phương thức GET và URL
		xhr.open("DELETE", url, true);


		xhr.onload = function() {
			if (xhr.status === 200) {

				alert("Xóa thành công .");


				setTimeout(function() {
					window.location.href = "http://localhost:8080/Spring-mvc/quan-tri/Quanlicomment_controller/danh-sach";
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
