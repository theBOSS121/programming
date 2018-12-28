package com.thenewboston.travis;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Slider extends Activity implements OnClickListener, OnCheckedChangeListener, OnDrawerOpenListener{

	SlidingDrawer sd;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sliding);
		Button handle1 = (Button) findViewById(R.id.handle1);
		Button handle2 = (Button) findViewById(R.id.handle2);
		Button handle3 = (Button) findViewById(R.id.handle3);
		Button handle4 = (Button) findViewById(R.id.handle4);
		CheckBox checkbox = (CheckBox) findViewById(R.id.cbSlidable);
		sd = (SlidingDrawer) findViewById(R.id.slidingD);
		sd.setOnDrawerOpenListener(this);
		checkbox.setOnCheckedChangeListener(this);
		handle1.setOnClickListener(this);
		handle2.setOnClickListener(this);
		handle3.setOnClickListener(this);
		handle4.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		if(v.getId() == R.id.handle1) {
			sd.open();
		}else if(v.getId() == R.id.handle2) {
			
		}else if(v.getId() == R.id.handle3) {
			sd.toggle();
		}else if(v.getId() == R.id.handle4) {
			sd.close();
		}
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView.isChecked()) {
			sd.lock();
		}else {
			sd.unlock();
		}
	}

	public void onDrawerOpened() {
		MediaPlayer mp = MediaPlayer.create(this, R.raw.explosion);
		mp.start();
	}

}
