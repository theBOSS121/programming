package com.theBOSS.retroshooter;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Client extends Entity{


	public InetAddress address;
	public int port;
	public int id;
	public static int index = 0;
	public double angle = 0.0;
	public int life = 100;
	
	public List<Bullet> bullets = new ArrayList<Bullet>();
	public boolean dontRender = false;
	
	public Client(InetAddress address, int port) {
		super(-1, -1, Renderer.player_sprite);
		this.address = address;
		this.port = port;
		id = index;
		index++;
	}
	
	public void update() {
		sprite = Sprite.rotate(Renderer.player_sprite, angle);
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
		}
		
	}
	
	public void render() {
		Renderer.screen.renderSprite(sprite, (int) x, (int) y);
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render();
		}
	}
	
}
