/*document.addEventListener("DOMContentLoaded", function () {
  // Function to load the initial data
  function load() {
    phantrang(1); // Load page 1 initially
  }

  // Function to handle pagination using AJAX
  function phantrang(page) {
    const xhr = new XMLHttpRequest();
    xhr.open("GET", `http://localhost/DACS2/phantrang_LH?page=${page}`, true);
    xhr.onload = function () {
      if (xhr.status === 200) {
        // Update the content of the table with the new data
        document.getElementById("table").innerHTML = xhr.responseText;
      } else {
        console.error("An error occurred: " + xhr.statusText);
      }
    };
    xhr.onerror = function () {
      console.error("An error occurred while making the request.");
    };
    xhr.send();
  }

  // Load the first page when the page is ready
  load();

  // Event listener for pagination links
  document.addEventListener("click", function (event) {
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
*/
function closeForm() {
  document.querySelector(".form-container").style.display = "none";
  document.querySelector(".overlay-background").style.display = "none";
}
function show(ma) {
  const xhr = new XMLHttpRequest();
  const url = `http://localhost:8080/Spring-mvc/quan-tri/lien-he?id=${ma}`;


  xhr.open("GET", url, true);

  xhr.onload = function () {
    if (xhr.status === 200) {
      const attribute = JSON.parse(xhr.responseText);

      document.getElementById("content").value = attribute.content;

      document.querySelector(".form-container").style.display = "block";
      document.querySelector(".overlay-background").style.display = "block";
    }
  };

  // Xử lý lỗi trong quá trình gửi yêu cầu
  xhr.onerror = function () {
    alert("Không thể kết nối tới server.");
  };
  // Gửi yêu cầu
  xhr.send();
}
function closeForm1() {
  document.querySelector(".form-container1").style.display = "none";
  document.querySelector(".overlay-background1").style.display = "none";
}

function feedback(email,khachhang,id) {
  document.getElementById("email").value = email;
  document.getElementById("id").value = id;
  document.getElementById("khachhang").value = khachhang;


  document.querySelector(".form-container1").style.display = "block";
  document.querySelector(".overlay-background1").style.display = "block";
}

function Delete(ma) {
  if (confirm("Bạn có chắc chắn muốn xóa không?")) {
    const xhr = new XMLHttpRequest();
    const url = `http://localhost:8080/Spring-mvc/quan-tri/contact?id=${ma}`;

    // Cấu hình yêu cầu với phương thức GET và URL
    xhr.open("DELETE", url, true);

    // Khi có phản hồi từ server
    xhr.onload = function () {
      if (xhr.status === 200) {
		alert("Delete Successfully")
        location.reload();
      }
    };

    // Xử lý lỗi trong quá trình gửi yêu cầu
    xhr.onerror = function () {
      alert("Không thể kết nối tới server.");
    };

    // Gửi yêu cầu
    xhr.send();
  }
}
