package theBOSS.PongUDPClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Pong extends Activity implements OnTouchListener, OnClickListener {

	public static final int PACKET_CAME = 0;
	
	ClientThread myThread;
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case PACKET_CAME:
					String incomingMessage = (String) msg.obj;
					break;
			}
		}
	};
	
	FrameLayout game;
	LinearLayout widgets;
	Renderer renderer;
	SensorManager sm;
	public static float sensorX = 0, sensorY = 0;
	
	static SharedPreferences someData;

	MediaPlayer song;
	
	public static boolean music = false;
	public static int ballColor = 0;
	
	public static int x = -1;
	
	static String ipAddress = "0.0.0.0";
	
	EditText ip;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ip = new EditText(Pong.this);
		ip.setPadding(0, 200, 0, 0);
		ip.setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels / 2);
		Button b = new Button(Pong.this);
		b.setOnClickListener(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		game = new FrameLayout(this);
		renderer = new Renderer(this);
		renderer.setOnTouchListener(this);
		widgets = new LinearLayout(Pong.this);
		
		
		widgets.addView(ip);	
		widgets.addView(b);
		game.addView(renderer);
		game.addView(widgets);
		setContentView(game);

		myThread = new ClientThread(getApplicationContext(), mHandler);
		myThread.start();
	}

	protected void onStop() {
		super.onStop();
		renderer.pause();
	}
	
	protected void onRestart() {
		super.onRestart();
		renderer.resume();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		myThread.close();
	}
	
	public void onBackPressed() {
		return;
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
			x = (int) event.getX();
		}else if(event.getAction() == MotionEvent.ACTION_UP) {
			x = -1;
		}
		return true;
	}

	public void onClick(View v) {
		ipAddress = ip.getText().toString();
	}	
}
