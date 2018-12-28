import java.awt.Graphics;

import javax.swing.JPanel;

public class Renderer extends JPanel{
	private static final long serialVersionUID = 4379041704872533563L;

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Puzzle.render(g);		
	}
	
}
