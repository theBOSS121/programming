package com.bombit.entity.mob;

import java.util.Random;

import com.bombit.entity.Bomb;
import com.bombit.graphics.AnimatedSprite;
import com.bombit.graphics.Screen;
import com.bombit.graphics.Sprite;
import com.bombit.graphics.SpriteSheet;
import com.bombit.input.Keyboard;
import com.bombit.level.tile.Tile;

public class Bot extends Mob{

	private String name;
	private Keyboard input;
	public Sprite sprite;
	private int anim = 0;
	private int reloadTime = 0;
	private int timer = 0;
	public int rand;
	public int dir = -1;
	public int col = -1;
	
	public AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 3);
	public AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 3);
	public AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 3);
	public AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 3);

	private int fireRate = 0;

	public AnimatedSprite animSprite = down;
	
	
	
	public Bot(String name) {
		this.name = name;
		sprite = Sprite.player_forward;
	}

	public Bot(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		sprite = Sprite.player_forward;
	}
	
	public String getName(){
		return name;
	}

	public void update() {
		timer++;
		Random random = new Random();
		if(timer % 60 == 0) {
			rand = random.nextInt(4);
		}
		if(dir != -1) {
			rand = dir;
		}
		
		if(walking) animSprite.update();
		else animSprite.setFrame(0);
		double xa = 0, ya = 0;
		if (rand == 0){
			xa -= speed;
			animSprite = left;
		}
		if (rand == 1){ 
			xa += speed;
			animSprite = right;
			
		}
		if (rand == 2){
			ya -= speed;
			animSprite = up;
		}
		if (rand == 3){
			ya += speed;
			animSprite = down;
		}
		
		if(bombs > 0) {
			if(reloadTime == 0) {
				if(collision()) {
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
	

	private boolean collision() {
		int xx = (int) Math.round((this.x - 8) / 16);
		int yy = (int) Math.round(this.y / 16);
		int i = 0;
		if(level.getTile(xx + 1, yy) == Tile.spawn_wall1 || level.getTile(xx + 1, yy) == Tile.spawn_wall2) {
			i++;
		}else {
			dir = 1;
		}
		if(level.getTile(xx - 1, yy) == Tile.spawn_wall1 || level.getTile(xx - 1, yy) == Tile.spawn_wall2) {
			i++;
		}else {
			dir = 0;
		}
		if(level.getTile(xx, yy + 1) == Tile.spawn_wall1 || level.getTile(xx, yy + 1) == Tile.spawn_wall2) {
			i++;
		}else {
			dir = 3;
		}
		if(level.getTile(xx, yy - 1) == Tile.spawn_wall1 || level.getTile(xx, yy - 1) == Tile.spawn_wall2) {
			i++;
		}else {
			dir = 2;
		}
		
		if(i == 3) {
			return true;
		}
		dir = -1;
		return false;
	}

	public void render(Screen screen) {
		int flip = 0;
		sprite = animSprite.getSprite();
		screen.renderMob((int) x - 16, (int) y - 16, sprite, this);
	}
}
