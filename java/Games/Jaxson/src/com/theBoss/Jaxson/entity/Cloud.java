package com.theBoss.Jaxson.entity;

import java.util.Random;
import com.theBoss.Jaxson.Jaxson;
import com.theBoss.Jaxson.graphics.Screen;
import com.theBoss.Jaxson.graphics.Sprite;

public class Cloud extends Entity{

	private double xDir;
	private Random rand = new Random();
	int xOffset;
	
	public Cloud(int x, int y, double xDir, Sprite sprite) {
		super(x, y, sprite);
		this.xDir = xDir;
	}
	
	public void update() {
		x += xDir;
		if(x + xOffset < -120 || x + xOffset > Jaxson.width + 50) {
			int side = rand.nextInt(2);
			int dir = rand.nextInt(2);
			if(side == 0) {
				x = -70 - xOffset;
				y = rand.nextInt(100) + 20;
				if(dir == 0) {
					xDir = rand.nextDouble()* 0.5 + 0.01;
				}
				if(dir == 1) {
					xDir = -(rand.nextDouble()* 0.5 + 0.01);
				}
			}
			if(side == 1) {
				x = Jaxson.width - xOffset;
				y = rand.nextInt(100) + 20;
				if(dir == 0) {
					xDir = rand.nextDouble()* 0.5 + 0.01;
				}
				if(dir == 1) {
					xDir = -(rand.nextDouble()* 0.5 + 0.01);
				}
			}
		}
	}
	
	public void render(Screen screen) {
		screen.renderEntity(this, 0);
		xOffset = screen.xOffset;
	}

}
