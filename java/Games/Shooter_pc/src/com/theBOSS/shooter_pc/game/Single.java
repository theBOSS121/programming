package com.theBOSS.shooter_pc.game;

import com.theBOSS.shooter_pc.Camera;
import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.entity.Player;
import com.theBOSS.shooter_pc.graphics.Sprite;
import com.theBOSS.shooter_pc.input.Joystick;

public class Single {
	
	Player player;
	Joystick movingJoystick, shootingJoystick;
	Camera camera;
	
	public Single(Joystick movingJoystick, Joystick shootingJoystick) {
		player = new Player(0, 0, Sprite.player, movingJoystick, shootingJoystick);
		camera = new Camera(0, 0);
		
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
