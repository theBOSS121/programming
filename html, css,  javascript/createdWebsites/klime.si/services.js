//for expension of services

let s = document.querySelectorAll('.service');
for(let i = 0; i < s.length; i++) {
    s[i].addEventListener('click', function(e) {clicked(s[i])});
    s[i].addEventListener('mouseenter', function(e) {hovered(s[i])});
    s[i].addEventListener('mouseleave', function(e) {unhovered(s[i])});
}

// will open the first one from the begining this is in 
// other js file cuz you cant have two listeners for 'load' event
// s[0].addEventListener("load", function(e) {
//     clicked(s[0]);
// });

let width = getWidth();

window.onresize = function(e){
    if(width != getWidth()) {
        clicked(undefined);
        width = getWidth();
    }
};

function clicked(ss) {
    let service = ss;
    let height;
    for(let i = 0; i < s.length; i++) {
        s[i].style.height = '80px';
        s[i].firstElementChild.style.background = "#e2e9e7";
        s[i].firstElementChild.style.color = "#6b6b6b";
    }
    if(service === undefined) return;
    if(service.offsetHeight) {
        height = service.offsetHeight;
    } else if(obj.style.pixelHeight) {
        height = service.style.pixelHeight;
    }
    if(height != 80) {
        service.style.height = '80px';
        service.firstElementChild.style.background = "#e2e9e7";
        service.firstElementChild.style.color = "#6b6b6b";
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
        service.firstElementChild.style.background = "#6b6b6b";
        service.firstElementChild.style.color = "#e2e9e7";
    }            
}

function hovered(ss) {
    let service = ss;
    if(service.offsetHeight) {
        height = service.offsetHeight;
    } else if(obj.style.pixelHeight) {
        height = service.style.pixelHeight;
    }
    service.firstElementChild.style.background = "#6b6b6b";
    service.firstElementChild.style.color = "#e2e9e7";
    
}

function unhovered(ss) {
    let service = ss;
    if(service.offsetHeight) {
        height = service.offsetHeight;
    } else if(obj.style.pixelHeight) {
        height = service.style.pixelHeight;
    }
    if(height == 80) {
        service.firstElementChild.style.background = "#e2e9e7";
        service.firstElementChild.style.color = "#6b6b6b";
    }
}