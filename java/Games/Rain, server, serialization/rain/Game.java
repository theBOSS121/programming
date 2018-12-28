package com.thecherno.rain;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.sql.ClientInfoStatus;

import javax.swing.JFrame;

import com.thecherno.rain.entity.mob.Player;
import com.thecherno.rain.graphics.Screen;
import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.graphics.UI.UIManager;
import com.thecherno.rain.input.Keyboard;
import com.thecherno.rain.input.Mouse;
import com.thecherno.rain.level.Level;
import com.thecherno.rain.level.TileCoordinate;
import com.thecherno.rain.net.Client;
import com.thecherno.raincloud.serialization.RCDatabase;
import com.thecherno.raincloud.serialization.RCField;
import com.thecherno.raincloud.serialization.RCObject;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	private static int width = 300;
	private static int height = 168;
	private static int scale = 3;

	private Thread thread;
	private JFrame frame;
	private Keyboard key;
	public static Level level;
	private Player player;
	private boolean running = false;

	private static UIManager uiManager;
	
	private Screen screen;
	public Client client;
	
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels;
	private int time = 0;

	public Game() {
		setSize();

		screen = new Screen(width, height);
		uiManager = new UIManager();
		frame = new JFrame();
		key = new Keyboard();
		
		client = new Client("192.168.1.71", 8192);
		if(!client.connect()){
			//doesnt work
			System.out.println("Can't connect to the server");
		}
		
		level = level.spawn;
		TileCoordinate playerSpawn = new TileCoordinate(18,42);
		player = new Player("Luka", playerSpawn.x(), playerSpawn.y(), key);
		level.add(player);
		
		addKeyListener(key);
		
		Mouse mouse = new Mouse();
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		
		save();
	}
	
	private void setSize(){
		RCDatabase db = RCDatabase.DeserializeFromFile("res/data/screen.bin");
		if(db != null){
			RCObject obj = db.findObject("Resolution");
			width = obj.findField("width").getInt();
			height = obj.findField("height").getInt();
			scale = obj.findField("scale").getInt();
		}
		
		Dimension size = new Dimension(width * scale + 80 * 3, height * scale);
		setPreferredSize(size);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
	
	private void save(){
		RCDatabase db = new RCDatabase("Screen");
		
		RCObject obj = new RCObject("Resolution");
		obj.addField(RCField.Integer("width", width));
		obj.addField(RCField.Integer("height", height));
		obj.addField(RCField.Integer("scale", scale));
		db.addObject(obj);
		
		db.serializeToFile("res/data/screen.bin");
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
				frame.setTitle("Rain | " + updates + " ups, " + frames +" fps");
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void update() {
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
		time++;
		key.update();
		level.update();
		uiManager.update();
			byte[] data = new byte[1024];
			byte[] userID = getBynaryFtomInt(client.userID);
			byte[] x = getBynaryFtomInt(player.getX());
			byte[] y = getBynaryFtomInt(player.getY());
			byte[] spriteNum = getBynaryFtomInt(num);
			for(int i = 0; i < 4; i++){
				data[i] = userID[i];
				data[i + 4] = x[i];
				data[i + 8] = y[i];
				data[i + 12] = spriteNum[i];
			}
			Client.send(data);
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
		double xScroll = player.getX() - screen.width / 2;
		double yScroll = player.getY() - screen.height / 2;
		level.render((int)xScroll, (int)yScroll, screen);
		//font.render(50, 50, -3, 0xffffff, "Hey guys,\nmy name\nis luka", screen);
		for(int i = 0; i < pixels.length; i++){
			pixels[i] = screen.pixels[i];
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0 , 0, width * scale, height * scale, null);
		uiManager.render(g);
		//g.fillRect(Mouse.getX()-32, Mouse.getY()-32, 64, 64);
		//if(Mouse.getButton() != -1) g.drawString("Button:" + Mouse.getButton(),  80, 80);
		g.dispose();
		bs.show();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle("Rain");
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);

		game.start();
		
	}

}
