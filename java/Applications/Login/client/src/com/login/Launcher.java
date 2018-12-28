package com.login;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Launcher extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JPanel window = new JPanel();
	private final int width = 265;
	private final int height = 340;
	
	private JButton login, signin;
	
	public Launcher() {
		createWindow();
	}
	
	private void createWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		getContentPane().add(window);
		setTitle("Launcher");
		setVisible(true);
		setSize(width, height);
		Image image = new  ImageIcon("C:\\Users\\Luka\\Pictures\\Saved Pictures\\icon.png").getImage();
		setIconImage(image);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setLayout(null);
		draw();
		repaint();
	}
	
	private void draw() {
		login = new JButton("Login");
		login.setBounds(51, 50, 150, 70);
		login.setFont(new Font("Ariel", 0, 15));
		window.add(login);

		signin = new JButton("Sign in");
		signin.setBounds(51, 180, 150, 70);
		signin.setFont(new Font("Ariel", 0, 15));
		window.add(signin);
		
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Login();
			}
		});
		signin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Register();
			}
		});
		
	}
}













