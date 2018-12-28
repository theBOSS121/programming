import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Object {
	
	public List<Squeer> squeers  = new ArrayList<Squeer>();
	int index = 1;
	int speed = 60;
	int which;
	int position = 0;
	int col;
	boolean falling;
	boolean left = false;
	boolean right = false;
	boolean up = false;
	
	public Object(int which, int col, boolean falling) {
		this.which = which;
		this.col = col;
		this.falling = falling;
		//which object
		if(which == 0) {
			squeers.add(new Squeer(4, 0, col));
			squeers.add(new Squeer(5, 0, col));
			squeers.add(new Squeer(4, -1, col));
			squeers.add(new Squeer(5, -1, col));
		}
		if(which == 1) {
			squeers.add(new Squeer(3, 0, col));
			squeers.add(new Squeer(4, 0, col));
			squeers.add(new Squeer(5, 0, col));
			squeers.add(new Squeer(6, 0, col));
		}
		if(which == 2) {
			squeers.add(new Squeer(4, 0, col));
			squeers.add(new Squeer(4, -1, col));
			squeers.add(new Squeer(5, -1, col));
			squeers.add(new Squeer(4, -2, col));
		}
		if(which == 3) {
			squeers.add(new Squeer(3, 0, col));
			squeers.add(new Squeer(4, 0, col));
			squeers.add(new Squeer(4, -1, col));
			squeers.add(new Squeer(5, -1, col));
		}
		if(which == 4) {
			squeers.add(new Squeer(4, 0, col));
			squeers.add(new Squeer(5, 0, col));
			squeers.add(new Squeer(3, -1, col));
			squeers.add(new Squeer(4, -1, col));
		}
		if(which == 5) {
			squeers.add(new Squeer(4, 0, col));
			squeers.add(new Squeer(5, 0, col));
			squeers.add(new Squeer(4, -1, col));
			squeers.add(new Squeer(4, -2, col));
		}
		if(which == 6) {
			squeers.add(new Squeer(4, 0, col));
			squeers.add(new Squeer(5, 0, col));
			squeers.add(new Squeer(5, -1, col));
			squeers.add(new Squeer(5, -2, col));
		}
		
	}
	
	public void update() {
		if(Tetris.key.down) speed = 10;
		else speed = 60;
		if(index % speed == 0 && falling) {
			for(int i = 0; i < squeers.size(); i++) {
				if(squeers.get(i).y >= 14 || collision(0)) {
					falling = false;
					break;
				}
			}

			for(int i = 0; i < squeers.size(); i++) {
				if(falling) {
					squeers.get(i).move(this);
				}
			}
		}
		
		//--------------------------------------------------
		if(Tetris.key.left) {
			left = true;
		}
		if(left) {
			boolean valid = true;
			if(!Tetris.key.left && falling) {
				for(int i = 0; i < squeers.size(); i++) {
					if(squeers.get(i).x <= 0 || collision(-1)) {
						valid = false;
					}
				}	
				for(int i = 0; i < squeers.size(); i++) {
					if(valid) {
						squeers.get(i).x--;
					}
				}	
				left = false;
			}
		}
		
		if(Tetris.key.right) {
			right = true;
		}
		if(right) {
			boolean valid = true;
			if(!Tetris.key.right && falling) {
				for(int i = 0; i < squeers.size(); i++) {
					if(squeers.get(i).x >= 9 || collision(1)) {
						valid = false;
					}
				}	
				
				for(int i = 0; i < squeers.size(); i++) {
					if(valid) {
						squeers.get(i).x++;
					}
				}				
				right = false;
			}
		}
		
		if(Tetris.key.up) {
			up = true;
		}
		if(up) {
			if(!Tetris.key.up && falling) {
				changePosition();
				position++;
				if(position == 4) position = 0;
				up = false;
			}
		}
		//--------------------------------------------------		
		/*
		boolean fall = false;
		for(int i = 0; i < squeers.size(); i++) {
			if(squeers.get(i).falling) fall = true;
		}
		if(!fall) falling = false;
		*/
		index++;
		for(int i = 0; i < squeers.size(); i++) {
			if(squeers.get(i).removed) squeers.remove(i);
		}
	}
	
	private void changePosition() {
		if(which == 1) {
			if(position == 0) {
				squeers.get(0).x += 1;
				//squeers.get(1).x 
				squeers.get(2).x -= 1;
				squeers.get(3).x -= 2;
				//squeers.get(0).y
				squeers.get(1).y -= 1;
				squeers.get(2).y -= 2;
				squeers.get(3).y -= 3;
			}
			if(position == 1) {
				if(!collision(-1) && !collision(1) && !collision(2)) {
					squeers.get(0).x -= 1;
					//squeers.get(1).x 
					squeers.get(2).x += 1;
					squeers.get(3).x += 2;
					//squeers.get(0).y
					squeers.get(1).y += 1;
					squeers.get(2).y += 2;
					squeers.get(3).y += 3;
				}else {
					position--;
				}
			}
			if(position == 2) {
				squeers.get(0).x += 2;
				squeers.get(1).x += 1;
				//squeers.get(2).x
				squeers.get(3).x -= 1;
				//squeers.get(0).y
				squeers.get(1).y -= 1;
				squeers.get(2).y -= 2;
				squeers.get(3).y -= 3;				
			}
			if(position == 3) {
				if(!collision(-1) && !collision(1) && !collision(-2)) {
					squeers.get(0).x -= 2;
					squeers.get(1).x -= 1;
					//squeers.get(2).x
					squeers.get(3).x += 1;
					//squeers.get(0).y
					squeers.get(1).y += 1;
					squeers.get(2).y += 2;
					squeers.get(3).y += 3;					
				}else {
					position--;
				}
			}
			
		}
		if(which == 2) {
			if(position == 0) {
				if(!collision(1)) {
					squeers.get(0).x += 1;
					//squeers.get(1).x +=
					//squeers.get(2).x -= 1;
					squeers.get(3).x += 2;
					//squeers.get(0).y +=
					//squeers.get(1).y -= 1;
					//squeers.get(2).y -= 2;
					squeers.get(3).y += 1;
				}else {
					position--;
				}
			}
			if(position == 1) {
				squeers.get(0).x += 1;
				squeers.get(1).x += 1;
				squeers.get(2).x += 1;
				//squeers.get(3).x -= 1;
				//squeers.get(0).y +=
				//squeers.get(1).y += 1;
				//squeers.get(2).y += 2;
				squeers.get(3).y -= 1;				
			}
			if(position == 2) {
				if(!collision(-1)) {
					squeers.get(0).x -= 2;
					//squeers.get(1).x -= 1;
					//squeers.get(2).x +=
					squeers.get(3).x -= 1;
					//squeers.get(0).y +=
					squeers.get(1).y += 1;
					squeers.get(2).y += 1;
					squeers.get(3).y += 1;
				}else {
					position--;
				}
			}
			if(position == 3) {
				//squeers.get(0).x -= 2;
				squeers.get(1).x -= 1;
				squeers.get(2).x -= 1;
				squeers.get(3).x -= 1;
				//squeers.get(0).y +=
				squeers.get(1).y -= 1;
				squeers.get(2).y -= 1;
				squeers.get(3).y -= 1;					
			}
			
		}
		if(which == 3) {
			if(position == 0) {
				squeers.get(0).x += 1;
				squeers.get(1).x -= 1;
				//squeers.get(2).x -= 1;
				squeers.get(3).x -= 2;
				//squeers.get(0).y +=
				squeers.get(1).y -= 1;
				//squeers.get(2).y -= 2;
				squeers.get(3).y -= 1;
			}
			if(position == 1) {
				if(!collision(1)) {
					squeers.get(0).x -= 1;
					squeers.get(1).x += 1;
					//squeers.get(2).x -= 1;
					squeers.get(3).x += 2;
					//squeers.get(0).y +=
					squeers.get(1).y += 1;
					//squeers.get(2).y -= 2;
					squeers.get(3).y += 1;	
				}else {
					position--;
				}
			}
			if(position == 2) {
				squeers.get(0).x += 1;
				squeers.get(1).x -= 1;
				//squeers.get(2).x -= 1;
				squeers.get(3).x -= 2;
				//squeers.get(0).y +=
				squeers.get(1).y -= 1;
				//squeers.get(2).y -= 2;
				squeers.get(3).y -= 1;			
			}
			if(position == 3) {
				if(!collision(1)) {
					squeers.get(0).x -= 1;
					squeers.get(1).x += 1;
					//squeers.get(2).x -= 1;
					squeers.get(3).x += 2;
					//squeers.get(0).y +=
					squeers.get(1).y += 1;
					//squeers.get(2).y -= 2;
					squeers.get(3).y += 1;	
				}else {
					position--;
				}
			}			
		}
		if(which == 4) {
			if(position == 0) {
				squeers.get(0).x -= 1;
				squeers.get(1).x -= 2;
				squeers.get(2).x += 1;
				//squeers.get(3).x -= 2;
				//squeers.get(0).y +=
				squeers.get(1).y -= 1;
				//squeers.get(2).y -= 2;
				squeers.get(3).y -= 1;
			}
			if(position == 1) {
				if(!collision(1)) {
				squeers.get(0).x += 1;
				squeers.get(1).x += 2;
				squeers.get(2).x -= 1;
				//squeers.get(3).x -= 2;
				//squeers.get(0).y +=
				squeers.get(1).y += 1;
				//squeers.get(2).y -= 2;
				squeers.get(3).y += 1;
				}else {
					position--;
				}
			}
			if(position == 2) {
				squeers.get(0).x -= 1;
				squeers.get(1).x -= 2;
				squeers.get(2).x += 1;
				//squeers.get(3).x -= 2;
				//squeers.get(0).y +=
				squeers.get(1).y -= 1;
				//squeers.get(2).y -= 2;
				squeers.get(3).y -= 1;			
			}
			if(position == 3) {
				if(!collision(1)) {
					squeers.get(0).x += 1;
					squeers.get(1).x += 2;
					squeers.get(2).x -= 1;
					//squeers.get(3).x -= 2;
					//squeers.get(0).y +=
					squeers.get(1).y += 1;
					//squeers.get(2).y -= 2;
					squeers.get(3).y += 1;	
				}else {
					position--;
				}
			}			
		}
		if(which == 5) {
			if(position == 0) {
				if(!collision(1)) {
					//squeers.get(0).x -= 1;
					squeers.get(1).x -= 1;
					squeers.get(2).x += 1;
					squeers.get(3).x += 2;
					//squeers.get(0).y +=
					squeers.get(1).y -= 1;
					//squeers.get(2).y -= 2;
					squeers.get(3).y += 1;
				}else {
					position--;
				}
			}
			if(position == 1) {
				squeers.get(0).x += 1;
				squeers.get(1).x += 1;
				//squeers.get(2).x -= 1;
				squeers.get(3).x -= 2;
				//squeers.get(0).y +=
				//squeers.get(1).y += 1;
				squeers.get(2).y -= 1;
				squeers.get(3).y -= 1;		
			}
			if(position == 2) {
				if(!collision(1)) {
					squeers.get(0).x -= 1;
					//squeers.get(1).x -= 2;
					squeers.get(2).x += 1;
					squeers.get(3).x += 2;
					//squeers.get(0).y +=
					squeers.get(1).y += 1;
					squeers.get(2).y += 2;
					squeers.get(3).y += 1;	
				}else {
					position--;
				}
			}
			if(position == 3) {
				//squeers.get(0).x += 1;
				//squeers.get(1).x += 2;
				squeers.get(2).x -= 2;
				squeers.get(3).x -= 2;
				//squeers.get(0).y +=
				//squeers.get(1).y += 1;
				squeers.get(2).y -= 1;
				squeers.get(3).y -= 1;			
			}			
		}
		if(which == 6) {
			if(position == 0) {
				if(!collision(1)) {
					//squeers.get(0).x -= 1;
					//squeers.get(1).x -= 1;
					squeers.get(2).x += 1;
					squeers.get(3).x -= 1;
					//squeers.get(0).y +=
					//squeers.get(1).y -= 1;
					squeers.get(2).y += 1;
					squeers.get(3).y += 1;
				}else {
					position--;
				}
			}
			if(position == 1) {
				//squeers.get(0).x += 1;
				squeers.get(1).x -= 1;
				squeers.get(2).x -= 2;
				squeers.get(3).x += 1;
				//squeers.get(0).y +=
				squeers.get(1).y -= 1;
				squeers.get(2).y -= 2;
				squeers.get(3).y -= 1;		
			}
			if(position == 2) {
				if(!collision(1)) {
					squeers.get(0).x += 2;
					//squeers.get(1).x -= 2;
					squeers.get(2).x += 1;
					squeers.get(3).x += 1;
					//squeers.get(0).y +=
					//squeers.get(1).y += 1;
					squeers.get(2).y += 1;
					squeers.get(3).y += 1;			
				}else {
					position--;
				}
			}
			if(position == 3) {
				squeers.get(0).x -= 2;
				squeers.get(1).x += 1;
				//squeers.get(2).x -= 2;
				squeers.get(3).x -= 1;
				//squeers.get(0).y +=
				squeers.get(1).y += 1;
				//squeers.get(2).y -= 1;
				squeers.get(3).y -= 1;			
			}			
		}
		
		
	}

	
	private boolean collision( int dir) {
		if(!falling) return true;
		//dir 0 - updown
		if(dir == 0) {
			for(int a = 0; a < squeers.size(); a++) {
				for(int i = 0; i < Tetris.objects.size() - 1; i++) {
					Object o = Tetris.objects.get(i);
					for(int j = 0; j < o.squeers.size(); j++) {
						Squeer s = o.squeers.get(j);
						if(squeers.get(a).x == s.x && squeers.get(a).y + 1 == s.y) {
							return true;
						}
						
					}		
					
				}	
			}
		}else {
			for(int a = 0; a < squeers.size(); a++) {
				for(int i = 0; i < Tetris.objects.size() - 1; i++) {
					Object o = Tetris.objects.get(i);
					for(int j = 0; j < o.squeers.size(); j++) {
						Squeer s = o.squeers.get(j);
						if(squeers.get(a).x + dir == s.x && squeers.get(a).y == s.y) {
							return true;
						}
						
					}		
					
				}	
			}
		}
		return false;
	}

	public void render(Graphics g) {
		for(int i = 0; i < squeers.size(); i++) {
			g.fillRect(squeers.get(i).x * 40, squeers.get(i).y * 40, 40, 40);
		}		
	}

}













