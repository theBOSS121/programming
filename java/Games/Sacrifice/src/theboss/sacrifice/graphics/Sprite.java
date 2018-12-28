package theboss.sacrifice.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	
	public int width, height;
	public int[] pixels;
	public boolean collidable = false;

	public static Sprite player = new Sprite("/player.png");
	public static Sprite enemy1 = new Sprite("/enemy1.png");
	public static Sprite enemy2 = new Sprite("/enemy2.png");
	public static Sprite enemy3 = new Sprite("/enemy3.png");
	public static Sprite enemy3_bg = new Sprite("/enemy3_bg.png");
	public static Sprite bg = new Sprite("/bg.png");
	public static Sprite player_shadow = new Sprite("/player_shadow.png");

	
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
	
	public Sprite(int[] pixels, int width, int height){
		this.width = width;
		this.height = height;
		this.pixels = new int[pixels.length];		
		for(int i = 0; i < pixels.length; i++){
			this.pixels[i] = pixels[i];
		}
	}
	
	public static Sprite rotate(Sprite sprite, double angle){
		angle -= Math.PI / 2;
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
