let selectedRating = 0;
const stars = document.querySelectorAll(".star");

document.addEventListener("DOMContentLoaded", () => {
  // Handle star rating

  stars.forEach((star) => {
    // Khi nhấp chuột để chọn
    star.addEventListener("click", function (event) {
      const rect = this.getBoundingClientRect(); //thông tin về vị trí và kích thước của phần tử sao;
      const mouseX = event.clientX - rect.left; //oán vị trí của con trỏ chuột;
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

  let submitBtn = document.getElementById("btn-submit");

  submitBtn.addEventListener("click", () => {
    if (selectedRating == 0) {
      alert("Hãy chọn đánh giá sao!");
      return;
    }
    const name = document.getElementById("name").value.trim();
    const comment = document.getElementById("comment").value.trim();
    const id = document.getElementById("id").value;

    if (!name || !comment) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
    }

    const formData = new FormData();
    formData.append("rating", selectedRating);
    formData.append("id", id);
    formData.append("comment", comment);

    fetch("http://localhost:8080/Spring-mvc/bai-viet", {
      method: "POST",
      body: formData,
    })
      .then((response) => response.json())
      .then((data) => {
        document.querySelectorAll(".star").forEach((star) => {
          star.classList.remove("full", "half");
        });
		
		console.log(data)

        document.getElementById("comment").value = "";
        if (data.content!=null) {
          alert("Đánh giá của bạn đã được gửi thành công!");

          var form = document.getElementById("comment-container");

          // Tạo phần tử div cho comment-item
          var commentItem = document.createElement("div");
          commentItem.classList.add("comment-item");

          // Tạo phần tử div cho comment-author
          var commentAuthor = document.createElement("div");
          commentAuthor.classList.add("comment-author");

          // Tạo phần tử span cho tên tác giả
          var authorName = document.createElement("span");
          authorName.classList.add("author-name");
          authorName.textContent = data.nameClient; // Thay đổi `data.user_name` theo dữ liệu từ server

          // Tạo phần tử span cho ngày đánh giá
          var commentDate = document.createElement("span");
          commentDate.classList.add("comment-date");
          commentDate.textContent = data.ngayDanhGiaString; // Thay đổi `data.ngaydanhgia` theo dữ liệu từ server

          // Thêm tên tác giả và ngày đánh giá vào comment-author
          commentAuthor.appendChild(authorName);
          commentAuthor.appendChild(commentDate);

          // Tạo phần tử p cho nội dung bình luận
          var commentContent = document.createElement("p");
          commentContent.classList.add("comment-content");
          commentContent.textContent = data.content; // Thay đổi `data.content` theo dữ liệu từ server

          // Thêm comment-author và commentContent vào commentItem
          commentItem.appendChild(commentAuthor);
          commentItem.appendChild(commentContent);

          // Thêm commentItem vào form (comment-container)
          form.insertBefore(commentItem, form.firstChild);

          var form1 = document.getElementById("reviewContent");

          // Tạo một phần tử div mới cho review-item
          var reviewItem = document.createElement("div");
          reviewItem.classList.add("review-item");

          // Tạo phần tử review-name và gán tên người dùng từ server
          var reviewName = document.createElement("div");
          reviewName.classList.add("review-name");
          reviewName.textContent = data.nameClient; // Tên người dùng từ server

          // Tạo phần tử review-rating và hiển thị đánh giá sao
          var reviewRating = document.createElement("div");
          reviewRating.classList.add("review-rating");
          var rating = data.rating;

          // Lặp qua số lượng sao để tạo các sao và nửa sao
          for (var i = 1; i <= 5; i++) {
            var star = document.createElement("i");
            star.style.color = "orange";
            if (rating >= i) {
              star.classList.add("fa", "fa-star");
            } else if (rating >= i - 0.5) {
              star.classList.add("fa-solid", "fa-star-half-stroke");
            } else {
              star.classList.add("fa", "fa-star-o");
            }
            reviewRating.appendChild(star);
          }

          // Tạo phần tử review-text và gán nội dung bình luận từ server
          var reviewText = document.createElement("div");
          reviewText.classList.add("review-text");
          reviewText.textContent = data.content; // Nội dung bình luận từ server

          // Thêm các phần tử vào reviewItem
          reviewItem.appendChild(reviewName);
          reviewItem.appendChild(reviewRating);
          reviewItem.appendChild(reviewText);

          // Thêm reviewItem vào form1 (ở đầu hoặc cuối tùy ý)
          form1.insertBefore(reviewItem, form1.firstChild);
        } else {
          alert(data.message);
          // alert("Có lỗi xảy ra khi gửi đánh giá.");
          // alert(data.message);
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        alert("Không thể gửi đánh giá. Vui lòng thử lại.");
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
