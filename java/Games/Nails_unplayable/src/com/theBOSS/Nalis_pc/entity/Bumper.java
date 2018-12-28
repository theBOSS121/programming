package com.theBOSS.Nalis_pc.entity;

import com.theBOSS.Nalis_pc.Nails;
import com.theBOSS.Nalis_pc.graphics.Screen;
import com.theBOSS.Nalis_pc.graphics.Sprite;

public class Bumper extends Entity{

	public int screenX = -1, screenY = -1;
	private boolean collidable = false;
	private double angle = 0.0;
	
	public Bumper(double x, double y, Sprite sprite) {
		super(x, y, sprite);
	}
	
	public void update() {
		collidable = false;
		screenY = 59;
		screenX = 44;
		
		if(!collidable) {
			x = screenX + Nails.screen.offX;
			y = screenY + Nails.screen.offY;
		}else {
			screenX = (int) (x - Nails.screen.offX);
			screenY = (int) (y - Nails.screen.offY);			
		}
		
		angle += 0.01;
		sprite = Sprite.rotate(Sprite.bumper, angle);
	}
	
	public void render() {
		if(!collidable) {
			Nails.screen.renderSpriteNoOffsets(sprite,(int) screenX,(int) screenY);
		}else {
			Nails.screen.renderSprite(sprite, (int) x, (int) y);
		}
	}

}
