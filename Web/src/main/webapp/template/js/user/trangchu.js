window.onload = function() {
	var owl = $(".owl-carousel");
	owl.owlCarousel({
		items: 1.5,
		margin: 100,
		center: true,
		loop: true,
		smartSpeed: 450,
		autoplay: true,
		autoplayTimeout: 3500,
	});
};

function filterProductsName(ele) {
	var filter = ele.value;

	if (filter == "") {
		// Hiển thị tất cả các sản phẩm khi input r��ng
		var ul = document.getElementById("products");
		var listLi = ul.getElementsByTagName("li");
		var filteredLi = Array.from(listLi).filter(function(li) {
			return !li.classList.contains("merge__item"); // Lọc bỏ các <li> có class 'merge__item'
		});
		for (var i = 0; i < filteredLi.length; i++) {
			var li = filteredLi[i];
			showLi(li);
		}
		alertNotHaveProduct(true);

		return;
	} else {
		// Gửi yêu cầu AJAX đến server để lấy danh sách sản phẩm phù hợp
		$.ajax({
			url: "http://localhost:8080/Spring-mvc/homepage/filter",
			data: { search: filter },
			success: function(response) {
				console.log(JSON.stringify(response, null, 2)); // Kiểm tra phản hồi từ server


				var foundIds = response;

				var ul = document.getElementById("products");
				var listLi = ul.getElementsByTagName("li");
				var filteredLi = Array.from(listLi).filter(function(li) {
					return !li.classList.contains("merge__item"); // Lọc bỏ các <li> có class 'merge__item'
				});
				var coSanPham = false;
				var soLuong = 0;

				for (var i = 0; i < filteredLi.length; i++) {
					var li = filteredLi[i];
					var idSp = li.getAttribute("id_sp");
					var found = false; // Biến kiểm tra nếu sản phẩm được tìm thấy

					// Kiểm tra xem id_sp có trong danh sách foundIds không
					for (var j = 0; j < foundIds.length; j++) {
						if (foundIds[j] === parseInt(idSp)) {
							found = true;
							coSanPham = true;
							soLuong++;
							showLi(li);
							break;
						}
					}

					// Nếu không tìm thấy, ẩn sản phẩm
					if (!found) {
						hideLi(li);
					}
				}

				// Thông báo nếu không có sản phẩm
				alertNotHaveProduct(coSanPham);
			}
			,
			error: function(xhr, status, error) {
				console.error("Đã xảy ra lỗi: " + error);
			},
		});
	}
}

function hideLi(li) {
	li.style.width = 0;
	li.style.opacity = 0;
	li.style.borderWidth = "0";
}
function showLi(li) {
	li.style.opacity = 1;
	li.style.width = "239px";
	li.style.borderWidth = "1px";
}

function alertNotHaveProduct(coSanPham) {
	var thongbao = document.getElementById("khongCoSanPham");
	if (!coSanPham) {
		thongbao.style.width = "auto";
		thongbao.style.opacity = "1";
		thongbao.style.display = "block";
		thongbao.style.margin = "auto";
		thongbao.style.transitionDuration = "1s";
	} else {
		thongbao.style.width = "0";
		thongbao.style.opacity = "0";
		thongbao.style.display = "block";
		thongbao.style.margin = "0";
		thongbao.style.transitionDuration = "0s";
	}
}

$(document).ready(function() {
	$("#search-box").on("keyup", function() {
		show_suggest(this);
	});

	$(".merge__item").on("click", function(event) {
		event.preventDefault();

		var parentUl = $(this).closest("ul");
		parentUl.find(".merge__item").removeClass("act");
		$(this).addClass("act");

		var dataUrl = $(this).data("url");

		var params = new URLSearchParams(dataUrl.split("?")[1]);

		var parentSanPham = $(this).closest(".sanPham");
		// Lấy giá trị của alias và id_pbsp
		var alias = params.get("p");
		var id_pbsp = params.get("id_pbsp");
		$.ajax({
			url: "http://localhost:8080/Spring-mvc/homepage/getPrice",
			type: "POST",
			data: {
				alias: alias,
				id_pbsp: id_pbsp,
			},
			success: function(response) {
				if (typeof response === "string") {
					response = JSON.parse(response);
				}

				// Xử lý trực tiếp object
				if (response && typeof response === "object") {
					parentSanPham.find("a").each(function() {
						var newHref =
							"http://localhost:8080/Spring-mvc/Detail?p=" +
							alias +
							"-" +
							response.kichThuocRam +
							"gb" +
							"-" +
							response.kichThuocRom +
							"gb-" +
							response.color;
						$(this).attr("href", newHref);
					});

					var parent = parentSanPham.find(".price strong");
					parent.text(response.priceSale.toLocaleString() + " đ");

					if (response.giaXuat !== response.priceSale) {
						parentSanPham
							.find(".price span")
							.text(response.giaXuat.toLocaleString() + " đ");
					}
				} else {
					console.log("Expected an object in response.");
				}
			},
			error: function(xhr, status, error) {
				console.log("Có lỗi xảy ra: " + error);
			},
		});
	});
});

function highlightText(text, searchTerm) {
	const regex = new RegExp(`(${searchTerm})`, "gi");
	return text.replace(
		regex,
		'<strong style="background-color: yellow;">$1</strong>'
	);
}

function show_suggest(input) {
	var suggest = input.value;

	if (suggest !== "") {
		$.ajax({
			url: "http://localhost:8080/Spring-mvc/homepage/show_suggest",
			data: { suggest: suggest },
			dataType: "json",
			success: function(response) {
				console.log("Response from server:", response);

				const suggestionBox = document.getElementById(
					"search-boxautocomplete-list"
				);
				suggestionBox.innerHTML = "";

				if (response && response.length > 0) {
					response.forEach(function(item) {
						const suggestionItem = document.createElement("div");

						// Tạo nội dung có highlight phần khớp tạo biểu thức chính quy với input nhập vào và bôi đậm
						const regex = new RegExp(`(${suggest})`, "gi");
						const highlightedText = item.replace(regex, "<strong>$1</strong>");

						// Đặt nội dung có highlight vào HTML của phần tử
						suggestionItem.innerHTML = highlightedText;

						// Thêm sự kiện click để chọn gợi ý
						suggestionItem.addEventListener("click", function() {
							input.value = item;
							suggestionBox.innerHTML = "";
						});

						suggestionBox.appendChild(suggestionItem);
					});
				} else {
					console.warn("No suggestions found for the given input.");
				}
			},
			error: function(xhr, status, error) {
				console.error("Đã xảy ra lỗi khi gửi yêu cầu AJAX: " + error);
				console.log(xhr.responseText);
			},
		});
	} else {
		document.getElementById("search-boxautocomplete-list").innerHTML = "";
	}
}


function themVaoGioHang(event, masp, num) {
	event.preventDefault();
	console.log(masp)
	console.log(num)
	fetch("http://localhost:8080/Spring-mvc/homepage/checkLogin")
		.then((response) => response.json())
		.then((data) => {
			if (data.loggedIn) {
				var currentHref = $(event.target).closest("a").attr("href");

				var regex = /p=([a-zA-Z0-9-]+)-((?:\d+|null)gb)-((?:\d+|null)gb)-(Màu\s.+)$/;
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
							console.error(response.message);

							alert(response.Result);

						},
						error: function(xhr, status, error) {
							console.error("Có lỗi xảy ra:", error);
						},
					});
				} else {
					regex = /p=([a-zA-Z0-9-]+)/;
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
							console.error(response.message);

							alert(response.Result);

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

function gotoTop() {
	if (window.jQuery) {
		jQuery("html,body").animate(
			{
				scrollTop: 0,
			},
			100
		);
	} else {
		document.getElementsByClassName("top-nav")[0].scrollIntoView({
			behavior: "smooth",
			block: "start",
		});
		document.body.scrollTop = 0; // For Safari
		document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
	}
}

function logOut() {
	const logoutUrl = "http://localhost:8080/Spring-mvc/logout";
	window.location.href = logoutUrl;
}
