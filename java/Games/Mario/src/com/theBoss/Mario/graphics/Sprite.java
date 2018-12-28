package com.theBoss.Mario.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.theBoss.Mario.level.Tile;

public class Sprite {

	public int width, height;
	public int[] pixels;
	
	public static Sprite player_still = new Sprite("/sheet.png", 1, 0, 17, 17);
	public static Sprite player_moving1 = new Sprite("/sheet.png", 32, 0, 17, 17);
	public static Sprite player_moving2 = new Sprite("/sheet.png", 63, 0, 17, 17);
	public static Sprite player_moving3 = new Sprite("/sheet.png", 94, 0, 17, 17);
	public static Sprite player_jumping = new Sprite("/sheet.png", 155, 0, 17, 17);
	
	public Sprite(String path, int x, int y, int width, int height) {
		try {
			BufferedImage image = ImageIO.read(Tile.class.getResource(path));
		this.width = width;
		this.height = height;
		pixels = image.getRGB(x, y, width, height, null, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
