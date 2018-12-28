package com.theBoss.Mario.entity.player;

import com.theBoss.Mario.entity.Entity;
import com.theBoss.Mario.graphics.Screen;
import com.theBoss.Mario.graphics.Sprite;
import com.theBoss.Mario.input.Keyboard;
import com.theBoss.Mario.level.Level;

public class Player extends Entity{

	private Keyboard key;
	private Level level;
	private Sprite sprite;
	private double upDown = 0.0, leftRight = 0.0;
	private boolean jump = true, falling;
	private long last = System.currentTimeMillis();
	private int flip = 0;
	
	public Player(double x, double y, Sprite sprite, Keyboard key, Level level) {
		super(x, y);
		this.sprite = sprite;
		this.key = key;
		this.level = level;
	}
	
	public void update() {
		movement();
		if(jump) sprite = Sprite.player_jumping;
		if(upDown == 0.0 && leftRight == 0.0) sprite = Sprite.player_still;
		if(leftRight != 0.0 && !jump) {
			if(System.currentTimeMillis() - last < 80) {
				sprite = Sprite.player_moving1;				
			}else if(System.currentTimeMillis() - last < 160) {
				sprite = Sprite.player_moving2;
			}else if(System.currentTimeMillis() - last < 240) {
				sprite = Sprite.player_moving3;
			}else {
				last = System.currentTimeMillis();
			}
		}
	}
	
	private void movement() {
		if(key.up && !jump) {
			upDown = -8.0;
		}
		if(key.down) {
			
		}		
		if(key.left) {
			leftRight = -3.0;
		}
		if(key.right) {
			leftRight = 3.0;
		}
		if(key.left && key.right) leftRight = 0.0;
		
		if(!key.right && !key.left) {
			leftRight = 0.0;
		}
				
		if(upDown < 0) jump = true;
		if(upDown > 0) falling = true;
		else falling = false;
		
		if(jump) {
			if(upDown <= 7.6) {
				upDown += 0.4;
			}			
		}

		if(!falling && jump) {
			if(level.getTile((int) ((x + 2.0) / 16.0),(int) ((y + upDown) / 16.0)).solid) {
				falling = true;
				jump = true;
				if(!level.getTile((int) ((x + 2.0) / 16.0),(int) ((y + upDown) / 16.0)).animPosible) {
					upDown = 0.0;
				}
			}
			if(level.getTile((int) ((x + sprite.width - 3.0) / 16.0),(int) ((y + upDown) / 16.0)).solid) {
				falling = true;
				jump = true;
				if(!level.getTile((int) ((x + sprite.width - 3.0) / 16.0),(int) ((y + upDown) / 16.0)).animPosible) {
					upDown = 0.0;
				}
			}
			if(level.getTile((int) ((x + 2.0) / 16.0),(int) ((y + upDown) / 16.0)).animPosible) {
				falling = true;
				jump = true;
				upDown = 0.0;
				level.tx = (int) ((x + 2.0) / 16.0);
				level.ty = (int) ((y + upDown) / 16.0) - 1;
				if(level.getTile(level.tx, level.ty).animPosible) level.tileAnimation = true;
			}
			if(level.getTile((int) ((x + sprite.width - 3.0) / 16.0),(int) ((y + upDown) / 16.0)).animPosible) {
				falling = true;
				jump = true;
				upDown = 0.0;
				level.tx = (int) ((x + sprite.width - 3.0) / 16.0);
				level.ty = (int) ((y + upDown) / 16.0) - 1;
				if(level.getTile(level.tx, level.ty).animPosible) level.tileAnimation = true;
			}
		}
		
		if(leftRight != 0.0) {
			if(level.getTile((int) ((x + 2.0 + leftRight) / 16), (int) (y / 16)).solid ||
			   level.getTile((int) ((x + 2.0 + leftRight) / 16), (int) ((y + sprite.height - 1.0) / 16)).solid ||
			   level.getTile((int) ((x - 3.0 + sprite.width + leftRight) / 16), (int) (y / 16)).solid ||
			   level.getTile((int) ((x - 3.0 + sprite.width + leftRight) / 16), (int) ((y + sprite.height - 1.0) / 16)).solid) {
				leftRight = 0.0;
			}
		}
		
		if((level.getTile((int) ((x + 2.0) / 16.0),(int) ((y + sprite.height + upDown) / 16.0)).solid ||
			level.getTile((int) ((x + sprite.width - 3.0) / 16.0),(int) ((y + sprite.height + upDown) / 16.0)).solid) && falling) {
			jump = false;
			upDown = 0.0;
			y = Math.round((y + sprite.height + upDown) / 16) * 16 - sprite.height;
		}
		
		if((!level.getTile((int) ((x + 2.0) / 16.0),(int) ((y + sprite.height + 8.0) / 16.0)).solid &&
			!level.getTile((int) ((x + sprite.width - 3.0) / 16.0),(int) ((y + sprite.height + 8.0) / 16.0)).solid) && !falling && !jump) {
			jump = true;
		}

		x += leftRight;
		y += upDown;
	}
	
	public void render(Screen screen) {
		if(leftRight > 0) flip = 0;
		if(leftRight < 0) flip = 1;
		screen.renderSprite((int) x, (int) y, sprite, flip);	
	}
	
}
