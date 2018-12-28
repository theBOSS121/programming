package com.theBOSS.EndlessShooter;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

import com.theBOSS.EndlessShooter.entity.Bot;
import com.theBOSS.EndlessShooter.entity.Bullet;
import com.theBOSS.EndlessShooter.entity.Plane;
import com.theBOSS.EndlessShooter.entity.Player;
import com.theBOSS.EndlessShooter.graphics.Screen;
import com.theBOSS.EndlessShooter.graphics.Sprite;
import com.theBOSS.EndlessShooter.input.Focus;
import com.theBOSS.EndlessShooter.input.Keyboard;
import com.theBOSS.EndlessShooter.input.Mouse;

public class EndlessShooter extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public final static int WIDTH = 600;
	public final static int HEIGHT = 600;
	public static int scale = 1;
	public static boolean focus = false;
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private Screen screen;
	private Keyboard key;
	private Mouse mouse;
	private Focus f;

	private Random rand = new Random();

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	private int updates = 0;
	private int time = 1;
	private Player player;

	private List<Plane> planes = new ArrayList<Plane>();
	private List<Bot> bots = new ArrayList<Bot>();

	private int planeNum = 3;
	private int botNum = 1;
	private int reSpawn = 600;
	private boolean gameOver = false;
	public static int state = 0;
	
	public static int bestScore = 0, score = 0;

	boolean hPlay = false, hSettings = false, hHelp = false, hCredits = false, hExit = false, hMusic = false, hSounds = false, hBack = false;
	
	Configuration config = new Configuration();
	
	public static boolean music = true, sounds = true;
	
	private Clip clip;
	
	public EndlessShooter() {
		Dimension size = new Dimension(WIDTH * scale, HEIGHT * scale);
		setPreferredSize(size);
		setMinimumSize(size);
		setMinimumSize(size);
		screen = new Screen(WIDTH, HEIGHT);
		frame = new JFrame();
		key = new Keyboard();
		mouse = new Mouse();
		f = new Focus();
		addKeyListener(key);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		addFocusListener(f);
		config.loadConfiguration("config.xml");
		if(music) {
			startMusic();
		}
	}
	
	private void startMusic() {
		try {
			URL url = this.getClass().getClassLoader().getResource("avoid.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);			
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-10.0f);
		}catch(UnsupportedAudioFileException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}catch(LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		running = true;
		thread = new Thread(this, "gameThread");
		thread.start();
	}

	public void stop() {
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
				frame.setTitle("Endless Shooter | " + updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	private void spawn() {
		int where = rand.nextInt(4);
		double dir = 0;
		if(where == 0) {
			dir = -Math.PI / 2 + Math.PI / 6;
			dir += rand.nextInt(120) / (180 / Math.PI);
			planes.add(new Plane(-32, HEIGHT / 8 + rand.nextInt(HEIGHT / 8 * 6), Sprite.plane, dir));
		}
		if(where == 1) {
			dir = Math.PI / 6;
			dir += rand.nextInt(120) / (180 / Math.PI);
			planes.add(new Plane(WIDTH / 8 + rand.nextInt(WIDTH / 8 * 6), -32, Sprite.plane, dir));
		}
		if(where == 2) {
			dir = Math.PI / 2 + Math.PI / 6;
			dir += rand.nextInt(120) / (180 / Math.PI);
			planes.add(new Plane(WIDTH + 32, HEIGHT / 8 + rand.nextInt(HEIGHT / 8 * 6), Sprite.plane, dir));
		}
		if(where == 3) {
			dir = Math.PI + Math.PI / 6;
			dir += rand.nextInt(120) / (180 / Math.PI);
			planes.add(new Plane(WIDTH / 8 + rand.nextInt(WIDTH / 8 * 6), HEIGHT + 32, Sprite.plane, dir));
		}
	}

	private void updateEnemies() {
		for(int i = 0; i < planes.size(); i++) {
			Plane p = planes.get(i);
			p.update();
			if(p.x + p.width > player.x && p.x < player.x + player.width && p.y + p.height > player.y && p.y < player.y + player.height && !p.remove) {
				if(player.life > 0) player.life -= 10;
				p.remove = true;
				p.rotatedSprite = Sprite.crashedPlane;
			}
			for(int i1 = 0; i1 < player.bullets.size(); i1++) {
				Bullet b = player.bullets.get(i1);
				if(p.x + p.width > b.x && p.x < b.x + b.width && p.y + p.height > b.y && p.y < b.y + b.height && !p.remove) {
					if(player.heal < 100) player.heal++;
					p.remove = true;
					p.rotatedSprite = Sprite.crashedPlane;
				}
			}
		}
		for(int i = 0; i < bots.size(); i++) {
			Bot bot = bots.get(i);
			bot.update();
			for(int i1 = 0; i1 < player.bullets.size(); i1++) {
				Bullet b = player.bullets.get(i1);
				if(bot.x + bot.width > b.x && bot.x < b.x + b.width && bot.y + bot.height > b.y && bot.y < b.y + b.height && bot.life > 0) {
					if(player.heal < 100) player.heal++;
					bot.life -= 10;
					b.remove = true;
				}
			}
			for(int i1 = 0; i1 < bot.bullets.size(); i1++) {
				Bullet b = bot.bullets.get(i1);
				if(player.x + player.width > b.x && player.x < b.x + b.width && player.y + player.height > b.y && player.y < b.y + b.height) {
					player.life -= 5;
					b.remove = true;
				}
			}
		}

		if(bots.size() < botNum && time % reSpawn == 0) {
			bots.add(new Bot(rand.nextInt(WIDTH - Sprite.p_front1.width), rand.nextInt(HEIGHT - Sprite.p_front1.width), Sprite.p_front1, player));
		}
		if(planes.size() < planeNum) {
			spawn();
		}
		for(int i = 0; i < planes.size(); i++) {
			if(planes.get(i).remove && planes.get(i).countDown <= 0) planes.remove(i);
		}
		for(int i = 0; i < bots.size(); i++) {
			if(bots.get(i).remove) bots.remove(i);
		}
	}
	
	private void updateMenu() {		
		if(mouse.isButtonUp(1) && Mouse.getX() > WIDTH / 5 && Mouse.getX() < WIDTH / 5 * 4 && Mouse.getY() > HEIGHT / 16) {
			if(Mouse.getY() < HEIGHT / 16 * 3 + 10) {
				startGame();
			}else if(Mouse.getY() < HEIGHT / 16 * 6 + 10 && Mouse.getY() > HEIGHT / 16 * 4) {
				state = 4;
			}else if(Mouse.getY() < HEIGHT / 16 * 9 + 10 && Mouse.getY() > HEIGHT / 16 * 7) {
				state = 5;
			}else if(Mouse.getY() < HEIGHT / 16 * 12 + 10 && Mouse.getY() > HEIGHT / 16 * 10) {
				state = 6;
			}else if(Mouse.getY() < HEIGHT / 16 * 15 + 10 && Mouse.getY() > HEIGHT / 16 * 13) {
				System.exit(0);
			}			
		}
		hPlay = false;
		hSettings = false;
		hHelp = false;
		hCredits = false;
		hExit = false;
		if(Mouse.getX() > WIDTH / 5 && Mouse.getX() < WIDTH / 5 * 4 && Mouse.getY() > HEIGHT / 16) {
			if(Mouse.getY() < HEIGHT / 16 * 3 + 10) {
				hPlay = true;
			}else if(Mouse.getY() < HEIGHT / 16 * 6 + 10 && Mouse.getY() > HEIGHT / 16 * 4) {
				hSettings = true;
			}else if(Mouse.getY() < HEIGHT / 16 * 9 + 10 && Mouse.getY() > HEIGHT / 16 * 7) {
				hHelp = true;
			}else if(Mouse.getY() < HEIGHT / 16 * 12 + 10 && Mouse.getY() > HEIGHT / 16 * 10) {
				hCredits = true;
			}else if(Mouse.getY() < HEIGHT / 16 * 15 + 10 && Mouse.getY() > HEIGHT / 16 * 13) {
				hExit = true;
			}		
		}
	}
	
	private void updateSettingsMenu() {
		if(mouse.isButtonUp(1) && Mouse.getX() > WIDTH / 10 && Mouse.getX() < WIDTH / 10 * 8 && Mouse.getY() > HEIGHT / 16 * 5) {
			if(Mouse.getY() < HEIGHT / 16 * 7 + 10) {
				if(music) {
					config.saveConfiguration("music", 0);
					music = false;
					clip.stop();
				}else {
					config.saveConfiguration("music", 1);
					music = true;	
					startMusic();
				}
			}else if(Mouse.getY() < HEIGHT / 16 * 10 + 10 && Mouse.getY() > HEIGHT / 16 * 8) {
				if(sounds) {
					config.saveConfiguration("sounds", 0);
					sounds = false;
				}else {
					config.saveConfiguration("sounds", 1);
					sounds = true;					
				}
			}else if(Mouse.getY() < HEIGHT / 16 * 14 + 10 && Mouse.getY() > HEIGHT / 16 * 12) {
				state = 0;
			}		
		}
		hMusic = false;
		hSounds = false;
		hBack = false;
		
		if(Mouse.getX() > WIDTH / 10 && Mouse.getX() < WIDTH / 10 * 8 && Mouse.getY() > HEIGHT / 16 * 5) {
			if(Mouse.getY() < HEIGHT / 16 * 7 + 10) {
				hMusic = true;
			}else if(Mouse.getY() < HEIGHT / 16 * 10 + 10 && Mouse.getY() > HEIGHT / 16 * 8) {
				hSounds = true;
			}else if(Mouse.getY() < HEIGHT / 16 * 14 + 10 && Mouse.getY() > HEIGHT / 16 * 12) {
				hBack = true;
			}		
		}
	}


	private void startGame() {
		player = new Player(WIDTH / 2 - Sprite.p_front1.width / 2, HEIGHT / 2 - Sprite.p_front1.height / 2, Sprite.p_front1, key);
		planeNum = 3;
		botNum = 1;
		reSpawn = 600;
		gameOver = false;
		planes = new ArrayList<Plane>();
		bots = new ArrayList<Bot>();
		time = 1;
		score = 0;
		state = 1;
	}

	private void update() {
		if(state == 0) {
			updateMenu();
		}else if(state == 4) {
			updateSettingsMenu();
		}else if(state == 1 || state == 3) {
			player.update();
			updateEnemies();
			if(time % 600 == 0 && planeNum < 10) planeNum++;
			if(time % 2400 == 0 && botNum < 3) botNum++;
			if(time % 30 == 0) reSpawn--;
			
			time++;
			if(state == 1 && time % 5 == 0) score++;
			if(state == 3) gameOver = true;
		}else if(state == 5 || state == 6) {
			if(mouse.isButtonUp(1)) {
				state = 0;
			}
		}
		if(state == 1) {
			if(key.isKeyUp(KeyEvent.VK_P) || mouse.isButtonUp(3)) {
				state = 2;
			}
		}else if(state == 2) {
			if(key.isKeyUp(KeyEvent.VK_P) || mouse.isButtonUp(1) || mouse.isButtonUp(3)) {
				state = 1;
			}
		}else if(state == 3 && mouse.isButtonUp(1)) {
				state = 0;
		}
		if(gameOver) {
			if(score > bestScore) {
				bestScore = score;
				config.saveConfiguration("best", bestScore);
			}
		}
		//has to be last
		key.update();
		mouse.update();
	}

	double rot = 0.0;
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		screen.clear();
		if(state == 1 || state == 2 || state == 3) {
			if(player.life <= 0) {
				player.render(screen);
			}
			for(int i = 0; i < bots.size(); i++) {
				bots.get(i).render(screen);
			}
			if(player.life > 0) {
				player.render(screen);
			}
			for(int i = 0; i < planes.size(); i++) {
				planes.get(i).render(screen);
			}
		}
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		if(state == 3) {
			rot += 0.003;		
			Sprite s = new Sprite(Sprite.rotate(pixels, WIDTH, HEIGHT, rot), WIDTH, HEIGHT);
			
			for(int i = 0; i < pixels.length; i++) {
				pixels[i] = s.pixels[i];
			}
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(image, 0, 0, WIDTH * scale, HEIGHT * scale, null);
		if(!focus && state == 1) {
			Font font = new Font("Ariel", 0, 100);
			g.setFont(font);
			g.setColor(new Color(0x654321));
			String sf = "Focus is lost";
			int sfw = g.getFontMetrics().stringWidth(sf) / 2;
			if(updates < 40) g.drawString(sf, getWidth() / 2 - sfw, getHeight() / 5 * 2);
		}
		drawStatesStuff(g);
		Font font = new Font("Ariel", 1, 20);
		g.setFont(font);
		g.setColor(Color.BLACK);
		if(state == 1 || state == 2) {
			g.drawString("Life:", 10, HEIGHT / 21 * 19 + 13);
			g.drawString("Heal:", 10, HEIGHT / 21 * 20 + 13);
		}
		g.dispose();
		bs.show();
	}
	
	private void drawStatesStuff(Graphics2D g) {
		if(state == 0) {
			drawMenu(g);			
		}
		if(state == 1) {
			Font font = new Font("Ariel", 1, 30);
			g.setFont(font);
			g.setColor(Color.BLACK);
			String s = "Score: " + score;
			int sp = g.getFontMetrics().stringWidth(s) / 2;
			g.drawString(s, WIDTH / 10 * 3 - sp, HEIGHT / 14);	
			String bs = "Best: " + bestScore;
			int bsp = g.getFontMetrics().stringWidth(bs) / 2;
			g.drawString(bs, WIDTH / 10 * 7 - bsp, HEIGHT / 14);			
		}
		if(state == 2 || state == 3) {
			Font font = new Font("Ariel", 1, 60);
			g.setFont(font);
			g.setColor(Color.BLACK);
			String s = "Score: " + score;
			int sp = g.getFontMetrics().stringWidth(s) / 2;
			g.drawString(s, WIDTH / 2 - sp, HEIGHT / 6 * 4);	
			String bs = "Best: " + bestScore;
			int bsp = g.getFontMetrics().stringWidth(bs) / 2;
			g.drawString(bs, WIDTH / 2 - bsp, HEIGHT / 6 * 3);			
		}
		if(state == 2) {
			Font font = new Font("Ariel", 1, 100);
			g.setFont(font);
			g.setColor(Color.BLACK);
			String pause = "Paused";
			int sp = g.getFontMetrics().stringWidth(pause) / 2;
			g.drawString(pause, WIDTH / 2 - sp, HEIGHT / 4);
			font = new Font("Ariel", 1, 25);
			g.setFont(font);
			String pressAnywhere = "Press anywhere to unpause";
			sp = g.getFontMetrics().stringWidth(pressAnywhere) / 2;
			g.drawString(pressAnywhere, WIDTH / 2 - sp, HEIGHT / 16 * 14);
		}
		if(state == 3) {
			Font font = new Font("Ariel", 1, 100);
			g.setFont(font);
			g.setColor(Color.BLACK);
			String gameOver = "Game Over";
			int sp = g.getFontMetrics().stringWidth(gameOver) / 2;
			g.drawString(gameOver, WIDTH / 2 - sp, HEIGHT / 4);
			
		}
		if(state == 4) {
			drawSettingsMenu(g);
		}
		if(state == 5 || state == 6 || state == 3) {
			Font font = new Font("Ariel", 1, 25);
			g.setFont(font);
			g.setColor(Color.BLACK);
			String pressAnywhere = "Press anywhere to go back to the menu";
			int sp = g.getFontMetrics().stringWidth(pressAnywhere) / 2;
			g.drawString(pressAnywhere, WIDTH / 2 - sp, HEIGHT / 16 * 14);
		}
		if(state == 6) {
			Font font = new Font("Ariel", 1, 35);
			g.setFont(font);
			g.setColor(Color.BLACK);
			String credits = "I am Luka Uranic and I made this";
			String credits2 = "game from scratch in java for";
			String credits3 = "Ludum dare 41. I made everything";
			String credits4 = "except of some sounds.";
			int sp = g.getFontMetrics().stringWidth(credits) / 2;
			int sp2 = g.getFontMetrics().stringWidth(credits2) / 2;
			int sp3 = g.getFontMetrics().stringWidth(credits3) / 2;
			int sp4 = g.getFontMetrics().stringWidth(credits4) / 2;
			g.drawString(credits, WIDTH / 2 - sp, HEIGHT / 16 * 3);
			g.drawString(credits2, WIDTH / 2 - sp2, HEIGHT / 16 * 5);
			g.drawString(credits3, WIDTH / 2 - sp3, HEIGHT / 16 * 7);
			g.drawString(credits4, WIDTH / 2 - sp4, HEIGHT / 16 * 9);
		}else if(state == 5) {
			Font font = new Font("Ariel", 1, 35);
			g.setFont(font);
			g.setColor(Color.BLACK);
			String credits = "Move with aswd or arrow keys,";
			String credits2 = "shoot with mouse, heal with";
			String credits3 = "space, pause the game with p key";
			String credits4 = "or right mouse button.";
			int sp = g.getFontMetrics().stringWidth(credits) / 2;
			int sp2 = g.getFontMetrics().stringWidth(credits2) / 2;
			int sp3 = g.getFontMetrics().stringWidth(credits3) / 2;
			int sp4 = g.getFontMetrics().stringWidth(credits4) / 2;
			g.drawString(credits, WIDTH / 2 - sp, HEIGHT / 16 * 3);
			g.drawString(credits2, WIDTH / 2 - sp2, HEIGHT / 16 * 5);
			g.drawString(credits3, WIDTH / 2 - sp3, HEIGHT / 16 * 7);
			g.drawString(credits4, WIDTH / 2 - sp4, HEIGHT / 16 * 9);
		}
	}

	
	private void drawSettingsMenu(Graphics2D g) {
		Font font = new Font("Ariel", 1, 90);
		g.setFont(font);
		g.setStroke(new BasicStroke(8f));
		g.setColor(Color.BLACK);
		String settings = "Settings", sMusic, sSounds, back = "back";
		if(music) sMusic = "music on";
		else sMusic = "music off";
		if(sounds) sSounds = "sounds on";
		else sSounds = "sounds off";
		int sm = g.getFontMetrics().stringWidth(sMusic) / 2, sso = g.getFontMetrics().stringWidth(sSounds) / 2, sb = g.getFontMetrics().stringWidth(back) / 2;
		g.drawString(sMusic, WIDTH / 2 - sm, HEIGHT / 16 * 7);
		g.drawString(sSounds, WIDTH / 2 - sso, HEIGHT / 16 * 10);
		g.drawString(back, WIDTH / 2 - sb, HEIGHT / 16 * 14);

		font = new Font("Ariel", 1, 120);
		g.setFont(font);
		int ss = g.getFontMetrics().stringWidth(settings) / 2;	
		g.drawString(settings, WIDTH / 2 - ss, HEIGHT / 16 * 3);
		if(hMusic) g.drawRect(WIDTH / 10, HEIGHT / 16 * 5, WIDTH / 10 * 8, HEIGHT / 16 * 2 + 10);
		if(hSounds) g.drawRect(WIDTH / 10, HEIGHT / 16 * 8, WIDTH / 10 * 8, HEIGHT / 16 * 2 + 10);
		if(hBack) g.drawRect(WIDTH / 10, HEIGHT / 16 * 12, WIDTH / 10 * 8, HEIGHT / 16 * 2 + 10);	
	}

	private void drawMenu(Graphics2D g) {
		Font font = new Font("Ariel", 1, 90);
		g.setFont(font);
		g.setStroke(new BasicStroke(8f));
		g.setColor(Color.BLACK);
		String play = "Play", settings = "Settings", help = "Help", credits = "Credits", exit = "Exit";
		int sp = g.getFontMetrics().stringWidth(play) / 2, ss = g.getFontMetrics().stringWidth(settings) / 2, sh = g.getFontMetrics().stringWidth(help) / 2, sc = g.getFontMetrics().stringWidth(credits) / 2, se = g.getFontMetrics().stringWidth(exit) / 2;
		g.drawString(play, WIDTH / 2 - sp, HEIGHT / 16 * 3);
		g.drawString(settings, WIDTH / 2 - ss, HEIGHT / 16 * 6);
		g.drawString(help, WIDTH / 2 - sh, HEIGHT / 16 * 9);
		g.drawString(credits, WIDTH / 2 - sc, HEIGHT / 16 * 12);
		g.drawString(exit, WIDTH / 2 - se, HEIGHT / 16 * 15);
		if(hPlay) g.drawRect(WIDTH / 5, HEIGHT / 16, WIDTH / 5 * 3, HEIGHT / 16 * 2 + 10);
		if(hSettings) g.drawRect(WIDTH / 5, HEIGHT / 16 * 4, WIDTH / 5 * 3, HEIGHT / 16 * 2 + 10);
		if(hHelp) g.drawRect(WIDTH / 5, HEIGHT / 16 * 7, WIDTH / 5 * 3, HEIGHT / 16 * 2 + 10);
		if(hCredits) g.drawRect(WIDTH / 5, HEIGHT / 16 * 10, WIDTH / 5 * 3, HEIGHT / 16 * 2 + 10);
		if(hExit) g.drawRect(WIDTH / 5, HEIGHT / 16 * 13, WIDTH / 5 * 3, HEIGHT / 16 * 2 + 10);
	}

	public static void main(String[] args) {
		EndlessShooter es = new EndlessShooter();
		es.frame.setResizable(false);
		es.frame.add(es);
		es.frame.pack();
		es.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		es.frame.setLocationRelativeTo(null);
		es.frame.setVisible(true);
		es.frame.setAlwaysOnTop(true);
		es.start();
	}
}
