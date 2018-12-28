package com.login;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Login extends JFrame{
	private static final long serialVersionUID = 6316245527286192536L;
	
	private JPanel window = new JPanel();
	private final int width = 400;
	private final int height = 250;
	
	private String username, password;
	
	private JTextField tusername;
	private JPasswordField tpassword;
	private JLabel lusername, lpassword, lwrong;
	private JButton login, signin;
	
	public Login() {
		createWindow();
	}
	
	public Login(int i) {
		if(i == 1) {
			createWindow();
			wrong();
		}
		if(i == 2) {
			createWindow();
			blanc();
			
		}
	}
	
	private void blanc() {
		lwrong = new JLabel("You can't leave password or username blank");
		lwrong.setForeground(Color.RED);
		lwrong.setFont(new Font("Verdana", 0, 15));
		lwrong.setBounds(22, 50, 400, 30);
		window.add(lwrong);
	}

	private void wrong() {
		lwrong = new JLabel("Password or username is wrong");
		lwrong.setForeground(Color.RED);
		lwrong.setFont(new Font("Verdana", 0, 15));
		lwrong.setBounds(70, 50, 400, 30);
		window.add(lwrong);
	}

	private void createWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		getContentPane().add(window);
		setTitle("Login");
		setVisible(true);
		Image image = new  ImageIcon("C:\\Users\\Luka\\Pictures\\Saved Pictures\\icon.png").getImage();
		setIconImage(image);
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.setLayout(null);
		draw();
		repaint();
	}
	
	private void draw() {
		
		
		tusername = new JTextField();
		tusername.setBounds(80, 100, 180, 30);
		window.add(tusername);
		tpassword = new JPasswordField();
		tpassword.setBounds(80, 150, 180, 30);
		window.add(tpassword);
		
		lusername = new JLabel("Username:");
		lusername.setBounds(20, 100, 180, 30);
		window.add(lusername);
		lpassword = new JLabel("Password:");
		lpassword.setBounds(20, 150, 180, 30);
		window.add(lpassword);
		
		login = new JButton("Login");
		login.setBounds(270, 150, 100, 30);
		window.add(login);
		signin = new JButton("Sign in");
		signin.setBounds(270, 100, 100, 30);
		window.add(signin);
		
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendLoginPacket();
				dispose();
			}
		});
		signin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Register();
			}
		});
	}
	
	private void sendLoginPacket() {
		username = tusername.getText();
		password = new String(tpassword.getPassword());
		
		if(username.equals("") || password.equals("")) {
			new Login(2);
		}else {
			String login = "/l/" + "/u/" + username + "/p/" + password + "/el/" + "/" + "/end/";
			
			Client.send(login.getBytes());
		}
	}
}













