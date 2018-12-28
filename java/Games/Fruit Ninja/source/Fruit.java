import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Fruit {
	public double angle, x, y, rotation, xdir, ydir;
	public BufferedImage image;
	public int[] pixels, newPixels;
	Random rand = new Random();
	public boolean cuted = false, in = false;
	public boolean removed = false;
	
	public Fruit(double x, double y, double rotation, int which) {
		this.x = x;
		this.y = y;
		if(x < FruitNinja.WIDTH / 4) {
			xdir = rand.nextDouble() * rand.nextInt(6) + 3.5;	
		}else if(x < FruitNinja.WIDTH / 2) {
			xdir = rand.nextDouble() * rand.nextInt(4) + 1;	
		}else if(x < FruitNinja.WIDTH / 4 * 3) {
			xdir = -rand.nextDouble() * rand.nextInt(4) - 1;	
		}else {
			xdir = -rand.nextDouble() * rand.nextInt(6) - 3.5;
		}
		ydir = -15.0;
		
		this.rotation = rotation;
		try {
			FruitNinja.f1 = ImageIO.read(Renderer.class.getResource("/fruit1.png"));
			FruitNinja.f2 = ImageIO.read(Renderer.class.getResource("/fruit2.png"));
			FruitNinja.f3 = ImageIO.read(Renderer.class.getResource("/fruit3.png"));
			FruitNinja.f4 = ImageIO.read(Renderer.class.getResource("/fruit4.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(which == 0) {
			image = FruitNinja.f1;
		}
		if(which == 1) {
			image = FruitNinja.f2;
		}
		if(which == 2) {
			image = FruitNinja.f3;
		}
		if(which == 3) {
			image = FruitNinja.f4;
		}
		pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
	}

	public void update() {
		if(!in && y < FruitNinja.WIDTH) {
			in = true;
		}
		angle += rotation;
		newPixels = rotate(pixels, image.getWidth(), image.getHeight(), angle);
		image.setRGB(0, 0, image.getWidth(), image.getHeight(), newPixels, 0, image.getWidth());
		
		ydir += 0.17;
		y += ydir;
		x += xdir;
		
	}
	
	private static int[] rotate(int[] pixels, int width, int height, double angle){
		int[] result = new int[width * height]; 
		
		double nx_x = rot_x(-angle, 1.0, 0.0);
		double nx_y = rot_y(-angle, 1.0, 0.0);
		double ny_x = rot_x(-angle, 0.0, 1.0);
		double ny_y = rot_y(-angle, 0.0, 1.0);
		
		double x0 = rot_x(-angle, -width / 2.0, -height / 2.0) + width / 2.0;
		double y0 = rot_y(-angle, -width / 2.0, -height / 2.0) + height / 2.0;
		
		for(int y = 0; y < height; y++){
			double x1 = x0;
			double y1 = y0;
			for(int x = 0; x < width; x++){
				int xx = (int) x1;
				int yy = (int) y1;
				int col = 0;
				if(xx < 0 || xx>= width || yy < 0 || yy >= height) col = 0x0; 
				else col = pixels[xx + yy * width];
				result[x + y * width] = col;
				x1 += nx_x;
				y1 += nx_y;
			}
			x0 += ny_x;
			y0 += ny_y;
		}	
		return result;
	}
	
	private static double rot_x(double angle, double x, double y){
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * cos - y * sin;
	}
	
	private static double rot_y(double angle, double x, double y){
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * sin + y * cos;
	}
}
