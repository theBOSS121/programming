package com.bombit.gui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.bombit.Game;

public class Options extends Launcher{
	private static final long serialVersionUID = 1L;

	
	private int width = 550;
	private int height = 450;
	private JButton OK;
	private Rectangle rOK, rresolution, rlevels, rplayer, rcol1, rcol2;
	private JTextField twidth, theight;
	private JLabel lresolution, llevels, lplayer, lcol1, lcol2;
	private Choice resolution = new Choice();
	private Choice levels = new Choice();
	private Choice player = new Choice();
	private Choice col1 = new Choice();
	private Choice col2 = new Choice();
	int s = 0, w, h, l = 1, p = 1, c1, c2; 
	
	public Options() {
		super(1);
		setTitle("Options");
		setSize(new Dimension(width, height));
		setLocationRelativeTo(null);
		drawButtons();
	}
	
	private void drawButtons() {
		OK = new JButton("OK");
		rOK = new Rectangle(width - 100, height - 70, button_width, button_height - 10);
		OK.setBounds(rOK);
		window.add(OK);
		
		rresolution = new Rectangle(80, 80, 80, 25);
		resolution.setBounds(rresolution);
		resolution.add("285, 295");
		resolution.add("570, 590");
		resolution.add("855, 885");
		resolution.add("1140, 1180");
		resolution.select(1);
		window.add(resolution);
		
		rlevels = new Rectangle(80, 120, 80, 25);
		levels.setBounds(rlevels);
		levels.add("1");
		levels.add("2");
		levels.add("3");
		levels.add("4");
		levels.select(0);
		window.add(levels);
		
		rplayer = new Rectangle(80, 160, 80, 25);
		player.setBounds(rplayer);
		player.add("1");
		player.add("2");
		player.add("multiplayer");
		player.select(2);
		window.add(player);
		
		rcol1 = new Rectangle(300, 140, 80, 25);
		col1.setBounds(rcol1);
		col1.add("red");
		col1.add("green");
		col1.add("blue");
		col1.add("white");
		col1.select(1);
		window.add(col1);
		
		rcol2 = new Rectangle(300, 180, 80, 25);
		col2.setBounds(rcol2);
		col2.add("red");
		col2.add("green");
		col2.add("blue");
		col2.add("white");
		col2.select(2);
		window.add(col2);
		
		lresolution = new JLabel("Resolution:");
		lresolution.setBounds(20, 80, 120, 20);
		window.add(lresolution);
		llevels = new JLabel("Levels:");
		llevels.setBounds(20, 120, 120, 20);
		window.add(llevels);
		lplayer = new JLabel("Players:");
		lplayer.setBounds(20, 160, 120, 20);
		window.add(lplayer);
		lcol1 = new JLabel("Player 1 color:");
		lcol1.setBounds(200, 140, 120, 20);
		window.add(lcol1);
		lcol2 = new JLabel("Player 2 color:");
		lcol2.setBounds(200, 180, 120, 20);
		window.add(lcol2);
		
		/*
		twidth = new JTextField();
		twidth.setBounds(80, 150, 60, 20);
		window.add(twidth);
		theight = new JTextField();
		theight.setBounds(80, 180, 60, 20);
		window.add(theight);
		*/
		
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Launcher(0);
				//config.saveConfiguration("width", parsheWidth());
				//config.saveConfiguration("height", parsheHeight());
				config.saveConfiguration("scale", parsheScale());
				config.saveConfiguration("level", parsheLevel());
				config.saveConfiguration("player", parshePlayer());
				config.saveConfiguration("col1", parsheCol1());
				config.saveConfiguration("col2", parsheCol2());
			}
		});
		
	}
	
	private void drop() {
		int selection = resolution.getSelectedIndex();
		if(selection == 0) {
			s = 1;
		}
		if(selection == 1 || selection == -1) {
			s = 2;
		}
		if(selection == 2) {
			s = 3;
		}
		if(selection == 3) {
			s = 4;
		}
			
	}
	
	private void dropLevel() {
		int selection = levels.getSelectedIndex();
		if(selection == 0) {
			l = 1;
		}
		if(selection == 1 || selection == -1) {
			l = 2;
		}
		if(selection == 2) {
			l = 3;
		}
		if(selection == 3) {
			l = 4;
		}
		
	}
	
	private void dropPlayer() {
		int selection = player.getSelectedIndex();
		if(selection == 0) {
			p = 1;
		}
		if(selection == 1 || selection == -1) {
			p = 2;
		}
		if(selection == 2) {
			p = 0;
		}
	}
	
	private void dropCol1() {
		int selection = col1.getSelectedIndex();
		if(selection == 0) {
			c1 = 0xff0000;
		}
		if(selection == 1 || selection == -1) {
			c1 = 0x00ff00;
		}
		if(selection == 2) {
			c1 = 0x0000ff;
		}
		if(selection == 3) {
			c1 = 0xffffff;
		}
		
	}
	
	private void dropCol2() {
		int selection = col2.getSelectedIndex();
		if(selection == 0) {
			c2 = 0xff0000;
		}
		if(selection == 1 || selection == -1) {
			c2 = 0x00ff00;
		}
		if(selection == 2) {
			c2 = 0x0000ff;
		}
		if(selection == 3) {
			c2 = 0xffffff;
		}
		
	}
	
	private int parsheCol1() {
		dropCol1();
		return c1;
	}
	
	private int parsheCol2() {
		dropCol2();
		return c2;
	}
	
	private int parshePlayer() {
		dropPlayer();
		return p;
	}
	
	private int parsheLevel() {
		dropLevel();
		return l;
	}

	private int parsheScale() {
		drop();
		return s;
	}
	
	private int parsheWidth() {
		try {
			int w = Integer.parseInt(twidth.getText());
			return w;
		}catch(NumberFormatException e) {
			drop();
			return w;
		}
	}
	
	private int parsheHeight() {
		try {
			int h = Integer.parseInt(theight.getText());
			return h;
		}catch(NumberFormatException e) {
			drop();
			return h;
		}
	}
}











