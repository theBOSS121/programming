package com.mime.minefront;

import java.awt.event.KeyEvent;

import com.mime.minefront.entity.mob.Player;
import com.mime.minefront.input.InputHandler;
import com.mime.minefront.level.Level;

public class Game {
	public int time;
	public Player player;
	public Level level;
	public static final int WIDTH_IN_BLOCKS = 10;
	public static final int DEPTH_IN_BLOCKS = 10;
	
	public static final int BLOCKS_WIDTH = 8;
	public static final int BLOCKS_DEPTH = 8;
	public static final int LEVEL_WIDTH = WIDTH_IN_BLOCKS * BLOCKS_WIDTH;
	public static final int LEVEL_DEPTH = DEPTH_IN_BLOCKS * BLOCKS_DEPTH;
	
	
	public Game(InputHandler input){
		player = new Player(input, 10, 10);
		level = new Level(WIDTH_IN_BLOCKS, DEPTH_IN_BLOCKS);
		level.addEntity(player);
	}
	
	public void tick(){
		time++;
		level.tick();
	}
	
}
