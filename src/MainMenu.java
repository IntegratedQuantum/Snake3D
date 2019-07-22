import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Graphics2D;

//TODO: Add Settings and level select.
public class MainMenu extends MenuOverlay {
	boolean isDead;
	long lastT = System.currentTimeMillis();
	int pressed = -1; // Shows which button is currently pressed by the mouse.
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
				main.changeOverlay(null);
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(!isDead && pressed == -1) {
			int x = e.getX();
			int y = e.getY();
			// Button 0: "Start"
			if(x >= 300 && x <= 500 && y >= 350 && y <= 450) {
				pressed = 0;
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(!isDead && pressed != -1) {
			int x = e.getX();
			int y = e.getY();
			switch(pressed) {
				case 0: // Button 0: "Start"
					if(x >= 300 && x <= 500 && y >= 350 && y <= 450) {
						main.overlay = null;
					} else {
						pressed = -1;
					}
					break;
			}
		}
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
		Assets.ax += (System.currentTimeMillis()-lastT)/600.0; // Make the cube spin when in menu.
		lastT = System.currentTimeMillis();
		if(isDead) {
			g2d.setColor(Assets.deathColor);
			g2d.setFont(new Font("Sanserif", 100, 100));
			g2d.drawString("Game", 300, 350);
			g2d.drawString("Over!", 300, 450);
		} else {
			// Button 0: "Start"
			g2d.setColor(Assets.bgColor);
			g2d.fillRect(300, 350, 200, 100);
			g2d.setColor(pressed == 0 ? Assets.selectColor : Assets.buttonColor);
			g2d.drawRect(300, 350, 200, 100);
			g2d.setFont(new Font("Sanserif", 50, 50));
			g2d.drawString("Start", 340, 415);
		}
	}
}