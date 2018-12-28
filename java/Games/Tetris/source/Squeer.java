
public class Squeer {
	public int x;
	public int y;
	public int col;
	public boolean removed = false;
	
	public Squeer(int x, int y, int col) {
		this.x = x;
		this.y = y;
		this.col = col;
	}
	
	public void move(Object o){
		if(o.falling) {
			this.y++;
		}		
	}

}
