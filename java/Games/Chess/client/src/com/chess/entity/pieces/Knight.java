package com.chess.entity.pieces;

import com.chess.entity.Entity;
import com.chess.graphics.Sprite;

public class Knight extends Entity{

	public Knight(int x, int y, Sprite sprite, int col) {
		this.x = x;
		this.y = y;
		lastX = x;
		lastY = y;
		this.col = col;
		this.sprite = sprite;
	}
	
	public boolean isAllowed() {
		if(lastX / 16  == x / 16  || lastY / 16 == y / 16) {
			return false;
		}
		if((lastX / 16 + 2 == x / 16  && lastY / 16 + 1 == y / 16) ||
		   (lastX / 16 - 2 == x / 16  && lastY / 16 - 1 == y / 16) ||
		   (lastX / 16 - 1 == x / 16  && lastY / 16 - 2 == y / 16) ||
		   (lastX / 16 + 1 == x / 16  && lastY / 16 + 2 == y / 16) ||
		   (lastX / 16 - 2 == x / 16  && lastY / 16 + 1 == y / 16) ||
		   (lastX / 16 + 2 == x / 16  && lastY / 16 - 1 == y / 16) ||
		   (lastX / 16 - 1 == x / 16  && lastY / 16 + 2 == y / 16) ||
		   (lastX / 16 + 1 == x / 16  && lastY / 16 - 2 == y / 16)) {
			return true;
		}
		
		return false;
	}
}
