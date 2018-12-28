package com.theBOSS.PlatformerServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private int port;
	private Thread listenThread;
	private boolean listening = false;
	private DatagramSocket socket;
	private byte[] recievedDataBuffer = new byte[1024];
	private List<Client> clients = new ArrayList<Client>(); 
	static Server server;
	
	public Server(int port) {
		this.port = port;
	}


	private void start() {
		try {
			socket = new DatagramSocket(port);
			System.out.println("PlatformerServer started on " + InetAddress.getLocalHost().getHostAddress() + " and port " + port);
			listening = true;
			listenThread = new Thread(() -> listen(), "listen");
			listenThread.start();
			loop();
		}catch(SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
	}


	private void loop() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		int updates = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				sendUpdatesToAllClients();
				updates++;
				delta--;
			}
		}
	}


	private void sendUpdatesToAllClients() {
		if(clients.size() < 2 || clients.size() > 2) return;
		String data = "update" + "/x/" + clients.get(1).x + "/y/" + clients.get(1).y + "/e/";
		send(data.getBytes(), clients.get(0).address, clients.get(0).port);
		data = "update" + "/x/" + clients.get(0).x + "/y/" + clients.get(0).y + "/e/";
		send(data.getBytes(), clients.get(1).address, clients.get(1).port);
	}


	private void listen() {
		while(listening) {
			try {
				DatagramPacket packet = new DatagramPacket(recievedDataBuffer, 1024);
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
		if(s.startsWith("connect")) {
			clients.add(new Client(packet.getAddress(), packet.getPort()));
		}
		if(s.startsWith("update")) {
			for(int i = 0; i < clients.size(); i++) {
				if(packet.getAddress().equals(clients.get(i).address) && packet.getPort() == clients.get(i).port) {
					clients.get(i).x = Integer.parseInt(s.split("/x/|/y/")[1]);
					clients.get(i).y = Integer.parseInt(s.split("/y/|/e/")[1]);
				}
			}
		}
	}
	
	private void send(byte[] data, InetAddress address, int port) {
		try {
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			socket.send(packet);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		server = new Server(1111);
		server.start();
	}

}
