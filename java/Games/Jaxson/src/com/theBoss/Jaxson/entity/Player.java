package com.theBoss.Jaxson.entity;

import com.theBoss.Jaxson.graphics.Screen;
import com.theBoss.Jaxson.graphics.Sprite;
import com.theBoss.Jaxson.input.Keyboard;
import com.theBoss.Jaxson.level.Level;

public class Player extends Entity{

	private Keyboard key;
	private Level level;
	private int flip = 0;
	private long last = System.currentTimeMillis(), jumpAnim = System.currentTimeMillis(), crouchTime;	
	private double fallingSpeed = 0.0, leftRight = 0.0;
	private boolean jumping = false, crouching = false, crouchingAgain = true;
	
	public Player(int x, int y, Sprite sprite, Keyboard key, Level level) {
		super(x, y, sprite);
		this.key = key;
		this.level = level;
	}
	
	public void update() {
		movement();
		sprites();
	}
	
	private void movement() {
		if(key.down && crouchingAgain) {
			crouching = true;
			crouchingAgain = false;
			crouchTime = System.currentTimeMillis();
		}
		if(crouching) {
			if(System.currentTimeMillis() - crouchTime >= 500) {
				crouching = false;
			}
			if(!key.down) {
				crouching = false;
				crouchingAgain = true;
			}
		}else {
			if(!key.down) crouchingAgain = true;
		}
		
		if(key.left) {
			leftRight = -3.0;
		}
		if(key.right) {
			leftRight = 3.0;
		}		
		
		if(key.left && key.right || !key.left && !key.right) leftRight = 0.0;
		
		if(key.up && !jumping) {
			fallingSpeed = -7.0;
			jumping = true;
		}

		if(level.getTile((int) ((x  + 2 + leftRight) / 16), (int) ((y) / 16)) == null && level.getTile((int) ((x + width - 3 + leftRight) / 16), (int) ((y) / 16)) == null &&
		   level.getTile((int) ((x  + 2 + leftRight) / 16), (int) ((y + height - 1) / 16)) == null && level.getTile((int) ((x + width - 3 + leftRight) / 16), (int) ((y + height - 1) / 16)) == null) {
			x += leftRight;
		}else {
			leftRight = 0.0;
		}
		if(level.getTile((int) (x / 16), (int) ((y + height + fallingSpeed) / 16)) == null && level.getTile((int) ((x + width) / 16), (int) ((y + height + fallingSpeed) / 16)) == null) {
			fallingSpeed += 0.3;
			y += fallingSpeed;
			jumping = true;
			sprite = Sprite.player_still;
		}else {
			fallingSpeed = 0.0;
			jumping = false;
		}
		if(level.getTile((int) (x / 16), (int) ((y + fallingSpeed) / 16)) != null && level.getTile((int) ((x + width) / 16), (int) ((y + fallingSpeed) / 16)) != null) {
			fallingSpeed = 0.3;
		}
	}

	private void sprites() {
		if(!key.left && !key.right && !key.up && !key.down) {
			sprite = Sprite.player_still;
		}		
		if(key.right || key.left) {
			if(System.currentTimeMillis() - last < 100) {
				sprite = Sprite.player_moving1;
			}else if(System.currentTimeMillis() - last < 200) {
				sprite = Sprite.player_moving2;	
			}else if(System.currentTimeMillis() - last < 300) {
				sprite = Sprite.player_moving3;
			}else if(System.currentTimeMillis() - last < 400) {
				sprite = Sprite.player_moving4;
			}else if(System.currentTimeMillis() - last < 500) {
				sprite = Sprite.player_moving5;
			}else {
				last = System.currentTimeMillis();
			}
		}
		if(key.left && key.right) sprite = Sprite.player_still;		

		if(crouching) {
			sprite = Sprite.player_crouch;
		}else {
			if(sprite == Sprite.player_crouch) sprite = Sprite.player_still;
		}
		if(jumping) {
			if(jumpAnim == -1) jumpAnim = System.currentTimeMillis();
			if(System.currentTimeMillis() - jumpAnim < 140) {
				sprite = Sprite.player_jump1;
			}else if(System.currentTimeMillis() - jumpAnim < 280) {
				sprite = Sprite.player_jump2;	
			}else if(System.currentTimeMillis() - jumpAnim < 320) {
				sprite = Sprite.player_jump3;
			}else if(System.currentTimeMillis() - jumpAnim < 460) {
				sprite = Sprite.player_jump4;	
			}else if(System.currentTimeMillis() - jumpAnim < 600) {
				sprite = Sprite.player_jump5;
			}
		}
		if(!jumping && jumpAnim >= 600) {
			jumpAnim = -1;
		}
		if(jumping && crouching) sprite = Sprite.player_crouch;
	}

	public void render(Screen screen) {
		if(key.left) flip = 1;
		if(key.right) flip = 0;
		screen.renderEntity(this, flip);
	}
	
}
