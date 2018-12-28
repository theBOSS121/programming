package theBOSS.PongUDPServer;

import java.net.InetAddress;

public class Client {

	public InetAddress address;
	public int port;
	
	public int x;
	
	public Client(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}
	
}
