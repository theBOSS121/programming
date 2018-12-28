
public class Bullet {
	public double x, y, nx, ny;
	public double range = 0;
	public boolean remove = false;	

	public Bullet(double x, double y, double angle, double speed) {
		this.x = x;
		this.y = y;
		nx = Math.cos(angle) * speed;
		ny = Math.sin(angle) * speed;	
	}
	
	public void move() {
		x += nx;
		y += ny;
		range += Math.sqrt(nx * nx + ny * ny);
		if(x < -Asteroids.width) x = Asteroids.WIDTH;
		if(y < -Asteroids.height) y = Asteroids.HEIGHT;
		if(x > Asteroids.WIDTH) x = -Asteroids.width;
		if(y > Asteroids.HEIGHT) y = -Asteroids.height;
	}
	
}
