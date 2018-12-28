package com.thecherno.rain.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.thecherno.rain.graphics.Sprite;
import com.thecherno.rain.util.BinaryWriter;
import com.thecherno.raincloud.serialization.RCDatabase;

public class Client {
	
	private final static byte[] PACKET_HEADER = new byte[] { 0x40, 0x40 };
	private final static byte PACKET_TYPE_CONNECT = 0x01; 
	private boolean listening = false;
	private Thread listenThread;
	
	private enum Error{
		NONE, INVALID_HOST, SOCKET_EXECEPTION
	}
	
	private String ipAddress;
	private static int port;
	private Error errorCode = Error.NONE;
	public int userID;
	public boolean connected = false;
	
	private static InetAddress serverAddress;
	
	private static DatagramSocket socket;
	
	private final int MAX_PACKET_SIZE = 1024;
	private byte[] recivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];
	
	List<ServerClient> clients = new ArrayList<ServerClient>();
	
	public Client(String host){
		String[] parts = host.split(":");
		if(parts.length !=2){
			errorCode = Error.INVALID_HOST;
			return;
		}
		ipAddress = parts[0];
		try{
			port = Integer.parseInt(parts[1]);
		}catch(NumberFormatException e){
			errorCode = Error.INVALID_HOST;
			return;
		}
	}
	
	public Client(String host, int port){
		ipAddress = host;
		this.port = port;
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
		InetAddress address = packet.getAddress();
		int port = packet.getPort();
		
		
		if(new String(data, 0, 4).equals("RCDB")){
			RCDatabase database = RCDatabase.Deserialize(data);
			process(database);
		}else if (!connected){
			userID = ByteBuffer.wrap(data, 0, 4).getInt();
			System.out.println("UserID = " + userID);
			connected = true;
		}else if(new String(data, 0, 3).equals("NEW")){
			if(clients.size() + 1 == ByteBuffer.wrap(data, 3, 4).getInt()){
					int x = ByteBuffer.wrap(data, ((ByteBuffer.wrap(data, 3, 4).getInt() - 1) * 12) + 7, 4).getInt();
					int y = ByteBuffer.wrap(data, ((ByteBuffer.wrap(data, 3, 4).getInt() - 1) * 12 + 4) + 7, 4).getInt();
					int spriteNum = ByteBuffer.wrap(data, ((ByteBuffer.wrap(data, 3, 4).getInt() - 1) * 12 + 8) + 7, 4).getInt();
					Sprite sprite = getSprite(spriteNum);
					clients.add(new ServerClient(x, y, sprite));
			}else if (clients.size() == 0 && ByteBuffer.wrap(data, 3, 4).getInt() > 1){
				for(int i = 0; i < ByteBuffer.wrap(data, 3, 4).getInt(); i++){
					int x = ByteBuffer.wrap(data,7 + (i * 12), 4).getInt();
					int y = ByteBuffer.wrap(data,4 + 7 + (i * 12), 4).getInt();
					int spriteNum = ByteBuffer.wrap(data,8 + 7 + (i * 12), 4).getInt();
					Sprite sprite = getSprite(spriteNum);
					clients.add(new ServerClient(x, y, sprite));
				}
			}else if(clients.size() != 0){
				for(int i = 0; i < ByteBuffer.wrap(data, 3, 4).getInt(); i++){
					clients.get(i).x = ByteBuffer.wrap(data, 7 + (i * 12), 4).getInt();
					clients.get(i).y = ByteBuffer.wrap(data, 4 + 7 + (i * 12), 4).getInt();
					clients.get(i).sprite = getSprite(ByteBuffer.wrap(data, 8 + 7 + (i * 12), 4).getInt());
				}
			}
		}
	}

	private Sprite getSprite(int spriteNum) {
		Sprite sprite = Sprite.player_back;
		if(spriteNum == 0) sprite = Sprite.player_forward;
		if(spriteNum == 1) sprite = Sprite.player_forward_1;
		if(spriteNum == 2) sprite = Sprite.player_forward_2;
		if(spriteNum == 3) sprite = Sprite.player_back;
		if(spriteNum == 4) sprite = Sprite.player_back_1;
		if(spriteNum == 5) sprite = Sprite.player_back_2;
		if(spriteNum == 6) sprite = Sprite.player_side;
		if(spriteNum == 7) sprite = Sprite.player_side_1;
		if(spriteNum == 8) sprite = Sprite.player_side_2;
		if(spriteNum == 9) sprite = Sprite.player_side2;
		if(spriteNum == 10) sprite = Sprite.player_side2_1;
		if(spriteNum == 11) sprite = Sprite.player_side2_2;
		return sprite;
	}

	private void process(RCDatabase database) {
		//database
	}

	public boolean connect(){
		try {
			serverAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			errorCode = Error.INVALID_HOST;
			return false;
		}
		
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			errorCode = Error.SOCKET_EXECEPTION;
			return false;
		}
		
		sendConnectionPacket();
		

		listening = true;
		
		listenThread = new Thread(() -> listen(), "listen");
		listenThread.start();
		return true;
	}
	
	private void sendConnectionPacket() {
		BinaryWriter writer = new BinaryWriter();
		writer.write(PACKET_HEADER);
		writer.write(PACKET_TYPE_CONNECT);
		send(writer.getBuffer());
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
	
	public void send(RCDatabase database){
		assert(socket.isConnected());
		byte[] data = new byte[database.getSize()];
		database.getBytes(data, 0);
		send(data);
	}
	
	public Error getErrorCode(){
		return errorCode;
	}
	
}




