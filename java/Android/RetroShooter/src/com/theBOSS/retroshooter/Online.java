package com.theBOSS.retroshooter;

import java.util.Random;

public class Online {
	
	Player player;
	Joystick movingJoystick, shootingJoystick;
	Camera camera;

	private int sizeWidth = 500;
	private int sizeHeight = 500;
	
	private Random rand = new Random();
	
	public Online(Joystick movingJoystick, Joystick shootingJoystick) {
		player = new Player(rand.nextInt(sizeWidth) + Renderer.WIDTH / 2, rand.nextInt(sizeHeight) + Renderer.HEIGHT / 2, Renderer.player_sprite, movingJoystick, shootingJoystick, sizeWidth + Renderer.WIDTH / 2, sizeHeight + Renderer.HEIGHT / 2);
		camera = new Camera(0, 0);
		
	}
	
	public void update() {
		player.update();
		if(Menu.isClient) {
			for(int i = 0; i < ClientThread.opponents.size(); i++) {
				ClientThread.opponents.get(i).update();
			}
		}else {
			for(int i = 0; i < ServerThread.clients.size(); i++) {
				ServerThread.clients.get(i).update();
			}
			ServerThread.sendAllToAll();
		}
		checkForCollisoins();
		if(Menu.isClient) {
			for(int i = 0; i < ClientThread.opponents.size(); i++) {
				if(ClientThread.opponents.get(i).life <= 0) ClientThread.opponents.get(i).dontRender = true;
			}
		}else {
			for(int i = 0; i < ServerThread.clients.size(); i++) {
				if(ServerThread.clients.get(i).life <= 0) ServerThread.clients.get(i).dontRender = true;
			}
		}
		if(Menu.isClient) {
			for(int i = 0; i < ClientThread.opponents.size(); i++) {
				if(ClientThread.opponents.get(i).remove) ClientThread.opponents.remove(i);
			}
		}
		
		if(!player.isDead) camera.update(player, 0, 0);
		else {
			int index = 0;
			if(Menu.isClient) {
				while(index < ClientThread.opponents.size()) {
					if(index == ClientThread.opponents.size()) break;
					if(ClientThread.opponents.get(index).life > 0) {
						camera.update(ClientThread.opponents.get(index), 0, 0);
						break;
					}
					index++;
				}
			}else {
				while(index < ServerThread.clients.size()) {
					if(ServerThread.clients.get(index).life > 0) {
						camera.update(ServerThread.clients.get(index), 0, 0);
						break;
					}
					index++;
				}
			}
		}
		Renderer.screen.setOffSet(camera.getOffX(), camera.getOffY());
	}
	
	private void checkForCollisoins() {
		if(Menu.isClient) {
			for(int i = 0; i < ClientThread.opponents.size(); i++) {
				Client c = ClientThread.opponents.get(i);
				for(int j = 0; j < player.bullets.size(); j++) {
					Bullet b = player.bullets.get(j);
					if(b.x > c.x && b.x < c.x + c.width && b.y > c.y && b.y < c.y + c.height) {
						c.life -= 20;
					}					
				}
				for(int a = 0; a < ClientThread.opponents.size(); a++) {
						Client client = ClientThread.opponents.get(a); 
						if(i == a) continue;
					for(int l = 0; l < client.bullets.size(); l++) {
						Bullet b = client.bullets.get(l);
						if(b.x > c.x && b.x < c.x + c.width && b.y > c.y && b.y < c.y + c.height) {
							c.life -= 20;
						}
					}
				}
				
				for(int j = 0; j < c.bullets.size(); j++) {
					Bullet b = c.bullets.get(j);
					if(b.x > player.x && b.x < player.x + player.width && b.y > player.y && b.y < player.y + player.height) {
						player.life -= 200;
					}					
				}
			}
		}else {
			for(int i = 0; i < ServerThread.clients.size(); i++) {
				Client c = ServerThread.clients.get(i);
				for(int j = 0; j < player.bullets.size(); j++) {
					Bullet b = player.bullets.get(j);
					if(b.x > c.x && b.x < c.x + c.width && b.y > c.y && b.y < c.y + c.height) {
						c.life -= 20;
					}					
				}
				for(int a = 0; a < ServerThread.clients.size(); a++) {
					Client client = ServerThread.clients.get(a); 
					if(i == a) continue;
					for(int l = 0; l < client.bullets.size(); l++) {
						Bullet b = client.bullets.get(l);
						if(b.x > c.x && b.x < c.x + c.width && b.y > c.y && b.y < c.y + c.height) {
							c.life -= 20;
						}
					}
				}
				
				for(int j = 0; j < c.bullets.size(); j++) {
					Bullet b = c.bullets.get(j);
					if(b.x > player.x && b.x < player.x + player.width && b.y > player.y && b.y < player.y + player.height) {
						player.life -= 200;
					}					
				}
			}
		}
	}
	
	public void render() {
		if(!player.isDead) player.render();
		if(Menu.isClient) {
			for(int i = 0; i < ClientThread.opponents.size(); i++) {
				if(!ClientThread.opponents.get(i).dontRender) {
					ClientThread.opponents.get(i).render();					
				}
			}
		}else {
			for(int i = 0; i < ServerThread.clients.size(); i++) {
				if(!ServerThread.clients.get(i).dontRender)	ServerThread.clients.get(i).render();
			}
		}
	}

}
