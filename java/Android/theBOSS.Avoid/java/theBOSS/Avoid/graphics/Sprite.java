package theBOSS.Avoid.graphics;

import android.graphics.Bitmap;

public class Sprite {
    Bitmap b;
    public int height;
    public int[] pixels = new int[(this.width * this.height)];
    public int width;

    public Sprite(Bitmap b) {
        this.b = b;
        this.width = b.getWidth();
        this.height = b.getHeight();
        b.getPixels(this.pixels, 0, this.width, 0, 0, this.width, this.height);
    }
}
