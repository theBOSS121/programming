package com.theBOSS.shooter_pc.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
	
	public boolean listening = false;
	public Thread listenThread;
	public String address;
	public static int port;
	public static InetAddress serverAddress;
	public static DatagramSocket socket;
	byte[] receivedDataBuffer = new byte[1024];
	
	public Client(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	public void connect() {
		try {
			serverAddress = InetAddress.getByName(address);
			socket = new DatagramSocket();
			//conection packet
			String message = "Hi server!";
			send(message.getBytes());
			listening = true;
			listenThread = new Thread(()->listen(), "listen");
			listenThread.start();
		}catch(UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		
	}

	private void listen() {
		while(listening) {
			try {
				DatagramPacket packet = new DatagramPacket(receivedDataBuffer, receivedDataBuffer.length);
				socket.receive(packet);
				process(packet);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void process(DatagramPacket packet) {
		System.out.println(new String(packet.getData()));
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
