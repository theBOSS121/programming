package com.theBOSS.EndlessShooter.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{

	private boolean[] keys = new boolean[120];
	private boolean[] keysLast = new boolean[120];
	public boolean up, down, left, right, healing;
	public static boolean clear = false;
	
	public void update(){
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		healing = keys[KeyEvent.VK_SPACE];
		if(clear) {
			for(int i = 0; i < keys.length; i++) {
				keys[i] = false;
			}
			clear = false;
		}

		for(int i = 0; i  < 120; i++) {
			keysLast[i] = keys[i];
		}
	}
	
	public boolean isKey(int keyCode) {
		return keys[keyCode];
	}
	
	public boolean isKeyUp(int keyCode) {
		return !keys[keyCode] && keysLast[keyCode];
	}
	
	public boolean isKeyDown(int keyCode) {
		return keys[keyCode] && !keysLast[keyCode];
	}
	
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}

	public void keyTyped(KeyEvent e) {
	}

}
