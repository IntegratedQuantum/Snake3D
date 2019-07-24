import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Graphics2D;

//TODO: Add Settings and level select.
public class MainMenu extends MenuOverlay {
	boolean isDead;
	long lastT = System.currentTimeMillis();
	long startT; // Time this overlay was created.
	int pressed = -1; // Shows which button is currently pressed by the mouse.
	int difficulty = 0;
	int level = 0;
	public MainMenu(boolean isDead) {
		this.isDead = isDead;
		startT = System.currentTimeMillis();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		if(isDead) {
			if(System.currentTimeMillis() - startT < 1000) // Don't leave the death screen for 1s to prevent the death screen from disappearing due to keys that were still pressed while the player died and released shortly afterwards. 
				return;
			if(e.getKeyCode() == 65 || e.getKeyCode() == 68 || e.getKeyCode() == 83 || e.getKeyCode() == 87) // Let the player still turn the cube in death screen.
				return;
			main.g.init();
			isDead = false;
			return;
		}
		if(e.getKeyCode() == 32) { // 32 = 'space'
			main.changeOverlay(null);
		}
		if(e.getKeyCode() == 71) { // 71 = 'G'
			difficulty++;
			if(difficulty == Assets.difficulty.length) {
				difficulty = 0;
			}
			main.g.difficulty = difficulty;
		}
		if(e.getKeyCode() == 76) { // 76 = 'L'
			level++;
			if(level == Assets.levels.length) {
				level = 0;
			}
			main.g.level = level;
			main.g.initGame();
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
			// Difficulty:
			if(x >= 0 && x <= 100) {
				for(int i = 0; i < Assets.difficulty.length; i++) {
					if(y >= 215+i*35 && y < 250+i*35) {
						pressed = i+1;
					}
				}
			}
			// Levels:
			if(x >= 700 && x <= 800) {
				for(int i = 0; i < Assets.levels.length; i++) {
					if(y >= 215+i*35 && y < 250+i*35) {
						pressed = i+1+Assets.difficulty.length;
					}
				}
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
					}
					break;
				case 1: // Button 1: "normal"
					if(x >= 0 && x <= 100 && y >= 215 && y <= 250) {
						difficulty = 0;
					}
					break;
				case 2: // Button 2: "hard"
					if(x >= 0 && x <= 100 && y >= 250 && y <= 285) {
						difficulty = 1;
					}
					break;
				case 3: // Button 3: "none"
					if(x >= 700 && x <= 800 && y >= 215 && y <= 250) {
						level = 0;
						main.g.level = level;
						main.g.initGame();
					}
					break;
				case 4: // Button 4: "level 1"
					if(x >= 700 && x <= 800 && y >= 250 && y <= 285) {
						level = 1;
						main.g.level = level;
						main.g.initGame();
					}
					break;
				case 5: // Button 5: "level 2"
					if(x >= 700 && x <= 800 && y >= 285 && y <= 320) {
						level = 2;
						main.g.level = level;
						main.g.initGame();
					}
					break;
				case 6: // Button 6: "big cube"
					if(x >= 700 && x <= 800 && y >= 320 && y <= 355) {
						level = 3;
						main.g.level = level;
						main.g.initGame();
					}
					break;
			}
			main.g.difficulty = difficulty;
			pressed = -1;
		}
	}
	@Override
	public void paint(Graphics2D g2d) {
		g2d.setColor(Assets.textColor);
		g2d.drawString("Press Space to Start.", 0, 56);
		g2d.drawString("Use ← and → to change direction.", 0, 76);
		g2d.drawString("Use g and l to cycle through gamemodes.", 0, 96);
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
			g2d.setColor(pressed == 0 ? Assets.pressColor : Assets.buttonColor);
			g2d.drawRect(300, 350, 200, 100);
			g2d.setFont(new Font("Sanserif", 50, 50));
			g2d.drawString("Start", 340, 415);
			// Difficulty selection:
			g2d.setColor(Assets.textColor);
			g2d.setFont(new Font("Sanserif", 20, 20));
			g2d.drawString("Difficulty:", 2, 200);
			for(int i = 0; i < Assets.difficulty.length; i++) {
				g2d.setColor(Assets.bgColor);
				g2d.fillRect(-1, 215+i*35, 101, 30);
				if(difficulty == i) {
					g2d.setColor(Assets.selectionColor);
				} else if(i+1 == pressed) {
					g2d.setColor(Assets.pressColor);
				} else {
					g2d.setColor(Assets.buttonColor);
				}
				g2d.drawRect(-1, 215+i*35, 101, 30);
				g2d.drawString(Assets.difficulty[i], 2, 235+35*i);
			}
			// Level selection:
			g2d.setColor(Assets.textColor);
			g2d.drawString("Level:", 703, 200);
			for(int i = 0; i < Assets.levels.length; i++) {
				g2d.setColor(Assets.bgColor);
				g2d.fillRect(700, 215+i*35, 101, 30);
				if(level == i) {
					g2d.setColor(Assets.selectionColor);
				} else if(i+1+Assets.difficulty.length == pressed) {
					g2d.setColor(Assets.pressColor);
				} else {
					g2d.setColor(Assets.buttonColor);
				}
				g2d.drawRect(700, 215+i*35, 101, 30);
				g2d.drawString(Assets.levels[i], 703, 235+35*i);
			}
		}
	}
}