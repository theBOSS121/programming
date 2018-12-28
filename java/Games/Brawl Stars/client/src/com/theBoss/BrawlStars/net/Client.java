package com.theBoss.BrawlStars.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.theBoss.BrawlStars.BrawlStars;
import com.theBoss.BrawlStars.entity.Bullet;
import com.theBoss.BrawlStars.level.Level;

public class Client {

	private boolean listening = false;
	public boolean connected = false;
	public int opponentsID;
	private Thread listenThread;
	private String ipAddress;
	private int port;
	private InetAddress serverAddress;
	private DatagramSocket socket;
	private byte[] receivedDataBuffer = new byte[1024];
	public Opponent opponent;
	public Level level;
	
	public Client(String ipAddress, int port, Level level) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.level = level;
	}
	
	public void connect() {
		try {
			serverAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		send("/connect/".getBytes());
		
		listening = true;
		listenThread = new Thread(() -> listen(), "listen");
		listenThread.start();	
	}

	private void listen() {
		while(listening) {
			DatagramPacket packet = new DatagramPacket(receivedDataBuffer, 1024);
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
		string = string.split("/e/")[0];
		if(!connected) {
			if(string.startsWith("/c/")) {
				opponentsID = Integer.parseInt(string.split("/c/")[1]);
				opponent = new Opponent(level);
				if(opponentsID % 2 == 0) {
					BrawlStars.player.y = 32 * 18 - 16;
					BrawlStars.player.startX = 32 * 4 + 16;
					BrawlStars.player.startY = 32 * 18 - 16;
					BrawlStars.client.opponent.startX = 32 * 4 + 16;
					BrawlStars.client.opponent.startY = 32 + 16;
				}else {
					BrawlStars.player.startX = 32 * 4 + 16;
					BrawlStars.player.startY = 32 + 16;
					BrawlStars.client.opponent.startX = 32 * 4 + 16;
					BrawlStars.client.opponent.startY = 32 * 18 - 16;
				}
				connected = true;
				BrawlStars.started = true;
			}
		}
		if(connected) {
			if(string.startsWith("/s/")) {
				int x = Integer.parseInt(string.split("/s/|/x/")[1]);
				int y = Integer.parseInt(string.split("/x/|/y/")[1]);
				double angle = Double.parseDouble(string.split("/y/|/e/")[1]);
				opponent.setPositionAndAngle(x, y, angle);
			}
			if(string.startsWith("/b/")) {
				opponent.bullets.add(new Bullet(Double.parseDouble(string.split("/b/|/x/")[1]), Double.parseDouble(string.split("/x/|/y/")[1]),
						Double.parseDouble(string.split("/y/|/xd/")[1]), Double.parseDouble(string.split("/xd/|/e/")[1]), 5.0, 6, 6, level));
			}
		}	
	}
	
	public void send(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}






