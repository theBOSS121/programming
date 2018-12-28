package com.theBoss.Jaxson.entity;

import com.theBoss.Jaxson.graphics.Screen;
import com.theBoss.Jaxson.graphics.Sprite;

public class Entity {
	public double x, y;
	public int width, height;
	public Sprite sprite;
	
	public Entity(int x, int y, Sprite sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		this.width = sprite.width;
		this.height = sprite.height;
	}
	
	public void update() {}
	
	public void render(Screen screen) {}	
}
