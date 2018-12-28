package com.theBoss.Jaxson.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile {

	public int width, height;
	public int[] pixels;
	
	public static Tile ul = new Tile("/tileset.png", 0, 0, 16, 16);
	public static Tile um = new Tile("/tileset.png", 16, 0, 16, 16);
	public static Tile ur = new Tile("/tileset.png", 32, 0, 16, 16);
	public static Tile ml = new Tile("/tileset.png", 0, 16, 16, 16);
	public static Tile mm = new Tile("/tileset.png", 16, 16, 16, 16);
	public static Tile mr = new Tile("/tileset.png", 32, 16, 16, 16);
	public static Tile dl = new Tile("/tileset.png", 0, 32, 16, 16);
	public static Tile dm = new Tile("/tileset.png", 16, 32, 16, 16);
	public static Tile dr = new Tile("/tileset.png", 32, 32, 16, 16);
	
	public static Tile lu = new Tile("/tileset.png", 48, 0, 16, 16);
	public static Tile ru = new Tile("/tileset.png", 64, 0, 16, 16);
	public static Tile ld = new Tile("/tileset.png", 48, 16, 16, 16);
	public static Tile rd = new Tile("/tileset.png", 64, 16, 16, 16);
	
	public Tile(String path, int x, int y, int width, int height) {
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
