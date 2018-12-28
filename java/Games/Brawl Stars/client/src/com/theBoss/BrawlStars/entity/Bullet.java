package com.theBoss.BrawlStars.entity;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.theBoss.BrawlStars.entity.Mob.Player;
import com.theBoss.BrawlStars.level.Level;

public class Bullet extends Entity{
	
	private double xd, yd;
	public boolean onScreen = true;
	private Level level;
	
	public Bullet(double x, double y, double xd, double yd, double speed, int width, int height, Level level) {
		super(x, y, speed, width, height);
		try {
			BufferedImage img = ImageIO.read(Player.class.getResource("/sheet.png"));
			pixels  = img.getRGB(32, 0, 6, 6, null, 0, 6);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.xd = xd;
		this.yd = yd;
		this.level = level;
	}
	
	public void update() {
		x += xd;
		y += yd;
		if(level.tiles[(int) (x / 32) + (int) (y / 32) * level.lvlWidth] == 0xff000000 || level.tiles[(int) ((x + width) / 32) + (int) (y / 32) * level.lvlWidth] == 0xff000000 ||
		   level.tiles[(int) (x / 32) + (int) ((y + height) / 32) * level.lvlWidth] == 0xff000000 || level.tiles[(int) ((x + width) / 32) + (int) ((y + height) / 32) * level.lvlWidth] == 0xff000000) {
			onScreen = false;
		}
	}

}
