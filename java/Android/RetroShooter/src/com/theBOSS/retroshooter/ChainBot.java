package com.theBOSS.retroshooter;

public class ChainBot extends Entity{

	private double angle = 0.0, speed = 0.3;
	private Player player;
	public int life = 100;
	
	public ChainBot(double x, double y, Sprite sprite, Player player) {
		super(x, y, sprite);
		this.player = player;
	}
	
	public void update() {
		sprite = Sprite.rotate(Renderer.saw, angle);
		angle += 0.2;
		
		if(player.x < x + 2) x -= speed;
		if(player.x > x + 2) x += speed;
		if(player.y < y + 2) y -= speed;
		if(player.y > y + 2) y += speed;
	}

	public void render() {
		Renderer.screen.renderSprite(sprite, (int) x, (int) y);
	}
	
}
