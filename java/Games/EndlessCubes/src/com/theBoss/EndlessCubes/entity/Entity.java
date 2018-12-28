package com.theBoss.EndlessCubes.entity;

import java.util.Random;

import com.theBoss.EndlessCubes.EndlessCubes;
import com.theBoss.EndlessCubes.graphics.Screen;

public class Entity {
	public double x, y, z, width, height, lenght;
	public int col;
	public int renderedCol;
	
	private Random rand = new Random();
	
	public Entity(double x, double y, double z, double width, double height, double lenght, int col) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.lenght = lenght;
		this.col = col;
		renderedCol = col;
	}
	
	public void update() {
		//z -= Screen.time * 0.01;
		z -= EndlessCubes.speed;
		if(z + lenght <= 0) {
			z += 15.4 * 10;
			int a = rand.nextInt(4);
			if(a == 0) {
				x = -7.0;
				y = 0.0;
			}else if(a == 1) {
				x = 0.0;
				y = 0.0;
			}else if(a == 2) {
				x = -7.0;
				y = 7.15;
			}else {
				x = 0.0;
				y = 7.15;
			}
			EndlessCubes.score++;
		}
		setCol(col);
	}
	
	public double getZ() {
		return z;
	}
	
	private void setCol(int col) {
		int brightness = (int) (Screen.renderDistance / (z + lenght));
		if(brightness < 0){
			brightness = 0;
		}
		
		if(brightness > 255){
			brightness = 255;
		}
		
		int r = (this.col >> 16) & 0xff;
		int g = (this.col >> 8) & 0xff;
		int b = (this.col) & 0xff;
			
		r = r * brightness / 255;
		g = g * brightness / 255;
		b = b * brightness / 255;
		
		renderedCol = r << 16 | g << 8 | b;
	}

	public void render(Screen screen) {
		screen.renderEntity(x, y, z, width, height, lenght, renderedCol);
	}

}
