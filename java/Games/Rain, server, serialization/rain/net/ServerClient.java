package com.thecherno.rain.net;

import com.thecherno.rain.Game;
import com.thecherno.rain.entity.mob.Mob;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;

public class ServerClient extends Mob{
	public int x, y;
	public boolean status = false;
	
	public Sprite sprite;
	
	public ServerClient(int x, int y, Sprite sprite){
		this.x = x;
		this.y = y;
		status = true;
		this.sprite = sprite;
		Game.level.addPlayer(this);
	}
	
	public void setSprite(Sprite sprite){
		this.sprite = sprite;
	}


	public void update() {
	}


	public void render(Screen screen) {
		//sprite = Sprite.player_side;
		screen.renderMob((x - 16), (y - 16), this.sprite, 0);
	}
}
