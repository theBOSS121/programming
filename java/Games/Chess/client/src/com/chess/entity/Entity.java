package com.chess.entity;

import java.util.Random;

import com.chess.graphics.Screen;
import com.chess.graphics.Sprite;
import com.chess.level.Level;

public class Entity {
	
	public int x;
	public int lastX;
	public int y;
	public int lastY;
	public int col;
	public Sprite sprite;
	private boolean removed = false;
	protected Level level;
	protected final Random random = new Random();
	public boolean moved = false;
	
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
	
	public int getPiecesX() {
		return x / 16;
	}
	
	public int getPiecesY() {
		return y / 16;
	}
	
	public boolean isAllowed() {
		return false;
	}
	
	public boolean inBetween(int lx, int ly, int nx, int ny) {
		if(lx == nx) {
			for(int i = 0; i < level.entities.size(); i++) {
				Entity e = level.entities.get(i);
				if(e.x == nx) {
					if((e.y < ly && e.y > ny) ||(e.y > ly && e.y < ny)) {
						return true;
					}
				}
			}
			return false;
		}
		if(ly == ny) {
			for(int i = 0; i < level.entities.size(); i++) {
				Entity e = level.entities.get(i);
				if(e.y == ny) {
					if((e.x < lx && e.x > nx) ||(e.x > lx && e.x < nx)) {
						return true;
					}
				}
			}
			return false;
		}
		if(lx < nx && ly < ny) {
			for(int i = 1; i < nx / 16 - lx / 16; i++) {
				for(int a = 0; a < level.entities.size(); a++) {
					Entity e = level.entities.get(a);
					if(lx / 16 + i == e.x / 16 && ly / 16 + i == e.y / 16) {
						return true;
					}
				}
			}
			return false;
		}
		if(lx > nx && ly > ny) {
			for(int i = 1; i < lx / 16 - nx / 16; i++) {
				for(int a = 0; a < level.entities.size(); a++) {
					Entity e = level.entities.get(a);
					if(lx / 16 - i == e.x / 16 && ly / 16 - i == e.y / 16) {
						return true;
					}
				}
			}
			return false;
		}
		if(lx > nx && ly < ny) {
			for(int i = 1; i < lx / 16 - nx / 16; i++) {
				for(int a = 0; a < level.entities.size(); a++) {
					Entity e = level.entities.get(a);
					if(lx / 16 - i == e.x / 16 && ly / 16 + i == e.y / 16) {
						return true;
					}
				}
			}
			return false;
		}
		if(lx < nx && ly > ny) {
			for(int i = 1; i < nx / 16 - lx / 16; i++) {
				for(int a = 0; a < level.entities.size(); a++) {
					Entity e = level.entities.get(a);
					if(lx / 16 + i == e.x / 16 && ly / 16 - i == e.y / 16) {
						return true;
					}
				}
			}
			return false;
		}
		
		return false;
	}
	
}













