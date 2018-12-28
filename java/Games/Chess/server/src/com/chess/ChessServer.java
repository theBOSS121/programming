package com.chess;

public class ChessServer {
	public static void main(String[] args) {
		Server server = new Server(8192);
		server.start();
	}
}
