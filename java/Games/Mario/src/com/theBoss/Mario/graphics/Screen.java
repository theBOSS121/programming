package com.theBoss.Mario.graphics;

import com.theBoss.Mario.level.Tile;

public class Screen {

	private int width, height;
	public int[] pixels;
	private int xOffset, yOffset;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0x6b8cff;
		}
	}

	public void renderTile(int xPos, int yPos, Tile tile) {
		if(tile == null || tile == Tile.voidTile) return;
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0 ; y < tile.height; y++) {
			int yr = yPos + y;
			for(int x = 0; x < tile.width; x++) {
				int xr = xPos + x;
				if(xr >= width || xr < 0 || yr >= height || yr < 0) continue;
				if(xr + yr * width < pixels.length && xr + yr * width >= 0) { 
					int col = tile.pixels[x + y * tile.width];
					if(col == 0xffff00ff) continue;
					pixels[xr + yr * width] = col;
				}
			}
		}
	}	
	
	public void renderSprite(int xPos, int yPos, Sprite sprite, int flip) {
		if(sprite == null) return;
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0 ; y < sprite.height; y++) {
			int yr = yPos + y;
			for(int x = 0; x < sprite.width; x++) {
				int xr = xPos + x;
				if(flip == 1) xr = sprite.width - 1 + xPos - x;
				if(xr >= width || xr < 0 || yr >= height || yr < 0) continue;
				if(xr + yr * width < pixels.length && xr + yr * width >= 0) { 
					int col = sprite.pixels[x + y * sprite.width];
					if(col == 0xffff00ff) continue;
					pixels[xr + yr * width] = col;
				}
			}
		}
	}	
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
