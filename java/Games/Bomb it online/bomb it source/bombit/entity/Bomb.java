package com.bombit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import com.bombit.Game;
import com.bombit.entity.mob.Mob;
import com.bombit.graphics.Screen;
import com.bombit.graphics.Sprite;
import com.bombit.level.Level;
import com.bombit.level.tile.Tile;

public class Bomb extends Entity{

	private boolean planted, exploded, complete = false;
	private boolean[] done;
	private int timer = 0;
	private int range;
	private boolean left = false, right = false, up = false, down = false;
	private Mob mob;
	private Random random = new Random();
	private List<Mob> mobs = new ArrayList<Mob>();
	
	public Bomb(double x, double y, int range, Mob mob, Level level) {
		init(level);
		this.x = x - 8;
		this.y = y + 16;
		this.x = Math.round(this.x/16) * 16 + 8;
		this.y = Math.round(this.y/16) * 16 - 8;
		this.range = range;
		this.mob = mob;
		for(int i = 0; i < level.players.size(); i++) {
			Mob m = level.players.get(i);
			if((this.x / 16 - 0.5) == Math.round((m.x - 8) / 16) && (this.y / 16 - 0.5) == Math.round((m.y - 8) / 16)){
				mobs.add(m);
			}
		}
		
		done = new boolean[mobs.size()];
		for(int i = 0; i < mobs.size(); i++) {
			Mob m = mobs.get(i);
			m.lefted = false;
			done[i] = false;
		}
		
		planted = true;
		sprite = Sprite.bomb;
	}
	
	public void update() {
		timer++;
		for(int i = 0; i < mobs.size(); i++) {
			if(done[i] == false) {
				complete = false;
				break;
			}else {
				complete = true;
			}
		}
		
		if(exploded == false && !complete) {
			for(int i = 0; i < mobs.size(); i++) {
				Mob m = mobs.get(i);
				if(( (this.x / 16 - 0.5) + 0.99 < (m.x - 8) / 16 || (this.x / 16 - 0.5) - 0.99 > (m.x - 8) / 16) 
				|| ((this.y / 16 - 0.5) + 0.99 < (m.y / 16) || (this.y / 16 - 0.5) - 0.99 > (m.y / 16))) {
					m.lefted = true;
					done[i] = true;
				}
			}
		}
		
		if(timer == 120) {
			mob.bombs++;
			sprite = Sprite.explodedBomb;
			exploded = true;
			planted = false;
		}else if(timer < 180 && timer > 120) {
			playerCollision();
			
		}if(timer > 180) {
			collision();
			this.remove();
		}
	}
	
	private void playerCollision() {
		int xx = (int) (this.x / 16 - 0.5);
		int yy = (int) (this.y / 16 - 0.5);
		int x2 = xx;
		int y2 = yy; 
		for(int i1 = 0; i1 < level.players.size(); i1++) {
			Mob e = level.players.get(i1);
			if(x2 == (int) Math.round((e.x - 8) / 16) && y2 == (int) Math.round(e.y / 16)) {
				e.remove();
			}
			
		}
		right = false;
		left = false;
		down = false;
		up = false;
		for(int i = 1; i < range + 1; i++) {
			if(!right) {
				x2 = xx + i;
				y2 = yy;
				if(level.getTile(x2, y2) == Tile.spawn_wall2 || level.getTile(x2, y2) == Tile.spawn_wall1) {
					right = true;
				}
				for(int i1 = 0; i1 < level.players.size(); i1++) {
					Mob e = level.players.get(i1);
					if(x2 == (int) Math.round((e.x - 8) / 16) && y2 == (int) Math.round(e.y / 16)) {
						e.remove();
					}
					
				}
			}
			if(!left) {
				x2 = xx - i;
				y2 = yy;
				if(level.getTile(x2, y2) == Tile.spawn_wall2 || level.getTile(x2, y2) == Tile.spawn_wall1) {
					left = true;
				}
				for(int i1 = 0; i1 < level.players.size(); i1++) {
					Mob e = level.players.get(i1);
					if(x2 == (int) Math.round((e.x - 8) / 16) && y2 == (int) Math.round(e.y / 16)) {
						e.remove();
					}
					
				}
			}
			if(!down) {
				x2 = xx;
				y2 = yy + i;
				if(level.getTile(x2, y2) == Tile.spawn_wall2 || level.getTile(x2, y2) == Tile.spawn_wall1) {
					down = true;
				}
				for(int i1 = 0; i1 < level.players.size(); i1++) {
					Mob e = level.players.get(i1);
					if(x2 == (int) Math.round((e.x - 8) / 16) && y2 == (int) Math.round(e.y / 16)) {
						e.remove();
					}
					
				}
			}
			if(!up) {
				x2 = xx;
				y2 = yy - i;
				if(level.getTile(x2, y2) == Tile.spawn_wall2 || level.getTile(x2, y2) == Tile.spawn_wall1) {
					up = true;
				}
				for(int i1 = 0; i1 < level.players.size(); i1++) {
					Mob e = level.players.get(i1);
					if(x2 == (int) Math.round((e.x - 8) / 16) && y2 == (int) Math.round(e.y / 16)) {
						e.remove();
					}
				}
			}
		}
	}

	private void collision() {
		int xx = (int) (this.x / 16 - 0.5);
		int yy = (int) (this.y / 16 - 0.5);
		int x2 = xx;
		int y2 = yy; 
		right = false;
		left = false;
		down = false;
		up = false;
		for(int i = 1; i < range + 1; i++) {
			if(!right) {
				x2 = xx + i;
				y2 = yy;
				if(level.getTile(x2, y2) == Tile.spawn_wall2) {
					level.tiles[x2 + y2 * level.width] = Tile.col_spawn_floor;
					if(random.nextInt(3) == 0 && !Game.multiplayer) {
						int a = random.nextInt(3);
						if(a == 0) {
							level.add(new plusBomb(x2 * 16, y2 * 16));
							
						}else if(a == 1) {
							level.add(new plusRange(x2 * 16, y2 * 16));
							
						}else if(a == 2) {
							level.add(new Speed(x2 * 16, y2 * 16));
							
						}
					}
					right = true;
				}
				if(level.getTile(x2, y2) == Tile.spawn_wall1) {
					right = true;
				}
			}
			if(!left) {
				x2 = xx - i;
				y2 = yy;
				if(level.getTile(x2, y2) == Tile.spawn_wall2) {
					level.tiles[x2 + y2 * level.width] = Tile.col_spawn_floor;
					if(random.nextInt(3) == 0 && !Game.multiplayer) {
						int a = random.nextInt(3);
						if(a == 0) {
							level.add(new plusBomb(x2 * 16, y2 * 16));
							
						}else if(a == 1) {
							level.add(new plusRange(x2 * 16, y2 * 16));
							
						}else if(a == 2) {
							level.add(new Speed(x2 * 16, y2 * 16));
							
						}
					}
					left = true;
				}
				if(level.getTile(x2, y2) == Tile.spawn_wall1) {
					left = true;
				}
			}
			if(!down) {
				x2 = xx;
				y2 = yy + i;
				if(level.getTile(x2, y2) == Tile.spawn_wall2) {
					level.tiles[x2 + y2 * level.width] = Tile.col_spawn_floor;
					if(random.nextInt(3) == 0 && !Game.multiplayer) {
						int a = random.nextInt(3);
						if(a == 0) {
							level.add(new plusBomb(x2 * 16, y2 * 16));
							
						}else if(a == 1) {
							level.add(new plusRange(x2 * 16, y2 * 16));
							
						}else if(a == 2) {
							level.add(new Speed(x2 * 16, y2 * 16));
							
						}
					}
					down = true;
				}
				if(level.getTile(x2, y2) == Tile.spawn_wall1) {
					down = true;
				}
			}
			if(!up) {
				x2 = xx;
				y2 = yy - i;
				if(level.getTile(x2, y2) == Tile.spawn_wall2) {
					level.tiles[x2 + y2 * level.width] = Tile.col_spawn_floor;
					if(random.nextInt(3) == 0 && !Game.multiplayer) {
						int a = random.nextInt(3);
						if(a == 0) {
							level.add(new plusBomb(x2 * 16, y2 * 16));
							
						}else if(a == 1) {
							level.add(new plusRange(x2 * 16, y2 * 16));
							
						}else if(a == 2) {
							level.add(new Speed(x2 * 16, y2 * 16));
							
						}
					}
					up = true;
				}
				if(level.getTile(x2, y2) == Tile.spawn_wall1) {
					up = true;
				}
			}
		}
	}

	public void render(Screen screen) {
		if(planted)
			screen.renderSprite((int) x - 8,(int) y - 8, sprite, true);
		if(exploded) {
			screen.renderSprite((int) x - 8,(int) y - 8, sprite, true);
			int xx = (int) (this.x / 16 - 0.5);
			int yy = (int) (this.y / 16 - 0.5);
			int x2 = xx;
			int y2 = yy; 
			right = false;
			left = false;
			down = false;
			up = false;
			for(int i = 1; i < range + 1; i++) {
				if(!right) {
					x2 = xx + i;
					y2 = yy;
					if(level.getTile(x2, y2) == Tile.spawn_floor) {
						screen.renderSprite((int) x + (16 * i) - 8,(int) y - 8, sprite, true);
					}
					if(level.getTile(x2, y2) == Tile.spawn_wall2) {		
						screen.renderSprite((int) x + (16 * i) - 8,(int) y - 8, sprite, true);				
						right = true;
					}
					if(level.getTile(x2, y2) == Tile.spawn_wall1) {
						right = true;
					}
				}
				if(!left) {
					x2 = xx - i;
					y2 = yy;
					if(level.getTile(x2, y2) == Tile.spawn_floor) {
						screen.renderSprite((int) x - (16 * i) - 8,(int) y - 8, sprite, true);
					}
					if(level.getTile(x2, y2) == Tile.spawn_wall2) {		
						screen.renderSprite((int) x - (16 * i) - 8,(int) y - 8, sprite, true);				
						left = true;
					}
					if(level.getTile(x2, y2) == Tile.spawn_wall1) {
						left = true;
					}
				}
				if(!down) {
					x2 = xx;
					y2 = yy + i;
					if(level.getTile(x2, y2) == Tile.spawn_floor) {
						screen.renderSprite((int) x - 8,(int) y + (16 * i) - 8, sprite, true);
					}
					if(level.getTile(x2, y2) == Tile.spawn_wall2) {
						screen.renderSprite((int) x - 8,(int) y + (16 * i) - 8, sprite, true);
						down = true;
					}
					if(level.getTile(x2, y2) == Tile.spawn_wall1) {
						down = true;
					}
				}
				if(!up) {
					x2 = xx;
					y2 = yy - i;
					if(level.getTile(x2, y2) == Tile.spawn_floor) {
						screen.renderSprite((int) x - 8,(int) y - (16 * i) - 8, sprite, true);
					}
					if(level.getTile(x2, y2) == Tile.spawn_wall2) {
						screen.renderSprite((int) x - 8,(int) y - (16 * i) - 8, sprite, true);
						up = true;
					}
					if(level.getTile(x2, y2) == Tile.spawn_wall1) {
						up = true;
					}
				}
			}
		
		}
	}
	
}
