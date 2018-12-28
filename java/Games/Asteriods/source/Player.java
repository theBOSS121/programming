
public class Player {

	public double x, y, nx, ny, speed;
	public double angle;
	
	public Player(int x, int y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	public void move() {
		x += nx * speed;
		y += ny * speed;
		if(x < -Asteroids.width) x = Asteroids.WIDTH;
		if(y < -Asteroids.height) y = Asteroids.HEIGHT;
		if(x > Asteroids.WIDTH) x = -Asteroids.width;
		if(y > Asteroids.HEIGHT) y = -Asteroids.height;
	}
	
	public void changeDir(){
		nx = Math.cos(angle);
		ny = Math.sin(angle);		
	}
}
