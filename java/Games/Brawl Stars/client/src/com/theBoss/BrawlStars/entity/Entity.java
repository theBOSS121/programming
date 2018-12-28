package com.theBoss.BrawlStars.entity;

public class Entity {
	public double x, y, speed;
	public int width, height;
	public int[] pixels;
	public int[] rotatedPixels;
	
	public Entity(double x, double y, double speed, int width, int height) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	public void update() {}

	public void render() {}
}
