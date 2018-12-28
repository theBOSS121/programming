
public class Ball {
	public int player = 0;
	public int x, y;
	public boolean falling;

	public Ball(int x, int y, boolean falling, int player) {
		this.x = x;
		this.y = y;
		this.falling = falling;
		this.player = player;
	}
	
	public void update() {
		y += 20;
		if(y >= 510) {
			y = 510;
			falling = false;
		}
		if(collision()) {
			falling = false;
		}		
	}

	private boolean collision() {
		for(int i = 0; i < ConnectFour.balls.size() - 1; i++) {
			Ball b = ConnectFour.balls.get(i);
			if(x == b.x && y + 90 > b.y) {
				y = b.y - 100;
				return true;
			}
		}
		return false;
	}
	
}
