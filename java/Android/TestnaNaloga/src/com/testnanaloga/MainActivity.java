package com.testnanaloga;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener, OnTouchListener {

	Button	goToActivityOne, goToActivityTwo;
	
	private int x = -1, y = -1;
	private boolean meybeBack = false, closeApp = false;
	
	//for collapsing status bar
	boolean currentFocus;
	boolean isPaused;
	Handler collapseNotificationHandler;
	///////////////////////////
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RelativeLayout r = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
		setContentView(r);
		r.setOnTouchListener(this);	
		
		goToActivityOne = (Button) findViewById(R.id.bActivity1);
		goToActivityTwo = (Button) findViewById(R.id.bActivity2);
		
		goToActivityOne.setOnClickListener(this);
		goToActivityTwo.setOnClickListener(this);
		
		
		
	}
	
	public void onClick(View v) {
		if(v.getId() == R.id.bActivity1) {
			Intent i = new Intent(MainActivity.this, ActivityOne.class);
			startActivity(i);
		}else if(v.getId() == R.id.bActivity2) {
			Intent i = new Intent(MainActivity.this, ActivityTwo.class);
			startActivity(i);
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
	        }, 100L);
	    }   
	}
	
	protected void onPause() {
	    super.onPause();   
	    isPaused = true;
	}

	protected void onResume() {
	    super.onResume();
	    isPaused = false;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////
}
