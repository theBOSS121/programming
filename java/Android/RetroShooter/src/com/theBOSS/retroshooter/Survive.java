package com.theBOSS.retroshooter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Survive {
	
	Player player;
	Joystick movingJoystick, shootingJoystick;
	Camera camera;
	
	public List<Entity> bots;
	
	private int  time = 0;
	
	private double startCircle = 0;
	private boolean stopWithCircle = false;
	
	private Light l;
	
	private Random rand = new Random();
	
	private int sizeWidth = 500;
	private int sizeHeight = 500;
	
	public Survive(Joystick movingJoystick, Joystick shootingJoystick) {
		this.movingJoystick = movingJoystick;
		this.shootingJoystick = shootingJoystick;
		init();
	}
		
	private void init() {
		player = new Player(sizeWidth / 2, sizeHeight / 2, Renderer.player_sprite, movingJoystick, shootingJoystick, sizeWidth + Renderer.WIDTH / 2, sizeHeight + Renderer.HEIGHT / 2);
		camera = new Camera((int) player.x + player.width / 2 - Renderer.WIDTH / 2, (int) player.y + player.height / 2 - Renderer.HEIGHT / 2);
		bots = new ArrayList<Entity>();
		time = 0;
		startCircle = 0;
		stopWithCircle = false;
		
		l = new Light(30, 0xffffffff);
	}
	
	public void update() {
		Renderer.screen.setAmbientColor(0xff555555);
		if(!stopWithCircle) {
			startCircle += 0.08;
			if(startCircle >= 4 * Math.PI) stopWithCircle = true;
			if(startCircle < 2 * Math.PI) camera.update(player, (int) (Math.cos(startCircle - Math.PI / 2) * 25), (int) (Math.sin(startCircle - Math.PI / 2) * 25));
			else camera.update(player, (int) -(Math.cos(startCircle - Math.PI / 2) * 25), (int) (Math.sin(startCircle - Math.PI / 2) * 25));
			
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
		Renderer.screen.setOffSet(camera.getOffX(), camera.getOffY());
		if(stopWithCircle) time++;
	}
	
	private void spawn() {
		if(time < 500) {
			if(time % 250 == 0) {
				int which = rand.nextInt(3);
				int xx = rand.nextInt(sizeWidth) + Renderer.WIDTH / 2;
				int yy = rand.nextInt(sizeHeight) + Renderer.HEIGHT / 2;
				if(which == 0) {
					bots.add(new ChainBot(xx, yy, Renderer.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(xx, yy, Renderer.player_sprite, player));					
				}else {
					bots.add(new TeleportingBot(xx, yy, Renderer.player_sprite, player));
				}
			}
		}else if(time < 1000) {
			if(time % 150 == 0) {
				int which = rand.nextInt(3);
				int xx = rand.nextInt(sizeWidth) + Renderer.WIDTH / 2;
				int yy = rand.nextInt(sizeHeight) + Renderer.HEIGHT / 2;
				if(which == 0) {
					bots.add(new ChainBot(xx, yy, Renderer.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(xx, yy, Renderer.player_sprite, player));					
				}else {
					bots.add(new TeleportingBot(xx, yy, Renderer.player_sprite, player));
				}
			}
		}else if(time < 2000) {
			if(time % 100 == 0) {
				int which = rand.nextInt(3);
				int xx = rand.nextInt(sizeWidth) + Renderer.WIDTH / 2;
				int yy = rand.nextInt(sizeHeight) + Renderer.HEIGHT / 2;
				if(which == 0) {
					bots.add(new ChainBot(xx, yy, Renderer.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(xx, yy, Renderer.player_sprite, player));					
				}else {
					bots.add(new TeleportingBot(xx, yy, Renderer.player_sprite, player));
				}
			}
		}else if(time < 2500) {
			if(time % 80 == 0) {
				int which = rand.nextInt(3);
				int xx = rand.nextInt(sizeWidth) + Renderer.WIDTH / 2;
				int yy = rand.nextInt(sizeHeight) + Renderer.HEIGHT / 2;
				if(which == 0) {
					bots.add(new ChainBot(xx, yy, Renderer.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(xx, yy, Renderer.player_sprite, player));					
				}else {
					bots.add(new TeleportingBot(xx, yy, Renderer.player_sprite, player));
				}
			}
		}else if(time < 3000) {
			if(time % 60 == 0) {
				int which = rand.nextInt(3);
				int xx = rand.nextInt(sizeWidth) + Renderer.WIDTH / 2;
				int yy = rand.nextInt(sizeHeight) + Renderer.HEIGHT / 2;
				if(which == 0) {
					bots.add(new ChainBot(xx, yy, Renderer.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(xx, yy, Renderer.player_sprite, player));					
				}else {
					bots.add(new TeleportingBot(xx, yy, Renderer.player_sprite, player));
				}
			}
		}else if(time < 3500) {
			if(time % 40 == 0) {
				int which = rand.nextInt(3);
				int xx = rand.nextInt(sizeWidth) + Renderer.WIDTH / 2;
				int yy = rand.nextInt(sizeHeight) + Renderer.HEIGHT / 2;
				if(which == 0) {
					bots.add(new ChainBot(xx, yy, Renderer.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(xx, yy, Renderer.player_sprite, player));					
				}else {
					bots.add(new TeleportingBot(xx, yy, Renderer.player_sprite, player));
				}
			}
		}else {
			if(time % 30 == 0) {
				int which = rand.nextInt(3);
				int xx = rand.nextInt(sizeWidth) + Renderer.WIDTH / 2;
				int yy = rand.nextInt(sizeHeight) + Renderer.HEIGHT / 2;
				if(which == 0) {
					bots.add(new ChainBot(xx, yy, Renderer.saw, player));
				}else if(which == 1) {
					bots.add(new ShooterBot(xx, yy, Renderer.player_sprite, player));					
				}else {
					bots.add(new TeleportingBot(xx, yy, Renderer.player_sprite, player));
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
		Renderer.screen.renderLight(l, (int) player.x + player.width / 2, (int) player.y + player.height / 2);
	}

}
