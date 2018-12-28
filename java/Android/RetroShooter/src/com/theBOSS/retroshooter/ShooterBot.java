package com.theBOSS.retroshooter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		sprite = Sprite.rotate(Renderer.player_sprite, angle);
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
		Renderer.screen.renderSprite(sprite, (int) x, (int) y);
	}
	
	private void shoot() {
		int x = Renderer.player_sprite.width / 2;
		int y = Renderer.player_sprite.height / 2;

		if(angle < 0 && angle > -Math.PI / 2) x+=2;
		if(angle < -3 * Math.PI / 4 ) x--;
		 
		bullets.add(new Bullet(x + this.x, y + this.y, 1.2, null, angle));				
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
