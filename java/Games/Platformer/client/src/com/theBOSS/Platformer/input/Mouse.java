package com.theBOSS.Platformer.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.theBOSS.Platformer.Platformer;

public class Mouse implements MouseListener, MouseMotionListener{

	private static int x = -1, y = -1;
	private boolean[] buttons = new boolean[5];
	private boolean[] lastButtons = new boolean[5];
	
	public static int getX() {
		return x;
	}
	
	public static int getY() {
		return y;
	}

	public void update() {
		for(int i = 0; i < buttons.length; i++) {
			lastButtons[i] = buttons[i];
		}
	}
	
	public boolean button(int button) {
		return buttons[button];
	}
	
	public boolean buttonDown(int button) {
		return buttons[button] && !lastButtons[button];
	}
	
	public boolean buttonUp(int button) {
		return !buttons[button] && lastButtons[button];
	}
	
	public void mouseDragged(MouseEvent e) {
		x = e.getX() / Platformer.SCALE;
		y = e.getY() / Platformer.SCALE;		
	}

	public void mouseMoved(MouseEvent e) {
		x = e.getX() / Platformer.SCALE;
		y = e.getY() / Platformer.SCALE;
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
	}

	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}	
}
