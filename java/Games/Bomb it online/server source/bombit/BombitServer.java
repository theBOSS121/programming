package com.bombit;

public class BombitServer {

	public static void main(String[] args) {
		Server server = new Server(8192);
		server.start();
	}

}
