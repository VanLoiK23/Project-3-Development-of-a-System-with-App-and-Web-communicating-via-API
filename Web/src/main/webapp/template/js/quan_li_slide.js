document.addEventListener("DOMContentLoaded", function() {
	function load() {
		phantrang(1); // Load page 1 initially
	}

	// Function to handle pagination using AJAX
	function phantrang(page) {
		const xhr = new XMLHttpRequest();
		xhr.open(
			"GET",
			`http://localhost/DACS2/phantrang_slide?page=${page}`,
			true
		);
		xhr.onload = function() {
			if (xhr.status === 200) {
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

function show(id) {
	const xhr = new XMLHttpRequest();
	const url = `http://localhost:8080/Spring-mvc/quan-tri/banner/edit_Slide?id=${id}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			const slideData = JSON.parse(xhr.responseText).exists;


			document.getElementById("id").value = slideData.id || "";
			document.getElementById('preview').src = slideData.image || "";

			document.querySelector(".form-container").style.display = "block";
			document.querySelector(".overlay-background").style.display = "block";

			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/banner/edit_Slide?id=${id}`;
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

function Delete(id) {
	if (confirm("Bạn có chắc chắn muốn xóa không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/banner?id=${id}`;

		xhr.open("DELETE", url, true);

		// Khi có phản hồi từ server
		xhr.onload = function() {
			if (xhr.status === 200) {
				alert("Xóa thành công .");


				setTimeout(function() {
					window.location.href = "http://localhost:8080/Spring-mvc/quan-tri/slide/danh-sach";
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
