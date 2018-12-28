package com.theBOSS.shooter_pc.entity;

import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.graphics.Sprite;

public class Bullet extends Entity{
	
	private double xDir = 0, yDir = 0, speed = 0;	
	
	public Bullet(double x, double y, double speed, Sprite sprite, double angle) {
		super(x, y, sprite);
		this.speed = speed;
		setDirs(angle);
	}
	
	private void setDirs(double angle) {
		if(angle == 0) {xDir = 2 * speed; yDir = 0 * speed;}
		if(angle == Math.PI / 8) {xDir = 2 * speed; yDir = 1 * speed;}
		if(angle == Math.PI / 4) {xDir = 2 * speed; yDir = 2 * speed;}
		if(angle == 3 * Math.PI / 8) {xDir = 1 * speed; yDir = 2 * speed;}
		if(angle == Math.PI / 2) {xDir = 0 * speed; yDir = 2 * speed;}
		if(angle == 5 * Math.PI / 8) {xDir = -1 * speed; yDir = 2 * speed;}
		if(angle == 3 * Math.PI / 4) {xDir = -2 * speed; yDir = 2 * speed;}
		if(angle == 7 * Math.PI / 8) {xDir = -2 * speed; yDir = 1 * speed;}
		if(angle == Math.PI) {xDir = -2 * speed; yDir = 0 * speed;}
		if(angle == -7 * Math.PI / 8) {xDir = -2 * speed; yDir = -1 * speed;}
		if(angle == -3 * Math.PI / 4) {xDir = -2 * speed; yDir = -2 * speed;}
		if(angle == -5 * Math.PI / 8) {xDir = -1 * speed; yDir = -2 * speed;}
		if(angle == -Math.PI / 2) {xDir = 0 * speed; yDir = -2 * speed;}
		if(angle == -3 * Math.PI / 8) {xDir = 1 * speed; yDir = -2 * speed;}
		if(angle == -Math.PI / 4) {xDir = 2 * speed; yDir = -2 * speed;}
		if(angle == -Math.PI / 8) {xDir = 2 * speed; yDir = -1 * speed;}
	}

	public void update() {
		x += xDir;
		y += yDir;
	}
	
	public void render() {
		Shooter.screen.renderPixel((int) x, (int) y, 0xff000000);
	}

}
