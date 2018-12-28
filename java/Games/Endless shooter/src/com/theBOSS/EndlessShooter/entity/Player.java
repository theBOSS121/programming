package com.theBOSS.EndlessShooter.entity;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.theBOSS.EndlessShooter.EndlessShooter;
import com.theBOSS.EndlessShooter.graphics.Screen;
import com.theBOSS.EndlessShooter.graphics.Sprite;
import com.theBOSS.EndlessShooter.input.Keyboard;
import com.theBOSS.EndlessShooter.input.Mouse;

public class Player extends Entity {

	private Keyboard key;
	private int flip = 0;
	private int time = 0;
	private double dir = 0.0;
	private boolean walking = false;
	private double speed = 4.0;
	boolean coolDown = false;
	boolean soundPlaying = false;

	public int life = 100;
	public int heal = 0;
	boolean dead = false;
	private int deadTime = 0;
	
	public List<Bullet> bullets = new ArrayList<Bullet>();

	public Player(int x, int y, Sprite sprite, Keyboard key) {
		super(x, y, sprite);
		this.key = key;
	}

	public void update() {
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
		}
		if(!dead) {
			if(key.up) {
				if(y - speed > 0) {
					y -= speed;
					Screen.bgOffY++;
				}
			}
			if(key.down) {
				if(y + speed + height <= EndlessShooter.HEIGHT) {
					y += speed;
					Screen.bgOffY--;
				}
			}
			if(key.left) {
				if(x - speed > 0) {
					x -= speed;
					Screen.bgOffX++;
				}
			}
			if(key.right) {
				if(x + speed + width < EndlessShooter.WIDTH) {
					x += speed;
					Screen.bgOffX--;
				}
			}
			if(key.healing) {
				if(heal > 0 && life < 100) {
					heal--;
					life++;
					if(!soundPlaying && EndlessShooter.sounds) {
						try {
							URL url = this.getClass().getClassLoader().getResource("healing.wav");
							AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
							Clip clip = AudioSystem.getClip();
							clip.open(audioIn);
							clip.start();
							FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
							gainControl.setValue(-10.0f);
							soundPlaying = true;
						}catch(UnsupportedAudioFileException e) {
							e.printStackTrace();
						}catch(IOException e) {
							e.printStackTrace();
						}catch(LineUnavailableException e) {
							e.printStackTrace();
						}
					}
				}
			}else {
				soundPlaying = false;
			}
			
			if(!key.up && !key.down && !key.left && !key.right) walking = false;
			else walking = true;
	
			if(time % 10 == 0 && walking) {
				if(sprite == Sprite.p_front1) sprite = Sprite.p_front2;
				else if(sprite == Sprite.p_front2) sprite = Sprite.p_front3;
				else if(sprite == Sprite.p_front3) sprite = Sprite.p_front1;
			}
			if(!walking && !dead) {
				sprite = Sprite.p_front3;
			}
	
			double dx = Mouse.getX() - x - width / 2;
			double dy = Mouse.getY() - y - height / 2;
			double dir = Math.atan2(dy, dx);
			rotatedSprite = Sprite.rotate(sprite, dir);
	
			if(coolDown && Mouse.getButton() != MouseEvent.BUTTON1) coolDown = false;
	
			if(Mouse.getButton() == MouseEvent.BUTTON1 && !coolDown) {
				bullets.add(new Bullet((int) x + width / 2, (int) y + height / 2, Sprite.bullet, dir, 12));
				coolDown = true;
			}
		}
		for(int i = 0; i < bullets.size(); i++)	{
			if(bullets.get(i).remove) bullets.remove(i);
			
		}
		if(life <= 0) {
			dead = true;
			heal = 0;
			life = 0;
			EndlessShooter.state = 3;
		}
		
		if(dead) {
			rotatedSprite = Sprite.rotate(Sprite.p_dead, dir);
			deadTime++;
			if(deadTime >= 60) {
				remove = true;				
			}
			if(!soundPlaying && EndlessShooter.sounds) {
				try {
					URL url = this.getClass().getClassLoader().getResource("death.wav");
					AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
					Clip clip = AudioSystem.getClip();
					clip.open(audioIn);
					clip.start();
					FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
					gainControl.setValue(-10.0f);
					soundPlaying = true;
				}catch(UnsupportedAudioFileException e) {
					e.printStackTrace();
				}catch(IOException e) {
					e.printStackTrace();
				}catch(LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		}

		time++;
	}

	public void render(Screen screen) {
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(screen);
		}
		screen.renderEntity(this, flip);
		if(EndlessShooter.state != 3) {
			screen.renderHealthBar(EndlessShooter.WIDTH / 16 * 2, EndlessShooter.HEIGHT / 21 * 19, life / 100.0, 8, 2);
			screen.renderHealthBar(EndlessShooter.WIDTH / 16 * 2, EndlessShooter.HEIGHT / 21 * 20, heal / 100.0, 8, 2);
		}

	}
}
