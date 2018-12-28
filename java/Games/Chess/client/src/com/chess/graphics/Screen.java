package com.chess.graphics;

import java.util.Random;

import com.chess.level.tile.Tile;

public class Screen {

	public int width;
	public int height;
	public int[] pixels;
	public final int MAP_SIZE = 64;
	public final int MAP_SIZE_MASK = MAP_SIZE - 1;
	public int[] tiles = new int[MAP_SIZE * MAP_SIZE];
	public int xOffset, yOffset;
	private Random random = new Random();
	
	private final int ALPHA_COL = 0xffff00ff;
	
	public Screen(int width, int height){
		this.width = width;
		this.height = height;
		pixels = new int[width * height]; // 50400
		
		for(int i = 0; i < MAP_SIZE * MAP_SIZE; i++){
			tiles[i] = random.nextInt(0xffffff);
		}
	}
	
	public void clear(){
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = 0;
		}
	}
	
	public void renderTextCharcter(int xp, int yp, Sprite sprite, int color, boolean fixed){
		if(fixed){
			xp -= xOffset;
			yp -= yOffset;
		}
		for(int y = 0; y < sprite.getHeight(); y++){
			int ya = y + yp;
			for(int x = 0; x < sprite.getWidth(); x++){
				int xa = x + xp;
				if(xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				int col = sprite.pixels[x + y * sprite.getWidth()];
				if(col != ALPHA_COL && col != 0xff7f007f) pixels[xa + ya * width] = color;
			}
		}
	}
	
	public void renderSprite(int xp, int yp, Sprite sprite, boolean fixed){
		if(fixed){
			xp -= xOffset;
			yp -= yOffset;
		}
		for(int y = 0; y < sprite.getHeight(); y++){
			int ya = y + yp;
			for(int x = 0; x < sprite.getWidth(); x++){
				int xa = x + xp;
				if(xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
				int col = sprite.pixels[x + y * sprite.getWidth()];
				if(col != ALPHA_COL && col != 0xff7f007f) pixels[xa + ya * width] = col;
			}
		}
	}
	
	public void renderTile(int xp, int yp, Tile tile){
		xp -= xOffset;
		yp -= yOffset;
		for(int y = 0; y < tile.sprite.SIZE; y++){
			int ya = y + yp;
			for(int x = 0; x < tile.sprite.SIZE; x++){
				int xa = x + xp;
				if(xa < -tile.sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
				if(xa < 0) xa = 0;
				pixels[xa + ya * width] = tile.sprite.pixels[x + y * tile.sprite.SIZE];
			}
		}
	}

	public void drawRect(int xp, int yp, int width, int height,int color, boolean fixed) {
		if(fixed){
			xp -= xOffset;
			yp -= yOffset;
		}
		for(int x = xp; x < xp + width; x++){
			if(x < 0 || x >= this.width || yp >= this.height) continue;
			if(yp > 0) pixels[x + yp * this.width] = color;
			if(yp + height >= this.height) continue;
			if(yp + height > 0) pixels[x + (yp + height) * this.width] = color;
		}
		for(int y = yp; y <= yp + height; y++){
			if(xp >= this.width || y < 0 || y >= this.height) continue;
			if(xp > 0) pixels[xp + y * this.width] = color;
			if(xp + width >= this.width) continue;
			if(xp + width > 0) pixels[(xp + width) + y * this.width] = color;
		}
		
	}
	
	public void fillRect(int xp, int yp, int width, int height,int color, boolean fixed){
		if(fixed){
			xp -= xOffset;
			yp -= yOffset;
		}

		for(int y = 0; y < height; y++){
			int yo = yp + y;
			if(yo < 0 || yo >= this.height) continue;
			for(int x = 0; x < width; x++){
				int xo = xp + x;
				if(xo < 0 || xo >= this.width) continue;
				pixels[xo + yo * this.width] = color;
			}
		}
		
	}
	
	public void setOffset(int xOffset, int yOffset){
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	
}








