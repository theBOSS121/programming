package com.thecherno.rain.graphics.UI;

import java.awt.Color;
import java.awt.Graphics;

import org.w3c.dom.ranges.RangeException;

import com.thecherno.rain.util.Vector2i;

public class UIProgressBar extends UIComponent{

	private double progress;
	private Color foregroundColor;
	
	public UIProgressBar(Vector2i position, Vector2i size) {
		super(position);
		this.size = size;
		foregroundColor = new Color(0xff00ff);
	}
	
	public void setProgress(double progress){
		if(progress < 0.0 || progress > 1.0) throw new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR, "progress must be from 0 to 100");
		this.progress = progress;
	}
	
	public void setForegroundColor(int color){
		this.foregroundColor = new Color(color);
	}
	
	public double getProgress(){
		return progress;
	}
	
	public void update(){
		
	}
	
	public void render(Graphics g){
		g.setColor(color);
		g.fillRect(position.x + offset.x, position.y + offset.y, size.x, size.y);

		g.setColor(foregroundColor);
		g.fillRect(position.x + offset.x, position.y + offset.y, (int)(progress * size.x), size.y);
	}

}
