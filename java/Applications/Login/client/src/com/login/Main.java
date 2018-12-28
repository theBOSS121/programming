package com.login;


public class Main {

	public static Client client;
	
	public static void main(String[] args) {
		new Launcher();
		//localhost should be ip of the server
		client = new Client("localhost", 8192);
		client.connect();
	}
	
}
