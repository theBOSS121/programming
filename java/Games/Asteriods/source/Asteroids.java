import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Asteroids {

	public final static int WIDTH = 800, HEIGHT = 600;
	public static int width = 40, height = 40, howMany = 4, lives = 3, score = 0;
	private static int[] pixels, newPixels;
	private boolean running = true, valid = false;
	private static boolean started = true;
	private boolean shoot = false;
	JFrame frame;
	Renderer renderer;
	private Random rand = new Random();
	public static BufferedImage ship, live, asterid;
	private static boolean gameOver = false;
	private static Player player;
	private Keyboard key = new Keyboard();
	private long startTime;
	
	public static List<Object> objects = new ArrayList<Object>();
	public static List<Bullet> bullets = new ArrayList<Bullet>();
	public static int bestScore = 0;
	Configuration config = new Configuration();

	public Asteroids() {
		config.loadConfiguration("config.xml");	
		frame = new JFrame("Asteriods");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(WIDTH + 6, HEIGHT + 29);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		player = new Player(WIDTH / 2 - width / 2 - 1, HEIGHT / 2 - height / 2 - 1, 3 * Math.PI / 2);
		renderer = new Renderer();
		frame.add(renderer);
		frame.addKeyListener(key);
		frame.setLocationRelativeTo(null);
		try {
			asterid = ImageIO.read(Renderer.class.getResource("/asteroid.png"));
			ship = ImageIO.read(Renderer.class.getResource("/ship.png"));
			live = ImageIO.read(Renderer.class.getResource("/ship.png"));
			width = ship.getWidth();
			height = ship.getHeight();
			pixels = new int[width * height];
			ship.getRGB(0, 0, width, height, pixels, 0, width);
			newPixels = pixels;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(started) {
			objects.add(new Object(10 + rand.nextInt(100), 10 + rand.nextInt(100), (rand.nextInt(2) + 1), (rand.nextInt(2) + 1), 3));
			objects.add(new Object(WIDTH - 110 - rand.nextInt(100), 10 + rand.nextInt(100), -(rand.nextInt(2) + 1), (rand.nextInt(2) + 1), 3));
			objects.add(new Object(10 + rand.nextInt(100), HEIGHT - 110 - rand.nextInt(100), (rand.nextInt(2) + 1), -(rand.nextInt(2) + 1), 3));
			objects.add(new Object(WIDTH - 110 - rand.nextInt(100), HEIGHT - 110 - rand.nextInt(100), -(rand.nextInt(2) + 1), -(rand.nextInt(2) + 1), 3));
		}
		for(int i = 0; i < 4; i++) {
			if(rand.nextInt(2) == 1) {
				objects.get(i).dx = -1 * (objects.get(i).dx);
				
			}
			if(rand.nextInt(2) == 1) {
				objects.get(i).dy = -1 *(objects.get(i).dy);
			}
		}
		startTime = System.currentTimeMillis();
		gameLoop();
		
	}

	private void gameLoop() {
		double delta = 1000.0 / 60.0;
		double deltaF = 1000.0 / 100.0;
		double lastTime = System.currentTimeMillis();
		double currentTime;
		int updates = 0;
		int frames = 0;
		
		while(running) {
			currentTime = System.currentTimeMillis();
			if(currentTime - lastTime > 1000.0) {
				frame.setTitle("Asteroids " + updates + ", " + frames);
				updates = 0;
				frames = 0;
				lastTime += 1000.0;
			}
			if(currentTime - lastTime > delta * updates) {
				updates++;
				update();
			}
			if(currentTime - lastTime > deltaF * frames) {
				renderer.repaint();
				frames++;
			}
		}		
	}
	
	private void update() {
		key.update();
		if(!gameOver) {
			for(int i = 0; i < objects.size(); i++) {
				objects.get(i).update();
			}		
			keys();
			for(int i = 0; i < bullets.size(); i++) {
				bullets.get(i).move();
				if(bullets.get(i).range > 650) {
					bullets.get(i).remove = true;
				}
			}		
			for(int i = 0; i < bullets.size(); i++) {
				if(bullets.get(i).remove) {
					bullets.remove(i);
				}
			}		
			bulletCollision();
			if(System.currentTimeMillis() - startTime > 2000) {
				started = false;
				playerCollision();		
			}
			player.move();	
			newPixels = rotate(pixels, width, height, player.angle);
			ship.setRGB(0, 0, width, height, newPixels, 0, width);	
			if(objects.size() == 0) {
				makeObjects();
			}
		}if(gameOver) {
			if(key.space) {
				valid = true;
			}
			if(valid && !key.space) {
				valid = false;
				gameOver = false;
				lives = 3;
				howMany = 3;
				objects = new ArrayList<>();
				makeObjects();
				score = 0;
			}
		}
	}

	private void makeObjects() {
		howMany++;
		objects.add(new Object(10 + rand.nextInt(100), 10 + rand.nextInt(100), (rand.nextInt(2) + 1), (rand.nextInt(2) + 1), 3));
		objects.add(new Object(WIDTH - 110 - rand.nextInt(100), 10 + rand.nextInt(100), -(rand.nextInt(2) + 1), (rand.nextInt(2) + 1), 3));
		objects.add(new Object(10 + rand.nextInt(100), HEIGHT - 110 - rand.nextInt(100), (rand.nextInt(2) + 1), -(rand.nextInt(2) + 1), 3));
		objects.add(new Object(WIDTH - 110 - rand.nextInt(100), HEIGHT - 110 - rand.nextInt(100), -(rand.nextInt(2) + 1), -(rand.nextInt(2) + 1), 3));
		for(int i = 4; i < howMany; i++) {
			objects.add(new Object(rand.nextInt(WIDTH), rand.nextInt(HEIGHT), (rand.nextInt(2) + 1), (rand.nextInt(2) + 1), 3));
			while(objects.get(objects.size() - 1).x > 200 && objects.get(objects.size() - 1).x < WIDTH - 200) {
				objects.get(objects.size() - 1).x = rand.nextInt(WIDTH); 
				objects.get(objects.size() - 1).y = rand.nextInt(HEIGHT);
			}
		}
		for(int i = 0; i < objects.size(); i++) {
			if(rand.nextInt(2) == 1) {
				objects.get(i).dx = -1 * (objects.get(i).dx);
				
			}
			if(rand.nextInt(2) == 1) {
				objects.get(i).dy = -1 *(objects.get(i).dy);
			}
		}
	}

	private void keys() {
		if(key.left) player.angle -= 0.08;
		if(key.right) player.angle += 0.08;
		if(key.up) { 
			player.changeDir();
			if(key.left) player.angle += 0.04;
			if(key.right) player.angle -= 0.04;
			if(player.speed < 4) {
				player.speed *= 1.01;
			}if(player.speed < 1.0) player.speed = 1.0;
		}
		if(!key.up) {
			if(player.speed > 3.0) player.speed *= 0.99;
			else if(player.speed > 2.0) player.speed *= 0.90;
			else player.speed *= 0.98;
			if(player.speed < 0.01) player.speed = 0;
		}
		if(key.space && !shoot) shoot = true;
		if(shoot) {
			if(!key.space) {
				bullets.add(new Bullet(player.x + width / 2, player.y + height / 2, player.angle, 6.0));
				shoot = false;
			}
		}
	}
	
	private void playerCollision() {
		for(int j = 0; j < objects.size(); j++) {
			if(player.x + width > objects.get(j).x && player.x < objects.get(j).x + objects.get(j).width && 
			   player.y + height > objects.get(j).y && player.y < objects.get(j).y + objects.get(j).height) {
				if(objects.get(j).dimension != 1) {
					objects.add(new Object(objects.get(j).x, objects.get(j).y, objects.get(j).dx + rand.nextInt(5) - 2, objects.get(j).dy + rand.nextInt(5) - 2, objects.get(j).dimension - 1));
					while(objects.get(objects.size() - 1).dx == 0 || objects.get(objects.size() - 1).dy == 0) {
						objects.get(objects.size() - 1).dx = rand.nextInt(5) - 2;
						objects.get(objects.size() - 1).dy = rand.nextInt(5) - 2;
					}
					objects.add(new Object(objects.get(j).x, objects.get(j).y, rand.nextInt(5) - 2, rand.nextInt(5) - 2, objects.get(j).dimension - 1));
					while(objects.get(objects.size() - 1).dx == 0 || objects.get(objects.size() - 1).dy == 0) {
						objects.get(objects.size() - 1).dx = rand.nextInt(5) - 2;
						objects.get(objects.size() - 1).dy = rand.nextInt(5) - 2;
					}
				}
				if(objects.get(j).dimension == 3) score++;
				else if(objects.get(j).dimension == 2) score += 3;
				else score += 5;
				objects.remove(j);
				player.x = WIDTH / 2 - width / 2;
				player.y = HEIGHT / 2 - height / 2;
				player.speed = 0;
				lives--;
				startTime = System.currentTimeMillis();
				started = true;
				if(lives == 0) {
					gameOver = true;
				}
				if(gameOver) {
					if(bestScore < score) {
						bestScore = score;
						config.saveConfiguration("best", bestScore);
					}
				}
				break;
			}
		}
	}

	private void bulletCollision() {
		for(int i = 0; i < bullets.size(); i++) {
			for(int j = 0; j < objects.size(); j++) {
				if(bullets.get(i).x + 2 > objects.get(j).x && bullets.get(i).x < objects.get(j).x + objects.get(j).width && 
				   bullets.get(i).y + 2 > objects.get(j).y && bullets.get(i).y < objects.get(j).y + objects.get(j).height) {
					bullets.get(i).remove = true;
					if(objects.get(j).dimension != 1) {
						objects.add(new Object(objects.get(j).x, objects.get(j).y, objects.get(j).dx + rand.nextInt(5) - 2, objects.get(j).dy + rand.nextInt(5) - 2, objects.get(j).dimension - 1));
						while(objects.get(objects.size() - 1).dx == 0 || objects.get(objects.size() - 1).dy == 0) {
							objects.get(objects.size() - 1).dx = rand.nextInt(5) - 2;
							objects.get(objects.size() - 1).dy = rand.nextInt(5) - 2;
						}
						objects.add(new Object(objects.get(j).x, objects.get(j).y, rand.nextInt(5) - 2, rand.nextInt(5) - 2, objects.get(j).dimension - 1));
						while(objects.get(objects.size() - 1).dx == 0 || objects.get(objects.size() - 1).dy == 0) {
							objects.get(objects.size() - 1).dx = rand.nextInt(5) - 2;
							objects.get(objects.size() - 1).dy = rand.nextInt(5) - 2;
						}
					}
					if(objects.get(j).dimension == 3) score++;
					else if(objects.get(j).dimension == 2) score += 3;
					else score += 5;
					objects.remove(j);
					break;
				}
			}
		}
	}

	public static void render(Graphics g) {
		if(!gameOver) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.GRAY);
			g.setFont(new Font("Ariel", 0, 30));
			String s = "Score: " + score;
			int w = g.getFontMetrics().stringWidth(s) / 2;
			g.drawString(s, 90 - w, 80);
			String bs = "Best: " + bestScore;
			int bw = g.getFontMetrics().stringWidth(bs) / 2;
			g.drawString(bs, 90 - bw, 120);
			
			for(int i = 0; i < lives; i++) {
				g.drawImage(live , i * width + 30, 10, width, height, null);			
			}
			
			for(int i = 0; i < objects.size(); i++) {
				Object o = objects.get(i);
				g.drawImage(asterid, o.x, o.y, o.width, o.height, null);
				
			}
			
			for(int i = 0; i < bullets.size(); i++) {
				g.setColor(Color.WHITE);
				g.drawRect((int) bullets.get(i).x, (int) bullets.get(i).y, 2, 2);
			}
			if(started) {
				if(System.currentTimeMillis() % 700 < 350) {
					g.drawImage(ship,(int) player.x ,(int) player.y, null);
				}
			}else {
				g.drawImage(ship,(int) player.x ,(int) player.y, null);	
			}
		}if(gameOver) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.GRAY);
			g.setFont(new Font("Ariel", 0, 100));
			String gs = "Game Over";
			int gw = g.getFontMetrics().stringWidth(gs) / 2;
			g.drawString(gs, WIDTH / 2 - gw, 150);
			String s = "Score: " + score;
			int w = g.getFontMetrics().stringWidth(s) / 2;
			g.drawString(s, WIDTH / 2 - w, 300);
			String bs = "Best score: " + bestScore;
			int bw = g.getFontMetrics().stringWidth(bs) / 2;
			g.drawString(bs, WIDTH / 2 - bw, 450);			
		}
	}
	
	private static int[] rotate(int[] pixels, int width, int height, double angle){
		int[] result = new int[width * height]; 
		
		double nx_x = rot_x(-angle, 1.0, 0.0);
		double nx_y = rot_y(-angle, 1.0, 0.0);
		double ny_x = rot_x(-angle, 0.0, 1.0);
		double ny_y = rot_y(-angle, 0.0, 1.0);
		
		double x0 = rot_x(-angle, -width / 2.0, -height / 2.0) + width / 2.0;
		double y0 = rot_y(-angle, -width / 2.0, -height / 2.0) + height / 2.0;
		
		for(int y = 0; y < height; y++){
			double x1 = x0;
			double y1 = y0;
			for(int x = 0; x < width; x++){
				int xx = (int) x1;
				int yy = (int) y1;
				int col = 0;
				if(xx < 0 || xx>= width || yy < 0 || yy >= height) col = 0x0; 
				else col = pixels[xx + yy * width];
				result[x + y * width] = col;
				x1 += nx_x;
				y1 += nx_y;
			}
			x0 += ny_x;
			y0 += ny_y;
		}	
		return result;
	}
	
	private static double rot_x(double angle, double x, double y){
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * cos - y * sin;
	}
	
	private static double rot_y(double angle, double x, double y){
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * sin + y * cos;
	}
	
	
	public static void main(String[] args) {
		new Asteroids();
	}
}
