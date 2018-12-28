var canvas = document.querySelector('canvas');
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

var c = canvas.getContext('2d');

// c.fillStyle = "rgba(150, 10, 10, 0.4)";
// c.fillRect(100, 100, 100, 100);

//line 
// c.beginPath();
// c.moveTo(70, 300);
// c.lineTo(400, 200);
// c.lineTo(500, 400);
// c.strokeStyle = "#fa34a4";
// c.stroke();

//Arc/Circle
// c.beginPath();
// c.arc(300, 300, 30, 0, 2 * Math.PI, false);
// c.strokeStyle = 'blue';
// c.stroke();

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

var colorArray = [
	'#2c3e50',
	'#e74c3c',
	'#ecf0f1',
	'#3498db',
	'#298089',
];

function Circle(x, y, dx, dy, radius, maxRadius) {
	this.x = x;
	this.y = y;
	this.dx = dx;
	this.dy = dy;
	this.radius = radius;
	this.minRadius = radius;
	this.maxRadius = maxRadius;
	this.color = colorArray[Math.floor(Math.random() * colorArray.length)];
	
	this.draw = function() {
		c.beginPath();
		c.arc(this.x, this.y, this.radius, 0, 2 * Math.PI, false);
		c.fillStyle = this.color;
		c.fill();
	}
	
	this.update = function() {
		if(this.x + this.radius > innerWidth || this.x - this.radius < 0) this.dx *= -1;
		if(this.y + this.radius > innerHeight || this.y - this.radius < 0) this.dy *= -1;
		this.x += this.dx;
		this.y += this.dy;
		
		//interactivity
		if(mouse.x - this.x < 50 && mouse.x - this.x > -50 && mouse.y - this.y < 50 && mouse.y - this.y > -50) {
			if(this.radius < maxRadius) {
				this.radius += 1;
			}
		}else if(this.radius > this.minRadius) {
			this.radius -= 1;
		}
		
		
		this.draw();
	}
	
}





var circleArray = [];
function init() {
	circleArray = [];
	for(var i = 0; i < 800; i++) {
		var radius = Math.random() * 6 + 2;
		var x = Math.random() * (innerWidth - radius * 2) + radius;
		var y = Math.random() * (innerHeight - radius * 2) + radius;
		var dx = (Math.random() - 0.5);
		var dy = (Math.random() - 0.5);
		var maxRadius = Math.random() * 25 + 15;
		circleArray.push(new Circle(x, y, dx, dy, radius, maxRadius));
		
	}
}

function animate() {
	requestAnimationFrame(animate);
	c.clearRect(0, 0, innerWidth, innerHeight);
	
	for(var i = 0; i < circleArray.length; i++) {
		circleArray[i].update();
		
	}
	
	
}

init();
animate();