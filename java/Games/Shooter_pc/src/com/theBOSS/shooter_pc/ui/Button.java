package com.theBOSS.shooter_pc.ui;

import java.awt.event.MouseEvent;

import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.graphics.Sprite;
import com.theBOSS.shooter_pc.input.Mouse;

public class Button {

	public int x, y, width, height;
	public Sprite sprite, clickedSprite;
	public boolean wasIn = false, pressed = false, clicked = false;	
	
	public Button(int x, int y, Sprite sprite, Sprite clickedSprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
		this.clickedSprite = clickedSprite;
		this.width = sprite.width;
		this.height = sprite.height;
	}
	
	public void update() {
		if((wasIn || Mouse.buttonDown(MouseEvent.BUTTON1)) && Mouse.getX() >= x && Mouse.getX() < x + width && Mouse.getY() >= y && Mouse.getY() < y + height) {
			pressed = true;
			wasIn = true;
		}
		if(!(Mouse.getX() >= x && Mouse.getX() < x + width && Mouse.getY() >= y && Mouse.getY() < y + height) && pressed) { pressed = false;}
		
		if(Mouse.buttonUp(MouseEvent.BUTTON1)) {
			if(pressed) {
				clicked = true;
			}
			pressed = false;
			wasIn = false;
		}
		
	}
	
	public void render() {
		if(!pressed) {
			Shooter.screen.renderSpriteNoOffsets(sprite, x, y);
		}else {
			Shooter.screen.renderSpriteNoOffsets(clickedSprite, x, y);			
		}
	}
	
}
