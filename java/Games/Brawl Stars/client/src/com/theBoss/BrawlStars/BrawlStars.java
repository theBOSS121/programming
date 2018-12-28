package com.theBoss.BrawlStars;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.theBoss.BrawlStars.entity.Mob.Player;
import com.theBoss.BrawlStars.graphics.Screen;
import com.theBoss.BrawlStars.input.Keyboard;
import com.theBoss.BrawlStars.input.Mouse;
import com.theBoss.BrawlStars.level.Level;
import com.theBoss.BrawlStars.net.Client;

public class BrawlStars extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	
	public static int width = 300;
	public static int height = 350;
	public static int scale = 2;
	
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	public static boolean started = false;
	
	public static Client client;
	private Screen screen;
	private Keyboard key;
	private Mouse mouse;
	public static Level level;
	public static Player player;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	public BrawlStars() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		level = new Level("/level.png");
		screen = new Screen(width, height, level);
		frame = new JFrame();
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		key = new Keyboard();
		addKeyListener(key);
		player = new Player(32 * 4 + 16, 32 + 16, 2.0, 32, 32, key, mouse, level);
		client = new Client("localhost", 8192, level);
		client.connect();
	}
	
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop() {
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
				if(started) {
					update();
					updates++;
				}	
					delta--;
			}
			if(started) {
				render();
				frames++;
			}
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				frame.setTitle("Brawl Stars | " + updates + " ups, " + frames +" fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void update() {
		key.update();
		player.update();
		if(client.connected) {
			String s = "/s/" + client.opponentsID + "/id/" + (int) player.x + "/x/" + (int) player.y + "/y/" + player.angle + "/e/";
			client.send(s.getBytes());
		}
		if(client.opponent != null) {
			client.opponent.update();
		}
				
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		screen.setOffset((int) player.x - width / 2 + player.width / 2, (int) player.y - height / 2 + player.height / 2);
		screen.render();
		if(client.opponent != null) {
			if(client.opponent.safe != 0 && System.currentTimeMillis() % 700 < 350) {				
				screen.renderOpponnent(client.opponent);
				screen.renderHealthBar(client.opponent.x, client.opponent.y - 12, client.opponent.life / 100.0);
			}
			if(client.opponent.safe == 0) {
				screen.renderOpponnent(client.opponent);		
				screen.renderHealthBar(client.opponent.x, client.opponent.y - 12, client.opponent.life / 100.0);
			}
			for(int i = 0; i < client.opponent.bullets.size(); i++) {
				screen.renderBullet((int) client.opponent.bullets.get(i).x,(int) client.opponent.bullets.get(i).y, client.opponent.bullets.get(i));
			}
		}
		player.render(screen);
		
		for(int i = 0 ; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		BrawlStars brawlStars = new BrawlStars();
		brawlStars.frame.setResizable(false);
		brawlStars.frame.setTitle("Brawl Stars");
		brawlStars.frame.add(brawlStars);
		brawlStars.frame.pack();
		brawlStars.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		brawlStars.frame.setVisible(true);
		brawlStars.frame.setLocationRelativeTo(null);
		brawlStars.start();
	}
	
}






















