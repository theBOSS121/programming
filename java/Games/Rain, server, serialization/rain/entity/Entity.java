package com.thecherno.rain.entity;

import java.util.Random;

import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.level.Level;

public class Entity {
	
	protected double x, y;
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
	
}













