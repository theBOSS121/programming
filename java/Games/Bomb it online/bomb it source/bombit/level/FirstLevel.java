package com.bombit.level;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.bombit.level.tile.Tile;

public class FirstLevel extends Level {
	
	public FirstLevel(String path) {
		super(path);
	}

	protected void loadLevel(String path){
		try{
			BufferedImage image = ImageIO.read(SpawnLevel.class.getResource(path));
			int w = width = image.getWidth();
			int h = height = image.getHeight();
			tiles = new int [w * h];
			image.getRGB(0, 0, w, h, tiles, 0, w);
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Exception! Could not load level file!");
		}
	}
	
	protected void generateLevel(){
		Random random = new Random();
		int rand;
		for(int i = 0; i < tiles.length; i++) {
			if(tiles[i] == Tile.col_spawn_floor)
					tiles[i] = Tile.col_spawn_wall2;
		}
	}
}
