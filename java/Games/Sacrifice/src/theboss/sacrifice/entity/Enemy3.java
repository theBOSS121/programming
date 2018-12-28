package theboss.sacrifice.entity;

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

import theboss.sacrifice.Sacrifice;
import theboss.sacrifice.game.Game;
import theboss.sacrifice.graphics.Sprite;

public class Enemy3 extends Entity{

	double dir = 0.0;
	double speed = 1.5;
	
	Random rand = new Random();
	
	public boolean noFrictionInTheZone = false;
	
	public List<Particle> particles = new ArrayList<Particle>();
	
	private double transparency = 1.0;
	
	public Enemy3(double x, double y, Sprite sprite) {
		super(x, y, sprite);
		this.mass = 1.5;
		dir = rand.nextDouble() * 2 * Math.PI;
		
		velX = Math.cos(dir) * speed;
		velY = Math.sin(dir) * speed;
		
		this.x = Sacrifice.WIDTH / 2 - sprite.width / 2 - (Math.cos(dir) * 570);
		this.y = Sacrifice.HEIGHT / 2 - sprite.height / 2 - (Math.sin(dir) * 570);
	}
	
	public void update() {
		double dir = Math.atan2(velY, velX);
		double wantedDir = Math.atan2(Sacrifice.HEIGHT / 2 - this.y - this.height / 2, Sacrifice.WIDTH / 2 - this.x - this.width / 2);
		if(dir != wantedDir && !isInTheZone()) {
			double wVelX = Math.cos(wantedDir) * speed;
			double wVelY = Math.sin(wantedDir) * speed;
			if(wVelX > velX) {
				velX += 0.01;				
			}else {
				velX -= 0.01;								
			}
			if(wVelY > velY) {
				velY += 0.01;				
			}else {
				velY -= 0.01;								
			}
//			velX = wVelX;
//			velY = wVelY;
		}
		if(hit) {
			noFrictionInTheZone = true;
			hit = false;
			counter++;
			transparency -= 0.25;
		}
		
		if(isInTheZone() && !noFrictionInTheZone) {
			velX *= 0.98;
			velY *= 0.98;
		}
		if(noFrictionInTheZone && !isInTheZone()) {
			noFrictionInTheZone = false;
		}
		
		x += velX;
		y += velY;
		
		if(counter >= 4 ) {
			if(counter == 4) {
				creatrParticle();	
				explde();
			}
			if(counter > 30) {
				remove = true;
			}
			x = -1000;
			y = -1000;
			velX = 0;
			velY = 0;
			counter++;
			Game.canExplodeCounter = 0;
		}
		
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).update();
			if(particles.get(i).x < 0 || particles.get(i).x > Sacrifice.WIDTH || particles.get(i).y < 0 || particles.get(i).y > Sacrifice.HEIGHT) {
				particles.get(i).remove = true;
			}
		}
		for(int i = 0; i < particles.size(); i++) {
			if(particles.get(i).remove) {
				particles.remove(i);
			}
		}
		
		if(isInTheZone()) {
			timeInTheZone++;
		}else {
			timeInTheZone = 0;
		}
		
	}
	
	private void explde() {
		double dir = 0.0;
		double xDiff;
		double yDiff;
		int explosionMagintude = 1;
//		for(int i = 0; i < Game.enemies.size(); i++) {
//			xDiff = Game.enemies.get(i).x - this.x;
//			yDiff = Game.enemies.get(i).y - this.y;
//			dir = Math.atan2(yDiff, xDiff);
//			Game.enemies.get(i).velX = Math.cos(dir) * explosionMagintude;
//			Game.enemies.get(i).velY = Math.sin(dir) * explosionMagintude;
//		}
		xDiff = Game.player.x - this.x;
		yDiff = Game.player.y - this.y;
		dir = Math.atan2(yDiff, xDiff);
		Game.player.velX = Math.cos(dir) * explosionMagintude;
		Game.player.velY = Math.sin(dir) * explosionMagintude;
		Game.player.transparent = 0.0;
		Game.player.explodeTimer = 1;
		Game.player.exploding = true;
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
	
	private void creatrParticle() {
		for(int i = 0; i < 50; i++) {
			double dir = rand.nextDouble() * 2 * Math.PI;
			int distanceFromCenter = rand.nextInt(radius);
			int nx = (int) (this.x + this.width / 2 + Math.cos(dir) * distanceFromCenter);
			int ny = (int) (this.y + this.height / 2 + Math.sin(dir) * distanceFromCenter);
			
			int w = 10;
			int h = 10;
			int[] p = new int[w * h];
			for(int yy = 0; yy < h; yy++) {
				for(int xx = 0; xx < w; xx++) {
				p[xx + yy * w] = 0xff2E7A2F;
				if(xx < 3 || xx > h-3 || yy < 3 || yy > h-3) {
					if(rand.nextInt(3) != 0) {
						p[xx + yy * w] = 0x00000000;
					}
				}
			}
		}
		Sprite s = new Sprite(p, w, h);
		
		particles.add(new Particle(nx, ny, s, dir));
	}
		
	}

	public void render() {
		if(transparency < 1.0) Sacrifice.screen.renderSprite(Sprite.enemy3_bg, (int) x, (int) y);
		Sacrifice.screen.renderSprite(sprite, (int) x, (int) y, transparency);
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).render();
		}
	}		
}
