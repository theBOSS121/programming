package com.theBOSS.shooter_pc.entity;

import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.graphics.Sprite;

public class ChainBot extends Entity{

	private double angle = 0.0, speed = 0.3;
	private Player player;
	public int life = 100;
	
	public ChainBot(double x, double y, Sprite sprite, Player player) {
		super(x, y, sprite);
		this.player = player;
	}
	
	public void update() {
		sprite = Sprite.rotate(Sprite.saw, angle);
		angle += 0.2;
		
		if(player.x < x + 2) x -= speed;
		if(player.x > x + 2) x += speed;
		if(player.y < y + 2) y -= speed;
		if(player.y > y + 2) y += speed;
	}

	public void render() {
		Shooter.screen.renderSprite(sprite, (int) x, (int) y);
	}
	
}
