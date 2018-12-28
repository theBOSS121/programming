package com.mime.minefront.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mime.minefront.Game;
import com.mime.minefront.entity.Entity;

public class Level {

	public Block[] blocks;
	public final int width, depth;
	final Random random = new Random();
	
	private List<Entity> entities = new ArrayList<Entity>();
	
	public Level(int width, int depth) {
		this.width = width;
		this.depth = depth;
		blocks = new Block[width * depth];
		generateLevel();
	}
	
	public void tick() {
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	private void generateLevel() {
		for(int z = 0; z < depth; z++) {
			for(int x = 0; x < width; x++) {
				Block block = null;
				if(random.nextInt(18) == 0) {
					block = new SolidBlock();
					block.z = z * Game.BLOCKS_DEPTH;
					block.x = x * Game.BLOCKS_WIDTH;
				}else {
					block = new Block();
					block.z = -8 * Game.BLOCKS_DEPTH;
					block.x = -8 * Game.BLOCKS_WIDTH;
					/*
					if(random.nextInt(15) == 0) {
						block.addSprite(new Sprite(0, 0, 0));
					}*/
				}	
				blocks[x + z * width] = block;
			}
		}		
	}

	public Block create(int x, int z) {
		if(x < 0 || z < 0 || x >= width || z >= depth) {
			return Block.solidWall;
		}
		
		return blocks[x + z * width];
	}

	
}
