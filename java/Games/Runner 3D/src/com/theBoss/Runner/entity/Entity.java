package com.theBoss.Runner.entity;

import com.theBoss.Runner.Runner;
import com.theBoss.Runner.Graphics.Screen;

public class Entity {
	
	public double x, y, z, width, height, lenght;
	
	public Entity(double x, double y, double z, double width, double height, double lenght) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.lenght = lenght;
	}
	
	public void update() {
		z -= 0.74 * Runner.speed;
		//z -= 0.1;
		if(z + lenght <= 0) {
			z = 592.0 - lenght;
		}
	}
	
	public void render(Screen screen) {
		screen.renderEntity(x, y, z, width, height, lenght);
	}
	
}
