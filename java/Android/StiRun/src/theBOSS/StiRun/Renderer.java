package theBOSS.StiRun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
	int time = -1;
	int fps = 0;
	static int speed;
	long last, lefted;
	
	long animChanged = 0;
	int animDisplayed = 0;
	
	List<Bitmap> playerRunning = new ArrayList<Bitmap>();
	List<Bitmap> playerJumpRolling = new ArrayList<Bitmap>();
	
	private List<MyRect> obstacles = new ArrayList<MyRect>();
	
	Bitmap box, topBox;
	
	private Random rand = new Random();
	
	private Rect pRect, bRect, tbRect;
	
	int animCounter;

	public boolean fastRollOver = false, gameOver = false;	
	
	private int type = -2;
		
	public Renderer(Context context) {
		super(context);
		font = Typeface.createFromAsset(context.getAssets(), "font.ttf");
		isRunning = true;
		ourHolder = getHolder();
		ourThread = new Thread(this);
		ourThread.start();		
		init();
	}

	private void init() {
		playerRunning.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_run));
		playerRunning.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_run2));
		playerRunning.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_run3));
		playerRunning.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_run4));
		
		playerJumpRolling.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_jump));
		playerJumpRolling.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_jump2));
		playerJumpRolling.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_jump3));
		playerJumpRolling.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_jump4));
		playerJumpRolling.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_jump5));
		playerJumpRolling.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_jump6));
		playerJumpRolling.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_jump7));
		playerJumpRolling.add(BitmapFactory.decodeResource(getResources(), R.drawable.player_jump8));
		
		box = BitmapFactory.decodeResource(getResources(), R.drawable.box);
		topBox = BitmapFactory.decodeResource(getResources(), R.drawable.topbox);
		animChanged = System.currentTimeMillis();
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
		int frames = 0;
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
				frames++;
			}catch(IllegalArgumentException e) {}
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				fps = frames;
				frames = 0;
			}
		}
	}

	private void update() {
		if(System.currentTimeMillis() - animChanged > 90 && animDisplayed == 0) {
			animChanged = System.currentTimeMillis();
			animCounter++;
			if(animCounter >= playerRunning.size()) {
				animCounter = 0;
			}	
		}
		if(System.currentTimeMillis() - animChanged > 130 && animDisplayed == 1 && (animCounter < 5 || animCounter == 7)) {
			animChanged = System.currentTimeMillis();
			animCounter++;
			if(animCounter >= playerJumpRolling.size()) {
				animCounter = 0;
				animDisplayed = 0;
				fastRollOver = false;
			}
		}
		if(System.currentTimeMillis() - animChanged > 200 && animDisplayed == 1 && animCounter >= 5 && animCounter < 8) {
			animChanged = System.currentTimeMillis();
			animCounter++;
		}		
		if(animDisplayed == 1) {
			if(animCounter < 2) {
				pRect.top -= height / 80;
				pRect.bottom -= height / 80;
			}
		}
		if(pRect != null) {
			if((animDisplayed == 1 && animCounter >= 4 || animDisplayed == 0 || fastRollOver ) && pRect.bottom < height / 3 * 2) {
				pRect.top += height / 80;
				pRect.bottom += height / 80;			
			}
		}
		
		for(int i = 0; i < obstacles.size(); i++) {
			obstacles.get(i).update();
		}
		for(int i = 0; i < obstacles.size(); i++) {
			if(obstacles.get(i).remove) obstacles.remove(i);
		}
		
		if(type == -1) {
			type = rand.nextInt(4);
			if(type == 0) {
				Rect r = bRect;
				r.left += width / 2 * 3;
				r.right += width / 2 * 3;
				obstacles.add(new MyRect(r, 0));
			}else if(type == 1) {
				Rect r = tbRect;
				r.left += width / 2 * 3;
				r.right += width / 2 * 3;
				obstacles.add(new MyRect(r, 1));				
			}else if(type == 2) {
				Rect r = bRect;
				r.left += width / 2 * 3;
				r.right += width / 2 * 3;
				obstacles.add(new MyRect(r, 0));
				r = tbRect;
				r.left += width / 2 * 3 + height / 8 * 4;
				r.right += width / 2 * 3 + height / 8 * 4;
				obstacles.add(new MyRect(r, 1));				
			}else {
				Rect r = bRect;
				r.left += width / 2 * 3;
				r.right += width / 2 * 3;
				obstacles.add(new MyRect(r, 0));
				r = tbRect;
				r.left += width / 2 * 3 + height / 8 * 7;
				r.right += width / 2 * 3 + height / 8 * 7;
				obstacles.add(new MyRect(r, 1));	
			}
		}
		
		if(type != -2 && obstacles.size() == 0) {
			type = -1;
		}

		collision();
	}
	
	private void collision() {
		for(int i = 0; i < obstacles.size(); i++) {
			if(obstacles.get(i).r.left < height / 6 && obstacles.get(i).type == 0) {
				if(pRect.bottom > height / 2 + height / 24 && obstacles.get(i).r.right > height / 6) {
					gameOver = true;
				}
				
			}
			if(obstacles.get(i).r.left < height / 12 * 3 && obstacles.get(i).type == 1) {
				if(animDisplayed == 0 && obstacles.get(i).r.left > height / 9 || animDisplayed == 1 && pRect.top < height / 12 * 3) {
					gameOver = true;
				}
			}			
		}
	}
	
	private void render() {
		if(!ourHolder.getSurface().isValid()) return;
		canvas = ourHolder.lockCanvas();
		if(width == 0 || height == 0) {
			width = canvas.getWidth();
			height = canvas.getHeight();
			pRect = new Rect(0, height / 3, height / 3, height / 3 * 2);
			bRect = new Rect(0, height / 6 * 3, height / 6, height / 6 * 4);
			tbRect = new Rect(0, 0, height / 12, height / 5 * 2);
			speed = height / 50;
			type = -1;
		}		
		Paint p = new Paint();
		p.setARGB(255, 255, 0, 0);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(font);
		p.setTextSize(width / 20);
		canvas.drawARGB(255, 0, 191, 255);
		Paint p2 = new Paint();
		p2.setColor(Color.BLACK);
		canvas.drawRect(0, height / 3 * 2, width, height, p2);
		if(animDisplayed == 0) canvas.drawBitmap(playerRunning.get(animCounter), null, pRect, null);
		if(animDisplayed == 1) canvas.drawBitmap(playerJumpRolling.get(animCounter), null, pRect, null);
		
		for(int i = 0; i < obstacles.size(); i++) {
			if(obstacles.get(i).type == 0) {
				canvas.drawBitmap(box, null, obstacles.get(i).r, null);				
			}
			if(obstacles.get(i).type == 1) {
				canvas.drawBitmap(topBox, null, obstacles.get(i).r, null);				
			}
		}		
		
		canvas.drawText("fps: " + fps, width / 2, height / 10, p);
		if(gameOver)
			canvas.drawText("Game Over", width / 2, height / 10 * 2, p);
		
//		canvas.drawBitmap(player.b, null, new Rect(0, 0, 400, 400), null);
		
		ourHolder.unlockCanvasAndPost(canvas);
	}
}
