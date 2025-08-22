document.addEventListener("DOMContentLoaded", function() {

	function load() {
		addHoverEvent(); // Load page 1 initially
	}

	function addHoverEvent() {
		document.querySelectorAll(".kho-row").forEach((row) => {
			row.addEventListener("mouseover", function() {
				const khoId = this.getAttribute("data-kho-id");
				const productDisplay = document.getElementById(`product_play_${khoId}`);

				if (productDisplay) {
					productDisplay.style.display = "block";
					productDisplay.style.pointerEvents = "none";

					const rect = this.getBoundingClientRect();
					productDisplay.style.top = `${window.scrollY + rect.bottom}px`;
					productDisplay.style.left = `${window.scrollX + rect.right - 830}px`;

					setTimeout(() => productDisplay.classList.add("active"), 10);
				}
			});

			row.addEventListener("mouseout", function() {
				const khoId = this.getAttribute("data-kho-id");
				const productDisplay = document.getElementById(`product_play_${khoId}`);

				if (productDisplay) {
					productDisplay.classList.remove("active");
					productDisplay.style.pointerEvents = "none";
					setTimeout(() => (productDisplay.style.display = "none"), 300);
				}
			});
		});
	}

	load(); // Load the initial data


});

document.addEventListener("DOMContentLoaded", function() {
	const searchInput = document.getElementById("search");

	searchInput.addEventListener("keyup", function() {
		const search = searchInput.value;

		if (search == "") {
			fetch("http://localhost:8080/Spring-mvc/quan-tri/khuvuckho/handle_empty_data_input", {
				method: "POST",
				headers: {
					"Content-Type": "application/x-www-form-urlencoded",
				},
				body: "empty_Input=" + encodeURIComponent(search),
			})
				.then((response) => response.text())
				.then((data) => {
					document.getElementById("ds").innerHTML = data;
				})
				.catch((error) => {
					console.error("Có lỗi xảy ra:", error);
				});
		} else {
			fetch("http://localhost:8080/Spring-mvc/quan-tri/khuvuckho/search", {
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
	const url = `http://localhost:8080/Spring-mvc/quan-tri/khu-vuc-kho/edit_Kho?id=${ma}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			const attribute = JSON.parse(xhr.responseText).exists;

			document.getElementById("id").value = attribute.id;
			document.getElementById("name").value = attribute.tenKhuVuc;
			document.getElementById("ghichu").value = attribute.ghiChu;

			document.querySelector(".form-container").style.display = "block";
			document.querySelector(".overlay-background").style.display = "block";

			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/khu-vuc-kho/edit_Kho?id=${ma}`;
			history.pushState(
				{
					path: newUrl,
				},
				"",
				newUrl
			);
		} else {
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
		const url = `http://localhost:8080/Spring-mvc/quan-tri/khu-vuc-kho?id=${ma}`;

		// Cấu hình yêu cầu với phương thức GET và URL
		xhr.open("DELETE", url, true);

		// Khi có phản hồi từ server
		xhr.onload = function() {
			if (xhr.status === 200) {
				alert("Xóa thành công .");

				setTimeout(function () {
				       window.location.href = "http://localhost:8080/Spring-mvc/quan-tri/khuvuckho/danh-sach";
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
	const newNameInput = document.getElementById("new_name");

	newNameInput.addEventListener("keyup", function() {
		const newName = newNameInput.value;
		const add = document.getElementById("add");

		fetch(`http://localhost:8080/Spring-mvc/quan-tri/khu-vuc-kho/check-kho-name`, {
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
						"Tên kho đã tồn tại. Vui lòng chọn tên khác."
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
