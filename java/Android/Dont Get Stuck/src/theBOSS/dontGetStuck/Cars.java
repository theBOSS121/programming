package theBOSS.dontGetStuck;

import android.graphics.Rect;

public class Cars {

	Rect rect;
	int ySpeed;
	boolean crashed = false;
		
	public Cars(int x, int y, int width, int height, int screenHeight, int ySpeed) {
		rect = new Rect(x, y, x + width, y + height);
		this.ySpeed = ySpeed;
	}
	
}
