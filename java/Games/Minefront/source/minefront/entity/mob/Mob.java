package com.mime.minefront.entity.mob;

import com.mime.minefront.Game;
import com.mime.minefront.entity.Entity;

public class Mob extends Entity {

	public void move(int xa, int za, double rot, double walkSpeed) {
		if(xa != 0 && za != 0) {
			move(xa, 0, rot, walkSpeed);
			move(0, za, rot, walkSpeed);
			return;
		}
		double nx = xa * Math.cos(rot) + za * Math.sin(rot);
		double nz = za * Math.cos(rot) - xa * Math.sin(rot);
		
		x += nx * walkSpeed;
		z += nz * walkSpeed;
	}
	
	public boolean collision(double xa, double za, double yy, double rot, double walkSpeed) {
		double nx = (xa * Math.cos(rot) + za * Math.sin(rot)) * walkSpeed;
		double nz = (za * Math.cos(rot) - xa * Math.sin(rot)) * walkSpeed;
		double xx = x;
		double zz = z;
		//stop you before you can see trough wall
		double offset = 0;
		
		xx += nx;
		zz += nz;
		
		if(xx < 0 || xx > Game.LEVEL_WIDTH) {
			if(zz > 0 && zz < Game.LEVEL_DEPTH) {
				z = zz;
			}
			return true;
		}
		if(zz < 0 || zz > Game.LEVEL_DEPTH) {
			if(xx > 0 && xx < Game.LEVEL_WIDTH) {
				x = xx;
			}
			return true;
		}
		
		for(int i = 0; i < game.level.blocks.length; i++) {
			if(xx > game.level.blocks[i].x - offset && xx < game.level.blocks[i].x + 8 + offset && zz > game.level.blocks[i].z - offset  && zz <  game.level.blocks[i].z + 8 + offset) {
				return true;
			}
			
		}
		
		return false;
	}
	
}
