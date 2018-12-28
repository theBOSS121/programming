package theboss.sacrifice.game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import theboss.sacrifice.Configuration;
import theboss.sacrifice.Sacrifice;
import theboss.sacrifice.entity.Enemy;
import theboss.sacrifice.entity.Enemy2;
import theboss.sacrifice.entity.Enemy3;
import theboss.sacrifice.entity.Entity;
import theboss.sacrifice.entity.Player;
import theboss.sacrifice.graphics.Sprite;
import theboss.sacrifice.input.Keyboard;
import theboss.sacrifice.input.Mouse;

public class Game {
	
	public static final int GAME = 0, MENU = 1;
	
	public static Player player;

	private Random rand = new Random();
	
	public static List<Entity> enemies = new ArrayList<Entity>();
	
	
	public static int state = GAME;	
	
	public int counter = 0;
	public static int score = 0;
	public static int bestScore = 0;
	public static boolean gameOver = false;
	public static int overTimer = 0;
	public static int canExplodeCounter = 0;

	Configuration config = new Configuration();
	
	public Game() {
		config.loadConfiguration("config.xml");
		try {
			URL url = this.getClass().getClassLoader().getResource("sacrifice.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);			
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-20.0f);
		}catch(UnsupportedAudioFileException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}catch(LineUnavailableException e) {
			e.printStackTrace();
		}catch(Exception e) {}
		player = new Player(Sacrifice.WIDTH / 2 - Sprite.player.width / 2,Sacrifice.HEIGHT / 2 - Sprite.player.height / 2, Sprite.player);
		
	}
	
	public void update() {
		if(!gameOver) {		
			player.update();
			overTimer = 0;
			for(int i = 0; i < enemies.size(); i++) {
				enemies.get(i).update();
				if(enemies.get(i).timeInTheZone > overTimer) {
					overTimer = enemies.get(i).timeInTheZone;
				}
				if(enemies.get(i).x + enemies.get(i).width < 0 || enemies.get(i).x > Sacrifice.WIDTH || enemies.get(i).y + enemies.get(i).height < 0 || enemies.get(i).x > Sacrifice.HEIGHT) {
					if(enemies.get(i).onScreen && enemies.get(i).counter < 3) {
						enemies.get(i).remove = true;
					}
				}else {
					enemies.get(i).onScreen = true;
				}
				if(Math.sqrt(Math.pow(enemies.get(i).x - Sacrifice.WIDTH / 2, 2) + Math.pow(enemies.get(i).y - Sacrifice.HEIGHT / 2, 2)) > 600) {
					if(enemies.get(i).counter < 3) {
						enemies.get(i).remove = true;					
					}
				}
			}
			for(int i = 0; i < enemies.size(); i++) {
				if(enemies.get(i).remove) {
					enemies.remove(i);
					if(!player.canExplde && !player.exploding) {
						canExplodeCounter++;
					}
					score++;
					break;
				}
			}
			if(canExplodeCounter >= 10) {
				player.canExplde = true;
				canExplodeCounter = 0;
			}
			
			if(overTimer > 600) {
				gameOver = true;
			}
			collision();
			if(player.canExplde && (Keyboard.keyDown(KeyEvent.VK_E) || Keyboard.keyDown(KeyEvent.VK_SPACE))) {
				explde();
				player.canExplde = false;
			}
			if(counter < 1800) {					
				if(counter % 120 == 0) {
					createrRandomEnemy();
				}
			}else if(counter < 3600){			
				if(counter % 90 == 0) {
					createrRandomEnemy();
				}				
			}else if(counter < 5400){			
				if(counter % 60 == 0) {
					createrRandomEnemy();
				}				
			}else if(counter < 7200) {
				if(counter % 45 == 0) {
					createrRandomEnemy();
				}
			}else {
				if(counter % 30 == 0) {
					createrRandomEnemy();
				}
			}
			counter++;
			if(score > bestScore) {
				bestScore = score;
				config.saveConfiguration("best", bestScore);
			}
		}else {
			
			if(Keyboard.key(KeyEvent.VK_R) || Mouse.buttonUp(1)) {
				restart();
			}
		}
		
		Sacrifice.screen.setOffSet(0, 0);
	}
	
	private void restart() {
		enemies = new ArrayList<Entity>();
		player = new Player(Sacrifice.WIDTH / 2 - Sprite.player.width / 2,Sacrifice.HEIGHT / 2 - Sprite.player.height / 2, Sprite.player);
		score = 0;
		counter = 0;
		overTimer = 0;
		canExplodeCounter = 0;
		gameOver = false;
	}

	private void createrRandomEnemy() {
		int r = rand.nextInt(6);
		if(counter < 1800) {			
			if(r == 0) {
				enemies.add(new Enemy3(Sacrifice.WIDTH / 2 - Sprite.enemy3.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy3.height / 2, Sprite.enemy3));			
			}else if(r == 1 || r == 2) {
				enemies.add(new Enemy(Sacrifice.WIDTH / 2 - Sprite.enemy1.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy1.height / 2, Sprite.enemy1));
			}else {
				enemies.add(new Enemy2(Sacrifice.WIDTH / 2 - Sprite.enemy2.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy2.height / 2, Sprite.enemy2));
			}		
		}else if(counter < 3600) {	
			if(r == 0) {
				enemies.add(new Enemy3(Sacrifice.WIDTH / 2 - Sprite.enemy3.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy3.height / 2, Sprite.enemy3));			
			}else if(r == 1 || r == 2) {
				enemies.add(new Enemy2(Sacrifice.WIDTH / 2 - Sprite.enemy2.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy2.height / 2, Sprite.enemy2));
			}else {
				enemies.add(new Enemy(Sacrifice.WIDTH / 2 - Sprite.enemy1.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy1.height / 2, Sprite.enemy1));
			}			
		}else if(counter < 5400) {	
			if(r == 0) {
				enemies.add(new Enemy3(Sacrifice.WIDTH / 2 - Sprite.enemy3.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy3.height / 2, Sprite.enemy3));			
			}else if(r == 1 || r == 2) {
				enemies.add(new Enemy2(Sacrifice.WIDTH / 2 - Sprite.enemy2.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy2.height / 2, Sprite.enemy2));
			}else {
				enemies.add(new Enemy(Sacrifice.WIDTH / 2 - Sprite.enemy1.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy1.height / 2, Sprite.enemy1));
			}			
		}else if(counter < 7200) {
			if(r == 0) {
				enemies.add(new Enemy(Sacrifice.WIDTH / 2 - Sprite.enemy1.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy1.height / 2, Sprite.enemy1));
			}else if(r == 1 || r == 2) {
				enemies.add(new Enemy3(Sacrifice.WIDTH / 2 - Sprite.enemy3.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy3.height / 2, Sprite.enemy3));			
			}else {
				enemies.add(new Enemy2(Sacrifice.WIDTH / 2 - Sprite.enemy2.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy2.height / 2, Sprite.enemy2));
			}		
		}else {
			if(r == 0) {
				enemies.add(new Enemy2(Sacrifice.WIDTH / 2 - Sprite.enemy2.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy2.height / 2, Sprite.enemy2));
			}else if(r == 1 || r == 2) {
				enemies.add(new Enemy3(Sacrifice.WIDTH / 2 - Sprite.enemy3.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy3.height / 2, Sprite.enemy3));			
			}else {
				enemies.add(new Enemy(Sacrifice.WIDTH / 2 - Sprite.enemy1.width / 2, Sacrifice.HEIGHT / 2 - Sprite.enemy1.height / 2, Sprite.enemy1));
			}		
		}
	}

	private void explde() {
		double dir = 0.0;
		double xDiff;
		double yDiff;
		int explosionMagintude = 10;
		for(int i = 0; i < enemies.size(); i++) {
			xDiff = enemies.get(i).x - player.x;
			yDiff = enemies.get(i).y - player.y;
			dir = Math.atan2(yDiff, xDiff);
			enemies.get(i).velX = Math.cos(dir) * explosionMagintude;
			enemies.get(i).velY = Math.sin(dir) * explosionMagintude;
		}
		player.exploding = true;
		player.explodeTimer = 0;
		player.transparent = 0.0;
		try {
			URL url = this.getClass().getClassLoader().getResource("explosion.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
//			clip.loop(Clip.LOOP_CONTINUOUSLY);			
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-15.0f);
		}catch(UnsupportedAudioFileException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}catch(LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	
	Clip cwp, cwe;
	
	private void collision() {
		double xDiff, yDiff;
		double distance;
		
		for(int i = 0; i < enemies.size(); i++) {
			xDiff = enemies.get(i).x - player.x;
			yDiff = enemies.get(i).y - player.y;
			distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
			
			if(distance < enemies.get(i).radius + player.radius) {
				if(player.transparent > 0.99) {
					resolveCollision(player, enemies.get(i));
					enemies.get(i).hit = true;	
					try {
						URL url = this.getClass().getClassLoader().getResource("collision.wav");
						AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
						cwp = AudioSystem.getClip();
						cwp.open(audioIn);
						if(!cwp.isRunning()) cwp.start();
//						clip.loop(Clip.LOOP_CONTINUOUSLY);			
						FloatControl gainControl = (FloatControl) cwp.getControl(FloatControl.Type.MASTER_GAIN);
						gainControl.setValue(-10.0f);
						
					}catch(UnsupportedAudioFileException e) {
						e.printStackTrace();
					}catch(IOException e) {
						e.printStackTrace();
					}catch(LineUnavailableException e) {
						e.printStackTrace();
					}
				}				
			}
			
		}
		
		for(int i = 0; i < enemies.size(); i++) {
			for(int j = 0; j < enemies.size(); j++) {
				if(i == j) continue;
				xDiff = enemies.get(i).x - enemies.get(j).x;
				yDiff = enemies.get(i).y - enemies.get(j).y;
				distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
				
				if(distance < enemies.get(i).radius + enemies.get(j).radius) {
					resolveCollision(enemies.get(j), enemies.get(i));
					enemies.get(i).hit = true;	
					enemies.get(j).hit = true;	
					try {
						URL url = this.getClass().getClassLoader().getResource("collision.wav");
						AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
						cwe = AudioSystem.getClip();
						cwe.open(audioIn);
						if(!cwe.isRunning()) cwe.start();
//						clip.loop(Clip.LOOP_CONTINUOUSLY);			
						FloatControl gainControl = (FloatControl) cwe.getControl(FloatControl.Type.MASTER_GAIN);
						gainControl.setValue(-10.0f);
					}catch(UnsupportedAudioFileException e) {
						e.printStackTrace();
					}catch(IOException e) {
						e.printStackTrace();
					}catch(LineUnavailableException e) {
						e.printStackTrace();
					}
				}
			}			
		}

	}
	
	public double rotate(double vx, double vy, double angle, boolean which) { 
		double velX = vx;
		double velY = vy;
		
		velX = vx * Math.cos(angle) - vy * Math.sin(angle);
		velY = vx * Math.sin(angle) + vy * Math.cos(angle);
		
		if(which) {
			return velX;	
		}else {
			return velY;
		}
		
	}
	
	public void resolveCollision(Entity e1, Entity e2) {
		double xVelocityDiff = e1.velX - e2.velX;
		double yVelocityDiff = e1.velY - e2.velY;

		double xDist = e2.x - e1.x;
		double yDist = e2.y - e1.y;
		
		if(xVelocityDiff * xDist + yVelocityDiff * yDist >= 0) {
			double angle = -Math.atan2(yDist, xDist);
			double m1 = e1.mass;
			double m2 = e2.mass;
			
			double ux1 = rotate(e1.velX, e1.velY, angle, true);
			double ux2 = rotate(e2.velX, e2.velY, angle, true);
			double uy1 = rotate(e1.velX, e1.velY, angle, false);
			double uy2 = rotate(e2.velX, e2.velY, angle, false);
			
			double vx1 = (ux1 * (m1 - m2) + 2 * ux2 * m2 )/ (m1 + m2);
			double vx2 = (ux2 * (m2 - m1) + 2 * ux1 * m1 )/ (m1 + m2);
			double vy1 = uy1;
			double vy2 = uy2;
			
			double vxFinal1 = rotate(vx1, vy1, -angle, true);
			double vxFinal2 = rotate(vx2, vy2, -angle, true);
			double vyFinal1 = rotate(vx1, vy1, -angle, false);
			double vyFinal2 = rotate(vx2, vy2, -angle, false);
			
			e1.velX = vxFinal1;
			e1.velY = vyFinal1;
			e2.velX = vxFinal2;
			e2.velY = vyFinal2;	
		}		
	}

	public void render() {
		Sacrifice.screen.renderSprite(Sprite.player_shadow, (int) player.x + player.sprite.width / 2 - 7, (int) player.y + player.sprite.height - 6, player.transparent);
		for(int i = 0; i < enemies.size(); i++) {
			Sacrifice.screen.renderSprite(Sprite.player_shadow, (int) enemies.get(i).x + enemies.get(i).sprite.width / 2 - 7, (int) enemies.get(i).y + enemies.get(i).sprite.height - 6);
		}
		player.render();
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).render();
		}
		
		
	}
}
