import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


// main class opened o startup. Responsible for some game mechanics.
public class main extends JPanel implements KeyListener, MouseListener {
	static int [] frsize = {800, 800}; // TODO: Allow resizing and use frsize everywhere.
	Object fruit; // Snakes eat fruits here!
	Snake snake;
	int size = 8; // Size of the game cube.
	int [] Cscode = {39, 37}; // Store the keycodes used to move right/left. TODO: allow the user to change them and store them in a file.
	boolean [] Cs = new boolean[250]; // Store if a key is pressed, to differentiate actual key pressing from getting a keyPressed-signal.
	boolean moved = false;
	ArrayList<Object> border = new ArrayList<Object>();
	int [] score = new int[Assets.levels.length+Assets.difficulty.length*Assets.levels.length];
	int [] highscore = new int[Assets.levels.length+Assets.difficulty.length*Assets.levels.length];
	int n = 0;
	int difficulty = 0;
	int level = 0;
	String [] gamemodes = {"normal", "hard", "level 1", "level 2", "big cube"};
	JFrame frame;
	int [][][] levelData = { // TODO: store the level data in a file.
			{			//x, y
				{3, 1},
				{4, 1},
				{2, 2},
				{3, 2},
				{4, 2},
				{5, 2},
				{1, 3},
				{2, 3},
				{3, 3},
				{4, 3},
				{5, 3},
				{6, 3},
				{1, 4},
				{2, 4},
				{3, 4},
				{4, 4},
				{5, 4},
				{6, 4},
				{2, 5},
				{3, 5},
				{4, 5},
				{5, 5},
				{3, 6},
				{4, 6},
			},
			{
				{0, 0},
				{0, 1},
				{0, 2},
				{0, 5},
				{0, 6},
				{0, 7},
				{1, 0},
				{2, 0},
				{5, 0},
				{6, 0},
				{1, 7},
				{2, 7},
				{5, 7},
				{6, 7},
				{7, 0},
				{7, 1},
				{7, 2},
				{7, 5},
				{7, 6},
				{7, 7},
			}
	};
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(overlay == null) {
			if(!Cs[e.getKeyCode()]) {
				keyLatest(e.getKeyCode()); // Submit the latest new keypress to the game.
			}
		}
		Cs[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		Cs[e.getKeyCode()] = false;
		if(overlay == null) {
			if(e.getKeyCode() == 32) { // 32 = 'space'
				overlay = new MainMenu(false);
			}
		} else {
			overlay.keyReleased(e);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {
		if(overlay != null)
			overlay.mousePressed(e);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(overlay != null)
			overlay.mouseReleased(e);
	}
	
	// Reset the current level.
	public void initGame() {
		if(level == 3) {
			size = 16;
		} else {
			size = 8;
		}
		snake = new Snake(size, size/2, size/2, 0);
		border = new ArrayList<Object>();
		Assets.g3d = new Graphics3D(size);
		Assets.g3d.reload();

		// Generate the two levels.
		// TODO: make this more general.
		if(level == 1) {
			// GM 3 contains circular borders on two opposing faces.
			for(int i = 0; i < levelData[0].length; i++) {
				border.add(new Object(levelData[0][i][0], levelData[0][i][1], 4));
			}
			for(int i = 0; i < levelData[0].length; i++) {
				border.add(new Object(levelData[0][i][0], levelData[0][i][1], 5));
			}
		}
		else if(level == 2) {
			// GM 3 contains partially bordered sides. All sides are equal.
			for(int j = 0; j < 6; j++) {
				for(int i = 0; i < levelData[1].length; i++) {
					border.add(new Object(levelData[1][i][0], levelData[1][i][1], j));
				}
			}
		}
		fruit = new Object(size, snake, border);
	}
	
	// Reset the game environment.
	public void init() {
		overlay = new MainMenu(false);
		score = new int[Assets.levels.length+Assets.difficulty.length*Assets.levels.length];
		Assets.ax = 0;
		Assets.ay = 0;
		highscore = Assets.load();
		initGame();
	}
	
	long last = System.currentTimeMillis();
	long last2 = System.nanoTime();
	public void update() {
		boolean doIt = false; // Only update the rotation of the cube if it changed.
		long cur = System.nanoTime(); // Store the time to use the same time for all following calculations
		if(Cs[87]) { // 87 = 'w'
			Assets.ay -= (cur-last2)/300000000.0;
			doIt = true;
		}
		if(Cs[83]) { // 83 = 's'
			Assets.ay += (cur-last2)/300000000.0;
			doIt = true;
		}
		if(Cs[65]) { // 65 = 'a'
			Assets.ax += (cur-last2)/300000000.0;
			doIt = true;
		}
		if(Cs[68]) { // 68 = 'd'
			Assets.ax -= (cur-last2)/300000000.0;
			doIt = true;
		}
		Assets.g3d.reload();

		if(overlay == null) {
			if(System.currentTimeMillis() >= last+130-score[level+difficulty*Assets.levels.length]/4) { // Update the movement of the snake at a certain speed depending on the score.
				moved = false;
				last = System.currentTimeMillis();
				snake.move();
				if(snake.eat(fruit)) {
					if(difficulty == 1) {
						fruit.turnToBorder(); // Make a new border in hard mode every time the snake eats a fruit..
						border.add(fruit);
					}
					fruit = new Object(size, snake, border);
					score[level+difficulty*Assets.levels.length]++;
					if(score[level+difficulty*Assets.levels.length] > highscore[level+difficulty*Assets.levels.length]) {
						highscore[level+difficulty*Assets.levels.length]  = score[level+difficulty*Assets.levels.length];
					}
				}
				else if(snake.eatSelf()) {
					overlay = new MainMenu(true);
					if(highscore[level+difficulty*Assets.levels.length] > Assets.load()[level+difficulty*Assets.levels.length]) {
						Assets.save(highscore);
					}
				}
				if(snake.eatBorder(border)) {
					overlay = new MainMenu(true);
					if(highscore[level+difficulty*Assets.levels.length] > Assets.load()[level+difficulty*Assets.levels.length]) {
						Assets.save(highscore);
					}
				}
			}
		}
		last2 = cur;
	}
	
	// Create a new fruit.
	public void refruit() {
		fruit = new Object(size, snake, border);
	}
	
	public void keyLatest(int keyCode) {
		if(!moved) {
			if(keyCode == 37) {
				snake.changedir(-1);
				moved = true; // Only move once every update.
			}
			else if(keyCode == 39) {
				snake.changedir(1);
				moved = true; // Only move once every update.
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Assets.bgColor);
		g2d.fillRect(0, 0, 800, 900);
		g2d.fillRect(0, 0, 125, 40);
		g2d.setColor(Assets.textColor);
		g2d.drawString("Score: "+score[level+difficulty*Assets.levels.length]+"/"+highscore[level+difficulty*Assets.levels.length], 0, 16);
		g2d.drawString("Difficulty: "+Assets.difficulty[difficulty], 0, 36);
		Assets.g3d.drawCube(g2d);
		if(overlay != null) {
			overlay.paint(g2d);
		}
	}

	static MenuOverlay overlay = null;
	static main g;
	static void changeOverlay(MenuOverlay ov) {
		overlay = ov;
	}
	public static void main(String [] args) {
		g = new main();
		g.repaint();
		g.frame = new JFrame("snake");
		g.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		g.frame.setUndecorated(true);
		g.frame.setVisible(true);
		g.frame.setSize(800, 900);
		g.frame.add(g);
		g.frame.addKeyListener(g);
		g.frame.addMouseListener(g);
		g.init();
		while(true) {
			g.update();
			g.repaint();
			try {
				Thread.sleep(1); // Sleep 1 ms to spare processor power. This also reduces graphic bugs(flickering in graphics) that might be created due to the creation of a new Thread in repaint().
			}
			catch(Exception e) {}
		}
	}
}
