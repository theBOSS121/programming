package com.theBOSS.retroshooter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity{

	private double speed = 2.0;
	private double fallSpeed = 0;
	private double angle = -Math.PI / 2;
	private boolean onGround = false;
	public int life = 500;
	private int sizeX = 0, sizeY = 0;
	
	Joystick movingJoystick, shootingJoystick;
	
	public List<Bullet> bullets = new ArrayList<Bullet>();
	private boolean readyToShoot = false;
	
	public boolean isDead = false;
	
	public Player(double x, double y, Sprite sprite, Joystick movingJoystick, Joystick shootingJoystick, int sizeX, int sizeY) {
		super(x, y, sprite);
		this.movingJoystick = movingJoystick;
		this.shootingJoystick = shootingJoystick;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public void update() {
		moving();
		
		//correction for the camera
		if(x < Renderer.WIDTH / 2) x = Renderer.WIDTH / 2;
		if(y < Renderer.HEIGHT / 2) y = Renderer.HEIGHT / 2;
		if(x > sizeX) x = sizeX;
		if(y > sizeY) y = sizeY;
		///////////////////////////

		if(shootingJoystick.inTheMiddle && !movingJoystick.inTheMiddle) {
			angle = movingJoystick.angle;
		}else if(!shootingJoystick.inTheMiddle){
			angle = shootingJoystick.angle;
		}
		sprite = Sprite.rotate(Renderer.player_sprite, angle);
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
		}
		
		if(!shootingJoystick.inTheMiddle) readyToShoot = true; 
		
		if(readyToShoot  && shootingJoystick.inTheMiddle) {shoot(); readyToShoot = false;}
		
		
		if(Game.state == Game.ONLINE) {
			String data = "/u/" + ClientThread.id + "/x/" + (int) x + "/y/" + (int) y + "/a/" + angle + "/e/";
			if(Menu.isClient) {				
				try {
					ClientThread.send(data.getBytes(), InetAddress.getByName(RetroShooter.ipAddress));
				}catch(UnknownHostException e) {
					e.printStackTrace();
				}
			}else {
				for(int i = 0; i < ServerThread.clients.size(); i++) {
					ServerThread.send(data.getBytes(), ServerThread.clients.get(i).address, ServerThread.clients.get(i).port);
				}
			}
		}
		if(life <= 0) isDead = true;
	}

	private void moving() {		
		if(movingJoystick.x > 10) {
			if(movingJoystick.x > 10 + 8) {
				speed = 2.0;
			}else {
				speed = 1.0;
			}
			x += speed;
		}else if(movingJoystick.x < 10) {
			if(movingJoystick.x < 10 - 8) {
				speed = 2.0;
			}else {
				speed = 1.0;
			}
			x -= speed;
		}
		if(movingJoystick.y > 141) {
			if(movingJoystick.y > 141 + 8) {
				speed = 2.0;
			}else {
				speed = 1.0;
			}
			y += speed;
		}else if(movingJoystick.y < 141) {
			if(movingJoystick.y < 141 - 8) {
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
		
		int x = Renderer.player_sprite.height / 2;
		int y = Renderer.player_sprite.width / 2;
		
		if(angle < 0 && angle > -Math.PI / 2) x++;
		if(angle > Math.PI / 2 && angle < Math.PI) x--;
		
			Renderer.screen.renderLine((int) (x + this.x), (int) (y + this.y), xDir, yDir, col);
			if((xDir == 2 || xDir == -2) && (yDir == 1 || yDir == -1)) {
//				Shooter.screen.renderLine((int) (x + this.x + 1), (int) (y + this.y), xDir, yDir, col);
				Renderer.screen.renderLine((int) (x + this.x - 1), (int) (y + this.y), xDir, yDir, col);
			}
			if((xDir == 1 || xDir == -1) && (yDir == 2 || yDir == -2)) {
//				Shooter.screen.renderLine((int) (x + this.x), (int) (y + this.y + 1), xDir, yDir, col);
				Renderer.screen.renderLine((int) (x + this.x), (int) (y + this.y - 1), xDir, yDir, col);
			}
				
			
		
	}
	
	private void shoot() {
		int x = Renderer.player_sprite.width / 2;
		int y = Renderer.player_sprite.height / 2;

		if(angle < 0 && angle > -Math.PI / 2) x+=2;
		if(angle < -3 * Math.PI / 4 ) x--;
		 
		double xx = x + this.x;
		double yy = y + this.y;
		double ang = shootingJoystick.angle;
		
		bullets.add(new Bullet(xx, yy, 1.2, null, ang));		
		
		if(Game.state == Game.ONLINE) {
			String data = "/b/" + ClientThread.id + "/x/" + (int) xx + "/y/" + (int) yy + "/a/" + ang + "/e/";			
			if(Menu.isClient) {
				try {
					ClientThread.send(data.getBytes(), InetAddress.getByName(RetroShooter.ipAddress));
				}catch(UnknownHostException e) {
					e.printStackTrace();
				}
			}else {
				if(life > 0) {
					for(int i = 0; i < ServerThread.clients.size(); i++) {
						ServerThread.send(data.getBytes(), ServerThread.clients.get(i).address, ServerThread.clients.get(i).port);
					}
				}
			}
		}
	}

	public void render() {
		if(!shootingJoystick.inTheMiddle) setDirsAndDrawLine(shootingJoystick.angle, 0x778888ff);
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render();
		}
		Renderer.screen.renderSprite(sprite, (int) x, (int) y);
	}
	
}
