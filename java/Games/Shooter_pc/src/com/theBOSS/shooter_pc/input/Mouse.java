package com.theBOSS.shooter_pc.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.theBOSS.shooter_pc.Shooter;

public class Mouse implements MouseListener, MouseMotionListener{

	private static int x = -1, y = -1;
	private static boolean[] buttons = new boolean[5];
	private static boolean[] lastButtons = new boolean[5];
	
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
	
	public static boolean button(int button) {
		return buttons[button];
	}
	
	public static boolean buttonDown(int button) {
		return buttons[button] && !lastButtons[button];
	}
	
	public static boolean buttonUp(int button) {
		return !buttons[button] && lastButtons[button];
	}
	
	public void mouseDragged(MouseEvent e) {
		x = e.getX() / Shooter.SCALE;
		y = e.getY() / Shooter.SCALE;		
	}

	public void mouseMoved(MouseEvent e) {
		x = e.getX() / Shooter.SCALE;
		y = e.getY() / Shooter.SCALE;
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
