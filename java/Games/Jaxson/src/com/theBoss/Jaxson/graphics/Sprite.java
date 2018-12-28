package com.theBoss.Jaxson.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {

	public int width, height;
	public int[] pixels;
	
	public static Sprite player_still = new Sprite("/jaxson.png", 0, 0, 22, 32);
	public static Sprite player_moving1 = new Sprite("/jaxson.png", 22, 0, 22, 32);
	public static Sprite player_moving2 = new Sprite("/jaxson.png", 44, 0, 22, 32);
	public static Sprite player_moving3 = new Sprite("/jaxson.png", 66, 0, 22, 32);
	public static Sprite player_moving4 = new Sprite("/jaxson.png", 88, 0, 22, 32);
	public static Sprite player_moving5 = new Sprite("/jaxson.png", 110, 0, 22, 32);
	public static Sprite player_crouch = new Sprite("/jaxson.png", 0, 32, 22, 32);
	public static Sprite player_jump1 = new Sprite("/jaxson.png", 22, 32, 22, 32);
	public static Sprite player_jump2 = new Sprite("/jaxson.png", 44, 32, 22, 32);
	public static Sprite player_jump3 = new Sprite("/jaxson.png", 66, 32, 22, 32);
	public static Sprite player_jump4 = new Sprite("/jaxson.png", 88, 32, 22, 32);
	public static Sprite player_jump5 = new Sprite("/jaxson.png", 110, 32, 22, 32);
	public static Sprite cloud = new Sprite("/cloud.png", 0, 0, 70, 27);
	public static Sprite bee = new Sprite("/bee.png", 0, 0, 16, 16);
	
	
	public Sprite(String path, int x, int y, int width, int height){
		try {
			BufferedImage image = ImageIO.read(Sprite.class.getResource(path));
			this.width = width;
			this.height = height;
			pixels = image.getRGB(x, y, width, height, null, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
