package theBOSS.PongUDPClient;
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
	
	public ClientThread(Context applicationContext, Handler handler) {
		mContext = applicationContext;
		mHandler = handler;
		try {
			socket = new DatagramSocket();
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
	
	private void process(DatagramPacket receivePacket) {
		String s = new String(receivePacket.getData());
		mHandler.obtainMessage(Pong.PACKET_CAME, s).sendToTarget();
		if(s.startsWith("update")) {
			Renderer.opponent.x = Integer.parseInt(s.split("/x/|/e/")[1]);
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
}