package com.theBoss.Jaxson.entity;

import com.theBoss.Jaxson.graphics.Screen;
import com.theBoss.Jaxson.graphics.Sprite;
import com.theBoss.Jaxson.level.Level;

public class Enemy extends Entity{

	@SuppressWarnings("unused")
	private Level level;
	private double time;
	private int side;
	
	public Enemy(int x, int y, Sprite sprite, Level level, int side) {
		super(x, y, sprite);
		this.level = level;
		this.side = side;
	}
	
	public void update() {
		x += 0.5 * Math.sin(time);
		if(side == 0) {
			y += 0.5 * -Math.cos(time);
			x += 0.5;
		}else {			
			y += 0.5 * Math.cos(time);
			x -= 0.5;
		}
		time += 0.2;
		
	}
	
	public void render(Screen screen) {
		screen.renderEntity(this, side);
	}
	
}
