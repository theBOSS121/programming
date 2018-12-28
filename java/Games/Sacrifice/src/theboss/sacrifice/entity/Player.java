package theboss.sacrifice.entity;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import theboss.sacrifice.Sacrifice;
import theboss.sacrifice.graphics.Sprite;
import theboss.sacrifice.input.Keyboard;

public class Player extends Entity{

	public double maxVel = 5.0;
	public boolean canExplde = false;
	public boolean exploding = false;
	public int explodeTimer = 0;
	public double transparent = 1.0;
	
	public List<Particle> particles = new ArrayList<Particle>();
	public Random rand = new Random();
	
	public Player(double x, double y, Sprite sprite) {
		super(x, y, sprite);
		mass = 1.0;
	}	
	
	public void update() {
		if(transparent >=0.1) {
			if((Keyboard.key(KeyEvent.VK_A) || Keyboard.key(KeyEvent.VK_LEFT)) && (!Keyboard.key(KeyEvent.VK_D) && ! Keyboard.key(KeyEvent.VK_RIGHT))) {
				if(velX >= -maxVel) {
					velX -= 0.2;
				}
			}
			if((Keyboard.key(KeyEvent.VK_D) || Keyboard.key(KeyEvent.VK_RIGHT)) && (!Keyboard.key(KeyEvent.VK_A) && ! Keyboard.key(KeyEvent.VK_LEFT))) {
				if(velX <= maxVel) {
					velX += 0.2;
				}
			}
			if((Keyboard.key(KeyEvent.VK_A) && Keyboard.key(KeyEvent.VK_D) || Keyboard.key(KeyEvent.VK_LEFT) && Keyboard.key(KeyEvent.VK_RIGHT)) ||
			   (!Keyboard.key(KeyEvent.VK_A) && !Keyboard.key(KeyEvent.VK_D) && !Keyboard.key(KeyEvent.VK_LEFT) && ! Keyboard.key(KeyEvent.VK_RIGHT))) {
				if(Math.abs(velX) < 0.1) {
					velX = 0;
				}
				if(velX > 0.0) {
					velX -= 0.05;
				}else if(velX < 0.0) {
					velX += 0.05;
				}
			}
			if((Keyboard.key(KeyEvent.VK_W) || Keyboard.key(KeyEvent.VK_UP)) && (!Keyboard.key(KeyEvent.VK_S) && ! Keyboard.key(KeyEvent.VK_DOWN))) {
				if(velY >= -maxVel) {
					velY -= 0.2;
				}
			}
			if((Keyboard.key(KeyEvent.VK_S) || Keyboard.key(KeyEvent.VK_DOWN)) && (!Keyboard.key(KeyEvent.VK_W) && ! Keyboard.key(KeyEvent.VK_UP))) {
				if(velY <= maxVel) {
					velY += 0.2;
				}
			}
			if((Keyboard.key(KeyEvent.VK_W) && Keyboard.key(KeyEvent.VK_S) || Keyboard.key(KeyEvent.VK_UP) && Keyboard.key(KeyEvent.VK_DOWN)) ||
		      (!Keyboard.key(KeyEvent.VK_W) && !Keyboard.key(KeyEvent.VK_S) && !Keyboard.key(KeyEvent.VK_UP) && ! Keyboard.key(KeyEvent.VK_RIGHT))) {
				if(Math.abs(velY) < 0.1) {
					velY = 0;
				}
				if(velY > 0.0) {
					velY -= 0.05;
				}else if(velY < 0.0) {
					velY += 0.05;
				}
			}
		}
		
		x += velX;
		y += velY;
		
		if(exploding) {
			if(explodeTimer == 0) {
				createParticles();
			}else if(explodeTimer == 1) {
				x = Sacrifice.WIDTH / 2 - sprite.width / 2;
				y = Sacrifice.HEIGHT / 2 - sprite.height / 2;
				velX = 0;
				velY = 0;
			}
			if(transparent < 1.0 && explodeTimer > 30) transparent += 0.03;
			if(transparent >= 1.0) {
				transparent = 1.0;
				exploding = false;
			}
			explodeTimer++;
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
		if(x < 0) {
			x = 0;
			velX = 0;
		}
		if(x + width > Sacrifice.WIDTH) {
			x = Sacrifice.WIDTH - width;
			velX = 0;
		}
		if(y < 0) {
			y = 0;
			velY = 0;
		}
		if(y + height > Sacrifice.HEIGHT) {
			y = Sacrifice.HEIGHT - height;
			velY = 0;
		}
	}
	
	private void createParticles() {
		for(int i = 0; i < 100; i++) {
			double dir = rand.nextDouble() * 2 * Math.PI;
			int distanceFromCenter = rand.nextInt(radius);
			int nx = (int) (this.x + this.width / 2 + Math.cos(dir) * distanceFromCenter);
			int ny = (int) (this.y + this.height / 2 + Math.sin(dir) * distanceFromCenter);
			
			int w = 10;
			int h = 10;
			int[] p = new int[w * h];
			for(int yy = 0; yy < h; yy++) {
				for(int xx = 0; xx < w; xx++) {
					p[xx + yy * w] = 0xff3e606f;
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
		Sacrifice.screen.renderSprite(sprite, (int) x, (int) y, transparent);
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).render();
		}
	}
	
}
