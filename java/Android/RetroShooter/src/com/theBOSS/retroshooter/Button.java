package com.theBOSS.retroshooter;

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
		if(!RetroShooter.isDown1) {
			if(pressed) {
				clicked = true;
			}
			pressed = false;
			wasIn = false;
		}

		if((wasIn || RetroShooter.isDownNow) && RetroShooter.x1 >= x && RetroShooter.x1 < x + width && RetroShooter.y1 >= y && RetroShooter.y1 < y + height) {
			pressed = true;
			wasIn = true;
		}
		
		if(!(RetroShooter.x1 >= x && RetroShooter.x1 < x + width && RetroShooter.y1 >= y && RetroShooter.y1 < y + height) && pressed) { pressed = false;}
	}
	
	public void render() {
		if(!pressed) {
			Renderer.screen.renderSpriteNoOffsets(sprite, x, y);
		}else {
			Renderer.screen.renderSpriteNoOffsets(clickedSprite, x, y);			
		}
	}
	
}
