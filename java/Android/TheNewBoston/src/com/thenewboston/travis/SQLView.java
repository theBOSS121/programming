package com.thenewboston.travis;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SQLView extends Activity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqlview);
		TextView tv = (TextView) findViewById(R.id.tvSQLinfo);
		HotOrNot info = new HotOrNot(this);
		info.open();
		String data = info.getData();
		info.close();
		tv.setText(data);
	}
	
}
