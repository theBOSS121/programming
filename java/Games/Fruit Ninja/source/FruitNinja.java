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

public class FruitNinja {
	//game is not finised
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private JFrame frame;
	private Renderer renderer;
	private boolean running = true, started = false, valid = false;
	public static BufferedImage bg, f1, f2, f3, f4;
	private Random rand = new Random();
	private Mouse mouse = new Mouse();
	private static boolean gameOver;
	private static int lives = 3;
	
	public static List<Fruit> fruits = new ArrayList<Fruit>();
	public static List<Integer> x = new ArrayList<Integer>();
	public static List<Integer> y = new ArrayList<Integer>();

	public FruitNinja() {
		frame = new JFrame("Fruit Ninja");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(WIDTH + 7, HEIGHT + 30);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		renderer = new Renderer();
		frame.add(renderer);
		frame.addMouseListener(mouse);
		frame.addMouseMotionListener(mouse);
		frame.setLocationRelativeTo(null);
		try {
			bg = ImageIO.read(Renderer.class.getResource("/bg.png"));
			f1 = ImageIO.read(Renderer.class.getResource("/fruit1.png"));
			f2 = ImageIO.read(Renderer.class.getResource("/fruit2.png"));
			f3 = ImageIO.read(Renderer.class.getResource("/fruit3.png"));
			f4 = ImageIO.read(Renderer.class.getResource("/fruit4.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		gameLoop();		
	}
	
	
	private void gameLoop() {
		double delta = 1000.0 / 60.0;
		double deltaF = 1000.0 / 100.0;
		double lastTime = System.currentTimeMillis();
		double currentTime;
		int updates = 0;
		int frames = 0;
		
		while(running ) {
			currentTime = System.currentTimeMillis();
			if(currentTime - lastTime > 1000.0) {
				frame.setTitle("Fruit Ninja " + updates + ", " + frames);
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
		if(!gameOver) {
			if(!started) {
				for(int i = 0 ; i < rand.nextInt(4) + 1; i++) {
					fruits.add(new Fruit(rand.nextInt(WIDTH * 2) - WIDTH / 2, HEIGHT + 100, rand.nextDouble() / 6, rand.nextInt(4)));
				}
				started = true;
			}
			
			for(int i = 0; i < fruits.size(); i++) {
				fruits.get(i).update();
				if(!fruits.get(i).cuted && fruits.get(i).in && fruits.get(i).y > WIDTH && !fruits.get(i).removed) {
					fruits.get(i).removed = true;
					lives--;
				}
				if(fruits.get(i).in && fruits.get(i).y > WIDTH) {
					fruits.get(i).removed = true;
				}
			}
			
			if(Mouse.dragging) {
				Mouse.dragging = false;
			}
			
			if(!Mouse.dragging && x.size() > 0) {
				x.remove(0);
				y.remove(0);
			}
			
			if(Mouse.pressed) {
				collision();
			}			
			
			for(int i = 0; i < fruits.size(); i++) {
				if(fruits.get(i).removed) {
					fruits.remove(i);
				}
			}
			
			if(started && rand.nextInt(120) == 0) {
				started = false;
			}
			
			if(lives <= 0) {
				gameOver = true;
			}	
		}else{
			if(!Mouse.pressed) {
				valid = true;
			}
			if(valid) {
				if(Mouse.pressed) {
					gameOver = false;
					lives = 3;
					started = true;
					fruits = new ArrayList<Fruit>();
				}
			}
		}
	}


	private void collision() {
		for(int i = 0; i < fruits.size(); i++) {
			Fruit f = fruits.get(i);
			if(!f.cuted) {
				if(Mouse.getX() > f.x && Mouse.getX() < f.x + 100 && Mouse.getY() > f.y && Mouse.getY() < f.y + 100) {
					f.cuted = true;
				}			
			}
		}
	}


	public static void render(Graphics g) {
		g.drawImage(bg, 0, 0, 801, 601, null);	
		
		for(int i = 0; i < fruits.size(); i++) {
			if(!fruits.get(i).cuted) {
				g.drawImage(fruits.get(i).image, (int) fruits.get(i).x, (int) fruits.get(i).y, null);
			}else {
				g.drawImage(fruits.get(i).image, (int) fruits.get(i).x + 50, (int) fruits.get(i).y - 20, 50, 50, null);
				g.drawImage(fruits.get(i).image, (int) fruits.get(i).x - 50, (int) fruits.get(i).y + 10, 50, 50, null);
			} 
		}
		
		for(int i = 0; i < x.size(); i++) {
			g.setColor(Color.GREEN);
			g.fillRect(x.get(i).intValue(), y.get(i).intValue(), 8, 8);
		}	
		
		if(gameOver) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Ariel", 1, 100));
			g.drawString("Game Over", 130, 100);
		}
		
	}

	public static void main(String[] args) {
		new FruitNinja();
	}
}
