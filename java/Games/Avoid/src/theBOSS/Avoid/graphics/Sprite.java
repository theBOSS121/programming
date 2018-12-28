package theBOSS.Avoid.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {

	public int width, height;
	public int[] pixels;
	
	public static Sprite blue_ball = new Sprite("/blue_ball.png");
	public static Sprite orange_ball = new Sprite("/orange_ball.png");
	public static Sprite green_ball = new Sprite("/green_ball.png");
	public static Sprite purple_ball = new Sprite("/purple_ball.png");
	public static Sprite yellow_ball = new Sprite("/yellow_ball.png");
	public static Sprite bg = new Sprite("/bg.png");
	
	
	public Sprite(String path){
		try {
			BufferedImage image = ImageIO.read(Sprite.class.getResource(path));
			this.width = image.getWidth();
			this.height = image.getHeight();
			pixels = image.getRGB(0, 0, width, height, null, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
