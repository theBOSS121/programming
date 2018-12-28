package com.theBOSS.shooter_pc.ui;

import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.game.Game;
import com.theBOSS.shooter_pc.game.Online;
import com.theBOSS.shooter_pc.graphics.Light;
import com.theBOSS.shooter_pc.graphics.Sprite;

public class Menu {
	
	public static final int FIRST_MENU = 0, MODES_MENU = 1, OPTIONS_MENU = 2, HELP_MENU = 3;
	
	int state = FIRST_MENU;
	
	Button play, options, help, survive, online, back, sounds, client_server;
	
	private Light l;
	
	public static boolean isClient = true;
	
	public Menu() {
		l = new Light(80, 0xffffffff);
		
		play = new Button(Shooter.WIDTH / 2 - Sprite.play.width / 2, Shooter.HEIGHT / 4 - Sprite.play.height / 2, Sprite.play, Sprite.play_clicked);
		options = new Button(Shooter.WIDTH / 2 - Sprite.options.width / 2, Shooter.HEIGHT / 4 * 2 - Sprite.options.height / 2, Sprite.options, Sprite.options_clicked);
		help = new Button(Shooter.WIDTH / 2 - Sprite.help.width / 2, Shooter.HEIGHT / 4 * 3 - Sprite.help.height / 2, Sprite.help, Sprite.help_clicked);
		survive = new Button(Shooter.WIDTH / 2 - Sprite.survive.width / 2, Shooter.HEIGHT / 4 * 2 - Sprite.survive.height / 2, Sprite.survive, Sprite.survive_clicked);
		online = new Button(Shooter.WIDTH / 2 - Sprite.online.width / 2, Shooter.HEIGHT / 4 - Sprite.online.height / 2, Sprite.online, Sprite.online_clicked);
		sounds = new Button(Shooter.WIDTH / 2 - Sprite.sounds.width / 2, Shooter.HEIGHT / 4 * 2 - Sprite.sounds.height / 2, Sprite.sounds, Sprite.sounds_clicked);
		client_server = new Button(Shooter.WIDTH / 2 - Sprite.client.width / 2, Shooter.HEIGHT / 4 * 1 - Sprite.client.height / 2, Sprite.client, Sprite.client_clicked);
		back = new Button(Shooter.WIDTH / 2 - Sprite.back.width / 2, Shooter.HEIGHT / 4 * 3 - Sprite.back.height / 2, Sprite.back, Sprite.back_clicked);
	}
	
	public void update() {
		Shooter.screen.setAmbientColor(0xff121212);	
		if(state == FIRST_MENU) {
			play.update();
			options.update();
			help.update();
			if(play.clicked) {state = MODES_MENU; play.clicked = false;}
			if(options.clicked) {state = OPTIONS_MENU; options.clicked = false;}
			if(help.clicked) {state = HELP_MENU; help.clicked = false;}
		}
		if(state == MODES_MENU) {
			survive.update();
			online.update();
			back.update();
			if(survive.clicked) {Game.state = Game.SURVIVE; survive.clicked = false;}
			if(online.clicked) {Game.state = Game.ONLINE; online.clicked = false; Online.onlineModeEntered();}
			if(back.clicked) {state = FIRST_MENU; back.clicked = false;}
		}
		if(state == OPTIONS_MENU) {
			back.update();
			sounds.update();
			client_server.update();
			if(back.clicked) {state = FIRST_MENU; back.clicked = false;}
			if(sounds.clicked) {
				if(sounds.sprite == Sprite.sounds) {sounds.sprite = Sprite.no_sounds; sounds.clickedSprite = Sprite.no_sounds_clicked;}
				else {sounds.sprite = Sprite.sounds; sounds.clickedSprite = Sprite.sounds_clicked;}
				sounds.clicked = false;
				//logic missing
			}if(client_server.clicked) {
				if(client_server.sprite == Sprite.client) {isClient = false; client_server.sprite = Sprite.server; client_server.clickedSprite = Sprite.server_clicked; client_server.x = Shooter.WIDTH / 2 - Sprite.server.width / 2;}
				else {isClient = true; client_server.sprite = Sprite.client; client_server.clickedSprite = Sprite.client_clicked; client_server.x = Shooter.WIDTH / 2 - Sprite.client.width / 2;}
				client_server.clicked = false;
				//logic missing
			}
		}
		if(state == HELP_MENU) {
			back.update();
			if(back.clicked) {state = FIRST_MENU; back.clicked = false;}
		}
		
	}
	
	public void render() {
		if(state == FIRST_MENU) {
			play.render();
			options.render();
			help.render();
		}
		if(state == MODES_MENU) {
			survive.render();
			online.render();
			back.render();
		}
		if(state == OPTIONS_MENU) {
			back.render();
			sounds.render();
			client_server.render();
		}
		if(state == HELP_MENU) {
			back.render();
		}
		l.clear();
		Shooter.screen.renderLight(l, 50, 75);
	}

}
