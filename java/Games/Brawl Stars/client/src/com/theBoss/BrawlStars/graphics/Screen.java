package com.theBoss.BrawlStars.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.theBoss.BrawlStars.entity.Entity;
import com.theBoss.BrawlStars.level.Level;
import com.theBoss.BrawlStars.net.Opponent;

public class Screen {

	private int width, height, xOffset, yOffset;
	public int[] pixels, floor, wall, water, healthBar;
	private Level level;
	
	public final int MAP_WIDTH, MAP_HEIGHT; 
	
	private static BufferedImage sheet;
	Random rand = new Random();
	
	public Screen(int width, int height, Level level) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		this.level = level;
		try {
			sheet = ImageIO.read(Screen.class.getResource("/sheet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		MAP_WIDTH = level.lvlWidth;
		MAP_HEIGHT = level.lvlHeight;

		floor = sheet.getRGB(64, 0, 32, 32, null, 0, 32);
		wall = sheet.getRGB(96, 0, 32, 32, null, 0, 32);
		water = sheet.getRGB(128, 0, 32, 32, null, 0, 32);
		healthBar = sheet.getRGB(32, 16, 32, 16, null, 0, 32);
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void render() {
		for(int y = 0; y < height; y ++) {
			int yy = y + yOffset;
			if(yy < 0 || yy > MAP_HEIGHT * 32) continue;
			for(int x = 0; x < width; x ++) {
				int xx = x + xOffset;
				if(xx < 0 || xx > MAP_WIDTH * 32) continue;
				int tileIndex = (xx >> 5) + (yy >> 5) * MAP_WIDTH;
				if(tileIndex >= 0 && tileIndex < level.tiles.length) {
					if(level.tiles[tileIndex] == 0xffff0000) {
						pixels[x + y * width] = floor[xx % 32 + yy % 32 * 32];
					}else if(level.tiles[tileIndex] == 0xff0000ff) {
						pixels[x + y * width] = water[xx % 32 + yy % 32 * 32];
					}else {
						pixels[x + y * width] = wall[xx % 32 + yy % 32 * 32];
					}
				}
			}	
		}
	}

	public void renderPlayer(Entity e) {
		//player is in the middle of the screen
		for(int y = 0; y < e.height; y++) {
			for(int x = 0; x < e.width; x++) {
				int col = e.rotatedPixels[x + y * e.width];
				if(col == 0xffff00ff) continue;				
				pixels[(x + width / 2 - e.width / 2) + (y + height / 2 - e.height / 2) * width] = col;
			}	
		}
	}	
	
	public void renderBullet(int xx, int yy, Entity e) {
		int xo = xx - xOffset;
		int yo = yy - yOffset;		
		for(int y = 0; y < e.height; y++) {
			int yr = yo + y;
			for(int x = 0; x < e.width; x++) {
				int xr = xo + x;
				if(yr < 0 || yr > height || xr + e.width < 0 || xr > width) break;
				if(xr < 0) xr = 0;
				if(xr + yr * width > 0 && xr + yr * width < pixels.length) {
					int col = e.pixels[x + y * e.width];
					if(col != 0xffff00ff) pixels[xr + yr * width] = col;
				}				
			}	
		}
	}
	
	public void renderHealthBar(int xx, int yy, double life) {
		int xo = xx - xOffset;
		int yo = yy - yOffset;		
		for(int y = 0; y < 16; y++) {
			int yr = yo + y;
			for(int x = 0; x < 32; x++) {
				int xr = xo + x;
				if(yr < 0 || yr > height || xr + 32 < 0 || xr > width) break;
				if(xr < 0) xr = 0;
				if(xr + yr * width > 0 && xr + yr * width < pixels.length) {
					int col = healthBar[x + y * 32];
					if(col == 0xff00ff00 && x / 32.0 > life) col = 0xffff00ff;
					if(col == 0xff00ff00 && life <= 0.25) col = 0xffff0000;
					else if(col == 0xff00ff00 && life <= 0.5) col = 0xffff6a00;
					else if(col == 0xff00ff00 && life <= 0.75) col = 0xffffd800;
					if(col != 0xffff00ff) pixels[xr + yr * width] = col;
				}				
			}	
		}
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void renderOpponnent(Opponent o) {
		int xo = o.x - xOffset;
		int yo = o.y - yOffset;
		for(int y = 0; y < o.height; y++) {
			int yr = yo + y;
			for(int x = 0; x < o.width; x++) {
				int xr = xo + x;
				if(yr < 0 || yr > height || xr + o.width < 0 || xr + 1 > width) break;
				if(xr < 0) xr = 0;
				if(xr + yr * width > 0 && xr + yr * width < pixels.length) {
					int col = o.rotatedPixels[x + y * o.width];
					if(col != 0xffff00ff) pixels[xr + yr * width] = col;
				}				
			}	
		}
		
	}	
}












