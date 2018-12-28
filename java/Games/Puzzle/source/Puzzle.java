import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Random;

import javax.swing.JFrame;

public class Puzzle {

	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	public boolean running = true;
	public JFrame frame;
	public Mouse mouse;
	Renderer renderer;
	private int selected = -1;
	private boolean dragging;
	static boolean started = false;
	private int lastX;
	private int lastY;
	
	public static Rectangle[] rects = { new Rectangle(0, 0, 150, 150),
								 		new Rectangle(150, 0, 150, 150),
								 		new Rectangle(300, 0, 150, 150),
								 		new Rectangle(450, 0, 150, 150),
								 		new Rectangle(0, 150, 150, 150),
								 		new Rectangle(150, 150, 150, 150),
								 		new Rectangle(300, 150, 150, 150),
								 		new Rectangle(450, 150, 150, 150),
								 		new Rectangle(0, 300, 150, 150),
								 		new Rectangle(150, 300, 150, 150),
								 		new Rectangle(300, 300, 150, 150),
								 		new Rectangle(450, 300, 150, 150),
								 		new Rectangle(0, 450, 150, 150),
								 		new Rectangle(150, 450, 150, 150),
								 		new Rectangle(300, 450, 150, 150)};
	
	public Puzzle() {
		frame = new JFrame();
		renderer = new Renderer();
		frame.add(renderer);
		frame.setTitle("Puzzle");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(WIDTH + 7, HEIGHT + 30);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		mouse = new Mouse();
		frame.addMouseListener(mouse);
		frame.addMouseMotionListener(mouse);
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
				frame.setTitle("Puzzle " + updates + ", " + frames);
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
			if(!started) {
				suffle();
				started = true;
			}
		}		
	}
	
	
	private void suffle() {
		Random rand = new Random();

		for(int i = 0; i < rects.length - 1; i++) {
			if(rects[i].x == 450 &&  rects[i].y == 450) {
				return;
			}
		}
		
		for(int i = 0; i < rects.length; i++) {
			int which = rand.nextInt(rects.length);
			int x = rects[which].x;
			int y = rects[which].y;
			rects[which].x = rects[i].x;
			rects[which].y = rects[i].y;
			rects[i].x = x;
			rects[i].y = y;
		}
		if(!solvable()) {
			suffle();
		}		
	}

	private boolean solvable() {
		int[] nums = new int[15];
		int total = 0;
		
		for(int i = 0; i < rects.length; i++) {
			nums[rects[i].y / 150 * 4 + rects[i].x / 150] = i + 1;
		}

		for(int i = 0; i < nums.length; i++) {
			for(int j = i; j < nums.length; j++) {
				if(nums[i] > nums[j]) {
					total++;
				}
			}
		}
		
		System.out.println("Total is: " + total);
		
		if(total % 2 == 0) {
			return true;
		}else {
			return false;
		}
	}

	public void update() {
		if(Mouse.dragging) {
			for(int i = 0; i < rects.length; i++) {
				if(Mouse.getX() / 150 == rects[i].x / 150 && Mouse.getY() / 150 == rects[i].y / 150 && !dragging) {
					selected = i;
					lastX = rects[i].x;
					lastY = rects[i].y;
					dragging = true;
				}
			}
		}else {
			dragging = false;
			if(selected != -1) {
				if(rects[selected].x % 150 < 75) {
					rects[selected].x = (rects[selected].x / 150) * 150;
				}else {
					rects[selected].x = (rects[selected].x / 150 + 1) * 150;
				}
				if(rects[selected].y % 150 < 75) {
					rects[selected].y = (rects[selected].y / 150) * 150;
				}else {
					rects[selected].y = (rects[selected].y / 150 + 1) * 150;
				}
				
				if(rects[selected].x < 0) {
					rects[selected].x = 0;
				}
				if(rects[selected].x > 450) {
					rects[selected].x = 450;
				}
				if(rects[selected].y < 0) {
					rects[selected].y = 0;
				}
				if(rects[selected].y > 450) {
					rects[selected].y = 450;
				}
				for(int i = 0; i < rects.length;i++) {
					if(selected == i) continue;
					if(rects[selected].intersects(rects[i])) {
						rects[selected].x = lastX;
						rects[selected].y = lastY;
					}
				}
				
				if(!(rects[selected].x == lastX && (rects[selected].y + 150 == lastY || rects[selected].y - 150 == lastY)) &&
				   !(rects[selected].y == lastY && (rects[selected].x + 150 == lastX || rects[selected].x - 150 == lastX))) {
					rects[selected].x = lastX;
					rects[selected].y = lastY;
				}
				
			}
			selected = -1;
		}
		
		if(dragging) {
			rects[selected].x = Mouse.getX() - 75;
			rects[selected].y = Mouse.getY() - 75;
		}
	}

	public static void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH + 1, HEIGHT + 1);
		g.setFont(new Font("Ariel", 0, 100));
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		for(int i = 0; i < rects.length; i++) {
			g.setColor(Color.BLUE);
			g.fillRect(rects[i].x, rects[i].y, rects[i].width, rects[i].height);
			String string = Integer.toString(i + 1);
			int w = g.getFontMetrics().stringWidth(string) / 2;
			g.setColor(Color.BLACK);
			g.drawString(string, rects[i].x + rects[i].width / 2 - w, rects[i].y + rects[i].height - 40);
		}
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, WIDTH, HEIGHT);
		g.drawLine(0, HEIGHT / 4, WIDTH, HEIGHT / 4);
		g.drawLine(0, HEIGHT / 4 * 2, WIDTH, HEIGHT / 4 * 2);
		g.drawLine(0, HEIGHT / 4 * 3, WIDTH, HEIGHT / 4 * 3);
		g.drawLine(WIDTH / 4, 0, WIDTH / 4, HEIGHT);
		g.drawLine(WIDTH / 4 * 2, 0, WIDTH / 4 * 2, HEIGHT);
		g.drawLine(WIDTH / 4 * 3, 0, WIDTH / 4 * 3, HEIGHT);
		
		
	}

	public static void main(String[] args) {
		new Puzzle();
	}

}
