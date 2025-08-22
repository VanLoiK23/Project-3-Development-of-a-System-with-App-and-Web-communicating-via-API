/*document.addEventListener("DOMContentLoaded", function() {
	// Function to load the initial data
	function load() {
		phantrang(1); // Load page 1 initially
	}

	// Function to handle pagination using AJAX
	function phantrang(page) {
		const xhr = new XMLHttpRequest();
		xhr.open("GET", `http://localhost/DACS2/phantrang_Articles?page=${page}`, true);
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
});*/
/*document.addEventListener("DOMContentLoaded", function() {
	const searchInput = document.getElementById("search");

	searchInput.addEventListener("keyup", function() {
		const search = searchInput.value;

		if (search == "") {
			fetch(
				"http://localhost/DACS2/Articles/handle_empty_data_input",
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
			fetch("http://localhost/DACS2/Articles/search", {
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
});*/

function Delete(ma) {
	if (confirm("Bạn có chắc chắn muốn xóa không?")) {
		const xhr = new XMLHttpRequest();
		const url = `http://localhost:8080/Spring-mvc/quan-tri/bai-viet?id=${ma}`;

		xhr.open("DELETE", url, true);

		xhr.onload = function() {
			if (xhr.status === 200) {
				location.reload();
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
$(document).ready(function() {
	$('#summernote').summernote({
		height: 600, // Chiều cao của khung soạn thảo
		minHeight: 200, // Chiều cao tối thiểu
		maxHeight: 1000, // Chiều cao tối đa
		toolbar: [
			// Nhóm các chức năng chính
			['style', ['style']], // Bao gồm định dạng style (heading, bold, italic, underline, etc.)
			['font', ['bold', 'italic', 'underline', 'clear']], // Định dạng chữ đậm, nghiêng, gạch chân, xóa định dạng
			['fontname', ['fontname']], // Đổi font chữ
			['fontsize', ['fontsize']], // Kích thước chữ
			['color', ['color']], // Màu chữ và nền
			['para', ['ul', 'ol', 'paragraph']], // Định dạng đoạn văn, danh sách gạch đầu dòng và đánh số
			['height', ['height']], // Độ cao của dòng
			['table', ['table']], // Thêm bảng
			['insert', ['link', 'picture', 'video']], // Chèn link, hình ảnh, video
			['view', ['codeview', 'help']], // Chế độ toàn màn hình, chế độ xem mã nguồn, và trợ giúp
			['misc', ['undo', 'redo']] // Chức năng undo, redo
		],
		callbacks: {
			onChange: function(contents, $editable) {
				$('#htmlContent').val(contents);
			}
		}
	});
});