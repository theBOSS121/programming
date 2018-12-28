package com.mime.minefront.graphics;

import com.mime.minefront.Game;
import com.mime.minefront.entity.mob.Player;
import com.mime.minefront.level.Block;
import com.mime.minefront.level.Level;

public class Render3D extends Render{
	
	public double[] zBuffer;
	public double[] zBufferWall;
	public double renderDistance = 5000;
	private double forward, right, up, cosine, sine, walking;
	private int spriteSheetWidth = 128;
	
	int c = 0;
	
	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];
		zBufferWall = new double[width];
	}
	
	public void floor(Game game){
		
		for(int x = 0; x < width; x++) {
			zBufferWall[x] = 0;
		}
		
		double floorPosition = 8.0;
		double ceilingPosition = 8.0;
		forward = game.player.z;
		right = game.player.x;
		up = game.player.y;
		walking = 0;
		double rotation = game.player.rotation;
		cosine = Math.cos(rotation);
		sine = Math.sin(rotation);
		
		for(int y = 0; y < height; y++){
			double ceiling = (y - height / 2.0) / height;
			c = 0;
			
			double z = (floorPosition + up) / ceiling;
			if(Player.walk){
				walking = Math.sin(game.time / 6.0) * 0.5;
				z = (floorPosition + up + walking) / ceiling;
			}
			if(Player.crouchWalk && Player.walk){
				walking = Math.sin(game.time / 6.0) * 0.25;
				z = (floorPosition + up + walking) / ceiling;
			}
			if(Player.runWalk && Player.walk){
				walking = Math.sin(game.time / 6.0) * 0.8;
				z = (floorPosition + up + walking) / ceiling;
			}
			if(ceiling < 0){
				z = (ceilingPosition - up) / -ceiling;
				c = 1;
				if(Player.walk){
					z = (floorPosition - up - walking) / -ceiling;
				}
			}
			
			for(int x = 0; x < width; x++){
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine;
				double yy = z * cosine - depth * sine;
				int xPix = (int) ((xx + right) * 4);
				int yPix = (int) ((yy + forward) * 4);
				zBuffer[x + y * width] = z; 
				//pixels[x + y * width] = ((xPix & 15) * 16) | ((yPix & 15) * 16) << 8;
				if(c == 0) {
					pixels[x + y * width] = Texture.floor.pixels[(xPix & 31) + 32 + (yPix & 31) * spriteSheetWidth];
				}else {
					pixels[x + y * width] = Texture.floor.pixels[(xPix & 31) + 64 + (yPix & 31) * spriteSheetWidth];
				}
			}
		}
		
		Level level = game.level;
		int size = 50;
		
		for(int xBlock = -size; xBlock <= size; xBlock++) {
			for(int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);
				if(block.solid) {
					if(!east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0);
					}
					if(!south.solid) {
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0);
					}
				}else {
					if(east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0);
					}
					if(south.solid) {
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0);
					}
				}
				if(block.solid) {
					if(!east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0);
					}
					if(!south.solid) {
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0);
					}
				}else {
					if(east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0);
					}
					if(south.solid) {
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0);
					}
				}
			}
			
		}
		
		for(int xBlock = -size; xBlock <= size; xBlock++) {
			for(int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);
				if(block.solid) {
					if(!east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 1);
					}
					if(!south.solid) {
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 1);
					}
				}else {
					if(east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 1);
					}
					if(south.solid) {
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 1);
					}
				}if(block.solid) {
					if(!east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 1);
					}
					if(!south.solid) {
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 1);
					}
				}else {
					if(east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 1);
					}
					if(south.solid) {
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 1);
					}
				}
			}
			
		}
		//sprites not walls
		for(int xBlock = -size; xBlock <= size; xBlock++) {
			for(int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				for(int s = 0; s < block.sprites.size(); s++) {
					Sprite sprite = block.sprites.get(s);
					renderSprite(xBlock + sprite.x, sprite.y, zBlock + sprite.z, 0.5);
				}
		
			}
		}
	}
	
	public void renderSprite(double x, double y, double z, double hOffset) {
		double upCorrect = -0.125;
		double rightCorrect = 0.0625;
		double forwardCorrect = 0.0625;
		double walkCorrect = 0.0625;

		double xc = ((x / 2) - (right * rightCorrect)) * 2.0;
		double yc = ((y / 2) - (up * upCorrect)) + (walking * walkCorrect) * 2 + hOffset;
		double zc = ((z / 2) - (forward * forwardCorrect)) * 2.0;
		
		double rotX = xc * cosine - zc * sine;
		double rotY = yc;
		double rotZ = zc * cosine + xc * sine;

		double xCenter = width / 2;
		double yCenter = height / 2;
		
		double xPixel = rotX / rotZ * height + xCenter;
		double yPixel = rotY / rotZ * height + yCenter;
		
		double xPixelL = xPixel - (height / 2) / rotZ;
		double xPixelR = xPixel + (height / 2) / rotZ;
		double yPixelL = yPixel - (height / 2) / rotZ;
		double yPixelR = yPixel + (height / 2) / rotZ;
		
		int xpl = (int) xPixelL;
		int xpr = (int) xPixelR;
		int ypl = (int) yPixelL;
		int ypr = (int) yPixelR;
		
		if(xpl < 0) {
			xpl = 0;
		}
		if(xpr > width) {
			xpr = width;
		}
		if(ypl < 0) {
			ypl = 0;
		}
		if(ypr > height) {
			ypr = height;
		}
		rotZ *= 8;
		
		for(int yp = ypl; yp < ypr; yp++) {
			double pixelRotationY = (yp - yPixelR) / (yPixelL - yPixelR);
			int yTexture = (int) (pixelRotationY * 8 * 4);
			for(int xp = xpl; xp < xpr; xp++) {
				double pixelRotationX = (xp - xPixelR) / (xPixelL - xPixelR);
				int xTexture = (int) (pixelRotationX * 8 * 4);
				if(zBuffer[xp + yp * width] > rotZ) {
					int col = Texture.floor.pixels[(xTexture & 31) + 96 + (yTexture & 31) * spriteSheetWidth];
					if(col != 0xffff00ff) { 
						pixels[xp + yp * width] = col;
						zBuffer[xp + yp * width] = rotZ;
					}
				}
			}
		}
	}
	
	public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
		double upCorrect = 0.0625;
		double rightCorrect = 0.0625;
		double forwardCorrect = 0.0625;
		double walkCorrect = -0.0625;
		
		double xcLeft = ((xLeft / 2) - (right * rightCorrect)) * 2.0;
		double zcLeft = ((zDistanceLeft / 2) - (forward * forwardCorrect)) * 2.0;
		
		double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
		double yCornerTL = ((yHeight - 1.0) - (-up * upCorrect + walking * walkCorrect) * 2.0);
		double yCornerBL = ((yHeight) - (-up * upCorrect + walking * walkCorrect) * 2.0);
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;
		
		double xcRight = ((xRight / 2) - (right * rightCorrect)) * 2.0;
		double zcRight = ((zDistanceRight / 2) - (forward * forwardCorrect)) * 2.0;
		
		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = ((yHeight - 1.0) - (-up * upCorrect + walking * walkCorrect) * 2.0);
		double yCornerBR = ((yHeight) - (-up * upCorrect + walking * walkCorrect) * 2.0);
		double rotRightSideZ = zcRight * cosine + xcRight * sine;
		
		double tex30 = 0;
		double tex40 = 8;
		double clip = 0.5;
		
		if(rotLeftSideZ < clip && rotRightSideZ < clip) {
			return;
		}
		
		if(rotLeftSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex30 = tex30 + (tex40 - tex30) * clip0;
		}
		if(rotRightSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex40 = tex30 + (tex40 - tex30) * clip0;
		}
		
		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2.0);
		double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2.0);
		
		if(xPixelLeft >= xPixelRight) {
			return;
		}
		
		int xPixelLeftInt = (int) xPixelLeft;
		int xPixelRightInt = (int) xPixelRight;
		
		if(xPixelLeftInt < 0) {
			xPixelLeftInt = 0;
		}
		if(xPixelRightInt > width) {
			xPixelRightInt = width;
		}
		
		double yPixelLeftTop = yCornerTL / rotLeftSideZ * height + height / 2.0;
		double yPixelLeftBottom = yCornerBL / rotLeftSideZ * height + height / 2.0;
		double yPixelRightTop = yCornerTR / rotRightSideZ * height + height / 2.0;
		double yPixelRightBottom = yCornerBR / rotRightSideZ * height + height / 2.0;
		
		double tex1 = 1 / rotLeftSideZ;
		double tex2 = 1 / rotRightSideZ;
		double tex3 = tex30 / rotLeftSideZ;
		double tex4 = tex40 / rotRightSideZ - tex3;
		
		for(int x = xPixelLeftInt ; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
			double zWall = (tex1 + (tex2 - tex1) * pixelRotation);
			
			if(zBufferWall[x] > zWall) {
				continue;
			}
			zBufferWall[x] = zWall;
			
			int xTexture = (int) ((tex3 + tex4 * pixelRotation) / zWall * 4);
			
			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;
			
			int yPixelTopInt = (int) (yPixelTop);
			int yPixelBottomInt = (int) (yPixelBottom);
			
			if(yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}
			if(yPixelBottomInt > height) {
				yPixelBottomInt = height;
			}
			
			for(int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
				int yTexture = (int) (8 * pixelRotationY * 4);
				
				//pixels[x + y * width] = xTexture * 100 + yTexture * 100 * 256;
				pixels[x + y * width] = Texture.floor.pixels[(xTexture & 31) + (yTexture & 31) * spriteSheetWidth];
				
				zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 8;
				
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
}
















