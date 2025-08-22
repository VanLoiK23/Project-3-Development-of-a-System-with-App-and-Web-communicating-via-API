function move(element){
    var navItems = document.getElementsByClassName("nav-item");
  for (var i = 0; i < navItems.length; i++) {
    navItems[i].classList.remove("active");
  }

  element.classList.add("active");
}

let  menu=document.querySelector('#menu-icon');
let navbar=document.querySelector('.navbar');
menu.onclick=()=>{
  menu.classList.toggle('bx-x');
  navbar.classList.toggle('open');
}
window.onscroll = function() {
  myFunction();
};

function myFunction() {
  const header = document.getElementById("header");
  const stickyPoint = 150; 
  if (window.pageYOffset > stickyPoint) {
      header.classList.add("sticky");
  } else {
      header.classList.remove("sticky");
  }
}

function updateContainerClass() {
  var container = document.getElementById('ct');
  if (window.innerWidth <1200) {
      container.classList.remove('container');
      container.classList.add('container-fluid');
  } 
}

updateContainerClass();

window.addEventListener('resize', updateContainerClass);

var slider = document.getElementById("slider");
var output = document.getElementById("filterValue");

output.innerHTML = addCommasToNumberString(slider.value) + " VNĐ";

slider.oninput = function () {
    output.innerHTML = addCommasToNumberString(this.value) + " VNĐ";

    filterProducts(this.value);

    updateSliderColor();
}

function filterProducts(value) {
    var products = document.querySelectorAll('.single-product');

    products.forEach(function (product) {
        var productPrice = parseInt(product.dataset.price); 
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

function addMenuClickEvent() {
  let menu1 = document.querySelector('#me');
  let menu2 = document.querySelector('#menu-close');
  let navbar1 = document.querySelector('.sidebar_widget');

  if (menu1 && menu2 && navbar1) {
      menu1.onclick = () => {
          navbar1.classList.toggle('open');
      }

      menu2.onclick = () => {
          let navbar2 = document.querySelector('.sidebar_widget.open');
          if (navbar2) {
              navbar2.classList.remove('open');
          }
      }
  }
}

addMenuClickEvent();

var searchInput = document.getElementById('searchInput');
var searchButton = document.getElementById('searchButton');
var products = document.querySelectorAll('.product-area .single-product');

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

document.querySelector('.circle-icon i').addEventListener('animationend', function() {
    document.querySelector('.circle-icon').style.borderRadius = '0px';
});
