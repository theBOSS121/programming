package com.thecherno.rain.entity.mob;

import com.thecherno.rain.graphics.AnimatedSprite;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.graphics.SpriteSheet;

public class Dummy extends Mob{

	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 3);
	
	private AnimatedSprite animSprite = down;
	
	private int time = 0;
	private int xa = 0;
	private	int ya = 0;
	
	public Dummy(int x, int y){
		this.x = x << 4;
		this.y = y << 4;
		sprite = Sprite.player_forward;
	}
	
	public void update() {
		time++;
		if(time % (random.nextInt(50) + 30) == 0){
			xa = random.nextInt(3) - 1;
			ya = random.nextInt(3) - 1;
			if(random.nextInt(4) == 0){
				xa = 0;
				ya = 0;
			}
		}
		if(walking) animSprite.update();
		else animSprite.setFrame(0);
		if (ya < 0){
			dir = Direction.UP;
			animSprite = up;
		}else if (ya > 0){
			dir = Direction.DOWN;
			animSprite = down;
		}
		if (xa < 0){
			dir = Direction.LEFT;
			animSprite = left;
		}else if (xa >0){ 
			dir = Direction.RIGHT;
			animSprite = right;
		}
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
		} else {
			walking = false;
		}
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		screen.renderMob((int)(x - 16), (int)(y - 16), sprite, 0);
	}

}
