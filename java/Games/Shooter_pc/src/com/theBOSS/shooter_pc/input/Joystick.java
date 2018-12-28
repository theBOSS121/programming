package com.theBOSS.shooter_pc.input;

import com.theBOSS.shooter_pc.Shooter;
import com.theBOSS.shooter_pc.graphics.Sprite;

public class Joystick {
	
	public int x = Sprite.joystick.width / 2 + 1, y = Sprite.joystick.height / 2 + 1;
	private int xx, yy;
	public double angle = -Math.PI / 2;
	
	public boolean inTheMiddle = true, in = false;
	
	public Joystick(int xx, int yy) {
		this.xx = xx;
		this.yy = yy;
		x += xx;
		y += yy;
	}
	
	public void update() {
		if(Mouse.button(1)) {
			if(Mouse.getX() > xx + 2 && Mouse.getX() < xx + 28 && Mouse.getY() > yy + 2 && Mouse.getY() < yy + 28) {
				in = true;
			}
			if(in && Mouse.getX() > xx + 2 && Mouse.getX() < xx + 28) {
				x = Mouse.getX() - Sprite.joystick.width / 2;
				inTheMiddle = false;
			}
			if(in && Mouse.getY() > yy + 2 && Mouse.getY() < yy + 28) {
				y = Mouse.getY() - Sprite.joystick.height / 2;
				inTheMiddle = false;
			}
			if(!inTheMiddle) angle = getOneOf16Anglea(Math.atan2((y + Sprite.joystick.height / 2) - (yy + Sprite.joystick_bg.height / 2) , (x + Sprite.joystick.width / 2) - (xx + Sprite.joystick_bg.width / 2)));
			
		}else if(!Mouse.button(1)){
			x = xx + Sprite.joystick.width / 2 + 1;
			y = yy + Sprite.joystick.height / 2 + 1;
			inTheMiddle = true;
			in = false;
		}		
	}
	
	public void render() {
		Shooter.screen.renderSpriteNoOffsets(Sprite.joystick_bg, xx, yy);
		Shooter.screen.renderSpriteNoOffsets(Sprite.joystick, x, y);
	}	
	
	
	public double getOneOf16Anglea(double ang) {
		if(ang > -Math.PI / 16 && ang < Math.PI / 16) ang = 0;
		else if(ang > Math.PI / 16 && ang < 3 * Math.PI / 16) ang = Math.PI / 8;
		else if(ang > 3 * Math.PI / 16 && ang < 5 * Math.PI / 16) ang = Math.PI / 4;
		else if(ang > 5 * Math.PI / 16 && ang < 7 * Math.PI / 16) ang = 3 * Math.PI / 8;
		else if(ang > 7 * Math.PI / 16 && ang < 9 * Math.PI / 16) ang = Math.PI / 2;
		else if(ang > 9 * Math.PI / 16 && ang < 11 * Math.PI / 16) ang = 5 * Math.PI / 8;
		else if(ang > 11 * Math.PI / 16 && ang < 13 * Math.PI / 16) ang = 3 * Math.PI / 4;
		else if(ang > 13 * Math.PI / 16 && ang < 15 * Math.PI / 16) ang = 7 * Math.PI / 8;
		else if(ang < -Math.PI / 16 && ang > -3 * Math.PI / 16) ang = -Math.PI / 8;
		else if(ang < -3 * Math.PI / 16 && ang > -5 * Math.PI / 16) ang = -Math.PI / 4;
		else if(ang < -5 * Math.PI / 16 && ang > -7 * Math.PI / 16) ang = -3 * Math.PI / 8;
		else if(ang < -7 * Math.PI / 16 && ang > -9 * Math.PI / 16) ang = -Math.PI / 2;
		else if(ang < -9 * Math.PI / 16 && ang > -11 * Math.PI / 16) ang = -5 * Math.PI / 8;
		else if(ang < -11 * Math.PI / 16 && ang > -13 * Math.PI / 16) ang = -3 * Math.PI / 4;
		else if(ang < -13 * Math.PI / 16 && ang > -15 * Math.PI / 16) ang = -7 * Math.PI / 8;
		else ang = Math.PI;		
		return ang;
	}
	
}