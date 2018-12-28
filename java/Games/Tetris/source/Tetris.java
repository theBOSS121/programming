import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

public class Tetris {

	private static final int WIDTH = 400;
	private static final int HEIGHT = 600;
	private boolean running = true;
	private boolean oneIsFallling = false;
	private JFrame frame;
	private Renderer renderer;
	public static Keyboard key;
	private Random rand = new Random();
	private int col = 0, lastCol = 0;
	private static int score = 0;
	private int[] row = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	private static boolean gameOver, paused = false;
	Configuration config = new Configuration();
	public static List<Object> objects = new ArrayList<Object>();
	public static int bestScore;
	
	public Tetris() {
		frame = new JFrame("Tetris");
		renderer = new Renderer();
		frame.add(renderer);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(WIDTH + 7, HEIGHT + 30);
		frame.setLocationRelativeTo(null);
		key = new Keyboard();
		frame.addKeyListener(key);
		config.loadConfiguration("config.xml");
		gameLoop();
	}
	
	private void gameLoop() {
		double delta = 1000.0 / 60.0;
		double deltaF = 1000.0 / 100.0;
		double lastTime = System.currentTimeMillis();
		double currentTime;
		int updates = 0;
		int frames = 0;
		
		while(running) {
			currentTime = System.currentTimeMillis();
			if(currentTime - lastTime > 1000.0) {
				frame.setTitle("Tetris " + updates + ", " + frames);
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
	
	private void update() {
		key.update();
		if(!gameOver) {
			if(!paused) {
				if(!oneIsFallling) {
					col = rand.nextInt(6);
					while(col == lastCol) {
						col = rand.nextInt(6);
					}			
					objects.add(new Object(rand.nextInt(7), col, true));
					score++;
					lastCol = col;
					oneIsFallling = true;
				}
				
				for(int i = 0; i < objects.size(); i++) {
					objects.get(i).update();
				}
				
				if(!objects.get(objects.size() - 1).falling) {
					oneIsFallling = false;
					lookForRow();
				}
			}
			
			if(key.paused) {
				paused = true;
			}else {
				paused = false;
			}
			
		}else {
			if(score > bestScore) { 
				bestScore = score;
				config.saveConfiguration("best", bestScore);
			}
			if(key.space) {
				oneIsFallling = false;
				score = 0;
				objects = new ArrayList<Object>();
				gameOver = false;
				paused = false;
			}
		}
	}
	
	private void lookForRow() {
		for(int i = 0; i < row.length; i++) {
			row[i] = 0;
		}
		
		for(int i = 0; i < objects.size(); i++) {
			Object o = objects.get(i);
			for(int j = 0; j < o.squeers.size(); j++) {
				if(o.squeers.get(j).y < 0) {
					gameOver = true;
					return;
				}
				row[o.squeers.get(j).y]++;
			}
		}
		for(int i = 0; i < row.length; i++) {
			if(row[i] > 9) {
				destroy(i);
			}
		}
	}

	private void destroy(int index) {
		for(int i = 0; i < objects.size(); i++) {
			Object o = objects.get(i);
			for(int j = 0; j < o.squeers.size(); j++) {
				Squeer s = o.squeers.get(j);
				if(s.y == index) {
					o.squeers.get(j).removed = true;
				}
			}
		}
		
		for(int i = 0; i < objects.size(); i++) {
			Object o = objects.get(i);
			for(int j = 0; j < o.squeers.size(); j++) {
				if(o.squeers.get(j).y < index) o.squeers.get(j).y++;
			}
		}
	}

	public static void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, WIDTH + 1, HEIGHT + 1);
		
		if(!gameOver) {
			for(int i = 0; i < objects.size(); i++) {
				if(objects.get(i).col == 0) g.setColor(Color.RED);
				if(objects.get(i).col == 1) g.setColor(Color.BLUE);
				if(objects.get(i).col == 2) g.setColor(Color.GREEN);
				if(objects.get(i).col == 3) g.setColor(Color.YELLOW);
				if(objects.get(i).col == 4) g.setColor(Color.ORANGE);
				if(objects.get(i).col == 5) g.setColor(Color.PINK);
				objects.get(i).render(g);
			}
			
			
			g.setColor(Color.BLACK);
			for(int i = 0; i < 15; i++) {
				if(i < 10) {
					g.drawLine(WIDTH / 10 * i, 0, WIDTH / 10 * i, HEIGHT);
				}
				g.drawLine(0, HEIGHT / 15 * i, WIDTH, HEIGHT / 15 * i);
			}
		}else {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Ariel", 0, 70));
			String sg = "Game Over";
			int ig = g.getFontMetrics().stringWidth(sg) / 2;
			g.drawString(sg, WIDTH / 2 - ig, 150);
			g.setFont(new Font("Ariel", 0, 30));
			String ss = "Your score is: " + score;
			int is = g.getFontMetrics().stringWidth(ss) / 2;
			g.drawString(ss, WIDTH / 2 - is, 250);
			String sb = "Your best score is: " + bestScore;
			int ib = g.getFontMetrics().stringWidth(sb) / 2;
			g.drawString(sb, WIDTH / 2 - ib, 300);
			String sp = "Press space to play again";
			int ip = g.getFontMetrics().stringWidth(sp) / 2;
			g.drawString(sp, WIDTH / 2 - ip, 500);
			
		}
	}
	
	public static void main(String[] args) {
		new Tetris();
	}
}
