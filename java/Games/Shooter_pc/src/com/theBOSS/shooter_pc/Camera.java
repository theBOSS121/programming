package com.theBOSS.shooter_pc;

import com.theBOSS.shooter_pc.entity.Entity;
import com.theBOSS.shooter_pc.game.Game;

public class Camera {

	private int offX = 0, offY;

	public Camera(int offX, int offY) {
		this.offX = offX;
		this.offY = offY;
	}

	public void update(Entity e, int x, int y) {
		int targetX = (int) (e.x + e.width / 2 - Shooter.WIDTH / 2) + x;
		int targetY = (int) (e.y + e.height / 2 - Shooter.HEIGHT / 2) + y;
		double yMove = 0.3 * (offY - targetY);
		double xMove = 0.3 * (offX - targetX);
		if(yMove < 0 && yMove > -1) yMove = -1;
		if(xMove < 0 && xMove > -1) xMove = -1;
		offY -= yMove;
		offX -= xMove;
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
