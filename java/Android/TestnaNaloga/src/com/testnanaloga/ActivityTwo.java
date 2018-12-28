package com.testnanaloga;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ActivityTwo extends Activity implements OnTouchListener, OnItemSelectedListener, OnSeekBarChangeListener{

	private int x = -1, y = -1;
	private int height, width;
	private boolean meybeBack = false, closeApp = false, justOneFinger = true, onTheEdge = true;
	Vibrator vibrate;
	
	TextView tv;
	SeekBar sb;
	Spinner spinner;
	
	ImageView image;

	String[] lik = {"Kvadrat", "Trikotnik", "Krog"};

	private int stroke = 40;
	int ww = 0;
	int ss = 100;
	
	//for collapsing status bar
	boolean currentFocus;
	boolean isPaused;
	Handler collapseNotificationHandler;
	///////////////////////////
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		RelativeLayout r = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_two, null);
		setContentView(r);
		r.setOnTouchListener(this);		
		vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		tv = (TextView) findViewById(R.id.tvProcenti);
		sb = (SeekBar) findViewById(R.id.sb2);
		sb.setMax(100);
		sb.setProgress(100);
		sb.setOnSeekBarChangeListener(this);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityTwo.this, android.R.layout.simple_spinner_item, lik);
		spinner = (Spinner) findViewById(R.id.spinner2);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		image = (ImageView) findViewById(R.id.iv);
		height = Resources.getSystem().getDisplayMetrics().heightPixels;
		width = Resources.getSystem().getDisplayMetrics().widthPixels;
		image.setMinimumWidth(height);
		image.setMaxWidth(height);
		image.setMinimumHeight(height);
		image.setMaxHeight(height);
		draw(0, 100);
	}
	
	private void draw(int which, float size) {
		Paint p = new Paint();
		p.setColor(Color.BLUE);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(stroke);
		Bitmap bitmap = Bitmap.createBitmap(height, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		if(size >= 3) {
			if(which == 0) {
				int s = (int) (height * (size / 100.0));
				int offset = (height - s) / 2;
				canvas.drawRect(new Rect(0 + offset + stroke / 2, 0 + offset + stroke / 2, height - offset -1 - stroke / 2, height - offset -1 - stroke / 2) ,p);
			}else if(which == 1) {
				int s = (int) (height * (size / 100.0));
				int offset = (height - s) / 2;
				canvas.drawLine(0 + offset, 0 + offset + stroke / 2, height - offset -1,  0 + offset + stroke / 2, p);
				canvas.drawLine(0 + offset + stroke / 2, 0 + offset, 0 + offset + stroke / 2,  height - offset -1, p);
				canvas.drawLine(height - offset -2 - stroke / 3, 4 + offset + stroke / 2, 4 + offset + stroke / 2, height - offset -2 - stroke / 3, p);
			}else if(which == 2) {
				canvas.drawCircle(height / 2, height / 2, (float) ((size / 100.0) * (height / 2)) - stroke / 2, p);
			}
		}
		image.setImageBitmap(bitmap);
	}

	public void onBackPressed() {return;}
	
	

	@SuppressWarnings("deprecation")
	public boolean onTouch(View v, MotionEvent event) {
		int xPos = (int) event.getX();
		int yPos = (int) event.getY();
		onTheEdge = isItOn(xPos, yPos);
		
		if(event.getAction() == MotionEvent.ACTION_POINTER_2_DOWN) {
			justOneFinger = false;
			x = (int) event.getX();
			meybeBack = true;
			closeApp = false;
		}
		if(event.getAction() == MotionEvent.ACTION_POINTER_2_UP) {
			justOneFinger = true;
			onTheEdge = true;
		}
		if(event.getAction() == MotionEvent.ACTION_POINTER_3_DOWN) {
			y = (int) event.getY();
			closeApp = true;
			meybeBack = false;
		}	
		if(event.getAction() == MotionEvent.ACTION_UP) {
			if(meybeBack) {
				meybeBack = false;
				if(event.getX() + width / 5 < x) {
					super.onBackPressed();
				}
			}
			if(closeApp) {
				closeApp = false;
				if(event.getY() - height / 3 > y) {
					  moveTaskToBack(true);
				}
			}
			justOneFinger = true;
			onTheEdge = false;
		}
		
		if(justOneFinger && onTheEdge) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				vibrate.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));
			}else{
				vibrate.vibrate(3000);
			}
		}
		if(!justOneFinger || !onTheEdge) {
			vibrate.cancel();
		}
		return true;
	}

	private boolean isItOn(int xx, int yy) {
		if(ss < 3) return false;
		int centerX = width - (height / 2);
		int centerY = (height / 2);
		//it should be stroke / 2 but i wanted to make a hitbox bigger			
		if(ww == 0) {
			if((xx > centerX - (ss / 100.0) * (height / 2) - (stroke * 2) && xx < centerX - (ss / 100.0) * (height / 2) + (stroke * 2) && 
			    yy > centerY - (ss / 100.0) * (height / 2) - (stroke * 2) && yy < centerY + (ss / 100.0) * (height / 2) + (stroke * 2)) ||
			   (xx > centerX + (ss / 100.0) * (height / 2) - (stroke * 2) && xx < centerX + (ss / 100.0) * (height / 2) + (stroke * 2) && 
				yy > centerY - (ss / 100.0) * (height / 2) - (stroke * 2) && yy < centerY + (ss / 100.0) * (height / 2) + (stroke * 2)) ||
			   (yy > centerY - (ss / 100.0) * (height / 2) - (stroke * 2) && yy < centerY - (ss / 100.0) * (height / 2) + (stroke * 2) && 
				xx > centerX - (ss / 100.0) * (height / 2) - (stroke * 2) && xx < centerX + (ss / 100.0) * (height / 2) + (stroke * 2)) ||
			   (yy > centerY + (ss / 100.0) * (height / 2) - (stroke * 2) && yy < centerY + (ss / 100.0) * (height / 2) + (stroke * 2) && 
				xx > centerX - (ss / 100.0) * (height / 2) - (stroke * 2) && xx < centerX + (ss / 100.0) * (height / 2) + (stroke * 2))) {
				return true;
			}
		}else if(ww == 1) {
			int x1 = (int) (centerX + (ss / 100.0) * (height / 2));
			int x2 = (int) (centerX - (ss / 100.0) * (height / 2));
			int y1 = (int) (centerY - (ss / 100.0) * (height / 2));
			int y2 = (int) (centerY + (ss / 100.0) * (height / 2));
			if((xx > centerX - (ss / 100.0) * (height / 2) - (stroke * 2) && xx < centerX - (ss / 100.0) * (height / 2) + (stroke * 2) && 
				yy > centerY - (ss / 100.0) * (height / 2) - (stroke * 2) && yy < centerY + (ss / 100.0) * (height / 2) + (stroke * 2)) ||
			   (yy > centerY - (ss / 100.0) * (height / 2) - (stroke * 2) && yy < centerY - (ss / 100.0) * (height / 2) + (stroke * 2) && 
				xx > centerX - (ss / 100.0) * (height / 2) - (stroke * 2) && xx < centerX + (ss / 100.0) * (height / 2) + (stroke * 2)) ||
			    onTheLine(x1, y1, x2, y2, xx, yy)) {
				return true;
			}
			
		}else if(ww == 2) {
			int r = (int) (((ss / 100.0) * height) / 2);
			int dist = (int) Math.sqrt((xx - centerX) * (xx - centerX) + (yy - centerY) * (yy - centerY));
			
			if(dist > r - (stroke * 2) && dist < r + (stroke * 2)) {
				return true;
			}
			
		}		
		return false;
	}
	
	private boolean onTheLine(int x1, int y1, int x2, int y2, int xx, int yy) {
		int k = (y2 - y1) / (x2 - x1);
		int n = y1 - k * x1;
		int n2 = yy - k * xx;
		
		if(n2 > n - (stroke * 2) && n2 < n + (stroke * 2)) { 
			if(xx < x1 && xx > x2 && yy > y1 && yy < y2) {
				return true;
			}
		}		
		
		return false;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		ww = position;
		draw(ww, ss);
	}

	public void onNothingSelected(AdapterView<?> parent) {}

	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		tv.setText(progress + " %");
		ss = progress;
		draw(ww, ss);
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		
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
	        }, 80L);
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
