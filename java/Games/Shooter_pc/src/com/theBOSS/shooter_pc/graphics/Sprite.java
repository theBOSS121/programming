package com.theBOSS.shooter_pc.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	
	public int width, height;
	public int[] pixels;
	public int lightBlock = Light.NONE;
	public boolean collidable = false;

	public static Sprite player = new Sprite("/player.png");
	public static Sprite bg = new Sprite("/bg.png");
	public static Sprite joystick_bg = new Sprite("/joystick_bg.png");
	public static Sprite joystick = new Sprite("/joystick.png");
	public static Sprite saw = new Sprite("/saw.png");
	public static Sprite play = new Sprite("/play.png");
	public static Sprite play_clicked = new Sprite("/play_clicked.png");
	public static Sprite options = new Sprite("/options.png");
	public static Sprite options_clicked = new Sprite("/options_clicked.png");
	public static Sprite help = new Sprite("/help.png");
	public static Sprite help_clicked = new Sprite("/help_clicked.png");
	public static Sprite single_player = new Sprite("/single_player.png");
	public static Sprite single_player_clicked = new Sprite("/single_player_clicked.png");
	public static Sprite survive = new Sprite("/survive.png");
	public static Sprite survive_clicked = new Sprite("/survive_clicked.png");
	public static Sprite online = new Sprite("/online.png");
	public static Sprite online_clicked = new Sprite("/online_clicked.png");
	public static Sprite back = new Sprite("/back.png");
	public static Sprite back_clicked = new Sprite("/back_clicked.png");
	public static Sprite sounds = new Sprite("/sounds.png");
	public static Sprite sounds_clicked = new Sprite("/sounds_clicked.png");
	public static Sprite no_sounds = new Sprite("/no_sounds.png");
	public static Sprite no_sounds_clicked = new Sprite("/no_sounds_clicked.png");
	public static Sprite client = new Sprite("/client.png");
	public static Sprite client_clicked = new Sprite("/client_clicked.png");
	public static Sprite server = new Sprite("/server.png");
	public static Sprite server_clicked = new Sprite("/server_clicked.png");

	
	
	public Sprite(String path) {
		try {
			BufferedImage image = ImageIO.read(Sprite.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = image.getRGB(0, 0, width, height, null, 0, width);
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}
	public Sprite(String path, boolean colidable) {
		try {
			BufferedImage image = ImageIO.read(Sprite.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = image.getRGB(0, 0, width, height, null, 0, width);
			this.collidable = colidable;
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	public Sprite(String path, int lightBlock) {
		try {
			BufferedImage image = ImageIO.read(Sprite.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = image.getRGB(0, 0, width, height, null, 0, width);
			this.lightBlock = lightBlock;
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	public Sprite(String path, int lightBlock, boolean colidable) {
		try {
			BufferedImage image = ImageIO.read(Sprite.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = image.getRGB(0, 0, width, height, null, 0, width);
			this.lightBlock = lightBlock;
			this.collidable  = colidable;
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	public Sprite(int[] pixels, int width, int height){
		this.width = width;
		this.height = height;
		this.pixels = new int[pixels.length];		
		for(int i = 0; i < pixels.length; i++){
			this.pixels[i] = pixels[i];
		}
	}
	
	public static Sprite rotate(Sprite sprite, double angle){
		return new Sprite(rotate(sprite.pixels, sprite.width, sprite.height, angle), sprite.width, sprite.height);
	}
	
	public static int[] rotate(int[] pixels, int width, int height, double angle){
		int[] result = new int[width * height]; 
		
		double nx_x = rot_x(-angle, 1.0, 0.0);
		double nx_y = rot_y(-angle, 1.0, 0.0);
		double ny_x = rot_x(-angle, 0.0, 1.0);
		double ny_y = rot_y(-angle, 0.0, 1.0);
		
		double x0 = rot_x(-angle, -width / 2.0, -height / 2.0) + width / 2.0;
		double y0 = rot_y(-angle, -width / 2.0, -height / 2.0) + height / 2.0;
		
		for(int y = 0; y < height; y++){
			double x1 = x0;
			double y1 = y0;
			for(int x = 0; x < width; x++){
				int xx = (int) x1;
				int yy = (int) y1;
				
				//low resolution fix in shooter game sprite was odd (liha)
				if(angle >= Math.PI / 2 && angle <= Math.PI) xx--;
				if(angle >= 0.0 && angle <= Math.PI / 2) yy--;
				//low resolution pixel fix
				
				int col = 0x00ffffff;
				if(xx < 0 || xx>= width || yy < 0 || yy >= height) col = 0x00ffffff; 
				else col = pixels[xx + yy * width];
				result[x + y * width] = col;
				x1 += nx_x;
				y1 += nx_y;
			}
			x0 += ny_x;
			y0 += ny_y;
		}
		
		return result;
	}
	
	private static double rot_x(double angle, double x, double y){
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * cos - y * sin;
	}
	
	private static double rot_y(double angle, double x, double y){
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * sin + y * cos;
	}
}
