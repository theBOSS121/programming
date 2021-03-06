package theBOSS.Avoid.entity;

import java.util.Random;

import theBOSS.Avoid.Avoid;
import theBOSS.Avoid.graphics.Screen;
import theBOSS.Avoid.graphics.Sprite;

public class Mob3 extends Entity{

	private int xd, yd;
	private int time = 0;
	
	private static Random rand = new Random();
	
	public Mob3(double x, double y, Sprite sprite) {
		super(x, y, sprite);
		changeDir();
	}

	public void update(){
		if(x + xd < 0 || x + xd + width > Avoid.width) xd *= -1;
		if(y + yd < 0 || y + yd  + height > Avoid.height) yd *= -1;
		if(hitable) {
			x += xd;
			y += yd;
		
			if(time % 240 == 120) changeDir();
		}		
		if(time > 120) hitable = true;
		time++;
	}
	
	private void changeDir() {
		do{
			xd = rand.nextInt(7) - 3;
			yd = rand.nextInt(7) - 3;
		}while(!(xd == yd || xd == -yd) || xd == 0 || yd == 0);
	}
	
	public void render(Screen screen) {
		if(hitable) {
			screen.renderSprite((int) x,(int) y, -1, sprite);	
		}else if(!hitable && time % 50 < 25){
			screen.renderSprite((int) x,(int) y, -1, sprite);	
		}
	}
	
}
