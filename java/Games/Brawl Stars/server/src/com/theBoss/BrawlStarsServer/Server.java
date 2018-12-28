package com.theBoss.BrawlStarsServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {
		
	private int port;
	private Thread listenThread;
	private boolean listening;
	private DatagramSocket socket;
	private byte[] recievedDataBuffer = new byte[1024];
	
	private List<ClientsData> clients = new ArrayList<ClientsData>();
	
	public Server(int port) {
		this.port = port;
	}
	
	private void start() {
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		listening = true;
		listenThread = new Thread(() -> listen(), "listen");
		listenThread.start();
				
	}

	private void listen() {
		while(listening) {
			DatagramPacket packet = new DatagramPacket(recievedDataBuffer, 1024);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			process(packet);
		}
	}

	private void process(DatagramPacket packet) {
		String string = new String(packet.getData());
		if(string.startsWith("/connect/")) {
			clients.add(new ClientsData(packet.getAddress(), packet.getPort()));
			if(clients.size() % 2 == 0 && clients.size() != 0) {
				String s = "/c/" + clients.get(clients.size() - 1).userID + "/e/";
				send(s.getBytes(), clients.get(clients.size() - 2).address, clients.get(clients.size() - 2).port);
				s = "/c/" + clients.get(clients.size() - 2).userID + "/e/";
				send(s.getBytes(), clients.get(clients.size() - 1).address, clients.get(clients.size() - 1).port);
			}
		}
		if(string.startsWith("/s/")) {
			int x, y;
			double angle;
			x = Integer.parseInt(string.split("/id/|/x/")[1]);
			y = Integer.parseInt(string.split("/x/|/y/")[1]);
			angle = Double.parseDouble(string.split("/y/|/e/")[1]);
			for(int i = 0; i < clients.size(); i++) {
				if(packet.getAddress() == clients.get(i).address && packet.getPort() == clients.get(i).port) {
					clients.get(i).setPositionAndAngle(x, y, angle);
				}
			}
			for(int i = 0; i < clients.size(); i++) {
				if(Integer.parseInt(string.split("/s/|/id/")[1]) == clients.get(i).userID) {
					String s = "/s/" + x + "/x/" + y + "/y/" + angle + "/e/";
					send(s.getBytes(), clients.get(i).address, clients.get(i).port);
				}
			}
		}
		if(string.startsWith("/b/")) {
			double x = Double.parseDouble(string.split("/id/|/x/")[1]);
			double y = Double.parseDouble(string.split("/x/|/y/")[1]);
			double xd = Double.parseDouble(string.split("/y/|/xd/")[1]);
			double yd = Double.parseDouble(string.split("/xd/|/e/")[1]);
			for(int i = 0; i < clients.size(); i++) {
				if(Integer.parseInt(string.split("/b/|/id/")[1]) == clients.get(i).userID) {
					String s = "/b/" + x + "/x/" + y + "/y/" + xd + "/xd/" + yd + "/e/";
					send(s.getBytes(), clients.get(i).address, clients.get(i).port);
				}
			}
		}
		
	}

	private void send(byte[] data, InetAddress address, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server(8192);
		server.start();
	}

}






