package com.theBOSS.retroshooter;

import java.net.InetAddress;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class RetroShooter extends Activity implements OnTouchListener {

	public static final int PACKET_CAME = 0;
	public static final int IS_RUNNING = 1;
	public static final int IP_ADDRESS = 2;
	

	public static ServerThread myServerThread = null;
	public static ClientThread myClientThread = null;
	
	public static String infos = "";
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case PACKET_CAME:
					String incomingMessage = (String) msg.obj;
					infos = incomingMessage;
					break;
				case IS_RUNNING:
					String socketStatus = (String) msg.obj;
					infos = socketStatus;
					break;
				case IP_ADDRESS:
					InetAddress myIPAddress = (InetAddress) msg.obj;
					infos += myIPAddress.toString();
					break;	
			}
		}
	};
	
	Renderer renderer;
	
	public static int x1 = -1, y1 = -1, x2 = -1, y2 = -1, id1 = -1, id2 = -1;
	public static boolean isDown1 = false, isDown2 = false, isDownNow = false;
	
	static String ipAddress = "192.168.1.69";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		renderer = new Renderer(this);
		renderer.setOnTouchListener(this);
		setContentView(renderer); 
		
		myServerThread = new ServerThread(getApplicationContext(), mHandler);
		myClientThread = new ClientThread(getApplicationContext(), mHandler);

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
		myServerThread.closeSocket();
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		
		if(Game.state == Game.MENU) {
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				x1 = (int) (event.getX() * Renderer.WIDTH) / (getWindowManager().getDefaultDisplay().getWidth());
				y1 = (int) (event.getY() * Renderer.HEIGHT) / (getWindowManager().getDefaultDisplay().getHeight());
				isDownNow = true;
				isDown1 = true;
			}
			if(event.getAction() == MotionEvent.ACTION_MOVE) {
				x1 = (int) (event.getX() * Renderer.WIDTH) / (getWindowManager().getDefaultDisplay().getWidth());
				y1 = (int) (event.getY() * Renderer.HEIGHT) / (getWindowManager().getDefaultDisplay().getHeight());
				isDown1 = true;
				isDownNow = false;
			}else if(event.getAction() == MotionEvent.ACTION_UP) {
				x1 = -1;
				y1 = -1;
				isDown1 = false;
				isDownNow = false;
			}
		}else {
			
			//repair
			
	        int pointerIndex = event.getActionIndex();
	        int pointerId = event.getPointerId(pointerIndex);
	        int maskedAction = event.getActionMasked();
	        
	        if(event.getPointerCount() < 3) {

	        switch (maskedAction) {
	        case MotionEvent.ACTION_DOWN: {
	        	if(event.getX(0) < getWindowManager().getDefaultDisplay().getWidth() / 2) {
	        		id1 = pointerIndex;
	        		isDown1 = true;
	        	}else {
	        		id2 = pointerId;
	        		isDown2 = true;
	        	}
	        	break;
	        }
	        case MotionEvent.ACTION_POINTER_DOWN: {
	        	if(pointerIndex < 2) {
		        	if(event.getX(1) < getWindowManager().getDefaultDisplay().getWidth() / 2) {
		        		id1 = pointerIndex;
		        		isDown1 = true;
		        	}else {
		        		id2 = pointerId;
		        		isDown2 = true;
		        	}
		            break;
	        	}
	        }
	        case MotionEvent.ACTION_MOVE: {
	        	if(id1 != -1) {
	        		if(event.getPointerCount() == 1) id1 = 0;
	        		x1 = (int) ((event.getX(id1) * Renderer.WIDTH) / (getWindowManager().getDefaultDisplay().getWidth()));
	        		y1 = (int) (event.getY(id1) * Renderer.HEIGHT) / (getWindowManager().getDefaultDisplay().getHeight());
	        		isDown1 = true;
	        	}
	        	if(id2 != -1) {
	        		if(event.getPointerCount() == 1) id2 = 0;
	        		x2 = (int) ((event.getX(id2) * Renderer.WIDTH) / (getWindowManager().getDefaultDisplay().getWidth()));
	        		y2 = (int) (event.getY(id2) * Renderer.HEIGHT) / (getWindowManager().getDefaultDisplay().getHeight()); 
	        		isDown2 = true;     		
	        	}
	            break;
	        }
	        case MotionEvent.ACTION_UP:{
        		id1 = -1;
        		x1 = -1;
        		y1 = -1;
        		isDown1 = false;
        		id2 = -1;
        		x2 = -1;
        		y2 = -1;
        		isDown2 = false;
	        	break;
	        }
	        case MotionEvent.ACTION_POINTER_UP:{
	        	if(pointerId == id1) {
	        		id1 = -1;
	        		x1 = -1;
	        		y1 = -1;
	        		if(id2 != 0) id2 = 0;
	        		isDown1 = false;
	        	}
	        	if(pointerId == id2) {
	        		id2 = -1;
	        		x2 = -1;
	        		y2 = -1;
	        		if(id1 != 0) id1 = 0;
	        		isDown2 = false;
	        	}
	        	break;
	        }
	        	
	        }	
		
	        }
		
	        
	    //repair 
		}
		
		return true;
	}

	public static void server() {
		myServerThread.init();
		myServerThread.start();	
	}
	
	public static void client() {
		myClientThread.init();
		myClientThread.start();
	}
	
}
