package flappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener{
	
	public static FlappyBird flappyBird;
	public final int WIDTH = 800, HEIGHT = 740;
	public Renderer renderer;
	public Rectangle bird;
	public int ticks, yMotion, score;
	public boolean gameOver, started = false;
	public ArrayList<Rectangle> columns;
	public Random rand;

	
	public FlappyBird(){
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20,this);
		
		renderer = new Renderer();
		rand = new Random();
		jframe.add(renderer);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.setTitle("Flappy Bird");
		//jframe.setResizable(false);
		jframe.setVisible(true);
		jframe.addMouseListener(this);
		jframe.addKeyListener(this);

		bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
	}
	
	public void addColumn(boolean start){
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);
		if(start){
		columns.add(new Rectangle(WIDTH + width + columns.size() * 300, 
				HEIGHT - height - 120, width, height));
		columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 
				0, width, HEIGHT - height - space));
		}else{
			columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600,
					HEIGHT - height - 120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() - 1).x, 
					0, width, HEIGHT - height - space));
		}
	}
	
	public void paintColumn(Graphics g, Rectangle column){
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	
	public void jump(){
		if(gameOver){
			bird = new Rectangle(WIDTH/2 - 10, HEIGHT/2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
		
			gameOver = false;
		}
		if(!started){
			started = true;
		}else if(!gameOver){
			if(yMotion>0){
				yMotion = 0;
			}
			yMotion -= 10;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		int speed = 10;
		ticks++;
		if(started){
			for(int i = 0; i < columns.size(); i++){
				Rectangle column = columns.get(i);
				column.x -= speed;
			}
			if(ticks%2 == 0 && yMotion < 15){
				yMotion += 2;
			}
			for(int i = 0; i < columns.size(); i++){
				Rectangle column = columns.get(i);
				if(column.x + column.width < 0){
					columns.remove(column);
					if(column.y == 0){
						addColumn(false);
					}
				}
			}
			bird.y += yMotion;
			for(Rectangle column : columns){
				if(column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 -10 &&
					bird.x + bird.width / 2 < column.x + column.width / 2 + 10){
					score++;
				}
				if(column.intersects(bird)){
					gameOver = true;
					if(bird.x <= column.x){
						bird.x = column.x - bird.width;
					}else{
						if(column.y != 0){
							bird.y = column.y -bird.height;
						}else if(bird.y < column.height){
							bird.y = column.height;
						}
					}
				}
			}
			if(bird.y > HEIGHT - 120 -bird.height || bird.y < 0){
				gameOver = true;
			}
			if(bird.y + yMotion >= HEIGHT -120){
				bird.y = HEIGHT - 120- bird.height;
			}
		}
		renderer.repaint();
	}

	public void repaint(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.ORANGE);
		g.fillRect(0, HEIGHT-120, WIDTH, 150);

		g.setColor(Color.GREEN);
		g.fillRect(0, HEIGHT-120, WIDTH, 20);
		
		g.setColor(Color.RED);
		g.fillRect(bird.x, bird.y, bird.height, bird.width);
		
		for(Rectangle column : columns){
			paintColumn(g, column);
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Ariel",1,100));
		
		if(!started){
			g.drawString("Click to start!", 75, HEIGHT/2 - 50);
		}
		
		if(gameOver){
			g.drawString("Game Over!", 100, HEIGHT/2 - 50);
		}
		String scoreString = String.valueOf(score);
		int scoreWidth = g.getFontMetrics().stringWidth(scoreString);
		
		if(!gameOver && started){
			g.drawString(scoreString,WIDTH / 2 - scoreWidth/2,100);
		}
	}
	
	public static void main(String[] args){
		flappyBird = new FlappyBird();
		
	}

	public void mouseClicked(MouseEvent arg0) {
		jump();
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent arg0) {
		
	}

	public void mouseReleased(MouseEvent arg0) {
		
	}

	public void keyPressed(KeyEvent arg0) {
		
	}

	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_SPACE){
			jump();
		}
	}

	public void keyTyped(KeyEvent arg0) {
		
	}


}
