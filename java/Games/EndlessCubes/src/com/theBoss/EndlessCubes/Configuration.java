package com.theBoss.EndlessCubes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.theBoss.EndlessCubes.graphics.Screen;

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
			propeties.storeToXML(write, "Options");
		}catch(Exception e) {}	
	}
	
	public void loadConfiguration(String path) {
		try {
			InputStream read = new FileInputStream(path);
			propeties.loadFromXML(read);
			String best = propeties.getProperty("best");
			setBest(Integer.parseInt(best));
			String brightness = propeties.getProperty("brightness");
			setBrightness(Integer.parseInt(brightness));
			read.close();
		}catch(FileNotFoundException e) {
			saveConfiguration("best", 0);
			saveConfiguration("brightness", 1400);
			loadConfiguration(path);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void setBest(int best) {
		EndlessCubes.bestScore = best;
	}	
	
	public void setBrightness(int renderDistance) {
		Screen.renderDistance = renderDistance;
	}
}
