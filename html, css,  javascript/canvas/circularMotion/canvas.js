var canvas = document.querySelector('canvas');
var c = canvas.getContext('2d');
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

let mouse = {
	x:canvas.width / 2,
	y:canvas.height / 2
}

var colors = [
	'#2c3e50',
	'#e74c3c',
	'#ecf0f1',
	'#3498db',
	'#298089',
];

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

function randomIntFromRange(min, max) {
	return Math.floor(Math.random() * (max - min + 1) + min)
}

function randomColor() {
	return colors[Math.floor(Math.random() * colors.length)];
}

function distance(x1, y1, x2, y2) {
	let xDistance = x2 - x1;
	let yDistance = y2 - y1;
	
	return Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
}

function rotate(velocity, angle) {
	const rotatedVelocities = {
		x: velocity.x * Math.cos(angle) - velocity.y * Math.sin(angle),	
		y: velocity.x * Math.sin(angle) + velocity.y * Math.cos(angle)
	};
	return rotatedVelocities;
}

function resolveCollision(particle, otherParticle) {
	const xVelocityDiff = particle.velocity.x - otherParticle.velocity.x;
	const yVelocityDiff = particle.velocity.y - otherParticle.velocity.y;
	
	const xDist = otherParticle.x - particle.x;
	const yDist = otherParticle.y - particle.y;
	
	if(xVelocityDiff * xDist + yVelocityDiff * yDist >= 0) {
		const angle = -Math.atan2(yDist, xDist);
		const m1 = particle.mass;
		const m2 = otherParticle.mass;
		
		const u1 = rotate(particle.velocity, angle);
		const u2 = rotate(otherParticle.velocity, angle);
		
		const v1 = {x:(u1.x * (m1 - m2) + 2 * u2.x * m2 )/ (m1 + m2), y:u1.y};
		const v2 = {x:(u2.x * (m2 - m1) + 2 * u1.x * m1 )/ (m1 + m2), y:u2.y};
		
		const vFinal1 = rotate(v1, -angle);
		const vFinal2 = rotate(v2, -angle);
		
		particle.velocity.x = vFinal1.x;
		particle.velocity.y = vFinal1.y;
		otherParticle.velocity.x = vFinal2.x;
		otherParticle.velocity.y = vFinal2.y;
		
	}
}

function Particle(x, y, radius) {
	this.x = x;
	this.y = y;
	this.radius = radius;
	this.color = randomColor();
	this.radians = Math.random() * 2 * Math.PI;
	this.velocity = Math.random() * 0.04 + 0.01;	
	this.distanceFromCenter = randomIntFromRange(20, 30);
	this.lastMouse = {x:x, y:y};
	
	this.update = function() {
		const lastPoint = {x:this.x, y:this.y};
		this.radians += this.velocity;
		
		this.lastMouse.x += (mouse.x - this.lastMouse.x) * 0.2;
		this.lastMouse.y += (mouse.y - this.lastMouse.y) * 0.2;
		
		this.x = this.lastMouse.x + Math.cos(this.radians) * this.distanceFromCenter;
		this.y = this.lastMouse.y + Math.sin(this.radians) * this.distanceFromCenter;
		
		this.draw(lastPoint);
	}
		
	this.draw = function(lastPoint) {
		c.beginPath();
		c.strokeStyle = this.color;
		c.lineWidth = this.radius;
		c.moveTo(lastPoint.x, lastPoint.y);
		c.lineTo(this.x, this.y);
		c.stroke();
	}
	
}

let particles;

function init() {
	particles = [];
	for(let i = 0; i < 50; i++) {		
		
		particles.push(new Particle(canvas.width / 2, canvas.height / 2, (Math.random() * 2) + 1));
	}
}

function animate() {
	requestAnimationFrame(animate);
	c.fillStyle = "rgba(255, 255, 255, 0.05)";
	c.fillRect(0, 0, innerWidth, innerHeight);
	
	for(let i = 0; i < particles.length; i++) {
		particles[i].update();
	}
	
}

init();
animate();