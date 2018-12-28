package com.theBOSS.Nalis_pc;

import com.theBOSS.Nalis_pc.entity.Entity;
import com.theBOSS.Nalis_pc.game.Game;

public class Camera {

	private int offX = 0, offY;

	public Camera(int offX, int offY) {
		this.offX = offX;
		this.offY = offY;
	}

	public void update(Entity e, int y,  Game game) {
		int targetY = (int) (e.y + e.height / 2 - Nails.HEIGHT / 2) + y;
		double yMove = 0.1 * (offY - targetY);
		if(yMove < 0 && yMove > -1) yMove = -1;
		offY -= yMove;
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
