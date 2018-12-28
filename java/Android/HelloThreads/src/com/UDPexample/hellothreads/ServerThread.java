package com.UDPexample.hellothreads;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

public class ServerThread extends Thread{
	
	Handler mHandler;
	Context mContext;
	DatagramSocket serverSocket;
	
	public ServerThread(Context currentContext, Handler handler) {
		mContext = currentContext;
		mHandler = handler;
		int port = 1111;
		try {
			serverSocket = new DatagramSocket(port);
			mHandler.obtainMessage(MainActivity.IS_RUNNING, "Server Started on port: " + port).sendToTarget();
		}catch(SocketException e) {
			e.printStackTrace();
		}
		InetAddress myIP = getMyWiFiIPAddress();
		mHandler.obtainMessage(MainActivity.IP_ADDRESS, myIP).sendToTarget();
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
				String s = new String(receivePacket.getData());
				mHandler.obtainMessage(MainActivity.PACKET_CAME, s).sendToTarget();
			}catch(Exception e) {
				socketOK = false;
			}
		}		
	}
}















