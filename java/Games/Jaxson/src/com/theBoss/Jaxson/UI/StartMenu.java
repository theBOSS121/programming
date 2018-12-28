package com.theBoss.Jaxson.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import com.theBoss.Jaxson.input.Mouse;

public class StartMenu {
	
	public BufferedImage exitImage, creditsImage, optionsImage, helpImage, scoresImage;
	public JLabel exit, credits, options, help, scores;
	private boolean enteredExit, enteredCredits, enteredOptions, enteredHelp, enteredScores;
	
	public StartMenu() {
		try {
			exitImage = ImageIO.read((StartMenu.class.getResource("/exitButton.png")));
			creditsImage = ImageIO.read((StartMenu.class.getResource("/creditsButton.png")));
			optionsImage = ImageIO.read((StartMenu.class.getResource("/optionsButton.png")));
			helpImage = ImageIO.read((StartMenu.class.getResource("/helpButton.png")));
			scoresImage = ImageIO.read((StartMenu.class.getResource("/scoresButton.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		exit = new JLabel();
		credits = new JLabel();
		options = new JLabel();
		help = new JLabel();
		scores = new JLabel();
	}
	
	public void update(int screenWidth, int screenHeight) {
		exit.setLocation(screenWidth / 30, screenHeight / 32 * 25);
		exit.setSize(screenWidth / 10, screenHeight / 15);
		credits.setLocation(screenWidth / 30, screenHeight / 32 * 22);
		credits.setSize(screenWidth / 5, screenHeight / 15);
		options.setLocation(screenWidth / 30, screenHeight / 32 * 19);
		options.setSize(screenWidth / 5, screenHeight / 15);
		help.setLocation(screenWidth / 30, screenHeight / 32 * 16);
		help.setSize(screenWidth / 10, screenHeight / 15);
		scores.setLocation(screenWidth / 30, screenHeight / 32 * 13);
		scores.setSize(screenWidth / 6, screenHeight / 15);
		if(isMouseIn(exit) && Mouse.getButton() == 1) {
			System.exit(0);
		}
		if(isMouseIn(credits) && Mouse.getButton() == 1) {
			System.out.println("Credits to theBoss");
		}
		if(isMouseIn(options) && Mouse.getButton() == 1) {
			System.out.println("Options");
		}
		if(isMouseIn(help) && Mouse.getButton() == 1) {
			System.out.println("Hepl!");
		}
		if(isMouseIn(scores) && Mouse.getButton() == 1) {
			System.out.println("scores");
		}
		if(isMouseIn(exit)) {
			enteredExit = true;
		}else {
			enteredExit = false;			
		}
		if(isMouseIn(credits)) {
			enteredCredits = true;
		}else {
			enteredCredits = false;			
		}
		if(isMouseIn(options)) {
			enteredOptions = true;
		}else {
			enteredOptions = false;			
		}
		if(isMouseIn(help)) {
			enteredHelp = true;
		}else {
			enteredHelp = false;			
		}
		if(isMouseIn(scores)) {
			enteredScores = true;
		}else {
			enteredScores = false;			
		}
	}
	
	private boolean isMouseIn(JLabel l) {
		if(Mouse.getX() > l.getX() && Mouse.getX() < l.getX() + l.getWidth() && Mouse.getY() > l.getY() && Mouse.getY() < l.getY() + l.getHeight()) {
			return true;
		}		
		return false;
	}
	
	public void render(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(exitImage, exit.getX(), exit.getY(), exit.getWidth(), exit.getHeight(), null);
		g.drawImage(creditsImage, credits.getX(), credits.getY(), credits.getWidth(), credits.getHeight(), null);
		g.drawImage(optionsImage, options.getX(), options.getY(), options.getWidth(), options.getHeight(), null);
		g.drawImage(helpImage, help.getX(), help.getY(), help.getWidth(), help.getHeight(), null);
		g.drawImage(scoresImage, scores.getX(), scores.getY(), scores.getWidth(), scores.getHeight(), null);
		g.setStroke(new BasicStroke(4f));
		g.setColor(Color.BLACK);
		if(enteredExit) {
			g.drawRect(exit.getX() - 5, exit.getY() - 5, exit.getWidth() + 9, exit.getHeight() + 9);
		}
		if(enteredCredits) {
			g.drawRect(credits.getX() - 5, credits.getY() - 5, credits.getWidth() + 9, credits.getHeight() + 9);
		}
		if(enteredOptions) {
			g.drawRect(options.getX() - 5, options.getY() - 5, options.getWidth() + 9, options.getHeight() + 9);
		}
		if(enteredHelp) {
			g.drawRect(help.getX() - 5, help.getY() - 5, help.getWidth() + 9, help.getHeight() + 9);
		}
		if(enteredScores) {
			g.drawRect(scores.getX() - 5, scores.getY() - 5, scores.getWidth() + 9, scores.getHeight() + 9);
		}
	}

}
