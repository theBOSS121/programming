package com.theBOSS.EndlessShooter.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import com.theBOSS.EndlessShooter.EndlessShooter;

public class Mouse implements MouseListener, MouseMotionListener{

	private static int mouseX = -1;
	private static int mouseY = -1;
	private static int mouseB = -1;

	private final int NUM_BUTTONS = 5;
	private boolean[] buttons = new boolean[NUM_BUTTONS];
	private boolean[] buttonsLast = new boolean[NUM_BUTTONS];
	
	public void update() {
		for(int i = 0; i  < NUM_BUTTONS; i++) {
			buttonsLast[i] = buttons[i];
		}
	}
	
	public static int getX(){
		return mouseX;
	}
	
	public static int getY(){
		return mouseY;
	}
	
	public static int getButton(){
		return mouseB;
	}
	
	public boolean isButton(int button) {
		return buttons[button];
	}
	
	public boolean isButtonUp(int button) {
		return !buttons[button] && buttonsLast[button];
	}
	
	public boolean isButtonDown(int button) {
		return buttons[button] && !buttonsLast[button];
	}
		
	
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX() / EndlessShooter.scale;
		mouseY = e.getY() / EndlessShooter.scale;
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX() / EndlessShooter.scale;
		mouseY = e.getY() / EndlessShooter.scale;		
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		mouseB = e.getButton();
		buttons[e.getButton()] = true;
	}

	public void mouseReleased(MouseEvent e) {
		mouseB = MouseEvent.NOBUTTON;
		buttons[e.getButton()] = false;
	}
	
}
