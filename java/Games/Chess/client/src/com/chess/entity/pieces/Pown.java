package com.chess.entity.pieces;

import com.chess.entity.Entity;
import com.chess.graphics.Sprite;

public class Pown extends Entity{

	public boolean firstMove = false;
	
	public Pown(int x, int y, Sprite sprite, int col) {
		this.x = x;
		this.y = y;
		lastX = x;
		lastY = y;
		this.col = col;
		this.sprite = sprite;
	}
	
	public boolean isAllowed() {
		//should work everithing except el passon?
		if(firstMove == false) {
			if(col == 0) {
				if(lastX / 16 == x / 16 && lastY / 16 + 2 == y / 16) {
					int num = 0;
					for(int i = 0; i < level.entities.size(); i++) {
						if(level.entities.get(i).x == this.x && level.entities.get(i).y / 16 + 1 == y / 16) {
							return false;
						}
						
						if(this.y == level.entities.get(i).y && this.x == level.entities.get(i).x) {
							num++;
						}
					}
					if(num == 2) {
						return false;
					}
					firstMove = true;
					return true;
				}
			}else if(col == 1){
				if(lastX / 16 == x / 16 && lastY / 16 - 2 == y / 16) {
					int num = 0;
					for(int i = 0; i < level.entities.size(); i++) {
						if(level.entities.get(i).x == this.x && level.entities.get(i).y / 16 - 1 == y / 16) {
							return false;
						}
						
						if(this.y == level.entities.get(i).y && this.x == level.entities.get(i).x) {
							num++;
						}
					}
					if(num == 2) {
						return false;
					}
					firstMove = true;
					return true;
				}
			}
		}
		if(col == 0) {
			if(lastX / 16 == x / 16 && lastY / 16 + 1 == y / 16) {
				int num = 0;
				for(int i = 0; i < level.entities.size(); i++) {
					if(this.y == level.entities.get(i).y && this.x == level.entities.get(i).x) {
						num++;
					}
				}
				if(num == 2) return false;
				moved = true;
				firstMove = true;
				return true;
			}
			if(lastX / 16 - 1 == x / 16 && lastY / 16 + 1 == y / 16 || lastX / 16 + 1 == x / 16 && lastY / 16 + 1 == y / 16) {
				for(int i = 0; i < level.entities.size(); i++) {
					if(y == level.entities.get(i).y && x == level.entities.get(i).x && level.entities.get(i).col != col) {
						moved = true;
						firstMove = true;
						return true;
					}
				}
				return false;
			}
			
		}else if(col == 1){
			if(lastX / 16 == x / 16 && lastY / 16 - 1 == y / 16) {
				int num = 0;
				for(int i = 0; i < level.entities.size(); i++) {
					if(this.y == level.entities.get(i).y && this.x == level.entities.get(i).x) {
						num++;
					}
				}
				if(num == 2) return false;
				moved = true;
				firstMove = true;
				return true;
			}
			
			if(lastX / 16 - 1 == x / 16 && lastY / 16 - 1 == y / 16 || lastX / 16 + 1 == x / 16 && lastY / 16 - 1 == y / 16) {
				for(int i = 0; i < level.entities.size(); i++) {
					if(y == level.entities.get(i).y && x == level.entities.get(i).x && level.entities.get(i).col != col) {
						moved = true;
						firstMove = true;
						return true;
					}
				}
				return false;
			}
		}
		return false;
	}
	
}
