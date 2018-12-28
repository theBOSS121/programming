package com.thenewboston.travis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StartingPoint extends Activity {

	int counter;
	Button add, sub;
	TextView display;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starting_point);
		counter = 0;
		add = (Button) findViewById(R.id.bAdd);
		sub = (Button) findViewById(R.id.bSub);
		display = (TextView) findViewById(R.id.tvDisplay);
		add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				counter++;
				display.setText("Your total is " + counter);
			}
		});
		sub.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				counter--;
				display.setText("Your total is " + counter);
			}
		});
	}
}
