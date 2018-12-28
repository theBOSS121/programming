package theBOSS.PongUDPClient;

import android.graphics.Bitmap;

public class Sprite {
    Bitmap b;
    public int height;
    public int width;
    public int[] pixels;

    public Sprite(Bitmap b) {
        this.b = b;
        this.width = b.getWidth();
        this.height = b.getHeight();
        pixels = new int[width * height];
        b.getPixels(pixels, 0, width, 0, 0, width, height);
    }
}