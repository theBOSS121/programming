package com.mime.minefront;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {
	
	Properties propeties = new Properties();

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
			propeties.storeToXML(write, "Resolution");
		}catch(Exception e) {}	
	}
	
	public void loadConfiguration(String path) {
		try {
			InputStream read = new FileInputStream(path);
			propeties.loadFromXML(read);
			String width = propeties.getProperty("width");
			String height = propeties.getProperty("height");
			setResolution(Integer.parseInt(width), Integer.parseInt(height));
			read.close();
		}catch(FileNotFoundException e) {
			saveConfiguration("width", 800);
			saveConfiguration("height", 600);
			loadConfiguration(path);
		}catch(Exception e) {}
		
	}
	
	public void setResolution(int width, int height) {
		Display.width = width;
		Display.height = height;
	}
	
	
}
