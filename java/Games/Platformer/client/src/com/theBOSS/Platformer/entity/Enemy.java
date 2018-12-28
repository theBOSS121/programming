package com.theBOSS.Platformer.entity;

import com.theBOSS.Platformer.Screen;
import com.theBOSS.Platformer.graphics.Sprite;

public class Enemy extends Entity{

	public Enemy(double x, double y, Sprite sprite) {
		super(x, y, sprite);
	}

	public void update() {
		
	}
	
	public void render(Screen screen) {
		screen.renderSprite(sprite, (int) x, (int) y);
	}
	
}
