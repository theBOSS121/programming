package com.chess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Launcher extends JFrame{
	private static final long serialVersionUID = 1L;
	JButton multiplayer;
	JButton twoPlayers;
	
	JPanel window;
	
	public Launcher() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e) {}
		
		window = new JPanel();
		setVisible(true);
		setTitle("Chess");
		setSize(280, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(window);
		window.setLayout(null);	
		setButtons();
		setIconImage(new  ImageIcon(Launcher.class.getResource("/textures/chessIcon.png")).getImage());
	}
	
	private void setButtons() {
		multiplayer = new JButton("multiplayer!");
		multiplayer.setBounds(40, 60, 190, 50);
		window.add(multiplayer);
		

		twoPlayers = new JButton("Two players!");
		twoPlayers.setBounds(40, 150, 190, 50);
		window.add(twoPlayers);

		multiplayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.width = 145;//209;
				Game.multiplayer = true;
				dispose();
				Game.gameStart();
			}
		});
		
		twoPlayers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.width = 145;
				Game.multiplayer = false;
				dispose();
				Game.gameStart();
			}
		});
	}
	
}
