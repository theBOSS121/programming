package theBOSS.dontGetStuck;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class DontGetStuck extends Activity implements OnTouchListener {
	
	Renderer renderer;
	static boolean left = false, right = false, tapListener = false, ready = false, restart = false;
	static SharedPreferences someData;

	MediaPlayer song;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		renderer = new Renderer(this);
		renderer.setOnTouchListener(this);
		setContentView(renderer);
		someData = getSharedPreferences("theBOSS.dontGetStuck.bestScore", 0);
		String dataReturned = someData.getString("bestScore", "0");
		renderer.bestScore = Integer.parseInt(dataReturned);
//		song = MediaPlayer.create(DontGetStuck.this, R.raw.dgs);
//		song.setLooping(true);
//		song.start();
	}

	protected void onPause() {
		super.onPause();
		renderer.pause();
//		song.release();
	}

	protected void onRestart() {
		super.onRestart();
		renderer.resume();
		song = MediaPlayer.create(DontGetStuck.this, R.raw.dgs);
//		song.setLooping(true);
//		song.start();
		
	}
	
	//because super finish the activity so you can play on
	public void onBackPressed() {
		return;
	}

	public static void save(int best) {
		SharedPreferences.Editor editor = someData.edit();
		editor.putString("bestScore", Integer.toString(best));
		editor.commit();
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		try {
			Thread.sleep(50);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		if(renderer.gameOver && tapListener && event.getAction() == MotionEvent.ACTION_DOWN) {
			ready = true;
		}
		
		if(ready && event.getAction() == MotionEvent.ACTION_DOWN) {
			ready = false;
			restart = true;
		}
		if(restart && event.getAction() == MotionEvent.ACTION_UP) {
			renderer.restart();
			restart = false;
		}
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			if(event.getX() > renderer.width / 2) {
				right = true;
				left = false;
			}
			if(event.getX() < renderer.width / 2) { 
				left = true;
				right = false;
			}
		}else if(event.getAction() == MotionEvent.ACTION_UP) {
			left = false;
			right = false;
		}else if(event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
			if(event.getX() > renderer.width / 2) {
				right = true;
				left = false;
			}
			if(event.getX() < renderer.width / 2) { 
				left = true;
				right = false;
			}
		}else if(event.getAction() == MotionEvent.ACTION_POINTER_UP) {
			left = false;
			right = false;
		}
		return true;
	}

}








