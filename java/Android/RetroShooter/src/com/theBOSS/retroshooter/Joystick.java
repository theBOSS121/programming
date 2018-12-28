package com.theBOSS.retroshooter;

public class Joystick {
	
	public int x = Renderer.joystick.width / 2 + 1, y = Renderer.joystick.height / 2 + 1;
	private int xx, yy, side;
	public double angle = -Math.PI / 2;
	
	public boolean inTheMiddle = true, in = false;
	
	public Joystick(int xx, int yy, int side) {
		this.xx = xx;
		this.yy = yy;
		this.side = side;
		x += xx;
		y += yy;
	}
	
	public void update() {
		if(side == 0) {
			if(RetroShooter.isDown1) {
				if(RetroShooter.x1 > xx + 2 && RetroShooter.x1 < xx + 28 && RetroShooter.y1 > yy + 2 && RetroShooter.y1 < yy + 28) {
					in = true;
				}
				if(in && RetroShooter.x1 > xx + 2 && RetroShooter.x1 < xx + 28) {
					x = RetroShooter.x1 - Renderer.joystick.width / 2;
					inTheMiddle = false;
				}
				if(in && RetroShooter.y1 > yy + 2 && RetroShooter.y1 < yy + 28) {
					y = RetroShooter.y1 - Renderer.joystick.height / 2;
					inTheMiddle = false;
				}
				if(!inTheMiddle) angle = getOneOf16Anglea(Math.atan2((y + Renderer.joystick.height / 2) - (yy + Renderer.joystick_bg.height / 2) , (x + Renderer.joystick.width / 2) - (xx + Renderer.joystick_bg.width / 2)));
				
			}else if(!RetroShooter.isDown1){
				x = xx + Renderer.joystick.width / 2 + 1;
				y = yy + Renderer.joystick.height / 2 + 1;
				inTheMiddle = true;
				in = false;
			}		
		}else if(side == 1) {
			if(RetroShooter.isDown2) {
				if(RetroShooter.x2 > xx + 2 && RetroShooter.x2 < xx + 28 && RetroShooter.y2 > yy + 2 && RetroShooter.y2 < yy + 28) {
					in = true;
				}
				if(in && RetroShooter.x2 > xx + 2 && RetroShooter.x2 < xx + 28) {
					x = RetroShooter.x2 - Renderer.joystick.width / 2;
					inTheMiddle = false;
				}
				if(in && RetroShooter.y2 > yy + 2 && RetroShooter.y2 < yy + 28) {
					y = RetroShooter.y2 - Renderer.joystick.height / 2;
					inTheMiddle = false;
				}
				if(!inTheMiddle) angle = getOneOf16Anglea(Math.atan2((y + Renderer.joystick.height / 2) - (yy + Renderer.joystick_bg.height / 2) , (x + Renderer.joystick.width / 2) - (xx + Renderer.joystick_bg.width / 2)));
				
			}else if(!RetroShooter.isDown2){
				x = xx + Renderer.joystick.width / 2 + 1;
				y = yy + Renderer.joystick.height / 2 + 1;
				inTheMiddle = true;
				in = false;
			}
		}
	}
	
	public void render() {
		Renderer.screen.renderSpriteNoOffsets(Renderer.joystick_bg, xx, yy);
		Renderer.screen.renderSpriteNoOffsets(Renderer.joystick, x, y);
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