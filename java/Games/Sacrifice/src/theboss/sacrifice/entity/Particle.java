package theboss.sacrifice.entity;

import java.util.Random;

import theboss.sacrifice.Sacrifice;
import theboss.sacrifice.graphics.Sprite;

public class Particle extends Entity {
	
	public Random rand = new Random();
	
	
	public Particle(double x, double y, Sprite sprite, double dir) {
		super(x, y, sprite);
		double speed = rand.nextDouble() * 10 + 35;
		velX = Math.cos(dir) * speed;
		velY = Math.sin(dir) * speed;
	}
	
	public void update() {
		x += velX;
		y += velY;
		velX *= 0.97;
		velY *= 0.97;
	}
	
	public void render() {
		Sacrifice.screen.renderSprite(sprite, (int) x, (int) y);
	}

}
