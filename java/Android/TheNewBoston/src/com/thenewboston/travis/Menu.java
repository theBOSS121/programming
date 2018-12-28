package com.thenewboston.travis;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity{

	private String[] classes = {"StartingPoint", "TextPlay", "Email", "Camera", "Data", "GFX", "GFXSurface","SoundStuff", "Slider", 
								"Tabs", "SimpleBrowser", "Flipper", "SharedPrefs", "InternalData", "ExternalData", "SQLiteExamle",
								"Accelerate", "HttpExamle", "GLExample", "Voice", "TextVoice", "StatusBar", "SeekBarVolume"};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setListAdapter(new ArrayAdapter<String>(Menu.this, android.R.layout.simple_list_item_1, classes));
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		try {
			Class ourClass = Class.forName("com.thenewboston.travis." + classes[position]);
			Intent ourIntent = new Intent(Menu.this, ourClass);
			startActivity(ourIntent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.cool_menu, menu);
		return true;
	}	

	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.aboutUs) {
			Intent i = new Intent("com.thenewboston.travis.ABOUT");
			startActivity(i);
		}else if(item.getItemId() == R.id.preferences) {
			Intent i = new Intent("com.thenewboston.travis.PREFS");
			startActivity(i);
		}else if(item.getItemId() == R.id.exit) {
			finish();
		}
		return false;
	}
}
