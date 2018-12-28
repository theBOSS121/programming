package com.chess.entity.pieces;

import com.chess.entity.Entity;
import com.chess.graphics.Sprite;

public class King extends Entity{
	
	
	
	public King(int x, int y, Sprite sprite, int col) {
		this.x = x;
		this.y = y;
		lastX = x;
		lastY = y;
		this.col = col;
		this.sprite = sprite;
	}
	
	public boolean isAllowed() {
		if((lastX / 16 + 1 == x / 16  && lastY / 16 == y / 16) || (lastX / 16 - 1 == x / 16  && lastY / 16 == y / 16) 
		|| (lastX / 16 == x / 16  && lastY / 16 + 1 == y / 16) || (lastX / 16  == x / 16  && lastY / 16 - 1 == y / 16)
		|| (lastX / 16 + 1 == x / 16  && lastY / 16 + 1 == y / 16) || (lastX / 16 - 1 == x / 16  && lastY / 16 - 1 == y / 16)
		|| (lastX / 16 + 1 == x / 16  && lastY / 16 - 1 == y / 16) || (lastX / 16 - 1 == x / 16  && lastY / 16  + 1== y / 16)) {
			moved = true;
			return true;
		}
		
		if(moved == false) {
			if(lastY / 16 == y / 16 && lastX / 16 + 2 == x / 16) {
				if(!inBetween(lastX, lastY, x, y)) {
					for(int i = 0; i < level.entities.size(); i++) {
						Entity e = level.entities.get(i);
						if(e.y == y && e.x / 16 - 1 == x / 16 && e.moved == false) {
							e.x = (x / 16 - 1) * 16;
							e.lastX = e.x;
							moved = true;
							e.moved = true;
							return true;
						}
					}
				}
			}
			
			if(lastY / 16 == y / 16 && lastX / 16 - 2 == x / 16) {
				if(!inBetween(lastX, lastY, x, y) && !inBetween(x, y, x - 32, y)) {
					for(int i = 0; i < level.entities.size(); i++) {
						Entity e = level.entities.get(i);
						if(e.y == y && e.x / 16 + 2 == x / 16 && e.moved == false) {
							e.x = (x / 16 + 1) * 16;
							e.lastX = e.x;
							moved = true;
							e.moved = true;
							return true;
						}
					}
				}
			}			
		}
		
		return false;
	}
}
