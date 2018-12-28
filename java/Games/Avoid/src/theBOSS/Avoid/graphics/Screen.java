package theBOSS.Avoid.graphics;

public class Screen {
	private int width, height;
	public int[] pixels;
	
	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
	}
	
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i]  = Sprite.bg.pixels[i];
		}
	}
	
	//trace je sled za igralcem nism se naredu
	public void renderSprite(int xPos, int yPos, int trace, Sprite s) {
		for(int y = 0; y < s.height; y++) {		
			int yp = y + yPos;
			for(int x = 0; x < s.width; x++) {		
				int xp = x + xPos;		
				int col = s.pixels[x + y * s.width];
				if(col == 0xffff00ff) continue;
				if(xp + yp * width >= pixels.length || xp + yp * width < 0) continue;
				pixels[xp + yp * width] = col;			
			}	
		}
	}
}
