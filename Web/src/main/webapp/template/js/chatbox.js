/*chat box*/
const fixedResponses = {
    "Xin chào": "Chào mừng bạn đến với cửa hàng kinh doanh quần áo của chúng tôi! Bạn có cần giúp gì không?",
    "Hello": "Chào mừng bạn đến với cửa hàng kinh doanh quần áo của chúng tôi! Bạn có cần giúp gì không?",
    "Bạn là cái gì?": "Tôi là chatbox được sinh ra để phục vụ bạn.",
    "Tôi có thể liên lạc trợ giúp qua đâu": "huynhvanloi956@gmail.com.",
    "Cảm ơn bạn": "Không có gì ! Tôi được sinh ra để phục vụ bạn mà",
    "Cửa hàng của bạn đang bán những loại quần áo nào": "Các mặt hàng quần áo chúng tôi, vê quần áo nam,nữ,trẻ em và đặc biệt các phụ kiện thời trang mùa hè",
    "Tôi muốn biết về quần áo nam": "Chúng tôi hiện đang có quần áo phông thoải mái, thoáng mát và áo khoác dành cho nam giới",
    "Tôi muốn biết về quần áo nữ": "Chúng tôi hiện đang có quần áo Blouse cho nữ và đầm, váy ngắn thời trang",
    "Tôi muốn biết về quần áo trẻ em": "Chúng tôi có quần áo dành cho bé gái và bé trai, với những thiết kế độc đáo từ những nhà thiết kế hàng đầu thế giới",
    "Tôi muốn biết về phụ kiện": "Chúng tôi hiện đang có các phụ kiện về kính mắt thời trang và mũ sành điệu",
    "Quản trị viên của trang web này?": "Chúng tôi đang được vận hành bởi nhà thành lập: 'HUỲNH VĂN LỢI'",
    "Lịch sử hình thành": "Vào năm 2010, cửa hàng quần áo của chúng tôi được thành lập từ niềm đam mê cháy bỏng với thời trang và mong muốn mang đến cho khách hàng những sản phẩm chất lượng, phong cách và độc đáo. ",
    "Giá dao động của áo phông nam": "Giá dao động từ 300.000đ đến 8.000.000đ",
    "Giá dao động của áo nữ": "Giá dao động từ 350.000đ đến 5.000.000đ",
    "Áo nam hot nhất": "Chúng tôi đang có mặt hàng hot nhất tháng 6 là: Áo sơ mi nam Slim dạo phố Mùa Thu",
    "Các chính sách ưu đãi": "Các ưu đãi như miễn phí ship khi tổng tiền trên 1.000.000đ, ưu đãi trong các dịp đặc biệt, đặc biệt là chính sách đổi trả thuận tiền đảm bảo quyền lời khách hàng."
};

function sendMessage() {
    const userInput = document.getElementById("user-input").value;
    if (userInput.trim() === "") return;

    displayMessage("You: " + userInput, 'user');

    const botResponse = fixedResponses[userInput] || "Xin lỗi! Tôi không hiểu bạn đang nói gì.";
    displayMessage("Bot: " + botResponse, 'bot');

    document.getElementById("user-input").value = "";
    scrollChatboxToBottom();
}

function displayMessage(message, sender) {
    const chatboxMessages = document.getElementById("chatbox-messages");
    const messageElement = document.createElement("div");
    messageElement.textContent = message;
    messageElement.classList.add('message', sender);
    chatboxMessages.appendChild(messageElement);

    scrollChatboxToBottom();
}

function scrollChatboxToBottom() {
    const chatboxBody = document.querySelector('.chatbox-body');
    chatboxBody.scrollTop = chatboxBody.scrollHeight;
}

function toggleChatbox() {
    const chatbox = document.getElementById("chatbox");
    if (chatbox.style.display === "none" || chatbox.style.display === "") {
        chatbox.style.display = "block";
    } else {
        chatbox.style.display = "none";
    }
}
