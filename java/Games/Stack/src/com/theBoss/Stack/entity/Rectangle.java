package com.theBoss.Stack.entity;

import java.util.Random;

import com.theBoss.Stack.graphics.Screen;

public class Rectangle {
	
	public double x, y, xDir;
	public int width, height, xOffset;
	
	boolean onScreen = false;
	
	private Screen screen;
	private Random random = new Random();
	
	public Rectangle(double x, double y, int width, int height, double xDir, Screen screen) {
		this.x = x;
		this.y = y;
		this.xDir = xDir;
		this.width = width;
		this.height = height;
		this.screen = screen;
		xOffset = random.nextInt(width);
	}
	
	public void update() {
		x += xDir;
		if(onScreen && (x <= 0 || x + width >= screen.width)) xDir *= -1;
		if(!onScreen && (x >= 0 && x + width < screen.width)) onScreen = true;
	}
	
	public void render() {
		screen.renderRectangle((int) x, (int) y, width, height, xOffset);
	}

}
