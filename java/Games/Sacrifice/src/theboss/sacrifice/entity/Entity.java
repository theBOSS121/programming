package theboss.sacrifice.entity;

import theboss.sacrifice.Sacrifice;
import theboss.sacrifice.graphics.Sprite;

public class Entity {

	public double x, y;
	public int width, height;
	public Sprite sprite;
	public int radius;
	
	public double velX = 0.0, velY = 0.0, mass = 1.0;
	
	public boolean remove = false;
	public boolean hit = false;
	public boolean onScreen = false;
	public int counter = 0;
	public int timeInTheZone = 0;
	
	public Entity(double x, double y, Sprite sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		width = sprite.width;
		height = sprite.height;
		radius = width / 2;
	}
	
	public void update() {}
	public void render() {}
	
	public int getOnScreenX() {
		return (int) (x - Sacrifice.screen.offX);
	}
	
	public int getOnScreenY() {
		return (int) (y - Sacrifice.screen.offY);
	}
	
	public boolean isInTheZone() {
		double xDiff = Sacrifice.WIDTH / 2 - this.x - this.width / 2;
		double yDiff = Sacrifice.HEIGHT / 2 - this.y - this.width / 2;
		double dist = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
		
		if(dist < 154) {
			return true;
		}else {
			return false;
		}
		
	}
}
