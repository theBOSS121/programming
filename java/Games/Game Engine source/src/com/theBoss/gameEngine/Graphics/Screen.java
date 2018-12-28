package com.theBoss.gameEngine.Graphics;

import java.util.Random;

public class Screen {

	private int width, height;
	public int[] pixels;
	public final int MAP_WIDTH = 16, MAP_HEIGHT = 32; 
	
	public int[] tiles = new int[MAP_WIDTH * MAP_HEIGHT];
	private Random rand = new Random();
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		
		for(int i = 0 ; i < MAP_WIDTH * MAP_HEIGHT; i++) {
			tiles[i] = rand.nextInt(0xffffff);
		}
		
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void render(int xOffset, int yOffset) {
		for(int y = 0; y < height; y++) {
			int yy = y + yOffset;
			//if(yy < 0 || yy > height) break;
			for(int x = 0; x < width; x++) {
				int xx = x + xOffset;
				//if(xx < 0 || xx > width) break;
				int tileIndex = ((xx >> 5) & MAP_WIDTH - 1) + ((yy >> 5) & MAP_HEIGHT - 1) * MAP_WIDTH;
				pixels[x + y * width] = tiles[tileIndex];
			}	
		}
	}
	
}
