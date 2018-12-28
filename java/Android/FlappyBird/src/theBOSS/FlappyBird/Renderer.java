package theBOSS.FlappyBird;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Renderer extends SurfaceView implements Runnable {

	Typeface font;

	SurfaceHolder ourHolder;
	Thread ourThread = null;
	boolean isRunning = false;
	static Canvas canvas;
	static float width = 0;
	static float height = 0;
	int score = 0, bestScore = 0;
	static boolean gameOver = false;
	Random rand = new Random();
	float x = -1f, y = -1f;
	boolean tapped = false, jumping = false;
	float jumpHeight = 0;
	float fallHeight = 0;
	float fallingBuffer = 150.0f;
	float jumpingBuffer = 80.0f;
	float x1, x2, x3;
	float y1, y2, y3;

	private boolean begun = false, firstTap = false, passed = false;

	public Renderer(Context context) {
		super(context);
		font = Typeface.createFromAsset(context.getAssets(), "font.TTF");
		isRunning = true;
		ourHolder = getHolder();
		ourThread = new Thread(this);
		ourThread.start();
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
		firstTap = false;
		ourThread = null;
	}

	public void resume() {
		isRunning = true;
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
			render();

			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
			}
		}
	}

	private void update() {
		if(!gameOver) {
			if(x == -1 && y == -1 && width != 0 && height != 0) {
				restart();
			}
			if(begun && tapped) {
				firstTap = true;
			}

			if(tapped) {
				tapped = false;
				jumping = true;
				jumpHeight = 0;
				jumpingBuffer = 80.0f;
			}
			if(!jumping && firstTap) {
				y += height / fallingBuffer;
				fallHeight += height / fallingBuffer;
				if(fallingBuffer > 50) fallingBuffer -= 3.0;
			}
			if(jumping) {
				y -= height / jumpingBuffer;
				jumpHeight += height / jumpingBuffer;
				jumpingBuffer += 40.0f;
				if(jumpHeight >= height / 20) {
					jumping = false;
					jumpHeight = 0;
					fallHeight = 0;
					fallingBuffer = 150.0f;
				}
			}
			if(firstTap) {
				x1 -= (int) (width / 150);
				x2 -= (int) (width / 150);
				x3 -= (int) (width / 150);
				if(x1 <= -width / 10) {
					x1 += width / 4 * 9;
					y1 = rand.nextInt((int) (height - height / 4)) + height / 8;
					passed = false;
				}
				if(x2 <= -width / 10) {
					x2 += width / 4 * 9;
					y2 = rand.nextInt((int) (height - height / 4)) + height / 8;
					passed = false;
				}
				if(x3 <= -width / 10) {
					x3 += width / 4 * 9;
					y3 = rand.nextInt((int) (height - height / 4)) + height / 8;
					passed = false;
				}
				if(!passed && (x1 < x || x2 < x || x3 < x)) {
					score++;
					passed = true;
				}
				gameOver = lookForCollision();
				if(gameOver) {
					if(score > bestScore) {
						bestScore = score;
						FlappyBird.save(bestScore);
					}
				}
			}
		}
	}

	private boolean lookForCollision() {
		if(x + width / 18 > x1 - width / 12 && x - width / 18 < x1 + width / 12 && (y - height / 36 < y1 - height / 8 || y + height / 36 > y1 + height / 8)) {
			return true;
		}
		if(x + width / 18 > x2 - width / 12 && x - width / 18 < x2 + width / 12 && (y - height / 36 < y2 - height / 8 || y + height / 36 > y2 + height / 8)) {
			return true;
		}
		if(x + width / 18 > x3 - width / 12 && x - width / 18 < x3 + width / 12 && (y - height / 36 < y3 - height / 8 || y + height / 36 > y3 + height / 8)) {
			return true;
		}

		return false;
	}

	private void render() {
		if(!ourHolder.getSurface().isValid()) return;
		canvas = ourHolder.lockCanvas();
		width = canvas.getWidth();
		height = canvas.getHeight();
		if(x == -1) {
			ourHolder.unlockCanvasAndPost(canvas);
			return;
		}
		canvas.drawRGB(127, 255, 0);
		if(!gameOver) {
			Paint p = new Paint();
			if(x != -1 && y != -1) {
				p.setARGB(255, 0, 0, 255);
				canvas.drawRect(x - width / 18, y - height / 36, x + width / 18, y + height / 36, p);
				p.setARGB(255, 0, 128, 0);
				canvas.drawRect(x1 - width / 12, 0, x1 + width / 12, y1 - height / 8, p);
				canvas.drawRect(x1 - width / 12, y1 + height / 8, x1 + width / 12, height, p);
				canvas.drawRect(x2 - width / 12, 0, x2 + width / 12, y2 - height / 8, p);
				canvas.drawRect(x2 - width / 12, y2 + height / 8, x2 + width / 12, height, p);
				canvas.drawRect(x3 - width / 12, 0, x3 + width / 12, y3 - height / 8, p);
				canvas.drawRect(x3 - width / 12, y3 + height / 8, x3 + width / 12, height, p);
			}

			p.setARGB(255, 0, 0, 0);
			p.setTextAlign(Align.CENTER);
			p.setTextSize(width / 5);
			p.setTypeface(font);
			canvas.drawText("" + score, canvas.getWidth() / 2, height / 5, p);
		}else {
			Paint p = new Paint();
			p.setARGB(255, 0, 128, 0);
			p.setTextAlign(Align.CENTER);
			p.setTextSize(width / 8);
			p.setTypeface(font);
			canvas.drawText("GAME OVER", canvas.getWidth() / 2, height / 4, p);
			canvas.drawText("Score: " + score, canvas.getWidth() / 2, height / 4 * 2, p);
			canvas.drawText("Best score: " + bestScore, canvas.getWidth() / 2, height / 4 * 3, p);
		}
		ourHolder.unlockCanvasAndPost(canvas);
	}

	public void restart() {
		x = width / 5 * 1;
		y = height / 2;
		x1 = width;
		x2 = x1 + width / 4 * 3;
		x3 = x2 + width / 4 * 3;
		y1 = rand.nextInt((int) (height - height / 4)) + height / 8;
		y2 = rand.nextInt((int) (height - height / 4)) + height / 8;
		y3 = rand.nextInt((int) (height - height / 4)) + height / 8;
		begun = true;
		firstTap = false;
		score = 0;
		gameOver = false;
	}

}
