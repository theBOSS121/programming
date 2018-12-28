package com.loginServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Properties;


public class Configuration {
	
	Properties propeties = new Properties();
	private int  numOFClients;

	public void saveConfiguration(String key, int value) {
		String path = "config.xml";
		try {
			File file = new File(path);
			boolean exist = file.exists();
			if(!exist) {
				file.createNewFile();
			}
			OutputStream write = new FileOutputStream(path);
			propeties.setProperty(key, Integer.toString(value));
			propeties.storeToXML(write, "Options");
		}catch(Exception e) {}	
	}
	
	public void saveConfiguration(String key, String value) {
		String path = "config.xml";
		try {
			File file = new File(path);
			boolean exist = file.exists();
			if(!exist) {
				file.createNewFile();
			}
			OutputStream write = new FileOutputStream(path);
			propeties.setProperty(key, value);
			propeties.storeToXML(write, "Options");
		}catch(Exception e) {}	
	}
	
	public void remove(String key) {
		String path = "config.xml";
		try {
			File file = new File(path);
			boolean exist = file.exists();
			if(!exist) {
				file.createNewFile();
			}
			OutputStream write = new FileOutputStream(path);
			propeties.remove(key);
			propeties.storeToXML(write, "Options");
		}catch(Exception e) {}	
	}
	
	public void loadConfiguration(String path) {
		try {
			InputStream read = new FileInputStream(path);
			propeties.loadFromXML(read);
			//load all clients and save them in clients list
			numOFClients = Integer.parseInt(propeties.getProperty("numberOfClients"));
			for(int i = 0; i < numOFClients; i++) {
				String address = propeties.getProperty(Integer.toString(i) + "address");
				int port = Integer.parseInt(propeties.getProperty(Integer.toString(i) + "port"));
				Server.clients.add(new ServerClient(InetAddress.getByName(address), port));
				Server.clients.get(i).username = propeties.getProperty(Integer.toString(i) + "username");
				Server.clients.get(i).password = propeties.getProperty(Integer.toString(i) + "password");
				Server.clients.get(i).mail = propeties.getProperty(Integer.toString(i) + "mail");
				Server.clients.get(i).age = propeties.getProperty(Integer.toString(i) + "age");
				Server.clients.get(i).country = propeties.getProperty(Integer.toString(i) + "country");
				Server.clients.get(i).sex = propeties.getProperty(Integer.toString(i) + "sex");
				Server.clients.get(i).phoneNum = propeties.getProperty(Integer.toString(i) + "phone");
			}
			read.close();
		}catch(FileNotFoundException e) {
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
