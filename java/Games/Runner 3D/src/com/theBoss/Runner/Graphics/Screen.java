package com.theBoss.Runner.Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.theBoss.Runner.Runner;

public class Screen {
	private int width, height;
	public int[] pixels, floor, floor2, sky;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		try {
			BufferedImage image = ImageIO.read(Screen.class.getResource("/sheet.png"));
			floor = image.getRGB(0, 0, 32, 32, null, 0, 32);
			floor2 = image.getRGB(64, 0, 32, 32, null, 0, 32);
			sky = image.getRGB(32, 0, 32, 32, null, 0, 32);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0xffffff;
		}
	}
	
	public void render() {
		for(int y = 0; y < height; y++){
			double ceiling = (y - height / 2.0) / 256;
			
			double z = (Runner.floorPosition) / ceiling;
			
			if(ceiling < 0){
				z = (Runner.ceilingPosition) / -ceiling;				
			}
			
			for(int x = 0; x < width; x++){
				double depth = (x - width / 2.0) / 256;
				depth *= z;
				double xx = depth + Runner.leftRight;
				double yy = z + Runner.time;
				int xPix = (int) (xx);
				int yPix = (int) (yy);
				//to erase double pixels
				if(xx < 0) xPix--;
				if(yy < 0) yPix--;
				if(ceiling > 0) {
					if(z < 250) {
						pixels[x + y * width] = floor[(xPix & 31) + (yPix & 31) * 32];
					}else {
						pixels[x + y * width] = floor2[(xPix & 31) + (yPix & 31) * 32];						
					}
				
				}else {
					pixels[x + y * width] = sky[(xPix & 31) + (yPix & 31) * 32];
				}
				if(xx < -30 && y > height / 2) pixels[x + y * width] = 0xff808080;
				if(xx > 30 && y > height / 2) pixels[x + y * width] = 0xff808080;
				if(xx < -30 && y > height / 2 && z > 250) pixels[x + y * width] = 0xff6b6b6b;
				if(xx > 30 && y > height / 2 && z > 250) pixels[x + y * width] = 0xff6b6b6b;
			}
		}
	}
/*
	public void renderEntity(double x, double y, double z, double w, double h, double l) {
		if(z + l <= 0) return;
		for(double zz = z; zz < z + l; zz += 0.5) {
			for(double yy = y; yy < y + h; yy += 0.5) {
				for(double xx = x; xx < x + w; xx += 0.5) {	
					if(zz <= 0) continue;
					double xc = xx - Game.leftRight * 1.8;
					double yc = yy - (8.0 - Game.floorPosition) * 1.8;
					int xPos = (int) ((xc) * 256 / zz);
					int yPos = (int) ((yc) * 256 / zz);
					xPos += width / 2;
					yPos += height / 2;
					if(xPos < 0 || xPos > width) continue;
					if(xPos + yPos * width >= 0 && xPos + yPos * width < pixels.length)
					pixels[xPos + yPos * width] = 0xff000000;
				}	
			}
		}
	}
*/
	public void renderEntity(double x, double y, double z, double w, double h, double l) {
		if(z + l <= 0) return;
		
		int lufx = (int) ((x - Runner.leftRight * 1.8) * 256 / z) + width / 2;
		int lufy = (int) ((y - (8.0 - Runner.floorPosition)* 1.8) * 256 / z) + height / 2;		
		int rufx = (int) ((x + w - Runner.leftRight * 1.8) * 256 / z) + width / 2;
		//int rufy = (int) ((y - (8.0 - Game.floorPosition)* 1.8) * 256 / z) + height / 2;		
		//int ldfx = (int) ((x - Game.leftRight * 1.8) * 256 / z) + width / 2;
		int ldfy = (int) ((y + h - (8.0 - Runner.floorPosition)* 1.8) * 256 / z) + height / 2;		
		//int rdfx = (int) ((x + w - Game.leftRight * 1.8) * 256 / z) + width / 2;
		//int rdfy = (int) ((y + h - (8.0 - Game.floorPosition)* 1.8) * 256 / z) + height / 2;		
		int lubx = (int) ((x - Runner.leftRight * 1.8) * 256 / (z + l)) + width / 2;
		int luby = (int) ((y - (8.0 - Runner.floorPosition)* 1.8) * 256 / (z + l)) + height / 2;		
		int rubx = (int) ((x + w - Runner.leftRight * 1.8) * 256 / (z + l)) + width / 2;
		//int ruby = (int) ((y - (8.0 - Game.floorPosition)* 1.8) * 256 / (z + l)) + height / 2;
		//int ldbx = (int) ((x - Game.leftRight * 1.8) * 256 / (z + l)) + width / 2;
		int ldby = (int) ((y + h - (8.0 - Runner.floorPosition)* 1.8) * 256 / (z + l)) + height / 2;		
		//int rdbx = (int) ((x + w - Game.leftRight * 1.8) * 256 / (z + l)) + width / 2;
		//int rdby = (int) ((y + h - (8.0 - Game.floorPosition)* 1.8) * 256 / (z + l)) + height / 2;
		
		//front of the cube
		for(int yy = lufy; yy < ldfy; yy++) {
			if(ldfy < 0 || lufy > height) break;
			if(yy < 0 || yy > height) continue;
			for(int xx = lufx; xx < rufx; xx++) {
				if(rufx < 0 || lufx > width) break;
				if(xx < 0 || xx > width) continue;
				if(xx + yy * width >= 0 && xx + yy * width < pixels.length) {
					pixels[xx + yy * width] = 0;
				}
			}
		}
		//back of the cube
		for(int yy = luby; yy < ldby; yy++) {
			if(ldby < 0 || luby > height) break;
			if(yy < 0 || yy > height) continue;
			for(int xx = lubx; xx < rubx; xx++) {
				if(rubx < 0 || lubx > width) break;
				if(xx < 0 || xx > width) continue;
				if(xx + yy * width >= 0 && xx + yy * width < pixels.length) {
					pixels[xx + yy * width] = 0;
				}
			}
		}
		//top of the cube
		for(double zz = z; zz < z + l; zz += 0.01) {
			int lucx = (int) ((x - Runner.leftRight * 1.8) * 256 / zz) + width / 2;
			int rucx = (int) ((x + w - Runner.leftRight * 1.8) * 256 / zz) + width / 2;
			int yy = (int) ((y - (8.0 - Runner.floorPosition)* 1.8) * 256 / zz) + height / 2;
			if(yy < 0 || yy > height) continue;
			for(int xx = lucx; xx < rucx; xx++) {
				if(rucx < 0 || lucx > width) break;
				if(xx < 0 || xx > width) continue;
				if(xx + yy * width >= 0 && xx + yy * width < pixels.length) {
					pixels[xx + yy * width] = 0;
				}
			}
		}
		//bottom of the cube
		for(double zz = z; zz < z + l; zz += 0.01) {
			int ldcx = (int) ((x - Runner.leftRight * 1.8) * 256 / zz) + width / 2;
			int rdcx = (int) ((x + w - Runner.leftRight * 1.8) * 256 / zz) + width / 2;
			int yy = (int) ((y + h - (8.0 - Runner.floorPosition)* 1.8) * 256 / zz) + height / 2;
			if(yy < 0 || yy > height) continue;
			for(int xx = ldcx; xx < rdcx; xx++) {
				if(rdcx < 0 || ldcx > width) break;
				if(xx < 0 || xx > width) continue;
				if(xx + yy * width >= 0 && xx + yy * width < pixels.length) {
					pixels[xx + yy * width] = 0;
				}
			}
		}
		// in this game is no rotation so you dont relly need left and right
		// side of the cube since every side is the same color
	}
}




