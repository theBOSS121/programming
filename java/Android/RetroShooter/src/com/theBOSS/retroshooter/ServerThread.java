package com.theBOSS.retroshooter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager.ServiceResponseListener;
import android.os.Handler;
import android.util.Log;

public class ServerThread extends Thread{
	
	Handler mHandler;
	Context mContext;
	static DatagramSocket serverSocket;
	
	public static List<Client> clients = new ArrayList<Client>();
	
	public ServerThread(Context currentContext, Handler handler) {
		mContext = currentContext;
		mHandler = handler;
	}
	
	public void init() {
		int port = 1111;
		try {
			serverSocket = new DatagramSocket(port);
			mHandler.obtainMessage(RetroShooter.IS_RUNNING, "" + port).sendToTarget();
		}catch(SocketException e) {
			e.printStackTrace();
		}
		InetAddress myIP = getMyWiFiIPAddress();
		mHandler.obtainMessage(RetroShooter.IP_ADDRESS, myIP).sendToTarget();
	}
	
	private InetAddress getMyWiFiIPAddress() {
		WifiManager mWifi = (WifiManager) (mContext.getSystemService(Context.WIFI_SERVICE));
		WifiInfo info = mWifi.getConnectionInfo();
		DhcpInfo dhcp = mWifi.getDhcpInfo();
		int myIntegerIpAddress = dhcp.ipAddress;
		
		byte[] quads = new byte[4];
		for(int i = 0; i < 4; i++) {
			quads[i] = (byte) ((myIntegerIpAddress >> i * 8) & 0xff);
		}
		
		try {
			InetAddress myIpAddress = InetAddress.getByAddress(quads);
			return myIpAddress;
		}catch(Exception e) {
			return null;
		}		
	}

	public void closeSocket() {
		serverSocket.close();
	}
	
	public void run() {
		boolean socketOK = true;
		byte[] receiveData;
		while(socketOK) {
			receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				serverSocket.receive(receivePacket);
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
			boolean exists = false;
			for(int i = 0; i < clients.size(); i++) {
				if(packet.getAddress() == clients.get(i).address && packet.getPort() == clients.get(i).port) {
					exists = true;
				}
			}
			if(!exists) {
				clients.add(new Client(packet.getAddress(), packet.getPort()));
				String data = "/c/" + clients.get(clients.size() - 1).id + "/e/";
				send(data.getBytes(), packet.getAddress(), packet.getPort());
			}
		}else if(s.startsWith("/u/")) {
			int id = Integer.valueOf(s.split("/u/|/x/")[1]);
			for(int i = 0; i < clients.size(); i++) {
				if(clients.get(i).id == id) {
					clients.get(i).x = Integer.valueOf(s.split("/x/|/y/")[1]);
					clients.get(i).y = Integer.valueOf(s.split("/y/|/a/")[1]);
					clients.get(i).angle = Double.valueOf(s.split("/a/|/e/")[1]);
				}
			}
		}else if(s.startsWith("/b/")) {
			int id = Integer.valueOf(s.split("/b/|/x/")[1]);
			for(int i = 0; i < clients.size(); i++) {
				if(clients.get(i).id == id) {
					double x = Double.valueOf(s.split("/x/|/y/")[1]);
					double y = Double.valueOf(s.split("/y/|/a/")[1]);
					double angle = Double.valueOf(s.split("/a/|/e/")[1]);
					clients.get(i).bullets.add(new Bullet(x, y, 1.2, null, angle));
					sendBulletInfoToAll(x, y, angle, i);
				}
			}
		}
	}

	private void sendBulletInfoToAll(double x, double y, double angle, int j) {
		for(int i = 0; i < clients.size(); i++) {
			if(i == j) continue;
			String data = "/b/" + clients.get(j).id + "/x/" + (int) x + "/y/" + (int) y + "/a/" + angle + "/e/";
			send(data.getBytes(), clients.get(i).address, clients.get(i).port);
		}
	}

	public static void send(byte[] data, InetAddress address, int port) {
		try {			
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			serverSocket.send(packet);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendAllToAll() {
		for(int i = 0; i < clients.size(); i++) {
			for(int j = 0; j < clients.size(); j++) {
				if(i == j) continue;
				if(clients.get(i).life > 0) {
					String data = "/u/" + clients.get(i).id + "/x/" + (int) clients.get(i).x + "/y/" + (int) clients.get(i).y + "/a/" + clients.get(i).angle + "/e/";
					send(data.getBytes(), clients.get(j).address, clients.get(j).port);
				}
			}
		}
	}
	
}
