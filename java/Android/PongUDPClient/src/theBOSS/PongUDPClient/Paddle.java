package theBOSS.PongUDPClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Paddle {
	
	public int x, y, width, height;
	public Sprite s;	
	private Screen screen;
	
	public Paddle(int x, int y, Sprite s, Screen screen) {
		this.x = x;
		this.y = y;
		this.s = s;
		width = s.width;
		height = s.height;
		this.screen = screen;
	}
	
	public void update() {
		if(Pong.x != -1) {
			if(Pong.x < Renderer.screenWidth / 2) {
				if(x > 0) x--;
			}else {
				if(x + width < Renderer.WIDTH) x++;
			}
		}
		String s = "/x/" + Integer.toString(x) + "/e/";
		try {
			if(!Pong.ipAddress.equals("0.0.0.0"))
			ClientThread.send(s.getBytes(), InetAddress.getByName(Pong.ipAddress));
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void render() {
		screen.renderSprite(x, y, s);
	}

}
