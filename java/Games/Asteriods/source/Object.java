
public class Object {
	
	public int x, y, dx, dy, dimension, width, height;
	
	public Object(int x, int y, int dx, int dy, int dimension) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.dimension = dimension;
		if(dimension == 3) {
			width = Asteroids.asterid.getWidth(null);
			height = Asteroids.asterid.getHeight(null);
		}
		if(dimension == 2) {
			width = Asteroids.asterid.getWidth(null) / 2;
			height = Asteroids.asterid.getHeight(null) / 2;			
		}
		if(dimension == 1) {
			width = Asteroids.asterid.getWidth(null) / 4;
			height = Asteroids.asterid.getHeight(null) / 4;	
		}
	}

	public void update() {
		x += dx;
		y += dy;
		if(x < -100) x = 800;
		if(y < -80) y = 600;
		if(x > 800) x = -100;
		if(y > 600) y = -80;
	}	
	
}
