class TypeWriter {
    constructor(txtElement, words, wait = 3000) {
        this.txtElement = txtElement;
        this.words = words;
        this.txt = '';
        this.wordIndex = 0;
        this.wait = parseInt(wait, 10);
        this.type();
        this.isDeleting = false;
    }

    type() {
        const current = this.wordIndex % this.words.length;
        const fullTxt = this.words[current];
        if(this.isDeleting) {
            this.txt = fullTxt.substring(0, this.txt.length - 1);
        }else {
            this.txt = fullTxt.substring(0, this.txt.length + 1);
        }
        this.txtElement.innerHTML = '<span class="txt">' + this.txt + '</span>'

        let typeSpeed = 180;

        if(this.isDeleting) {
            typeSpeed /= 2;
        }

        if(!this.isDeleting && this.txt === fullTxt) {
            typeSpeed = this.wait;
            this.isDeleting = true;
        }else if(this.isDeleting && (this.txt === "")) {
            this.isDeleting = false;
            this.wordIndex++;
            typeSpeed = 500;
        }

        setTimeout(() => this.type(), typeSpeed);
    }
}

function startTypeWriters() {
    const txtElements = document.querySelectorAll('.txt-type');
    for(let i = 0; i < txtElements.length; i++) {
        const words = JSON.parse(txtElements[i].getAttribute('data-words'));
        const wait = txtElements[i].getAttribute('data-wait');
        new TypeWriter(txtElements[i], words, wait);
    }
}

document.addEventListener('DOMContentLoaded', init);
let tspansForCursor = document.querySelectorAll('.typewriter p span');

function init() {
    startTypeWriters();
    for(let i = 0; i < tspansForCursor.length; i++) {
        tspansForCursor[i].classList.add("cursor");
    }
}