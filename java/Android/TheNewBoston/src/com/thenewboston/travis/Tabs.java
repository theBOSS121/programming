package com.thenewboston.travis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class Tabs extends Activity implements OnClickListener{

	TabHost th;
	TextView showResults;
	long start = 0, stop;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);
		th = (TabHost) findViewById(R.id.tabhost);
		Button newTab = (Button) findViewById(R.id.bAddTab);
		Button bStart = (Button) findViewById(R.id.bstartWatch);
		Button bStop = (Button) findViewById(R.id.bstopWatch);
		showResults = (TextView) findViewById(R.id.tvShowResults);
		newTab.setOnClickListener(this);
		bStart.setOnClickListener(this);
		bStop.setOnClickListener(this);
		th.setup();
		TabSpec specs = th.newTabSpec("tag1");
		specs.setContent(R.id.tab1);
		specs.setIndicator("StopWatch");
		th.addTab(specs);
		specs = th.newTabSpec("tag2");
		specs.setContent(R.id.tab2);
		specs.setIndicator("Tab 2");
		th.addTab(specs);
		specs = th.newTabSpec("tag3");
		specs.setContent(R.id.tab3);
		specs.setIndicator("Tab 3");
		th.addTab(specs);
	}

	public void onClick(View v) {
		if(v.getId() == R.id.bAddTab) {
			TabSpec ourSpec = th.newTabSpec("tag1");
			ourSpec.setContent(new TabHost.TabContentFactory() {
				public View createTabContent(String tag) {
					TextView text = new TextView(Tabs.this);
					text.setText("You've created new tab");
					return (text);
				}
			});
			ourSpec.setIndicator("new tab");
			th.addTab(ourSpec);			
		}else if(v.getId() == R.id.bstartWatch) {
			start = System.currentTimeMillis();
		}else if(v.getId() == R.id.bstopWatch) {
			stop = System.currentTimeMillis();
			if(start != 0) {
				double result = (stop - start) / 1000.0;
				showResults.setText(Double.toString(result));
			}
		}
		
	}
	
}
