package com.theBOSS.shooter_pc.game;

import com.theBOSS.shooter_pc.Camera;
import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.entity.Player;
import com.theBOSS.shooter_pc.graphics.Sprite;
import com.theBOSS.shooter_pc.input.Joystick;
import com.theBOSS.shooter_pc.udp.Client;
import com.theBOSS.shooter_pc.udp.Server;
import com.theBOSS.shooter_pc.ui.Menu;

public class Online {
	
	Player player;
	Joystick movingJoystick, shootingJoystick;
	Camera camera;
	
	static Server server = null;
	static Client client = null;
	
	public Online(Joystick movingJoystick, Joystick shootingJoystick) {
		
		player = new Player(0, 0, Sprite.player, movingJoystick, shootingJoystick);
		camera = new Camera(0, 0);
		
	}
	
	public static void onlineModeEntered() {
		if(Menu.isClient) {
			client = new Client("localhost", 8192);
			client.connect();
		}else {
			server = new Server(8192);
			server.start();
		}
	}
	
	
	public void update() {
		Shooter.screen.setAmbientColor(0xff666666);
		player.update();
		camera.update(player, 0, 0);
		Shooter.screen.setOffSet(camera.getOffX(), camera.getOffY());
	}
	
	public void render() {
		player.render();
	}

}
