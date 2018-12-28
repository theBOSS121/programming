package com.theBOSS.EndlessShooter.entity;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.theBOSS.EndlessShooter.EndlessShooter;
import com.theBOSS.EndlessShooter.graphics.Screen;
import com.theBOSS.EndlessShooter.graphics.Sprite;

public class Bullet extends Entity{

	private int speed;
	private double dir;
	
	public Bullet(int x, int y, Sprite sprite, double dir, int speed) {
		super(x, y, sprite);
		this.dir = dir; 
		this.speed = speed;
		if(EndlessShooter.sounds) {
			try {
				URL url = this.getClass().getClassLoader().getResource("shoot.wav");
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				clip.start();
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-20.0f);
			}catch(UnsupportedAudioFileException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}catch(LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	public void update() {
		x += Math.cos(dir) * speed;
		y += Math.sin(dir) * speed;
		if(x + width < 0 || y + height < 0 || x > EndlessShooter.WIDTH || y > EndlessShooter.HEIGHT) remove = true;
	}
	
	public void render(Screen screen) {
		screen.renderEntity(this, 0);
	}
	
}
