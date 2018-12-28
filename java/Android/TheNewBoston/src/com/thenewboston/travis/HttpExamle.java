package com.thenewboston.travis;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

public class HttpExamle extends Activity{

	TextView httpStuff;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.httpex);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().detectAll().build());
		httpStuff = (TextView) findViewById(R.id.tvHttp);
		GetMethodEx test = new GetMethodEx();
		String returned;
		try {
			returned = test.getInternetData();
			httpStuff.setText(returned);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
