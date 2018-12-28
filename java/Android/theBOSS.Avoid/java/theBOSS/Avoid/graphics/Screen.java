package theBOSS.Avoid.graphics;

import java.util.Random;

public class Screen {
    public int[] bg;
    private int height;
    public int[] pixels;
    private Random rand = new Random();
    public int[] smallBg;
    private int width;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[(width * height)];
        this.bg = new int[(width * height)];
        this.smallBg = new int[(((width / 5) * height) / 5)];
        for (int i = 0; i < this.smallBg.length; i++) {
            int rgb = this.rand.nextInt(36) + 203;
            this.smallBg[i] = ((-16777216 | (rgb << 16)) | (rgb << 8)) | rgb;
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.bg[(y * width) + x] = this.smallBg[(x / 5) + (((y / 5) * width) / 5)];
            }
        }
    }

    public void clear() {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                this.pixels[(this.width * y) + x] = this.bg[(this.width * y) + x];
            }
        }
    }

    public void renderSprite(int xPos, int yPos, int trace, Sprite s) {
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