package com.theBoss.Mario.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile {
	
	public int width, height;
	public int[] pixels;
	public boolean solid = false, animPosible = false;
	
	public static Tile floor = new Tile("/sheet.png", 0, 204, 16, 16, true, false);
	public static Tile block = new Tile("/sheet.png", 16, 204, 16, 16, true, true);
	public static Tile plu = new Tile("/sheet.png", 0, 332, 16, 16, true, false);
	public static Tile pru = new Tile("/sheet.png", 16, 332, 16, 16, true, false);
	public static Tile pld = new Tile("/sheet.png", 0, 348, 16, 16, true, false);
	public static Tile prd = new Tile("/sheet.png", 16, 348, 16, 16, true, false);
	public static Tile stair = new Tile("/sheet.png", 0, 220, 16, 16, true, false);
	public static Tile question = new Tile("/sheet.png", 384, 204, 16, 16, true, true);
	public static Tile hidden = new Tile(false, true);
	public static Tile emptyQuestion = new Tile("/sheet.png", 48, 204, 16, 16, true, false);
	public static Tile voidTile = new Tile();
	
	public Tile(String path, int x, int y, int width, int height, boolean solid, boolean animPosible) {
		try {
			BufferedImage image = ImageIO.read(Tile.class.getResource(path));
		this.width = width;
		this.height = height;
		pixels = image.getRGB(x, y, width, height, null, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.solid = solid;
		this.animPosible = animPosible;
	}
	
	public Tile() {
		solid = false;
		width = 16;
		height = 16;
		pixels = new int[16 * 16];
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xffff00ff;
		}
	}

	public Tile(boolean solid, boolean animPosible) {
		this.solid = solid;
		this.animPosible = animPosible;
		width = 16;
		height = 16;
		pixels = new int[16 * 16];
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xffff00ff;
		}
	}
}
