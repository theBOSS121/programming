package com.bombit.level;

import java.util.ArrayList;
import java.util.List;

import com.bombit.entity.Bomb;
import com.bombit.entity.Entity;
import com.bombit.entity.mob.Bot;
import com.bombit.entity.mob.Mob;
import com.bombit.entity.mob.Player;
import com.bombit.graphics.Screen;
import com.bombit.level.tile.Tile;
import com.bombit.net.ServerClient;

public class Level {
	
	public int width, height;
	protected int[] tilesInt;
	public int[] tiles;
	
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Bomb>  bombs = new ArrayList<Bomb>();
	public List<Mob> players = new ArrayList<Mob>();
	
	public static Level level4 = new FirstLevel("/levels/level4.png");
	public static Level level3 = new FirstLevel("/levels/level3.png");
	public static Level level2 = new FirstLevel("/levels/level2.png");
	public static Level level1 = new FirstLevel("/levels/level1.png");
	
	public Level(int width, int height){
		this.width = width;
		this.height = height;
		tilesInt = new int[width * height];
		generateLevel();
	}

	public Level(String path){
		loadLevel(path);
		generateLevel();		
	}

	protected void generateLevel(){
	}
	
	protected void loadLevel(String path){		
	}
	
	public void update(){
		for(int i = 0; i < entities.size(); i++){
			entities.get(i).update();
		}
		for(int i = 0; i < players.size(); i++){
			players.get(i).update();
		}

		for(int i = 0; i < bombs.size(); i++){
			bombs.get(i).update();
		}
		remove();
	}
	
	private void remove(){
		for(int i = 0; i < entities.size(); i++){
			if(entities.get(i).isRemoved()) entities.remove(i);
		}
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).isRemoved()) players.remove(i);
		}
		for(int i = 0; i < bombs.size(); i++){
			if(bombs.get(i).isRemoved()) bombs.remove(i);
		}
	}
	
	public boolean tileCollision(int x, int y, int size, int xOffset, int yOffset){
		boolean solid = false;
		for(int c = 0; c < 4; c++){
			int xt = (x - c % 2 * size + xOffset) >> 4;
			int yt = (y - c / 2 * size + yOffset) >> 4;
			if(getTile(xt, yt).solid()) solid = true;
		}
		return solid;
	}
	
	public void render(Screen screen){
		screen.setOffset(width/ 2 - 6, height/2 - 8);
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
		for(int i = 0; i < bombs.size(); i++){
			bombs.get(i).render(screen);
		}
		for(int i = 0; i < players.size(); i++){
			players.get(i).render(screen);
		}
	}
	
	public void add(Entity e){
		e.init(this);
		if(e instanceof Player || e instanceof Bot || e instanceof ServerClient){
			players.add((Mob) e);
		}else if(e instanceof Bomb) {
			bombs.add((Bomb) e);
		}else{
			entities.add(e);
		}
	}
	
	public List<Mob> getPlayers(){
		return players;
	}
	
	public Mob getPlayerAt(int index){
		return (Player) players.get(index);
	}
	
	public Player getClientPlayer(){
		return (Player) players.get(0);
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
	
	public List<Mob> getPlayers(Entity e, int radius){
		List<Mob> result = new ArrayList<Mob>();
		int ex = (int) e.getX();
		int ey = (int) e.getY();
		for(int i = 0; i < players.size(); i++){
			Mob player = players.get(i);
			int x = (int) player.getX();
			int y = (int) player.getY();
			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if(distance <= radius) result.add(player);
		}
		return result;
	}
	
	// Grass = 0xFF00FF00
	// Flower = 0xFFFFFF00
	// Rock = 0xFF7F7F00
	public Tile getTile(int x, int y){
		if(x < 0 || y < 0 || x >= width || y >= height) return Tile.voidTile;
		if(tiles[x+y*width] == Tile.col_spawn_floor) return Tile.spawn_floor;
		if(tiles[x+y*width] == Tile.col_floor) return Tile.spawn_floor;
		if(tiles[x+y*width] == Tile.col_spawn_grass) return Tile.spawn_grass;
		if(tiles[x+y*width] == Tile.col_spawn_hedge) return Tile.spawn_hedge;
		if(tiles[x+y*width] == Tile.col_spawn_wall1) return Tile.spawn_wall1;
		if(tiles[x+y*width] == Tile.col_spawn_wall2) return Tile.spawn_wall2;
		if(tiles[x+y*width] == Tile.col_spawn_water) return Tile.spawn_water;
		return Tile.voidTile;
	}
}







