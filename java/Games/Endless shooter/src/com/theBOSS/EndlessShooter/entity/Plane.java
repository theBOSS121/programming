package com.theBOSS.EndlessShooter.entity;

import com.theBOSS.EndlessShooter.EndlessShooter;
import com.theBOSS.EndlessShooter.graphics.Screen;
import com.theBOSS.EndlessShooter.graphics.Sprite;

public class Plane extends Entity{

	private double dir;
	private double speed = 2.0;
	public boolean  removable = false;
	public int countDown = 20;
	
	public Plane(int x, int y, Sprite sprite, double dir) {
		super(x, y, sprite);
		this.dir = dir;
		rotatedSprite = Sprite.rotate(sprite, dir);
	}

	public void update() {
		if(!remove) {
			x += Math.cos(dir) * speed;
			y += Math.sin(dir) * speed;
		}else {
			if(countDown >= 0) countDown --;
		}
		
		if((x + width < 0 || y + height < 0 || x > EndlessShooter.WIDTH || y > EndlessShooter.HEIGHT) && removable) remove = true;
		if(x < -200 || y < -200 || x > EndlessShooter.WIDTH + 200 || y > EndlessShooter.HEIGHT) remove = true;
		if(x > 0 && y > 0 && x + width < EndlessShooter.WIDTH && y + height < EndlessShooter.HEIGHT) removable = true;
		
	}
	
	public void render(Screen screen) {
		screen.renderEntity(this, 0);
	}	
	
}
