let selectedRating = 0;
const stars = document.querySelectorAll(".star");

document.addEventListener("DOMContentLoaded", () => {
	// Handle star rating


	stars.forEach((star) => {
		// Khi nhấp chuột để chọn
		star.addEventListener("click", function(event) {
			const rect = this.getBoundingClientRect();
			const mouseX = event.clientX - rect.left;
			const width = rect.width;

			selectedRating = parseInt(this.dataset.value); // Lấy giá trị sao

			// Nếu nhấp vào nửa đầu của sao thì giảm 0.5 điểm
			if (mouseX < width / 2) {
				selectedRating -= 0.5;
			}
			console.log(selectedRating);

			highlightStars(selectedRating);

		});
	});





	let submitBtn = document.getElementById("submit-btn");
	let updateBtn = document.getElementById("update-btn");
	const displayedImage = document.getElementById("displayedImage");
	const imageInput = document.getElementById("imageInput");

	// When the image is clicked, trigger the file input
	displayedImage.addEventListener("click", () => {
		imageInput.click();
	});

	// When a new file is selected, update the image source
	imageInput.addEventListener("change", (event) => {
		const file = event.target.files[0];
		if (file) {
			const reader = new FileReader();
			reader.onload = function(e) {
				displayedImage.src = e.target.result;
			};
			reader.readAsDataURL(file);
		}
	});

	submitBtn.addEventListener("click", () => {
		if (selectedRating == 0) {
			alert("Hãy chọn đánh giá sao!");
			return;
		}

		let reviewText = document.getElementById("reviewText");
		const text = reviewText.value;
		const file = imageInput.files[0];

		const masp = localStorage.getItem("temporaryKey");
		const maphieuxuat = localStorage.getItem("temporaryKey1");

		if (!file) {
			alert("Hãy tải lên một hình ảnh!");
			return;
		}

		const formData = new FormData();
		formData.append("rating", selectedRating);
		formData.append("masp", masp);
		formData.append("maphieuxuat", maphieuxuat);
		formData.append("review", text);
		formData.append("image", file);

		fetch("http://localhost:8080/Spring-mvc/Info-Individual/addComment", {
			method: "POST",
			body: formData,
		})
			.then((response) => response.json())
			.then((data) => {
				if (data.success) {
					alert("Đánh giá của bạn đã được gửi thành công!");
					document.querySelector(".review-form-container").style.display =
						"none";
						
					location.reload()
				} else {
					alert("Có lỗi xảy ra khi gửi đánh giá.");
				}
			})
			.catch((error) => {
				console.error("Error:", error);
				alert("Không thể gửi đánh giá. Vui lòng thử lại.");
			});
	});

	updateBtn.addEventListener("click", () => {
		let reviewText = document.getElementById("reviewText");
		const text = reviewText.value;
		const file = imageInput.files[0];

		const masp = localStorage.getItem("temporaryKey");
		const maphieuxuat = localStorage.getItem("temporaryKey1");

		const formData = new FormData();
		formData.append("masp", masp);
		formData.append("maphieuxuat", maphieuxuat);

		if (selectedRating) {
			formData.append("rating", selectedRating);
		}
		if (reviewText) {
			formData.append("review", text);
		}
		if (file) {
			formData.append("image", file);
		}

		fetch("http://localhost:8080/Spring-mvc/Info-Individual/addComment", {
			method: "POST",
			body: formData,
		})
			.then((response) => response.json())
			.then((data) => {
				if (data.success) {
					alert("Đánh giá của bạn đã được sửa thành công!");
					document.querySelector(".review-form-container").style.display =
						"none";
					location.reload()
				} else {
					/*alert("Đánh giá của bạn đã được sửa thành công!");
					document.querySelector(".review-form-container").style.display =
					  "none";*/
					alert("Có lỗi xảy ra khi gửi đánh giá.");
				}
			})
			.catch((error) => {
				console.error("Error:", error);
				alert("Đánh giá của bạn đã được sửa thành công!");
				document.querySelector(".review-form-container").style.display = "none";
			});
	});
});

// Làm nổi bật các ngôi sao dựa trên rating
function highlightStars(rating) {
	stars.forEach((star) => {
		const starValue = parseInt(star.dataset.value);
		if (rating >= starValue) {
			star.classList.add("full");
			star.classList.remove("half");
		} else if (rating >= starValue - 0.5) {
			star.classList.add("half");
			star.classList.remove("full");
		} else {
			star.classList.remove("full", "half");
		}
	});
}

function open_cmmt(masp, maphieuxuat) {
	const title = document.querySelector(".review-form h3");
	title.textContent = "Đánh giá sản phẩm"; // Đổi tiêu đề thành "Đánh giá sản phẩm"

	localStorage.setItem("temporaryKey", masp);
	localStorage.setItem("temporaryKey1", maphieuxuat);

	// Hiển thị form đánh giá
	document.querySelector(".review-form-container").style.display = "block";

	// Clear giá trị cũ khi mở form
	const reviewText = document.getElementById("reviewText");
	const displayedImage = document.getElementById("displayedImage");

	reviewText.value = "";

	stars.forEach((star) => {
		star.classList.remove("full", "half");
	});

	displayedImage.src =
		"https://i.ibb.co/hFQYvzZG/download.png";

	let submitButton = document.getElementById("submit-btn");

	// Ẩn phần tử submit-btn
	submitButton.style.display = "block";

	// Lấy phần tử update-btn
	let updateButton = document.getElementById("update-btn");

	// Hiển thị phần tử update-btn
	updateButton.style.display = "none";
}

function update_cmmt(masp, maphieuxuat) {
	// Lưu trữ thông tin vào localStorage (để dùng sau này nếu cần)
	localStorage.setItem("temporaryKey", masp);
	localStorage.setItem("temporaryKey1", maphieuxuat);

	// Hiển thị form đánh giá
	document.querySelector(".review-form-container").style.display = "block";

	// Fetch dữ liệu bình luận từ PHP, truyền masp và maphieuxuat qua URL hoặc body
	fetch(
		`http://localhost:8080/Spring-mvc/Info-Individual/show_comment?masp=${masp}&maphieuxuat=${maphieuxuat}`
	)
		.then((response) => response.json())
		.then((data) => {
			// Hiển thị bình luận trên form đánh giá
			displayComments(data);
		})
		.catch((error) => console.error("Error fetching comments:", error));

	function displayComments(comment) {
		const title = document.querySelector(".review-form h3");
		title.textContent = "Chỉnh sửa bình luận";

		// Xóa hết sao cũ trước khi cập nhật lại
		stars.forEach((star) => star.classList.remove("full", "half"));

		// Hiển thị tất cả các bình luận
		const reviewText = document.getElementById("reviewText");
		const displayedImage = document.getElementById("displayedImage");

		if (comment != null) {
			const firstComment = comment; // Nếu bạn muốn hiển thị bình luận đầu tiên

			// Cập nhật sao đã chọn
			selectedRating = firstComment.rating;

			// Hiển thị sao đã chọn trên form đánh giá
			highlightStars(selectedRating);

			// Cập nhật văn bản bình luận
			reviewText.value = firstComment.reviewText;

			// Cập nhật hình ảnh (nếu có)
			displayedImage.src = firstComment.imageUrl;

			let submitButton = document.getElementById("submit-btn");

			// Ẩn phần tử submit-btn
			submitButton.style.display = "none";

			// Lấy phần tử update-btn
			let updateButton = document.getElementById("update-btn");

			// Hiển thị phần tử update-btn
			updateButton.style.display = "block";
		} else {
			// Nếu không có bình luận nào
			reviewText.value = "";
			displayedImage.src = "default_image_url.jpg"; // Hình ảnh mặc định nếu không có
		}
	}
}

function close_cmmt() {
	document.querySelector(".review-form-container").style.display = "none";
}
function showSection(sectionId) {
	if (sectionId == "logout") {
		if (confirm("Bạn có muốn đăng xuất khỏi tài khoản không")) {
			const logoutUrl = "http://localhost:8080/Spring-mvc/logout";
			window.location.href = logoutUrl;
		}
	}
	// Ẩn tất cả các section
	const sections = document.querySelectorAll(".section");
	sections.forEach((section) => {
		section.classList.remove("active");
	});

	// Hiển thị section được chọn
	const selectedSection = document.getElementById(sectionId);
	if (selectedSection) {
		selectedSection.classList.add("active");
	}
}

function closePopup() {
	document.getElementById("cancelPopup").style.display = "none";
}
function cancle(maphieuxuat) {
	// Sử dụng confirm để hỏi người dùng
	var isConfirmed = confirm("Bạn có chắc chắn muốn hủy đơn hàng?");

	// Nếu người dùng xác nhận
	if (isConfirmed) {
		// Hiển thị popup để nhập lý do hủy
		document.getElementById("cancelPopup").style.display = "flex";

		// Lưu id đơn hàng vào window để sử dụng sau này
		window.maphieuxuat = maphieuxuat;
	}
}

function submitCancel() {
	var cancelReason = document.getElementById("cancelReason").value.trim();

	// Kiểm tra xem người dùng đã nhập lý do chưa
	if (cancelReason === "") {
		alert("Vui lòng nhập lý do hủy đơn hàng.");
		return;
	}

	// Nếu có lý do hủy, chuyển hướng với lý do hủy đã nhập
	window.location.href =
		"http://localhost:8080/Spring-mvc/account/huydonhang/" +
		window.maphieuxuat +
		"?reason=" +
		encodeURIComponent(cancelReason);
}

document.getElementById("timeFilter").addEventListener("change", function() {
	const selectedValue = this.value;

	// Send the selected filter to the server
	fetch("http://localhost:8080/Spring-mvc/Info-Individual/filterOrder?filter="+selectedValue, {
		method: "POST",
		headers: {
			"Content-Type": "application/x-www-form-urlencoded",
		},
/*		body: `filter=${encodeURIComponent(selectedValue)}`,
*/	})
		/*.then((response) => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.text(); // Expect plain HTML as response
		})*/
		.then(response => response.json())
		.then((data) => {
			const tableElement = document.getElementById("ds");
			   let html = "";
			   console.log("Dữ liệu nhận được từ server:", data);
			   console.log("Kiểu dữ liệu:", typeof data);
			   console.log("Số phần tử:", Array.isArray(data) ? data.length : "Không phải mảng");

			   if (Array.isArray(data) && data.length > 0) {
			     data.forEach(item => {
			       html += `
			         <tr>
			           <td>#84-${item.idkhachHang}${item.id}</td>
					   <td>${formatDate(item.thoiGian)}</td>
			           <td>${getStatusText(item.status)}</td>
			           <td>${formatCurrency(item.tongTien)} đ</td>
			           <td>
			             <button class='view-btn' style='background-color:#007bff' onclick="window.location.href='http://localhost:8080/Spring-mvc/account/donhang/${item.id}'">View</button>
			             ${item.status === 0 ? `<button class="view-btn" onclick="cancle(${item.id})">Hủy</button>` : ""}
			           </td>
			         </tr>
			       `;
			     });
			   } else {
			     html = `<tr style='border:0; height:300px'><td colspan='6' class='text-center'>
			       <img style='border:0;width:100%;height:250px !important;' src='https://i.ibb.co/1fjWK3sC/images.jpg' >
			     </td></tr>`;
			   }

			   tableElement.innerHTML = html;
		})
		.catch((error) => {
			console.error("Request failed:", error);
			alert("Đã xảy ra lỗi khi tải dữ liệu. Vui lòng thử lại.");
		});
});

function formatDate(timestamp) {
  const date = new Date(timestamp);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  const hour = String(date.getHours()).padStart(2, "0");
  const minute = String(date.getMinutes()).padStart(2, "0");
  const second = String(date.getSeconds()).padStart(2, "0");
  return `${day}/${month}/${year} ${hour}:${minute}:${second}`;
}


function getStatusText(status) {
  switch (status) {
    case 0: return "Đang chờ duyệt";
    case 1: return "Đang lấy hàng";
    case 2: return "Đang chờ giao hàng";
    case 3: return "Đang giao hàng";
    case 4: return "Đã giao";
    case -1: return "Nhân viên đã hủy";
    case -2: return "Đang chờ nhân viên duyệt đơn hủy";
    default: return "Bạn đã hủy";
  }
}

function formatCurrency(amount) {
  return amount.toLocaleString("vi-VN");
}

