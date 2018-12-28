package com.example.hellothreadsclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.Handler;

public class ClientThread extends Thread{

	Handler mHandler;
	Context mContext;
	static DatagramSocket socket;
	boolean socketOK = true;
	public static int port = 1111;
	
	public static InetAddress serverAddress;	
	
	public ClientThread(Context applicationContext, Handler handler) {
		try {
			serverAddress = InetAddress.getByName("0.0.0.0");
		}catch(UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		mContext = applicationContext;
		mHandler = handler;
		try {
			socket = new DatagramSocket();
		}catch (Exception e) {
			
		}
		
	}

	
	public void run() {
		byte[] receiveData = new byte[1024];
		while(socketOK) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				socket.receive(receivePacket);
				String s = new String(receivePacket.getData());
				mHandler.obtainMessage(MainActivityClient.PACKET_CAME, s).sendToTarget();
			}catch(Exception e) {
				socketOK = false;
			}
		}	
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
