package com.theBOSS.Nalis_pc.entity;

import com.theBOSS.Nalis_pc.Nails;
import com.theBOSS.Nalis_pc.graphics.Screen;
import com.theBOSS.Nalis_pc.graphics.Sprite;

public class Entity {

	public double x, y;
	public int width, height;
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
	
	public int getOnScreenX() {
		return (int) (x - Nails.screen.offX);
	}
	
	public int getOnScreenY() {
		return (int) (y - Nails.screen.offY);
	}
}
