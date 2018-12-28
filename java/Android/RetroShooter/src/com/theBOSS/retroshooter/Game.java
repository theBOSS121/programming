package com.theBOSS.retroshooter;

public class Game {
	
	public static final int MENU = 0, SURVIVE = 1, ONLINE = 2;
	
	private Joystick movingJoystick, shootingJoystick; 
	
	public static int state = MENU;
	
	private Menu menu = new Menu();
	private Survive survive;
	private Online online;
	
	public Game() {
		movingJoystick = new Joystick(2, 133, 0);
		shootingJoystick = new Joystick(68, 133, 1);	
		survive = new Survive(movingJoystick, shootingJoystick);
		online = new Online(movingJoystick, shootingJoystick);
	}
	public void update() {		
		if(state == MENU) {
			menu.update();
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
		Renderer.screen.clear();
		if(state == MENU) {
			menu.render();
		}
		if(state == SURVIVE) {
			survive.render();
		}
		if(state == ONLINE) {
			online.render();
		}
						
		if(state != MENU) {
			movingJoystick.render();
			shootingJoystick.render();
		}
		
		Renderer.screen.process();
	}
}
