package com.mime.minefront.entity;

import com.mime.minefront.Game;

public class Entity {

	public double x, z, y;
	protected boolean removed;
    public static Game game;
	
	protected Entity() {
	}
	
	public void remove() {
		removed = true;
	}
	
	public void tick() {
		
	}
	
}
