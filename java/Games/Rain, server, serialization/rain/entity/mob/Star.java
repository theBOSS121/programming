package com.thecherno.rain.entity.mob;

import java.util.List;

import com.thecherno.rain.graphics.AnimatedSprite;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.graphics.SpriteSheet;
import com.thecherno.rain.level.Node;
import com.thecherno.rain.util.Vector2i;

public class Star extends Mob{
	private AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 3);
	private AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 3);
	private AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 3);
	private AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 3);
	
	private AnimatedSprite animSprite = down;
	
	private double xa = 0;
	private double ya = 0;
	private List<Node> path = null;
	private int time = 0;
	
	public Star(int x,int y){
		this.x = x << 4;
		this.y = y << 4;
		sprite = Sprite.player_forward;
	}
	
	private void move(){
		xa = 0;
		ya = 0;
		
		int px = level.getPlayerAt(0).getX();
		int py = level.getPlayerAt(0).getY();
		Vector2i start = new Vector2i(getX() >> 4, getY() >> 4);
		Vector2i destination = new Vector2i(px >> 4, py >> 4);
		if(time % 3 == 0)path = level.findPath(start, destination);
		if(path != null){
			if(path.size() > 0){
				Vector2i vec = path.get(path.size() - 1).tile;
				if(x < vec.getX() << 4) xa++;
				if(x > vec.getX() << 4) xa--;
				if(y < vec.getY() << 4) ya++;
				if(y > vec.getY() << 4) ya--;
			}
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
		time++;
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
