package com.login;

import java.awt.Choice;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Profile extends JFrame{
	private static final long serialVersionUID = -4169612451675842247L;
	
	private JPanel window = new JPanel();
	private final int width = 600;
	private final int height = 500;
	
	public boolean enable = false;
	private final int userID;
	private static byte pixels[];
	
	private String username, password, mail, phone, sex, country, age;
	
	private JTextField tusername, tmail, tphone;
	private JPasswordField tpassword;
	private JLabel lusername, lpassword, lmail, lphone, lsex, lcountry, lage;
	private Choice csex, ccountry, cage;
	private JButton edit, save, delete, logOut, getPhoto;
	
	private static ImageIcon img;
	private static JLabel limg;
	
	public Profile(int userID, String username, String password, String mail, String age, String country, String sex, String phoneNum) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.mail = mail;
		this.age = age;
		this.country = country;
		this.sex = sex;
		this.phone = phoneNum;
		createWindow();
	}
	
	private void createWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		getContentPane().add(window);
		setTitle("Your Profile");
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
		csex.setBounds(100, 25, 180, 30);
		csex.add("male");
		csex.add("female");
		csex.add("other");
		csex.select(sex);
		csex.setEnabled(enable);
		window.add(csex);
		ccountry = new Choice();
		ccountry.setBounds(100, 275, 180, 30);
		ccountry.add("Slovenija");
		ccountry.add("England");
		ccountry.add("USA");
		ccountry.add("Germany");
		ccountry.add("France");
		ccountry.add("Spain");
		ccountry.add("Russia");
		ccountry.setEnabled(enable);
		ccountry.select(country);
		window.add(ccountry);
		cage = new Choice();
		cage.setBounds(100, 325, 180, 30);
		cage.add("1-10");
		cage.add("10-17");
		cage.add("18-30");
		cage.add("30-99");
		cage.setEnabled(enable);
		cage.select(age);
		window.add(cage);
		
		tusername = new JTextField(username);
		tusername.setBounds(100, 70, 180, 30);
		tusername.setEnabled(enable);
		window.add(tusername);
		tpassword = new JPasswordField(password);
		tpassword.setBounds(100, 120, 180, 30);
		tpassword.setEnabled(enable);
		window.add(tpassword);
		tmail = new JTextField(mail);
		tmail.setBounds(100, 170, 180, 30);
		tmail.setEnabled(enable);
		window.add(tmail);
		tphone = new JTextField(phone);
		tphone.setBounds(100, 220, 180, 30);
		tphone.setEnabled(enable);
		window.add(tphone);

		lsex = new JLabel("Sex:");
		lsex.setBounds(20, 20, 180, 30);
		window.add(lsex);
		lusername = new JLabel("Username:");
		lusername.setBounds(20, 70, 180, 30);
		window.add(lusername);
		lpassword = new JLabel("Password:");
		lpassword.setBounds(20, 120, 180, 30);
		window.add(lpassword);
		lmail = new JLabel("Gmail:");
		lmail.setBounds(20, 170, 180, 30);
		window.add(lmail);
		lphone = new JLabel("Phone number:");
		lphone.setBounds(20, 220, 180, 30);
		window.add(lphone);
		lcountry = new JLabel("Country");
		lcountry.setBounds(20, 270, 180, 30);
		window.add(lcountry);
		lage = new JLabel("Age");
		lage.setBounds(20, 320, 180, 30);
		window.add(lage);
		img = new ImageIcon("C:/Users/Luka/Pictures/Saved Pictures/profile.png");			
		if(pixels != null) {
			img = new ImageIcon(pixels);
		}
		limg = new JLabel(img);
		limg.setBounds(314, 25, 240, 240);
		window.add(limg);
		
		edit = new JButton("Edit");
		edit.setBounds(20, 405, 120, 30);
		window.add(edit);
		save = new JButton("Save");
		save.setBounds(20, 365, 120, 30);
		window.add(save);
		delete = new JButton("Delete");
		delete.setBounds(160, 405, 120, 30);
		window.add(delete);
		logOut = new JButton("Log out");
		logOut.setBounds(160, 365, 120, 30);
		window.add(logOut);
		getPhoto = new JButton("Get profile picture");
		getPhoto.setBounds(314, 365, 240, 70);
		window.add(getPhoto);
		
		
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!enable) {
					enable = true;
					csex.setEnabled(enable);
					ccountry.setEnabled(enable);
					cage.setEnabled(enable);
					tusername.setEnabled(enable);
					tpassword.setEnabled(enable);
					tmail.setEnabled(enable);
					tphone.setEnabled(enable);
				}
			}
		});
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(enable) {
					enable = false;
					csex.setEnabled(enable);
					ccountry.setEnabled(enable);
					cage.setEnabled(enable);
					tusername.setEnabled(enable);
					tpassword.setEnabled(enable);
					tmail.setEnabled(enable);
					tphone.setEnabled(enable);
					sendInformation();
				}
			}
		});
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendDelete();
				dispose();
				new Login();
			}
		});
		logOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Login();
			}
		});
		getPhoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							img = new ImageIcon(chooser.getSelectedFile().getAbsolutePath());
							
							limg.setIcon(img);
						}catch(Exception e) {}
					}
				});
				
			}
		});
		
	}
	
	private void sendDelete() {
		String delete = "/d/" + "/id/" + userID +  "/end/";
		
		Client.send(delete.getBytes());
	}

	private void sendInformation() {
		username = tusername.getText();
		password = new String(tpassword.getPassword());
		mail = tmail.getText();
		age = cage.getSelectedItem();
		country = ccountry.getSelectedItem();
		sex = csex.getSelectedItem();
		phone = tphone.getText();
		
		String informations = "/e/" + "/id/" + userID + "/u/" + username + "/p/" + password + "/m/" + mail + "/a/" + age + "/c/" + country + "/s/" + sex + "/pn/" + phone + "/ee/" + "/" +  "/end/";
		
		Client.send(informations.getBytes());
		
	}
	
}
