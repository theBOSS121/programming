package theBOSS.dontGetStuck;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Renderer extends SurfaceView implements Runnable {

	Typeface font;

	SurfaceHolder ourHolder;
	Thread ourThread = null;
	boolean isRunning = false;
	static Canvas canvas;
	int width = 0;
	int height = 0;
	int speed, score = 0, bestScore = 0;
	int moveX = 0;
	int lineY = 0;
	int time = -1;
	boolean right = false, left = false, gameOver = false;
	long last, lefted;

	Bitmap bg, roadLine;
	Bitmap car, carLeft1, carRight1, carOrange, carCrashed, carOrangeCrashed;
	
	Rect carRect;
	
	private Random rand = new Random();
	
	private List<Cars> rects = new ArrayList<Cars>();
	private List<Integer> places = new ArrayList<Integer>();
	
	public Renderer(Context context) {
		super(context);
		font = Typeface.createFromAsset(context.getAssets(), "font.ttf");
		isRunning = true;
		ourHolder = getHolder();
		ourThread = new Thread(this);
		ourThread.start();

		bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		roadLine = BitmapFactory.decodeResource(getResources(), R.drawable.roadline);
		car = BitmapFactory.decodeResource(getResources(), R.drawable.car);
		carCrashed = BitmapFactory.decodeResource(getResources(), R.drawable.carcrashed);
		carLeft1 = BitmapFactory.decodeResource(getResources(), R.drawable.turnleft1);
		carRight1 = BitmapFactory.decodeResource(getResources(), R.drawable.turnright1);
		carOrange = BitmapFactory.decodeResource(getResources(), R.drawable.carorange);
		carOrangeCrashed = BitmapFactory.decodeResource(getResources(), R.drawable.carorangecrashed);
		
	}

	public void pause() {
		isRunning = false;
		while(true) {
			try {
				ourThread.join();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		lefted = System.currentTimeMillis();
		ourThread = null;
	}

	public void resume() {
		isRunning = true;
		last += System.currentTimeMillis() - lefted;
		ourThread = new Thread(this);
		ourThread.start();
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		requestFocus();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				delta--;
			}
			try {
				render();
			}catch(IllegalArgumentException e) {}
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
			}
		}
	}

	private void update() {
		if(!gameOver) {
			check();
			if(left) {
				moveX += speed;
				carRect.offset(-speed, 0);			
			}
			if(right) {
				moveX += speed;
				carRect.offset(speed, 0);			
			}
			
			if(moveX >= width / 5) {
				moveX -= width / 5;
				left = false;
				right = false;
				check();
			}
			
			if(time == -1) {
				last = System.currentTimeMillis();
				time = rand.nextInt(1000) + 1500;
			}
			
			if(System.currentTimeMillis() - time >= last) {
				int count = 1;
				if(score < 30) {
					count = rand.nextInt(3) + 1;
				}else if(score < 60) {
					count = rand.nextInt(4) + 1;
				}else if(score < 90) {
					count = rand.nextInt(3) + 2;
				}else if(score < 120) {
					count = rand.nextInt(2) + 3;
				}else {
					count = 4;					
				}
				
				places = new ArrayList<Integer>();
				for(int i = 0; i < count; i++) {
					boolean valid = true; 
					int place;
					do {
						valid = true;
						place = rand.nextInt(5);
						for(int i1 = 0; i1 < places.size(); i1++) {
							if(places.get(i1).intValue() == place) {
								valid = false;
							}
						}
						places.add(Integer.valueOf(place));
						
					}while(!valid);
					place *= 2;
					rects.add(new Cars((int) (width / 10 + width / 10 * place - width * 0.075), (int) (-height / 9), (int) (width * 0.075 * 2), (int) (height / 9), height, height / (rand.nextInt(80) + 100)));
				}
				time = -1;
			}
			
			for(int i = 0; i < rects.size(); i++) {
				rects.get(i).rect.top += rects.get(i).ySpeed;
				rects.get(i).rect.bottom += rects.get(i).ySpeed;
				if(rects.get(i).rect.top > height) {
					rects.remove(i);
					score++;
				}
			}
			
			collision();	
			
			lineY += height / 400;
			if(lineY >= height / 4) lineY -= height / 4;		
			
		}else {
			if(!DontGetStuck.left && !DontGetStuck.right) {
				DontGetStuck.tapListener = true;
			}
		}
		
	}

	private void collision() {
		for(int i = 0; i < rects.size(); i++) {
			if(carRect.contains(rects.get(i).rect.left, rects.get(i).rect.top) || carRect.contains(rects.get(i).rect.right, rects.get(i).rect.top) ||
			   carRect.contains(rects.get(i).rect.left, rects.get(i).rect.bottom) || carRect.contains(rects.get(i).rect.right, rects.get(i).rect.bottom)) {
				gameOver = true;
				rects.get(i).crashed = true;
				if(score > bestScore) {
					bestScore = score;
					DontGetStuck.save(bestScore);
				}
			}
		}		
	}

	private void check() {
		if(DontGetStuck.left && !left && !right && carRect.left > width / 5) {
			left = true;
		}
		if(DontGetStuck.right && !right && !left && carRect.right < width / 5 * 4) {
			right = true;
		}
	}

	private void render() {
		if(!ourHolder.getSurface().isValid()) return;
		canvas = ourHolder.lockCanvas();
		if(width == 0 || height == 0) {
			width = canvas.getWidth();
			height = canvas.getHeight();
			speed = width / 60;
			carRect = new Rect((int) (width / 2 - width * 0.075), height - height / 5, (int) (width / 2 + width * 0.075),(int) (height - height / 5 + height / 9));
		}
//		canvas.drawBitmap(bgPixels, 0, bg.getWidth(), 0, 0, bg.getWidth(), bg.getHeight(), false, null);
		canvas.drawBitmap(bg, null, new Rect(0, 0, width, height), null);
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 5; j++) {
				canvas.drawBitmap(roadLine, null, new Rect((int) (width / 5 + width / 5 * i - (width * 0.01)), lineY + j * height / 4 - height / 4, (int) (width / 5 + width / 5 * i + (width * 0.01)), (int) (height * 0.16) + lineY + j * height / 4 - height / 4), null);
			}
		}
		for(int i = 0; i < rects.size(); i++) {
			if(!rects.get(i).crashed) canvas.drawBitmap(carOrange, null, rects.get(i).rect, null);
			else canvas.drawBitmap(carOrangeCrashed, null, rects.get(i).rect, null);
		}
		Paint p = new Paint();
		p.setARGB(180, 40, 60, 90);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(font);
		p.setTextSize(width / 3);
		
		if(!gameOver) {
			if(!left && !right)	canvas.drawBitmap(car, null, carRect, null);
			else if(left) canvas.drawBitmap(carRight1, null, carRect, null);
			else if(right) canvas.drawBitmap(carLeft1, null, carRect, null);
			canvas.drawText("" + score, width / 2, height / 5, p);
		}else {
			canvas.drawBitmap(carCrashed, null, carRect, null);
			p.setTextSize(width / 5);
			canvas.drawText("Score: " + score, width / 2, height / 5, p);
			canvas.drawText("Best: " + bestScore, width / 2, height / 5 * 2, p);			
			p.setTextSize(width / 10);
			canvas.drawText("Tap to play again", width / 2, height / 20 * 19, p);						
		}
		ourHolder.unlockCanvasAndPost(canvas);
	}

	public void restart() {
		score  = 0;
		time = -1;
		moveX = 0;
		lineY = 0;
		left = right = false;
		carRect = new Rect((int) (width / 2 - width * 0.075), height - height / 5, (int) (width / 2 + width * 0.075),(int) (height - height / 5 + height / 9));
		rects = new ArrayList<Cars>();
		places = new ArrayList<Integer>();
		gameOver = false;
	}
}
