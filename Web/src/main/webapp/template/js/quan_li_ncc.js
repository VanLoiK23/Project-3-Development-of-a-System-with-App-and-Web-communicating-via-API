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
			`http://localhost/DACS2/phantrang_NCC?page=${page}`,
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
				"http://localhost/DACS2/supplier/handle_empty_data_input",
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
			fetch("http://localhost/DACS2/supplier/search", {
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
	const url = `http://localhost:8080/Spring-mvc/quan-tri/nha-cung-cap/edit_NCC?id=${ma}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			const attribute = JSON.parse(xhr.responseText).exists;

			document.getElementById("id").value = attribute.id;
			document.getElementById("name").value = attribute.tenNhaCungCap;
			document.getElementById("address").value = attribute.diaChi;
			document.getElementById("email").value = attribute.email;
			document.getElementById("sdt").value = attribute.soDienThoai;

			document.querySelector(".form-container").style.display = "block";
			document.querySelector(".overlay-background").style.display = "block";

			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/nha-cung-cap/edit_NCC?id=${ma}`;
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

function add() {
	document.querySelector(".form-container1").style.display = "block";
	document.querySelector(".overlay-background1").style.display = "block";
}

function closeForm() {
	document.querySelector(".form-container").style.display = "none";
	document.querySelector(".overlay-background").style.display = "none";
}

function closeForm1() {
	document.querySelector(".form-container1").style.display = "none";
	document.querySelector(".overlay-background1").style.display = "none";
}

function goBack() {
	window.history.back();
}

function Delete(ma) {
	if (confirm("Bạn có chắc chắn muốn xóa không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/nha-cung-cap?id=${ma}`;

		// Cấu hình yêu cầu với phương thức GET và URL
		xhr.open("DELETE", url, true);

		// Khi có phản hồi từ server
		xhr.onload = function() {
			if (xhr.status === 200) {
				
				alert("Xóa thành công .");

				
				setTimeout(function() {
					window.location.href = "http://localhost:8080/Spring-mvc/quan-tri/supplier/danh-sach";
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
document.addEventListener('DOMContentLoaded', function() {
	const newNameInput = document.getElementById('new_name');

	newNameInput.addEventListener('keyup', function() {
		const newName = newNameInput.value;
		const add = document.getElementById('add');

		fetch(`http://localhost:8080/Spring-mvc/quan-tri/nha-cung-cap/check-NCC-name`, {
			method: 'POST',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded',
			},
			body: new URLSearchParams({ new: newName })
		})
			.then((response) => response.json()) // nhận object dạng { exists: true }
			.then((data) => {
				if (data.exists === true) {
					newNameInput.style.backgroundColor = '#FFB6C1';
					newNameInput.setAttribute('title', 'Giá trị thuộc tính đã tồn tại. Vui lòng chọn tên khác.');
					add.disabled = true;
					add.style.backgroundColor = 'gray';
				} else {
					newNameInput.style.backgroundColor = 'transparent';
					newNameInput.removeAttribute('title');
					add.disabled = false;
					add.style.backgroundColor = 'green';
				}
			})
			.catch(error => console.error('Error:', error));
	});
});

