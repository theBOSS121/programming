package com.theBOSS.shooter_pc.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Server {

	public int port;
	public Thread listenThread;
	public boolean listening = false;
	private DatagramSocket socket;
	byte[] receievedDataBuffer = new byte[1024];
	
//	public List<Client> clients = new ArrayList<Client>();
	
	public Server(int port) {
		this.port = port;
	}
	
	public void start() {
		try {
			socket = new DatagramSocket(port);
			System.out.println("Server started on " + InetAddress.getLocalHost() + "on port " + port);
			listening = true;
			listenThread = new Thread(()->listen(), "listen");
			listenThread.start();
			System.out.println("Server is listening...");
		}catch(SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

	private void listen() {
		while(listening) {
			try {
				DatagramPacket packet = new DatagramPacket(receievedDataBuffer, receievedDataBuffer.length);	
				socket.receive(packet);
				process(packet);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}		
	}

	private void process(DatagramPacket packet) {
		System.out.println(new String(packet.getData()));
		send("Hi client!".getBytes(), packet.getAddress(), packet.getPort());
	}
	
	public void send(byte[] data, InetAddress address, int port) {
		try {
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			socket.send(packet);
		}catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server sended a packet");
	}
	
}
