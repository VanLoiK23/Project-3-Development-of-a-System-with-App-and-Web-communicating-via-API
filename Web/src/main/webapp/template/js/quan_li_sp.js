document.addEventListener("DOMContentLoaded", function() {
	const searchInput = document.getElementById("search");
	let timer; // Biến debounce

	searchInput.addEventListener("keyup", function() {
		clearTimeout(timer); // Hủy request cũ nếu có
		const search = searchInput.value.trim();

		if (search === "") {
			location.reload();
			$("#pagination").show();
		} else {
			timer = setTimeout(() => { // Đợi 500ms mới gửi request
				$.ajax({
					url: "http://localhost:8080/Spring-mvc/quan-tri/san-pham/search",
					type: "GET",
					data: { name: search },
					dataType: "json",
					success: function(data) {
						let tableBody = $("#productTable #dssp");
						tableBody.empty(); // Xóa dữ liệu cũ
						$("#pagination").hide();
						$("#pagination").twbsPagination("destroy"); // Xóa phân trang khi search


						if (data.length === 0) {
							tableBody.html(`
                                <tr>
                                    <td colspan="7" class="text-center">
                                        <span style="color:gray;">Không tìm thấy kết quả phù hợp.</span>
                                    </td>
                                </tr>
                            `);
							$("#pagination").hide();
							$("#pagination").twbsPagination("destroy");

							return;
						}

						let i = 0;

						data.forEach(item => {
							let imgUrl = item.hinhanh ? item.hinhanh.split(",")[0] : "";
							let disableUrl = `/Spring-mvc/quan-tri/Quanlisanpham_controller/status?type=disable&id=${item.masp}&page=${currentPage}`;
							let activeUrl = `/Spring-mvc/quan-tri/Quanlisanpham_controller/status?type=active&id=${item.masp}&page=${currentPage}`;
							let updateUrl = `/Spring-mvc/quan-tri/Quanlisanpham_controller/update_sp?id_sp=${item.masp}`;


							let existingRow = $(`#productTable tbody tr[data-id='${item.masp}']`);

							if (existingRow.length === 0) {
								let row = `
                                <tr data-id="${item.masp}" style="color: black">
                                    <td>${++i}</td>
                                    <td><img src="${imgUrl}" alt="Ảnh sản phẩm" style="width:50px;height:50px;" /></td>
                                    <td style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">${item.tensp}</td>
                                    <td>${item.soluongnhap}</td>
                                    <td>
                                        ${item.status == 1
										? `<a href='${disableUrl}'><i class='fa-regular fa-circle-check'></i></a>`
										: `<a href='${activeUrl}'><i class='fa-regular fa-circle-xmark'></i></a>`
									}
                                    </td>
                                    <td>
                                        <button type="button" style="background-color: #C71585" onclick="importt(${item.masp})">
                                            <a style="color: white"><i class='fa-solid fa-cart-shopping'></i></a>
                                        </button>
                                    </td>
                                    <td>
                                        <button type="button" onclick="show_details_sp(${item.masp})">
                                            <a style="color: white"><i class='fa-solid fa-eye'></i></a>
                                        </button>
                                        <button>
                                            <a style="color: white" href="${updateUrl}">
                                                <i class='fa-solid fa-pen-to-square'></i>
                                            </a>
                                        </button>
                                        <button onclick="Delete(${item.masp})">
                                            <i class='fa-solid fa-trash'></i>
                                        </button>
                                    </td>
                                </tr>
                            `;
								tableBody.append(row);
							}
						});
					},
					error: function(xhr, status, error) {
						console.error("Lỗi khi gọi API:", error);

						$("#pagination").hide();
						$("#pagination").twbsPagination("destroy");
						$("#productTable #dssp").html(`
                            <tr>
                                <td colspan="7" class="text-center">
                                    <span style="color:gray;">Không tìm thấy kết quả phù hợp.</span>
                                </td>
                            </tr>
                        `);
					}
				});
			}, 500); // Chỉ gửi request sau 500ms nếu không nhập tiếp
		}
	});
});


function show(name_table, ma) {
	const xhr = new XMLHttpRequest();
	const url = `http://localhost/DACS2/Attribute_sp/edit_Attribute?ct=${name_table}&id=${ma}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			const attributeData = JSON.parse(xhr.responseText);

			const attributeKey = attribute[name_table];
			const attributeValueKey = attribute_value[name_table];

			// Gán giá trị vào các trường input
			document.getElementById("id").value = attributeData[attributeKey] || "";
			document.getElementById("value").value = attributeData[attributeValueKey];

			document.querySelector(".form-container").style.display = "block";
			document.querySelector(".overlay-background").style.display = "block";

			const newUrl = `http://localhost/DACS2/Attribute_sp/edit_Attribute?ct=${name_table}&id=${ma}`;
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

function add(name_table) {
	const xhr = new XMLHttpRequest();
	const url = `http://localhost/DACS2/Attribute_sp/add_Attribute?ct=${name_table}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			document.querySelector(".form-container1").style.display = "block";
			document.querySelector(".overlay-background1").style.display = "block";

			const newUrl = `http://localhost/DACS2/Attribute_sp/add_Attribute?ct=${name_table}`;
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

function closeForm() {
	document.querySelector(".form-container").style.display = "none";
	document.querySelector(".overlay-background").style.display = "none";
	goBack();
}

function closeForm2() {

	let confirmClose = window.confirm("Dữ liệu chưa được lưu. Bạn có chắc chắn muốn đóng form?");

	// Nếu người dùng chọn "OK" (Đồng ý đóng)
	if (confirmClose) {
		document.querySelector(".form-container2").style.display = "none";
		document.querySelector(".overlay-background2").style.display = "none";

		document.getElementById('imeiInputsContainer').innerHTML = "";
		document.getElementById("tb").innerHTML = "";

		goBack(); // Gọi hàm goBack() nếu có
	}
}

function closeForm1() {
	document.querySelector(".form-container1").style.display = "none";
	document.querySelector(".overlay-background1").style.display = "none";
	const formContainer = document.querySelector(".form-container");
	formContainer.style.backgroundColor = "#ffffff";
	const images = document.querySelectorAll(".form-container img");
	images.forEach((img) => {
		img.style.filter = "none";
	});
	goBack();
}

function goBack() {
	window.history.back();
}

function Delete(ma) {
	if (confirm("Bạn có chắc chắn muốn xóa không?")) {
		fetch(`http://localhost:8080/Spring-mvc/quan-tri/san-pham?id=${ma}`, {
			method: "DELETE"
		})
			.then(response => {
				if (!response.ok) throw new Error("Lỗi server!");
				return response.text();
			})
			.then(data => {
				alert(data); // Hiển thị thông báo từ server
				location.reload(); // Tải lại trang sau khi xóa
			})
			.catch(error => alert("Không thể kết nối tới server: " + error.message));
	}
}

document.addEventListener("DOMContentLoaded", function() {
	const newNameInput = document.getElementById("new_name");

	newNameInput.addEventListener("keyup", function() {
		const newName = newNameInput.value;
		const ct = newNameInput.getAttribute("category");
		const add = document.getElementById("add");

		fetch(`http://localhost/DACS2/Attribute_sp/check_value?ct=${ct}`, {
			method: "POST",
			headers: {
				"Content-Type": "application/x-www-form-urlencoded",
			},
			body: new URLSearchParams({ new: newName }),
		})
			.then((response) => response.text())
			.then((data) => {
				if (data === "true") {
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

function show_details_sp(id) {
	event.preventDefault();

	const xhr = new XMLHttpRequest();
	const url = `http://localhost:8080/Spring-mvc/quan-tri/san-pham/show_details_sp?id=${id}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			const attribute = JSON.parse(xhr.responseText);

			// Gán giá trị vào các trường input
			document.getElementById("id").value = attribute.masp;
			let existingImages = attribute.hinhanh.split(",");
			document.getElementById("img").src = existingImages[0];
			document.getElementById("name").value = attribute.tensp;
			document.getElementById("xx").value = attribute.xuatxu;
			document.getElementById("dlp").value = attribute.dungluongpin;
			document.getElementById("size").value = attribute.manhinh;
			document.getElementById("cr_s").value = attribute.camerasau;
			document.getElementById("cr_tr").value = attribute.cameratruoc;
			document.getElementById("hdh").value = attribute.hedieuhanh;
			document.getElementById("th").value = attribute.thuongHieu;
			document.getElementById("version").value = attribute.phienbanhdh;
			document.getElementById("kho").value = attribute.khuvuckho;

			document.querySelector(".form-container").style.display = "block";
			document.querySelector(".overlay-background").style.display = "block";

			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/san-pham/show_details_sp?id=${id}`;
			history.pushState(
				{
					path: newUrl,
				},
				"",
				newUrl
			);
		}
	};

	// Xử lý lỗi trong quá trình gửi yêu cầu
	xhr.onerror = function() {
		alert("Không thể kết nối tới server.");
	};

	// Gửi yêu cầu
	xhr.send();
}

function show_config() {
	const id = document.getElementById("id").value;
	const xhr = new XMLHttpRequest();
	const url = `http://localhost:8080/Spring-mvc/quan-tri/san-pham/show_config_sp?id=${id}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			document.getElementById("list_config").innerHTML = xhr.responseText;

			const formContainer = document.querySelector(".form-container");
			formContainer.style.backgroundColor = "rgba(0, 0, 0, 0.1)";

			const inputs = document.querySelectorAll(".form-container input");
			const images = document.querySelectorAll(".form-container img");

			inputs.forEach((input) => {
				input.style.backgroundColor = "rgba(0, 0, 0, 0.1)";
				input.style.border = "0";
			});

			images.forEach((img) => {
				img.style.filter = "brightness(50%)";
			});

			document.querySelector(".form-container1").style.display = "block";
			document.querySelector(".overlay-background1").style.display = "block";

			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/san-pham/show_config_sp?id=${id}`;
			history.pushState(
				{
					path: newUrl,
				},
				"",
				newUrl
			);
		}
	};

	xhr.onerror = function() {
		alert("Không thể kết nối tới server.");
	};

	// Gửi yêu cầu
	xhr.send();
}

function importt(id) {
	const xhr = new XMLHttpRequest();
	const url = `http://localhost:8080/Spring-mvc/quan-tri/san-pham/import_sp?id=${id}`;

	xhr.open("GET", url, true);

	xhr.onload = function() {
		if (xhr.status === 200) {
			const response = JSON.parse(xhr.responseText);

			const attribute = response.details_sp;

			// Gán giá trị vào các trường input
			document.getElementById("id1").value = attribute.masp;
			let existingImages = attribute.hinhanh.split(",");
			document.getElementById("img1").src = existingImages[0];
			document.getElementById("name1").value = attribute.tensp;
			document.getElementById("dlp1").value = attribute.dungluongpin;
			document.getElementById("hdh1").value = attribute.hedieuhanh;
			document.getElementById("kho1").value = attribute.khuvuckho;

			const list_pb = response.list_pb;
			const selectElement = document.getElementById("import");

			selectElement.innerHTML = "";
			list_pb.forEach((pb) => {
				const option = document.createElement("option");
				option.value = pb.maphienbansp;
				option.setAttribute("id_pb_sp", pb.maphienbansp);
				option.setAttribute("data-ram", pb.ram || "Không");
				option.setAttribute("data-rom", pb.rom || "Không");
				option.setAttribute("data-gianhap", pb.gianhap);
				option.setAttribute("data-color", pb.mausac);

				const formattedPrice = new Intl.NumberFormat("vi-VN", {
					style: "currency",
					currency: "VND",
				}).format(pb.gianhap);

				// Kiểm tra giá trị kích thước RAM và ROM
				const ramText = pb.ram ? `${pb.ram}GB` : "Không";
				const romText = pb.rom ? `${pb.rom}GB` : "Không";

				option.textContent = `${ramText} - ${romText} - ${pb.mausac} - ${formattedPrice}`;

				selectElement.appendChild(option);
			});

			document.querySelector(".form-container2").style.display = "block";
			document.querySelector(".overlay-background2").style.display = "block";

			const newUrl = `http://localhost:8080/Spring-mvc/quan-tri/san-pham/import_sp?id=${id}`;
			history.pushState(
				{
					path: newUrl,
				},
				"",
				newUrl
			);
		}
	};

	// Xử lý lỗi trong quá trình gửi yêu cầu
	xhr.onerror = function() {
		alert("Không thể kết nối tới server.");
	};

	// Gửi yêu cầu
	xhr.send();
}



let rowCount = 0;

function addRow() {
	const selectElement = document.getElementById("import");
	const selectedOption = selectElement.options[selectElement.selectedIndex];
	const inputValue = document.getElementById("number").value;

	const ram = selectedOption.getAttribute("data-ram");
	const rom = selectedOption.getAttribute("data-rom");
	const gianhap = selectedOption.getAttribute("data-gianhap");
	const color = selectedOption.getAttribute("data-color");
	const id = selectedOption.getAttribute("id_pb_sp");

	if (!ram || !rom || !gianhap) {
		alert("Vui lòng chọn phiên bản cần nhập.");
		return;
	}
	if (!inputValue) {
		alert("Vui lòng nhập giá trị vào ô số lượng");
		return;
	}

	const formattedGianhap = parseInt(gianhap).toLocaleString('vi-VN', {
		style: 'currency',
		currency: 'VND'
	});

	rowCount++;

	const tableBody = document.getElementById("tb");

	// Kiểm tra hàng đã tồn tại chưa
	if (ram == 'Không' && rom == 'Không') {
		const existingRow = Array.from(tableBody.rows).find(row => {
			return row.cells[1].textContent === `${ram} ` &&
				row.cells[2].textContent === `${rom} ` &&
				row.cells[3].textContent === `${color}` &&
				row.cells[4].textContent === `${formattedGianhap}`;
		});

		// Nếu hàng tồn tại, cập nhật số lượng
		if (existingRow) {
			const quantityCell = existingRow.cells[5];
			quantityCell.textContent = parseInt(quantityCell.textContent) + parseInt(inputValue);
		} else {
			// Nếu không có hàng, tạo hàng mới
			const newRow = document.createElement("tr");

			newRow.innerHTML = `
        <td><input type="hidden" class="id" name="mapbsp[]" value="${id}">${rowCount}</td>
        <td><input type="hidden" class="ram" name="ram[]" value="${ram}">${ram} </td>
        <td><input type="hidden" class="rom" name="rom[]" value="${rom}">${rom} </td>
        <td><input type="hidden" class="color" name="color[]" value="${color}">${color}</td>
        <td><input type="hidden" class="price" name="price[]" value="${gianhap}">${formattedGianhap}</td>
        <td><input type="hidden" name="number[]" value="${inputValue}">${inputValue}</td>
    `;

			// Thêm sự kiện click để chọn option tương ứng
			newRow.addEventListener('click', function() {
				selectCorrespondingOption(selectedOption.value);
			});

			tableBody.appendChild(newRow);
		}

	} else {
		const existingRow = Array.from(tableBody.rows).find(row => {
			return row.cells[1].textContent === `${ram} GB` &&
				row.cells[2].textContent === `${rom} GB` &&
				row.cells[3].textContent === `${color}` &&
				row.cells[4].textContent === `${formattedGianhap}`;
		});

		// Nếu hàng tồn tại, cập nhật số lượng
		if (existingRow) {
			const quantityCell = existingRow.cells[5];
			quantityCell.textContent = parseInt(quantityCell.textContent) + parseInt(inputValue);
		} else {
			// Nếu không có hàng, tạo hàng mới
			const newRow = document.createElement("tr");

			newRow.innerHTML = `
        <td><input type="hidden" class="id" name="mapbsp[]" value="${id}">${rowCount}</td>
        <td><input type="hidden" class="ram" name="ram[]" value="${ram}">${ram} GB</td>
        <td><input type="hidden" class="rom" name="rom[]" value="${rom}">${rom} GB</td>
        <td><input type="hidden" class="color" name="color[]" value="${color}">${color}</td>
        <td><input type="hidden" class="price" name="price[]" value="${gianhap}">${formattedGianhap}</td>
        <td><input type="hidden" name="number[]" value="${inputValue}">${inputValue}</td>
    `;

			// Thêm sự kiện click để chọn option tương ứng
			newRow.addEventListener('click', function() {
				selectCorrespondingOption(selectedOption.value);
			});

			tableBody.appendChild(newRow);
		}

	}

	// Xóa giá trị ô nhập số lượng
	document.getElementById("number").value = '';
}

function selectCorrespondingOption(value) {
	const selectElement = document.getElementById("import");
	selectElement.value = value;
}

function generateIMEIInputs() {
    const tableBody = document.getElementById('tb');
    const imeiInputsContainer = document.getElementById('imeiInputsContainer');

    // Xóa nội dung cũ của imeiInputsContainer
    imeiInputsContainer.innerHTML = '';

    if (tableBody.rows.length === 0) {
        console.log("Bảng không có nội dung.");
        return; // Dừng hàm nếu không có hàng
    } else {
        // Lặp qua các hàng trong bảng để tạo input cho mỗi mã phiên bản
        Array.from(tableBody.rows).forEach((row, index) => {
            const hiddenInput = row.querySelector('input[type="hidden"].id');
            const hiddenInput1 = row.querySelector('input[type="hidden"].ram');
            const hiddenInput2 = row.querySelector('input[type="hidden"].rom');
            const hiddenInput3 = row.querySelector('input[type="hidden"].color');
            const hiddenInput4 = row.querySelector('input[type="hidden"].price');

            if (hiddenInput) {
                const id = hiddenInput.value;
                const litle = hiddenInput1.value + " GB-" + hiddenInput2.value + " GB-" +
                    hiddenInput3.value + "-" + hiddenInput4.value + " Đ";

                // Tạo một nhãn cho mỗi input IMEI
                const label = document.createElement('label');
                label.textContent = `Nhập IMEI cho mã phiên bản ${litle}:`;
                label.htmlFor = `imei_${id}`;

                // Tạo input để nhập IMEI
                const input = document.createElement('input');
                input.type = 'text';
                input.name = `imei[]`;
                input.className = `form-control imei-input`;
                input.id = `imei_${id}`;
                input.placeholder = `Nhập IMEI cho mã phiên bản ${id}`;
                input.required = true;
                input.maxLength = 15;
                input.minLength = 15;
                input.pattern = "^\\d{15}$";
                input.title = "IMEI phải có đúng 15 số";

                // Tạo phần hiển thị lỗi
                const errorMessage = document.createElement('span');
                errorMessage.className = 'error-message';
                errorMessage.style.color = 'red';
                errorMessage.style.display = 'none';
                errorMessage.textContent = "IMEI phải có đúng 15 số.";

                // Thêm sự kiện kiểm tra đầu vào
                input.addEventListener('input', validateIMEI);

                // Thêm nhãn, input và lỗi vào container
                imeiInputsContainer.appendChild(label);
                imeiInputsContainer.appendChild(input);
                imeiInputsContainer.appendChild(errorMessage);
                imeiInputsContainer.appendChild(document.createElement('br'));
            }
        });

        // Tạo nút submit
        const button = document.createElement('input');
        button.type = 'submit';
        button.name = 'submit';
        button.value = 'Nhập hàng';
        button.className = 'submit';
        button.id = 'submitIMEI';
        button.style.marginTop = '10px';
        button.disabled = true; // Ban đầu vô hiệu hóa nút

        imeiInputsContainer.appendChild(button);
        imeiInputsContainer.style.display = 'block';

        // Kiểm tra trạng thái nút submit khi nhập liệu
        checkSubmitButton();
    }
}

// Hàm kiểm tra IMEI hợp lệ
function validateIMEI(event) {
    const input = event.target;
    const errorMessage = input.nextElementSibling;
    
    if (/^\d{15}$/.test(input.value)) {
        errorMessage.style.display = 'none';
    } else {
        errorMessage.style.display = 'block';
    }

    // Cập nhật trạng thái của nút submit
    checkSubmitButton();
}

// Hàm kiểm tra tất cả các ô IMEI và kích hoạt nút submit nếu hợp lệ
function checkSubmitButton() {
    const inputs = document.querySelectorAll('.imei-input');
    const submitButton = document.getElementById('submitIMEI');
    let isValid = true;

    inputs.forEach(input => {
        if (!/^\d{15}$/.test(input.value)) {
            isValid = false;
        }
    });

    submitButton.disabled = !isValid; // Chỉ bật nút nếu tất cả IMEI hợp lệ
}


document.addEventListener('DOMContentLoaded', function() {
	document.getElementById('img').addEventListener('change', function(event) {
		const file = event.target.files[0];
		if (file && file.type.startsWith('image/')) {
			const reader = new FileReader();
			reader.onload = function(e) {
				const preview = document.getElementById('preview');
				const select = document.getElementById('select');

				preview.src = e.target.result;
				preview.style.display = 'block';
				select.style.marginLeft = '65px';
			};
			reader.readAsDataURL(file);
		} else {
			alert('Vui lòng chọn một tệp ảnh hợp lệ.');
		}
	});

	// Get the image and input elements
	const displayedImage = document.getElementById('displayedImage');
	const imageInput = document.getElementById('imageInput');

	// When the image is clicked, trigger the file input
	displayedImage.addEventListener('click', () => {
		imageInput.click();
	});

	// When a new file is selected, update the image source
	imageInput.addEventListener('change', (event) => {
		const file = event.target.files[0];
		if (file) {
			const reader = new FileReader();
			reader.onload = function(e) {
				displayedImage.src = e.target.result;
			};
			reader.readAsDataURL(file);
		}
	});

	const displayedImage1 = document.getElementById('displayedImage1');
	const imageInput1 = document.getElementById('imageInput1');

	// When the image is clicked, trigger the file input
	displayedImage1.addEventListener('click', () => {
		imageInput1.click();
	});

	// When a new file is selected, update the image source
	imageInput1.addEventListener('change', (event) => {
		const file = event.target.files[0];
		if (file) {
			const reader = new FileReader();
			reader.onload = function(e) {
				displayedImage1.src = e.target.result;
			};
			reader.readAsDataURL(file);
		}
	});

	const displayedImage2 = document.getElementById('displayedImage2');
	const imageInput2 = document.getElementById('imageInput2');

	// When the image is clicked, trigger the file input
	displayedImage2.addEventListener('click', () => {
		imageInput2.click();
	});

	// When a new file is selected, update the image source
	imageInput2.addEventListener('change', (event) => {
		const file = event.target.files[0];
		if (file) {
			const reader = new FileReader();
			reader.onload = function(e) {
				displayedImage2.src = e.target.result;
			};
			reader.readAsDataURL(file);
		}
	});
});


$(document).ready(function() {
	$('#summernote').summernote({
		height: 400, // Chiều cao của khung soạn thảo
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

document.addEventListener('DOMContentLoaded', function() {
	const addButton = document.querySelector('.fa-plus');
	const editButton = document.querySelector('.fa-file-pen');
	const deleteButton = document.querySelector('.fa-trash-can');
	const tbody = document.getElementById('tb');

	let selectedRow = null;

	// Thêm dữ liệu vào bảng
	addButton.addEventListener('click', function() {
		const ram = document.getElementById('ram').value;
		const rom = document.getElementById('rom').value;
		const color = document.getElementById('color').value;
		const price_n = document.getElementById('price_n').value;
		const price_x = document.getElementById('price_x').value;

		const formatted_N = new Intl.NumberFormat('vi-VN', {
			style: 'currency',
			currency: 'VND'
		}).format(price_n);
		const formatted_X = new Intl.NumberFormat('vi-VN', {
			style: 'currency',
			currency: 'VND'
		}).format(price_x);


		if (ram == '' && rom == '' && color && price_n && price_x) {
			const newRow = tbody.insertRow();

			newRow.innerHTML = `
                   <td>${tbody.rows.length}</td>
                   <td><input type="hidden" name="ram[]" value="">Không</td>
                   <td><input type="hidden" name="rom[]"value="">Không</td>
                   <td><input type="hidden" name="color[]" value="${color}">${color}</td>
                   <td><input type="hidden" name="price_n[]" value="${price_n}">${formatted_N}</td>
                   <td><input type="hidden" name="price_x[]" value="${price_x}">${formatted_X}</td>`;



			newRow.addEventListener('click', function() {
				if (selectedRow) {
					selectedRow.classList.remove('selected');
				}
				selectedRow = newRow;
				selectedRow.classList.add('selected');
				fillForm(selectedRow);
			});

			clearForm();
		} else if (ram && rom && color && price_n && price_x) {
			const newRow = tbody.insertRow();
			newRow.innerHTML = `
                   <td>${tbody.rows.length}</td>
                   <td><input type="hidden" name="ram[]" value="${ram.replace(" GB", "")}">${ram}</td>
                   <td><input type="hidden" name="rom[]"value="${rom.replace(" GB", "")}">${rom}</td>
                   <td><input type="hidden" name="color[]" value="${color}">${color}</td>
                   <td><input type="hidden" name="price_n[]" value="${price_n}">${formatted_N}</td>
                   <td><input type="hidden" name="price_x[]" value="${price_x}">${formatted_X}</td>`;



			newRow.addEventListener('click', function() {
				if (selectedRow) {
					selectedRow.classList.remove('selected');
				}
				selectedRow = newRow;
				selectedRow.classList.add('selected');
				fillForm(selectedRow);
			});

			clearForm();
		} else {
			alert('Vui lòng điền đầy đủ thông tin!');
		}
	});


	const existingRows = tbody.querySelectorAll('tr');
	existingRows.forEach(row => {
	    row.addEventListener('click', function () {
	        if (selectedRow) {
	            selectedRow.classList.remove('selected');
	        }
	        selectedRow = row;
	        selectedRow.classList.add('selected');
	        fillForm(selectedRow);
	    });
	});
	
	editButton.addEventListener('click', function() {
		// Lấy các giá trị từ ô nhập liệu
		const ram = document.getElementById('ram').value;
		const rom = document.getElementById('rom').value;
		const color = document.getElementById('color').value;
		const price_n = document.getElementById('price_n').value;
		const price_x = document.getElementById('price_x').value;


		if (ram == '' && rom == '' && color && price_n && price_x) {
			if (selectedRow) { // Kiểm tra xem đã chọn hàng chưa
				selectedRow.cells[1].innerHTML = `<input type="hidden" name="ram[]" value="">Không`;
				selectedRow.cells[2].innerHTML = `<input type="hidden" name="rom[]" value="">Không`;
				selectedRow.cells[3].innerHTML = `<input type="hidden" name="color[]" value="${color}">${color}`;
				selectedRow.cells[4].innerHTML = `<input type="hidden" name="price_n[]" value="${price_n}">${price_n}`;
				selectedRow.cells[5].innerHTML = `<input type="hidden" name="price_x[]" value="${price_x}">${price_x}`;

				clearForm(); // Xóa thông tin trong các ô nhập liệu
				selectedRow.classList.remove('selected'); // Bỏ chọn hàng đã sửa
				selectedRow = null; // Đặt lại selectedRow
			} else {
				alert('Vui lòng chọn hàng để sửa!'); // Nếu không có hàng nào được chọn
			}
		}
		else if (ram && rom && color && price_n && price_x) {
			if (selectedRow) { // Kiểm tra xem đã chọn hàng chưa
				selectedRow.cells[1].innerHTML = `<input type="hidden" name="ram[]" value="${ram.replace(" GB", "")}">${ram}`;
				selectedRow.cells[2].innerHTML = `<input type="hidden" name="rom[]" value="${rom.replace(" GB", "")}">${rom}`;
				selectedRow.cells[3].innerHTML = `<input type="hidden" name="color[]" value="${color}">${color}`;
				selectedRow.cells[4].innerHTML = `<input type="hidden" name="price_n[]" value="${price_n}">${price_n}`;
				selectedRow.cells[5].innerHTML = `<input type="hidden" name="price_x[]" value="${price_x}">${price_x}`;

				clearForm(); // Xóa thông tin trong các ô nhập liệu
				selectedRow.classList.remove('selected'); // Bỏ chọn hàng đã sửa
				selectedRow = null; // Đặt lại selectedRow
			} else {
				alert('Vui lòng chọn hàng để sửa!'); // Nếu không có hàng nào được chọn
			}
		} else {
			alert('Vui lòng điền đầy đủ thông tin!'); // Nếu các ô nhập liệu không đầy đủ
		}
	});


	// Xóa dữ liệu
	deleteButton.addEventListener('click', function() {
		if (confirm('Bạn có chắc chắn muốn xóa hàng này?')) {
			if (selectedRow) {
				tbody.deleteRow(selectedRow.rowIndex - 1);
				clearForm();
				selectedRow = null;
				alert('Xóa thành công!');
			} else {
				alert('Vui lòng chọn hàng để xóa!');
			}
		}
	});

	function fillForm(row) {
		document.getElementById('ram').value = row.cells[1].innerText;
		document.getElementById('rom').value = row.cells[2].innerText;
		document.getElementById('color').value = row.cells[3].innerText;
		document.getElementById('price_n').value = row.cells[4].innerText;
		document.getElementById('price_x').value = row.cells[5].innerText;
	}

	function clearForm() {
		document.getElementById('ram').value = '';
		document.getElementById('rom').value = '';
		document.getElementById('color').value = '';
		document.getElementById('price_n').value = '';
		document.getElementById('price_x').value = '';
	}
});

$(document).ready(function() {
    // Khi người dùng nhập vào ô input
    $("#name").on("keyup", function() {
        var productName = $(this).val();
        
        // Kiểm tra nếu tên sản phẩm không rỗng
        if (productName) {
            $.ajax({
                url: 'http://localhost:8080/Spring-mvc/quan-tri/san-pham/check-product-name', // Địa chỉ API kiểm tra tên sản phẩm
                type: 'GET',
                data: { tensp: productName },  // Gửi tên sản phẩm
                success: function(response) {
                    if (response.exists) {
                        // Nếu tên sản phẩm đã tồn tại, hiển thị thông báo và vô hiệu hóa nút
                        $("#error-message").text("Tên sản phẩm đã tồn tại").show();
                       /* $("#add-product-btn").prop("disabled", true);*/ // Vô hiệu hóa nút thêm sản phẩm
                        $("#name").addClass("error"); // Thêm lớp 'error' vào input để thay đổi nền
                    } else {
                        // Nếu tên sản phẩm chưa tồn tại, ẩn thông báo và kích hoạt nút
                        $("#error-message").hide();
                      /*  $("#add-product-btn").prop("disabled", false); */// Kích hoạt nút thêm sản phẩm
                        $("#name").removeClass("error"); // Xóa lớp 'error' khỏi input để phục hồi nền
                    }
                },
                error: function() {
                    // Nếu có lỗi trong yêu cầu Ajax, ẩn thông báo và kích hoạt nút
                    $("#error-message").hide();
                    /*$("#add-product-btn").prop("disabled", false);*/
                    $("#name").removeClass("error"); // Xóa lớp 'error' nếu có lỗi trong yêu cầu
                }
            });
        } else {
            // Nếu tên sản phẩm trống, ẩn thông báo và vô hiệu hóa nút
            $("#error-message").hide();
            $("#add-product-btn").prop("disabled", true);
            $("#name").removeClass("error"); // Xóa lớp 'error' nếu không có tên sản phẩm
        }
    });
});

