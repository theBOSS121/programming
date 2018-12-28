package com.theBoss.Mario.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.theBoss.Mario.entity.Entity;
import com.theBoss.Mario.graphics.Screen;

public class Level {

	int width, height;
	public int tx, ty, xo, yo;
	public boolean tileAnimation = false;
	public int[] tiles;
	long last = -1;
	
	public static Level level1 = new Level("/levels/level1.png");
	
	public List<Entity> entities = new ArrayList<Entity>();
	
	public Level(String path) {
		try {
			BufferedImage image = ImageIO.read(Level.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			tiles = image.getRGB(0, 0, width, height, null, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void add(Entity e) {
		entities.add(e);
	}
	
	public void update() {
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
		if(tileAnimation) {
			if(last == -1) last = System.currentTimeMillis();
			
			if(System.currentTimeMillis() - last < 25) {
				yo = -1;
			}else if(System.currentTimeMillis() - last < 50) {
				yo = -2;
			}else if(System.currentTimeMillis() - last < 75) {
				yo = -3;
			}else if(System.currentTimeMillis() - last < 100) {
				yo = -2;
			}else if(System.currentTimeMillis() - last < 125) {
				yo = -1;
			}else {
				yo = 0;
				if(tiles[tx + ty * width] == 0xffffff00 || tiles[tx + ty * width] == 0xff0000ff) tiles[tx + ty * width] = 0xffffff01;
				last = -1;
				tileAnimation = false;
			}
			
		}
	}
	
	public void render(Screen screen) {
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}

		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				if(x == tx && y == ty) {
					screen.renderTile(x * 16 + xo, y * 16 + yo, getTile(x, y));
				}else {
					screen.renderTile(x * 16, y * 16, getTile(x, y));
				}
			}
		}
	}

	public Tile getTile(int x, int y) {
		if(x + y * width >= tiles.length || x + y * width < 0) return Tile.voidTile;
		if(tiles[x + y * width] == 0xffcc783d) return Tile.floor;
		if(tiles[x + y * width] == 0xffff0000) return Tile.block;
		if(tiles[x + y * width] == 0xffcc6c3d) return Tile.stair;
		if(tiles[x + y * width] == 0xffffff00) return Tile.question;
		if(tiles[x + y * width] == 0xff0000ff) return Tile.hidden;
		if(tiles[x + y * width] == 0xffffff01) return Tile.emptyQuestion;
		if(tiles[x + y * width] == 0xff00ff00) {
			if(tiles[x + (y - 1) * width] == 0xff00ff00) {
				if(tiles[x + 1 + y * width] == 0xff00ff00) {
					return Tile.pld;
				}else {
					return Tile.prd;
				}
			}else {
				if(tiles[x + 1 + y * width] == 0xff00ff00) {
					return Tile.plu;
				}else {
					return Tile.pru;
				}
			}
		}
		return Tile.voidTile;
	}	
}
