package com.theBOSS.Nalis_pc.game;

import com.theBOSS.Nalis_pc.Camera;
import com.theBOSS.Nalis_pc.Nails;
import com.theBOSS.Nalis_pc.entity.Bumper;
import com.theBOSS.Nalis_pc.entity.Player;
import com.theBOSS.Nalis_pc.graphics.Light;
import com.theBOSS.Nalis_pc.graphics.Screen;
import com.theBOSS.Nalis_pc.graphics.Sprite;

public class Game {
	
	private Player player;
	private Camera camera;
	private Bumper bumper;
	
	private Light l = new Light(50, 0xffffffff);
	private Light l1 = new Light(50, 0xffffffff);
	private Light l2 = new Light(50, 0xffffffff);
	private Light l3 = new Light(50, 0xffffffff);
	private Light l4 = new Light(50, 0xffffffff);
	
	public Game() {
		player = new Player(Nails.WIDTH / 2 - Sprite.player.width / 2, Nails.HEIGHT / 2 - Sprite.player.height / 2, Sprite.player);		
		camera = new Camera(0, 0);
		bumper = new Bumper(Nails.WIDTH / 2 - Sprite.bumper.width / 2, Nails.HEIGHT / 2 + Sprite.bumper.height / 2, Sprite.bumper);
	}
	
	public void update() {
		Nails.screen.setAmbientColor(0xff333333);
		player.update();
		bumper.update();
		checkForCollision();
		camera.update(player, 70, this);
		Nails.screen.setOffSet(camera.getOffX(), camera.getOffY());
	}
	
	private void checkForCollision() {
//		if(player.x + player.width > bumper.x && player.x < bumper.x + bumper.width && player.y + player.height > bumper.y && player.y < bumper.y + bumper.height) {
//			System.out.println("collision with bitmaps");
//		}
		
		for(int y = 0; y < bumper.height; y++) {
			for(int x = 0; x < bumper.width; x++) {
				if(bumper.sprite.pixels[x + y * bumper.sprite.width] != 0x00ffffff) {
					int xx = x + bumper.getOnScreenX();
					int yy = y + bumper.getOnScreenY();
					if(player.getOnScreenX() <= xx && player.getOnScreenX() + player.width - 1 >= xx && player.getOnScreenY() <= yy && player.getOnScreenY() + player.height - 1 >= yy) {
//						System.out.println("colliding");
					}
				}
			}
		}
		
	}

	public void render() {
		//lights stuff before
		l.clear();
		Nails.screen.renderLight(l, (int) player.x + player.width / 2, (int) player.y + player.height / 2);
		l1.clear();
		Nails.screen.renderLight(l1, (int) 20 + Nails.screen.offX, (int) 100 + Nails.screen.offY);
		l2.clear();
		Nails.screen.renderLight(l2, (int) 80 + Nails.screen.offX, 100 + Nails.screen.offY);
		l3.clear();
		Nails.screen.renderLight(l3, (int) 80 + Nails.screen.offX, (int) 60 + Nails.screen.offY);
		l4.clear();
		Nails.screen.renderLight(l4, (int) 20 + Nails.screen.offX, (int) 60 + Nails.screen.offY);
		bumper.render();
		player.render();
	}
}
