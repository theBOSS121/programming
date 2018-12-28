import java.awt.Graphics;

import javax.swing.JPanel;

public class Renderer extends JPanel{
	private static final long serialVersionUID = -5606690746521300613L;

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Asteroids.render(g);
	}
	
}
