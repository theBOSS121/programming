package com.theBoss.Runner;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.theBoss.Runner.Graphics.Screen;
import com.theBoss.Runner.entity.Entity;
import com.theBoss.Runner.input.Keyboard;

public class Runner extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private int width = 700, height = 600;
	private int scale = 1;
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private Screen screen;
	private Keyboard key;
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public static double leftRight = 0.0, lastLeftRight = 0.0;
	private boolean left = false, right = false, up = false, down = false, jump = false, gameOver = false;
	private int lastMove = -1;// 0-left 1-right 2-up 3-down
	public static double floorPosition = 8.0;
	public static double ceilingPosition = 8.0;
	public static double time = 0.0;
	public static double speed = 1.0;
	
	private List<Entity> entities;
	private Random rand = new Random();
	
	public Runner() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		frame = new JFrame();
		key = new Keyboard();
		addKeyListener(key);
		screen = new Screen(width, height);
		setEntities();
	}
	private void setEntities() {
		entities = new ArrayList<Entity>();
		for(int i = 0; i < 10; i++) {
			int j = rand.nextInt(6);
			int b = rand.nextInt(6);
			for(int a = 0; a < 6; a++) {
				if(a == j || a == b) continue;
				if(a < 3) {
					entities.add(new Entity(-36.5 + a * 29.0, -15.0, 253.0 + i * 59.3, 15.0, 15.0, 5.0));
				}
				if(a > 2) {
					entities.add(new Entity(-36.5 + (a - 3) * 29.0, 0.0, 253.0 + i * 59.3, 15.0, 15.0, 5.0));
				}
			}
		}	
	}
/*
	entities.add(new Entity(-7.5, -15.0, 253.0, 15.0, 15.0, 5.0));
	entities.add(new Entity(-36.5, -15.0, 253.0, 15.0, 15.0, 5.0));
	entities.add(new Entity(21.5, -15.0, 253.0, 15.0, 15.0, 5.0));
	entities.add(new Entity(-7.5, -15.0, 253.0, 15.0, 15.0, 5.0));
	entities.add(new Entity(-36.5, -15.0, 253.0, 15.0, 15.0, 5.0));
	entities.add(new Entity(21.5, -15.0, 253.0, 15.0, 15.0, 5.0));
*/

	private synchronized void start(){
		running = true;
		thread = new Thread(this, "game");
		thread.start();
	}
	
	private synchronized void stop() {
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
		render();
		render();
		render();
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
				frame.setTitle("Runner | " + updates + " ups, " + frames +" fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}	

	private void update() {
		time += 0.4 * speed;
		speed += 0.0001;
		key.update();
		movement();
		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(e.z <= 0 && e.z + e.lenght >= 0) {
				if(e.x == -36.5 && leftRight <= -8.0 && e.y == -15.0 && floorPosition >= 8.0) {
					gameOver = true;
				}
				if(e.x == -7.5 && (leftRight <= 8.0 && leftRight >= -8.0) && e.y == -15.0 && floorPosition >= 8.0) {
					gameOver = true;
				}
				if(e.x == 21.5 && leftRight >= 8.0 && e.y == -15.0 && floorPosition >= 8.0) {
					gameOver = true;
				}
				if(e.x == -36.5 && leftRight <= -8.0 && e.y == 0.0 && floorPosition <= 8.0) {
					gameOver = true;
				}
				if(e.x == -7.5 && (leftRight <= 8.0 && leftRight >= -8.0) && e.y == 0.0 && floorPosition <= 8.0) {
					gameOver = true;
				}
				if(e.x == 21.5 && leftRight >= 8.0 && e.y == 0.0 && floorPosition <= 8.0) {
					gameOver = true;
				}
			}
		}
		
		if(gameOver) {
			setEntities();
			leftRight = 0.0;
			floorPosition = 8.0;
			gameOver = false;
			speed = 1.0;
			time = 0.0;
		}
	}

	
	private void movement() {
		if(lastMove != 0) {
			if(key.left && !left && leftRight > -8.0) {
				left = true;
				lastMove = 0;
				if(!right) {
					lastLeftRight = leftRight;
				}else {
					lastLeftRight = lastLeftRight + 16.0;
					right = false;
				}
			}
		}else {
			if(!key.left) {
				lastMove = -1;
			}
		}
		if(lastMove != 1) {
			if(key.right && !right && leftRight < 8.0) {
				right = true;
				lastMove = 1;
				if(!left) {
					lastLeftRight = leftRight;
				}else {
					lastLeftRight = lastLeftRight - 16.0;
					left = false;
				}
			}			
		}else {
			if(!key.right) {
				lastMove = -1;
			}
		}
		if(lastMove != 3) {
			if(key.down && !down) {
				lastMove = 3;
				down = true;
				up = false;
				jump = false;
			}			
		}else {
			if(!key.down) {
				lastMove = -1;
			}
		}
		if(lastMove != 2) {
			if(key.up && !up && !jump) {
				lastMove = 2;
				up = true;
				jump = true;
				down = false;
			}					
		}else {
			if(!key.up && !jump) {
				lastMove = -1;
			}
		}
		if(left) {
			leftRight -= 1.0  * speed;
			if(lastLeftRight - leftRight >= 16.0) {
				leftRight = lastLeftRight - 16.0;
				left = false;
			}
		}
		if(right) {
			leftRight += 1.0 * speed;
			if(lastLeftRight - leftRight <= -16.0) {
				leftRight = lastLeftRight + 16.0;
				right = false;
			}
		}
		if(down) {
			if(speed < 1.7) {
				floorPosition *= 0.94;
			}else {
				floorPosition *= 0.9;
			}
			if(floorPosition < 2.0) {
				down = false;
			}
		}
		if(up) {
			if(speed < 1.7) {
				floorPosition *= 1.05;
			}else {
				floorPosition *= 1.12;
			}
			if(floorPosition > 30.0) {
				up = false;
			}
		}
		if(!down && !up) {
			if(floorPosition < 8.0) {
				if(speed < 1.7) {
					floorPosition *= 1.07;
				}else {
					floorPosition *= 1.1;
				}
				if(floorPosition > 8.0) {
					floorPosition = 8.0;
				}
			}
			if(floorPosition > 8.0) {
				if(speed < 1.7) {
					floorPosition *= 0.95;
				}else {
					floorPosition *= 0.94;
				}
				if(floorPosition < 8.0) {
					floorPosition = 8.0;
					jump = false;
				}
			}
		}
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		screen.clear();
		screen.render();

		for(int i = 0; i < entities.size(); i++) {
			entities.get(i).render(screen);
		}
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Runner game = new Runner();
		game.frame.setResizable(false);
		game.frame.setTitle("Runner");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setVisible(true);
		game.frame.setLocationRelativeTo(null);
		game.start();
	}	
}
