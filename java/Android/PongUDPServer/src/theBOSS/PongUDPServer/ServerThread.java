package theBOSS.PongUDPServer;

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

public class ServerThread extends Thread{
	
	Handler mHandler;
	Context mContext;
	static DatagramSocket serverSocket;
	
	public List<Client> clients = new ArrayList<Client>();
	
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
				process(receivePacket);
			}catch(Exception e) {
				socketOK = false;
			}
		}		
	}
	
	private void process(DatagramPacket receivePacket) {
		String s = new String(receivePacket.getData());
		
		boolean newClient = true;
		
		for(int i = 0; i < clients.size(); i++) {
			if(receivePacket.getAddress().equals(clients.get(i).address) && receivePacket.getPort() == clients.get(i).port) {
				newClient = false;
				if(s.startsWith("/x/")) {
					String sx = s.split("/x/|/e/")[1];
					clients.get(i).x = Integer.parseInt(sx);
					mHandler.obtainMessage(MainActivity.PACKET_CAME, sx).sendToTarget();	
					if(clients.size() == 2) {
						sendXToBoth();
					}
				}
			}
		}
		
		if(newClient) {
			clients.add(new Client(receivePacket.getAddress(), receivePacket.getPort()));
			mHandler.obtainMessage(MainActivity.NEW_CLIENT, Integer.toString(clients.size()) + " clients connected").sendToTarget();					
		}
	}

	private void sendXToBoth() {
		String data = "update" + "/x/" + clients.get(1).x + "/e/";
		send(data.getBytes(), clients.get(0).address, clients.get(0).port);
		data = "update" + "/x/" + clients.get(0).x + "/e/";
		send(data.getBytes(), clients.get(1).address, clients.get(1).port);
		
	}

	public static void send(byte[] data, InetAddress address, int port) {
		try {			
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			serverSocket.send(packet);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
