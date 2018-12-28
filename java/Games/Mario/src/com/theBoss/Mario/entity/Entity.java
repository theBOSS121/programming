package com.theBoss.Mario.entity;

import com.theBoss.Mario.graphics.Screen;

public class Entity {
	
	public double x, y;
	
	public Entity(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void update() {}
	
	public void render(Screen screen) {}
	
}
