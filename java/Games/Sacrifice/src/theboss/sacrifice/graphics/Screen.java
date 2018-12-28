package theboss.sacrifice.graphics;

public class Screen {

	private int width, height;
	public int[] pixels;
	
	public int offX = 0, offY = 0;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}

	public void clear() {
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				pixels[x + y * width] = Sprite.bg.pixels[x + y * width];
			}
		}
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
			}
		}
	}
	
	public void renderSprite(Sprite s, int xp, int yp, double alpha) {
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
				int col = combineColor(s.pixels[x + y * s.width], xx, yy, alpha);
				pixels[xx + yy * width] = col;										
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
			}
		}
	}
	
	private int combineColor(int col, int x, int y, double a) {
		int pixelCol = pixels[x + y * width];
		int alpha = (int) (((col >> 24) & 0xff) * a);
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
	
	public void renderPixel(int xp, int yp, int color) {
        xp -= offX;
        yp -= offY;
        if(xp < -0 || yp < 0 || xp >= width || yp >= height) return;
        int col = combineColor(color, xp, yp);
        pixels[xp + yp * width] = col;
    }
	
}
