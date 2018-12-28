import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener{

	private static int mouseX = -1;
	private static int mouseY = -1;
	public static boolean pressed = false, dragging;
	
	public static int getX(){
		return mouseX;
	}
	
	public static int getY(){
		return mouseY;
	}
	
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX() - 3;
		mouseY = e.getY() - 26;
		FruitNinja.x.add(Mouse.getX());
		FruitNinja.y.add(Mouse.getY());
		if(FruitNinja.x.size() > 60) {
			FruitNinja.x.remove(0);
			FruitNinja.y.remove(0);
		}
		pressed = true;
		dragging = true;
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
		pressed = true;
	}

	public void mouseReleased(MouseEvent e) {
		pressed = false;
	}
	
}
