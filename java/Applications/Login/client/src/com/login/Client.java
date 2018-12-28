package com.login;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;


public class Client {
	
	private boolean listening = false;
	private Thread listenThread;
	
	private String ipAddress;
	private static int port;
	private int userID;
	public boolean connected = false;
	
	private static InetAddress serverAddress;
	
	private static DatagramSocket socket;
	
	private final int MAX_PACKET_SIZE = 1024;
	private byte[] recivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];
	
	public Client(String host){
		String[] parts = host.split(":");
		ipAddress = parts[0];
		try{
			port = Integer.parseInt(parts[1]);
		}catch(NumberFormatException e){
			return;
		}
	}
	
	public Client(String host, int port){
		ipAddress = host;
		Client.port = port;
	}
	
	private void listen() {
		while(listening){
			DatagramPacket packet = new DatagramPacket(recivedDataBuffer, MAX_PACKET_SIZE);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			process(packet);
		}	
	}
	
	private void process(DatagramPacket packet){
		byte[] data = packet.getData();
		String string = new String(packet.getData());
		string = string.split("/end/")[0];		

		if(!connected) {
			if(string.startsWith("/ping/")) {
				System.out.println("server is working");
				connected = true;
			}
			
		}else if(string.startsWith("/w/")){
			new Login(1);
		}else if(string.startsWith("/i/")){
			int userID = Integer.parseInt(string.split("/id/|/u/")[1]);
			String username = string.split("/u/|/p/")[1];
			String password = string.split("/p/|/m/")[1];
			String mail = string.split("/m/|/a/")[1];
			String age = string.split("/a/|/c/")[1];
			String country = string.split("/c/|/s/")[1];
			String sex = string.split("/s/|/pn/")[1];
			String phoneNum = string.split("/pn/|/ei/")[1];
			
			new Profile(userID, username, password, mail, age, country, sex, phoneNum);
		}else {
			userID = ByteBuffer.wrap(data, 0, 4).getInt();
			System.out.println("userid is " + userID);
		}
		
	}

	public void connect(){
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
		
		sendConnectionPacket();
		
		listening = true;
		
		listenThread = new Thread(() -> listen(), "listen");
		listenThread.start();
	}
	
	private void sendConnectionPacket() {
		BinaryWriter writer = new BinaryWriter();
		writer.write("/ping//end/".getBytes());
		send(writer.getBuffer());
	}
	
	public int getUserID() {
		return userID;
	}

	public static void send(byte[] data){
		assert(socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}




