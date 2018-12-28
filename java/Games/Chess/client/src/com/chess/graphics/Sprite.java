package com.chess.graphics;

public class Sprite {
	
	public final int SIZE;
	private int x, y;
	private int width, height;
	public int[] pixels;
	public SpriteSheet sheet;
	
	//pices
	public static Sprite b_pown = new Sprite(16, 5, 0, SpriteSheet.chess);
	public static Sprite b_queen = new Sprite(16, 4, 0, SpriteSheet.chess);
	public static Sprite b_king = new Sprite(16, 3, 0, SpriteSheet.chess);
	public static Sprite b_knight = new Sprite(16, 2, 0, SpriteSheet.chess);
	public static Sprite b_bishop = new Sprite(16, 1, 0, SpriteSheet.chess);
	public static Sprite b_rook = new Sprite(16, 0, 0, SpriteSheet.chess);
	public static Sprite w_pown = new Sprite(16, 5, 1, SpriteSheet.chess);
	public static Sprite w_queen = new Sprite(16, 4, 1, SpriteSheet.chess);
	public static Sprite w_king = new Sprite(16, 3, 1, SpriteSheet.chess);
	public static Sprite w_knight = new Sprite(16, 2, 1, SpriteSheet.chess);
	public static Sprite w_bishop = new Sprite(16, 1, 1, SpriteSheet.chess);
	public static Sprite w_rook = new Sprite(16, 0, 1, SpriteSheet.chess);

	
	
	//true sprites here
	public static Sprite col1 = new Sprite(16, 6, 0, SpriteSheet.chess);
	public static Sprite col2 = new Sprite(16, 7, 0, SpriteSheet.chess);
	public static Sprite s1 = new Sprite(16, 0, 2, SpriteSheet.chess);
	public static Sprite s2 = new Sprite(16, 1, 2, SpriteSheet.chess);
	public static Sprite s3 = new Sprite(16, 2, 2, SpriteSheet.chess);
	public static Sprite s4 = new Sprite(16, 3, 2, SpriteSheet.chess);
	public static Sprite s5 = new Sprite(16, 4, 2, SpriteSheet.chess);
	public static Sprite s6 = new Sprite(16, 5, 2, SpriteSheet.chess);
	public static Sprite s7 = new Sprite(16, 6, 2, SpriteSheet.chess);
	public static Sprite s8 = new Sprite(16, 7, 2, SpriteSheet.chess);
	public static Sprite sa = new Sprite(16, 0, 3, SpriteSheet.chess);
	public static Sprite sb = new Sprite(16, 1, 3, SpriteSheet.chess);
	public static Sprite sc = new Sprite(16, 2, 3, SpriteSheet.chess);
	public static Sprite sd = new Sprite(16, 3, 3, SpriteSheet.chess);
	public static Sprite se = new Sprite(16, 4, 3, SpriteSheet.chess);
	public static Sprite sf = new Sprite(16, 5, 3, SpriteSheet.chess);
	public static Sprite sg = new Sprite(16, 6, 3, SpriteSheet.chess);
	public static Sprite sh = new Sprite(16, 7, 3, SpriteSheet.chess);
	public static Sprite squeer = new Sprite(16, 6, 1, SpriteSheet.chess);
	
	
	protected Sprite(SpriteSheet sheet, int width, int height){
		if(width == height) SIZE = width;
		else SIZE = -1;
		this.width = width;
		this.height = height;
		this.sheet = sheet;
	}
	
	public Sprite(int size, int x, int y, SpriteSheet sheet){
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE * SIZE];
		this.x = x * size;
		this.y = y * size;
		this.sheet = sheet;
		load();
	}
	
	public Sprite(int width, int height, int colour){
		SIZE = -1;
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		setColour(colour);
	}

	public Sprite(int size, int colour){
		SIZE = size;
		this.width = size;
		this.height = size;
		pixels = new int[SIZE*SIZE];
		setColour(colour);
	}
	
	public Sprite(int[] pixels, int width, int height){
		SIZE = (width == height) ? width : -1;
		this.width = width;
		this.height = height;
		this.pixels = new int[pixels.length];		
		for(int i = 0; i < pixels.length; i++){
			this.pixels[i] = pixels[i];
		}
	}
	
	public static Sprite rotate(Sprite sprite, double angle){
		return new Sprite(rotate(sprite.pixels, sprite.width, sprite.height, angle), sprite.width, sprite.height);
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
				if(xx < 0 || xx>= width || yy < 0 || yy >= height) col = 0xffff00ff; 
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
		return x * cos + y * -sin;
	}
	
	private static double rot_y(double angle, double x, double y){
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * sin + y * cos;
	}
	
	public static Sprite[] split(SpriteSheet sheet) {
		int amount = (sheet.getWidth() * sheet.getHeight()) / (sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT);
		Sprite[] sprites = new Sprite[amount];
		int current = 0;
		int[] pixels = new int[sheet.SPRITE_WIDTH * sheet.SPRITE_HEIGHT];
		
		for(int yp = 0; yp < sheet.getHeight() / sheet.SPRITE_HEIGHT; yp++){
			for(int xp = 0; xp < sheet.getWidth() / sheet.SPRITE_WIDTH; xp++){
				for(int y = 0; y < sheet.SPRITE_HEIGHT; y++){
					for(int x = 0; x < sheet.SPRITE_WIDTH; x++){
						int xo = x + xp * sheet.SPRITE_WIDTH;
						int yo = y + yp * sheet.SPRITE_WIDTH;
						pixels[x + y * sheet.SPRITE_WIDTH] = sheet.getPixels()[xo + yo * sheet.getWidth()];
					}
				}
				sprites[current++] = new Sprite(pixels, sheet.SPRITE_WIDTH, sheet.SPRITE_HEIGHT);
			}
		}
		return sprites;
	}
	
	
	private void setColour(int colour) {
		for(int i = 0; i < width * height; i++){
			pixels[i] = colour;
		}
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	private void load() {
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				pixels[x + y * width] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SPRITE_WIDTH];
			}
		}
	}
}
