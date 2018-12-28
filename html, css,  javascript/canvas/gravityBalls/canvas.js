var canvas = document.querySelector('canvas');
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

var c = canvas.getContext('2d');

var mouse = {
	x:undefined,
	y:undefined
}

window.addEventListener('mousemove', function(event){
	mouse.x = event.x;
	mouse.y = event.y;
	
});

window.addEventListener('resize', function(){
	canvas.width = window.innerWidth;
	canvas.height = window.innerHeight;	
	init();
});

window.addEventListener('click', function(){
	
	init();
});

var colorArray = [
	'#2c3e50',
	'#e74c3c',
	'#ecf0f1',
	'#3498db',
	'#298089',
];

var gravity = 1;
var frictionY = 0.85;
var frictionX = 0.995;


function randomIntFromRange(min, max) {
	return Math.floor(Math.random() * (max - min + 1) + min)
}

function Ball(x, y, dx, dy, radius) {
	this.x = x;
	this.y = y;
	this.dx = dx;
	this.dy = dy;
	this.radius = radius;
	this.color = colorArray[Math.floor(Math.random() * colorArray.length)];
	
	this.draw = function() {
		c.beginPath();
		c.arc(this.x, this.y, this.radius, 0, 2 * Math.PI, false);
		c.fillStyle = this.color;
		// c.stroke();
		c.fill();
	}
	
	this.update = function() {
		if(this.y + this.radius + this.dy> canvas.height) {
			this.dy = -this.dy * frictionY;
			this.dx *= frictionX;
		}else {
			this.dy += gravity;
		}
		if(this.x + this.radius + this.dx > canvas.width || this.x - this.radius <= 0) {
			this.dx = -this.dx;
		}
		
		this.y += this.dy;
		this.x += this.dx;
		
		this.draw();
	}
	
}

var ballArray = [];
function init() {
	ballArray = [];
	for(var i = 0; i < 100; i++) {
		var radius = randomIntFromRange(8, 20);
		var x = randomIntFromRange(radius, canvas.width - radius);
		var y = randomIntFromRange(0, canvas.height - radius);
		var dx = randomIntFromRange(-4, 4);
		var dy = randomIntFromRange(-2, 2);
		ballArray.push(new Ball(x, y, dx, dy, radius));
	}
}

function animate() {
	requestAnimationFrame(animate);
	c.fillStyle = '#777';
	c.fillRect(0, 0, innerWidth, innerHeight);
	
	
	for(var i = 0; i < ballArray.length; i++) {
		ballArray[i].update();
	}
	
}

init();
animate();