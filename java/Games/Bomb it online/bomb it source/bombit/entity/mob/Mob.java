package com.bombit.entity.mob;

import com.bombit.entity.Entity;
import com.bombit.graphics.Screen;

public abstract class Mob extends Entity{
	
	protected boolean moving = false;	
	protected boolean walking = false;
	protected int health;
	public int bombs = 1;
	public boolean lefted = true;
	public int range = 1;
	public double speed = 1.0;

	protected enum Direction{
		UP, DOWN, LEFT, RIGHT
	}
	
	protected Direction dir;
		
	public void move(double xa, double ya, Mob mob){
		if(xa != 0 && ya != 0){
			move(xa, 0, mob);
			move(0, ya, mob);
			return;
		}
		if(xa > 0) dir = Direction.RIGHT;
		if(xa < 0) dir = Direction.LEFT;
		if(ya > 0) dir = Direction.DOWN;
		if(ya < 0) dir = Direction.UP;
		
		while(xa != 0){
			if(Math.abs(xa) > 1){
				if(!collision(abs(xa), ya, mob)){
					this.x += abs(xa);
				}
				xa -= abs(xa);
			}else{
				if(!collision(abs(xa), ya, mob)){
					this.x += xa;
				}
				xa = 0;
			}
		}
		while(ya != 0){
			if(Math.abs(ya) > 1){
				if(!collision(xa, abs(ya), mob)){
					this.y += abs(ya);
				}
				ya -= abs(ya);
			}else{
				if(!collision(xa, abs(ya), mob)){
					this.y += ya;
				}
				ya = 0;
			}
		}			
	}
	
	private int abs(double value){
		if(value < 0) return -1;
		return 1;
	}
	
	public abstract void update();
	
	public abstract void render(Screen screen);
	
	
	
}
