package com.chess.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.chess.entity.Entity;
import com.chess.graphics.Screen;
import com.chess.level.tile.Tile;

public class Level {
	
	public int width, height;
	protected int[] tilesInt;
	public int[] tiles;
	
	public List<Entity> entities = new ArrayList<Entity>();
	
	public static Level board = new Level("/levels/board.png");
	
	public Level(int width, int height){
		this.width = width;
		this.height = height;
		tilesInt = new int[width * height];
	}

	public Level(String path){
		loadLevel(path);		
	}
	
	protected void loadLevel(String path){
		try{
			BufferedImage image = ImageIO.read(Level.class.getResource(path));
			int w = width = image.getWidth();
			int h = height = image.getHeight();
			tiles = new int [w * h];
			image.getRGB(0, 0, w, h, tiles, 0, w);
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Exception! Could not load level file!");
		}
	}
	
	public void update(){
		for(int i = 0; i < entities.size(); i++){
			entities.get(i).update();
		}
		remove();
		
	}
	
	public void remove(){
		for(int i = 0; i < entities.size(); i++){
			if(entities.get(i).isRemoved()) entities.remove(i);
		}
	}
	
	public boolean tileCollision(int x, int y, int size, int xOffset, int yOffset){
		boolean solid = false;
		for(int c = 0; c < 4; c++){
			//int xt = (x - c % 2 * size + xOffset) >> 4;
			//int yt = (y - c / 2 * size + yOffset) >> 4;
		}
		return solid;
	}
	
	public void render(Screen screen){
		screen.setOffset(0, 0);
		int x0 = 0;
		int x1 = 18;
		int y0 = 0;
		int y1 = 18;
		for(int y = y0; y < y1; y++){
			for(int x = x0; x < x1; x++){
				getTile(x, y).render(x, y, screen);
				
			}
		}
		for(int i = 0; i < entities.size(); i++){
			entities.get(i).render(screen);
		}
	}
	
	public void add(Entity e){
		e.init(this);
		entities.add(e);
		
	}
	
	public List<Entity> getEntities(Entity e, int radius){
		List<Entity> result = new ArrayList<Entity>();
		int ex = (int) e.getX();
		int ey = (int) e.getY();
		for(int i = 0; i < entities.size(); i++){
			Entity entity = entities.get(i);
			if(entity.equals(e)) continue;
			int x = (int) entity.getX();
			int y = (int) entity.getY();
			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if(distance <= radius) result.add(entity);
		}
		return result;
	}
	
	// Grass = 0xFF00FF00
	// Flower = 0xFFFFFF00
	// Rock = 0xFF7F7F00
	public Tile getTile(int x, int y){
		if(x < 0 || y < 0 || x >= width || y >= height) return Tile.col1;
		if(tiles[x+y*width] == Tile.c2) return Tile.col2;
		if(tiles[x+y*width] == Tile.c1) return Tile.col1;
		if(tiles[x+y*width] == Tile.sq1) return Tile.s1;
		if(tiles[x+y*width] == Tile.sq2) return Tile.s2;
		if(tiles[x+y*width] == Tile.sq3) return Tile.s3;
		if(tiles[x+y*width] == Tile.sq4) return Tile.s4;
		if(tiles[x+y*width] == Tile.sq5) return Tile.s5;
		if(tiles[x+y*width] == Tile.sq6) return Tile.s6;
		if(tiles[x+y*width] == Tile.sq7) return Tile.s7;
		if(tiles[x+y*width] == Tile.sq8) return Tile.s8;
		if(tiles[x+y*width] == Tile.sqa) return Tile.sa;
		if(tiles[x+y*width] == Tile.sqb) return Tile.sb;
		if(tiles[x+y*width] == Tile.sqc) return Tile.sc;
		if(tiles[x+y*width] == Tile.sqd) return Tile.sd;
		if(tiles[x+y*width] == Tile.sqe) return Tile.se;
		if(tiles[x+y*width] == Tile.sqf) return Tile.sf;
		if(tiles[x+y*width] == Tile.sqg) return Tile.sg;
		if(tiles[x+y*width] == Tile.sqh) return Tile.sh;
		if(tiles[x+y*width] == Tile.squeer) return Tile.sq;
		return Tile.col1;
	}
}







