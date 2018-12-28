package com.mime.minefront.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.mime.minefront.Display;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener{

	public boolean[] key = new boolean[68836]; 
	public static int MouseX;
	public static int MouseY;
	public static int mouseButton;
	
	public boolean forward, back, left, right, rleft, rright, jump, crouch, run; 
	
	public void tick() {
		forward = key[KeyEvent.VK_W];
		back = key[KeyEvent.VK_S];
		left = key[KeyEvent.VK_A];
		right = key[KeyEvent.VK_D];
		rleft = key[KeyEvent.VK_LEFT];
		rright = key[KeyEvent.VK_RIGHT];
		jump = key[KeyEvent.VK_SPACE];
		crouch = key[KeyEvent.VK_CONTROL];
		run = key[KeyEvent.VK_SHIFT];
	}
	
	public void mouseDragged(MouseEvent e) {
		MouseX = e.getX();
		MouseY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		MouseX = e.getX();
		MouseY = e.getY();
	}

	public void mouseClicked(MouseEvent e) {
	
	}

	public void mouseEntered(MouseEvent e) {
		Display.oldX = e.getX();
		Display.newX = e.getX();
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		mouseButton = e.getButton();
	}

	public void mouseReleased(MouseEvent e) {
		mouseButton = 0;
	}

	public void focusGained(FocusEvent e) {
		
	}

	public void focusLost(FocusEvent e) {
		for(int i = 0; i < key.length; i++){
			key[i] = false;
		}
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode > 0 && keyCode < key.length){
			key[keyCode] = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode > 0 && keyCode < key.length){
			key[keyCode] = false;
		}
	}

	public void keyTyped(KeyEvent e) {
		
	}
	
	
	
}
