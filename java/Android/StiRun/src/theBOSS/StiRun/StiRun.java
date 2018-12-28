package theBOSS.StiRun;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class StiRun extends Activity implements OnTouchListener {
	
	Renderer renderer;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		renderer = new Renderer(this);
		renderer.setOnTouchListener(this);
		setContentView(renderer);
	}

	protected void onPause() {
		super.onPause();
		renderer.pause();
	}

	protected void onRestart() {
		super.onRestart();
		renderer.resume();
		
	}
	
	public void onBackPressed() {
		return;
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		try {
			Thread.sleep(50);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			if(renderer.animDisplayed != 1) {
				renderer.animDisplayed = 1;
				renderer.animCounter = 0;
				renderer.animChanged = System.currentTimeMillis();
			} else if (!renderer.fastRollOver && renderer.animCounter <= 3){
				renderer.fastRollOver = true;
				renderer.animCounter = 3;
				renderer.animChanged = System.currentTimeMillis();
			}
			if(renderer.gameOver) renderer.gameOver = false;
		}
		if(event.getAction() == MotionEvent.ACTION_UP) {
			
		}
		return true;
	}

}
