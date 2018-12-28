package com.thecherno.rain.entity.mob;

import java.util.List;

import com.thecherno.rain.graphics.AnimatedSprite;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.graphics.SpriteSheet;

public class Chaser extends Mob{

	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 3);
	
	private AnimatedSprite animSprite = down;
	
	private double xa = 0;
	private double ya = 0;
	private double speed = 0.8;
	
	public Chaser(int x,int y){
		this.x = x << 4;
		this.y = y << 4;
		sprite = Sprite.player_forward;
	}
	
	private void move(){
		xa = 0;
		ya = 0;
		List<Mob> players = level.getPlayers(this, 100);
		if(players.size() > 0){
			Mob player = players.get(0);
			
				if(x + speed < player.getX()) xa += speed;
				if(x - speed > player.getX()) xa -= speed;
			
			if(y + speed < player.getY()) ya += speed;
			if(y - speed > player.getY()) ya -= speed;
		}
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
		} else {
			walking = false;
		}
	}
	
	public void update() {
		move();
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
		
	}

	public void render(Screen screen) {
		sprite = animSprite.getSprite();
		screen.renderMob((int)(x - 16),(int) (y - 16), this);
	}

}
