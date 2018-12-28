package com.mime.minefront.gui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.mime.minefront.Display;

public class Options extends Launcher{
	private static final long serialVersionUID = 1L;

	
	private int width = 550;
	private int height = 450;
	private JButton OK;
	private Rectangle rOK, rresolution;
	private JTextField twidth, theight;
	private JLabel lwidth, lheight;
	private Choice resolution = new Choice();
	int w = 0; 
	int h = 0;
	
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
		
		rresolution = new Rectangle(50, 80, 80, 25);
		resolution.setBounds(rresolution);
		resolution.add("640, 480");
		resolution.add("800, 600");
		resolution.add("1024, 768");
		resolution.select(1);
		window.add(resolution);

		lwidth = new JLabel("Width:");
		lwidth.setBounds(30, 150, 120, 20);
		window.add(lwidth);
		lheight = new JLabel("Height:");
		lheight.setBounds(30, 180, 120, 20);
		window.add(lheight);
		
		twidth = new JTextField();
		twidth.setBounds(80, 150, 60, 20);
		window.add(twidth);
		theight = new JTextField();
		theight.setBounds(80, 180, 60, 20);
		window.add(theight);
		
		
		OK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Launcher(0);
				config.saveConfiguration("width", parsheWidth());
				config.saveConfiguration("height", parsheHeight());
			}
		});
		
	}
	
	private void drop() {
		Display.selection = resolution.getSelectedIndex();
		int selection = resolution.getSelectedIndex();
		if(selection == 0) {
			w = 640;
			h = 480;
		}
		if(selection == 1 || selection == -1) {
			w = 800;
			h = 600;
		}
		if(selection == 2) {
			w = 1024;
			h = 768;
		}
			
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











