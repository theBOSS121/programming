package com.theBOSS.shooter_pc.entity;

import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.graphics.Screen;
import com.theBOSS.shooter_pc.graphics.Sprite;

public class Entity {
	public double x, y;
	public int width, height;
	public Sprite sprite;
	public boolean remove = false;
	
	public Entity(double x, double y, Sprite sprite) {
		this.x = x;
		this.y = y;
		if(sprite == null) return;
		this.sprite = sprite;
		width = sprite.width;
		height = sprite.height;
	}
	
	public void update() {}
	public void render() {}
	
	public int getOnScreenX() {
		return (int) (x - Shooter.screen.offX);
	}
	
	public int getOnScreenY() {
		return (int) (y - Shooter.screen.offY);
	}
}
