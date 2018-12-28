package com.loginServer;

public class LoginServer {

	public static void main(String[] args) {
		Server server = new Server(8192);
		server.start();
	}
	
}
