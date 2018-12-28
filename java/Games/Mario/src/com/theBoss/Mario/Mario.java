package com.theBoss.Mario;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.theBoss.Mario.entity.player.Player;
import com.theBoss.Mario.graphics.Screen;
import com.theBoss.Mario.graphics.Sprite;
import com.theBoss.Mario.input.Keyboard;
import com.theBoss.Mario.level.Level;

public class Mario extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private int width = 384, height = 224, scale = 3;
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private Screen screen;
	private Keyboard key;
	private Level level;
	private Player player;
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	public Mario() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		addKeyListener(key);
		level = Level.level1;
		player = new Player(50.0, 100.0, Sprite.player_still, key, level);
		level.add(player);
	}
	
	private synchronized void start() {
		running = true;
		thread = new Thread(this, "gameThread");
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
				frame.setTitle("Game Engine | " + updates + " ups, " + frames +" fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	
	private void update() {
		key.update();
		level.update();
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.setOffset((int) (player.x - width / 2), 0);
		screen.clear();
		level.render(screen);
	
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, width * scale, height * scale, null);
		
		g.dispose();
		bs.show();
	}	

	public static void main(String[] args) {
		Mario mario = new Mario();
		mario.frame.setResizable(false);
		mario.frame.setTitle("Mario");
		mario.frame.add(mario);
		mario.frame.pack();
		mario.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mario.frame.setVisible(true);
		mario.frame.setLocationRelativeTo(null);
		mario.start();
	}
}
