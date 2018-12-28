package com.thenewboston.travis;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InternalData extends Activity implements OnClickListener{

	EditText sharedData;
	TextView dataResults;
	FileOutputStream fos;
	String 	FILENAME = "InternalString";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sharedpreferences);
		init();
	}
	

	private void init() {
		Button save = (Button) findViewById(R.id.bSave);
		Button load = (Button) findViewById(R.id.bLoad);
		sharedData = (EditText) findViewById(R.id.etSharedPrefs);
		dataResults = (TextView) findViewById(R.id.tvLoadSharedPrefs);
		save.setOnClickListener(this);
		load.setOnClickListener(this);
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onClick(View v) {
		if(v.getId() == R.id.bSave) {
			String data = sharedData.getText().toString();
//			Saving data via File
//			File f = new File(FILENAME);
//			try {
//				fos = new FileOutputStream(f);
//				fos.close();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			try {
				fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(data.getBytes());
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(v.getId() == R.id.bLoad) {
			new loadSomeStuff().execute(FILENAME);
		}
	}
	
	public class loadSomeStuff extends AsyncTask<String, Integer, String> {
		
		ProgressDialog dialog;
		
		protected void onPreExecute() {
			dialog = new ProgressDialog(InternalData.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setMax(100);
			dialog.show();
		}
		
		protected String doInBackground(String... params) {
			String collected = null;
			FileInputStream fis = null;
			for(int i = 0; i < 20; i++) {
				publishProgress(5);
				try {
					Thread.sleep(88);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			dialog.dismiss();
			try {
				fis = openFileInput(FILENAME);
				byte[] dataArray = new byte[fis.available()];
				while(fis.read(dataArray) != -1) {
					collected = new String(dataArray);
				}
				fis.close();
				return collected;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onProgressUpdate(Integer... progress) {
			dialog.incrementProgressBy(progress[0]);
		}
		
		protected void onPostExecute(String result) {
			dataResults.setText(result);
		}
		
	}	
}









