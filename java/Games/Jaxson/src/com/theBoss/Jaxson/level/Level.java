package com.theBoss.Jaxson.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.theBoss.Jaxson.entity.Cloud;
import com.theBoss.Jaxson.entity.Entity;
import com.theBoss.Jaxson.entity.Player;
import com.theBoss.Jaxson.graphics.Screen;

public class Level {

	public int width, height;
	public int[] tiles;
	//private Keyboard key;
	
	public static Level startLevel = new Level("/startLevel.png");
	
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Player> players = new ArrayList<Player>();
	public List<Cloud> clouds = new ArrayList<Cloud>();
	
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
		if(e instanceof Player) {
			players.add((Player) e);
		}else if(e instanceof Cloud) {
			clouds.add((Cloud) e);
		}else {
			entities.add(e);
		}
	}
	
	public void update() {
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
		for(int i = 0; i < players.size(); i++){
			players.get(i).update();
		}
		for(int i = 0; i < clouds.size(); i++){
			clouds.get(i).update();
		}
	}
	
	public void render(Screen screen) {
		for(int i = 0; i < clouds.size(); i++){
			clouds.get(i).render(screen);
		}
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				screen.renderTile(x * 16, y * 16, getTile(x, y));
			}
		}
		for(int i = 0; i < entities.size(); i++){
			entities.get(i).render(screen);
		}
		for(int i = 0; i < players.size(); i++){
			players.get(i).render(screen);
		}
	}

	public Tile getTile(int x, int y) {
		if(x + y * width < tiles.length && x + y * width >= 0) {
			if(tiles[x + y * width] == 0xffffff00) return Tile.ul;
			if(tiles[x + y * width] == 0xffff0000) return Tile.um;
			if(tiles[x + y * width] == 0xff00ffff) return Tile.ur;
			if(tiles[x + y * width] == 0xfff0f0f0) return Tile.ml;
			if(tiles[x + y * width] == 0xffffffff) return Tile.mm;
			if(tiles[x + y * width] == 0xff0f0f0f) return Tile.mr;
			if(tiles[x + y * width] == 0xff666666) return Tile.dl;
			if(tiles[x + y * width] == 0xff999999) return Tile.dm;
			if(tiles[x + y * width] == 0xff333333) return Tile.dr;
			if(tiles[x + y * width] == 0xff0000ff) return Tile.lu;
			if(tiles[x + y * width] == 0xff00ff00) return Tile.ru;
			if(tiles[x + y * width] == 0xffabcdef) return Tile.ld;
			if(tiles[x + y * width] == 0xfffedcba) return Tile.rd;
			
		}
			
		return null;
	}
	
}
