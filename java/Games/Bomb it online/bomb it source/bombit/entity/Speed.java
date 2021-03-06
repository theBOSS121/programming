package com.bombit.entity;

import com.bombit.entity.mob.Mob;
import com.bombit.graphics.Screen;
import com.bombit.graphics.Sprite;

public class Speed extends Entity{

	public Speed(double x, double y) {
		this.x = x;
		this.y = y;
		sprite = Sprite.speed;
	}
	
	public void update() {
		for(int i = 0 ; i < level.players.size(); i++) {
			Mob m = level.players.get(i);
			if((x / 16) == Math.round((m.x - 8) / 16) && (y / 16) == Math.round((m.y - 8) / 16)){
				if(m.speed == 1) {
					//m.speed++;
				}
				this.remove();
			}
		
		}
	}
	
	public void render(Screen screen) {
		screen.renderSprite((int) x,(int) y, sprite, true);
	}
}
