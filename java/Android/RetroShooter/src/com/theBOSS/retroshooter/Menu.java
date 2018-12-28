package com.theBOSS.retroshooter;

public class Menu {
	
	public static final int FIRST_MENU = 0, MODES_MENU = 1, OPTIONS_MENU = 2, HELP_MENU = 3;
	
	int state = FIRST_MENU;
	
	Button play, options, help, survive, online, back, sounds, client_server;
	
	private Light l;
	
	public static boolean isClient = true;
	
	public Menu() {
		l = new Light(75, 0xffffffff);
		
		play = new Button(Renderer.WIDTH / 2 - Renderer.play.width / 2, Renderer.HEIGHT / 4 - Renderer.play.height / 2, Renderer.play, Renderer.play_clicked);
		options = new Button(Renderer.WIDTH / 2 - Renderer.options.width / 2, Renderer.HEIGHT / 4 * 2 - Renderer.options.height / 2, Renderer.options, Renderer.options_clicked);
		help = new Button(Renderer.WIDTH / 2 - Renderer.help.width / 2, Renderer.HEIGHT / 4 * 3 - Renderer.help.height / 2, Renderer.help, Renderer.help_clicked);
		survive = new Button(Renderer.WIDTH / 2 - Renderer.survive.width / 2, Renderer.HEIGHT / 4 * 2 - Renderer.survive.height / 2, Renderer.survive, Renderer.survive_clicked);
		online = new Button(Renderer.WIDTH / 2 - Renderer.online.width / 2, Renderer.HEIGHT / 4 - Renderer.online.height / 2, Renderer.online, Renderer.online_clicked);
		sounds = new Button(Renderer.WIDTH / 2 - Renderer.sounds.width / 2, Renderer.HEIGHT / 4 * 2 - Renderer.sounds.height / 2, Renderer.sounds, Renderer.sounds_clicked);
		client_server = new Button(Renderer.WIDTH / 2 - Renderer.client.width / 2, Renderer.HEIGHT / 4 * 1 - Renderer.client.height / 2, Renderer.client, Renderer.client_clicked);
		back = new Button(Renderer.WIDTH / 2 - Renderer.back.width / 2, Renderer.HEIGHT / 4 * 3 - Renderer.back.height / 2, Renderer.back, Renderer.back_clicked);
	}
	
	public void update() {
		Renderer.screen.setAmbientColor(0xff111111);	
		if(state == FIRST_MENU) {
			play.update();
			options.update();
			help.update();
			if(play.clicked) {state = MODES_MENU; play.clicked = false;}
			if(options.clicked) {state = OPTIONS_MENU; options.clicked = false;}
			if(help.clicked) {state = HELP_MENU; help.clicked = false;}
		}
		if(state == MODES_MENU) {
			online.update();
			survive.update();
			back.update();
			if(online.clicked) {
				if(!isClient) RetroShooter.server();
				else RetroShooter.client();
				Game.state = Game.ONLINE; 
				Renderer.screen.setAmbientColor(0xff666666);
				online.clicked = false;
			}
			if(survive.clicked) {Game.state = Game.SURVIVE; survive.clicked = false;}
			if(back.clicked) {state = FIRST_MENU; back.clicked = false;}
		}
		if(state == OPTIONS_MENU) {
			back.update();
			sounds.update();
			client_server.update();
			if(back.clicked) {state = FIRST_MENU; back.clicked = false;}
			if(sounds.clicked) {
				if(sounds.sprite == Renderer.sounds) {sounds.sprite = Renderer.no_sounds; sounds.clickedSprite = Renderer.no_sounds_clicked;}
				else {sounds.sprite = Renderer.sounds; sounds.clickedSprite = Renderer.sounds_clicked;}
				sounds.clicked = false;
				//logic missing
			}if(client_server.clicked) {
				if(client_server.sprite == Renderer.client) {isClient = false; client_server.sprite = Renderer.server; client_server.clickedSprite = Renderer.server_clicked; client_server.x = Renderer.WIDTH / 2 - Renderer.server.width / 2;}
				else {isClient = true; client_server.sprite = Renderer.client; client_server.clickedSprite = Renderer.client_clicked; client_server.x = Renderer.WIDTH / 2 - Renderer.client.width / 2;}
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
			online.render();
			survive.render();
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
		Renderer.screen.renderLight(l, 50, 77);
	}

}
