package com.theBOSS.Platformer.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.theBOSS.Platformer.Camera;
import com.theBOSS.Platformer.Client;
import com.theBOSS.Platformer.Platformer;
import com.theBOSS.Platformer.Screen;
import com.theBOSS.Platformer.entity.Player;
import com.theBOSS.Platformer.graphics.Light;
import com.theBOSS.Platformer.graphics.Sprite;

public class Level {

	public static final int TILE_WIDTH = Sprite.tile1.width, TILE_HEIGHT = TILE_WIDTH;
	public int lvlWidth, lvlHeight;
	private int[] tiles;
	
	private Camera camera;
	private Screen screen;
	private Player player;
	
	public static Level level1 = new Level("/level.png");
	
	private Light players = new Light(100, 0xffffffff);
	private Light enemys = new Light(100, 0xffffffff);
	
	private boolean reversedDayNightCycle = false;

	public Level(String path) {
		try {
			BufferedImage image = ImageIO.read(Sprite.class.getResource(path));
			lvlWidth = image.getWidth();
			lvlHeight = image.getHeight();
			tiles = image.getRGB(0, 0, lvlWidth, lvlHeight, null, 0, lvlWidth);
		}catch(IOException e) {
			e.printStackTrace();
		}	
		this.screen = Platformer.screen;
		player = new Player(2 * TILE_WIDTH, 2 * TILE_HEIGHT, Sprite.player);
		camera = new Camera((int) player.x + player.width / 2 - Platformer.WIDTH / 2, (int) player.y + player.height / 2 - Platformer.HEIGHT / 2);
		
	}
	
	int time = 0;
	
	public void update() {
		time++;
		if(time % 10 == 0){
			int r = (screen.getAmbientColor() >> 16) & 0xff;
			int g = (screen.getAmbientColor() >> 8) & 0xff;
			int b = (screen.getAmbientColor()) & 0xff;
			if(r >= 240 || g >= 240 || b >= 240) {
				reversedDayNightCycle = false; r = 240; g = 240; b = 240;
			}
			else if(r < 10 || g < 10 || b < 10) {
				reversedDayNightCycle = true;
			}
			if(reversedDayNightCycle) {
				r += 1;
				g += 1;
				b += 1;
			}else {
				r -= 1;
				g -= 1;
				b -= 1;				
			}
			screen.setAmbientColor(0xff << 24 | r << 16 | g << 8 | b);
		}
		Platformer.enemy.update();
		player.update();
		camera.update(player, this);
		screen.setOffSet(camera.getOffX(), camera.getOffY());
		
		//send players position
		String update = "update";
		update += "/x/" + (int) player.x + "/y/" + (int) player.y + "/e/";
		Client.send(update.getBytes());
	}
	
	public void render() {
		renderLevel();
		Platformer.enemy.render(screen);
		player.render(screen);
	}
	
	private void renderLevel() {
		int y0 = (int) Math.floor(camera.getOffY() / TILE_HEIGHT);
		int y1 = (int) Math.ceil(Platformer.HEIGHT / TILE_HEIGHT) + 2;
		int x0 = (int) Math.floor(camera.getOffX() / TILE_WIDTH);
		int x1 = (int) Math.ceil(Platformer.WIDTH / TILE_WIDTH) + 2;
		for(int y = y0; y < y0 + y1; y++) {
			for(int x = x0; x < x0 + x1; x++) {
				screen.renderSprite(getTileSprite(x, y), x * TILE_WIDTH, y * TILE_HEIGHT);
			}
		}
		enemys.clear();
		screen.renderLight(enemys, (int) Platformer.enemy.x + Platformer.enemy.width - 2, (int) Platformer.enemy.y + Platformer.enemy.height / 2 + 8);

		players.clear();
		screen.renderLight(players, (int) player.x + player.width - 2, (int) player.y + player.height / 2 + 8);
	}

	public Sprite getTileSprite(int x, int y) {
		if(x < 0 || x >= lvlWidth || y < 0 || y >= lvlHeight) return Sprite.tile2;
		if(tiles[x + y * lvlWidth] == 0xff000000) return Sprite.tile2;
		if(tiles[x + y * lvlWidth] == 0xffffffff) return Sprite.tile1;
		else return Sprite.tile2;
	}	
	
	public int getTile(int x, int y) {
		if(x < 0 || x >= lvlWidth || y < 0 || y >= lvlHeight) return -1;
		return tiles[x + y * lvlWidth];
	}
}
