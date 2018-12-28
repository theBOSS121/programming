import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Mouse implements MouseListener, MouseMotionListener{

	private static int mouseX = -1;
	private static int mouseY = -1;
	public static boolean dragging = false;
	
	public static int getX(){
		return mouseX;
	}
	
	public static int getY(){
		return mouseY;
	}
	
	public void mouseDragged(MouseEvent e) {
		dragging = true;
		mouseX = e.getX() - 3;
		mouseY = e.getY() - 26;
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		mouseX = e.getX() - 3;
		mouseY = e.getY() - 26;
		dragging = true;
		if(e.getButton() == 3) {
			Puzzle.rects[0] = new Rectangle(0, 0, 150, 150);
			Puzzle.rects[1] = new Rectangle(150, 0, 150, 150);
			Puzzle.rects[2] = new Rectangle(300, 0, 150, 150);
			Puzzle.rects[3] = new Rectangle(450, 0, 150, 150);
			Puzzle.rects[4] = new Rectangle(0, 150, 150, 150);
			Puzzle.rects[5] = new Rectangle(150, 150, 150, 150);
			Puzzle.rects[6] = new Rectangle(300, 150, 150, 150);
			Puzzle.rects[7] = new Rectangle(450, 150, 150, 150);
			Puzzle.rects[8] = new Rectangle(0, 300, 150, 150);
			Puzzle.rects[9] = new Rectangle(150, 300, 150, 150);
			Puzzle.rects[10] = new Rectangle(300, 300, 150, 150);
			Puzzle.rects[11] = new Rectangle(450, 300, 150, 150);
			Puzzle.rects[12] = new Rectangle(0, 450, 150, 150);
			Puzzle.rects[13] = new Rectangle(150, 450, 150, 150);
			Puzzle.rects[14] = new Rectangle(300, 450, 150, 150);
			Puzzle.started = false;
		}
	}

	public void mouseReleased(MouseEvent e) {
		dragging = false;
	}
	
}
