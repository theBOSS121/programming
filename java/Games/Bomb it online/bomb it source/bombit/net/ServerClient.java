package com.bombit.net;

import com.bombit.Game;
import com.bombit.entity.Bomb;
import com.bombit.entity.mob.Mob;
import com.bombit.graphics.Screen;
import com.bombit.graphics.Sprite;

public class ServerClient extends Mob{
	public boolean status = false;
	
	public Sprite sprite;
	
	public ServerClient(int x, int y, Sprite sprite){
		this.x = x;
		this.y = y;
		status = true;
		this.sprite = sprite;
		this.init(level);
		Game.level.add(this);
	}
	
	public void setSprite(Sprite sprite){
		this.sprite = sprite;
	}


	public void update() {
		
	}
	
	public void plantBomb(int i ) {
		if(i == 1) {
			level.add(new Bomb((int) x,(int) y, range, this, level));
		}
	}


	public void render(Screen screen) {
		//sprite = Sprite.player_side;
		screen.renderMob((int) (x - 16),(int) (y - 16), this.sprite, this);
	}
}
