package theBOSS.Avoid.entity;

import theBOSS.Avoid.graphics.Screen;
import theBOSS.Avoid.graphics.Sprite;

public class Mob4 extends Entity{
	private int time = 0;
	Player player;
	
	public Mob4(double x, double y, Sprite sprite, Player player) {
		super(x, y, sprite);
		this.player = player;
	}

	public void update(){
		if(hitable) {
			if(x < player.x) x++;
			if(x > player.x) x--;
			if(y < player.y) y++;
			if(y > player.y) y--;
		}		
		if(time > 120) hitable = true;
		time++;
	}
	
	public void render(Screen screen) {
		if(hitable) {
			screen.renderSprite((int) x,(int) y, -1, sprite);	
		}else if(!hitable && time % 50 < 25){
			screen.renderSprite((int) x,(int) y, -1, sprite);	
		}
	}
	
	
}
