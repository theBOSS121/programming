package com.chess;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class Server {
	
	private int port;
	private Thread listenThread;
	private boolean listening = false;
	private DatagramSocket socket;
	//private boolean started = false;
	
	private final int MAX_PACKET_SIZE = 1024;
	private byte[] recivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];
	
	private List<ServerClient> clients = new ArrayList<ServerClient>();
	
	public Server(int port){
		this.port = port;
	}
	
	public void start(){
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("Server started on port 8192");
		
		listening = true;
		
		listenThread = new Thread(() -> listen(), "listen");
		listenThread.start();
		System.out.println("Server is listening...");
	}
	
	private void listen(){
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
		//InetAddress address = packet.getAddress();
		//int port = packet.getPort();
		String string = new String(packet.getData());
		
		if (data[0] == 0x40 && data[1] == 0x40){
			switch(data[2]){
				case 0x01:
					clients.add(new ServerClient(packet.getAddress(), packet.getPort()));
					
					byte[] send = new byte[128];
					byte[] userID = getBynaryFtomInt(clients.get(clients.size() - 1).userID);
					
					for(int i = 0; i < 4; i++){
						send[i] = userID[i];
					}
					
					send(send , clients.get(clients.size() - 1).address, clients.get(clients.size() - 1).port);	
					
					if(clients.size() % 2 == 0 && clients.size() !=0) {
						send(getBynaryFtomInt(clients.get(clients.size() - 1).userID), clients.get(clients.size() - 2).address, clients.get(clients.size() - 2).port);
						send(getBynaryFtomInt(clients.get(clients.size() - 2).userID), clients.get(clients.size() - 1).address, clients.get(clients.size() - 1).port);
					}
					
			}
		}if(string.startsWith("/moved/")) {
			for(int i = 0; i < clients.size(); i++) {
				if(clients.get(i).userID == ByteBuffer.wrap(data, 7, 4).getInt()) {
					send("/moved/".getBytes(), clients.get(i).address, clients.get(i).port);
				}
			}
		}else{
			for(int i = 0; i < clients.size(); i++){
				if(clients.get(i).userID == ByteBuffer.wrap(data, 0, 4).getInt()){
					int lastX = ByteBuffer.wrap(data, 4, 4).getInt();
					int lastY = ByteBuffer.wrap(data, 8, 4).getInt();
					int x = ByteBuffer.wrap(data, 12, 4).getInt();
					int y = ByteBuffer.wrap(data, 16, 4).getInt();
					BinaryWriter writer = new BinaryWriter();
					writer.write(lastX);
					writer.write(lastY);
					writer.write(x);
					writer.write(y);
					send(writer.getBuffer(), clients.get(i).address, clients.get(i).port);
				}
			}
		}
	}
	
	private byte[] getBynaryFtomInt(int num) {
		byte[] data = new byte[4];
		data[0] = (byte)((num >> 24) & 0xff);
		data[1] = (byte)((num >> 16) & 0xff);
		data[2] = (byte)((num >> 8) & 0xff);
		data[3] = (byte)((num >> 0) & 0xff);
		return data;
	}

	public void send(byte[] data, InetAddress address, int port){
		assert(socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
