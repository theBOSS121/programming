package theBOSS.Avoid;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


// 			 !!! boljsi avoid class je v mapi avoid!!!
// 				this is not bad at all



public class Avoid extends Activity implements SensorEventListener, OnTouchListener {
    public static int ballColor = 0;
    public static boolean music = false;
    public static float sensorX = 0.0f;
    public static float sensorY = 0.0f;
    static SharedPreferences someData;
    FrameLayout game;
    private long pousedTime;
    Renderer renderer;
    SensorManager sm;
    MediaPlayer song;
    LinearLayout widgets;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        getWindow().addFlags(128);
        this.sm = (SensorManager) getSystemService("sensor");
        someData = getSharedPreferences("theBOSS.Avoid.prefs", 0);
        String dataReturned = someData.getString("bestScore", "0.000");
        String dataReturned2 = someData.getString("lastScore", "0.000");
        String dataReturned3 = someData.getString("ballColor", "0");
        music = someData.getBoolean("music", true);
        ballColor = Integer.parseInt(dataReturned3);
        this.game = new FrameLayout(this);
        this.renderer = new Renderer(this);
        this.renderer.setOnTouchListener(this);
        this.renderer.bestScore = Float.parseFloat(dataReturned);
        this.renderer.score = Float.parseFloat(dataReturned2);
        this.widgets = new LinearLayout(this);
        this.game.addView(this.renderer);
        this.game.addView(this.widgets);
        setContentView(this.game);
    }

    protected void onStop() {
        super.onStop();
        this.renderer.pause();
        this.sm.unregisterListener(this);
        if (music) {
            this.song.release();
        }
    }

    protected void onPause() {
        super.onPause();
        if (Renderer.state == 1 && !Renderer.paused) {
            Renderer.paused = true;
            this.pousedTime = System.currentTimeMillis();
        }
    }

    protected void onRestart() {
        super.onRestart();
        this.renderer.resume();
    }

    protected void onResume() {
        super.onResume();
        if (this.sm.getSensorList(1).size() != 0) {
            this.sm.registerListener(this, (Sensor) this.sm.getSensorList(1).get(0), 1);
        }
        if (music) {
            this.song = MediaPlayer.create(this, R.raw.avoid);
            this.song.setLooping(true);
            this.song.start();
        }
    }

    public void onBackPressed() {
    }

    public static void save(float best, float last) {
        Editor editor = someData.edit();
        editor.putString("bestScore", Float.toString(best));
        editor.putString("lastScore", Float.toString(last));
        editor.commit();
    }

    public static void saveMusic(boolean music) {
        Editor editor = someData.edit();
        editor.putBoolean("music", music);
        editor.commit();
    }

    public static void saveBallColor(int col) {
        Editor editor = someData.edit();
        editor.putString("ballColor", Integer.toString(col));
        editor.commit();
    }

    public boolean onTouch(View v, MotionEvent event) {
        boolean z = true;
        if (Renderer.state == 0) {
            if (event.getAction() == 0 && event.getX() > ((float) ((this.renderer.screenWidth / 7) * 2)) && event.getX() < ((float) ((this.renderer.screenWidth / 7) * 5))) {
                if (event.getY() < ((float) (this.renderer.screenHeight / 3))) {
                    Renderer.state = 1;
                    this.renderer.restart(0);
                } else if (event.getY() < ((float) ((this.renderer.screenHeight / 3) * 2))) {
                    Renderer.state = 3;
                } else {
                    finish();
                }
            }
        } else if (Renderer.state == 1) {
            if (event.getAction() == 0) {
                if (Renderer.paused) {
                    z = false;
                }
                Renderer.paused = z;
                if (Renderer.paused) {
                    this.pousedTime = System.currentTimeMillis();
                } else {
                    Renderer.startingTime += System.currentTimeMillis() - this.pousedTime;
                }
            }
        } else if (Renderer.state == 2) {
            if (event.getAction() == 0 && event.getX() > ((float) (this.renderer.screenWidth / 3)) && event.getX() < ((float) ((this.renderer.screenWidth / 3) * 2))) {
                if (event.getY() > ((float) (this.renderer.screenHeight / 2)) && event.getY() < ((float) ((this.renderer.screenHeight / 4) * 3))) {
                    Renderer.state = 1;
                    this.renderer.restart(0);
                } else if (event.getY() > ((float) ((this.renderer.screenHeight / 4) * 3))) {
                    Renderer.state = 0;
                }
            }
        } else if (Renderer.state == 3 && event.getAction() == 0) {
            if (event.getX() > ((float) (this.renderer.screenWidth / 5)) && event.getX() < ((float) ((this.renderer.screenWidth / 5) * 4))) {
                if (event.getY() < ((float) (this.renderer.screenHeight / 3))) {
                    music = !music;
                    saveMusic(music);
                    if (music) {
                        this.song = MediaPlayer.create(this, R.raw.avoid);
                        this.song.setLooping(true);
                        this.song.start();
                    } else {
                        this.song.release();
                    }
                } else if (event.getY() < ((float) ((this.renderer.screenHeight / 3) * 2))) {
                    if (this.renderer.player.sprite == Renderer.bBall) {
                        this.renderer.player.sprite = Renderer.gBall;
                        ballColor = 1;
                        saveBallColor(1);
                    } else if (this.renderer.player.sprite == Renderer.gBall) {
                        this.renderer.player.sprite = Renderer.oBall;
                        ballColor = 2;
                        saveBallColor(2);
                    } else if (this.renderer.player.sprite == Renderer.oBall) {
                        this.renderer.player.sprite = Renderer.pBall;
                        ballColor = 3;
                        saveBallColor(3);
                    } else if (this.renderer.player.sprite == Renderer.pBall) {
                        this.renderer.player.sprite = Renderer.yBall;
                        ballColor = 4;
                        saveBallColor(4);
                    } else if (this.renderer.player.sprite == Renderer.yBall) {
                        this.renderer.player.sprite = Renderer.bBall;
                        ballColor = 0;
                        saveBallColor(0);
                    }
                }
            }
            if (event.getX() > ((float) ((this.renderer.screenWidth / 8) * 3)) && event.getX() < ((float) ((this.renderer.screenWidth / 8) * 5)) && event.getY() > ((float) ((this.renderer.screenHeight / 3) * 2))) {
                Renderer.state = 0;
            }
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onSensorChanged(SensorEvent event) {
        try {
            Thread.sleep(16);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        sensorX = event.values[1];
        sensorY = event.values[0];
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
