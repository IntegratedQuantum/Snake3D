import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;

// superclass to everything that happens in the menu.
public abstract class MenuOverlay implements MouseListener {
	public abstract void paint(Graphics2D g);
	public abstract void keyReleased(KeyEvent e); // Only keyReleased events are useful in menu.

	// Those functions of MouseListener are usually not required and therefor already implemented here to avoid redundancy.
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
}