package com.theBoss.EndlessCubes;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.theBoss.EndlessCubes.UI.Menu;
import com.theBoss.EndlessCubes.entity.Entity;
import com.theBoss.EndlessCubes.graphics.Screen;
import com.theBoss.EndlessCubes.input.Keyboard;
import com.theBoss.EndlessCubes.input.Mouse;

public class EndlessCubes extends Canvas implements Runnable{
	private static final long serialVersionUID = 276211587502757488L;
	
	private static final int resolution = 600;
	private static int width = resolution, height = width, scale = 1;

	public static double leftRight = 0.0, floorPosition = 4.0, ceilingPosition = 4.0, speed = 0.1; 
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private Screen screen;
	private Keyboard key;
	private Mouse mouse;
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private boolean left = false, right = false, down = false, up = false, poused = false, pouseBuffer = false, pouseBuffer2 = false;

	private static Random rand = new Random();

	public static boolean gameOver = true, m = true, transparency = false;
	private static int pouseSize;
	
	public Menu menu = new Menu();
		
	public static List<Entity> entities;

	public static int bestScore, score = 0;
	
	public static Configuration config = new Configuration();
	
	public EndlessCubes() {
		setPreferredSize(new Dimension(width * scale, height * scale));
		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		addKeyListener(key);
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		config.loadConfiguration("config.xml");
	}
	
	public static void startGame() {
		entities = new ArrayList<Entity>();
		double x = 0.0, y = 0.0;
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 3; j++) {
				int a = rand .nextInt(4);
				if(a == 0) {
					x = -7.0;
					y = 0.0;
				}else if(a == 1) {
					x = 0.0;
					y = 0.0;
				}else if(a == 2) {
					x = -7.0;
					y = 7.15;
				}else {
					x = 0.0;
					y = 7.15;
				}
				entities.add(new Entity(x, y, 31.3 + i * 15.4, 7.0, 7.15, 3.9, 0x8000ff));
			}
		}	
		leftRight = 0.0;
		floorPosition = 4.0;
		speed = 0.1;
		score = 0;
		gameOver = false;
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}

	private void start() {
		running = true;
		thread = new Thread(this, "GameThread");
		thread.start();
	}
	
	private void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		while (running) {
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				frame.setTitle("Endless Cubes | " + updates + " ups, " + frames +" fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	private void update() {
		key.update();
		if(!gameOver) {
			pouseCheck();
			if(!poused) {
				m = false;
				for(int i = 0; i < entities.size(); i++) {
					entities.get(i).update();
				}
				collision();
			
				input();
				if(speed <= 0.6) {
					speed += 0.00005;
				}
			}else {
				menu.state = 2;
				m = true;
			}
		}
		if(gameOver) {
			if(key.restart) {
				startGame();
				m = false;
			}
		}
		if(m) {
			menu.update(getWidth(), getHeight(), width);
		}
	}
	
	private void pouseCheck() {
		pouseSize = 64 * width / resolution;
		if(key.pouse && !pouseBuffer) pouseBuffer = true;
		if(pouseBuffer && !key.pouse) {
			poused = !poused;
			pouseBuffer = false;
		}
		
		if(Mouse.getX() > width + (getWidth() - width) / 2 - pouseSize && Mouse.getX() < width + (getWidth() - width) / 2 && Mouse.getY() > 0 && Mouse.getY() < pouseSize && Mouse.getButton() == 1 && !pouseBuffer2) {
			pouseBuffer2 = true;
		}
		if(Mouse.getX() > width + (getWidth() - width) / 2 - pouseSize && Mouse.getX() < width + (getWidth() - width) / 2 && Mouse.getY() > 0 && Mouse.getY() < pouseSize && Mouse.getButton() == 0 && pouseBuffer2) {
			poused = !poused;
			pouseBuffer2 = false;
		}
	}
	
	private void collision() {
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(e.z <= 0 && e.z + e.lenght >= 0) {
				if(e.x == -7.0 && leftRight <= 0.0 && e.y == 0.0 && floorPosition >= 4.0) {
					gameOver  = true;
					m = true;
					menu.state = 2;
					if(score / 3 > bestScore) {
						bestScore = score / 3;
						config.saveConfiguration("best", bestScore);
					}
				}
				
				if(e.x == 0.0 && leftRight >= 0.0 && e.y == 0.0 && floorPosition >= 4.0) {
					gameOver = true;
					m = true;
					menu.state = 2;
					if(score / 3 > bestScore) {
						bestScore = score / 3;
						config.saveConfiguration("best", bestScore);
					}
				}
				if(e.x == -7.0 && leftRight <= 0.0 && e.y == 7.15 && floorPosition <= 4.0) {
					gameOver = true;
					m = true;
					menu.state = 2;
					if(score / 3 > bestScore) {
						bestScore = score / 3;
						config.saveConfiguration("best", bestScore);
					}
				}
				if(e.x == 0.0 && leftRight >= 0.0 && e.y == 7.15 && floorPosition <= 4.0) {
					gameOver = true;
					m = true;
					menu.state = 2;
					if(score / 3 > bestScore) {
						bestScore = score / 3;
						config.saveConfiguration("best", bestScore);
					}
				}
			}
		}
		
	}

	private void input() {
		if(key.left) {
			left = true;
			right = false;
		}
		if(key.right) { 
			right = true;
			left = false;
		}
		if(key.down) {
			down = true;
			up = false;
		}
		if(key.up) { 
			up = true;
			down = false;
		}
		if(key.left && key.right) {
			left = false;
			right = false;
		}
		if(key.down && key.up) {
			up = false;
			down = false;
		}
		if(left) {
			if(leftRight > -2.0) {
				leftRight -= 0.4;
			}else {
				leftRight = -2.0;
				left = false;
				right = false;
			}
		}
		if(right) {
			if(leftRight < 2.0) {
				leftRight += 0.4;
			}else {
				leftRight = 2.0;
				left = false;
				right = false;
			}
		}
		if(down) {
			if(floorPosition > 2.0) {
				floorPosition -= 0.4;
			}else {
				floorPosition = 2.0;
				up = false;
				down = false;
			}
		}
		if(up) {
			if(floorPosition < 6.0) {
				floorPosition += 0.4;
			}else {
				floorPosition = 6.0;
				up = false;
				down = false;
			}
		}
		ceilingPosition = 8.0 - floorPosition;
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		screen.render();
		screen.renderDistanceLimiter();
		if(!gameOver) {
			if(!transparency) entities.sort(Comparator.comparingDouble(Entity::getZ).reversed());
			else entities.sort(Comparator.comparingDouble(Entity::getZ));
			for(int i = 0; i < entities.size(); i++) {
				entities.get(i).render(screen);
			}
			screen.renderPouse();
		}
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		Font f = new Font("Ariel", 1, (int) (width * 0.2));
		g.setFont(f);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		if(getWidth() >= getHeight()) {
			g.drawImage(image, (getWidth() - getHeight()) / 2, 0, getHeight(), getHeight(), null);
			width = getHeight();
			height = width;
		}
		else {
			g.drawImage(image, 0, (getHeight() - getWidth()) / 2, getWidth(), getWidth(), null);
			width = getWidth();
			height = width;
		}
		if(!gameOver || (gameOver && menu.state == 2)) {
			g.setColor(new Color(menu.col));
			String s = score / 3 + "";
			int ws = g.getFontMetrics().stringWidth(s) / 2;
			g.drawString(s, getWidth() / 2 - ws, getHeight() - width + width / 5);
		}
		if(m) menu.render(g);
		g.dispose();
		bs.show();		
	}
	
	public static void main(String[] args) {
		EndlessCubes game = new EndlessCubes();
		game.frame.setVisible(true);
		try {
			game.frame.setIconImage(ImageIO.read(EndlessCubes.class.getResource("/icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		game.frame.add(game); 
		game.frame.setTitle("Endless Cubes");
		game.frame.pack();
		game.frame.setLocationRelativeTo(null);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.start();
	}
	
}




