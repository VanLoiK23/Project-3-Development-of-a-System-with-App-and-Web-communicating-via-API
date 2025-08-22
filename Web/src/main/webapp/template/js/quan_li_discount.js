document.addEventListener("DOMContentLoaded", function() {
	// Function to load the initial data
	function load() {
		phantrang(1); // Load page 1 initially
	}

	// Function to handle pagination using AJAX
	function phantrang(page) {
		const xhr = new XMLHttpRequest();
		xhr.open(
			"GET",
			`http://localhost/DACS2/phantrang_discount?page=${page}`,
			true
		);
		xhr.onload = function() {
			if (xhr.status === 200) {
				// Update the content of the table with the new data
				document.getElementById("table").innerHTML = xhr.responseText;
			} else {
				console.error("An error occurred: " + xhr.statusText);
			}
		};
		xhr.onerror = function() {
			console.error("An error occurred while making the request.");
		};
		xhr.send();
	}

	// Load the first page when the page is ready
	load();

	// Event listener for pagination links
	document.addEventListener("click", function(event) {
		const target = event.target;
		// Check if a pagination link was clicked
		if (
			target.tagName === "LI" &&
			target.closest("ul").classList.contains("phantrang")
		) {
			const page = target.getAttribute("page"); // Get the page number from the 'page' attribute
			phantrang(page); // Load the selected page
		}
	});
});
document.addEventListener("DOMContentLoaded", function() {
	const searchInput = document.getElementById("search");

	searchInput.addEventListener("keyup", function() {
		const search = searchInput.value;

		if (search == "") {
			fetch(
				"http://localhost/DACS2/discount/handle_empty_data_input",
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
					document.getElementById("ds").innerHTML = data;
				})
				.catch((error) => {
					console.error("Có lỗi xảy ra:", error);
				});
		} else {
			fetch("http://localhost/DACS2/discount/search", {
				method: "POST",
				headers: {
					"Content-Type": "application/x-www-form-urlencoded",
				},
				body: "data=" + encodeURIComponent(search),
			})
				.then((response) => response.text())
				.then((data) => {
					document.getElementById("ds").innerHTML = data;
				})
				.catch((error) => {
					console.error("Có lỗi xảy ra:", error);
				});
		}
	});
});


function show(ma) {
	const xhr = new XMLHttpRequest();

	const url = `http://localhost:8080/Spring-mvc/quan-tri/ma-giam-gia/edit_Discount?id=${ma}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			const data = JSON.parse(xhr.responseText).exists;

			document.getElementById('id').value = data.id;
			document.getElementById('discount_code1').value = data.code;
			document.getElementById('discount_amount1').value = data.discountAmount;
			document.getElementById('limit_number1').value = data.numberUsed;
			const millis = data.expirationDate;
			const dateObj = new Date(millis);

			// Convert sang "yyyy-MM-dd"
			const formattedDate = dateObj.toISOString().slice(0, 10);
			document.getElementById('expiration_date1').value = formattedDate;
			document.getElementById('description1').value = data.description;
			document.getElementById('payment_limit1').value = data.paymentLimit;
			document.getElementById('status1').value = data.status;


			openForm1();
			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/ma-giam-gia/edit_Discount?id=${ma}`;

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

function openForm() {
	document.getElementById("overlay-background").classList.add("active");
}

function closeForm() {
	document.getElementById("overlay-background").classList.remove("active");
}

function openForm1() {
	document.getElementById("overlay-background1").classList.add("active");
}

function closeForm1() {
	document.getElementById("overlay-background1").classList.remove("active");
}


function goBack() {
	window.history.back();
}

function Delete(ma) {
	if (confirm("Bạn có chắc chắn muốn xóa không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/ma-giam-gia?id=${ma}`;

		// Cấu hình yêu cầu với phương thức GET và URL
		xhr.open("DELETE", url, true);

		// Khi có phản hồi từ server
		xhr.onload = function() {
			if (xhr.status === 200) {
				alert("Xóa thành công .");


				setTimeout(function() {
					window.location.href = "http://localhost:8080/Spring-mvc/quan-tri/discount/danh-sach";
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
document.addEventListener("DOMContentLoaded", function() {
	const newNameInput = document.getElementById("discount_code");

	newNameInput.addEventListener("keyup", function() {
		const newName = newNameInput.value;
		const add = document.getElementById("add");

		fetch(`http://localhost:8080/Spring-mvc/quan-tri/ma-giam-gia/check-Discount-name`, {
			method: "POST",
			headers: {
				"Content-Type": "application/x-www-form-urlencoded",
			},
			body: new URLSearchParams({ new: newName }),
		})
			.then((response) => response.json()) // nhận object dạng { exists: true }
			.then((data) => {
				if (data.exists === true) {
					newNameInput.style.backgroundColor = "#FFB6C1";
					newNameInput.setAttribute(
						"title",
						"Giá trị mã code đã tồn tại. Vui lòng chọn mã khác."
					);
					add.disabled = true;
					add.style.backgroundColor = "gray";
				} else {
					newNameInput.style.backgroundColor = "transparent";
					newNameInput.removeAttribute("title");
					add.disabled = false;
					add.style.backgroundColor = "green";
				}
			})
			.catch((error) => console.error("Error:", error));
	});
});


document.addEventListener("DOMContentLoaded", function() {
	const today = new Date();
	const yyyy = today.getFullYear();
	const mm = String(today.getMonth() + 1).padStart(2, '0');
	const dd = String(today.getDate()).padStart(2, '0');
	const todayStr = `${yyyy}-${mm}-${dd}`;

	const input = document.getElementById("expiration_date1");
	input.value = todayStr;        // Gán mặc định là hôm nay
	input.min = todayStr;          // Không cho chọn ngày trước đó

	const input1 = document.getElementById("expiration_date");
	input1.value = todayStr;        // Gán mặc định là hôm nay
	input1.min = todayStr;          // Không cho chọn ngày trước đó
});
