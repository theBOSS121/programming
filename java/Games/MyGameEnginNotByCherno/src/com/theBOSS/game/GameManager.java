package com.theBOSS.game;


import java.util.ArrayList;

import com.theBOSS.engine.AbstractGame;
import com.theBOSS.engine.GameContainer;
import com.theBOSS.engine.Renderer;
import com.theBOSS.engine.gfx.Image;
import com.theBOSS.engine.gfx.Light;

public class GameManager extends AbstractGame{

	public static final int TS = 16;
	
	private boolean[] collision;
	private int levelW, levelH;
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	
	private Camera camera;
	
	private Light l, m;
	
	//sound class doesnt work for some reson
	public GameManager() {
		objects.add(new Player(9, 4));
		loadLevel("/level.png");
		camera = new Camera("player");
		l = new Light(100, 0xff00ff00);
		m = new Light(100, 0xff00ff00);
	}
	
	public void init(GameContainer gc) {
		gc.getRenderer().setAmbientColor(0xff3d3d3d);		
	}
	
	public void update(GameContainer gc, float dt) {
		for(int i = 0; i < objects.size(); i++) {
			objects.get(i).update(gc, this, dt);
			if(objects.get(i).isDead()) {
				objects.remove(i);
				i--;
			}
		}
		camera.update(gc, this, dt);
	}
	
	public void render(GameContainer gc, Renderer r) {
		camera.render(r);
		
		for(int y = 0; y < levelH; y++) {
			for(int x = 0; x < levelW; x++) {
				if(collision[x + y * levelW]) {
					r.drawFillRect(x * TS, y * TS, TS, TS, 0xff0f0f0f);
				}else {
					r.drawFillRect(x * TS, y * TS, TS, TS, 0xfff9f9f9);					
				}
			}
		}
		
		for(int i = 0; i < objects.size(); i++) {
			objects.get(i).render(gc, r);
		}
		for(int y = 270; y < 273; y++) {
			for(int x = 120; x < 123; x++) {
				r.setLightBlock(x - r.getCamX(), y - r.getCamY(), Light.FULL);				
			}	
		}
		r.drawLight(m, gc.getInput().getMouseX() +(int)  camera.getOffX(),(int) camera.getOffY() + gc.getInput().getMouseY());
		r.drawLight(l, (int) getObject("player").getPosX() + getObject("player").getWidth() / 2, (int) getObject("player").getPosY() + getObject("player").getHeight() / 2);
	}
	
	public void loadLevel(String path) {
		Image levelImage = new Image(path);
		levelW = levelImage.getW();
		levelH = levelImage.getH();
		collision = new boolean[levelW * levelH];
		
		for(int y = 0; y < levelImage.getH(); y++) {
			for(int x = 0; x < levelImage.getW(); x++) {
				if(levelImage.getP()[x + y * levelImage.getW()] == 0xff000000) {
					collision[x + y * levelImage.getW()] = true;
				}else {
					collision[x + y * levelImage.getW()] = false;
				}
			}
		}
	}
	
	public void addObject(GameObject object) {
		objects.add(object);
	}
	
	public GameObject getObject(String tag) {
		for(int i = 0; i < objects.size(); i++) {
			if(objects.get(i).getTag().equals(tag)) {
				return objects.get(i);
			}
		}
		return null;
	}
	
	public boolean getCollision(int x, int y) {
		if(x < 0 || x >= levelW || y < 0 || y >= levelH) return true;
		return collision[x + y * levelW];
	}
	
	public static void main(String[] args) {
		GameContainer gc = new GameContainer(new GameManager());
		gc.start();
	}

}
