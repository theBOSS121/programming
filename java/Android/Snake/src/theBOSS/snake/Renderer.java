package theBOSS.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
	float headX = -1000, headY = -1000, yMove, xMove;
	List<Coordinate> body = new ArrayList<Coordinate>();
	int foodX = -100, foodY = -100;
	int dir = 0;
	int counter = 0;
	int score = 0, bestScore = 0;
	static boolean gameOver = false, changed = false;
	Random rand = new Random();

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
		while (true) {
			try {
				ourThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		gameOver = true;
		ourThread = null;
	}

	public void resume() {
		isRunning = true;
		ourThread = new Thread(this);
		restart();
		ourThread.start();
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		requestFocus();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
			}
			render();

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
			}
		}
	}

	private void update() {
		if (!gameOver) {
			xMove = width / 6;
			yMove = height / 10;
			if (counter % 20 == 0) {
				if (Snake.x > width / 2 && Snake.y > height / 4 && Snake.y < height / 4 * 3 && dir != 2) {
					dir = 3;
				} else if (Snake.x < width / 2 && Snake.y > height / 4 && Snake.y < height / 4 * 3 && dir != 3) {
					dir = 2;
				} else if (Snake.y > height / 4 * 3 && dir != 0) {
					dir = 1;
				} else if (Snake.y < height / 4 && dir != 1) {
					dir = 0;
				}
				changed = true;
				for (int i = body.size() - 1; i >= 0; i--) {
					if (i != 0) {
						body.get(i).x = body.get(i - 1).x;
						body.get(i).y = body.get(i - 1).y;
					} else {
						body.get(i).x = headX;
						body.get(i).y = headY;
					}
				}
				if (dir == 0) {
					headY -= yMove;
				} else if (dir == 1) {
					headY += yMove;
				} else if (dir == 2) {
					headX -= xMove;
				} else if (dir == 3) {
					headX += xMove;
				}
			}
			if (headX != -1000 && headY != -1000) {
				if (headX + xMove > width)
					headX = 0.0f;
				if (headX < 0.0f)
					headX = width - xMove;
				if (headY + yMove > height)
					headY = 0.0f;
				if (headY < 0.0f)
					headY = height - yMove;
			}
			if (foodX == (int) headX && foodY == (int) headY) {
				foodX = (int) ((width / 6) * rand.nextInt(6));
				foodY = (int) ((height / 10) * rand.nextInt(10));
				body.add(new Coordinate(body.get(body.size() - 1).x, body.get(body.size() - 1).y));
				score++;
			}

			if (foodX == -100 && foodY == -100 && height != 0 && width != 0) {
				foodX = (int) ((width / 6) * rand.nextInt(6));
				foodY = (int) ((height / 10) * rand.nextInt(10));
			}

			if (headX == -1000 && headY == -1000 && height != 0 && width != 0) {
				headX = width / 6 * 2;
				headY = height / 10 * 4;
				body.add(new Coordinate(headX, headY + yMove));
				body.add(new Coordinate(headX, headY + yMove * 2));
			}

			for (int i = 0; i < body.size(); i++) {
				if (headX == body.get(i).x && headY == body.get(i).y) {
					gameOver = true;
					if (score > bestScore) {
						bestScore = score;
						Snake.save(bestScore);
					}
				}
			}

			counter++;
		}
	}

	private void render() {
		if (!ourHolder.getSurface().isValid())
			return;
		canvas = ourHolder.lockCanvas();
		width = canvas.getWidth();
		height = canvas.getHeight();
		canvas.drawRGB(127, 255, 0);
		if (!gameOver) {
			Paint p = new Paint();
			p.setColor(Color.argb(255, 0, 100, 0));
			canvas.drawRect(0, height / 4 - 1, width, height / 4 + 1, p);
			canvas.drawRect(0, height / 4 * 3 - 1, width, height / 4 * 3 + 1, p);
			canvas.drawRect(width / 2 - 1, height / 4, width / 2 + 1, height / 4 * 3, p);
			if (foodX != -100 && foodY != -100) {
				p.setColor(Color.RED);
				canvas.drawRect(foodX + 2, foodY + 2, foodX + canvas.getWidth() / 6 - 4,
						foodY + canvas.getHeight() / 10 - 4, p);
			}
			p.setARGB(255, 40, 100, 10);
			canvas.drawRect(headX + 2, headY + 2, headX + canvas.getWidth() / 6 - 4,
					headY + canvas.getHeight() / 10 - 4, p);
			;
			for (int i = 0; i < body.size(); i++) {
				canvas.drawRect(body.get(i).x + 2, body.get(i).y + 2, body.get(i).x + canvas.getWidth() / 6 - 4,
						body.get(i).y + canvas.getHeight() / 10 - 4, p);
			}
			Paint p1 = new Paint();
			p1.setARGB(255, 0, 180, 0);
			p1.setTextAlign(Align.CENTER);
			p1.setTextSize(width / 5);
			p1.setTypeface(font);
			canvas.drawText("" + score, canvas.getWidth() / 2, height / 5, p1);
		}
		if (gameOver) {
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
		body = new ArrayList<Coordinate>();
		headX = width / 6 * 2;
		headY = height / 10 * 4;
		dir = 0;
		body.add(new Coordinate(headX, headY + yMove));
		body.add(new Coordinate(headX, headY + yMove * 2));

		foodX = (int) ((width / 6) * rand.nextInt(6));
		foodY = (int) ((height / 10) * rand.nextInt(10));
		score = 0;
		counter = 0;
		gameOver = false;
	}

}
