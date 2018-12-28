package com.theBoss.EndlessCubes.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.theBoss.EndlessCubes.EndlessCubes;
import com.theBoss.EndlessCubes.graphics.Screen;
import com.theBoss.EndlessCubes.input.Mouse;

public class Menu {
	int screenWidth, screenHeight, sizeOfGame;
	int xOffset, yOffset;
	private Font f;
	private String play = "Play";
	private String settings = "Settings";
	private String exit = "Exit";
	private String back = "Back";
	private String brightness = "+ brightness -";
	private String  transparency = "transparency";
	private int pw = 0, sw = 0, xw = 0,bw = 0, brw = 0, tw = 0, h = 0, fontAccent = 0;
	public int state = 0;
	private boolean backBuffer = false, playBuffer = false, settingsBuffer = false, exitBuffer = false, bestBuffer = false, brithnessBuffer = false, transparencyBuffer = false;
	public int col = 0xffbf00;
	//font accent is yOffset from y to rendered character
	
	public Menu() {
		
	}
	
	public void update(int screenWidth, int screenHeight, int sizeOfGame) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.sizeOfGame = sizeOfGame;
		xOffset = (screenWidth - sizeOfGame) / 2;
		yOffset = (screenHeight - sizeOfGame) / 2;
		if(state == 0) {
			if(isMouseIn(screenWidth / 2 - pw / 2, yOffset + sizeOfGame / 7 * 2 - fontAccent, pw, h) && Mouse.getButton() == 1 && !playBuffer) {
				playBuffer = true;
			}
			if(isMouseIn(screenWidth / 2 - pw / 2, yOffset + sizeOfGame / 7 * 2 - fontAccent, pw, h) && Mouse.getButton() == 0 && playBuffer) {
				EndlessCubes.gameOver = false;
				EndlessCubes.m = false;
				EndlessCubes.startGame();
			}
			if(isMouseIn(screenWidth / 2 - sw / 2, yOffset + sizeOfGame / 7 * 4 - fontAccent, sw, h) && Mouse.getButton() == 1 && !settingsBuffer) {
				settingsBuffer = true;
			}
			if(isMouseIn(screenWidth / 2 - sw / 2, yOffset + sizeOfGame / 7 * 4 - fontAccent, sw, h) && Mouse.getButton() == 0 && settingsBuffer) {
				state = 1;
			}
			if(isMouseIn(screenWidth / 2 - xw / 2, yOffset + sizeOfGame / 7 * 6 - fontAccent, xw, h) && Mouse.getButton() == 1 && !exitBuffer) {
				exitBuffer = true;
			}
			if(isMouseIn(screenWidth / 2 - xw / 2, yOffset + sizeOfGame / 7 * 6 - fontAccent, xw, h) && Mouse.getButton() == 0 && exitBuffer) {
				System.exit(0);
			}
		}
		if(state == 1) {
			if(isMouseIn(screenWidth / 2 - bw / 2, yOffset + sizeOfGame / 7 * 6 - fontAccent, bw, h) && Mouse.getButton() == 1 && !backBuffer) {
				backBuffer = true;
			}
			if(isMouseIn(screenWidth / 2 - bw / 2, yOffset + sizeOfGame / 7 * 6 - fontAccent, bw, h) && Mouse.getButton() == 0 && backBuffer) {
				state = 0;
				backBuffer = false;
			}
			if(isMouseIn(screenWidth / 2 - brw / 2, yOffset + sizeOfGame / 7 * 2 - fontAccent, brw, h) && Mouse.getButton() == 1 && !brithnessBuffer) {
				brithnessBuffer = true;
			}
			if(isMouseIn(screenWidth / 2 - brw / 2, yOffset + sizeOfGame / 7 * 2 - fontAccent, brw / 2, h) && Mouse.getButton() == 0 && brithnessBuffer) {
				Screen.renderDistance += 200;
				brithnessBuffer = false;
				EndlessCubes.config.saveConfiguration("brightness", Screen.renderDistance);
			}
			if(isMouseIn(screenWidth / 2, yOffset + sizeOfGame / 7 * 2 - fontAccent, brw / 2, h) && Mouse.getButton() == 0 && brithnessBuffer) {
				Screen.renderDistance -= 200;
				brithnessBuffer = false;
				EndlessCubes.config.saveConfiguration("brightness", Screen.renderDistance);
			}
			if(isMouseIn(screenWidth / 2 - tw / 2, yOffset + sizeOfGame / 7 * 4 - fontAccent, tw, h) && Mouse.getButton() == 1 && !transparencyBuffer) {
				transparencyBuffer = true;
			}
			if(isMouseIn(screenWidth / 2 - tw / 2, yOffset + sizeOfGame / 7 * 4 - fontAccent, tw, h) && Mouse.getButton() == 0 && transparencyBuffer) {
				EndlessCubes.transparency = !EndlessCubes.transparency;
				transparencyBuffer = false;
			}
		}
		if(state == 2) {
			if(EndlessCubes.gameOver) {
				if(Mouse.getButton() == 1) {
					bestBuffer = true;
				}
				if(Mouse.getButton() == 0 && bestBuffer) {
					state = 0;
					bestBuffer = false;
				}
			}
		}
		if(Mouse.getButton() == 0) {
			backBuffer = false;
			playBuffer = false;
			settingsBuffer = false;
			exitBuffer = false;
			bestBuffer = false;
		}
	}

	private boolean isMouseIn(int x, int y, int width, int height) {
		if(Mouse.getX() > x && Mouse.getX() < x + width && Mouse.getY() > y && Mouse.getY() < y + height) {
			return true;
		}		
		return false;
	}
	
	//state - 0 = startMenu
	//state - 1 = settings
	//state - 3 = bestScore screen
	
	public void render(Graphics g) {
		f = new Font("Ariel", 0, (int) (sizeOfGame * 0.15));
		g.setFont(f);
		h = g.getFontMetrics().getHeight();
		fontAccent = g.getFontMetrics().getAscent();
		g.setColor(new Color(col));
		if(state == 0) {
			pw = g.getFontMetrics().stringWidth(play);
			g.drawString(play, screenWidth / 2 - pw / 2, yOffset + sizeOfGame / 7 * 2);
			sw = g.getFontMetrics().stringWidth(settings);
			g.drawString(settings, screenWidth / 2 - sw / 2, yOffset + sizeOfGame / 7 * 4);
			xw = g.getFontMetrics().stringWidth(exit);
			g.drawString(exit, screenWidth / 2 - xw / 2, yOffset + sizeOfGame / 7 * 6);
		}
		if(state == 1) {
			bw = g.getFontMetrics().stringWidth(back);
			g.drawString(back, screenWidth / 2 - bw / 2, yOffset + sizeOfGame / 7 * 6);	
			brw = g.getFontMetrics().stringWidth(brightness);
			g.drawString(brightness, screenWidth / 2 - brw / 2, yOffset + sizeOfGame / 7 * 2);	
			tw = g.getFontMetrics().stringWidth(transparency);
			g.drawString(transparency, screenWidth / 2 - tw / 2, yOffset + sizeOfGame / 7 * 4);	
		}
		if(state == 2) {
			String best = "Best: " + EndlessCubes.bestScore;
			int bestw = g.getFontMetrics().stringWidth(best);
			g.drawString(best, screenWidth / 2 - bestw / 2, yOffset + sizeOfGame / 3 * 2);	
		}
	}
}
