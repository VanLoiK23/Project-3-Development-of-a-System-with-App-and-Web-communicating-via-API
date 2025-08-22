var selectedColor = null;

document.addEventListener("DOMContentLoaded", function() {
	const items = document.querySelectorAll(".box03__item");

	// Kiểm tra nếu có ít nhất một phần tử
	if (items.length === 0) {
		console.log("Không tìm thấy phần tử box03__item");
		return;
	}

	// Lọc các phần tử có lớp 'act'
	const actItems = Array.from(items).filter((item) =>
		item.classList.contains("act")
	);

	if (actItems.length > 0) {
		// Lấy phần tử cuối cùng có lớp 'act'
		const lastActItem = actItems[actItems.length - 1];
		let url = lastActItem.getAttribute("href");
		console.log("URL của phần tử cuối cùng có lớp act:", url);

		// Kiểm tra xem có giá trị p= trong URL không
		let match = url.match(/[?&]p=([^&]+)/);
		if (match) {
			selectedColor = match[1]; // Lấy giá trị sau p=
			console.log("Giá trị p= đã chọn:", selectedColor);
		} else {
			console.log("Không tìm thấy giá trị p= trong URL");
		}
	} else {
		console.log("Không tìm thấy phần tử nào có lớp act");
	}
});

// Hàm thêm vào giỏ hàng
function themVaoGioHang(event, masp, num) {
	event.preventDefault();

	console.log("selectedColor tại thời điểm click vào giỏ hàng:", selectedColor);

	if (!selectedColor) {
		alert("Vui lòng chọn cấu hình trước khi mua hàng.");
		return;
	}

	fetch("http://localhost:8080/Spring-mvc/homepage/checkLogin")
		.then((response) => response.json())
		.then((data) => {
			if (data.loggedIn) {
				var currentHref = selectedColor;


				var regex = /([a-zA-Z0-9-]+)-((?:\d+|null)gb)-((?:\d+|null)gb)-(Màu\s.+)$/;
				var match = currentHref.match(regex);

				if (match) {
					const alias = match[1]; // alias của sản phẩm
					const ramRaw = match[2]; // RAM
					const romRaw = match[3]; // ROM
					const color = match[4]; // Màu sắc

					let ram = ramRaw ? ramRaw.replace("gb", "") : null;
					let rom = romRaw ? romRaw.replace("gb", "") : null;

					if (ram === "null") ram = null;
					if (rom === "null") rom = null;

					// Gửi AJAX để thêm vào giỏ hàng
					$.ajax({
						url: "http://localhost:8080/Spring-mvc/product/addToCart",
						type: "POST",
						data: {
							masp: masp,
							alias: alias,
							rom: rom,
							ram: ram,
							color: color,
							num: num,
							isWithoutCart: false,
						},
						dataType: "json",
						success: function(response) {
							console.log(response.message);
							if (
								response.Result == 'Sản phẩm đã được thêm vào giỏ hàng!'
							) {
								alert(response.Result);

								if (confirm('Bạn có muốn chuyển tới giỏ hàng không?')) {
									window.location.href = "http://localhost:8080/Spring-mvc/Cart";
								}

							} else {
								alert(response.Result);
							}
						},
						error: function(xhr, status, error) {
							console.error("Có lỗi xảy ra:", error);
						},
					});
				} else {
					// Trường hợp không có regex
					regex = /([a-zA-Z0-9-]+)/;
					match = currentHref.match(regex);

					const alias = match[1];

					$.ajax({
						url: "http://localhost:8080/Spring-mvc/product/addToCart",
						type: "POST",
						data: {
							masp: masp,
							alias: alias,
							num: num,
							isWithoutCart: false,
						},
						dataType: "json",
						success: function(response) {
							console.log(response.message);
							if (
								response.Result == 'Sản phẩm đã được thêm vào giỏ hàng!'
							) {
								alert(response.Result);

								if (confirm('Bạn có muốn chuyển tới giỏ hàng không?')) {
									window.location.href = "http://localhost:8080/Spring-mvc/Cart";
								}

							} else {
								alert(response.Result);
							}
						},
						error: function(xhr, status, error) {
							console.error("Có lỗi xảy ra:", error);
						},
					});
				}
			} else {
				if (
					confirm(
						"Bạn cần đăng nhập để mua hàng. Bạn có muốn chuyển đến trang đăng nhập không?"
					)
				) {
					window.location.href = "http://localhost:8080/Spring-mvc/dang-nhap";
				}
			}
		})
		.catch((error) => console.error("Có lỗi xảy ra:", error));
}


function checkSL(event, masp, num) {
	event.preventDefault();


	if (!selectedColor) {
		alert("Vui lòng chọn cấu hình trước khi mua hàng.");
		return;
	}

	fetch("http://localhost:8080/Spring-mvc/homepage/checkLogin")
		.then((response) => response.json())
		.then((data) => {
			if (data.loggedIn) {
				var currentHref = selectedColor;


				var regex = /([a-zA-Z0-9-]+)-(\d+gb)-(\d+gb)-(Màu\s.+)$/;
				var match = currentHref.match(regex);

				if (match) {
					const alias = match[1]; // alias của sản phẩm
					const ramRaw = match[2]; // RAM
					const romRaw = match[3]; // ROM
					const color = match[4]; // Màu sắc

					let ram = ramRaw ? ramRaw.replace("gb", "") : null;
					let rom = romRaw ? romRaw.replace("gb", "") : null;

					if (ram === "null") ram = null;
					if (rom === "null") rom = null;

					// Gửi AJAX để thêm vào giỏ hàng
					$.ajax({
						url: "http://localhost:8080/Spring-mvc/product/addToCart",
						type: "POST",
						data: {
							masp: masp,
							alias: alias,
							rom: rom,
							ram: ram,
							color: color,
							num: num,
							isWithoutCart: true,
						},
						dataType: "json",
						success: function(response) {
							console.log(response.message);
							if (
								response.Result == 'Sản phẩm đã được thêm vào giỏ hàng!'
							) {
								alert("Đang chuyển đến trang thanh toán...");

								window.location.href = "http://localhost:8080/Spring-mvc/Cart/checkout";
							} else {
								alert(response.Result);
							}
						},
						error: function(xhr, status, error) {
							console.error("Có lỗi xảy ra:", error);
						},
					});
				} else {
					// Trường hợp không có regex
					regex = /([a-zA-Z0-9-]+)/;
					match = currentHref.match(regex);

					const alias = match[1];

					$.ajax({
						url: "http://localhost:8080/Spring-mvc/product/addToCart",
						type: "POST",
						data: {
							masp: masp,
							alias: alias,
							num: num,
							isWithoutCart: true,
						},
						dataType: "json",
						success: function(response) {
							console.log(response.message);
							if (
								response.Result == 'Sản phẩm đã được thêm vào giỏ hàng!'
							) {
								alert("Đang chuyển đến trang thanh toán...");
								window.location.href = "http://localhost:8080/Spring-mvc/Cart/checkout";

							} else {
								alert(response.Result);
							}
						},
						error: function(xhr, status, error) {
							console.error("Có lỗi xảy ra:", error);
						},
					});
				}
			} else {
				if (
					confirm(
						"Bạn cần đăng nhập để mua hàng. Bạn có muốn chuyển đến trang đăng nhập không?"
					)
				) {
					window.location.href = "http://localhost:8080/Spring-mvc/homepage/checkLogin";
				}
			}
		})
		.catch((error) => console.error("Có lỗi xảy ra:", error));
}


