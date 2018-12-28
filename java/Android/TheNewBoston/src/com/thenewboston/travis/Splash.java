package com.thenewboston.travis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Splash extends Activity{

	MediaPlayer ourSong;
	
	protected void onCreate(Bundle TravisLoveBacon) {
		super.onCreate(TravisLoveBacon);
		setContentView(R.layout.splash);
		ourSong = MediaPlayer.create(Splash.this, R.raw.shapeofyou);
		ourSong.seekTo(1900);
		
		SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		final boolean music = getPrefs.getBoolean("checkbox", true);
		if(music) ourSong.start();
		
		Thread timer = new Thread("splashSleepingThread") {
			public void run() {
				try {
					if(music) sleep(4800);
					else sleep(500);
					
				}catch(InterruptedException e) {
					e.printStackTrace();
				}finally {
					Intent openStartingPoint = new Intent("com.thenewboston.travis.MENU");
					startActivity(openStartingPoint);
				}
			}
		};
		timer.start();
	}
	
	protected void onPause() {
		super.onPause();
		ourSong.release();
		finish();
	}
	
}
