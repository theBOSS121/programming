package com.bombit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.bombit.level.Level;

public class Configuration {
	
	Properties propeties = new Properties();

	public void saveConfiguration(String key, int value) {
		String path = "res/settings/config.xml";
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
			String scale = propeties.getProperty("scale");
			String level = propeties.getProperty("level");
			String player = propeties.getProperty("player");
			String col1 = propeties.getProperty("col1");
			String col2 = propeties.getProperty("col2");
			setResolution(Integer.parseInt(scale));
			setLevel(Integer.parseInt(level));
			setPlayer(Integer.parseInt(player));
			setColor(Integer.parseInt(col1));
			if(Game.players == 2)
				setColor2(Integer.parseInt(col2));
			read.close();
		}catch(FileNotFoundException e) {
			saveConfiguration("scale", 3);
			saveConfiguration("level", 1);
			saveConfiguration("player", 1);
			saveConfiguration("col1", 0x0000ff);
			saveConfiguration("col2", 0xff0000);
			loadConfiguration(path);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void setColor(int color) {
		Game.col1 = color;
	}
	
	private void setColor2(int color) {
		Game.col2 = color;
		
	}
	
	private void setPlayer(int player) {
		if(player == 1) {
			Game.players = 1;
		}
		if(player == 2) {
			Game.players = 2;
		}
		if(player == 0) {
			//change???
			Game.players = -1;
			Game.multiplayer = true;
		}
	}

	private void setLevel(int level) {
		if(level == 1) {
			Game.level = Level.level1;
		}if(level == 2) {
			Game.level = Level.level2;
		}if(level == 3) {
			Game.level = Level.level3;
		}if(level == 4) {
			Game.level = Level.level4;
		}
	}

	public void setResolution(int scale) {
		Game.scale = scale;
	}
	
	
}
