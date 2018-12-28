package theBOSS.Avoid.entity;

import theBOSS.Avoid.graphics.Screen;
import theBOSS.Avoid.graphics.Sprite;

public class Entity {
	public boolean hitable = false;
	public double x, y, width, height;
	public Sprite sprite;
	
	public Entity(double x, double y, Sprite sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		width = sprite.width;
		height = sprite.height;
	}
	
	public void update() {}
	
	public void render(Screen screen) {}
	
}
