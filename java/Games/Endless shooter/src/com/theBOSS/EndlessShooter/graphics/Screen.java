package com.theBOSS.EndlessShooter.graphics;

import com.theBOSS.EndlessShooter.entity.Entity;

public class Screen {

	private int width, height;
	public int[] pixels;
	public static int bgOffX = 0, bgOffY = 0;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	public void clear() {
//		i culdnt make it to move bg and other stuff in that amount of time
//		for(int y = 0; y < height; y++) {
//			int yy = y - ((bgOffY) % height);
//			if(yy >= height) yy -= height;
//			if(yy < 0) yy += height;
//			for(int x = 0; x < width; x++) {
//				int xx = x - ((bgOffX) % width);
//				if(xx >= width) xx -= width;
//				if(xx < 0) xx += width;
//				if(xx + y * width >= Sprite.bg.pixels.length || xx + y * width < 0) continue;
//				pixels[x + y * width] = Sprite.bg.pixels[xx + yy * width];
//			}
//		}
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = Sprite.bg.pixels[i];
		}
	}
	
	public void renderEntity(Entity e, int flip) {
		int xPos = (int) (e.x);
		int yPos = (int) (e.y);
		for(int y = 0; y < e.height; y++){
			int yr = yPos + y;
			for(int x = 0; x < e.width; x++){
				int xr = xPos + x;
				if(flip == 1) xr = e.width - 1 + xPos - x;
				if(xr >= width || yr >= height || xr < 0 || yr < 0 || xr + yr * width > pixels.length || xr + yr < 0) continue; 

				int alpha = ((e.rotatedSprite.pixels[x + y * e.width] >> 24) & 0xff);
				if(alpha == 255) {
					pixels[xr + yr * width] = e.rotatedSprite.pixels[x + y * e.width];
				}else {
					int pixelColor = pixels[xr + yr * width];

					int newRed = ((pixelColor >> 16) & 0xff) - (int) ((((pixelColor >> 16) & 0xff) - ((e.rotatedSprite.pixels[x + y * e.width] >> 16) & 0xff)) * (alpha / 255f));
					int newGreen = ((pixelColor >> 8) & 0xff) - (int) ((((pixelColor >> 8) & 0xff) - ((e.rotatedSprite.pixels[x + y * e.width] >> 8) & 0xff)) * (alpha / 255f));
					int newBlue = (pixelColor & 0xff) - (int) (((pixelColor & 0xff) - (e.rotatedSprite.pixels[x + y * e.width] & 0xff)) * (alpha / 255f));
			
					
					pixels[xr + yr * width] = (newRed << 16 | newGreen << 8 | newBlue);			
				}
			}
		}
	}
	
	public void renderHealthBar(int xx, int yy, double life, int scaleX, int scaleY) {
		for(int y = 0; y < 8 * scaleY; y++) {
			int yr = yy + y;
			for(int x = 0; x < 32 * scaleX; x++) {
				int xr = xx + x;
				if(yr + 8 * scaleY < 0 || yr > height || xr + 32 * scaleX < 0 || xr > width) break;
				if(xr < 0) xr = 0;
				if(xr + yr * width > 0 && xr + yr * width < pixels.length) {
					int col = Sprite.healthBar.pixels[x / scaleX + y / scaleY * Sprite.healthBar.width];
					if(col == 0xff00ff00 && x / (32.0 * scaleX) > life) col = 0xffff00ff;
					if(col == 0xff00ff00 && life <= 0.25) col = 0xffff0000;
					else if(col == 0xff00ff00 && life <= 0.5) col = 0xffff6a00;
					else if(col == 0xff00ff00 && life <= 0.75) col = 0xffffd800;
					if(col != 0xffff00ff) pixels[xr + yr * width] = col;
				}				
			}	
		}
	}
	
}
