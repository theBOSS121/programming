package theBOSS.Avoid;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import theBOSS.Avoid.entity.Entity;
import theBOSS.Avoid.entity.Mob;
import theBOSS.Avoid.entity.Mob2;
import theBOSS.Avoid.entity.Mob3;
import theBOSS.Avoid.entity.Mob4;
import theBOSS.Avoid.entity.Player;
import theBOSS.Avoid.graphics.Screen;
import theBOSS.Avoid.graphics.Sprite;

public class Renderer extends SurfaceView implements Runnable {
    public static final int HEIGHT = 350;
    public static final int WIDTH = 600;
    public static Sprite bBall;
    public static Sprite bg;
    static Canvas canvas;
    public static Sprite gBall;
    public static Sprite oBall;
    public static Sprite pBall;
    public static boolean paused = false;
    public static long startingTime;
    public static int state = 0;
    public static Sprite yBall;
    Bitmap bbg;
    public float bestScore = 0.0f;
    Bitmap blue_ball;
    Typeface font;
    int fps = 0;
    private boolean gameOver = false;
    Bitmap green_ball;
    private Bitmap image = Bitmap.createBitmap(WIDTH, HEIGHT, Config.ARGB_8888);
    boolean isRunning = false;
    private boolean menuGameOver = false;
    private List<Entity> menuMobs;
    private List<Entity> mobs;
    Bitmap orange_ball;
    SurfaceHolder ourHolder;
    Thread ourThread = null;
    private int[] pixels = new int[210000];
    public Player player;
    Bitmap purple_ball;
    private Random rand = new Random();
    public float score = 0.0f;
    Screen screen;
    public int screenHeight = -1;
    private Rect screenRect;
    public int screenWidth = -1;
    private boolean spawnNewMob = true;
    int time = 0;
    Bitmap yellow_ball;

    public Renderer(Context context) {
        super(context);
        init(context);
        this.isRunning = true;
        this.ourHolder = getHolder();
        this.ourThread = new Thread(this);
        this.ourThread.start();
    }

    private void init(Context context) {
        this.font = Typeface.createFromAsset(context.getAssets(), "font.ttf");
        this.screen = new Screen(WIDTH, HEIGHT);
        this.bbg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        this.blue_ball = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.blue_ball), 28, 28, false);
        this.green_ball = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.green_ball), 28, 28, false);
        this.orange_ball = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.orange_ball), 28, 28, false);
        this.purple_ball = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.purple_ball), 28, 28, false);
        this.yellow_ball = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.yellow_ball), 28, 28, false);
        bg = new Sprite(this.bbg);
        bBall = new Sprite(this.blue_ball);
        gBall = new Sprite(this.green_ball);
        oBall = new Sprite(this.orange_ball);
        pBall = new Sprite(this.purple_ball);
        yBall = new Sprite(this.yellow_ball);
        if (Avoid.ballColor == 0) {
            this.player = new Player((double) (300 - (bBall.width / 2)), (double) (175 - (bBall.height / 2)), bBall);
        }
        if (Avoid.ballColor == 1) {
            this.player = new Player((double) (300 - (bBall.width / 2)), (double) (175 - (bBall.height / 2)), gBall);
        }
        if (Avoid.ballColor == 2) {
            this.player = new Player((double) (300 - (bBall.width / 2)), (double) (175 - (bBall.height / 2)), oBall);
        }
        if (Avoid.ballColor == 3) {
            this.player = new Player((double) (300 - (bBall.width / 2)), (double) (175 - (bBall.height / 2)), pBall);
        }
        if (Avoid.ballColor == 4) {
            this.player = new Player((double) (300 - (bBall.width / 2)), (double) (175 - (bBall.height / 2)), yBall);
        }
    }

    public void pause() {
        this.isRunning = false;
        try {
            this.ourThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.ourThread = null;
    }

    public void resume() {
        this.isRunning = true;
        this.ourThread = new Thread(this);
        this.ourThread.start();
    }

    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0.0d;
        int frames = 0;
        requestFocus();
        while (this.isRunning) {
            long now = System.nanoTime();
            delta += ((double) (now - lastTime)) / 1.6666666666666666E7d;
            lastTime = now;
            while (delta >= 1.0d) {
                update();
                delta -= 1.0d;
            }
            try {
                render();
                frames++;
            } catch (IllegalArgumentException e) {
            }
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                this.fps = frames;
                frames = 0;
            }
        }
    }

    private void update() {
        int i;
        if (!paused) {
            this.player.update();
        }
        if (state == 0 || state == 3) {
            if (this.menuMobs == null) {
                restart(1);
            }
            for (i = 0; i < this.menuMobs.size(); i++) {
                ((Entity) this.menuMobs.get(i)).update();
            }
            collision(1);
            if (this.menuGameOver) {
                restart(1);
            }
        }
        if (state == 1) {
            if (this.menuMobs != null) {
                this.menuMobs = null;
            }
            if (!paused) {
                for (i = 0; i < this.mobs.size(); i++) {
                    ((Entity) this.mobs.get(i)).update();
                }
                if (this.time % 960 == 0) {
                    this.spawnNewMob = true;
                }
                if (this.spawnNewMob) {
                    spawn();
                    this.spawnNewMob = false;
                }
                collision(0);
                if (this.gameOver) {
                    if (this.score > this.bestScore) {
                        this.bestScore = this.score;
                    }
                    Avoid.save(this.bestScore, this.score);
                    state = 2;
                }
                if (!this.gameOver) {
                    this.score = (float) (((double) (System.currentTimeMillis() - startingTime)) / 1000.0d);
                }
                this.time++;
            }
        }
    }

    public void restart(int which) {
        if (which == 0) {
            this.mobs = new ArrayList();
            this.gameOver = false;
            this.time = 0;
            state = 1;
            this.spawnNewMob = true;
            this.score = 0.0f;
            startingTime = System.currentTimeMillis();
        } else if (which == 1) {
            this.menuMobs = new ArrayList();
            this.menuGameOver = false;
            spawnMenuMobs();
        }
    }

    private void collision(int which) {
        int i;
        Entity e;
        if (which == 0) {
            for (i = 0; i < this.mobs.size(); i++) {
                e = (Entity) this.mobs.get(i);
                if (this.player.x + this.player.width > e.x && this.player.x < e.x + e.width && this.player.y + this.player.height > e.y && this.player.y < e.y + e.height && e.hitable && this.player.hitable) {
                    this.gameOver = true;
                }
            }
        } else if (which == 1) {
            for (i = 0; i < this.menuMobs.size(); i++) {
                e = (Entity) this.menuMobs.get(i);
                if (this.player.x + this.player.width > e.x && this.player.x < e.x + e.width && this.player.y + this.player.height > e.y && this.player.y < e.y + e.height && e.hitable && this.player.hitable) {
                    this.menuGameOver = true;
                }
            }
        }
    }

    private void spawnMenuMobs() {
        for (int i = 0; i < 4; i++) {
            int which = i;
            int xPosible = 600 - oBall.width;
            int yPosible = 350 - oBall.height;
            if (which == 0) {
                if (this.player.sprite != oBall) {
                    this.menuMobs.add(new Mob((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), oBall));
                } else {
                    this.menuMobs.add(new Mob((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), bBall));
                }
            } else if (which == 1) {
                if (this.player.sprite != gBall) {
                    this.menuMobs.add(new Mob2((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), gBall));
                } else {
                    this.menuMobs.add(new Mob2((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), bBall));
                }
            } else if (which == 2) {
                if (this.player.sprite != pBall) {
                    this.menuMobs.add(new Mob3((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), pBall));
                } else {
                    this.menuMobs.add(new Mob3((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), bBall));
                }
            } else if (which == 3) {
                if (this.player.sprite != yBall) {
                    this.menuMobs.add(new Mob4((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), yBall, this.player));
                } else {
                    this.menuMobs.add(new Mob4((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), bBall, this.player));
                }
            }
        }
    }

    private void spawn() {
        int which = this.rand.nextInt(4);
        int xPosible = 600 - oBall.width;
        int yPosible = 350 - oBall.height;
        if (which == 0) {
            if (this.player.sprite != oBall) {
                this.mobs.add(new Mob((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), oBall));
            } else {
                this.mobs.add(new Mob((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), bBall));
            }
        } else if (which == 1) {
            if (this.player.sprite != gBall) {
                this.mobs.add(new Mob2((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), gBall));
            } else {
                this.mobs.add(new Mob2((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), bBall));
            }
        } else if (which == 2) {
            if (this.player.sprite != pBall) {
                this.mobs.add(new Mob3((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), pBall));
            } else {
                this.mobs.add(new Mob3((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), bBall));
            }
        } else if (which != 3) {
        } else {
            if (this.player.sprite != yBall) {
                this.mobs.add(new Mob4((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), yBall, this.player));
            } else {
                this.mobs.add(new Mob4((double) this.rand.nextInt(xPosible), (double) this.rand.nextInt(yPosible), bBall, this.player));
            }
        }
    }

    private void render() {
        if (this.ourHolder.getSurface().isValid()) {
            canvas = this.ourHolder.lockCanvas();
            if (canvas != null) {
                int i;
                Paint p = new Paint();
                p.setARGB(140, 127, 146, 255);
                p.setTextAlign(Align.CENTER);
                p.setTypeface(this.font);
                p.setTextSize((float) (this.screenWidth / 20));
                if (this.screenWidth == -1 || this.screenHeight == -1) {
                    this.screenWidth = canvas.getWidth();
                    this.screenHeight = canvas.getHeight();
                    this.screenRect = new Rect(0, 0, this.screenWidth, this.screenHeight);
                }
                this.screen.clear();
                this.player.render(this.screen);
                if (state == 1) {
                    for (i = 0; i < this.mobs.size(); i++) {
                        ((Entity) this.mobs.get(i)).render(this.screen);
                    }
                } else if ((state == 0 || state == 3) && this.menuMobs != null) {
                    for (i = 0; i < this.menuMobs.size(); i++) {
                        ((Entity) this.menuMobs.get(i)).render(this.screen);
                    }
                }
                for (i = 0; i < this.pixels.length; i++) {
                    this.pixels[i] = this.screen.pixels[i];
                }
                this.image.setPixels(this.pixels, 0, WIDTH, 0, 0, WIDTH, HEIGHT);
                canvas.drawBitmap(this.image, null, this.screenRect, null);
                if (state == 0) {
                    p.setTextSize((float) (this.screenWidth / 8));
                    canvas.drawText("Play", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 7) * 2), p);
                    canvas.drawText("Options", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 7) * 4), p);
                    canvas.drawText("Exit", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 7) * 6), p);
                    p.setTextSize((float) (this.screenWidth / 20));
                    canvas.drawText("Best score:", (float) ((this.screenWidth / 6) * 5), (float) (this.screenHeight / 10), p);
                    canvas.drawText(this.bestScore + "s", (float) ((this.screenWidth / 6) * 5), (float) ((this.screenHeight / 10) * 2), p);
                    canvas.drawText("Last score:", (float) (this.screenWidth / 6), (float) (this.screenHeight / 10), p);
                    canvas.drawText(this.score + "s", (float) (this.screenWidth / 6), (float) ((this.screenHeight / 10) * 2), p);
                } else if (state == 1) {
                    if (paused) {
                        p.setTextSize((float) (this.screenWidth / 20));
                        canvas.drawText("Best score:", (float) ((this.screenWidth / 6) * 5), (float) (this.screenHeight / 10), p);
                        canvas.drawText(this.bestScore + "s", (float) ((this.screenWidth / 6) * 5), (float) ((this.screenHeight / 10) * 2), p);
                        canvas.drawText("Score:", (float) (this.screenWidth / 6), (float) (this.screenHeight / 10), p);
                        canvas.drawText(this.score + "s", (float) (this.screenWidth / 6), (float) ((this.screenHeight / 10) * 2), p);
                        canvas.drawText("Tap anywhere to unpause", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 10) * 9), p);
                        p.setTextSize((float) (this.screenWidth / 8));
                        canvas.drawText("Paused", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 10) * 5), p);
                    } else {
                        canvas.drawText("Score: " + String.format("%.01f", new Object[]{Float.valueOf(this.score)}) + "s", (float) (this.screenWidth / 2), (float) (this.screenHeight / 10), p);
                        canvas.drawText("Best: " + this.bestScore + "s", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 10) * 2), p);
                    }
                } else if (state == 2) {
                    p.setTextSize((float) (this.screenWidth / 7));
                    canvas.drawText("Game Over", (float) (this.screenWidth / 2), (float) (this.screenHeight / 2), p);
                    p.setTextSize((float) (this.screenWidth / 10));
                    canvas.drawText("restart", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 11) * 8), p);
                    canvas.drawText("menu", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 11) * 10), p);
                    p.setTextSize((float) (this.screenWidth / 20));
                    canvas.drawText("Best score:", (float) ((this.screenWidth / 6) * 5), (float) (this.screenHeight / 10), p);
                    canvas.drawText(this.bestScore + "s", (float) ((this.screenWidth / 6) * 5), (float) ((this.screenHeight / 10) * 2), p);
                    canvas.drawText("Last score:", (float) (this.screenWidth / 6), (float) (this.screenHeight / 10), p);
                    canvas.drawText(this.score + "s", (float) (this.screenWidth / 6), (float) ((this.screenHeight / 10) * 2), p);
                } else if (state == 3) {
                    p.setTextSize((float) (this.screenWidth / 8));
                    if (Avoid.music) {
                        canvas.drawText("music on", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 7) * 2), p);
                    } else {
                        canvas.drawText("music off", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 7) * 2), p);
                    }
                    canvas.drawText("back", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 7) * 6), p);
                    p.setColor(this.player.sprite.pixels[(int) ((this.player.width / 2.0d) + ((this.player.height / 2.0d) * this.player.width))]);
                    canvas.drawText("ball color", (float) (this.screenWidth / 2), (float) ((this.screenHeight / 7) * 4), p);
                }
                this.ourHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
