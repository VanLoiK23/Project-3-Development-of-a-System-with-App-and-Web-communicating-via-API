$(document).ready(function() {
	$("#password").keyup(function() {
		var password = $("#password").val();

		if (password.length < 6) {
			$("#password").css("background-color", "#FFB6C1");
			$("#password").attr("title", "Mật khẩu phải có ít nhất 6 ký tự!");
		} else {
			$("#password").removeAttr("title");
			$("#password").css("background-color", "transparent");
		}
	});
});
function togglePassword() {
	var passwordField = document.getElementById("password");
	var showPasswordCheckbox = document.getElementById("showPassword");

	if (passwordField.type === "password") {
		passwordField.type = "text";
		showPasswordCheckbox.classList.remove("bxs-lock-alt");
		showPasswordCheckbox.classList.add("bxs-show");
	} else {
		passwordField.type = "password";
		showPasswordCheckbox.classList.remove("bxs-show");
		showPasswordCheckbox.classList.add("bxs-lock-alt");
	}
}
flatpickr("#datepicker", {
	dateFormat: "Y-m-d",
	locale: "vi",
	minDate: "01-01-1970",
	maxDate: "31-12-2007",
});

// Biến toàn cục theo dõi tình trạng của từng trường
var isNameValid = true;
var isEmailValid = true;
var isPhoneValid = true;

function updateButtonState() {
	// Nếu có ít nhất một trường bị lỗi, khóa nút
	if (!isNameValid || !isEmailValid || !isPhoneValid) {
		$(".btn").css("background-color", "gray");
		$(".btn").css("pointer-events", "none");
	} else {
		$(".btn").css("background-color", "white");
		$(".btn").css("pointer-events", "auto");
	}
}

$(document).ready(function() {
	// Kiểm tra tên
	$("#first_name, #last_name").keyup(function() {
		var first_name = $("#first_name").val();
		var last_name = $("#last_name").val();
		var user_name = last_name + " " + first_name;

		$.post("http://localhost:8080/Spring-mvc/khach-hang/checkUser_name", { name: user_name }, function(data) {
			if (data) {
				$("#first_name, #last_name").css("background-color", "#FFB6C1");
				isNameValid = false; // Đánh dấu tên không hợp lệ
				$("#first_name, #last_name").attr(
					"title",
					"Tên đã tồn tại. Vui lòng chọn tên khác."
				);
			} else {
				$("#first_name, #last_name").css("background-color", "transparent");
				isNameValid = true; // Tên hợp lệ
				$("#first_name, #last_name").removeAttr("title");
			}
			updateButtonState(); // Cập nhật trạng thái nút
		});
	});

	// Kiểm tra email
	$("#email").keyup(function() {
		var email = $("#email").val();

		if (email !== "") {
			$.post("http://localhost:8080/Spring-mvc/khach-hang/checkAccountIsLock", { email: email, phone: null }, function(data) {
				if (data) {
					$("#email").css("background-color", "#FFB6C1");
					isEmailValid = false; // Đánh dấu email không hợp lệ
					$("#email").attr(
						"title",
						"Tài khoản có email này đã bị khóa, vui lòng chọn email khác để đăng ký"
					);
				} else {
					$("#email").css("background-color", "transparent");
					isEmailValid = true; // Email hợp lệ
					$("#email").removeAttr("title");
				}
				updateButtonState(); // Cập nhật trạng thái nút
			});

			if (isEmailValid) {

				$.post("http://localhost:8080/Spring-mvc/khach-hang/checkEmail", { email: email }, function(data) {
					if (data) {
						$("#email").css("background-color", "#FFB6C1");
						isEmailValid = false; // Đánh dấu email không hợp lệ
						$("#email").attr(
							"title",
							"Tài khoản email này đã có , vui lòng chọn email khác để đăng ký"
						);
					} else {
						$("#email").css("background-color", "transparent");
						isEmailValid = true; // Email hợp lệ
						$("#email").removeAttr("title");
					}
					updateButtonState(); // Cập nhật trạng thái nút
				});
			}
		}

	});

	// Kiểm tra số điện thoại
	$("#phone").keyup(function() {
		var phone = $("#phone").val().trim();;

		if (phone !== "") {
			$.post("http://localhost:8080/Spring-mvc/khach-hang/checkAccountIsLock", { phone: phone, email: null }, function(data) {
				if (data) {
					$("#phone").css("background-color", "#FFB6C1");
					isPhoneValid = false; // Đánh dấu số điện thoại không hợp lệ
					$("#phone").attr(
						"title",
						"Tài khoản có số điện thoại này đã bị khóa, vui lòng chọn số khác để đăng ký"
					);
				} else {
					$("#phone").css("background-color", "transparent");
					isPhoneValid = true; // Số điện thoại hợp lệ
					$("#phone").removeAttr("title");
				}
				updateButtonState(); // Cập nhật trạng thái nút
			});
		}
	});
});
