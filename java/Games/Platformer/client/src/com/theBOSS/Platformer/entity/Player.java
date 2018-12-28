package com.theBOSS.Platformer.entity;

import java.awt.event.KeyEvent;

import com.theBOSS.Platformer.Screen;
import com.theBOSS.Platformer.graphics.Sprite;
import com.theBOSS.Platformer.input.Keyboard;
import com.theBOSS.Platformer.level.Level;

public class Player extends Entity{

	private double speed = 3.5;
	private double fallSpeed = 0;
	private double xDir = 0, yDir = 0;
	private boolean onGround = false;
	
	public Player(double x, double y, Sprite sprite) {
		super(x, y, sprite);
	}

	public void update() {
		xDir = 0;
		yDir = 0;
		if(Keyboard.key(KeyEvent.VK_W) && onGround) {
			fallSpeed = -10;
//			yDir = -speed;
		}
		if(Keyboard.key(KeyEvent.VK_S)) {
//			yDir = speed;
		}
		if(Keyboard.key(KeyEvent.VK_D)) {
			xDir = speed;
		}
		if(Keyboard.key(KeyEvent.VK_A)) {
			xDir = -speed;
		}
		
		if(!onGround) {
			fallSpeed += 0.6;
		}

		collision();
		yDir += fallSpeed;
		
		x += xDir;
		y += yDir;
		
	}
	
	private void collision() {
		//player coruners
		if(Level.level1.getTileSprite((int) x / Level.TILE_WIDTH,(int) y / Level.TILE_HEIGHT).collidable) {		
			x = (int) x + (Sprite.tile1.width - (x % Sprite.tile1.width - 1));
		}	
		if(Level.level1.getTileSprite((int) (x) / Level.TILE_WIDTH,(int) (y + height - 1) / Level.TILE_HEIGHT).collidable) {
			fallSpeed = 0.0;
			y = (int) y - (y % Sprite.tile1.height - 1);
		}		
		if(Level.level1.getTileSprite((int) (x + width - 1) / Level.TILE_WIDTH,(int) (y) / Level.TILE_HEIGHT).collidable) {
			x = (int) x - (x % Sprite.tile1.width - 1);
		}		
		if(Level.level1.getTileSprite((int) (x + width - 1) / Level.TILE_WIDTH,(int) (y + height - 1) / Level.TILE_HEIGHT).collidable) {
			fallSpeed = 0.0;
			y = (int) y - (y % Sprite.tile1.height - 1);
		}
		//under player for ground test
		if(Level.level1.getTileSprite((int) (x) / Level.TILE_WIDTH,(int) (y + height) / Level.TILE_HEIGHT).collidable ||
		   Level.level1.getTileSprite((int) (x + width - 1) / Level.TILE_WIDTH,(int) (y + height) / Level.TILE_HEIGHT).collidable) {
			onGround = true;
		}else {
			onGround = false;
		}
		
	}

	public void render(Screen screen) {
		screen.renderSprite(sprite, (int) x, (int) y);
	}
	
}
