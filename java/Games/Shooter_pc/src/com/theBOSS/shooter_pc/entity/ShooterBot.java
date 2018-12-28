package com.theBOSS.shooter_pc.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.graphics.Sprite;

public class ShooterBot extends Entity{

	private double angle = 0.0, speed = 0.5;
	private int time = 0;
	private int randomCoolDown = 1, randX = 0, randY = 0;
	public int life = 100;
	
	public List<Bullet> bullets = new ArrayList<Bullet>();
	private Random rand = new Random();
	
	Player player;
	
	public ShooterBot(double x, double y, Sprite sprite, Player player) {
		super(x, y, sprite);
		this.player = player;
	}
	
	public void update() {
		angle = getOneOf16Anglea(Math.atan2((player.y + player.sprite.height / 2) - (y + sprite.height / 2), (player.x + player.sprite.width / 2) - (x + sprite.width / 2)));
		sprite = Sprite.rotate(Sprite.player, angle);
		if(time % 60 == 0) {
			shoot();
		}
		if(time % randomCoolDown == 0) {
			randX = rand.nextInt(3) - 1;
			randY = rand.nextInt(3) - 1;
			randomCoolDown = rand.nextInt(69) + 1 + time;
		}
		
		x += (randX) * speed;
		y += (randY) * speed;
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
		}
		time++;
	}

	public void render() {
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render();
		}
		Shooter.screen.renderSprite(sprite, (int) x, (int) y);
	}
	
	private void shoot() {
		for(int y = 0; y < sprite.height; y++) {
			for(int x = 0; x < sprite.width; x++) {
				if(sprite.pixels[x + y * width] == 0xf281807f) {
					bullets.add(new Bullet(x + this.x, y + this.y, 0.8, null, angle));	
					return;
				}
			}
		}
	}
	
	public double getOneOf16Anglea(double ang) {
		if(ang > -Math.PI / 16 && ang < Math.PI / 16) ang = 0;
		else if(ang > Math.PI / 16 && ang < 3 * Math.PI / 16) ang = Math.PI / 8;
		else if(ang > 3 * Math.PI / 16 && ang < 5 * Math.PI / 16) ang = Math.PI / 4;
		else if(ang > 5 * Math.PI / 16 && ang < 7 * Math.PI / 16) ang = 3 * Math.PI / 8;
		else if(ang > 7 * Math.PI / 16 && ang < 9 * Math.PI / 16) ang = Math.PI / 2;
		else if(ang > 9 * Math.PI / 16 && ang < 11 * Math.PI / 16) ang = 5 * Math.PI / 8;
		else if(ang > 11 * Math.PI / 16 && ang < 13 * Math.PI / 16) ang = 3 * Math.PI / 4;
		else if(ang > 13 * Math.PI / 16 && ang < 15 * Math.PI / 16) ang = 7 * Math.PI / 8;
		else if(ang < -Math.PI / 16 && ang > -3 * Math.PI / 16) ang = -Math.PI / 8;
		else if(ang < -3 * Math.PI / 16 && ang > -5 * Math.PI / 16) ang = -Math.PI / 4;
		else if(ang < -5 * Math.PI / 16 && ang > -7 * Math.PI / 16) ang = -3 * Math.PI / 8;
		else if(ang < -7 * Math.PI / 16 && ang > -9 * Math.PI / 16) ang = -Math.PI / 2;
		else if(ang < -9 * Math.PI / 16 && ang > -11 * Math.PI / 16) ang = -5 * Math.PI / 8;
		else if(ang < -11 * Math.PI / 16 && ang > -13 * Math.PI / 16) ang = -3 * Math.PI / 4;
		else if(ang < -13 * Math.PI / 16 && ang > -15 * Math.PI / 16) ang = -7 * Math.PI / 8;
		else ang = Math.PI;		
		return ang;
	}

}
