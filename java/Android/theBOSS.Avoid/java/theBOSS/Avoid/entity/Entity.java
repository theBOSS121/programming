package theBOSS.Avoid.entity;

import theBOSS.Avoid.graphics.Screen;
import theBOSS.Avoid.graphics.Sprite;

public class Entity {
    public double height;
    public boolean hitable = false;
    public Sprite sprite;
    public double width;
    public double x;
    public double y;

    public Entity(double x, double y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.width = (double) sprite.width;
        this.height = (double) sprite.height;
    }

    public void update() {
    }

    public void render(Screen screen) {
    }
}
