package theBOSS.PongUDPClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
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
	public static int screenWidth = -1, screenHeight = -1;
	public static final int WIDTH = 350;
	public static final int HEIGHT = 600;
	
	int fps = 0;
	private Rect screenRect;

	private Bitmap image = Bitmap.createBitmap(WIDTH, HEIGHT, Config.ARGB_8888);
	private int[] pixels = new int[WIDTH * HEIGHT];

	Bitmap bitmap;
	Sprite s;
	
	Screen screen;
	
	Paddle player;
	public static Paddle opponent;
	
	public Renderer(Context context) {
		super(context);
		init(context);
		isRunning = true;
		ourHolder = getHolder();
		ourThread = new Thread(this);
		ourThread.start();
	}

	private void init(Context context) {
		font = Typeface.createFromAsset(context.getAssets(), "font.TTF");
		screen = new Screen(WIDTH, HEIGHT);

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
		bitmap = Bitmap.createScaledBitmap(bitmap, 100, 30, false);
		s = new Sprite(bitmap);
		player = new Paddle(WIDTH / 2 - s.width / 2, HEIGHT - s.height - 5, s, screen);
		opponent = new Paddle(WIDTH / 2 - s.width / 2, 5, s, screen);
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
		player.update();
	}
	
	private void render() {
		if(!ourHolder.getSurface().isValid()) return;
		canvas = ourHolder.lockCanvas();
		Paint p = new Paint();
		p.setARGB(255, 0, 66, 99);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(font);
		p.setTextSize(screenWidth / 10);
		if(screenWidth == -1 || screenHeight == -1) {
			screenWidth = canvas.getWidth();
			screenHeight = canvas.getHeight();
			screenRect = new Rect(0, 0, screenWidth, screenHeight);
		}
		screen.clear();
		player.render();
		opponent.render();
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		
		image.setPixels(pixels, 0, WIDTH, 0, 0, WIDTH, HEIGHT);
		canvas.drawBitmap(image, null, screenRect, null);

		canvas.drawText("fps: " + fps, screenWidth / 2, screenHeight / 3, p);
		ourHolder.unlockCanvasAndPost(canvas);
	}
}
