package com.theBoss.Jaxson.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.theBoss.Jaxson.entity.Entity;
import com.theBoss.Jaxson.level.Tile;

public class Screen {

	private int width, height;
	public int xOffset, yOffset;
	public int[] pixels, bg;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		try {
			BufferedImage image = ImageIO.read(Screen.class.getResource("/bg.png"));
			bg = image.getRGB(0, 0, width, height, null, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int xx = x - ((xOffset / 4) % width);
				if(xx >= width) xx -= width;
				if(xx < 0) xx += width;
				if(xx + y * width >= bg.length || xx + y * width < 0) continue;
				pixels[x + y * width] = bg[xx + y * width];
			}
		}
	}
	
	public void renderEntity(Entity e, int flip) {
		int xPos = (int) (e.x + xOffset);
		int yPos = (int) (e.y + yOffset);
		for(int y = 0; y < e.height; y++){
			int yr = yPos + y;
			for(int x = 0; x < e.width; x++){
				int xr = xPos + x;
				if(flip == 1) xr = e.width - 1 + xPos - x;
				int col = e.sprite.pixels[x + y * e.width];	
				if(col == 0xffff00ff) continue;	
				if(col == 0x00ffffff) continue;
				if(xr >= width || yr >= height || xr < 0 || yr < 0 || xr + yr * width > pixels.length || xr + yr < 0) continue; 
				pixels[xr + yr * width] = col;		
			}
		}
	}

	public void renderTile(int xx, int yy, Tile tile) {
		if(tile == null) return;
		int xPos = xx + xOffset;
		int yPos = yy + yOffset;
		if(xPos > width || yPos > height || xPos + tile.width < 0 || yPos + height < 0) return;
		for(int y = 0; y < tile.height; y++){
			int yr = yPos + y;
			for(int x = 0; x < tile.width; x++){
				int xr = xPos + x;
				if(xr >= width || yr >= height || xr < 0 || yr < 0) continue;
				int col = tile.pixels[x + y * tile.width];
				if(col == 0xffff00ff) continue;
				pixels[xr + yr * width] = col;				
			}
		}
	}	

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

}
