package theBOSS.StiRun;

import android.graphics.Rect;

public class MyRect {

	Rect r;
	int type;
	boolean remove = false;
	
	public MyRect(Rect r, int type) {
		this.r = r;
		this.type = type;
	}
	
	public void update() {
		r.left -= Renderer.speed;
		r.right -= Renderer.speed;
		if(r.right < 0) {
			remove = true;
		}
	}
	
}
