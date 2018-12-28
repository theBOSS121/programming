package theBOSS.Avoid;

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

import theBOSS.Avoid.entity.Entity;
import theBOSS.Avoid.entity.Mob;
import theBOSS.Avoid.entity.Mob2;
import theBOSS.Avoid.entity.Mob3;
import theBOSS.Avoid.entity.Mob4;
import theBOSS.Avoid.entity.Player;
import theBOSS.Avoid.graphics.Screen;
import theBOSS.Avoid.graphics.Sprite;
import theBOSS.Avoid.input.Keyboard;
import theBOSS.Avoid.input.Mouse;

public class Avoid extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public final static int width = 600, height = 350, scale = 1;
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private Screen screen;
	public static Keyboard key;
	public Mouse mouse;
	
	private Random rand = new Random();

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Player player;

	private List<Entity> mobs = new ArrayList<Entity>();
	
	private boolean spawnNewMob = true;
	private boolean gameOver = false;
	
	int time = 0;

	public Avoid() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		addKeyListener(key);
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);

		player = new Player(width / 2 - Sprite.blue_ball.width / 2, height / 2 - Sprite.blue_ball.height, Sprite.blue_ball);
	}

	private synchronized void start() {
		running = true;
		thread = new Thread(this, "gameThread");
		thread.start();
	}

	private synchronized void stop() {
		try {
			thread.join();
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
				frame.setTitle("Avoid | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	private void update() {
		key.update();
		player.update();
		for(int i = 0; i < mobs.size(); i++) {
			mobs.get(i).update();
		}
		if(time % (60 * 16) == 0) {
			spawnNewMob = true;
		}
		if(spawnNewMob) {
			spawn();
			spawnNewMob = false;
		}
		collision();
		
		if(gameOver) {
			restart();
		}
		
		time++;
	}

	private void restart() {
		mobs = new ArrayList<Entity>();
		gameOver = false;
		time = 0;
		spawnNewMob = true;
	}

	private void collision() {
		for(int i = 0; i < mobs.size(); i++) {
			Entity e = mobs.get(i);
			if(player.x + player.width > e.x && player.x < e.x + e.width && player.y + player.height > e.y && player.y < e.y + e.height && e.hitable && player.hitable) {
				gameOver = true;
			}
		}
	}

	private void spawn() {
		int which = rand.nextInt(4);
		int xPosible = width - Sprite.orange_ball.width;
		int yPosible = height - Sprite.orange_ball.height;
		if(which == 0) {		
			mobs.add(new Mob(rand.nextInt(xPosible), rand.nextInt(yPosible), Sprite.orange_ball));
		}else if(which == 1) {
			mobs.add(new Mob2(rand.nextInt(xPosible), rand.nextInt(yPosible), Sprite.green_ball));
		}else if(which == 2) {
			mobs.add(new Mob3(rand.nextInt(xPosible), rand.nextInt(yPosible), Sprite.purple_ball));
		}else if(which == 3) {
			mobs.add(new Mob4(rand.nextInt(xPosible), rand.nextInt(yPosible), Sprite.yellow_ball, player));
		}
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.clear();
		player.render(screen);
		for(int i = 0; i < mobs.size(); i++) {
			mobs.get(i).render(screen);
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
		Avoid avoid = new Avoid();
		avoid.frame.setVisible(true);
		avoid.frame.setResizable(false);
		avoid.frame.add(avoid);
		avoid.frame.setTitle("Avoid");
		avoid.frame.pack();
		avoid.frame.setLocationRelativeTo(null);
		avoid.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		avoid.start();
	}

}
