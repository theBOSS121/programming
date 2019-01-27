// for expending
let s = document.querySelectorAll('.service');
for(let i = 0; i < s.length; i++) {
    s[i].addEventListener('click', (e) => {clicked(s[i])});
    s[i].style.height = '80px';
}

function clicked(s) {
    let service = s;
    let numOfMores = service.childElementCount - 1;
    let height;
    if(service.offsetHeight) {
        height = service.offsetHeight;
    } else if(obj.style.pixelHeight) {
        height = service.style.pixelHeight;
    }
    if(height != 80) {
        service.style.height = '80px';
    }else {                
        service.style.height = 'auto';
        if(service.offsetHeight) {
            height = service.offsetHeight;
        } else if(obj.style.pixelHeight) {
            height = service.style.pixelHeight;
        }
        service.style.height = '80px';
        setTimeout(() => {
            service.style.height = height + 'px';
        } , 0);
    }            
}

// for navbar
window.onscroll = () => {scroll()};
var bar = document.querySelector(".bar");
var timeoutForHide;
var sticky = bar.offsetTop + 0;

function scroll() {        
    clearTimeout(timeoutForHide);
    showNav();
    if (window.pageYOffset >= sticky) {
        bar.classList.add("fixed");
        timeoutForHide = setTimeout(hideNav, 2000);
    } else {
        bar.classList.remove("fixed");
        pushdownNav();
    }
}
function hideNav() {bar.style.top = '-70px';}
function showNav() {bar.style.top = '0px';}    
function pushdownNav() {bar.style.top = sticky + 'px';}

// for slider

let larrow = document.querySelectorAll('.slide-left');
let rarrow = document.querySelectorAll('.slide-right');
for(let i = 0; i < larrow.length; i++) {
    larrow[i].addEventListener('click', goLeft);
    rarrow[i].addEventListener('click', goRight);
}

let slides = Array.from(document.querySelectorAll('.slider .slide'));
let slides2 = Array.from(document.querySelectorAll('.slider2 .slide'))

let now = new Date().getTime();
let old = now;

let i0 = 0;
let i1 = 1;
let i2 = 2;
let ii0 = i0;
let ii1 = i1;
let ii2 = i2;

function goLeft() {
    now = new Date().getTime();
    if(now - old < 500) {
        return false;
    }
    old = now;

    slides[i0].style.left = '50%';
    slides[i1].style.left = '150%';
    slides[i2].style.left = '-50%';
    slides[i0].style.opacity = '1';
    slides[i1].style.opacity = '1';
    slides[i2].style.opacity = '0';
    slides2[i0].style.left = '50%';
    slides2[i1].style.left = '150%';
    slides2[i2].style.left = '-50%';
    slides2[i0].style.opacity = '1';
    slides2[i1].style.opacity = '1';
    slides2[i2].style.opacity = '0';
    ii0 = i0;
    ii1 = i1;
    ii2 = i2;
    i0 = ii2;
    i1 = ii0;
    i2 = ii1;
}

function goRight() {
    now = new Date().getTime();
    if(now - old < 500) {
        return false;
    }
    old = now;

    slides[i0].style.left = '150%';
    slides[i1].style.left = '-50%';
    slides[i2].style.left = '50%';
    slides[i0].style.opacity = '0';
    slides[i1].style.opacity = '1';
    slides[i2].style.opacity = '1';
    slides2[i0].style.left = '150%';
    slides2[i1].style.left = '-50%';
    slides2[i2].style.left = '50%';
    slides2[i0].style.opacity = '0';
    slides2[i1].style.opacity = '1';
    slides2[i2].style.opacity = '1';
    ii0 = i0;
    ii1 = i1;
    ii2 = i2;
    i0 = ii1;
    i1 = ii2;
    i2 = ii0;    
}

// for navigation to open

let body = document.getElementsByTagName('body')[0];
let openClose = document.querySelector('.open-close-nav');
openClose.addEventListener('click', openOrCloseMenu);
let nav = document.querySelector('nav');
let opened = false;

let links = document.querySelectorAll('.links li a');
for(let i = 0; i < links.length; i++) {
    links[i].addEventListener('click', openOrCloseMenu);
}

function openOrCloseMenu() {
    if(!opened) {
        nav.style.right = '0%';
        openClose.style.background = "url('img/close-menu.svg')";
        openClose.style.transform = 'rotateZ(720deg)';
        // body.style.overflow = 'hidden';
        opened = true;
        clearTimeout(timeoutForHide);
    }else {
        nav.style.right = '-100%';
        openClose.style.background = "url('img/open-menu.svg')";
        openClose.style.transform = 'rotateZ(360deg)';
        body.style.overflow = 'visible';
        body.style.overflowX = 'hidden';
        opened = false;
    }
}