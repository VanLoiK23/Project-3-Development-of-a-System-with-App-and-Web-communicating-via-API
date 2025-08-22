// Other important pens.
// Map: https://codepen.io/themustafaomar/pen/ZEGJeZq
// Navbar: https://codepen.io/themustafaomar/pen/VKbQyZ

"use strict";

//Lay phan tu tuong ung
function $i(selector) {
	return document.querySelector(selector);
}

// Tim tat ca cac phan tu con cua noi dung
function find(el, selector) {
	let finded;
	return (finded = el.querySelector(selector)) ? finded : null;
}

// Tim cac phan tu anh em cua phan tu cu the e1
function siblings(el) {
	const siblings = [];
	for (let sibling of el.parentNode.children) {
		if (sibling !== el) {
			siblings.push(sibling);
		}
	}
	return siblings;
}

const showAsideBtn = $i(".show-side-btn");
const sidebar = $i(".sidebar");
const wrapper = $i("#wrapper");

showAsideBtn.addEventListener("click", function() {
	$i(`#${this.dataset.show}`).classList.toggle("show-sidebar");
	wrapper.classList.toggle("fullwidth");
});

// man hinh nho hon 767 sidebar mo rong
if (window.innerWidth < 767) {
	sidebar.classList.add("show-sidebar");
}

//nếu lớn hơn 767px, sidebar sẽ tự động đóng lại.
window.addEventListener("resize", function() {
	if (window.innerWidth > 767) {
		sidebar.classList.remove("show-sidebar");
	}
});

// dropdown menu in the side nav
var slideNavDropdown = $i(".sidebar-dropdown");

$i(".sidebar .categories").addEventListener("click", function(event) {
	//        event.preventDefault()
	// ngăn chặn tag a chuyển hướng
	//Kiểm tra xem phần tử được nhấn có phải là một thẻ <a> hay không.
	const isMenuLink =
		event.target.tagName === "A"
			? !!event.target.classList.contains("side-menu-header")
			: true; //Kiểm tra xem nó có lớp side-menu-header

	// để tìm phần tử cha gần nhất có lớp has-dropdown
	const item = event.target.closest(".has-dropdown");

	//không tìm thấy mục có lớp has-dropdown, hàm sẽ dừng lại
	if (!item) {
		return;
	}
	//Chuyển hướng nếu không phải là liên kết menu
	if (!isMenuLink) {
		//window.open(event.target.getAttribute('href'), '_blank').focus();
		return (window.location = event.target.getAttribute("href"));
	}

	//Mở hoặc đóng dropdown
	item.classList.toggle("opened");

	//Đóng các dropdown khác
	siblings(item).forEach((sibling) => {
		sibling.classList.remove("opened");
	});

	//Nếu mục có lớp opened, tìm phần tử con có lớp sidebar-dropdown và thêm lớp active để hiển thị dropdown.
	if (item.classList.contains("opened")) {
		const toOpen = find(item, ".sidebar-dropdown");

		if (toOpen) {
			toOpen.classList.add("active");
		}

		//Nếu không mở (khi nhấn vào liên kết menu), nó sẽ xóa lớp active khỏi các dropdown khác.
		siblings(item).forEach((sibling) => {
			const toClose = find(sibling, ".sidebar-dropdown");

			if (toClose) {
				toClose.classList.remove("active");
			}
		});
	} else if (isMenuLink) {
		find(item, ".sidebar-dropdown").classList.toggle("active");
	}
});

//Đóng sidebar khi nhấn nút đóng
$i(".sidebar .close-aside").addEventListener("click", function() {
	$i(`#${this.dataset.close}`).classList.add("show-sidebar");
	wrapper.classList.remove("margin");
});

// Global defaults

Chart.defaults.global.animation.duration = 2000; // Animation duration
Chart.defaults.global.title.display = false; // Remove title
Chart.defaults.global.defaultFontColor = "#71748c"; // Font color
Chart.defaults.global.defaultFontSize = 13; // Font size for every label

// Tooltip global resets
Chart.defaults.global.tooltips.backgroundColor = "#111827";
Chart.defaults.global.tooltips.borderColor = "blue";

// Gridlines global resets
Chart.defaults.scale.gridLines.zeroLineColor = "#3b3d56";
Chart.defaults.scale.gridLines.color = "#3b3d56";
Chart.defaults.scale.gridLines.drawBorder = false;

// Legend global resets
Chart.defaults.global.legend.labels.padding = 0;
Chart.defaults.global.legend.display = false;

// Ticks global resets
Chart.defaults.scale.ticks.fontSize = 12;
Chart.defaults.scale.ticks.fontColor = "#71748c";
Chart.defaults.scale.ticks.beginAtZero = false;
Chart.defaults.scale.ticks.padding = 10;

// Elements globals
Chart.defaults.global.elements.point.radius = 0;

// Responsivess
Chart.defaults.global.responsive = true;
Chart.defaults.global.maintainAspectRatio = false;

//The line chart

$.ajax({
	url: "http://localhost:8080/Spring-mvc/quan-tri/thong-ke/don-hang/week", // URL API của bạn
	method: "GET",
	dataType: "json",
	success: function(response) {
		// Lấy dữ liệu từ response
		const days = [
			"Chủ nhật",
			"Thứ 2",
			"Thứ 3",
			"Thứ 4",
			"Thứ 5",
			"Thứ 6",
			"Thứ 7",
		];
		const orders = [0, 0, 0, 0, 0, 0, 0];

		// response là 1 mảng các object { dayOf: "Thứ X", count: N }
		response.forEach(item => {
			const index = days.indexOf(item.dayOf);
			if (index !== -1) {
				orders[index] = item.count;
			}
		});

		// Kiểm tra nếu không có đơn hàng
		if (orders.every(order => order === 0)) {
			document.getElementById("myChart11").innerHTML =
				"Không có đơn hàng nào trong tuần này.";
			return;
		}

		if (myChart) {
			myChart.destroy();
		}

		// Vẽ biểu đồ đường
		var myChart = new Chart(document.getElementById("myChart11"), {
			type: "line", // Biểu đồ đường
			data: {
				labels: days, // Các ngày trong tuần
				datasets: [
					{
						label: "Số đơn hàng",
						data: orders, // Dữ liệu số lượng đơn hàng
						borderColor: "#0d6efd", // Màu đường
						backgroundColor: "rgba(13, 110, 253, 0.2)", // Màu nền trong suốt dưới đường
						borderWidth: 2, // Độ dày đường
						pointBackgroundColor: "#0d6efd", // Màu điểm dữ liệu
						pointBorderColor: "#fff", // Màu viền điểm
						pointRadius: 5, // Kích thước điểm
						fill: true, // Tô nền dưới đường
					},
				],
			},
			options: {
				responsive: true,
				scales: {
					yAxes: [
						{
							beginAtZero: true, // Bắt đầu trục Y từ 0
							stepSize: 1, // Bước nhảy giữa các giá trị (số nguyên)
							ticks: {
								callback: function(value) {
									return Math.floor(value) === value ? value : ""; // Lọc bỏ số thập phân
								},
								suggestedMin: 0, // Giới hạn giá trị min
								suggestedMax: Math.max(...orders), // Giới hạn giá trị max
							}
						},
					],
					xAxes: [
						{
							gridLines: {
								display: false,
							},
						},
					],
				},
				tooltip: {
					enabled: true, // Bật tooltip
					callbacks: {
						label: function(tooltipItem) {
							// Hiển thị số đơn hàng trong tooltip
							return `Số đơn hàng: ${tooltipItem.raw}`;
						},
					},
				},

				legend: {
					display: true, // Hiển thị chú thích
					labels: {
						fontColor: "white", // Màu chữ chú thích
					},
				},
			},
		});
	},
	error: function(xhr, status, error) {
		console.error("Lỗi khi lấy dữ liệu: ", error);
	},
});

$.ajax({
	url: "http://localhost:8080/Spring-mvc/quan-tri/thong-ke/doanh-thu/week", // URL API của bạn
	method: "GET",
	dataType: "json",
	success: function(response) {
		const days = [
			"Chủ nhật",
			"Thứ 2",
			"Thứ 3",
			"Thứ 4",
			"Thứ 5",
			"Thứ 6",
			"Thứ 7",
		];
		const revenue = [0, 0, 0, 0, 0, 0, 0];



		response.forEach(item => {
			const index = days.indexOf(item.dayOf);
			if (index !== -1) {
				revenue[index] = item.total;
			}
		});

		// Kiểm tra nếu không có đơn hàng
		if (revenue.every(rev => rev === 0)) {
			document.getElementById("myChart12").innerHTML =
				"Không có doanh thu trong tuần này.";
			return;
		}



		function formatCurrency(value) {
			return new Intl.NumberFormat("vi-VN", {
				style: "currency",
				currency: "VND",
			}).format(value);
		}


		if (myChart) {
			myChart.destroy();
		}
		var myChart = new Chart(document.getElementById("myChart12"), {
			type: "line",
			data: {
				labels: days,
				datasets: [
					{
						label: "Doanh thu",
						data: revenue,
						borderColor: "#28a745",
						backgroundColor: "rgba(40, 167, 69, 0.2)",
						borderWidth: 2,
						pointBackgroundColor: "#28a745",
						pointBorderColor: "#fff",
						pointRadius: 5,
						fill: true,
					},
				],
			},
			options: {
				responsive: true,
				scales: {
					yAxes: [
						{
							gridLines: {},
							stepSize: 5000, // Mỗi khoảng trên trục Y là 5 triệu
							ticks: {
								callback: function(value) {
									// Định dạng số thành tiền tệ
									return new Intl.NumberFormat("vi-VN", {
										style: "currency",
										currency: "VND", // Hiển thị tiền tệ Việt Nam Đồng
									}).format(value);
								},
							},
						},
					],
					xAxes: [
						{
							gridLines: {
								display: false,
							},
						},
					],
				},
				tooltips: {
					callbacks: {
						label: function(tooltipItem, data) {
							// Lấy dataset tương ứng (Phiếu xuất hoặc Phiếu nhập)
							const datasetLabel =
								data.datasets[tooltipItem.datasetIndex].label;
							const value = tooltipItem.yLabel; // Giá trị của dữ liệu
							// Định dạng tiền tệ cho tooltip
							return (
								datasetLabel +
								": " +
								new Intl.NumberFormat("vi-VN", {
									style: "currency",
									currency: "VND",
								}).format(value)
							);
						},
					},

				},
				legend: {
					display: true,
					labels: {
						fontColor: "white",
					},
				},
			},
		});
	},
	error: function(xhr, status, error) {
		console.error("Lỗi khi lấy dữ liệu: ", error);
	},
});

// The bar chart

//week
$.ajax({
	url: "http://localhost:8080/Spring-mvc/quan-tri/thong-ke/don-hang/month",
	method: "GET",
	dataType: "json",
	success: function(response) {
		// Danh sách tất cả các tuần trong tháng
		const allWeeks = ["Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4", "Tuần 5"];
		const phieuxuat = new Array(allWeeks.length).fill(0); // Mảng mặc định giá trị 0

		// Duyệt qua dữ liệu trả về và gán giá trị cho tuần tương ứng
		response.forEach(item => {
			const index = allWeeks.indexOf(item.dayOf);
			if (index !== -1) {
				phieuxuat[index] = item.count;
			}
		});

		// Kiểm tra nếu không có đơn hàng
		if (phieuxuat.every(rev => rev === 0)) {
			document.getElementById("myChart13").innerHTML =
				"Không có đơn hàng trong tháng này.";
			return;
		}


		// Vẽ biểu đồ
		var myChart = new Chart(document.getElementById("myChart13"), {
			type: "bar", // Loại biểu đồ: cột
			data: {
				labels: allWeeks, // Nhãn là tất cả các tuần
				datasets: [
					{
						label: "Số đơn hàng", // Chú thích
						data: phieuxuat, // Dữ liệu phiếu xuất
						backgroundColor: "#FF6384", // Màu cột
						borderColor: "transparent", // Viền cột
						borderWidth: 2.5,
						barPercentage: 0.3, // Kích thước cột
					},
				],
			},
			options: {

				scales: {
					yAxes: [
						{
							beginAtZero: true, // Bắt đầu từ 0
							ticks: {
								stepSize: 1, // Mỗi bước tăng 1 đơn vị
								callback: function(value) {
									return value; // Hiển thị đơn vị
								},
							},
						},
					],
					xAxes: [
						{
							gridLines: {
								display: false,
							},
						},
					],
				},
				plugins: {
					tooltip: {
						callbacks: {
							label: function(tooltipItem) {
								return `${tooltipItem.raw} đơn hàng`; // Định dạng tooltip
							},
						},
					},
				},
			},
		});
	},
	error: function(xhr, status, error) {
		console.error("Lỗi khi lấy dữ liệu: ", error);
	},
});


$.ajax({
	url: "http://localhost:8080/Spring-mvc/quan-tri/thong-ke/doanh-thu/month",
	method: "GET",
	dataType: "json",
	success: function(response) {
		// Danh sách tất cả các tuần trong tháng
		const allWeeks = ["Tuần 1", "Tuần 2", "Tuần 3", "Tuần 4", "Tuần 5"];
		const phieuxuat = new Array(allWeeks.length).fill(0); // Mảng mặc định giá trị 0

		// Duyệt qua dữ liệu trả về và gán giá trị cho tuần tương ứng
		response.forEach(item => {
			const index = allWeeks.indexOf(item.dayOf);
			if (index !== -1) {
				phieuxuat[index] = item.total;
			}
		});

		// Kiểm tra nếu không có đơn hàng
		if (phieuxuat.every(rev => rev === 0)) {
			document.getElementById("myChart14").innerHTML =
				"Không có doanh thu trong tháng này.";
			return;
		}


		// Vẽ biểu đồ
		var myChart = new Chart(document.getElementById("myChart14"), {
			type: "bar", // Loại biểu đồ: cột
			data: {
				labels: allWeeks, // Nhãn là tất cả các tuần
				datasets: [
					{
						label: "Doanh thu", // Chú thích
						data: phieuxuat, // Dữ liệu phiếu xuất
						backgroundColor: "#9966FF", // Màu cột
						borderColor: "transparent", // Viền cột
						borderWidth: 2.5,
						barPercentage: 0.3, // Kích thước cột
					},
				],
			},
			options: {

				scales: {
					yAxes: [
						{
							gridLines: {},
							stepSize: 5000, // Mỗi khoảng trên trục Y là 5 triệu
							ticks: {
								callback: function(value) {
									// Định dạng số thành tiền tệ
									return new Intl.NumberFormat("vi-VN", {
										style: "currency",
										currency: "VND", // Hiển thị tiền tệ Việt Nam Đồng
									}).format(value);
								},
							},
						},
					],
					xAxes: [
						{
							gridLines: {
								display: false,
							},
						},
					],
				},
				tooltips: {
					callbacks: {
						label: function(tooltipItem, data) {
							// Lấy dataset tương ứng (Phiếu xuất hoặc Phiếu nhập)
							const datasetLabel =
								data.datasets[tooltipItem.datasetIndex].label;
							const value = tooltipItem.yLabel; // Giá trị của dữ liệu
							// Định dạng tiền tệ cho tooltip
							return (
								datasetLabel +
								": " +
								new Intl.NumberFormat("vi-VN", {
									style: "currency",
									currency: "VND",
								}).format(value)
							);
						},
					},
				},
			},
		});
	},
	error: function(xhr, status, error) {
		console.error("Lỗi khi lấy dữ liệu: ", error);
	},
});


//year
$.ajax({
	url: "http://localhost:8080/Spring-mvc/quan-tri/thong-ke/nhap-xuat/year",
	method: "GET",
	dataType: "json",
	success: function(response) {
		// Xử lý dữ liệu từ response
		const months = [
			"January",
			"February",
			"March",
			"April",
			"May",
			"June",
			"July",
			"August",
			"September",
			"October",
			"November",
			"December",
		];
		const phieuxuat = new Array(12).fill(0); // Mảng 12 tháng
		const phieunhap = new Array(12).fill(0); // Mảng 12 tháng

		// Cập nhật dữ liệu phiếu xuất
		for (const [month, total] of Object.entries(response.phieuxuat)) {
			phieuxuat[month - 1] = total;
		}

		// Cập nhật dữ liệu phiếu nhập
		for (const [month, total] of Object.entries(response.phieunhap)) {
			phieunhap[month - 1] = total;
		}

		// Vẽ biểu đồ
		var myChart = new Chart(document.getElementById("myChart"), {
			type: "bar",
			data: {
				labels: months, // Mảng tháng
				datasets: [
					{
						label: "Xuất hàng",
						data: phieuxuat, // Dữ liệu phiếu xuất
						backgroundColor: "#0d6efd",
						borderColor: "transparent",
						borderWidth: 2.5,
						barPercentage: 0.4,
					},
					{
						label: "Nhập hàng",
						data: phieunhap, // Dữ liệu phiếu nhập
						backgroundColor: "#dc3545",
						borderColor: "transparent",
						borderWidth: 2.5,
						barPercentage: 0.4,
					},
				],
			},
			options: {
				scales: {
					yAxes: [
						{
							gridLines: {},
							stepSize: 5000, // Mỗi khoảng trên trục Y là 5 triệu
							ticks: {
								callback: function(value) {
									// Định dạng số thành tiền tệ
									return new Intl.NumberFormat("vi-VN", {
										style: "currency",
										currency: "VND", // Hiển thị tiền tệ Việt Nam Đồng
									}).format(value);
								},
							},
						},
					],
					xAxes: [
						{
							gridLines: {
								display: false,
							},
						},
					],
				},
				tooltips: {
					callbacks: {
						label: function(tooltipItem, data) {
							// Lấy dataset tương ứng (Phiếu xuất hoặc Phiếu nhập)
							const datasetLabel =
								data.datasets[tooltipItem.datasetIndex].label;
							const value = tooltipItem.yLabel; // Giá trị của dữ liệu
							// Định dạng tiền tệ cho tooltip
							return (
								datasetLabel +
								": " +
								new Intl.NumberFormat("vi-VN", {
									style: "currency",
									currency: "VND",
								}).format(value)
							);
						},
					},
				},
			},
		});
	},
	error: function(xhr, status, error) {
		console.error("Lỗi khi lấy dữ liệu: ", error);
	},
});

// var chart = new Chart(document.getElementById("myChart2"), {
//   type: "line",
//   data: {
//     labels: [
//       "January",
//       "February",
//       "March",
//       "April",
//       "May",
//       "June",
//       "August",
//       "September",
//     ],
//     datasets: [
//       {
//         label: "My First dataset",
//         data: [4, 20, 5, 20, 5, 25, 9, 18],
//         backgroundColor: "transparent",
//         borderColor: "#0d6efd",
//         lineTension: 0.4,
//         borderWidth: 1.5,
//       },
//       {
//         label: "Month",
//         data: [11, 25, 10, 25, 10, 30, 14, 23],
//         backgroundColor: "transparent",
//         borderColor: "#dc3545",
//         lineTension: 0.4,
//         borderWidth: 1.5,
//       },
//       {
//         label: "Month",
//         data: [16, 30, 16, 30, 16, 36, 21, 35],
//         backgroundColor: "transparent",
//         borderColor: "#f0ad4e",
//         lineTension: 0.4,
//         borderWidth: 1.5,
//       },
//     ],
//   },
//   options: {
//     scales: {
//       yAxes: [
//         {
//           gridLines: {
//             drawBorder: false,
//           },
//           ticks: {
//             stepSize: 12,
//           },
//         },
//       ],
//       xAxes: [
//         {
//           gridLines: {
//             display: false,
//           },
//         },
//       ],
//     },
//   },
// });

// The line chart
$.ajax({
	url: "http://localhost:8080/Spring-mvc/quan-tri/thong-ke/nhap-xuat/quarter", // Đường dẫn API
	method: "GET",
	dataType: "json",
	success: function(response) {
		const quarters = ["Q1", "Q2", "Q3", "Q4"]; // Danh sách quý
		const phieuxuat = new Array(4).fill(0);
		const phieunhap = new Array(4).fill(0);

		// Cập nhật dữ liệu phiếu xuất
		for (const [quarter, total] of Object.entries(response.phieuxuat)) {
			phieuxuat[quarter - 1] = total; // Trừ 1 vì quý bắt đầu từ 1
		}

		// Cập nhật dữ liệu phiếu nhập
		for (const [quarter, total] of Object.entries(response.phieunhap)) {
			phieunhap[quarter - 1] = total;
		}

		// Vẽ biểu đồ
		var myChart = new Chart(document.getElementById("myChart2"), {
			type: "bar",
			data: {
				labels: quarters,
				datasets: [
					{
						label: "Xuất hàng",
						data: phieuxuat,
						backgroundColor: "#0d6efd",
						borderColor: "transparent",
						borderWidth: 2.5,
						barPercentage: 0.4,
					},
					{
						label: "Nhập hàng",
						data: phieunhap,
						backgroundColor: "#dc3545",
						borderColor: "transparent",
						borderWidth: 2.5,
						barPercentage: 0.4,
					},
				],
			},
			options: {
				scales: {
					yAxes: [
						{
							gridLines: {},
							stepSize: 10000,
							ticks: {
								// Chia trục Y theo khoảng tiền
								callback: function(value) {
									return new Intl.NumberFormat("vi-VN", {
										style: "currency",
										currency: "VND",
									}).format(value);
								},
							},
						},
					],
					xAxes: [
						{
							gridLines: {
								display: false,
							},
						},
					],
				},
				tooltips: {
					callbacks: {
						label: function(tooltipItem, data) {
							// Lấy dataset tương ứng (Phiếu xuất hoặc Phiếu nhập)
							const datasetLabel =
								data.datasets[tooltipItem.datasetIndex].label;
							const value = tooltipItem.yLabel; // Giá trị của dữ liệu
							// Định dạng tiền tệ cho tooltip
							return (
								datasetLabel +
								": " +
								new Intl.NumberFormat("vi-VN", {
									style: "currency",
									currency: "VND",
								}).format(value)
							);
						},
					},
				},
			},
		});
	},
	error: function(xhr, status, error) {
		console.error("Lỗi khi lấy dữ liệu: ", error);
	},
});

// var chart = document.getElementById("chart3");
// var myChart = new Chart(chart, {
//   type: "line",
//   data: {
//     labels: ["One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight"],
//     datasets: [
//       {
//         label: "Lost",
//         lineTension: 0.2,
//         borderColor: "#d9534f",
//         borderWidth: 1.5,
//         showLine: true,
//         data: [3, 30, 16, 30, 16, 36, 21, 40, 20, 30],
//         backgroundColor: "transparent",
//       },
//       {
//         label: "Lost",
//         lineTension: 0.2,
//         borderColor: "#5cb85c",
//         borderWidth: 1.5,
//         data: [6, 20, 5, 20, 5, 25, 9, 18, 20, 15],
//         backgroundColor: "transparent",
//       },
//       {
//         label: "Lost",
//         lineTension: 0.2,
//         borderColor: "#f0ad4e",
//         borderWidth: 1.5,
//         data: [12, 20, 15, 20, 5, 35, 10, 15, 35, 25],
//         backgroundColor: "transparent",
//       },
//       {
//         label: "Lost",
//         lineTension: 0.2,
//         borderColor: "#337ab7",
//         borderWidth: 1.5,
//         data: [16, 25, 10, 25, 10, 30, 14, 23, 14, 29],
//         backgroundColor: "transparent",
//       },
//     ],
//   },
//   options: {
//     scales: {
//       yAxes: [
//         {
//           gridLines: {
//             drawBorder: false,
//           },
//           ticks: {
//             stepSize: 12,
//           },
//         },
//       ],
//       xAxes: [
//         {
//           gridLines: {
//             display: false,
//           },
//         },
//       ],
//     },
//   },
// });

$.ajax({
	url: "http://localhost:8080/Spring-mvc/quan-tri/thong-ke/san-pham/top",
	method: "GET",
	dataType: "json",
	success: function(response) {
		const productNames = response.map((item) => item.name); // Lấy tên sản phẩm
		const totalSold = response.map((item) => item.soluongban); // Lấy số lượng bán
		const imgUrls = response.map((item) => item.img);

		// Vẽ biểu đồ tròn
		var myChart = new Chart(document.getElementById("myPieChart"), {
			type: "pie",
			data: {
				labels: productNames, // Tên sản phẩm
				datasets: [
					{
						data: totalSold, // Số lượng bán
						backgroundColor: [
							"#FF6384",
							"#36A2EB",
							"#FFCE56",
							"#4BC0C0",
							"#9966FF",
						], // Màu sắc
					},
				],
			},
			options: {
				responsive: true,
				plugins: {
					legend: {
						position: "top",
					},
					tooltip: {
						callbacks: {
							label: function(tooltipItem) {
								const value = tooltipItem.raw;
								const index = tooltipItem.dataIndex; // Lấy chỉ số của phần tử đang hover
								const imgUrl = imgUrls[index]; // Lấy URL hình ảnh tương ứng với sản phẩm
								return `${tooltipItem.label}: ${value} sản phẩm
                        <img src="${imgUrl}" style="width: 50px; height: 50px;">`;
							},
						},
					},
				},
			},
		});
	},
	error: function(xhr, status, error) {
		console.error("Lỗi khi lấy dữ liệu: ", error);
	},
});
