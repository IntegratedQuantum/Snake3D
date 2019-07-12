import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
public class main extends JPanel implements KeyListener {
	static int [] frsize = {800, 800}; // TODO: Allow resizing and use frsize everywhere.
	Object fruit; // Snakes eat fruits here!
	Snake snake;
	boolean pause = false;
	int size = 8; // Size of the game cube.
	int [] Cscode = {39, 37}; // Store the keycodes used to move right/left. TODO: allow the user to change them and store them in a file.
	boolean [] Cs = new boolean[250]; // Store if a key is pressed, to differentiate actual key pressing from getting a keyPressed-signal.
	boolean death = false;
	boolean moved = false;
	ArrayList<Object> border = new ArrayList<Object>();
	double ax;
	double ay;
	int [] score = new int[4];
	int [] highscore = new int[4];
	int n = 0;
	int gamemode;
	String [] gamemodes = {"normal", "hard", "level 1", "level 2"}; // TODO: Allow the levels to be played in hard-mode.
	JFrame frame;
	int [][][] level = { // TODO: store the level data in a file.
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
		if(!pause) {
			if(!Cs[e.getKeyCode()]) {
				keyLatest(e.getKeyCode()); // Submit the latest new keypress to the game.
			}
		}
		Cs[e.getKeyCode()] = true;
		if(pause || death) {
			if(e.getKeyCode() == 71) { // 71 = 'g'
				gamemode ++;
				if(gamemode == gamemodes.length) {
					gamemode = 0;
				}
				initGame();
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		Cs[e.getKeyCode()] = false;
		if(e.getKeyCode() == 32) { // 32 = 'space'
			if(pause) {
				pause = false;
			}
			else {
				pause = true;
			}
			if(death) {
				death = false;
				score[gamemode] = 0;
				init();
			}
		}
	}
	
	// Reset the current level.
	public void initGame() {
		border = new ArrayList<Object>();
		Assets.g3d = new Graphics3D(size);
		Assets.g3d.reload(0, 0);
		
		// Generate the two levels.
		// TODO: make this more general.
		if(gamemode == 2) {
			// GM 3 contains circular borders on two opposing faces.
			for(int i = 0; i < level[0].length; i++) {
				border.add(new Object(level[0][i][0], level[0][i][1], 4));
			}
			for(int i = 0; i < level[0].length; i++) {
				border.add(new Object(level[0][i][0], level[0][i][1], 5));
			}
		}
		else if(gamemode == 3) {
			// GM 3 contains partially bordered sides. All sides are equal.
			for(int j = 0; j < 6; j++) {
				for(int i = 0; i < level[1].length; i++) {
					border.add(new Object(level[1][i][0], level[1][i][1], j));
				}
			}
		}
		fruit = new Object(size, snake, border);
	}
	
	// Reset the game environment.
	public void init() {
		snake = new Snake(size, size/2, size/2, 0);
		pause = true;
		ax = 0;
		ay = 0;
		highscore = Assets.load();
		initGame();
	}
	
	long last = System.currentTimeMillis();
	long last2 = System.nanoTime();
	public void update() {
		boolean doIt = false; // Only update the rotation of the cube if it changed.
		long cur = System.nanoTime(); // Store the time to use the same time for all following calculations
		if(Cs[87]) { // 87 = 'w'
			ay -= (cur-last2)/300000000.0;
			doIt = true;
		}
		if(Cs[83]) { // 83 = 's'
			ay += (cur-last2)/300000000.0;
			doIt = true;
		}
		if(Cs[65]) { // 65 = 'a'
			ax += (cur-last2)/300000000.0;
			doIt = true;
		}
		if(Cs[68]) { // 68 = 'd'
			ax -= (cur-last2)/300000000.0;
			doIt = true;
		}
		if(doIt) {
			Assets.g3d.reload(ax, ay);
		}
		if(System.currentTimeMillis() >= last+130-score[gamemode]/4) { // Update the movement of the snake at a certain speed depending on the score.
			moved = false;
			last = System.currentTimeMillis();
			snake.move();
			if(snake.eat(fruit)) {
				if(gamemode == 1) {
					fruit.turnToBorder(); // Make a new border in hard mode every time the snake eats a fruit..
					border.add(fruit);
				}
				fruit = new Object(size, snake, border);
				score[gamemode]++;
				if(score[gamemode] > highscore[gamemode]) {
					highscore[gamemode]  = score[gamemode];
				}
			}
			else if(snake.eatSelf()) {
				death = true;
				if(highscore[gamemode] > Assets.load()[gamemode]) {
					Assets.save(highscore);
				}
			}
			if(snake.eatBorder(border)) {
				death = true;
				if(highscore[gamemode] > Assets.load()[gamemode]) {
					Assets.save(highscore);
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
	
	long lastdeath = System.currentTimeMillis();
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 800, 900);
		g2d.fillRect(0, 0, 125, 40);
		g2d.setColor(Color.GREEN);
		g2d.drawString("Score: "+score[gamemode]+"/"+highscore[gamemode], 0, 16);
		g2d.drawString("Gamemode: "+gamemodes[gamemode], 0, 36);
		if(pause) {
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 40, 230, 60);
			g2d.setColor(Color.GREEN);
			g2d.drawString("Press Space to Start or Stop.", 0, 56);
			g2d.drawString("Use ← and → to change direction.", 0, 76);
			g2d.drawString("Use g to cycle between gamemodes.", 0, 96);
		}
		Assets.g3d.drawCube(g2d);
		if(death) {
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 40, 230, 40);
			g2d.setColor(Color.GREEN);
			g2d.drawString("Press Space to restart.", 0, 56);
			g2d.drawString("Use g to cycle between gamemodes.", 0, 76);
			if(System.currentTimeMillis()-lastdeath > 1000) {
				lastdeath = System.currentTimeMillis();
			}
			ax += (System.currentTimeMillis()-lastdeath)/500.0; // Make the cube spin after death.
			lastdeath = System.currentTimeMillis();
			Assets.g3d.reload(ax, ay);
			g2d.setColor(new Color(100, 0, 0));
			g2d.setFont(new Font("Sanserif", 100, 100));
			g2d.drawString("Game", 300, 350);
			g2d.drawString("Over!", 300, 450);
		}
	}
	
	public static void main(String [] args) {
		main g = new main();
		g.repaint();
		g.frame = new JFrame("snake");
		g.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		g.frame.setUndecorated(true);
		g.frame.setVisible(true);
		g.frame.setSize(800, 900);
		g.frame.add(g);
		g.frame.addKeyListener(g);
		g.init();
		while(true) {
			if(!g.pause && !g.death) {
				g.update();
			}
			g.repaint();
			try {
				Thread.sleep(1); // Sleep 1 ms to spare processor power. This also reduces graphic bugs(flickering in graphics) that might be created due to the creation of a new Thread in repaint().
			}
			catch(Exception e) {}
		}
	}
}
