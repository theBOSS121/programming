package com.theBoss.BrawlStars.entity.Mob;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.theBoss.BrawlStars.BrawlStars;
import com.theBoss.BrawlStars.entity.Bullet;
import com.theBoss.BrawlStars.entity.Entity;
import com.theBoss.BrawlStars.graphics.Screen;
import com.theBoss.BrawlStars.input.Keyboard;
import com.theBoss.BrawlStars.input.Mouse;
import com.theBoss.BrawlStars.level.Level;
import com.theBoss.BrawlStars.net.Opponent;

public class Player extends Entity{
	
	Keyboard key;
	Mouse mouse;
	int reloadTime = 400;
	long lastTime = 0;
	public double angle;
	Level level;
	public int life = 100;
	public int startX, startY, safe = 0;
	
	private List<Bullet> bullets = new ArrayList<Bullet>();
	
	public Player(double x, double y, double speed, int width, int height, Keyboard key, Mouse mouse, Level level) {
		super(x, y, speed, width, height);
		this.key = key;
		try {
			BufferedImage img = ImageIO.read(Player.class.getResource("/sheet.png"));
			pixels  = img.getRGB(0, 0, 32, 32, null, 0, 32);
			rotatedPixels  = img.getRGB(0, 0, 32, 32, null, 0, 32);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.level = level;
		angle = Math.PI;
	}
	
	public void update() {
		double xx = 0, yy = 0;
		if(key.up) yy -= speed;
		if(key.down) yy += speed;
		if(key.left) xx -= speed;
		if(key.right) xx += speed;
		
		if(speed == 1.0) {
			move(xx, yy);
		}if(speed > 1.0) {
			move(xx / 2, yy / 2);
			move(xx / 2, yy / 2);
		}
		
		if(Mouse.pressed) {
			if(lastTime + reloadTime < System.currentTimeMillis()) {
				double dir = getAngle() - Math.PI / 2;
				double speed = 5.0;
				double xd = Math.cos(dir) * speed;
				double yd = Math.sin(dir) * speed;
				double xpos = x + width / 2 - 3;
				double ypos = y + height / 2 - 3;
				String s = "/b/" + BrawlStars.client.opponentsID + "/id/" + xpos + "/x/" + ypos + "/y/" + xd + "/xd/" + yd + "/e/";
				BrawlStars.client.send(s.getBytes());
				bullets.add(new Bullet(xpos, ypos, xd, yd, speed, 6, 6, level));
				lastTime = System.currentTimeMillis();
			}
		}
		
		if(Mouse.getX() != -1)
		angle = getAngle();
		
		rotatedPixels = rotate(pixels, width, height, angle);
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
		}
		for(int i = 0; i < bullets.size(); i++) {
			if(!bullets.get(i).onScreen) {
				bullets.remove(i);
			}
		}
		if(BrawlStars.client.opponent != null) {
			if(BrawlStars.client.opponent.safe == 0) {
				for(int i = 0; i < bullets.size(); i++) {
					Opponent o = BrawlStars.client.opponent;
					Bullet b = bullets.get(i);
					if(o.x + o.width > b.x && o.x < b.x + b.width && o.y + o.height > b.y && o.y < b.y + b.height) {
						o.life -= 25;
						b.onScreen = false;
					}
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

	public void render(Screen screen) {
		for(int i = 0; i < bullets.size(); i++) {
			screen.renderBullet((int) bullets.get(i).x,(int) bullets.get(i).y, bullets.get(i));
		}
		if(safe != 0 && System.currentTimeMillis() % 700 < 350) {
			screen.renderPlayer(this);
			screen.renderHealthBar((int) x, (int) y - 12, life / 100.0);
		}
		if(safe == 0) {
			screen.renderPlayer(this);			
			screen.renderHealthBar((int) x, (int) y - 12, life / 100.0);
		}
	}	
	
	private void move(double xx, double yy) {
		if(xx != 0 && yy != 0) {
			move(xx, 0);
			move(0, yy);
			return;
		}
				
		if(xx > 0) {
			if(level.tiles[(int) ((int) ((x + xx + width - 1) / 32) + (int) y / 32 * level.lvlWidth)] == 0xffff0000 &&
			   level.tiles[(int) ((int) ((x + xx + width - 1) / 32) + (int) (y + height - 1) / 32 * level.lvlWidth)] == 0xffff0000 ) {
				x += xx;
				
			}
		}else {
			if(level.tiles[(int) ((int) ((x + xx) / 32) + (int) y / 32 * level.lvlWidth)] == 0xffff0000 &&
			   level.tiles[(int) ((int) ((x + xx) / 32) + (int) (y + height - 1) / 32 * level.lvlWidth)] == 0xffff0000 ) {
				x += xx;			
			}
		}
		
		if(yy > 0) {
			if(level.tiles[(int) ((int) (x / 32) + (int) (y + yy + height - 1) / 32 * level.lvlWidth)] == 0xffff0000 &&
			   level.tiles[(int) ((int) ((x + width - 1) / 32) + (int) (y + yy + height - 1) / 32 * level.lvlWidth)] == 0xffff0000 ) {
				y += yy;
				
			}
		}else {
			if(level.tiles[(int) ((int) (x / 32) + (int) (y + yy) / 32 * level.lvlWidth)] == 0xffff0000 &&
			   level.tiles[(int) ((int) ((x + width - 1) / 32) + (int) (y + yy) / 32 * level.lvlWidth)] == 0xffff0000 ) {
				y += yy;			
			}
		}
		
		
	}
	
	

	private double getAngle() {
		double dir;		
		int xx = BrawlStars.width * BrawlStars.scale / 2 - Mouse.getX();
		int yy = BrawlStars.height * BrawlStars.scale / 2 - Mouse.getY();		
		dir = -Math.atan2(xx, yy);
		return dir;
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
