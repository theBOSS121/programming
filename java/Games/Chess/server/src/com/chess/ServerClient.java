package com.chess;

import java.net.InetAddress;

public class ServerClient{

	public int userID;
	public InetAddress address;
	public int port;
	public int x, y;
	public int sprite;
	public boolean status = false;
	public int bomb;
	
	private static int userIDcounter = 1;
	
	public ServerClient(InetAddress address, int port){
		userID = userIDcounter++;
		this.address = address;
		this.port = port;
		status = true;
	}
	
	public int getUserID(){
		return userID;
	}
	
}
