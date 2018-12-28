package com.theBoss.BrawlStarsServer;

import java.net.InetAddress;

public class ClientsData {

	public int clientsX, clientsY;
	public double clientsAngle;
	public InetAddress address;
	public int port;
	private static int counter = 0;
	public int userID;
	
	public ClientsData(InetAddress address, int port) {
		this.address = address;
		this.port = port;
		userID = counter;
		counter++;
	}
	
	public void setPositionAndAngle(int clientsX, int clientsY, double clientsAngle){
		this.clientsX = clientsX;
		this.clientsY = clientsY;
		this.clientsAngle = clientsAngle;
	}
	
}
