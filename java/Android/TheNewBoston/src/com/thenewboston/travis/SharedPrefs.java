package com.thenewboston.travis;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SharedPrefs extends Activity implements OnClickListener{

	EditText sharedData;
	TextView dataResults;
	public static String filename = "MySharedString";
	SharedPreferences someData;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sharedpreferences);
		init();
		someData = getSharedPreferences(filename, 0);
	}

	private void init() {
		Button save = (Button) findViewById(R.id.bSave);
		Button load = (Button) findViewById(R.id.bLoad);
		sharedData = (EditText) findViewById(R.id.etSharedPrefs);
		dataResults = (TextView) findViewById(R.id.tvLoadSharedPrefs);
		save.setOnClickListener(this);
		load.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(v.getId() == R.id.bSave) {
			String stringData = sharedData.getText().toString();
			SharedPreferences.Editor editor = someData.edit();
			editor.putString("sharedString", stringData);
			editor.commit();
		}else if(v.getId() == R.id.bLoad) {
			someData = getSharedPreferences(filename, 0);
			String dataReturned = someData.getString("sharedString", "Couldn't Load data");
			dataResults.setText(dataReturned);
		}
	}
		
	
}
