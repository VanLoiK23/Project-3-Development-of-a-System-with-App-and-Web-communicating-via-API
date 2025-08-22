var slider = document.getElementById("slider");
var output = document.getElementById("filterValue");

output.innerHTML = addCommasToNumberString(slider.value) + " VNĐ";

slider.oninput = function () {
    output.innerHTML = addCommasToNumberString(this.value) + " VNĐ";

    filterProduct(this.value);

    updateSliderColor();
}
function filterProduct(value) {
  var products = document.querySelectorAll('.product-area');

  products.forEach(function (product) {
      var productPrice = parseInt(product.dataset.price1); 
      if (productPrice > value) {
          product.style.display = 'block';
      } else {
          product.style.display = 'none';
      }
      if(value==0){
        product.style.display = 'block';
      }
  });
}

function updateSliderColor() {
    var value = slider.value;
    var percent = (value - slider.min) / (slider.max - slider.min) * 100;
    slider.style.background = 'linear-gradient(to right, black 0%, black ' + percent + '%, rgb(193, 191, 191) ' + percent + '%, white 100%)';
}

function addCommasToNumberString(numberString) {
    var number = parseInt(numberString);
    return number.toLocaleString('en-US');
}

var searchInput = document.getElementById('searchInput');
var searchButton = document.getElementById('searchButton');
var products = document.querySelectorAll('.shop_wrapper .product-area');

searchInput.addEventListener('input', function() {
    search();
});

searchButton.addEventListener('click', function() {
    search();
});

function search() {
    var searchText = searchInput.value.trim().toLowerCase();

    products.forEach(function(product) {
        var productName = product.querySelector('.title-2 a').innerText.trim().toLowerCase();
        if (productName.includes(searchText)) {
            product.style.display = 'block';
        } else {
            product.style.display = 'none';
        }
    });
}
