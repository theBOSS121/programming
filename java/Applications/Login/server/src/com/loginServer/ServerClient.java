package com.loginServer;

import java.net.InetAddress;

public class ServerClient{

	public int userID;
	public InetAddress address;
	public int port;
	public String username;
	public String password;
	public String mail;
	public String age;
	public String country;
	public String sex;
	public String phoneNum;
	public byte[] pixels;
	
	private static int userIDcounter = 1;
	
	public ServerClient(InetAddress address, int port){
		userID = userIDcounter++;
		this.address = address;
		this.port = port;
	}
	
	public int getUserID(){
		return userID;
	}
	
}
