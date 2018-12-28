package com.theBOSS.retroshooter;

public class Screen {
	
	private int width, height;
	public int[] pixels;
	
	public int offX = 0, offY = 0;
	@SuppressWarnings("unused")
	private boolean processing = false;
	//light map
	private int[] lm;
	//light block
	private int[] lb;
	private int ambientColor = 0xffffffff;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		lm = new int[pixels.length];
		lb = new int[pixels.length];
	}

	public void clear() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				pixels[x + y * width] = Renderer.bg.pixels[(Math.abs(x + offX) % Renderer.bg.width) + (Math.abs(y + offY) % Renderer.bg.height) * Renderer.bg.width];
				lm[x + y * width] = ambientColor;
				lb[x + y * width] = 0;
			}
		}
	}
	
	public void process() {	
		processing = true;
		for(int i = 0; i < pixels.length; i++) {
			float r = ((lm[i] >> 16) & 0xff) / 255f;
			float g = ((lm[i] >> 8) & 0xff) / 255f;
			float b = (lm[i] & 0xff) / 255f;
			pixels[i] = (0xff << 24 | (int) (((pixels[i] >> 16) & 0xff) * r) << 16 | (int) (((pixels[i] >> 8) & 0xff) * g) << 8 | (int) ((pixels[i] & 0xff) * b));
		}
		processing = false;
	}
	
	public void setOffSet(int offX, int offY) {
		this.offX = offX;
		this.offY = offY;
	}
	
	public void renderSprite(Sprite s, int xp, int yp) {
		xp -= offX;
		yp -= offY;
		if(xp < -s.width || yp < -s.height || xp >= width || yp >= height) return;
		int y0 = 0;
		int x0 = 0;
		int y1 = s.height;
		int x1 = s.width;
		if(xp < 0) x0 = -xp;
		if(yp < 0) y0 = -yp;
		if(xp + s.width >= width) x1 = width - xp;
		if(yp + s.height >= height) y1 = height - yp;
		
		for(int y = y0; y < y1; y++) {
				int yy = y + yp;
				if(yy >= height || yy < 0) continue;
			for(int x = x0; x < x1; x++) {
				int xx = x + xp;
				if(xx < 0 || xx >= width) continue;
				int col = combineColor(s.pixels[x + y * s.width], xx, yy);
				
				pixels[xx + yy * width] = col;
				setLightBlock(xx, yy, s.lightBlock);										
			}
		}
	}
	
	public void renderSpriteNoOffsets(Sprite s, int xp, int yp) {
		if(xp < -s.width || yp < -s.height || xp >= width || yp >= height) return;
		int y0 = 0;
		int x0 = 0;
		int y1 = s.height;
		int x1 = s.width;
		if(xp < 0) x0 = -xp;
		if(yp < 0) y0 = -yp;
		if(xp + s.width >= width) x1 = width - xp;
		if(yp + s.height >= height) y1 = height - yp;
		
		for(int y = y0; y < y1; y++) {
				int yy = y + yp;
				if(yy >= height || yy < 0) continue;
			for(int x = x0; x < x1; x++) {
				int xx = x + xp;
				if(xx < 0 || xx >= width) continue;
				int col = combineColor(s.pixels[x + y * s.width], xx, yy);
				
				pixels[xx + yy * width] = col;
				setLightBlock(xx, yy, s.lightBlock);										
			}
		}
	}
	
	public void renderPixel(int xp, int yp, int color) {
		xp -= offX;
		yp -= offY;
		if(xp < -0 || yp < 0 || xp >= width || yp >= height) return;
		int col = combineColor(color, xp, yp);
		pixels[xp + yp * width] = col;
	}
	
	public void renderLine(int xp, int yp, int xDir, int yDir, int color){
		xp -= offX;
		yp -= offY;
		if(xp < -0 || yp < 0 || xp >= width || yp >= height) return;
		while(xp >= 0 && yp >= 0 && xp < width && yp < height) {
			int col = combineColor(color, xp, yp);
			pixels[xp + yp * width] = col;
			xp += xDir;
			yp += yDir;
		}
	}
	
	private int combineColor(int col, int x, int y) {
		int pixelCol = pixels[x + y * width];
		int alpha = (col >> 24) & 0xff;
		if(alpha == 0xff) return col;
		if(alpha <= 0) return pixelCol;
		
		int pr = (pixelCol >> 16) & 0xff;
		int pg = (pixelCol >> 8) & 0xff;
		int pb = (pixelCol) & 0xff;
		int r = (col >> 16) & 0xff;
		int g = (col >> 8) & 0xff;
		int b = (col) & 0xff;
		
		int nr = (int) (pr - ((pr - r) * (alpha / 255f)));
		int ng = (int) (pg - ((pg - g) * (alpha / 255f)));
		int nb = (int) (pb - ((pb - b) * (alpha / 255f)));
		
		return (nr << 16) | (ng << 8) | nb;
	}
	
	public void setLightMap(int x, int y, int value) {
		if(x < 0 || x >= width || y < 0 || y >= height) return;
		
		if(lm[x + y * width] == ambientColor) {
			int baseColor = lm[x + y * width];
			
			int maxRed = Math.max(((baseColor >> 16) & 0xff), ((value >> 16) & 0xff));
			int maxGreen = Math.max(((baseColor >> 8) & 0xff), ((value >> 8) & 0xff));
			int maxBlue = Math.max((baseColor & 0xff), (value & 0xff));
			
			lm[x + y * width] = (0xff << 24 | maxRed << 16 | maxGreen << 8 | maxBlue);
		}else {
			int baseColor = lm[x + y * width];
			if(value < ambientColor) return;
			int maxRed = (int) ((((baseColor >> 16) & 0xff) + ((value >> 16) & 0xff)) * 0.9);
			if(maxRed > 0xff) maxRed = 0xff;
			int maxGreen = (int) ((((baseColor >> 8) & 0xff) + ((value >> 8) & 0xff)) * 0.9);
			if(maxGreen > 0xff) maxGreen = 0xff;
			int maxBlue = (int) ((((baseColor) & 0xff) + ((value) & 0xff)) * 0.9);
			if(maxBlue > 0xff) maxBlue = 0xff;
			lm[x + y * width] = (0xff << 24 | maxRed << 16 | maxGreen << 8 | maxBlue);			
			if(lm[x + y * width] > 0xffffffff) lm[x + y * width] = 0xffffffff;
		}
	}
	
	public void setLightBlock(int x, int y, int value) {
		if(x < 0 || x >= width || y < 0 || y >= height) return;
//		if(zb[x + y * pW] > zDepth) return;
		
		lb[x + y * width] = value;
	}
	
	public void renderLight(Light l, int offX, int offY) {	
		offX -= this.offX;
		offY -= this.offY;		
		if(offX + l.getRadius() < 0 || offY + l.getRadius() < 0 || offX - l.getRadius() > width || offY - l.getRadius() > height) return;
		for(int i = 0; i <= l.getDiameter(); i++) {
			drawLightLine(l, l.getRadius(), l.getRadius(), i, 0, offX, offY);
			drawLightLine(l, l.getRadius(), l.getRadius(), i, l.getDiameter(), offX, offY);
			drawLightLine(l, l.getRadius(), l.getRadius(), 0, i, offX, offY);
			drawLightLine(l, l.getRadius(), l.getRadius(), l.getDiameter(), i, offX, offY);
		}
	}
	
	private void drawLightLine(Light l, int x0, int y0, int x1, int y1, int offX, int offY) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		
		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		
		int err = dx - dy;
		int e2;
		
		while(true) {
			int screenX = x0 - l.getRadius() + offX;
			int screenY = y0 - l.getRadius() + offY;
			
			int lightColor = l.getLightValue(x0, y0);
			if(lightColor == 0) continue;
			
			if(screenX >= 0 && screenX < width && screenY >= 0 && screenY < height) {
				if(lb[screenX + screenY * width] == Light.FULL) return;
				setLightMap(screenX, screenY, lightColor);			
			}
			
			if(x0 == x1 && y0 == y1) break;
			e2 = 2 * err;
			if(e2 > -1 * dy) {
				err -= dy;
				x0 += sx;
			}
			if(e2 < dx) {
				err += dx;
				y0 += sy;
			}
		}
	}

	public int getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(int ambientColor) {
		this.ambientColor = ambientColor;
	}
	
}
