//package com.thenewboston.travis;
//
//import java.util.Random;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Paint.Align;
//import android.graphics.Rect;
//import android.graphics.Typeface;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//public class Renderer extends SurfaceView implements Runnable {
//    public static final int HEIGHT = 350;
//    public static final int WIDTH = 600;
//    static Canvas canvas;
//    public static boolean paused = false;
//    public static long startingTime;
//    public static int state = 0;
//    Bitmap bbg;
//    public float bestScore = 0.0f;
//    Bitmap blue_ball;
//    Typeface font;
//    int fps = 0;
//    private boolean gameOver = false;
//    Bitmap green_ball;
//    private Bitmap image = Bitmap.createBitmap(WIDTH, HEIGHT, Config.ARGB_8888);
//    boolean isRunning = false;
//    private boolean menuGameOver = false;
//    Bitmap orange_ball;
//    SurfaceHolder ourHolder;
//    Thread ourThread = null;
//    private int[] pixels = new int[210000];
//    Bitmap purple_ball;
//    private Random rand = new Random();
//    public float score = 0.0f;
//    Screen screen;
//    public int screenHeight = -1;
//    private Rect screenRect;
//    public int screenWidth = -1;
//    private boolean spawnNewMob = true;
//    int time = 0;
//    Bitmap yellow_ball;

    ////////////////////not the right file i changed it sorry it doesnt work;//////////////////////
    
//    public Renderer(Context context) {
//        super(context);
//        init(context);
//        this.isRunning = true;
//        this.ourHolder = getHolder();
//        this.ourThread = new Thread(this);
//        this.ourThread.start();
//    }
//
//    private void init(Context context) {
//        this.font = Typeface.createFromAsset(context.getAssets(), "font.ttf");
//        this.screen = new Screen(WIDTH, HEIGHT);;
//    }
//
//    public void pause() {
//        this.isRunning = false;
//        try {
//            this.ourThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        this.ourThread = null;
//    }
//
//    public void resume() {
//        this.isRunning = true;
//        this.ourThread = new Thread(this);
//        this.ourThread.start();
//    }
//
//    public void run() {
//        long lastTime = System.nanoTime();
//        long timer = System.currentTimeMillis();
//        double delta = 0.0d;
//        int frames = 0;
//        requestFocus();
//        while (this.isRunning) {
//            long now = System.nanoTime();
//            delta += ((double) (now - lastTime)) / 1.6666666666666666E7d;
//            lastTime = now;
//            while (delta >= 1.0d) {
//                update();
//                delta -= 1.0d;
//            }
//            try {
//                render();
//                frames++;
//            } catch (IllegalArgumentException e) {
//            }
//            if (System.currentTimeMillis() - timer > 1000) {
//                timer += 1000;
//                this.fps = frames;
//                frames = 0;
//            }
//        }
//    }
//
//    private void update() {
//       
//    }
//    
//    private void render() {
//        if (this.ourHolder.getSurface().isValid()) {
//            canvas = this.ourHolder.lockCanvas();
//            if (canvas != null) {
//                Paint p = new Paint();
//                p.setARGB(140, 127, 146, 255);
//                p.setTextAlign(Align.CENTER);
//                p.setTypeface(this.font);
//                p.setTextSize((float) (this.screenWidth / 20));
//                if (this.screenWidth == -1 || this.screenHeight == -1) {
//                    this.screenWidth = canvas.getWidth();
//                    this.screenHeight = canvas.getHeight();
//                    this.screenRect = new Rect(0, 0, this.screenWidth, this.screenHeight);
//                }
//                this.screen.clear();
//               
//                for (int i = 0; i < this.pixels.length; i++) {
//                    this.pixels[i] = this.screen.pixels[i];
//                }
//                this.image.setPixels(this.pixels, 0, WIDTH, 0, 0, WIDTH, HEIGHT);
//                canvas.drawBitmap(this.image, null, this.screenRect, null);
//                
//                this.ourHolder.unlockCanvasAndPost(canvas);
//            }
//        }
//    }
//}
