package theboss.sacrifice.entity;

import java.util.Random;

import theboss.sacrifice.Sacrifice;
import theboss.sacrifice.graphics.Sprite;

public class Enemy extends Entity{
	
	int counter = 0;
	double dir = 0.0;
	double speed = 1.0;
	
	Random rand = new Random();
	public boolean noFrictionInTheZone = false;
	
	public Enemy(double x, double y, Sprite sprite) {
		super(x, y, sprite);
		this.mass = 4.0;
		
		dir = rand.nextDouble() * 2 * Math.PI;
		
		velX = Math.cos(dir) * speed;
		velY = Math.sin(dir) * speed;
		
		this.x = Sacrifice.WIDTH / 2 - sprite.width / 2 - (Math.cos(dir) * 570);
		this.y = Sacrifice.HEIGHT / 2 - sprite.height / 2 - (Math.sin(dir) * 570);
	}
	
	public void update() {
		
		x += velX;
		y += velY;
		
		if(hit) {
			noFrictionInTheZone = true;
			hit = false;
		}
		
		if(isInTheZone() && !noFrictionInTheZone) {
			velX *= 0.93;
			velY *= 0.93;
		}
		if(noFrictionInTheZone && !isInTheZone()) {
			noFrictionInTheZone = false;
		}
		
		if(isInTheZone()) {
			timeInTheZone++;
		}else {
			timeInTheZone = 0;
		}
		
		
		counter++;
	}
	
	public void render() {
		Sacrifice.screen.renderSprite(sprite, (int) x, (int) y);
	}	

}
