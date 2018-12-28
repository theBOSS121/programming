package com.loginServer;

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
	private boolean listening = false;
	private boolean wrong = true;
	private DatagramSocket socket;
	
	private final int MAX_PACKET_SIZE = 1024;
	private byte[] recivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];
	Configuration config = new Configuration();
	
	public static List<ServerClient> clients = new ArrayList<ServerClient>();
	
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
		
		System.out.println("Server started on port " + port);
		config.loadConfiguration("config.xml");
		
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
			packet = null;
		}
	}
	
	
	private void process(DatagramPacket packet){
		//byte[] data = packet.getData();
		String string = new String(packet.getData());
		string = string.split("/end/")[0];
		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		if(string.startsWith("/ping/")) {
			send("/ping/".getBytes(), address, port);
		}else if(string.startsWith("/r/")) {
			clients.add(new ServerClient(address, port));
			clients.get(clients.size() - 1).username = string.split("/u/|/p/")[1];
			clients.get(clients.size() - 1).password = string.split("/p/|/m/")[1];
			clients.get(clients.size() - 1).mail = string.split("/m/|/a/")[1];
			clients.get(clients.size() - 1).age = string.split("/a/|/c/")[1];
			clients.get(clients.size() - 1).country = string.split("/c/|/s/")[1];
			clients.get(clients.size() - 1).sex = string.split("/s/|/pn/")[1];
			clients.get(clients.size() - 1).phoneNum = string.split("/pn/|/er/")[1];
			
			int id = clients.get(clients.size() - 1).userID;
			
			save();
			
			send(getBynaryFtomInt(id), clients.get(clients.size() - 1).address, clients.get(clients.size() - 1).port);
			
		}else if(string.startsWith("/e/")) {
			int ID = Integer.parseInt(string.split("/id/|/u/")[1]);
			for(int i = 0; i < clients.size(); i++) {
				ServerClient c = clients.get(i);
				if(ID == c.userID) {
					c.username = string.split("/u/|/p/")[1];
					c.password = string.split("/p/|/m/")[1];
					c.mail = string.split("/m/|/a/")[1];
					c.age = string.split("/a/|/c/")[1];
					c.country = string.split("/c/|/s/")[1];
					c.sex = string.split("/s/|/pn/")[1];
					c.phoneNum = string.split("/pn/|/ee/")[1];
				}
			}
			
			save();
			
		}else if(string.startsWith("/d/")) {
			int ID = Integer.parseInt(string.split("/id/|/u/")[1]);
			for(int i = 0; i < clients.size(); i++) {
				if(ID == clients.get(i).userID) {
					remove(i);
					clients.remove(i);
				}
			}
			save();
		}else if(string.startsWith("/l/")) {
			for(int i = 0; i < clients.size(); i++) {
				ServerClient c = clients.get(i);
				if(c.username.equals(string.split("/u/|/p/")[1]) && c.password.equals(string.split("/p/|/el/")[1])) {
					c.address = packet.getAddress();
					c.port = packet.getPort();
					
					String information = "/i/" + "/id/" + c.userID +  "/u/" + c.username  + "/p/" + c.password + "/m/" + c.mail + "/a/" + c.age + "/c/" + c.country + "/s/" + c.sex + "/pn/" + c.phoneNum + "/ei/" + "/" + "/end/";
					send(information.getBytes(), c.address, c.port);
					
					wrong = false;
				}
			}
			if(wrong) {
				String information = "/w/" + "/end/";
				send(information.getBytes(), packet.getAddress(), packet.getPort());
			}else {
				wrong = true;
			}
		}/*else if(string.startsWith("/photo/")) {
			for(int i = 0; i < clients.size(); i++) {
				ServerClient c = clients.get(i);
				if(Integer.parseInt(string.split("/photo/|//")[1]) == c.userID) {
					c.pixels = string.split("//|/e/")[1].getBytes();
				}
			}
			save();
		}*/
		
			
	}
	
	private void remove(int n) {
		for(int i = 0; i < clients.size(); i++) {
			if(i == n) {
				config.remove(Integer.toString(i) + "address");
				config.remove(Integer.toString(i) + "port");
				config.remove(Integer.toString(i) + "userID");
				config.remove(Integer.toString(i) + "username");
				config.remove(Integer.toString(i) + "password");
				config.remove(Integer.toString(i) + "mail");
				config.remove(Integer.toString(i) + "age");
				config.remove(Integer.toString(i) + "country");
				config.remove(Integer.toString(i) + "sex");
				config.remove(Integer.toString(i) + "phone");
			}
		}
	}

	private void save() {
		config.saveConfiguration("numberOfClients", clients.size());
		for(int i = 0; i < clients.size(); i++) {
			ServerClient c = clients.get(i);
			config.saveConfiguration(Integer.toString(i) + "address", c.address.getHostAddress());
			config.saveConfiguration(Integer.toString(i) + "port", c.port);
			config.saveConfiguration(Integer.toString(i) + "userID", c.userID);
			config.saveConfiguration(Integer.toString(i) + "username", c.username);
			config.saveConfiguration(Integer.toString(i) + "password", c.password);
			config.saveConfiguration(Integer.toString(i) + "mail", c.mail);
			config.saveConfiguration(Integer.toString(i) + "age", c.age);
			config.saveConfiguration(Integer.toString(i) + "country", c.country);
			config.saveConfiguration(Integer.toString(i) + "sex", c.sex);
			config.saveConfiguration(Integer.toString(i) + "phone", c.phoneNum);
		}
	}
	/*
	private void sendAllToAll() {
		int counter = 0;
		for(int i = 0; i < clients.size(); i++){
			byte[] data = new byte[MAX_PACKET_SIZE];
			BinaryWriter writer = new BinaryWriter();
			byte[] add = "NEW".getBytes();
			writer.write(add);
			writer.write(clients.size() - 1);
			for(int j = 0; j < clients.size(); j++){
				
			}
			data = writer.getBuffer();
			send(data, clients.get(i).address, clients.get(i).port);	
			counter++;
		}
	}*/
	

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






