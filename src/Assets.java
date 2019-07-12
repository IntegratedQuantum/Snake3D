import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;


// Stores the 3D-graphics object and is responsible for file-saving/-loading.
class Assets {
	static Graphics3D g3d;
	static void save(int [] score) {
		for(int k = 0; k < 4; k++) {
			String s = score[k]+"";
			try {
				FileWriter f = new FileWriter("score"+k+".snake3D");
				for(int i = 0; i < s.length(); i++) {
					f.write(s.charAt(i));
				}
				f.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	static int [] load() {
		FileReader fr;
		String score = "0";
		int ret[] = new int[4];
		for(int i = 0; i < 4; i++) {
			try {
				fr = new FileReader("score"+i+".snake3D");
				BufferedReader br = new BufferedReader(fr);
				score = br.readLine();
				br.close();
				fr.close();
				ret[i] = Integer.parseInt(score);
				score = "0";
			} catch (Exception e) {
				save(new int[]{0, 0, 0, 0});
			}
		}
		return ret;
	}
}
