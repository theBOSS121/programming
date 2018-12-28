package com.theBOSS.Nalis_pc;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.theBOSS.Nalis_pc.game.Game;
import com.theBOSS.Nalis_pc.graphics.Screen;
import com.theBOSS.Nalis_pc.input.Mouse;

public class Nails extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 100, HEIGHT = 160, SCALE = 6;
	private Thread thread;
	private boolean running = false;
	private JFrame frame;
	public static Screen screen;
	private Mouse mouse;
	private Game game;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	public Nails() {
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		screen = new Screen(WIDTH, HEIGHT);
		frame = new JFrame("Nails");
		
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		game = new Game();
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
				frame.setTitle("Nails | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	private void update() {
		game.update();
		mouse.update();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) { createBufferStrategy(3); return; }
		
		screen.clear();
		game.render();
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
		Nails n = new Nails();
		n.frame.setResizable(false);
		n.frame.add(n);
		n.frame.pack();
		n.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		n.frame.setLocationRelativeTo(null);
		n.frame.setVisible(true);
		n.frame.setAlwaysOnTop(true);
		n.start();
	}	
}
