package com.chess;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.chess.entity.Entity;
import com.chess.entity.pieces.Bishop;
import com.chess.entity.pieces.King;
import com.chess.entity.pieces.Knight;
import com.chess.entity.pieces.Pown;
import com.chess.entity.pieces.Queen;
import com.chess.entity.pieces.Rook;
import com.chess.graphics.Screen;
import com.chess.graphics.Sprite;
import com.chess.input.Keyboard;
import com.chess.input.Mouse;
import com.chess.level.Level;
import com.chess.net.Client;


public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static int width = 209;//145;
	public static int height = 151;
	public static int scale = 4;
	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private Mouse mouse;
	public static Level level;
	private static boolean running = false;
	public static boolean multiplayer = true;
	public static boolean started = false;
	private static int index = -1;
	public static int turn = 1;
	public static boolean yourMove = true;

	public Client client;
	
	private Screen screen;
	
	private static int[] squeerX;
	private static int[] squeerY;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels;

	public Game() {
		level = Level.board;
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		screen = new Screen(width, height);
		frame = new JFrame();
		key = new Keyboard();
		
		addKeyListener(key);

		if(multiplayer) {
			client = new Client("localhost", 8192);
			if(!client.connect()){
				//doesnt work
				System.out.println("Can't connect to the server");
			}		
		}
		
		mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		onBoard();
	}
	
	public static void recenterAndDisselect() {
		boolean moved = true;
		int offset = 0;
		if(index == -1) return;
		if(level.entities.get(index).x % 16 < 8) {
		level.entities.get(index).x = ((int) (level.entities.get(index).x / 16)) * 16;
		}else {
			level.entities.get(index).x = ((int) (level.entities.get(index).x / 16)) * 16 + 16;
		}
		if(level.entities.get(index).y % 16 < 8) {
			level.entities.get(index).y = ((int) (level.entities.get(index).y / 16)) * 16;
		}else {
			level.entities.get(index).y = ((int) (level.entities.get(index).y / 16)) * 16 + 16;
		}
		
		if(!level.entities.get(index).isAllowed() || level.entities.get(index).x / 16 < 1 || level.entities.get(index).x / 16 > 8
			|| level.entities.get(index).y / 16 < 0 || level.entities.get(index).y  / 16 > 7) {
			level.entities.get(index).x = level.entities.get(index).lastX;
			level.entities.get(index).y = level.entities.get(index).lastY;
			if(level.entities.get(index) instanceof Pown) {
				Pown p = (Pown) level.entities.get(index);
				p.firstMove = false;
			}

			moved = false;
			if(!multiplayer) {
				if(turn == 0) turn = 1;
				else turn = 0;
			}
		}else {
			for(int i = 0; i < level.entities.size(); i++) {
				if(i == index) continue;
				Entity e = level.entities.get(i);
				if(e.x == level.entities.get(index).x && e.y == level.entities.get(index).y) {
					if(e.col != level.entities.get(index).col) {
						if(lookForCheck() != level.entities.get(index).col) {
						e.remove();
						if(e.col == 0)	offset = 1;
						BinaryWriter writer = new BinaryWriter();
						writer.write(Client.opponentsID);
						writer.write(level.entities.get(index).lastX);
						writer.write(level.entities.get(index).lastY);
						writer.write(level.entities.get(index).x);
						writer.write(level.entities.get(index).y);
						level.entities.get(index - offset).lastX = level.entities.get(index).x;
						level.entities.get(index - offset).lastY = level.entities.get(index).y;
						byte[] data = new byte[1024];
						data = writer.getBuffer();
						if(multiplayer) {
							Client.send(data);
						}
						level.entities.get(index).lastX = level.entities.get(index).x;
						level.entities.get(index).lastY = level.entities.get(index).y;
						level.update();
						}
					}else {
						level.entities.get(index).x = level.entities.get(index).lastX;
						level.entities.get(index).y = level.entities.get(index).lastY;
						moved = false;
						if(!multiplayer) {
							if(turn == 0) turn = 1;
							else turn = 0;
						}
					}
				}
			}
			if(lookForCheck() == level.entities.get(index - offset).col) {
				level.entities.get(index).x = level.entities.get(index - offset).lastX;
				level.entities.get(index).y = level.entities.get(index - offset).lastY;
				moved = false;
				if(!multiplayer) {
					if(turn == 0) turn = 1;
					else turn = 0;
				}
			}
			
			if(level.entities.get(index - offset).y == 0 && level.entities.get(index - offset).col == 1 && level.entities.get(index - offset) instanceof Pown) {
				level.add(new Queen(level.entities.get(index - offset).x , level.entities.get(index - offset).y , Sprite.w_queen, 1));
				level.entities.get(index - offset).remove();
			}
			if(level.entities.get(index - offset).y == 7 * 16 && level.entities.get(index).col == 0 && level.entities.get(index) instanceof Pown) {
				level.add(new Queen(level.entities.get(index - offset).x , level.entities.get(index - offset).y , Sprite.b_queen, 0));
				level.entities.get(index - offset).remove();
			}
			if(multiplayer) {
			BinaryWriter writer = new BinaryWriter();
			writer.write(Client.opponentsID);
			writer.write(level.entities.get(index - offset).lastX);
			writer.write(level.entities.get(index - offset).lastY);
			writer.write(level.entities.get(index - offset).x);
			writer.write(level.entities.get(index - offset).y);
			byte[] data = new byte[1024];
			data = writer.getBuffer();
			Client.send(data);
			}
			level.entities.get(index - offset).lastX = level.entities.get(index - offset).x;
			level.entities.get(index - offset).lastY = level.entities.get(index - offset).y;
			offset = 0;
		}		

		if(moved && multiplayer) {
			yourMove = false;
			BinaryWriter writer = new BinaryWriter();
			writer.write("/moved/".getBytes());
			writer.write(Client.opponentsID);
			byte[] data = new byte[1024];
			data = writer.getBuffer();
			Client.send(data);
		}
		
		int winner = -1;//lookForCheckMate();
		if(winner != -1) {
			if(winner == 0) {
				System.out.println("white wins");
				running = false;
			}
			if(winner == 1) {
				System.out.println("bleck wins");
				running = false;
			}
		}
		index = -1;
		
		if(!multiplayer) {
			if(turn == 0) turn = 1;
			else turn = 0;
		}
		
	}
	
	/*
	
	private static int lookForCheckMate() {
		if(lookForCheck() != -1) {
			
			if(lookForCheck() == 0) {
				System.out.println("Check with black king");
				//move every posible white move and try if there is check at the and return piece to his positione
				if(checkMate(0)) {
					return 0;
				}
			}
			
			if(lookForCheck() == 1) {
				System.out.println("Check with white king");
				//move every posible bleck move and try if there is check at the and return piece to his positione
				if(checkMate(1)) {
					return 1;
				}
			}
			
		}
		return -1;
	}

	private static boolean checkMate(int num) {
		boolean value = true;
		//if there is no move to prevent checkmate return true
		for(int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if(e.col == num) {
				value = everyPossibleMove(e);
				if(value != true) return value;			
			}			
		}		
		return value;
	}

	private static boolean everyPossibleMove(Entity e) {
		boolean value = true;
		//if returns false there is no checkmate
		for(int i = 0; i < 64; i++) {
			e.x = squeerX[i];
			e.y = squeerY[i];
			if(e.isAllowed()) {
				if(lookForCheck() == e.col) {
					value = true;
				}else {
					value = false;
					//System.out.println(e.x + " " + e.y + " " + i);
					e.x = e.lastX;
					e.y = e.lastY;
					return value;
				}
			}
		}
		e.x = e.lastX;
		e.y = e.lastY;
		return value;
	}
*/
	private static int lookForCheck() {
		//1 is check with white king
		for(int i = 0; i < level.entities.size();i++) {
			Entity entity = level.entities.get(i);
			if(entity instanceof Pown) {
				if(entity.col == 0) {
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 1) {
							if((entity.y / 16 + 1 == e.y / 16 && entity.x / 16 + 1 == e.x / 16)
							|| (entity.y / 16 + 1 == e.y / 16 && entity.x / 16 - 1 == e.x / 16)) {
								return 1;
							}
						}
					}
				}else{
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 0) {
							if((entity.y / 16 - 1 == e.y / 16 && entity.x / 16 + 1 == e.x / 16)
							|| (entity.y / 16 - 1 == e.y / 16 && entity.x / 16 - 1 == e.x / 16)) {
								return 0;
							}
						}
					}
				}			
			}
			if(entity instanceof Knight) {
				if(entity.col == 0) {
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 1) {
							if((entity.y / 16 + 1 == e.y / 16 && entity.x / 16 + 2 == e.x / 16)
							|| (entity.y / 16 + 1 == e.y / 16 && entity.x / 16 - 2 == e.x / 16)
							|| (entity.y / 16 - 1 == e.y / 16 && entity.x / 16 + 2 == e.x / 16)
							|| (entity.y / 16 - 1 == e.y / 16 && entity.x / 16 - 2 == e.x / 16)
							|| (entity.y / 16 + 2 == e.y / 16 && entity.x / 16 + 1 == e.x / 16)
							|| (entity.y / 16 + 2 == e.y / 16 && entity.x / 16 - 1 == e.x / 16)
							|| (entity.y / 16 - 2 == e.y / 16 && entity.x / 16 + 1 == e.x / 16)
							|| (entity.y / 16 - 2 == e.y / 16 && entity.x / 16 - 1 == e.x / 16)) {
								return 1;
							}
						}
					}
				}else{
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 0) {
							if((entity.y / 16 + 1 == e.y / 16 && entity.x / 16 + 2 == e.x / 16)
							|| (entity.y / 16 + 1 == e.y / 16 && entity.x / 16 - 2 == e.x / 16)
							|| (entity.y / 16 - 1 == e.y / 16 && entity.x / 16 + 2 == e.x / 16)
							|| (entity.y / 16 - 1 == e.y / 16 && entity.x / 16 - 2 == e.x / 16)
							|| (entity.y / 16 + 2 == e.y / 16 && entity.x / 16 + 1 == e.x / 16)
							|| (entity.y / 16 + 2 == e.y / 16 && entity.x / 16 - 1 == e.x / 16)
							|| (entity.y / 16 - 2 == e.y / 16 && entity.x / 16 + 1 == e.x / 16)
							|| (entity.y / 16 - 2 == e.y / 16 && entity.x / 16 - 1 == e.x / 16)) {
								return 0;
							}
						}
					}
				}			
			}
			if(entity instanceof King) {
				if(entity.col == 0) {
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 1) {
							if(entity.y / 16 + 1 >= e.y / 16 && entity.y / 16 - 1 <= e.y / 16 
							&& entity.x / 16 + 1 >= e.x / 16 && entity.x / 16 - 1 <= e.x / 16) {
								return 1;
							}
						}
					}
				}else{
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 0) {
							if(entity.y / 16 + 1 >= e.y / 16 && entity.y / 16 - 1 <= e.y / 16 
							&& entity.x / 16 + 1 >= e.x / 16 && entity.x / 16 - 1 <= e.x / 16) {
								return 0;
							}
						}
					}
				}
			}
			if(entity instanceof Queen) {
				if(entity.col == 0) {
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 1) {
							if(!inBetween(entity.x, entity.y, e.x, e.y, 2)) {
								return 1;
							}
						}
					}
				}else{
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 0) {
							if(!inBetween(entity.x, entity.y, e.x, e.y, 2)) {
								return 0;
							}
						}
					}
				}				
			}
			if(entity instanceof Bishop) {
				if(entity.col == 0) {
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 1) {
							if(!inBetween(entity.x, entity.y, e.x, e.y, 1)) {
								return 1;
							}
						}
					}
				}else{
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 0) {
							if(!inBetween(entity.x, entity.y, e.x, e.y, 1)) {
								return 0;
							}
						}
					}
				}				
			}
			if(entity instanceof Rook) {
				if(entity.col == 0) {
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 1) {
							if(!inBetween(entity.x, entity.y, e.x, e.y, 0)) {
								return 1;
							}
						}
					}
				}else{
					for(int i1 = 0; i1 < level.entities.size(); i1++) {
						Entity e = level.entities.get(i1);
						if(e instanceof King && e.col == 0) {
							if(!inBetween(entity.x, entity.y, e.x, e.y, 0)) {
								return 0;
							}
						}
					}
				}				
			}
		}
		return -1;
	}
	
	public static void moveSelected() {
		if(index != -1) {
			level.entities.get(index).x = Mouse.getX() / 4 - 8;
			level.entities.get(index).y = Mouse.getY() / 4 - 8;
		}
	}
	
	public static void getSelectedPiece() {
		index = -1;
		for(int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if(e.x / 16 == Mouse.getX() / (4 * 16) && e.y / 16 == Mouse.getY() / (4 * 16)) {
				if(multiplayer && e.col == turn && yourMove) {
					index = i;
				}else if(e.col == turn && !multiplayer) {
					index = i;
				}
			}
			
		}
		
	}
	
	private static boolean inBetween(int lx, int ly, int nx, int ny, int watch) {
		//0 = rook / 1 bishop / 2 queen
		if(lx != nx && ly != ny && Math.abs(lx - nx) != Math.abs(ly - ny)) {
			return true;
		}
		if(watch == 0 || watch == 2) {
			if(lx == nx) {
				for(int i = 0; i < level.entities.size(); i++) {
					Entity e = level.entities.get(i);
					if(e.x == nx) {
						if((e.y < ly && e.y > ny) ||(e.y > ly && e.y < ny)) {
							return true;
						}
					}
				}
				return false;
			}
			if(ly == ny) {
				for(int i = 0; i < level.entities.size(); i++) {
					Entity e = level.entities.get(i);
					if(e.y == ny) {
						if((e.x < lx && e.x > nx) ||(e.x > lx && e.x < nx)) {
							return true;
						}
					}
				}
				return false;
			}
		}
		if(watch == 1 || watch == 2) {
			if(lx < nx && ly < ny) {
				for(int i = 1; i < nx / 16 - lx / 16; i++) {
					for(int a = 0; a < level.entities.size(); a++) {
						Entity e = level.entities.get(a);
						if(lx / 16 + i == e.x / 16 && ly / 16 + i == e.y / 16) {
							return true;
						}
					}
				}
				return false;
			}
			if(lx > nx && ly > ny) {
				for(int i = 1; i < lx / 16 - nx / 16; i++) {
					for(int a = 0; a < level.entities.size(); a++) {
						Entity e = level.entities.get(a);
						if(lx / 16 - i == e.x / 16 && ly / 16 - i == e.y / 16) {
							return true;
						}
					}
				}
				return false;
			}
			if(lx > nx && ly < ny) {
				for(int i = 1; i < lx / 16 - nx / 16; i++) {
					for(int a = 0; a < level.entities.size(); a++) {
						Entity e = level.entities.get(a);
						if(lx / 16 - i == e.x / 16 && ly / 16 + i == e.y / 16) {
							return true;
						}
					}
				}
				return false;
			}
			if(lx < nx && ly > ny) {
				for(int i = 1; i < nx / 16 - lx / 16; i++) {
					for(int a = 0; a < level.entities.size(); a++) {
						Entity e = level.entities.get(a);
						if(lx / 16 + i == e.x / 16 && ly / 16 - i == e.y / 16) {
							return true;
						}
					}
				}
				return false;
			}
		}
		return true;
	}
	
	private void onBoard() {
		//black
		for(int i = 1; i <= 8; i++) {
			level.add(new Pown(i * 16, 16, Sprite.b_pown, 0));
		}
		level.add(new Rook(16, 0, Sprite.b_rook, 0));
		level.add(new Rook(8 * 16, 0, Sprite.b_rook, 0));
		level.add(new Knight(2 * 16, 0, Sprite.b_knight, 0));
		level.add(new Knight(7 * 16, 0, Sprite.b_knight, 0));
		level.add(new Bishop(3 * 16, 0, Sprite.b_bishop, 0));
		level.add(new Bishop(6 * 16, 0, Sprite.b_bishop, 0));
		level.add(new Queen(4 * 16, 0, Sprite.b_queen, 0));
		level.add(new King(5 * 16, 0, Sprite.b_king, 0));		
		//white
		for(int i = 1; i <= 8; i++) {
			level.add(new Pown(i * 16, 6 * 16, Sprite.w_pown, 1));
		}
		level.add(new Rook(16, 7 * 16, Sprite.w_rook, 1));
		level.add(new Rook(8 * 16, 7 * 16, Sprite.w_rook, 1));
		level.add(new Knight(2 * 16, 7 * 16, Sprite.w_knight, 1));
		level.add(new Knight(7 * 16, 7 * 16, Sprite.w_knight, 1));
		level.add(new Bishop(3 * 16, 7 * 16, Sprite.w_bishop, 1));
		level.add(new Bishop(6 * 16, 7 * 16, Sprite.w_bishop, 1));
		level.add(new Queen(4 * 16, 7 * 16, Sprite.w_queen, 1));
		level.add(new King(5 * 16, 7 * 16, Sprite.w_king, 1));
		//defining all squers
		squeerX = new int[8*8];
		squeerY = new int[8*8];
		int counter = 0;
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				squeerX[counter] = x * 16 + 16;
				squeerY[counter] = y * 16;
				counter++;
			}
		}
		
	}
	
	public static int getWindowWidth(){
		return width * scale;
	}
	
	public static int getWindowHeight(){
		return height * scale;
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}

	public synchronized void stop() {
		running = false;
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
			while(delta >=1){
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				frame.setTitle("Chess | " + updates + " ups, " + frames +" fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void update() {
		key.update(0);
		level.update();
	}
	/*
	private byte[] getBynaryFtomInt(int num) {
		byte[] data = new byte[4];
		data[0] = (byte)((num >> 24) & 0xff);
		data[1] = (byte)((num >> 16) & 0xff);
		data[2] = (byte)((num >> 8) & 0xff);
		data[3] = (byte)((num >> 0) & 0xff);
		return data;
	}*/
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		screen.clear();
		level.render(screen);
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}
		Graphics g = bs.getDrawGraphics();
		g.setFont(new Font("Ariel", 0, 35));
		g.drawImage(image, 0 , 0, width * scale, height * scale, null);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Launcher();
		//gameStart();
	}

	public static void gameStart(){
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle("Chess");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setSize(getWindowWidth(), getWindowHeight());
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		game.frame.setIconImage(new  ImageIcon(Game.class.getResource("/textures/chessIcon.png")).getImage());
		game.start();
	}

}



