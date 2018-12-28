package com.theBoss.BrawlStars.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Level {
	
	public int[] tiles;
	public int lvlWidth, lvlHeight;	
	
	public Level(String path) {
		loadLevel(path);
	}
	
	private void loadLevel(String path) {
		try {
			BufferedImage image = ImageIO.read(Level.class.getResource(path));
			lvlWidth = image.getWidth();
			lvlHeight = image.getHeight();
			tiles = image.getRGB(0, 0, lvlWidth, lvlHeight, null, 0, lvlWidth);
		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	
}
