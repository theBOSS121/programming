package com.theBOSS.PlatformerServer;

import java.net.InetAddress;

public class Client {

	public InetAddress address;
	public int port;
	
	int x, y;
	
	public Client(InetAddress address, int port) {
		this.address = address;
		this.port = port;
		x = 100;
		y = 100;
	}

}
