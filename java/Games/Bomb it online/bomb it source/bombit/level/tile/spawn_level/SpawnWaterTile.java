package com.bombit.level.tile.spawn_level;

import com.bombit.graphics.Screen;
import com.bombit.graphics.Sprite;
import com.bombit.level.tile.Tile;

public class SpawnWaterTile extends Tile {

	public SpawnWaterTile(Sprite sprite) {
		super(sprite);
	}
	
	public void render(int x, int y, Screen screen){
		screen.renderTile(x << 4, y << 4, this);
	}

}
