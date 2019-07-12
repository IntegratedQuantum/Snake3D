import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;


// Stores the 3D-graphics object and is responsible for file-saving/-loading.
class Assets {
	static Graphics3D g3d;
	static void save(int [] score) {
		String s = "";
		for(int k = 0; k < 4; k++) {
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
		int ret[] = new int[4];
		try {
			FileReader fr = new FileReader("score.snake3D");
			BufferedReader br = new BufferedReader(fr);
			for(int i = 0; i < 4; i++) {
				String score = br.readLine();
				ret[i] = Integer.parseInt(score);
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			save(new int[]{0, 0, 0, 0});
		}
		return ret;
	}
}
