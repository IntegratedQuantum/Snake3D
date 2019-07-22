import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Graphics2D;

public class MainMenu extends MenuOverlay {
	boolean isDead;
	long lastT = System.currentTimeMillis();
	public MainMenu(boolean isDead) {
		this.isDead = isDead;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == 32) { // 32 = 'space'
			if(isDead) {
				main.g.init();
				isDead = false;
			} else {
				main.overlay = null;
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {

	}
	@Override
	public void mouseReleased(MouseEvent e) {

	}
	@Override
	public void paint(Graphics2D g2d) {
		g2d.setColor(Assets.textColor);
		g2d.drawString("Press Space to Start.", 0, 56);
		g2d.drawString("Use ← and → to change direction.", 0, 76);
		g2d.drawString("Use g to cycle between gamemodes.", 0, 96);
		if(System.currentTimeMillis()-lastT > 1000) {
			lastT = System.currentTimeMillis();
		}
		Assets.ax += (System.currentTimeMillis()-lastT)/500.0; // Make the cube spin when in menu.
		lastT = System.currentTimeMillis();
		if(isDead) {
			g2d.setColor(Assets.deathColor);
			g2d.setFont(new Font("Sanserif", 100, 100));
			g2d.drawString("Game", 300, 350);
			g2d.drawString("Over!", 300, 450);
		}
	}
}