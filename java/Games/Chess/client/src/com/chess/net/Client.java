package com.chess.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import com.chess.BinaryWriter;
import com.chess.Game;
import com.chess.entity.Entity;
import com.chess.entity.pieces.Pown;
import com.chess.entity.pieces.Queen;
import com.chess.graphics.Sprite;

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
	public static int userID;
	public static int opponentsID;
	public boolean connected = false;
	public boolean started = false;
	
	private static InetAddress serverAddress;
	
	private static DatagramSocket socket;
	
	private final int MAX_PACKET_SIZE = 1024;
	private byte[] recivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];
	
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
		//InetAddress address = packet.getAddress();
		//int port = packet.getPort();
		String string = new String(packet.getData());
		
		if (!connected){
			userID = ByteBuffer.wrap(data, 0, 4).getInt();
			System.out.println("UserID = " + userID);
			connected = true;
		}else if(!started){
			opponentsID = ByteBuffer.wrap(data, 0, 4).getInt();
			System.out.println("opponents id " + opponentsID);
			if(opponentsID < userID) {
				Game.yourMove = false;
				Game.turn = 0;
			}
			started = true;			
		}if(string.startsWith("/moved/")){
			Game.yourMove = true;
		}else {
			for(int i = 0; i < Game.level.entities.size(); i++) {
				Entity e = Game.level.entities.get(i);
				if(e.x == ByteBuffer.wrap(data, 0, 4).getInt() && e.y == ByteBuffer.wrap(data, 4, 4).getInt()) {
					e.x = ByteBuffer.wrap(data, 8, 4).getInt();
					e.y = ByteBuffer.wrap(data, 12, 4).getInt();
					e.lastX = e.x;
					e.lastY = e.y;

					for(int i1 = 0; i1 < Game.level.entities.size(); i1++) {
						Entity en = Game.level.entities.get(i1);
						if(i1 != i) {
							if(en.x == e.x && en.y == e.y) {
								Game.level.entities.get(i1).remove();
								Game.level.update();
								break;
							}
						}						
					}
					if(e instanceof Pown) {
						if(e.col == 0 && e.y == 7 * 16) {
							Game.level.add(new Queen(e.x, e.y, Sprite.b_queen, e.col));
							Game.level.entities.remove(i);	
						}
						if(e.col == 1 && e.y == 0) {
							Game.level.add(new Queen(e.x, e.y, Sprite.w_queen, e.col));
							Game.level.entities.remove(i);
						}
					}
					break;
				}				
			}			
		}
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
	
	public Error getErrorCode(){
		return errorCode;
	}
	
}




