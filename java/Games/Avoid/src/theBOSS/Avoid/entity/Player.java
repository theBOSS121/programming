package theBOSS.Avoid.entity;

import theBOSS.Avoid.Avoid;
import theBOSS.Avoid.graphics.Screen;
import theBOSS.Avoid.graphics.Sprite;

public class Player extends Entity{

	int trace = -1;
	
	public Player(double x, double y, Sprite sprite) {
		super(x, y, sprite);
		hitable = true;
	}

	
	public void update() {
		move();
	}
	
	private void move() {
		int xm = 0, ym = 0;
		if(Avoid.key.down) {
			ym += 5;
		}
		if(Avoid.key.up) {
			ym -= 5;
		}
		if(Avoid.key.left) {
			xm -= 5;
		}
		if(Avoid.key.right) {
			xm += 5;
		}
		
		x += xm;
		y += ym;
		
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		if(x + width > Avoid.width) x = Avoid.width - width;
		if(y + height > Avoid.height) y = Avoid.height - height;
		
		setTraceDir(xm, ym);
	}
	
	public void setTraceDir(int xm, int ym){
		if(xm == 0 && ym == 0) {
			trace = -1;
		}
		if(xm > 0 && ym > 0) {
			trace = 0;
		}
		if(xm < 0 && ym > 0) {
			trace = 1;
		}
		if(xm < 0 && ym < 0) {
			trace = 2;
		}
		if(xm > 0 && ym < 0) {
			trace = 3;
		}
		if(xm == 0 && ym > 0) {
			trace = 4;
		}
		if(xm == 0 && ym < 0) {
			trace = 5;
		}
		if(ym == 0 && xm > 0) {
			trace = 6;
		}
		if(ym == 0 && xm < 0) {
			trace = 7;
		}	
	}
	
	public void render(Screen screen) {
		screen.renderSprite((int) x,(int) y, trace, sprite);
	}
	
}
