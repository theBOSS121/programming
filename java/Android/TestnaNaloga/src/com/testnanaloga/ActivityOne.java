package com.testnanaloga;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ActivityOne extends Activity implements OnItemSelectedListener, OnClickListener, OnTouchListener{

	Button speak;
	EditText text;
	Spinner spinner;
	String[] languages = {"Anglešèina", "Nemšèina", "Francosèina", "Italijanšèina"};
	TextToSpeech tts;	
	
	int x = -1, y = -1;
	boolean meybeBack = false, closeApp = false;
	
	//for collapsing status bar
	boolean currentFocus;
	boolean isPaused;
	Handler collapseNotificationHandler;
	///////////////////////////
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		RelativeLayout r = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_one, null);
		setContentView(r);
		r.setOnTouchListener(this);		
		
		speak = (Button) findViewById(R.id.bTextToSpeech);
		text = (EditText) findViewById(R.id.etText);
		
		speak.setOnClickListener(this);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityOne.this, android.R.layout.simple_spinner_item, languages);
		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);

		tts = new TextToSpeech(ActivityOne.this, new OnInitListener() {
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR) {
					tts.setLanguage(Locale.ENGLISH);
				}
			}
		});	
		
		
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		int pos = spinner.getSelectedItemPosition();
		if(pos == 0) {
			tts = new TextToSpeech(ActivityOne.this, new OnInitListener() {
				public void onInit(int status) {
					if(status != TextToSpeech.ERROR) {
						tts.setLanguage(Locale.ENGLISH);
					}
				}
			});	
		}else if(pos == 1) {
			tts = new TextToSpeech(ActivityOne.this, new OnInitListener() {
				public void onInit(int status) {
					if(status != TextToSpeech.ERROR) {
						tts.setLanguage(Locale.GERMAN);
					}
				}
			});	
		}else if(pos == 2) {
			tts = new TextToSpeech(ActivityOne.this, new OnInitListener() {
				public void onInit(int status) {
					if(status != TextToSpeech.ERROR) {
						tts.setLanguage(Locale.FRENCH);
					}
				}
			});	
		}else if(pos == 3) {
			tts = new TextToSpeech(ActivityOne.this, new OnInitListener() {
				public void onInit(int status) {
					if(status != TextToSpeech.ERROR) {
						tts.setLanguage(Locale.ITALIAN);
					}
				}
			});	
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {}

	@SuppressWarnings("deprecation")
	public void onClick(View v) {
//		this one is for api under 21 deprecated
		if (Build.VERSION.SDK_INT >= 21) {
			tts.speak(text.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "ActivityOne");
		}else {
			tts.speak(text.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
		}
	}
	
	public void onBackPressed() {return;}

	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
			x = (int) event.getX();
			meybeBack = true;
			closeApp = false;
		}
		if(event.getAction() == MotionEvent.ACTION_POINTER_3_DOWN) {
			y = (int) event.getY();
			closeApp = true;
			meybeBack = false;
		}		
		if(event.getAction() == MotionEvent.ACTION_UP) {
			if(meybeBack) {
				meybeBack = false;
				if(event.getX() + Resources.getSystem().getDisplayMetrics().widthPixels / 5 < x) {
					super.onBackPressed();
				}
			}
			if(closeApp) {
				closeApp = false;
				if(event.getY() - Resources.getSystem().getDisplayMetrics().heightPixels / 3 > y) {
					  moveTaskToBack(true);
				}
			}
			
		}
		return true;
	}
	
	//for collapsing status bar///////////////////////////////////////////////////////////////////////
	public void onWindowFocusChanged(boolean hasFocus) {

	    currentFocus = hasFocus;

	    if (!hasFocus) {
	        collapseNow();
	    }
	}
	
	public void collapseNow() {

	    if (collapseNotificationHandler == null) {
	        collapseNotificationHandler = new Handler();
	    }

	    if (!currentFocus && !isPaused) {

	        collapseNotificationHandler.postDelayed(new Runnable() {

	            public void run() {
              

	                Object statusBarService = getSystemService("statusbar");
	                Class<?> statusBarManager = null;
	                
	                try {
	                    statusBarManager = Class.forName("android.app.StatusBarManager");
	                } catch (ClassNotFoundException e) {
	                    e.printStackTrace();
	                }

	                Method collapseStatusBar = null;

	                try {

	                    if (Build.VERSION.SDK_INT > 16) {
	                        collapseStatusBar = statusBarManager.getMethod("collapsePanels");
	                    } else {
	                        collapseStatusBar = statusBarManager.getMethod("collapse");
	                    }
	                } catch (NoSuchMethodException e) {
	                    e.printStackTrace();
	                }

	                collapseStatusBar.setAccessible(true);
	                
                    try {
						collapseStatusBar.invoke(statusBarService);
					}catch(IllegalAccessException e) {
						e.printStackTrace();
					}catch(IllegalArgumentException e) {
						e.printStackTrace();
					}catch(InvocationTargetException e) {
						e.printStackTrace();
					}
	                
                    if (!currentFocus && !isPaused) {
	                    collapseNotificationHandler.postDelayed(this, 200L);
	                    if (!currentFocus && !isPaused) {
		                    collapseNotificationHandler.postDelayed(this, 500L);
		                    if (!currentFocus && !isPaused) {
			                    collapseNotificationHandler.postDelayed(this, 1000L);
			                }
		                }
	                }
	            }
	        }, 1L);
	    }   
	}
	protected void onPause() {
		super.onPause();
		if(tts != null) {
			tts.stop();
			tts.shutdown();
		} 
	    isPaused = true;
	}
	
	protected void onResume() {
	    super.onResume();
	    isPaused = false;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
}
