package theBOSS.Avoid.entity;

import theBOSS.Avoid.graphics.Screen;
import theBOSS.Avoid.graphics.Sprite;

public class Mob4 extends Entity {
    Player player;
    private int time = 0;

    public Mob4(double x, double y, Sprite sprite, Player player) {
        super(x, y, sprite);
        this.player = player;
    }

    public void update() {
        if (this.hitable) {
            if (this.x < this.player.x) {
                this.x += 1.0d;
            }
            if (this.x > this.player.x) {
                this.x -= 1.0d;
            }
            if (this.y < this.player.y) {
                this.y += 1.0d;
            }
            if (this.y > this.player.y) {
                this.y -= 1.0d;
            }
        }
        if (this.time > 120) {
            this.hitable = true;
        }
        this.time++;
    }

    public void render(Screen screen) {
        if (this.hitable) {
            screen.renderSprite((int) this.x, (int) this.y, -1, this.sprite);
        } else if (!this.hitable && this.time % 50 < 25) {
            screen.renderSprite((int) this.x, (int) this.y, -1, this.sprite);
        }
    }
}
