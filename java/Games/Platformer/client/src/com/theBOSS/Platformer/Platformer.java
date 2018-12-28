package com.theBOSS.Platformer;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.theBOSS.Platformer.entity.Enemy;
import com.theBOSS.Platformer.graphics.Sprite;
import com.theBOSS.Platformer.input.Keyboard;
import com.theBOSS.Platformer.input.Mouse;
import com.theBOSS.Platformer.level.Level;

public class Platformer extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 400, HEIGHT = WIDTH * 3 / 4, SCALE = 2;	
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	
	BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	public static Screen screen;
	private Keyboard key;
	private Mouse mouse;
	private Level level;
	
	Client client;
	public static Enemy enemy;	
	
	public Platformer() {
		frame = new JFrame("Platformer");
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		
		screen = new Screen(WIDTH, HEIGHT);
		key = new Keyboard();
		mouse = new Mouse();
		addKeyListener(key);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		level = Level.level1;
		
		// multiplayer stuff
		
		client = new Client("192.168.1.77", 1111);
		client.connect();
		
		enemy = new Enemy(100, 100, Sprite.player);
		
	}
	
	public void start() {
		running = true;
		thread = new Thread(this);
		thread.run();
	}
	
	public void stop() {
		if(!running) return;
		try {
			thread.join();
			running = false;
		}catch(InterruptedException e) {
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
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;

			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frame.setTitle("Platformer | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}	
	
	public void update() {
		level.update();
		
		mouse.update();
		key.update();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) { createBufferStrategy(3); return; }
		
		screen.clear();
		level.render();
		screen.process();
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Platformer p = new Platformer();
		p.frame.setResizable(false);
		p.frame.add(p);
		p.frame.pack();
		p.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p.frame.setLocationRelativeTo(null);
		p.frame.setVisible(true);
		p.frame.setAlwaysOnTop(true);
		p.start();
	}

}
