package com.theBOSS.Platformer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	
	private boolean listening = false;
	private Thread listenThread;
	private String ipAddress;
	private static InetAddress serverAddress;
	private static int port;
	private static DatagramSocket socket;
	private byte[] receivedDataBuffer = new byte[1024];	
	
	public Client(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public void connect() {
		try {
			serverAddress = InetAddress.getByName(ipAddress);
			socket = new DatagramSocket();
			//send connection packet
			send("connect".getBytes());
			//listeningStuff
			listening = true;
			listenThread = new Thread(() -> listen());
			listenThread.start();
		}catch(UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
	}

	private void listen() {
		while(listening) {
			try {
				DatagramPacket packet = new DatagramPacket(receivedDataBuffer, 1024);
				socket.receive(packet);
				process(packet);
			}catch(IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	private void process(DatagramPacket packet) {
		byte[] data = packet.getData();
		String s = new String(packet.getData());
		if(s.startsWith("update")) {
			Platformer.enemy.x = Integer.parseInt(s.split("/x/|/y/")[1]);
			Platformer.enemy.y = Integer.parseInt(s.split("/y/|/e/")[1]);
		}
	}


	public static void send(byte[] data) {
		try {
			DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
			socket.send(packet);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
