package com.bombit.entity.mob;

import com.bombit.entity.Bomb;
import com.bombit.entity.Speed;
import com.bombit.entity.plusBomb;
import com.bombit.entity.plusRange;
import com.bombit.graphics.AnimatedSprite;
import com.bombit.graphics.Screen;
import com.bombit.graphics.Sprite;
import com.bombit.graphics.SpriteSheet;
import com.bombit.input.Keyboard;

public class Player extends Mob {

	private String name;
	private Keyboard input;
	public Sprite sprite;
	private int anim = 0;
	public int col = -1;
	
	public int reloadTime = 0;
	
	public AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 3);
	public AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 3);
	public AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 3);
	public AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 3);

	private int fireRate = 0;

	public AnimatedSprite animSprite = down;
	
	
	
	public Player(String name, Keyboard input) {
		this.name = name;
		this.input = input;
		sprite = Sprite.player_forward;
	}

	public Player(String name, int x, int y, Keyboard input) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.input = input;
		sprite = Sprite.player_forward;
	}
	
	public String getName(){
		return name;
	}

	public void update() {
		if(walking) animSprite.update();
		else animSprite.setFrame(0);
		double xa = 0, ya = 0;
		if (input.left){
			xa -= speed;
			animSprite = left;
		}
		if (input.right){ 
			xa += speed;
			animSprite = right;
			
		}
		if (input.up){
			ya -= speed;
			animSprite = up;
		}
		if (input.down){
			ya += speed;
			animSprite = down;
		}
		
		if(bombs > 0) {
			if(reloadTime == 0 && lefted == true) {
				if(input.bomb) {
					level.add(new Bomb((int) x,(int) y, range, this, level));
					bombs--;
					reloadTime = 18;
				}
			}
		}
		
		
		if(reloadTime != 0) reloadTime--;
		
		if (xa != 0 || ya != 0) {
			move(xa, ya, this);
			walking = true;
		} else {
			walking = false;
		}
		
	}
	
	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		screen.renderMob((int) x - 16, (int) y - 16, sprite, this);
	}
}
