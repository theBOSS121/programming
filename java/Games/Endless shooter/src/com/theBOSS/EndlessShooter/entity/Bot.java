package com.theBOSS.EndlessShooter.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.theBOSS.EndlessShooter.EndlessShooter;
import com.theBOSS.EndlessShooter.graphics.Screen;
import com.theBOSS.EndlessShooter.graphics.Sprite;

public class Bot extends Entity{

	Player p;
	private int flip = 0;
	private int time = 1;
	private double dir = 0.0;
	private boolean walking = false;
	private double speed = 2.5;
	boolean coolDown = false;
	boolean dead = false;
	int deadTime = 0;
	
	public int life = 100;
	
	public List<Bullet> bullets = new ArrayList<Bullet>();

	private Random rand = new Random();
	
	private int walkDir = 0;
	
	public Bot(int x, int y, Sprite sprite, Player p) {
		super(x, y, sprite);
		this.p = p;
	}
	
	private void walkStuff() {
		if(time % 30 == 0) {
			walkDir = rand.nextInt(9);
		}
		if(walkDir == 0) {
			walking = false;
		}else {
			walking = true;
			if(walkDir == 1 && x + width < EndlessShooter.WIDTH) x += speed;
			else if(walkDir == 2 && x > 0) x -= speed;
			else if(walkDir == 3 && y > 0) y -=speed;
			else if(walkDir == 4 && y + height < EndlessShooter.HEIGHT) y += speed;
			else if(walkDir == 5 && x > 0 && y > 0) {
				x -= speed;
				y -= speed;
			}
			else if(walkDir == 6 && x > 0 && y + height < EndlessShooter.HEIGHT) { 
				x -= speed;
				y += speed;
			}
			else if(walkDir == 7 && y > 0 && x + width < EndlessShooter.WIDTH) {
				y -= speed;
				x += speed;
			}
			else if(walkDir == 8 && x + width < EndlessShooter.WIDTH && y + height < EndlessShooter.HEIGHT) {
				x += speed;
				y += speed;
			}
			
		}
		
	}

	public void update() {
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
		}
		if(!dead) {			
			walkStuff();
	
			if(time % 10 == 0 && walking) {
				if(sprite == Sprite.p_front1) sprite = Sprite.p_front2;
				else if(sprite == Sprite.p_front2) sprite = Sprite.p_front3;
				else if(sprite == Sprite.p_front3) sprite = Sprite.p_front1;
			}
			if(!walking) {
				sprite = Sprite.p_front3;
			}
	
			double dx = p.x + p.width / 2 - x - width / 2;
			double dy = p.y + p.height / 2 - y - height / 2;
			double dir = Math.atan2(dy, dx);
			rotatedSprite = Sprite.rotate(sprite, dir);
	
	
			if(time % 90 == 0) {
				bullets.add(new Bullet((int) x + width / 2, (int) y + height / 2, Sprite.bullet, dir, 5));
			}
		}
		
		for(int i = 0; i < bullets.size(); i++)	{
			if(bullets.get(i).remove) bullets.remove(i);
			
		}
		if(life <= 0) {
			dead = true;
			life = 0;
			rotatedSprite = Sprite.rotate(Sprite.p_dead, dir);
		}
		if(dead) {
			deadTime++;
			if(deadTime >= 60) {
				remove = true;
			}
		}

		time++;
	}

	public void render(Screen screen) {
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(screen);
		}
		screen.renderEntity(this, flip);
		if(!dead) screen.renderHealthBar((int) x,(int) y, life / 100.0, 2, 1);

	}

}
