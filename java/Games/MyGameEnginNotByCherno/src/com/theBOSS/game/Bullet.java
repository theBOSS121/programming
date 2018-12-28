package com.theBOSS.game;

import com.theBOSS.engine.GameContainer;
import com.theBOSS.engine.Renderer;

public class Bullet extends GameObject{

	private int tileX, tileY;
	private float offX, offY;
	
	private int direction;
	private float speed = 200;
	
	public Bullet(int tileX, int tileY, float offX, float offY, int direction) {
		this.direction = direction;		
		this.tileX = tileX;
		this.tileY = tileY;
		this.offX = offX;
		this.offY = offY;
		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;		
	}
	
	public void update(GameContainer gc, GameManager gm, float dt) {
		switch(direction) {
			case 0: offY -= speed * dt; break;
			case 1: offX += speed * dt; break;
			case 2: offY += speed * dt; break;
			case 3: offX -= speed * dt; break;
		}
		
		if(offY > GameManager.TS) {
			tileY++;
			offY -= GameManager.TS;
		}
		if(offY < 0) {
			tileY--;
			offY += GameManager.TS;
		}
		if(offX > GameManager.TS) {
			tileX++;
			offX -= GameManager.TS;
		}
		if(offX < 0) {
			tileX--;
			offX += GameManager.TS;
		}
		
		if(gm.getCollision(tileX, tileY)) {
			this.dead = true;
		}
		
		posX = tileX * GameManager.TS + offX;
		posY = tileY * GameManager.TS + offY;
		
	}

	public void render(GameContainer gc, Renderer r) {
		r.drawFillRect((int) posX - 2,(int) posY - 2, 4, 4, 0xffff0000);
	}

}
