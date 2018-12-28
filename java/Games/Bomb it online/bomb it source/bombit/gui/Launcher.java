package com.bombit.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.bombit.Configuration;
import com.bombit.Game;
import com.bombit.level.Level;

public class Launcher extends JFrame{
	private static final long serialVersionUID = 1L;

	protected JPanel window = new JPanel();
	private JButton play, options, quit, multiplayer;
	private Rectangle rplay, roptions, rquit, rmultiplayer;
	Configuration config = new Configuration();
	
	private int width = 240;
	private int height = 320;
	protected int button_width = 88;
	protected int button_height = 40;
	
	public Launcher(int id) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {}
		//if you want no title or boarder
		//setUndecorated(true);
		setTitle("Bomb it!");
		setSize(new Dimension(width, height));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(window);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		window.setLayout(null);
		if(id == 0) {
			drawButtons();
		}
		repaint();
	}
	
	private void drawButtons() {		
		play = new JButton("Play!");
		rplay = new Rectangle(width / 2 - button_width / 2, 90, button_width, button_height);
		play.setBounds(rplay);
		window.add(play);
		
		multiplayer = new JButton("Multiplayer!");
		rmultiplayer = new Rectangle(width / 2 - button_width / 2, 140, button_width, button_height);
		multiplayer.setBounds(rmultiplayer);
		window.add(multiplayer);

		options = new JButton("Options!");
		roptions = new Rectangle(width / 2 - button_width / 2, 190, button_width, button_height);
		options.setBounds(roptions);
		window.add(options);

		quit = new JButton("Quit!");
		rquit = new Rectangle(width / 2 - button_width / 2, 240, button_width, button_height);
		quit.setBounds(rquit);
		window.add(quit);
	
		multiplayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				config.loadConfiguration("res/settings/config.xml");
				Game.level = Level.level1;
				Game.players = -1;
				Game.multiplayer = true;
				dispose();
				Game.gameStart();;
			}
		});
		
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				config.loadConfiguration("res/settings/config.xml");
				if(Game.players == -1) {
					Game.multiplayer = false;
					Game.players = 1;
				}
				dispose();
				Game.gameStart();;
			}
		});
		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Options();
			}
		});
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
	}
	
}
