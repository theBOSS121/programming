package com.theBoss.Stack;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.theBoss.Configuration;
import com.theBoss.Stack.entity.Rectangle;
import com.theBoss.Stack.graphics.Screen;
import com.theBoss.Stack.input.Keyboard;
import com.theBoss.Stack.input.Mouse;

public class Stack extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private static int width = 800, height = 600, scale = 1;
	private Thread thread;
	private JFrame frame;
	private boolean running = false;
	private Screen screen;
	private Keyboard key;
	private Mouse mouse;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	private List<Rectangle> rects;
	private Random rand = new Random();
	
	private int widthOfRects = 300, moveCounter = 0, score;

	public static int bestScore;
	private boolean notMoving = true, gameOver = false, mouseBuffer = false, mouseBuffer2 = false;
	
	Configuration config = new Configuration();
	
	public Stack() {
		setPreferredSize(new Dimension(width * scale, height * scale));
		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		mouse = new Mouse();
		addKeyListener(key);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		config.loadConfiguration("config.xml");
		init();
	}
	
	private void init() {
		widthOfRects = 300;
		moveCounter = 0;
		score = -1;
		notMoving = true;
		rects = new ArrayList<Rectangle>();
		rects.add(new Rectangle(width / 2 - 150, height - 100, widthOfRects, 99, 0.0, screen));
		rects.add(new Rectangle(width / 2 - 150, height - 200, widthOfRects, 99, 0.0, screen));
		rects.add(new Rectangle(width / 2 - 150, height - 300, widthOfRects, 99, 0.0, screen));
	}
	
	private void start() {
		running  = true;
		thread = new Thread(this, "gameThread");
		thread.start();
	}
	
	private void stop() {
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
				frame.setTitle("Stack | " + updates + " ups, " + frames +" fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	private void update() {
		if(!gameOver) {
			for(int i = 0; i < rects.size(); i++) {
				rects.get(i).update();
			}
			
			if(notMoving) {
				int side = rand.nextInt(2);
				if(side == 0) {
					double xDir = rand.nextDouble() * 8.0 + 2.5;
					rects.add(new Rectangle(-widthOfRects, height - 400, widthOfRects, 99, xDir, screen));
				}
				if(side == 1) {
					double xDir = rand.nextDouble() * -8.0 - 2.5;
					rects.add(new Rectangle(width, height - 400, widthOfRects, 99, xDir, screen));
				}
				notMoving = false;
				score++;
			}
			
			if(Mouse.getButton() == 1) {
				rects.get(rects.size() - 1).xDir = 0.0;
			}
			if(rects.get(rects.size() - 1).xDir == 0.0) {
				if(moveCounter == 0) {
					if(rects.get(rects.size() - 1).x < width / 2 - widthOfRects / 2) {
						widthOfRects = widthOfRects - (width / 2 - widthOfRects / 2 - (int) rects.get(rects.size() - 1).x);
						rects.get(rects.size() - 1).x += rects.get(rects.size() - 1).width - widthOfRects;
						rects.get(rects.size() - 1).width = widthOfRects;
					}
					if(rects.get(rects.size() - 1).x > width / 2 - widthOfRects / 2) {
						widthOfRects = widthOfRects + (width / 2 - widthOfRects / 2 - (int) rects.get(rects.size() - 1).x);
						rects.get(rects.size() - 1).width = widthOfRects;
					}
					if(widthOfRects <= 0) {
						gameOver = true;
						if(score > bestScore) {
							bestScore = score;
							config.saveConfiguration("best", bestScore);
						}
					}
				}
				for(int i = 0; i < rects.size(); i++) {
					rects.get(i).y++;
				}
				if(rects.get(rects.size() - 1).x < width / 2 - widthOfRects / 2) {
					rects.get(rects.size() - 1).x += 1.5;
				}
				if(rects.get(rects.size() - 1).x > width / 2 - widthOfRects / 2) {
					rects.get(rects.size() - 1).x -= 1.5;
				}
				moveCounter++;
				if(moveCounter == 100) {
					moveCounter = 0;
					notMoving = true;
					rects.remove(0);
				}
			}
		}
		if(gameOver) {
			if(Mouse.getButton() == 0) {
				mouseBuffer = true;
			}
			if(mouseBuffer && Mouse.getButton() == 1) {
				mouseBuffer2 = true;
				mouseBuffer = false;
			}
			if(mouseBuffer2 && Mouse.getButton() == 0) {
				gameOver = false;
				mouseBuffer = false;
				mouseBuffer2 = false;
				init();
			}
		}
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		screen.clear();
		screen.render();
		
		for(int i = 0; i < rects.size(); i++) {
			rects.get(i).render();
		}
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = screen.pixels[i];
		}
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		
		g.drawImage(image, 0, 0, width * scale, height * scale, null);
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(new Font("Ariel", 0, 100));
		g.setColor(Color.BLACK);

		if(gameOver) {
			String over = "Game Over";
			int ow = g.getFontMetrics().stringWidth(over) / 2;
			g.drawString(over, width / 2 - ow, height / 5);
			String s = "Score: " + score;
			int sw = g.getFontMetrics().stringWidth(s) / 2;
			g.drawString(s, width / 2 - sw, height / 5 * 3);
			String b = "Best score: " + bestScore;
			int bw = g.getFontMetrics().stringWidth(b) / 2;
			g.drawString(b, width / 2 - bw, height / 5 * 4);
		}
		if(!gameOver) {
			String s = "" + score;
			int sw = g.getFontMetrics().stringWidth(s) / 2;
			g.drawString(s, width / 2 - sw, 100);
		}
		
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Stack stack = new Stack();
		stack.frame.setResizable(false);
		stack.frame.setTitle("Stack");
		stack.frame.add(stack);
		stack.frame.pack();
		stack.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		stack.frame.setVisible(true);
		stack.frame.setLocationRelativeTo(null);
		stack.start();
	}
	
}