package com.theBOSS.Platformer.graphics;

public class Light {
	
	public static final int NONE = 0;
	public static final int FULL = 1;
	

	private int radius, diameter, color;
	private int[] lm;
	private boolean[] lookedBefore;
	
	
	public Light(int radius, int color) {
		this.radius = radius;
		this.diameter = radius * 2;
		this.color = color;
		lm = new int[diameter * diameter];
		
		for(int y = 0; y < diameter; y++) {
			for(int x = 0; x < diameter; x++) {
				double distance = Math.sqrt((x - radius) * (x - radius) + (y - radius) * (y - radius));
				if(distance < radius) {
					double power = 1.0 - (distance / radius);
					lm[x + y * diameter] = (0xff << 24 | (int) (((color >> 16) & 0xff) * power) << 16 | (int) (((color >> 8) & 0xff) * power) << 8 | (int) ((color & 0xff) * power));;
				}else {
					lm[x + y * diameter] = 0xff000000;
				}
			}
		}
		lookedBefore = new boolean[diameter * diameter];
		clear();
	}
	
	public void clear() {
		for(int i = 0; i < lookedBefore.length; i++) {
			lookedBefore[i] = false;
		}
	}

	public int getLightValue(int x, int y) {
		if(x < 0 || x >= diameter || y < 0 || y >= diameter) return 0xff000000;
		if(lookedBefore[x + y * diameter]) return 0xff000000;
		else { 
			lookedBefore[x + y * diameter] = true;
			return lm[x + y * diameter];
		}
	}
	
	public int getRadius() {
		return radius;
	}


	public void setRadius(int radius) {
		this.radius = radius;
	}


	public int getDiameter() {
		return diameter;
	}


	public void setDiameter(int diameter) {
		this.diameter = diameter;
	}


	public int getColor() {
		return color;
	}


	public void setColor(int color) {
		this.color = color;
	}


	public int[] getLm() {
		return lm;
	}


	public void setLm(int[] lm) {
		this.lm = lm;
	}
	
}
