package com.chess.entity.pieces;

import com.chess.entity.Entity;
import com.chess.graphics.Sprite;

public class Queen extends Entity{
	
	public Queen(int x, int y, Sprite sprite, int col) {
		this.x = x;
		this.y = y;
		lastX = x;
		lastY = y;
		this.col = col;
		this.sprite = sprite;
	}
	
	public boolean isAllowed() {
		if(lastX / 16  != x / 16  && lastY / 16 != y / 16) {
			for(int i = 1; i < 8; i++) {
				if((lastX / 16 + i == x / 16  && lastY / 16 + i == y / 16) ||
				   (lastX / 16 - i == x / 16  && lastY / 16 - i == y / 16) ||
				   (lastX / 16 - i == x / 16  && lastY / 16 + i == y / 16) ||
				   (lastX / 16 + i == x / 16  && lastY / 16 - i == y / 16)) {
					return !inBetween(lastX, lastY, x, y);
				}
			}
		}else {
			for(int i = 1; i < 8; i++) {
				if((lastX / 16 + i == x / 16  && lastY / 16 == y / 16) ||
				   (lastX / 16 - i == x / 16  && lastY / 16 == y / 16) ||
				   (lastX / 16 == x / 16  && lastY / 16 + i == y / 16) ||
				   (lastX / 16 == x / 16  && lastY / 16 - i == y / 16)) {
					return !inBetween(lastX, lastY, x, y);
				}
			}
		}
		return false;
	}
}
