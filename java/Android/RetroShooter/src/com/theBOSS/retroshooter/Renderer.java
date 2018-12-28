package com.theBOSS.retroshooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class Renderer extends SurfaceView implements Runnable {

//	Typeface font;

	SurfaceHolder ourHolder;
	Thread ourThread = null;
	boolean isRunning = false;
	static Canvas canvas;
	public static int screenWidth = -1, screenHeight = -1;
	public static Sprite bg;
	public static final int WIDTH = 100;
	public static final int HEIGHT = 165;
	
	int fps = 0;
	private Rect screenRect;

	private Bitmap image = Bitmap.createBitmap(WIDTH, HEIGHT, Config.ARGB_8888);
	private int[] pixels = new int[WIDTH * HEIGHT];

	Bitmap bitmap;
	public static Sprite joystick, joystick_bg, back_clicked, back, help, help_clicked, online_clicked, online, options_clicked, options, play_clicked, play, player_sprite, saw, single_player_clicked, single_player, 
						 survive_clicked, survive, sounds, sounds_clicked, no_sounds, no_sounds_clicked, client, client_clicked, server, server_clicked;
	
	public static Screen screen;
	
	public Game game;
	
	public Renderer(Context context) {
		super(context);
		init(context);
		isRunning = true;
		ourHolder = getHolder();
		ourThread = new Thread(this);
		ourThread.start();
	}

	private void init(Context context) {
//		font = Typeface.createFromAsset(context.getAssets(), "font.TTF");
		screen = new Screen(WIDTH, HEIGHT);

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		bitmap = Bitmap.createScaledBitmap(bitmap, 100, 180, false);
		bg = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.joystick);
		bitmap = Bitmap.createScaledBitmap(bitmap, 14, 14, false);
		joystick = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.joystick_bg);
		bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, false);
		joystick_bg = new Sprite(bitmap);
		

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back_clicked);
		bitmap = Bitmap.createScaledBitmap(bitmap, 56, 22, false);
		back_clicked = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.back);
		bitmap = Bitmap.createScaledBitmap(bitmap, 56, 22, false);
		back = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.help);
		bitmap = Bitmap.createScaledBitmap(bitmap, 56, 22, false);
		help = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.help_clicked);
		bitmap = Bitmap.createScaledBitmap(bitmap, 56, 22, false);
		help_clicked = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.online_clicked);
		bitmap = Bitmap.createScaledBitmap(bitmap, 80, 22, false);
		online_clicked = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.online);
		bitmap = Bitmap.createScaledBitmap(bitmap, 80, 22, false);
		online = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.options_clicked);
		bitmap = Bitmap.createScaledBitmap(bitmap, 89, 22, false);
		options_clicked = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.options);
		bitmap = Bitmap.createScaledBitmap(bitmap, 89, 22, false);
		options = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_clicked);
		bitmap = Bitmap.createScaledBitmap(bitmap, 56, 22, false);
		play_clicked = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play);
		bitmap = Bitmap.createScaledBitmap(bitmap, 56, 22, false);
		play = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player);
		bitmap = Bitmap.createScaledBitmap(bitmap, 13, 13, false);
		player_sprite = new Sprite(bitmap);		
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.saw);
		bitmap = Bitmap.createScaledBitmap(bitmap, 16, 16, false);
		saw = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.survive);
		bitmap = Bitmap.createScaledBitmap(bitmap, 88, 22, false);
		survive = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.survive_clicked);
		bitmap = Bitmap.createScaledBitmap(bitmap, 88, 22, false);
		survive_clicked = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sounds);
		bitmap = Bitmap.createScaledBitmap(bitmap, 85, 22, false);
		sounds = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sounds_clicked);
		bitmap = Bitmap.createScaledBitmap(bitmap, 85, 22, false);
		sounds_clicked = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_sounds);
		bitmap = Bitmap.createScaledBitmap(bitmap, 85, 22, false);
		no_sounds = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_sounds_clicked);
		bitmap = Bitmap.createScaledBitmap(bitmap, 85, 22, false);
		no_sounds_clicked = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.client);
		bitmap = Bitmap.createScaledBitmap(bitmap, 72, 22, false);
		client = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.client_clicked);
		bitmap = Bitmap.createScaledBitmap(bitmap, 72, 22, false);
		client_clicked = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.server);
		bitmap = Bitmap.createScaledBitmap(bitmap, 80, 22, false);
		server = new Sprite(bitmap);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.server_clicked);
		bitmap = Bitmap.createScaledBitmap(bitmap, 80, 22, false);
		server_clicked = new Sprite(bitmap);
		
		game = new Game();
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
		game.update();
	}
	
	private void render() {
		if(!ourHolder.getSurface().isValid()) return;
		canvas = ourHolder.lockCanvas();
		if(canvas == null) {
			return;
		}
		Paint p = new Paint();
		p.setARGB(255, 255, 255, 255);
		p.setTextAlign(Align.CENTER);
//		p.setTypeface(font);
		p.setTextSize(screenWidth / 10);
		if(screenWidth == -1 || screenHeight == -1) {
			screenWidth = canvas.getWidth();
			screenHeight = canvas.getHeight();
//			make squeers not racts pixels
//			double ration = screenWidth / WIDTH;
//			screenHeight = (int) (HEIGHT * ration);
			
			screenRect = new Rect(0, 0, screenWidth, screenHeight);
		}
		
		game.render();
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = Renderer.screen.pixels[i];
		}
		
		image.setPixels(pixels, 0, WIDTH, 0, 0, WIDTH, HEIGHT);
		
		canvas.drawBitmap(image, null, screenRect, null);

		canvas.drawText("FPS: " + fps, screenWidth / 2, screenHeight / 8, p);
		if(Menu.isClient && ClientThread.opponents.size() > 0)
		canvas.drawText("" + ClientThread.opponents.get(0).life, screenWidth / 2, screenHeight / 8 * 2, p);
		canvas.drawText("" + RetroShooter.infos, screenWidth / 2, screenHeight / 8 * 7, p);
		ourHolder.unlockCanvasAndPost(canvas);
	}
}
