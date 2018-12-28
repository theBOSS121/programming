import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ConnectFour {

	private final static int WIDTH = 800, HEIGHT = 600;
	private boolean running = true;
	JFrame frame;
	Renderer renderer;
	static BufferedImage board;
	private Mouse mouse;
	private boolean oneIsFallling = false;
	private static boolean gameOver = false;
	private static int player = 0;
	public static List<Ball> balls = new ArrayList<Ball>();
	
	public ConnectFour() {
		frame = new JFrame("Connect Four");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(WIDTH + 6, HEIGHT + 29);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		renderer = new Renderer();
		frame.add(renderer);
		frame.setLocationRelativeTo(null);
		try {
			board = ImageIO.read(ConnectFour.class.getResource("/board.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		mouse = new Mouse();
		frame.addMouseListener(mouse);
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
				frame.setTitle("Connect Four " + updates + ", " + frames);
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
		if(!gameOver) {
			if(!oneIsFallling ) {	
				if(Mouse.released) {
					boolean valid = true;
					for(int i = 0; i < balls.size(); i++) {
						if(balls.get(i).x == Mouse.getX() / 112 * 112 + 16 && balls.get(i).y == 10) {
							valid = false;
						}
					}
					if(valid) {
						balls.add(new Ball((Mouse.getX() / 112) * 112 + 16, -80, true, player));
						oneIsFallling = true;
						Mouse.released = false;
						if(player == 0) player = 1;
						else player = 0;
					}
				}
			}
			
			for(int i = 0; i < balls.size(); i++) {
				if(balls.get(i).falling) {
					balls.get(i).update();
				}
			}
			
			if(balls.size() > 0 && oneIsFallling) {
				if(!balls.get(balls.size() - 1).falling) {
					if(lookForEnd(balls.get(balls.size() - 1).player) || balls.size() == 42) {
						gameOver = true;
					}
					oneIsFallling = false;
				}
			}
		}else {
			if(Mouse.released) {
				Mouse.released = false;
				balls = new ArrayList<Ball>();
				gameOver = false;
			}
		}
	}

	private boolean lookForEnd(int which) {
		int a = 0;
		List<Integer> x = new ArrayList<Integer>();
		List<Integer> y = new ArrayList<Integer>();
		
		for(int i = 0; i < balls.size(); i++) {
			if(balls.get(i).player == which) {
				x.add(balls.get(i).x);
				y.add(balls.get(i).y);
				a++;
			}
		}
		
		if(a >= 4) {
			for(int i = 0; i < a; i++) {				
				int inX = 1;
				int inY = 1;
				int inXY = 1;
				int inYX = 1;
				int xx = x.get(i);
				int yy = y.get(i);
				for(int i1 = 0; i1 < 4; i1++) {
					for(int j = 0; j < a; j++) {
						if(i == j) continue;
						if(x.get(j) / 112 + i1 == xx / 112 && y.get(j) / 100 == yy / 100){
							inX++;
						}
						if(x.get(j) / 112  == xx / 112  && y.get(j) / 100 + i1 == yy / 100){
							inY++;
						}
						if(x.get(j) / 112  + i1 == xx / 112  && y.get(j) / 100 + i1== yy / 100){
							inXY++;
						}
						if(x.get(j) / 112  + i1 == xx / 112  && y.get(j) / 100 - i1 == yy / 100){
							inYX++;
						}
					}
				}
				if(inX == 4 || inY == 4 || inXY == 4 || inYX == 4) {
					return true;
				}
			}
			
		}
		return false;
	}

	public static void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for(int i = 0; i < balls.size(); i++) {
			if(balls.get(i).player == 0) g.setColor(Color.GREEN);
			if(balls.get(i).player == 1) g.setColor(Color.RED);
			g.fillOval(balls.get(i).x, balls.get(i).y, 82, 82);
		}
		
		g.drawImage(board, 0, 0, null);
		
		if(gameOver) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Ariel", 1, 140));
			String gs = "Game Over";
			int gw = g.getFontMetrics().stringWidth(gs) / 2;
			g.drawString(gs, WIDTH / 2 - gw, 200);
		}
	}

	public static void main(String[] args) {
		new ConnectFour();
	}
}
