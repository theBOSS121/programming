package com.theBoss.EndlessCubes.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.theBoss.EndlessCubes.EndlessCubes;

public class Screen {
	public int width, height;
	public int[] pixels;

	public static int renderDistance = 1400;
	private double[] zBuffer;
	private BufferedImage pouse;
	private int[] pousePixels;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		zBuffer = new double[width * height];
		try {
			pouse = ImageIO.read(EndlessCubes.class.getResource("/pouse.png"));
			pousePixels = pouse.getRGB(0, 0, pouse.getWidth(), pouse.getHeight(), null, 0, pouse.getWidth());
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
			double ceiling = (y - height / 2.0) / height;
			
			double z = (EndlessCubes.floorPosition) / ceiling;
			
			if(ceiling < 0){
				z = (EndlessCubes.ceilingPosition) / -ceiling;
			}
			
			for(int x = 0; x < width; x++){
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth;
				//double yy = z;
				zBuffer[x + y * width] = z;
				if(xx + EndlessCubes.leftRight > 3.9 && xx + EndlessCubes.leftRight < 4.5 || xx + EndlessCubes.leftRight < -3.9 && xx + EndlessCubes.leftRight > -4.5) pixels[x + y * width] = 0;
				
			}
		}
	}
	
	public void renderEntity(double x, double y, double z, double w, double h, double l, int col) {
		if(z + l <= 0) return;
		
		int lufx = (int) ((x - EndlessCubes.leftRight * 1.8) * 256 / z) + width / 2;
		int lufy = (int) ((y - (8.0 - EndlessCubes.floorPosition)* 1.8) * 256 / z) + height / 2;		
		int rufx = (int) ((x + w - EndlessCubes.leftRight * 1.8) * 256 / z) + width / 2;
		//int rufy = (int) ((y - (8.0 - Game.floorPosition)* 1.8) * 256 / z) + height / 2;		
		//int ldfx = (int) ((x - Game.leftRight * 1.8) * 256 / z) + width / 2;
		int ldfy = (int) ((y + h - (8.0 - EndlessCubes.floorPosition)* 1.8) * 256 / z) + height / 2;		
		//int rdfx = (int) ((x + w - Game.leftRight * 1.8) * 256 / z) + width / 2;
		//int rdfy = (int) ((y + h - (8.0 - Game.floorPosition)* 1.8) * 256 / z) + height / 2;		
		int lubx = (int) ((x - EndlessCubes.leftRight * 1.8) * 256 / (z + l)) + width / 2;
		int luby = (int) ((y - (8.0 - EndlessCubes.floorPosition)* 1.8) * 256 / (z + l)) + height / 2;		
		int rubx = (int) ((x + w - EndlessCubes.leftRight * 1.8) * 256 / (z + l)) + width / 2;
		//int ruby = (int) ((y - (8.0 - Game.floorPosition)* 1.8) * 256 / (z + l)) + height / 2;
		//int ldbx = (int) ((x - Game.leftRight * 1.8) * 256 / (z + l)) + width / 2;
		int ldby = (int) ((y + h - (8.0 - EndlessCubes.floorPosition)* 1.8) * 256 / (z + l)) + height / 2;		
		//int rdbx = (int) ((x + w - Game.leftRight * 1.8) * 256 / (z + l)) + width / 2;
		//int rdby = (int) ((y + h - (8.0 - Game.floorPosition)* 1.8) * 256 / (z + l)) + height / 2;
		
		//back of the cube
		for(int yy = luby; yy <= ldby; yy++) {
			if(ldby < 0 || luby > height) break;
			if(yy < 0 || yy > height) continue;
			for(int xx = lubx; xx <= rubx; xx++) {
				if(rubx < 0 || lubx > width) break;
				if(xx < 0 || xx >= width) continue;
				if(xx + yy * width >= 0 && xx + yy * width < pixels.length) {
					pixels[xx + yy * width] = col;
				}
			}
		}
		//bottom of the cube
		for(double zz = z; zz <= z + l; zz += 0.01) {
			int ldcx = (int) ((x - EndlessCubes.leftRight * 1.8) * 256 / zz) + width / 2;
			int rdcx = (int) ((x + w - EndlessCubes.leftRight * 1.8) * 256 / zz) + width / 2;
			int yy = (int) ((y + h - (8.0 - EndlessCubes.floorPosition)* 1.8) * 256 / zz) + height / 2;
			if(yy < 0 || yy > height) continue;
			for(int xx = ldcx; xx <= rdcx; xx++) {
				if(rdcx < 0 || ldcx > width) break;
				if(xx < 0 || xx >= width) continue;
				if(xx + yy * width >= 0 && xx + yy * width < pixels.length) {
					pixels[xx + yy * width] = col;
				}
			}
		}
		//top of the cube
		for(double zz = z; zz <= z + l; zz += 0.01) {
			int lucx = (int) ((x - EndlessCubes.leftRight * 1.8) * 256 / zz) + width / 2;
			int rucx = (int) ((x + w - EndlessCubes.leftRight * 1.8) * 256 / zz) + width / 2;
			int yy = (int) ((y - (8.0 - EndlessCubes.floorPosition)* 1.8) * 256 / zz) + height / 2;
			if(yy < 0 || yy > height) continue;
			for(int xx = lucx; xx <= rucx; xx++) {
				if(rucx < 0 || lucx > width) break;
				if(xx < 0 || xx >= width) continue;
				if(xx + yy * width >= 0 && xx + yy * width < pixels.length) {
					pixels[xx + yy * width] = col;
				}
			}
		}
		//front of the cube
		for(int yy = lufy; yy <= ldfy; yy++) {
			if(ldfy < 0 || lufy > height) break;
			if(yy < 0 || yy > height) continue;
			for(int xx = lufx; xx <= rufx; xx++) {
				if(rufx < 0 || lufx > width) break;
				if(xx < 0 || xx >= width) continue;
				if(xx + yy * width >= 0 && xx + yy * width < pixels.length) {
					pixels[xx + yy * width] = col;
				}
			}
		}
		
	}
	
	public void renderDistanceLimiter(){
		for(int i = 0; i < width * height; i++){
			int colour = pixels[i];
			int brightness = (int) (renderDistance / (zBuffer[i]));
			if(brightness < 0){
				brightness = 0;
			}
			
			if(brightness > 255){
				brightness = 255;
			}
			
			int r = (colour >> 16) & 0xff;
			int g = (colour >> 8) & 0xff;
			int b = (colour) & 0xff;
				
			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;
			
			pixels[i] = r << 16 | g << 8 | b;
		}
	}
	
	public void renderPouse() {
		for(int y = 0; y < pouse.getHeight(); y++) {
			for(int x = width - pouse.getWidth(); x < width; x++) {
				int xx = x - width + pouse.getWidth();
				int col = pousePixels[xx + y * pouse.getWidth()];
				if(col == 0xffff00ff) continue;
				pixels[x + y * width] = col;
			}
		}
	}
}
