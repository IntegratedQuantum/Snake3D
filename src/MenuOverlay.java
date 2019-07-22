import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;

// superclass to everything that happens in the menu.
public abstract class MenuOverlay {
	public abstract void paint(Graphics2D g);
	// Only keyReleased, mousePressed and mouseReleased events are useful in menu.
	public abstract void keyReleased(KeyEvent e);
	public abstract void mousePressed(MouseEvent e);
	public abstract void mouseReleased(MouseEvent e);
}