package com.theBOSS.EndlessShooter.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.theBOSS.EndlessShooter.EndlessShooter;

public class Focus implements FocusListener{

	public void focusGained(FocusEvent e) {
		EndlessShooter.focus = true;
	}

	public void focusLost(FocusEvent e) {
		EndlessShooter.focus = false;
		Keyboard.clear = true;
	}

}
