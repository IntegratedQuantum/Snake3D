import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;


// Stores the 3D-graphics object and is responsible for file-saving/-loading.
class Assets {
	static Graphics3D g3d;
	static double ax = 0;
	static double ay = 0;

	// Colors: TODO: Make them changeable from the menu.
	static Color borderColor = new Color(250, 250, 250);
	static Color bgColor = Color.BLACK;
	static Color lineColor = Color.WHITE;
	static Color snakeColor = Color.GREEN;
	static Color textColor = Color.GREEN; // Color used for the text that is displayed in the top left corner.
	static Color buttonColor = new Color(191, 255, 0);
	static Color pressColor = new Color(255, 191, 0);
	static Color selectionColor = new Color(255, 127, 0);
	static Color deathColor = new Color(100, 0, 0); // Color of the "Game Over!" text.

	// Text for levels/difficulty.
	static String [] difficulty = {"normal", "hard"};
	static String [] levels = {"none", "level 1", "level 2", "big cube"};

	// save/load highscore
	static void save(int [] score) {
		String s = "";
		for(int k = 0; k < levels.length+difficulty.length*levels.length; k++) {
			s += score[k]+"\n";
		}
		try {
			FileWriter f = new FileWriter("score.snake3D");
			for(int i = 0; i < s.length(); i++) {
				f.write(s.charAt(i));
			}
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static int [] load() {
		int ret[] = new int[levels.length+difficulty.length*levels.length];
		try {
			FileReader fr = new FileReader("score.snake3D");
			BufferedReader br = new BufferedReader(fr);
			for(int i = 0; i < levels.length+difficulty.length*levels.length; i++) {
				String score = br.readLine();
				ret[i] = Integer.parseInt(score);
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
