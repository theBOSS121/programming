package com.theBOSS.shooter_pc.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.theBOSS.shooter_pc.Camera;
import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.entity.ChainBot;
import com.theBOSS.shooter_pc.entity.Entity;
import com.theBOSS.shooter_pc.entity.Player;
import com.theBOSS.shooter_pc.entity.ShooterBot;
import com.theBOSS.shooter_pc.entity.TeleportingBot;
import com.theBOSS.shooter_pc.graphics.Light;
import com.theBOSS.shooter_pc.graphics.Sprite;
import com.theBOSS.shooter_pc.input.Joystick;

public class Survive {
	
	Player player;
	Joystick movingJoystick, shootingJoystick;
	Camera camera;
	
	public List<Entity> bots;
	
	private int  time = 0;
	
	private double startCircle = 0;
	private boolean stopWithCircle = false;
	
	Light l;
	
	private Random rand = new Random();
	
	public Survive(Joystick movingJoystick, Joystick shootingJoystick) {
		this.movingJoystick = movingJoystick;
		this.shootingJoystick = shootingJoystick;
		init();
	}
		
	private void init() {
		player = new Player(Shooter.WIDTH, Shooter.HEIGHT, Sprite.player, movingJoystick, shootingJoystick);
		camera = new Camera((int) player.x + player.width / 2 - Shooter.WIDTH / 2, (int) player.y + player.height / 2 - Shooter.HEIGHT / 2);
		bots = new ArrayList<Entity>();
		time = 0;
		startCircle = 0;
		stopWithCircle = false;
		
		l = new Light(40, 0xffffffff);
	}
	
	public void update() {
		Shooter.screen.setAmbientColor(0xff555555);
		if(!stopWithCircle) {
			startCircle += 0.1;
			if(startCircle >= 4 * Math.PI) stopWithCircle = true;
			if(startCircle < 2 * Math.PI) camera.update(player, (int) (Math.cos(startCircle - Math.PI / 2) * 40), (int) (Math.sin(startCircle - Math.PI / 2) * 40));
			else camera.update(player, (int) -(Math.cos(startCircle - Math.PI / 2) * 40), (int) (Math.sin(startCircle - Math.PI / 2) * 40));
			
		}else {
			camera.update(player, 0, 0);
		}
		player.update();
		for(int i = 0; i < bots.size(); i++) {
			bots.get(i).update();
		}
		
		if(stopWithCircle) {
			spawn();
		}
		
		if(player.life <= 0) init();
		
		
		checkForCollisoins();
		Shooter.screen.setOffSet(camera.getOffX(), camera.getOffY());
		if(stopWithCircle) time++;
	}
	
	private void spawn() {
		if(time < 500) {
			if(time % 250 == 0) {
				int which = rand.nextInt(3);
				if(which == 0) {
					bots.add(new ChainBot(100, 100, Sprite.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(100, 100, Sprite.player, player));					
				}else {
					bots.add(new TeleportingBot(100, 100, Sprite.player, player));
				}
			}
		}else if(time < 1000) {
			if(time % 150 == 0) {
				int which = rand.nextInt(3);
				if(which == 0) {
					bots.add(new ChainBot(100, 100, Sprite.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(100, 100, Sprite.player, player));					
				}else {
					bots.add(new TeleportingBot(100, 100, Sprite.player, player));
				}
			}
		}else if(time < 2000) {
			if(time % 100 == 0) {
				int which = rand.nextInt(3);
				if(which == 0) {
					bots.add(new ChainBot(100, 100, Sprite.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(100, 100, Sprite.player, player));					
				}else {
					bots.add(new TeleportingBot(100, 100, Sprite.player, player));
				}
			}
		}else if(time < 2500) {
			if(time % 80 == 0) {
				int which = rand.nextInt(3);
				if(which == 0) {
					bots.add(new ChainBot(100, 100, Sprite.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(100, 100, Sprite.player, player));					
				}else {
					bots.add(new TeleportingBot(100, 100, Sprite.player, player));
				}
			}
		}else if(time < 3000) {
			if(time % 60 == 0) {
				int which = rand.nextInt(3);
				if(which == 0) {
					bots.add(new ChainBot(100, 100, Sprite.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(100, 100, Sprite.player, player));					
				}else {
					bots.add(new TeleportingBot(100, 100, Sprite.player, player));
				}
			}
		}else if(time < 3500) {
			if(time % 40 == 0) {
				int which = rand.nextInt(3);
				if(which == 0) {
					bots.add(new ChainBot(100, 100, Sprite.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(100, 100, Sprite.player, player));					
				}else {
					bots.add(new TeleportingBot(100, 100, Sprite.player, player));
				}
			}
		}else {
			if(time % 30 == 0) {
				int which = rand.nextInt(3);
				if(which == 0) {
					bots.add(new ChainBot(100, 100, Sprite.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(100, 100, Sprite.player, player));					
				}else {
					bots.add(new TeleportingBot(100, 100, Sprite.player, player));
				}
			}
		}
	}

	private void checkForCollisoins() {
		for(int i = 0; i < bots.size(); i++) {
			Entity e = bots.get(i);
			if(e instanceof ChainBot) {
				//for player
				if(player.x + player.width > e.x && player.x < e.x + e.width && player.y + player.height > e.y && player.y < e.y + e.height) {
					player.life--;;
				}
				//for bot
				for(int j = 0; j < player.bullets.size(); j++) {
					if(e.x + e.width > player.bullets.get(j).x && e.x < player.bullets.get(j).x && e.y + e.height > player.bullets.get(j).y && e.y < player.bullets.get(j).y) {
						((ChainBot) e).life -= 50;
						if(((ChainBot) e).life <= 0) e.remove = true;
						player.bullets.remove(j);
						break;
					}
				}
			}
			else if(e instanceof ShooterBot) {
				ShooterBot s = (ShooterBot) e;
				for(int j = 0 ; j < s.bullets.size(); j++) {
					if(player.x + player.width > s.bullets.get(j).x && player.x < s.bullets.get(j).x && player.y + player.height > s.bullets.get(j).y && player.y < s.bullets.get(j).y) {
						player.life--;
						s.bullets.remove(j);
						break;
					}
				}
				for(int j = 0; j < player.bullets.size(); j++) {
					if(e.x + e.width > player.bullets.get(j).x && e.x < player.bullets.get(j).x && e.y + e.height > player.bullets.get(j).y && e.y < player.bullets.get(j).y) {
						player.bullets.remove(j);
						((ShooterBot) e).life -= 50;
						if(((ShooterBot) e).life <= 0) e.remove = true;
						break;
					}
				}
			}
			else if(e instanceof TeleportingBot) {
				TeleportingBot t = (TeleportingBot) e;
				for(int j = 0 ; j < t.bullets.size(); j++) {
					if(player.x + player.width > t.bullets.get(j).x && player.x < t.bullets.get(j).x && player.y + player.height > t.bullets.get(j).y && player.y < t.bullets.get(j).y) {
						player.life--;
						t.bullets.remove(j);
						break;
					}
				}
				for(int j = 0; j < player.bullets.size(); j++) {
					if(e.x + e.width > player.bullets.get(j).x && e.x < player.bullets.get(j).x && e.y + e.height > player.bullets.get(j).y && e.y < player.bullets.get(j).y) {
						((TeleportingBot) e).life -= 100;
						if(((TeleportingBot) e).life <= 0) e.remove = true;
						player.bullets.remove(j);
						break;
					}
				}
			}
		}
		
		for(int i = 0; i < bots.size(); i++) {
			if(bots.get(i).remove) {
				bots.remove(i);
			}
		}
	}
	
	public void render() {
		player.render();
		for(int i = 0; i < bots.size(); i++) {
			bots.get(i).render();
		}
		
		l.clear();
		Shooter.screen.renderLight(l, (int) player.x + player.width / 2, (int) player.y + player.height / 2);
	}

}
