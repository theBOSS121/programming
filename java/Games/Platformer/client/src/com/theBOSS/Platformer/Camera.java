package com.theBOSS.Platformer;

import com.theBOSS.Platformer.entity.Entity;
import com.theBOSS.Platformer.level.Level;

public class Camera {

	private int offX, offY;

	public Camera(int offX, int offY) {
		this.offX = offX;
		this.offY = offY;
	}

	public void update(Entity e, Level level) {
		int targetX = (int) (e.x + e.width / 2 - Platformer.WIDTH / 2);
		int targetY = (int) (e.y + e.height / 2 - Platformer.HEIGHT / 2);
		double xMove = 0.1 * (offX - targetX);
		double yMove = 0.1 * (offY - targetY);
		if(xMove < 0 && xMove > -1) xMove = -1;
		if(yMove < 0 && yMove > -1) yMove = -1;
		offX -= xMove;
		offY -= yMove;
		if(offX < 0) offX = 0;
		if(offY < 0) offY = 0;
		if(offX > level.lvlWidth * Level.TILE_WIDTH - Platformer.WIDTH) offX = level.lvlWidth * Level.TILE_WIDTH - Platformer.WIDTH;
		if(offY > level.lvlHeight * Level.TILE_HEIGHT - Platformer.HEIGHT) offY = level.lvlHeight * Level.TILE_HEIGHT - Platformer.HEIGHT;
	}

	public int getOffX() {
		return offX;
	}

	public void setOffX(int offX) {
		this.offX = offX;
	}

	public int getOffY() {
		return offY;
	}

	public void setOffY(int offY) {
		this.offY = offY;
	}

}
