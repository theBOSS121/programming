package com.theBOSS.retroshooter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;

public class ClientThread extends Thread{

	Handler mHandler;
	Context mContext;
	static DatagramSocket socket;
	boolean socketOK = true;
	public static int port = 1111;
	
	public static boolean connected = false;
	
	public static int id = -1;
	
	public static List<Client> opponents = new ArrayList<Client>();
	
	public ClientThread(Context applicationContext, Handler handler) {
		mContext = applicationContext;
		mHandler = handler;
	}

	public void init() {
		try {
			socket = new DatagramSocket();
			sendConnectionPacket();
		}catch (Exception e) {}
	}
	
	
	public void run() {
		byte[] receiveData = new byte[1024];
		while(socketOK) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				socket.receive(receivePacket);
				process(receivePacket);
			}catch(Exception e) {
				socketOK = false;
			}
		}	
	}
	
	private void process(DatagramPacket packet) {
		String s = new String(packet.getData());
		mHandler.obtainMessage(RetroShooter.PACKET_CAME, s).sendToTarget();
		
		if(s.startsWith("/c/")) {
			id = Integer.valueOf(s.split("/c/|/e/")[1]);
			mHandler.obtainMessage(RetroShooter.IS_RUNNING, "connected " + id).sendToTarget();
			connected = true;
		}else if(s.startsWith("/u/")) {
			int id = Integer.valueOf(s.split("/u/|/x/")[1]);
			boolean exist = false;
			for(int i = 0; i < opponents.size(); i++) {
				if(opponents.get(i).id == id) {
					exist = true;
					opponents.get(i).x = Integer.valueOf(s.split("/x/|/y/")[1]);
					opponents.get(i).y = Integer.valueOf(s.split("/y/|/a/")[1]);
					opponents.get(i).angle = Double.valueOf(s.split("/a/|/e/")[1]);
				}
			}
			if(!exist) {
				opponents.add(new Client(packet.getAddress(), packet.getPort()));
				opponents.get(opponents.size() - 1).id = id;
				for(int i = 0; i < opponents.size(); i++) {
					if(opponents.get(i).id == id) {
						opponents.get(i).x = Integer.valueOf(s.split("/x/|/y/")[1]);
						opponents.get(i).y = Integer.valueOf(s.split("/y/|/a/")[1]);
						opponents.get(i).angle = Double.valueOf(s.split("/a/|/e/")[1]);
					}
				}
			}			
		}else if(s.startsWith("/b/")) {
			int id = Integer.valueOf(s.split("/b/|/x/")[1]);
			for(int i = 0; i < opponents.size(); i++) {
				if(opponents.get(i).id == id) {
					opponents.get(i).bullets.add(new Bullet(Double.valueOf(s.split("/x/|/y/")[1]), Double.valueOf(s.split("/y/|/a/")[1]), 1.2, null, Double.valueOf(s.split("/a/|/e/")[1])));
				}
			}
		}
	}


	public static void send(byte[] data, InetAddress serverAddress) {
		try {			
			DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
			if(socket != null) {
				socket.send(packet);
			}else {
				try {
					socket = new DatagramSocket();
					socket.send(packet);					
				}catch (Exception e) {}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		socket.close();
	}

	public static void sendConnectionPacket() {
		try {
			send("/c/".getBytes(), InetAddress.getByName(RetroShooter.ipAddress));
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}
	}
}