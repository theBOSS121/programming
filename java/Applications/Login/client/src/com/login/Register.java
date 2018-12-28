package com.login;

import java.awt.Choice;
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

public class Register extends JFrame{
	private static final long serialVersionUID = 2350793150047713412L;
	
	private JPanel window = new JPanel();
	private final int width = 600;
	private final int height = 500;
	
	private String username, password, mail, phone, sex, country, age;
	
	private JTextField tusername, tmail, tphone;
	private JPasswordField tpassword;
	private JLabel lusername, lpassword, lmail, lphone, lsex, lcountry, lage;
	private Choice csex, ccountry, cage;
	private JButton signin, login;
	
	public Register() {
		createWindow();
	}
	
	private void createWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		getContentPane().add(window);
		setTitle("Sign in");
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
		
		csex = new Choice();
		csex.setBounds(100, 55, 180, 30);
		csex.add("male");
		csex.add("female");
		csex.add("other");
		window.add(csex);
		ccountry = new Choice();
		ccountry.setBounds(100, 305, 180, 30);
		ccountry.add("Slovenija");
		ccountry.add("England");
		ccountry.add("USA");
		ccountry.add("Germany");
		ccountry.add("France");
		ccountry.add("Spain");
		ccountry.add("Russia");
		window.add(ccountry);
		cage = new Choice();
		cage.setBounds(100, 355, 180, 30);
		cage.add("1-10");
		cage.add("10-17");
		cage.add("18-30");
		cage.add("30-99");
		window.add(cage);
		
		tusername = new JTextField();
		tusername.setBounds(100, 100, 180, 30);
		window.add(tusername);
		tpassword = new JPasswordField();
		tpassword.setBounds(100, 150, 180, 30);
		window.add(tpassword);
		tmail = new JTextField();
		tmail.setBounds(100, 200, 180, 30);
		window.add(tmail);
		tphone = new JTextField();
		tphone.setBounds(100, 250, 180, 30);
		window.add(tphone);

		lsex = new JLabel("Sex:");
		lsex.setBounds(20, 50, 180, 30);
		window.add(lsex);
		lusername = new JLabel("Username:");
		lusername.setBounds(20, 100, 180, 30);
		window.add(lusername);
		lpassword = new JLabel("Password:");
		lpassword.setBounds(20, 150, 180, 30);
		window.add(lpassword);
		lmail = new JLabel("Gmail:");
		lmail.setBounds(20, 200, 180, 30);
		window.add(lmail);
		lphone = new JLabel("Phone number:");
		lphone.setBounds(20, 250, 180, 30);
		window.add(lphone);
		lcountry = new JLabel("Country");
		lcountry.setBounds(20, 300, 180, 30);
		window.add(lcountry);
		lage = new JLabel("Age");
		lage.setBounds(20, 350, 180, 30);
		window.add(lage);
		
		
		signin = new JButton("Sing in");
		signin.setBounds(400, 350, 100, 35);
		window.add(signin);
		login = new JButton("Log in");
		login.setBounds(400, 300, 100, 35);
		window.add(login);
		
		signin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				sendInformation();
				new Login();
			}
		});
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Login();
			}
		});
	}
	
	private void sendInformation() {
		username = tusername.getText();
		password = new String(tpassword.getPassword());
		mail = tmail.getText();
		age = cage.getSelectedItem();
		country = ccountry.getSelectedItem();
		sex = csex.getSelectedItem();
		phone = tphone.getText();
		
		String registration = "/r/" + "/u/" + username + "/p/" + password + "/m/" + mail + "/a/" + age + "/c/" + country + "/s/" + sex + "/pn/" + phone + "/er/" + "/" +  "/end/";
		Client.send(registration.getBytes());
		
	}
	
}







