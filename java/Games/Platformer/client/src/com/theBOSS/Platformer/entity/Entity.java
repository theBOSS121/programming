package com.theBOSS.Platformer.entity;

import com.theBOSS.Platformer.Screen;
import com.theBOSS.Platformer.graphics.Sprite;

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
}
