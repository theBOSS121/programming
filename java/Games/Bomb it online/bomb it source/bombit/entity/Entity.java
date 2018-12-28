package com.bombit.entity;

import java.util.Random;

import com.bombit.entity.mob.Bot;
import com.bombit.entity.mob.Mob;
import com.bombit.graphics.Screen;
import com.bombit.graphics.Sprite;
import com.bombit.level.Level;

public class Entity {
	
	public double x;
	public double y;
	public Sprite sprite;
	private boolean removed = false;
	protected Level level;
	protected final Random random = new Random();
	
	public Entity(){}
	
	public Entity(int x, int y, Sprite sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void update(){}
	
	public void render(Screen screen){
		if(sprite != null) screen.renderSprite((int)x, (int)y, sprite, true);
	}
	
	public void remove(){
		//Remove from level
		removed = true;
	}
	
	public int getX(){
		return (int) x;
	}
	
	public int getY(){
		return (int) y;
	}
	
	public Sprite getSprite(){
		return sprite;
	}
	
	public boolean isRemoved(){
		return removed;
	}
	
	public void init(Level level){
		this.level = level;
	}
	
	protected boolean collision(double xa, double ya, Mob mob){
		boolean solid = false;
		for(int c = 0; c < 4; c++){
			double xt = ((x + xa) - 8) / 16;
			double yt = ((y + ya) - c / 2 * 30 + 15) / 16;
			int ix = (int) Math.ceil(xt);
			int iy = (int) Math.ceil(yt);
			if(c % 2 == 0) ix = (int) Math.floor(xt);
			if(c / 2 == 0) iy = (int) Math.floor(yt);
			if(level.getTile(ix, iy).solid()) {
				solid = true;
			}
			for(int i = 0; i < level.bombs.size(); i++) {
				Bomb b = level.bombs.get(i);
				if(ix == Math.round(b.x / 16 - 0.5) && iy == Math.round(b.y / 16 - 0.5) && mob.lefted == true) {
					solid = true;
				}
			}
		}
		return solid;
	}
	
}













