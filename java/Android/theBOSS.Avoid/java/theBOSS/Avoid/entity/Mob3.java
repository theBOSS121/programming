package theBOSS.Avoid.entity;

import java.util.Random;
import theBOSS.Avoid.graphics.Screen;
import theBOSS.Avoid.graphics.Sprite;

public class Mob3 extends Entity {
    private static Random rand = new Random();
    private int time = 0;
    private int xd;
    private int yd;

    public Mob3(double x, double y, Sprite sprite) {
        super(x, y, sprite);
        changeDir();
    }

    public void update() {
        if (this.x + ((double) this.xd) < 0.0d || (this.x + ((double) this.xd)) + this.width > 600.0d) {
            this.xd *= -1;
        }
        if (this.y + ((double) this.yd) < 0.0d || (this.y + ((double) this.yd)) + this.height > 350.0d) {
            this.yd *= -1;
        }
        if (this.hitable) {
            this.x += (double) this.xd;
            this.y += (double) this.yd;
            if (this.time % 240 == 120) {
                changeDir();
            }
        }
        if (this.time > 120) {
            this.hitable = true;
        }
        this.time++;
    }

    private void changeDir() {
        while (true) {
            this.xd = rand.nextInt(7) - 3;
            this.yd = rand.nextInt(7) - 3;
            if ((this.xd == this.yd || this.xd == (-this.yd)) && this.xd != 0 && this.yd != 0) {
                return;
            }
        }
    }

    public void render(Screen screen) {
        if (this.hitable) {
            screen.renderSprite((int) this.x, (int) this.y, -1, this.sprite);
        } else if (!this.hitable && this.time % 50 < 25) {
            screen.renderSprite((int) this.x, (int) this.y, -1, this.sprite);
        }
    }
}
