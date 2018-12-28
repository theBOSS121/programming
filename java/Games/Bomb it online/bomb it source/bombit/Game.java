
package com.bombit;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.bombit.entity.mob.Bot;
import com.bombit.entity.mob.Player;
import com.bombit.graphics.Screen;
import com.bombit.gui.Launcher;
import com.bombit.input.Keyboard;
import com.bombit.input.Mouse;
import com.bombit.level.Level;
import com.bombit.net.Client;


public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	static int width = 285;
	static int height = 295;
	public static int scale = 3;
	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	private Keyboard key2;
	public static Level level;
	public static Player player;
	public static Player player2;
	public static Bot bot1;
	public static Bot bot2;
	public static Bot bot3;
	private boolean running = false;
	public static int players = 1;
	public static int col1 = 0;
	public static int col2 = 0;
	public static boolean multiplayer = false;
	public static boolean started = false;

	private static UIManager uiManager;
	
	private Screen screen;
	public Client client;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels;

	public Game() {
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		screen = new Screen(width, height);
		uiManager = new UIManager();
		frame = new JFrame();
		key = new Keyboard();
		key2 = new Keyboard();
		//network stuff
		if(multiplayer) {
			player = new Player("Luka", key);
			level.add(player);
			player.col = col1;
			player.bombs = 2;
			players = 1;
			client = new Client("192.168.1.71", 8192);
			if(!client.connect()){
				//doesnt work
				System.out.println("Can't connect to the server");
			}
		}
		
		if(!multiplayer) {
			started = true;
			player = new Player("Luka", 24, 16, key);
			level.add(player);
			player.col = col1;
			if(players == 2) {
				player2 = new Player("Luka2", 264, 256, key2);
				level.add(player2);
				player2.col = col2;
			}
			
			bot1 = new Bot("Bot1", 24, 256);
			level.add(bot1);
			bot1.col = 0xff00ff;
			bot2 = new Bot("Bot2", 264, 16);
			level.add(bot2);
			bot2.col = 0x00ffff;
			if(players == 1) {
				bot3 = new Bot("Bot3", 264, 256);
				level.add(bot3);
				bot3.col = 0xffff00;
			}
			
			
		}
		addKeyListener(key);
		addKeyListener(key2);
		
		Mouse mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	
	public static int getWindowWidth(){
		return width * scale;
	}
	
	public static int getWindowHeight(){
		return height * scale;
	}
	
	public static UIManager getUIManager(){
		return uiManager;
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
				frame.setTitle("Bomb it | " + updates + " ups, " + frames +" fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void update() {
		if(started) {
			key.update(0);
			key2.update(1);
			level.update();
		}
		if(multiplayer) {
			int num = 0;
			if(player.animSprite == player.up) num = 0;
			//if(player.sprite == Sprite.player_forward_1) num = 1;
			//if(player.sprite == Sprite.player_forward_2) num = 2;
			if(player.animSprite == player.down) num = 3;
			//if(player.sprite == Sprite.player_back_1) num = 4;
			//if(player.sprite == Sprite.player_back_2) num = 5;
			if(player.animSprite == player.right) num = 6;
			//if(player.sprite == Sprite.player_side_1) num = 7;
			//if(player.sprite == Sprite.player_side_2) num = 8;
			if(player.animSprite == player.left) num = 9;
			//if(player.sprite == Sprite.player_side2_1) num = 10;
			//if(player.sprite == Sprite.player_side2_2) num = 11;
			
			byte[] data = new byte[1024];
			byte[] userID = getBynaryFtomInt(client.userID);
			byte[] x = getBynaryFtomInt(player.getX());
			byte[] y = getBynaryFtomInt(player.getY());
			byte[] spriteNum = getBynaryFtomInt(num);
			byte[] bombs = getBynaryFtomInt(0);
			if(player.reloadTime == 17) {
				bombs = getBynaryFtomInt(1);				
			}
			
			for(int i = 0; i < 4; i++){
				data[i] = userID[i];
				data[i + 4] = x[i];
				data[i + 8] = y[i];
				data[i + 12] = spriteNum[i];
				data[i + 16] = bombs[i];
			}
			Client.send(data);
		}
	}
	
	private byte[] getBynaryFtomInt(int num) {
		byte[] data = new byte[4];
		data[0] = (byte)((num >> 24) & 0xff);
		data[1] = (byte)((num >> 16) & 0xff);
		data[2] = (byte)((num >> 8) & 0xff);
		data[3] = (byte)((num >> 0) & 0xff);
		return data;
	}
	
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
		if(player.x == 0.0 || player.y == 0.0) {
			g.setColor(Color.WHITE);
			g.drawString("Can't connect to the server", 75, 200);
			g.drawString("Server doesn't work at the moment", 15, 300);
		}else if(!started) {
			g.setColor(Color.WHITE);
			g.drawString("Wait for the players...", 115, 250);
		}
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		new Launcher(0);
	}

	public static void gameStart(){
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle("Bomb it");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setSize(getWindowWidth(), getWindowHeight());
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		game.start();
	}

}



