package com.theBOSS.Nalis_pc.entity;

import com.theBOSS.Nalis_pc.Nails;
import com.theBOSS.Nalis_pc.graphics.Screen;
import com.theBOSS.Nalis_pc.graphics.Sprite;

public class Player extends Entity{

	private double speed = 3.5;
	private double fallSpeed = 0;
	private double xDir = 0, yDir = 0;
	private boolean onGround = false;
	
	public Player(double x, double y, Sprite sprite) {
		super(x, y, sprite);
	}

	public void update() {
		xDir = 0;
		yDir = 0;
		
		if(!onGround && fallSpeed <= 4.0) {
			fallSpeed += 1.0;
		}
		fallSpeed = 0.0;
		yDir += fallSpeed;
		
		x += xDir;
		y += yDir;
	}
	
	public void render() {
		Nails.screen.renderSprite(sprite, (int) x, (int) y);
	}
	
}
