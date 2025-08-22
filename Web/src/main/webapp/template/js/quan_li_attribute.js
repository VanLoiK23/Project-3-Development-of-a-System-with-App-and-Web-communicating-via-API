document.addEventListener("DOMContentLoaded", function() {
	// Function to load the initial data
	function load() {
		phantrang(1); // Load page 1 initially
	}

	// Function to handle pagination using AJAX
	function phantrang(page) {
		const xhr = new XMLHttpRequest();
		var valueFromPHP = "<?php echo $value; ?>";
		xhr.open(
			"GET",
			`http://localhost/DACS2/phantrang_attribute?page=${page}&ct=${valueFromPHP}`,
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
		const table_name = searchInput.getAttribute("name-table");

		if (search == "") {
			fetch(
				"http://localhost/DACS2/Attribute_sp/handle_empty_data_input/" +
				table_name,
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
			fetch("http://localhost/DACS2/Attribute_sp/search/" + table_name, {
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

const attribute = {
	xuatxu: "maxuatxu",
	hedieuhanh: "mahedieuhanh",
	thuonghieu: "mathuonghieu",
	mausac: "mamausac",
	dungluongram: "madungluongram",
	dungluongrom: "madungluongrom",
};

const attribute_value = {
	xuatxu: "tenXuatXu",
	hedieuhanh: "tenHeDieuHanh",
	thuonghieu: "tenThuongHieu",
	mausac: "tenMauSac",
	dungluongram: "kichThuocRam",
	dungluongrom: "kichThuocRom",
};

const attribute_value_th = {
	thuonghieu: "image",
};
function show(name_table, ma) {
	const xhr = new XMLHttpRequest();
	const url = `http://localhost:8080/Spring-mvc/quan-tri/thuoc-tinh/edit_Attribute?type=${name_table}&id=${ma}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			const attributeData = JSON.parse(xhr.responseText).exists;


			const attributeKey = attribute[name_table];
			const attributeValueKey = attribute_value[name_table];
			const attributeValueThKey = attribute_value_th[name_table];

			// Gán giá trị vào các trường input
			document.getElementById("id").value = ma || "";
			document.getElementById("value").value = attributeData[attributeValueKey];
			const element = document.getElementById('preview');

			if (element) {
				element.src = attributeData[attributeValueThKey] || "";
			};

			document.querySelector(".form-container").style.display = "block";
			document.querySelector(".overlay-background").style.display = "block";

			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/thuoc-tinh/edit_Attribute?ct=${name_table}&id=${ma}`;
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
	// Lấy giá trị của các input để kiểm tra xem người dùng đã nhập gì chưa
	const inputValue = document.getElementById("new_name").value;
	const fileInput = document.getElementById("img1");

	// Nếu có dữ liệu được nhập
	if (inputValue !== "" || fileInput.value !== "") {
		const confirmClose = confirm("Dữ liệu chưa được lưu, bạn có chắc chắn muốn thoát không?");
		if (!confirmClose) {
			return; // Nếu người dùng chọn "Không", không làm gì cả
		}
	}

	// Ẩn form và overlay
	document.querySelector(".form-container1").style.display = "none";
	document.querySelector(".overlay-background1").style.display = "none";

	// Reset input text
	document.getElementById("new_name").value = "";

	// Reset input file
	if (fileInput) {
		fileInput.value = "";
	}

	// Ẩn ảnh preview (nếu có)
	const previewImage = document.getElementById("preview1");
	if (previewImage) {
		previewImage.style.display = "none";
		previewImage.src = "";
	}
}


function goBack() {
	window.history.back();
}

function Delete(table_name, ma) {
	if (confirm("Bạn có chắc chắn muốn xóa không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/thuoc-tinh?type=${table_name}&id=${ma}`;

		xhr.open("DELETE", url, true);

		// Khi có phản hồi từ server
		xhr.onload = function() {
			if (xhr.status === 200) {
				alert("Xóa thành công .");

				location.reload();
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
document.addEventListener("DOMContentLoaded", function() {
	const newNameInput = document.getElementById("new_name");

	newNameInput.addEventListener("keyup", function() {
		const newName = newNameInput.value;
		const ct = newNameInput.getAttribute("category");
		const add = document.getElementById("add");

		fetch(`http://localhost:8080/Spring-mvc/quan-tri/thuoc-tinh/check-attribute-name?type=${ct}`, {
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
						"Giá trị thuộc tính đã tồn tại. Vui lòng chọn tên khác."
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

document.addEventListener('DOMContentLoaded', function() {
	document.getElementById('img').addEventListener('change', function(event) {
		const file = event.target.files[0];
		if (file && file.type.startsWith('image/')) {
			const reader = new FileReader();
			reader.onload = function(e) {
				const preview = document.getElementById('preview');
				preview.src = e.target.result;
				preview.style.display = 'block';
			};
			reader.readAsDataURL(file);
		} else {
			alert('Vui lòng chọn một tệp ảnh hợp lệ.');
		}
	});
});

document.addEventListener('DOMContentLoaded', function() {
	document.getElementById('img1').addEventListener('change', function(event) {
		const file = event.target.files[0];
		if (file && file.type.startsWith('image/')) {
			const reader = new FileReader();
			reader.onload = function(e) {
				const preview = document.getElementById('preview1');
				preview.src = e.target.result;
				preview.style.display = 'block';
			};
			reader.readAsDataURL(file);
		} else {
			alert('Vui lòng chọn một tệp ảnh hợp lệ.');
		}
	});
});
