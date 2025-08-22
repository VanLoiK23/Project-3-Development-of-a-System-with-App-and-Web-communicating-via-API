// Lấy giỏ hàng từ localStorage hoặc tạo mới nếu chưa tồn tại
let cart = JSON.parse(localStorage.getItem('cart')) || [];

// Hàm thêm sản phẩm vào giỏ hàng với số lượng
function addToCart(name, price, image, quantity) {
    const productIndex = cart.findIndex(item => item.name === name);
    if (productIndex > -1) {
        cart[productIndex].quantity += quantity;
    } else {
        cart.push({ name, price, image, quantity });
    }
    localStorage.setItem('cart', JSON.stringify(cart));
    alert('Sản phẩm đã được thêm vào giỏ hàng!');
}

// Hàm được gọi khi nhấn nút "Thêm vào giỏ hàng" trên trang sản phẩm
function addProductToCart() {
    const name = 'Tên sản phẩm';  // Lấy tên sản phẩm từ DOM nếu cần thiết
    const price = parseFloat(document.getElementById('product-price').textContent);
    const image = document.getElementById('product-image').src;
    const quantity = parseInt(document.getElementById('quantity').value);

    addToCart(name, price, image, quantity);
}

// Hàm định dạng số tiền thành VND
function formatCurrency(amount) {
    return amount.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
}

// Hàm hiển thị giỏ hàng
function displayCart() {
    const cartItemsContainer = document.getElementById('cart-items');
    const subtotalElement = document.getElementById('subtotal');
    const totalAmountElement = document.getElementById('total-amount');
    const free = document.getElementById('free');
    const fee = document.getElementById('fee');


    cartItemsContainer.innerHTML = '';
    let subtotal = 0;

    cart.forEach((item, index) => {
        const itemElement = document.createElement('tr');
        itemElement.innerHTML = `
            <td class="pro-thumbnail"><a href="#"><img class="img-fluid" src="${item.image}" alt="Product" /></a></td>
            <td class="pro-title"><a href="#">${item.name}</a></td>
            <td class="pro-price"><span style="font-size:17px">${formatCurrency(item.price)}</span></td>
            <td class="pro-quantity">
                <div class="quantity">
                    <div class="cart-plus-minus" style="margin-left:40px">
                        <button onclick="updateQuantity(${index}, ${item.quantity - 1})" style="font-weight:900;font-size:20px">-</button>
                        <input type="text" style="height:30px;width:100px; text-align:center;border-width:3px" value="${item.quantity}" readonly>
                        <button onclick="updateQuantity(${index}, ${item.quantity + 1})" style="font-weight:900;font-size:20px">+</button>
                    </div>
                </div>
            </td>
            <td class="pro-subtotal"><span style="font-size:17px">${formatCurrency(item.price * item.quantity)}</span></td>
            <td class="pro-remove"><button onclick="removeFromCart(${index})">Xóa</button></td>
        `;
        cartItemsContainer.appendChild(itemElement);
        subtotal += item.price * item.quantity;
    });

    subtotalElement.textContent = formatCurrency(subtotal);
    fee.textContent= formatCurrency(subtotal*0.1);
    const fee1=subtotal*0.1;
    if(subtotal<1000000){
    totalAmountElement.textContent = formatCurrency(subtotal + 200000+fee1);
    localStorage.setItem('totalAmount', formatCurrency(subtotal+ 200000+fee1));
}
    else{
        free.innerHTML='0 ₫';
        totalAmountElement.textContent = formatCurrency(subtotal+fee1);
        localStorage.setItem('totalAmount', formatCurrency(subtotal+fee1));
    }
}

// Hàm cập nhật số lượng sản phẩm
function updateQuantity(index, newQuantity) {
    if (newQuantity <= 0) {
        removeFromCart(index);  // Xóa sản phẩm nếu số lượng <= 0
    } else {
        cart[index].quantity = newQuantity;
        localStorage.setItem('cart', JSON.stringify(cart));
        displayCart();
    }
}

// Hàm xóa sản phẩm khỏi giỏ hàng
function removeFromCart(index) {
    cart.splice(index, 1);  // Xóa sản phẩm khỏi giỏ hàng
    localStorage.setItem('cart', JSON.stringify(cart));  // Cập nhật lại localStorage
    displayCart();  // Hiển thị lại giỏ hàng
}

// Hiển thị giỏ hàng khi trang được tải
if (window.location.pathname.endsWith('cart.html')) {  // Đảm bảo đúng tên file HTML
    displayCart();
}


function getInputValueAsInteger(inputId) {
    const inputValue = document.getElementById(inputId).value;
    
    const integerValue = parseInt(inputValue);

    return integerValue;
}

const totalAmount = localStorage.getItem('totalAmount');
document.getElementById('amount').innerHTML = formatCurrency(totalAmount);


