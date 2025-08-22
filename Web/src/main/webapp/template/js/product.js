let menu1 = document.querySelector('#me');
let navbar1 = document.querySelector('.sidebar_widget');

menu1.onclick = () => {
  navbar1.classList.toggle('open');
}

let menu2 = document.querySelector('#menu-close');

menu2.onclick = () => {
  let navbar2 = document.querySelector('.sidebar_widget.open');
  navbar2.classList.remove('open'); 
}










