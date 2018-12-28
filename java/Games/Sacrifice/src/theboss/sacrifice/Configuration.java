package theboss.sacrifice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import theboss.sacrifice.game.Game;

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
//			String music = propeties.getProperty("music");
//			setMusic(Integer.parseInt(music));
//			String sounds = propeties.getProperty("sounds");
//			setSounds(Integer.parseInt(sounds));
			String best = propeties.getProperty("best");
			setBest(Integer.parseInt(best));
			read.close();
		}catch(FileNotFoundException e) {
			saveConfiguration("best", 0);
//			saveConfiguration("sounds", 1);
//			saveConfiguration("music", 1);
			loadConfiguration(path);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

//	private void setSounds(int sounds) {
//		if(sounds == 0) {
//			Game.sounds = false;			
//		}else {
//			Game.sounds = true;						
//		}
//	}
//
//	private void setMusic(int music) {
//		if(music == 0) {
//			Game.music = false;			
//		}else {
//			Game.music = true;						
//		}	
//	}

	public void setBest(int best) {
		Game.bestScore = best;
	}	
}