package com.theBOSS.EndlessShooter.entity;

import com.theBOSS.EndlessShooter.graphics.Screen;
import com.theBOSS.EndlessShooter.graphics.Sprite;

public class Entity {
	public double x, y;
	public int width, height;
	public Sprite sprite;
	public Sprite rotatedSprite;
	public boolean remove = false;
	
	public Entity(int x, int y, Sprite sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		this.width = sprite.width;
		this.height = sprite.height;
		rotatedSprite = sprite;
	}
	
	public void update() {}
	
	public void render(Screen screen) {}	
}
