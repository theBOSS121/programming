package com.mime.minefront.entity.mob;

import com.mime.minefront.Display;
import com.mime.minefront.Game;
import com.mime.minefront.input.InputHandler;

public class Player extends Mob{
	
	public double y, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean walk = false;
	public static boolean crouchWalk = false;
	public static boolean runWalk = false;
	private InputHandler input;
	
	public Player(InputHandler input, double x, double z) {
		this.input = input;
		this.x = x;
		this.z = z;
	}
	
	public void tick() {
		double rotationSpeed = 0.004 * Display.MouseSpeed;
		double rotationSpeedKey = 0.024;
		double walkSpeed = 0.5;
		double jumpHeight = 0.5;
		double crouchHeight = -0.3;
		int xa = 0;
		int za = 0;
		
		if(input.forward){
			za ++;
			walk = true;
		}
		if(input.back){
			za--;
			walk = true;
		}
		if(input.left){
			xa--;
			walk = true;
		}
		if(input.right){
			xa++;
			walk = true;
		}
		if(turnLeft){
			if(InputHandler.mouseButton == 3) {
				
			}else {
				rotationa -= rotationSpeed;
			}
		}
		if(turnRight){
			if(InputHandler.mouseButton == 3) {
			}else {
				rotationa += rotationSpeed;
			}				
		}		
		if(input.rleft) {
			rotationa -= rotationSpeedKey;
		}
		if(input.rright) {
			rotationa += rotationSpeedKey;
		}		
		
		
		if(input.jump){
			y += jumpHeight;
		}   
		
		if(input.crouch){
			y += crouchHeight;
			walkSpeed = 0.2;
			input.run = false;
			crouchWalk = true;
		}
		if(input.run){
			walkSpeed = 1.0;
			walk = true;
			runWalk = true;
		}
		if(!input.forward && !input.back && !input.right && !input.left){
			walk = false;
		}
		if(!input.crouch){
			crouchWalk = false;
		}
		if(!input.run){
			runWalk = false;
		}
		
		rotation += rotationa;
		rotationa *= 0.5;
		
		
		if(xa != 0 || za != 0) {
			if(!collision(xa, za, y, rotation, walkSpeed))
				move(xa, za,rotation, walkSpeed);
		
		}
		
		if(y < 0.009 && y > -0.009) {
				y = 0;
		}
		y *= 0.9;
		
	}

	
}

	





