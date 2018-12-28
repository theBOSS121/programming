package theboss.sacrifice;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.text.DecimalFormat;

import javax.swing.JFrame;

import theboss.sacrifice.game.Game;
import theboss.sacrifice.graphics.Screen;
import theboss.sacrifice.input.Keyboard;
import theboss.sacrifice.input.Mouse;

public class Sacrifice  extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 800, HEIGHT = 800;
	public static float scale = 1.0f;
	public Thread thread;
	public boolean running = false;
	public JFrame frame;
	public static Screen screen;
	public Game game;	
	public static Mouse mouse;
	public static Keyboard key;
	
	public BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	public int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	public Sacrifice() {
		Dimension size = new Dimension((int) (WIDTH * scale),(int)  (HEIGHT * scale));
		setPreferredSize(size);
		screen = new Screen(WIDTH, HEIGHT);
		frame = new JFrame();
		game = new Game();
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		key = new Keyboard();
		addKeyListener(key);
	}
	
	public void start() {
		running = true;
		thread = new Thread(this, "loopThread");
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
				frame.setTitle("Sacrifice for the zone | " + updates + " ups, " + frames +" fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void update() {
		game.update();
		mouse.update();
		key.update();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		screen.clear();
		game.render();
		
		for(int i = 0; i < screen.pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(image, 0, 0, (int) (WIDTH * scale), (int) (HEIGHT * scale), null);
		g.setColor(new Color(0xff193441, true));
		g.setFont(new Font("Arial", 1, 24));
		String s = "Score: " + Game.score;
		int sw = g.getFontMetrics().stringWidth(s) / 2;
		String b = "Best: " + Game.bestScore;
		int bsw = g.getFontMetrics().stringWidth(b) / 2;
		g.drawString(s, getWidth() / 9 - sw, 50);
		g.drawString(b, getWidth() / 9 * 8 - bsw, 50);
		if(game.player.canExplde && !Game.gameOver) {
			String cane = "You can press e or space to explode";
			int canew = g.getFontMetrics().stringWidth(cane) / 2;
			g.drawString(cane, getWidth() / 2 - canew, 740);
		}

		g.setFont(new Font("Arial", 1, 100));
//		DecimalFormat numberFormat = new DecimalFormat("#.00");
		
		String t = "" + (Math.round((Game.overTimer / 60.0) * 10.0) / 10.0);
		int tw = g.getFontMetrics().stringWidth(t) / 2;
		g.drawString(t, getWidth() / 2 - tw, 150);
		
		if(Game.gameOver) {
			g.setFont(new Font("Arial", 1, 110));
			g.setColor(new Color(0xdd2E7A2F, true));
			String go = "GAME OVER";
			int gow = g.getFontMetrics().stringWidth(go) / 2;
			g.drawString(go, getWidth() / 2 - gow, 320);
			g.setColor(new Color(0xff193441, true));

			g.setFont(new Font("Arial", 0, 20));
			String tutorial = "You lose if any enemy ball stays in zone for 10 seconds";
			String tutorial1= "Move your ball with keyboard keys 'awsd' or 'arrow keys',";
			String tutorial2 = "explode yourself (sacrafice for the zone) with 'e' or 'space'.";
			String tutorial3 = "You can explode when you score 10 points and dont die in.";
			String tutorial4 = "between You die with your explosion or green ball's";
			String tutorial5 = "explosion. You score points with kicking balls of the";
			String tutorial6 = "screen. Green ball explodes if you kick it four times.";
			int tutorialw = g.getFontMetrics().stringWidth(tutorial) / 2;
			g.drawString(tutorial, getWidth() / 2 - tutorialw, 480);
			int tutorial1w = g.getFontMetrics().stringWidth(tutorial1) / 2;
			g.drawString(tutorial1, getWidth() / 2 - tutorial1w, 510);
			int tutorialw2 = g.getFontMetrics().stringWidth(tutorial2) / 2;
			g.drawString(tutorial2, getWidth() / 2 - tutorialw2, 540);
			int tutorialw3 = g.getFontMetrics().stringWidth(tutorial3) / 2;
			g.drawString(tutorial3, getWidth() / 2 - tutorialw3, 570);
			int tutorialw4 = g.getFontMetrics().stringWidth(tutorial4) / 2;
			g.drawString(tutorial4, getWidth() / 2 - tutorialw4, 600);
			int tutorialw5 = g.getFontMetrics().stringWidth(tutorial5) / 2;
			g.drawString(tutorial5, getWidth() / 2 - tutorialw5, 630);
			int tutorialw6 = g.getFontMetrics().stringWidth(tutorial6) / 2;
			g.drawString(tutorial6, getWidth() / 2 - tutorialw6, 660);
			
			
			g.setFont(new Font("Arial", 1, 15));
			String restart = "(press 'r' or click with mouse anywhere to restart)";
			int restartw = g.getFontMetrics().stringWidth(restart) / 2;
			g.drawString(restart, getWidth() / 2 - restartw, 750);
		}
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		Sacrifice s = new Sacrifice();
		s.frame.setResizable(false);
		s.frame.setTitle("Sacrifice");
		s.frame.add(s);
		s.frame.pack();
		s.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.frame.setVisible(true);
		s.frame.setLocationRelativeTo(null);
		s.frame.setAlwaysOnTop(true);
		s.start();
	}
}
