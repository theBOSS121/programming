package com.chess.level.tile;

import com.chess.graphics.Screen;
import com.chess.graphics.Sprite;

public class Tile {
	
	public Sprite sprite;
	
	public static Tile col1 = new Tile(Sprite.col1);
	public static Tile col2 = new Tile(Sprite.col2);
	public static Tile s1 = new Tile(Sprite.s1);
	public static Tile s2 = new Tile(Sprite.s2);
	public static Tile s3 = new Tile(Sprite.s3);
	public static Tile s4 = new Tile(Sprite.s4);
	public static Tile s5 = new Tile(Sprite.s5);
	public static Tile s6 = new Tile(Sprite.s6);
	public static Tile s7 = new Tile(Sprite.s7);
	public static Tile s8 = new Tile(Sprite.s8);
	public static Tile sa = new Tile(Sprite.sa);
	public static Tile sb = new Tile(Sprite.sb);
	public static Tile sc = new Tile(Sprite.sc);
	public static Tile sd = new Tile(Sprite.sd);
	public static Tile se = new Tile(Sprite.se);
	public static Tile sf = new Tile(Sprite.sf);
	public static Tile sg = new Tile(Sprite.sg);
	public static Tile sh = new Tile(Sprite.sh);
	public static Tile sq = new Tile(Sprite.squeer);

	public final static int c1 = 0xff808080;
	public final static int c2 = 0xff724715;
	public final static int sq1 = 0xff0000ff;
	public final static int sq2 = 0xff0001ff;
	public final static int sq3 = 0xff0002ff;
	public final static int sq4 = 0xff0003ff;
	public final static int sq5 = 0xff0004ff;
	public final static int sq6 = 0xff0005ff;
	public final static int sq7 = 0xff0006ff;
	public final static int sq8 = 0xff0007ff;
	public final static int squeer = 0xff0008ff;
	public final static int sqa = 0xff0009ff;
	public final static int sqb = 0xff0010ff;
	public final static int sqc = 0xff0011ff;
	public final static int sqd = 0xff0012ff;
	public final static int sqe = 0xff0013ff;
	public final static int sqf = 0xff0014ff;
	public final static int sqg = 0xff0015ff;
	public final static int sqh = 0xff0016ff;

	
	public Tile(Sprite sprite){
		this.sprite = sprite;
	}
	
	public void render(int x, int y, Screen screen){
		screen.renderTile(x << 4, y << 4, this);
	}
	
}
