document.addEventListener("DOMContentLoaded", function () {
  // Function to load the initial data
/*  function load() {
    phantrang(1); // Load page 1 initially
  }

  // Function to handle pagination using AJAX
  function phantrang(page) {
    const xhrSession = new XMLHttpRequest();
    xhrSession.open(
      "POST",
      "http://localhost/DACS2/Quanlicommentarticles_controller",
      true
    );
    xhrSession.setRequestHeader(
      "Content-Type",
      "application/x-www-form-urlencoded"
    );
    xhrSession.onload = function () {
      if (xhrSession.status !== 200) {
        console.error(
          "Failed to save page to session: " + xhrSession.statusText
        );
      }
    };
    xhrSession.send(`page=${page}`);

    const xhr = new XMLHttpRequest();
    xhr.open("GET", `http://localhost/DACS2/phantrang_CM_Articles?page=${page}`, true);
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
*/
  /*document
    .getElementById("applyFilters")
    .addEventListener("click", function () {
      // Get selected values from dropdowns
      const timeFilter = document.getElementById("timeFilter").value;
      const statusFilter = document.getElementById("statusFilter").value;

      // Send the selected filters to the server
      fetch("http://localhost/DACS2/phantrang_CM_Articles/select_CM", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `filter=${encodeURIComponent(
          timeFilter
        )}&status=${encodeURIComponent(statusFilter)}`,
      })
        .then((response) => {
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }
          return response.text(); // Expect plain HTML as response
        })
        .then((html) => {
          const tableElement = document.getElementById("ds");
          if (tableElement) {
            tableElement.innerHTML = html;
            // Update the table content

            document.querySelectorAll(".phantrang").forEach((element) => {
              element.style.display = "none";
            });
          }
        })
        .catch((error) => {
          console.error("Request failed:", error);
          alert("Đã xảy ra lỗi khi tải dữ liệu. Vui lòng thử lại.");
        });
    });*/
});

function Delete(ma) {
  if (confirm("Bạn có chắc chắn muốn xóa không?")) {
    const xhr = new XMLHttpRequest();
    const url = `http://localhost:8080/Spring-mvc/quan-tri/bai-viet/deleteComment?idComment=${ma}`;

    // Cấu hình yêu cầu với phương thức GET và URL
    xhr.open("DELETE", url, true);

    // Khi có phản hồi từ server
    xhr.onload = function () {
      if (xhr.status === 200) {
		alert("Delete Comment successlly")
     /*   location.reload();*/
		window.location.href="http://localhost:8080/Spring-mvc/quan-tri/QuanlicommentArticles_controller/danh-sach";
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
