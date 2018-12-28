package com.theBoss.gameEngine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{

	private boolean[] keys = new boolean[120];
	public boolean up, left, right, down, paused = false;
	
	public void update(){
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
	}
	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;		
	}

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_SPACE) {
			paused = !paused;
		}
	}

}
