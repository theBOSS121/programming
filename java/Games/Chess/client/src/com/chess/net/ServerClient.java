package com.chess.net;

import com.chess.Game;
import com.chess.entity.Entity;
import com.chess.graphics.Screen;
import com.chess.graphics.Sprite;

public class ServerClient extends Entity{
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
	


	public void render(Screen screen) {
		//sprite = Sprite.player_side;
		//screen.renderMob((int) (x - 16),(int) (y - 16), this.sprite, this);
	}
}
