package theBOSS.PongUDPClient;

public class Screen {
	
    private int height;
    private int width;
    public int[] pixels;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[(width * height)];
    }

    public void clear() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.pixels[x + y * this.width] = 0xffffff00;
            }
        }
    }

    public void renderSprite(int xPos, int yPos, Sprite s) {
        for (int y = 0; y < s.height; y++) {
            int yp = y + yPos;
            for (int x = 0; x < s.width; x++) {
                int xp = x + xPos;
                int col = s.pixels[(s.width * y) + x];
                if (col < 0 && (this.width * yp) + xp < this.pixels.length && (this.width * yp) + xp >= 0) {
                    this.pixels[(this.width * yp) + xp] = col;
                }
            }
        }
    }
}
