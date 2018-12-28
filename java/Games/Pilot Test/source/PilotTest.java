import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Random;

import javax.swing.JFrame;

public class PilotTest {

	private final static int WIDTH = 600;
	private final static int HEIGHT = 600;
	public Renderer renderer;
	private boolean running = true;
	public JFrame frame;
	public Mouse mouse;
	Random random = new Random();
	public static int x = WIDTH / 2;
	public static int y = HEIGHT / 2;
	private boolean dragging = false;
	private static boolean gameOver = false;
	private static boolean droped = false;
	private boolean started = false;
	private static long startTime = -1;
	private static long endTime;
	
	public static Rectangle[] rects = { new Rectangle(100, 100, 85, 85),
								 		new Rectangle(355, 90, 90, 75),
								 		new Rectangle(100, 430, 40, 80),
								 		new Rectangle(415, 450, 125, 30)};
	public static double best = 0;
	
	public double[] rx = {7.0, -4.6, 7.0, -5.2};
	public double[] ry = {4.3, 5.0, -6.0, -8.2};

	Configuration config = new Configuration();
	
	public PilotTest() {
		config.loadConfiguration("config.xml");
		frame = new JFrame();
		renderer = new Renderer();
		frame.add(renderer);
		frame.setTitle("PilotTest");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(WIDTH + 6, HEIGHT + 29);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		mouse = new Mouse();
		frame.addMouseListener(mouse);
		frame.addMouseMotionListener(mouse);
		gameLoop();
	}

	private void gameLoop() {
		double delta = 1000.0 / 60.0;
		double deltaF = 1000.0 / 1000.0;
		double lastTime = System.currentTimeMillis();
		double currentTime;
		int updates = 0;
		int frames = 0;
		
		while(running) {
			currentTime = System.currentTimeMillis();
			if(currentTime - lastTime > 1000.0) {
				frame.setTitle("Pilot Test " + updates + ", " + frames);
				updates = 0;
				frames = 0;
				lastTime += 1000.0;
			}
			if(currentTime - lastTime > delta * updates) {
				updates++;
				update();
			}
			if(currentTime - lastTime > deltaF * frames) {
				renderer.repaint();
				frames++;
			}
			
		}		
	}
	
	public void update() {
		if(gameOver && Mouse.dragged && droped) {
			started = false;
			dragging = false;
			startTime = -1;
			x = WIDTH / 2;
			y = HEIGHT / 2;
			rects[0] = new Rectangle(100, 100, 85, 85);
			rects[1] = new Rectangle(355, 90, 90, 75);
			rects[2] = new Rectangle(100, 430, 40, 80);
			rects[3] = new Rectangle(415, 450, 125, 30);
			rx[0] = 7.0;
			rx[1] = -4.6;
			rx[2] = 7.0;
			rx[3] = -5.2;
			ry[0] = 4.3;
			ry[1] = 5.0;
			ry[2] = -6.0;
			ry[3] = -8.2;
			gameOver = false;
			droped = false;
		}
		
		if(gameOver && !Mouse.dragged) {
			droped = true;
		}
		
		if(gameOver) return;			
		
		if(Mouse.dragged) {
			if((Mouse.mouseX - 2 > x - 25 && Mouse.mouseX - 2 < x + 25 && Mouse.mouseY - 25 > y - 25 && Mouse.mouseY - 25 < y + 25) || dragging) {
				if(startTime == -1) {
					startTime = System.currentTimeMillis();
				}
				x = Mouse.mouseX - 2;
				y = Mouse.mouseY - 25;
				dragging = true;
				started = true;
				//System.out.println(x + ", " + y);
			}
		}else {
			dragging = false;
		}
		if(started) {
			for(int i = 0; i < rects.length; i++) {
				rects[i].x += rx[i];
				rects[i].y += ry[i];
				if(rects[i].x < 0 || rects[i].x + rects[i].width > WIDTH) {
					rx[i] *= -1;
				}
				if(rects[i].y < 0 || rects[i].y + rects[i].height > HEIGHT) {
					ry[i] *= -1;
				}
			}
		}
		
		if(collision()) {
			gameOver = true;
			endTime = System.currentTimeMillis();
			if(best < ((endTime - startTime) / 1000.0)) {
				best = ((endTime - startTime) / 1000.0);
				config.saveConfiguration("best", best); 
			}
		}
		
		if(x < 85 || y < 85 || x > 515 || y > 515) {
			gameOver = true;
			endTime = System.currentTimeMillis();
			if(best < ((endTime - startTime) / 1000.0)) {
				best = ((endTime - startTime) / 1000.0);
				config.saveConfiguration("best", best);
			}
		}
		
	}

	private boolean collision() {
		for(int i = 0; i < rects.length; i++) {
			if(x + 25 > rects[i].x && x - 25 < rects[i].x + rects[i].width && y + 25 > rects[i].y && y - 25 < rects[i].y + rects[i].height) {
				return true;
			}
		}		
		return false;
	}

	public static void render(Graphics2D g) {
		//g.setStroke(new BasicStroke(10f));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		g.fillRect(60, 60, WIDTH - 120, HEIGHT - 120);	
		if(x != -1 && y != -1) {
			g.setColor(Color.RED);
			g.fillRect(x - 25, y - 25, 50, 50);
			
		}
		for(int i = 0; i < 4; i++) {
			g.setColor(Color.BLUE);
			g.fillRect(rects[i].x, rects[i].y, rects[i].width, rects[i].height);
		}
		if(gameOver) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Ariel", 1, 70));
			String sg = "Game Over";
			String st = "Time: " + ((endTime - startTime) / 1000.0) + "s";
			String sbt = "Best: " + best + "s";
			int ig = g.getFontMetrics().stringWidth(sg) / 2;
			int it = g.getFontMetrics().stringWidth(st) / 2;
			int ibt = g.getFontMetrics().stringWidth(sbt) / 2;
			g.drawString(sg, WIDTH / 2 - ig, 150);
			g.drawString(st, WIDTH / 2 - it, 300);
			g.drawString(sbt, WIDTH / 2 - ibt, 450);
		}
		
	}
	
	public static void main(String[] args) {
		new PilotTest();
	}

}
