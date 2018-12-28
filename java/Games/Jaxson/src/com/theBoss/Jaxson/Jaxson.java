package com.theBoss.Jaxson;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.theBoss.Jaxson.UI.StartMenu;
import com.theBoss.Jaxson.entity.Cloud;
import com.theBoss.Jaxson.entity.Enemy;
import com.theBoss.Jaxson.entity.Player;
import com.theBoss.Jaxson.graphics.Screen;
import com.theBoss.Jaxson.graphics.Sprite;
import com.theBoss.Jaxson.input.Keyboard;
import com.theBoss.Jaxson.input.Mouse;
import com.theBoss.Jaxson.level.Level;

public class Jaxson extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	
	public static int width = 400, height = 250, scale = 2;
	private JFrame frame;
	private Thread thread;
	private boolean running = false;
	public static boolean menu = false, changedMenu = false;
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	private Random rand = new Random();
	
	private Screen screen;
	private Level level;
	private Player player;
	private Keyboard key;
	private Mouse mouse;
	private Cloud[] clouds = new Cloud[2];
	private StartMenu sm;
	
	public Jaxson(){
		sm = new StartMenu();
		setPreferredSize(new Dimension(width * scale, height * scale));
		screen = new Screen(width, height);
		frame = new JFrame();
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		key = new Keyboard();
		addKeyListener(key);
		screen.setOffset(0, 10);
		level = Level.startLevel;
		for(int i = 0; i < clouds.length; i++) {
			int side = rand.nextInt(2);
			if(side == 0) {
				clouds[i] = new Cloud(rand.nextInt(470) - 70, rand.nextInt(100) + 20, rand.nextDouble()* 0.5 + 0.01, Sprite.cloud);
			}
			if(side == 1) {
				clouds[i] = new Cloud(rand.nextInt(470) - 70, rand.nextInt(100) + 20, -(rand.nextDouble() * 0.5 + 0.01), Sprite.cloud);
			}
			level.add(clouds[i]);
		}
		player = new Player(20 * 16 - Sprite.player_still.width / 2, 50, Sprite.player_still, key, level);
		level.add(player);
		screen.setOffset((int) -player.x + width / 2 - player.width / 2, -6);
		Enemy e = new Enemy(100, level.height * 16 - (16 * 3 + 12), Sprite.bee, level, 0);
		level.add(e);
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
				frame.setTitle("Jaxson | " + updates + " ups, " + frames +" fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	private void update() {
		level.update();
		key.update();
		if(key.menu && !changedMenu) {
			menu = !menu;
			changedMenu = true;
		}
		if(changedMenu && !key.menu) {
			changedMenu = false;
		}
		if(menu) sm.update(getWidth(), getHeight());
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(4);
			return;
		}
		screen.clear();
		if(-player.x + width / 2 - player.width / 2 < 0 && (-width + -player.x + width / 2 - player.width / 2) * -1 < level.width * 16) {
			screen.setOffset((int) -player.x + width / 2 - player.width / 2, -6);
		}
		
		level.render(screen);
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		if(menu) sm.render((Graphics2D) g);
	 
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Jaxson game = new Jaxson();
		game.frame.setVisible(true);
		game.frame.add(game); 
		game.frame.setTitle("Jaxson");
		try {
			game.frame.setIconImage(ImageIO.read(Jaxson.class.getResource("/icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		game.frame.pack();
		game.frame.setLocationRelativeTo(null);
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.start();
	}
}
