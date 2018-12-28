package theBOSS.FlappyBird;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class FlappyBird extends Activity implements OnTouchListener {

	Renderer renderer;
	static SharedPreferences someData;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		renderer = new Renderer(this);
		renderer.setOnTouchListener(this);
		setContentView(renderer);
		someData = getSharedPreferences("theBOSS.flappyBird.bestScore", 0);
		String dataReturned = someData.getString("bestScore", "0");
		renderer.bestScore = Integer.parseInt(dataReturned);
	}

	protected void onPause() {
		super.onPause();
		renderer.pause();
	}

	protected void onRestart() {
		super.onRestart();
		renderer.resume();
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
		renderer.tapped = true;
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				if(Renderer.gameOver) {
					renderer.restart();
				}
				break;
		}
		return true;
	}

}
