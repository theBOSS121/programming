package com.theBOSS.shooter_pc.game;

import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.input.Joystick;
import com.theBOSS.shooter_pc.ui.Menu;

public class Game {
	
	public static final int MENU = 0, SINGLE = 1, SURVIVE = 2, ONLINE = 3;
	
	private Joystick movingJoystick, shootingJoystick; 
	
	public static int state = MENU;
	
	private Menu menu = new Menu();
	private Survive survive;
	private Single single;
	private Online online;
	
	public Game() {
		movingJoystick = new Joystick(2, 133);
		shootingJoystick = new Joystick(68, 133);	
		survive = new Survive(movingJoystick, shootingJoystick);
		single = new Single(movingJoystick, shootingJoystick);
		online = new Online(movingJoystick, shootingJoystick);
	}
	public void update() {		
		if(state == MENU) {
			menu.update();
		}
		if(state == SINGLE) {
			single.update();
		}
		if(state == SURVIVE) {
			survive.update();
		}
		if(state == ONLINE) {
			online.update();
		}
		
		if(state != MENU) {
			movingJoystick.update();
			shootingJoystick.update();
		}	
	}
	

	public void render() {
		Shooter.screen.clear();
		if(state == MENU) {
			menu.render();
		}
		if(state == SINGLE) {
			single.render();
		}
		if(state == SURVIVE) {
			survive.render();
		}
		if(state == ONLINE) {
			online.render();
		}
				
		Shooter.screen.process();

		if(state != MENU) {
			movingJoystick.render();
			shootingJoystick.render();
		}
	}
}
