package com.theBoss.Stack.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Screen {

	public int width, height;
	public int[] pixels, bg, wood;
	
	private BufferedImage background, texture;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		try {
			background = ImageIO.read(Screen.class.getResource("/bg.png"));
			bg = background.getRGB(0, 0, background.getWidth(), background.getHeight(), null, 0, background.getWidth());
			texture = ImageIO.read(Screen.class.getResource("/wood.png"));
			wood = texture.getRGB(0, 0, texture.getWidth(), texture.getHeight(), null, 0, texture.getWidth());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = bg[i];
		}
	}
	
	public void render() {
		for(int i = 0; i < pixels.length; i++) {
			//pixels[i] = 0xffff00;
		}
	}

	public void renderRectangle(int x, int y, int width, int height, int xOffset) {
		for(int yy = y; yy < y + height ; yy++) {
			for(int xx = x; xx < x + width; xx++) {
				if(xx < 0 || xx >= this.width) continue;
				if(yy < 0 || yy >= this.height) continue;
				pixels[xx + yy * this.width] = wood[(xx - x + xOffset) % (texture.getWidth() - 1) + (yy - y) * texture.getWidth()];
			}	
		}
	}	
}
