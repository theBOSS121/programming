package com.thenewboston.travis;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class GFX extends Activity{

	MyBringBack ourView;
	WakeLock wl;	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		//wake lock
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "whatever");
				
		super.onCreate(savedInstanceState);
		wl.acquire();
		ourView = new MyBringBack(this);
		setContentView(ourView);
	}
	
	protected void onPause() {
		super.onPause();
		wl.release();
	}
	
}
