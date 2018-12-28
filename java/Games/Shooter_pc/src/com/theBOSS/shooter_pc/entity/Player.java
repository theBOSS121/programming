package com.theBOSS.shooter_pc.entity;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.graphics.Sprite;
import com.theBOSS.shooter_pc.input.Joystick;
import com.theBOSS.shooter_pc.input.Keyboard;

public class Player extends Entity{

	private double speed = 2.0;
	private double fallSpeed = 0;
	private double angle = -Math.PI / 2;
	private boolean onGround = false;
	public int life = 500;
	
	Joystick movingJoystick, shootingJoystick;
	
	public List<Bullet> bullets = new ArrayList<Bullet>();
	private boolean readyToShoot = false;
	
	public Player(double x, double y, Sprite sprite, Joystick movingJoystick, Joystick shootingJoystick) {
		super(x, y, sprite);
		this.movingJoystick = movingJoystick;
		this.shootingJoystick = shootingJoystick;
	}

	public void update() {
		moving();
		
		//correction for the camera
		if(x < Shooter.WIDTH / 2) x = Shooter.WIDTH / 2;
		if(y < Shooter.HEIGHT / 2) y = Shooter.HEIGHT / 2;
		///////////////////////////

		if(shootingJoystick.inTheMiddle && !movingJoystick.inTheMiddle) {
			angle = movingJoystick.angle;
		}else if(!shootingJoystick.inTheMiddle){
			angle = shootingJoystick.angle;
		}
		sprite = Sprite.rotate(Sprite.player, angle);
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
		}
		
		if(!shootingJoystick.inTheMiddle) readyToShoot = true; 
		
		if(readyToShoot  && shootingJoystick.inTheMiddle) {shoot(); readyToShoot = false;}
		
		
//		System.out.println("PlayerCLASS Life:" + life);
	}

	private void moving() {
		if(Keyboard.key(KeyEvent.VK_SHIFT)) {
			speed = 1.0;
		}else {
			speed = 2.0;
		}
		if(Keyboard.key(KeyEvent.VK_W)) {
			y += -speed;
		}
		if(Keyboard.key(KeyEvent.VK_S)) {
			y += speed;
		}
		if(Keyboard.key(KeyEvent.VK_A)) {
			x += -speed;
		}
		if(Keyboard.key(KeyEvent.VK_D)) {
			x += speed;
		}
		
		
		if(movingJoystick.x > 10) {
			if(movingJoystick.x > 10 + 5) {
				speed = 2.0;
			}else {
				speed = 1.0;
			}
			x += speed;
		}else if(movingJoystick.x < 10) {
			if(movingJoystick.x < 10 - 5) {
				speed = 2.0;
			}else {
				speed = 1.0;
			}
			x -= speed;
		}
		if(movingJoystick.y > 141) {
			if(movingJoystick.y > 141 + 5) {
				speed = 2.0;
			}else {
				speed = 1.0;
			}
			y += speed;
		}else if(movingJoystick.y < 141) {
			if(movingJoystick.y < 141 - 5) {
				speed = 2.0;
			}else {
				speed = 1.0;
			}
			y -= speed;
		}		
	}

	private void setDirsAndDrawLine(double angle, int col) {
		int xDir = 0, yDir = 0;
		if(angle == 0) {xDir = 1; yDir = 0;}
		if(angle == Math.PI / 8) {xDir = 2; yDir = 1;}
		if(angle == Math.PI / 4) {xDir = 1; yDir = 1;}
		if(angle == 3 * Math.PI / 8) {xDir = 1; yDir = 2;}
		if(angle == Math.PI / 2) {xDir = 0; yDir = 1;}
		if(angle == 5 * Math.PI / 8) {xDir = -1; yDir = 2;}
		if(angle == 3 * Math.PI / 4) {xDir = -1; yDir = 1;}
		if(angle == 7 * Math.PI / 8) {xDir = -2; yDir = 1;}
		if(angle == Math.PI) {xDir = -1; yDir = 0;}
		if(angle == -7 * Math.PI / 8) {xDir = -2; yDir = -1;}
		if(angle == -3 * Math.PI / 4) {xDir = -1; yDir = -1;}
		if(angle == -5 * Math.PI / 8) {xDir = -1; yDir = -2;}
		if(angle == -Math.PI / 2) {xDir = 0; yDir = -1;}
		if(angle == -3 * Math.PI / 8) {xDir = 1; yDir = -2;}
		if(angle == -Math.PI / 4) {xDir = 1; yDir = -1;}
		if(angle == -Math.PI / 8) {xDir = 2; yDir = -1;}
		for(int y = 0; y < sprite.height; y++) {
			for(int x = 0; x < sprite.width; x++) {
				if(sprite.pixels[x + y * width] == 0xf281807f) {
					Shooter.screen.renderLine((int) (x + this.x), (int) (y + this.y), xDir, yDir, col);
					if((xDir == 2 || xDir == -2) && (yDir == 1 || yDir == -1)) {
//						Shooter.screen.renderLine((int) (x + this.x + 1), (int) (y + this.y), xDir, yDir, col);
						Shooter.screen.renderLine((int) (x + this.x - 1), (int) (y + this.y), xDir, yDir, col);
					}
					if((xDir == 1 || xDir == -1) && (yDir == 2 || yDir == -2)) {
//						Shooter.screen.renderLine((int) (x + this.x), (int) (y + this.y + 1), xDir, yDir, col);
						Shooter.screen.renderLine((int) (x + this.x), (int) (y + this.y - 1), xDir, yDir, col);
					}
					return;
				}
			}
		}
	}
	
	private void shoot() {
		for(int y = 0; y < sprite.height; y++) {
			for(int x = 0; x < sprite.width; x++) {
				if(sprite.pixels[x + y * width] == 0xf281807f) {
					bullets.add(new Bullet(x + this.x, y + this.y, 1.2, null, shootingJoystick.angle));	
					return;
				}
			}
		}
	}

	public void render() {
		if(!shootingJoystick.inTheMiddle) setDirsAndDrawLine(shootingJoystick.angle, 0x778888ff);
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render();
		}
		Shooter.screen.renderSprite(sprite, (int) x, (int) y);
	}
	
}
