package com.thenewboston.travis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Accelerate extends Activity implements SensorEventListener {

	float x, y, sensorX, sensorY;
	Bitmap ball;
	SensorManager sm;
	MyBringBackSurface surface;

	public class MyBringBackSurface extends SurfaceView implements Runnable {

		SurfaceHolder ourHolder;
		Thread ourThread = null;
		boolean isRunning = false;

		public MyBringBackSurface(Context context) {
			super(context);
			isRunning = true;
			ourHolder = getHolder();
			ourThread = new Thread(this);
			ourThread.start();
		}

		public void pause() {
			isRunning = false;
			while (true) {
				try {
					ourThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			}
			ourThread = null;
		}

		public void resume() {
			isRunning = true;
			ourThread = new Thread(this);
			ourThread.start();
		}

		public void run() {
			while (isRunning) {
				if (!ourHolder.getSurface().isValid()) continue;
				Canvas canvas = ourHolder.lockCanvas();
				canvas.drawRGB(2, 2, 150);
				float centerX = canvas.getWidth() / 2 - ball.getWidth() / 2;
				float centerY = canvas.getHeight() / 2 - ball.getHeight() / 2;
				canvas.drawBitmap(ball, centerX + sensorX * 20, centerY + sensorY * 20, null);
				ourHolder.unlockCanvasAndPost(canvas);

			}
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// senzor za nagib
		sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sm.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			Sensor s = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
		}

		ball = BitmapFactory.decodeResource(getResources(), R.drawable.gear);
		x = y = sensorX = sensorY = 0;
		surface = new MyBringBackSurface(this);
		surface.resume();
		setContentView(surface);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	public void onSensorChanged(SensorEvent e) {
		try {
			Thread.sleep(16);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		sensorX = e.values[0];
		sensorY = e.values[1];
	}

	protected void onPause() {
		sm.unregisterListener(this);
		surface.pause();
		super.onPause();
	}

}
