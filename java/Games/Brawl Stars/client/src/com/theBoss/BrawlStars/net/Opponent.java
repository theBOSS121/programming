package com.theBoss.BrawlStars.net;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.theBoss.BrawlStars.BrawlStars;
import com.theBoss.BrawlStars.entity.Bullet;
import com.theBoss.BrawlStars.entity.Mob.Player;
import com.theBoss.BrawlStars.level.Level;

public class Opponent {
	
	public int x, y;
	public int width = 32, height = 32;
	public double angle;
	public int[] pixels, rotatedPixels;
	public Level level;
	public int life = 100;
	public int startX, startY, safe = 0;
	
	public List<Bullet> bullets = new ArrayList<Bullet>();
	
	public Opponent(Level level) {
		BufferedImage image;
		try {
			image = ImageIO.read(Opponent.class.getResource("/sheet.png"));
			pixels = image.getRGB(0, 0, 32, 32, null, 0, 32);
			rotatedPixels = image.getRGB(0, 0, width, height, null, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		this.level = level;
	}
	
	public void update() {
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			if(!bullets.get(i).onScreen) {
				bullets.remove(i);
			}
		}
		if(BrawlStars.player.safe == 0) {	
			for(int i = 0; i < bullets.size(); i++) {
				Player p = BrawlStars.player;
				Bullet b = bullets.get(i);
				if(p.x + p.width > b.x && p.x < b.x + b.width && p.y + p.height > b.y && p.y < b.y + b.height) {
					p.life -= 25;
					b.onScreen = false;
				}
			}
		}
		if(life <= 0) {
			life = 100;
			x = startX;
			y = startY;
			safe = 180;
		}
		if(safe > 0) safe--;
	}
	
	public void setPositionAndAngle(int x, int y, double angle){
		this.x = x;
		this.y = y;
		this.angle = angle;

		rotatedPixels = rotate(pixels, width, height, angle);
	}
	
	private static int[] rotate(int[] pixels, int width, int height, double angle){
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
				int col = 0;
				if(xx < 0 || xx>= width || yy < 0 || yy >= height) col = 0xffff00ff; 
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
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return x * cos - y * sin;
	}
	
	private static double rot_y(double angle, double x, double y){
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return x * sin + y * cos;
	}

}
