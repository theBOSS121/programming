package theBOSS.Avoid.entity;

import theBOSS.Avoid.Avoid;
import theBOSS.Avoid.graphics.Screen;
import theBOSS.Avoid.graphics.Sprite;

public class Player extends Entity {
    int trace;

    public Player(double x, double y, Sprite sprite) {
        super(x, y, sprite);
        this.trace = -1;
        this.hitable = true;
    }

    public void update() {
        move();
    }

    private void move() {
        double xm = ((double) Avoid.sensorX) * 2.0d;
        double ym = ((double) Avoid.sensorY) * 2.0d;
        if (xm < 0.5d && xm > -0.5d && ym < 0.5d && ym > -0.5d) {
            xm = 0.0d;
            ym = 0.0d;
        }
        this.x += xm;
        this.y += ym;
        if (this.x < 0.0d) {
            this.x = 0.0d;
        }
        if (this.y < 0.0d) {
            this.y = 0.0d;
        }
        if (this.x + this.width > 600.0d) {
            this.x = 600.0d - this.width;
        }
        if (this.y + this.height > 350.0d) {
            this.y = 350.0d - this.height;
        }
        setTraceDir((int) xm, (int) ym);
    }

    public void setTraceDir(int xm, int ym) {
        if (xm == 0 && ym == 0) {
            this.trace = -1;
        }
        if (xm > 0 && ym > 0) {
            this.trace = 0;
        }
        if (xm < 0 && ym > 0) {
            this.trace = 1;
        }
        if (xm < 0 && ym < 0) {
            this.trace = 2;
        }
        if (xm > 0 && ym < 0) {
            this.trace = 3;
        }
        if (xm == 0 && ym > 0) {
            this.trace = 4;
        }
        if (xm == 0 && ym < 0) {
            this.trace = 5;
        }
        if (ym == 0 && xm > 0) {
            this.trace = 6;
        }
        if (ym == 0 && xm < 0) {
            this.trace = 7;
        }
    }

    public void render(Screen screen) {
        screen.renderSprite((int) this.x, (int) this.y, this.trace, this.sprite);
    }
}