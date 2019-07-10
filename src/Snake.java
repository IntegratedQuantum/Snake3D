import java.awt.Color;
import java.util.List;

public class Snake {
	int [][] seg = new int [50][3]; // I don't expect you to get a higher score than 50. TODO: Allow higher scores without crashing.
	int length = 3; // Start with a length of 3.
	Color color = new Color(0, 255, 0); // TODO: make this changeable from a menu.
	private int size;
	private int [][] diruse = { // direction is encoded as number from 0 to 3. This is the 'encoder'.
		{0, -1},
		{1, 0},
		{0, 1},
		{-1, 0}
	};
	private int dir;

	public Snake(int size, int x, int y, int l) {
		for(int i = 0; i < 50; i++) {
			for(int j = 0; j < 3; j++) {
				seg[i][j] = 1000; // Give non-existent segments an arbitrary high default value.
			}
		}
		this.size = size-1; // Size of the cube.
		seg[0][0] = x;
		seg[0][1] = y;
		seg[0][2] = l; // l is a value between 0 and 5 representing the face this segment is on.
		dir = 0;
	}
	
	// Changes the internal direction based on a relative direction(-1=←, 0=↑, 1=→).
	public void changedir(int dir) {
		this.dir += dir;
		if(this.dir > 3) {
			this.dir = 0;
		}
		if(this.dir < 0) {
			this.dir = 3;
		}
	}
	
	// Moves the whole snake one block into the direction given by dir.
	public void move() {
		try {
			// Remove the end of the tail from the list to paint by g3d.
			Assets.g3d.setColor(seg[length][0], seg[length][1], seg[length][2], Color.BLACK);
		}catch(Exception ignored){}
		// Moving the tail.
		for(int i = length; i > 0; i--) {
			seg[i][0] = seg[i-1][0];
			seg[i][1] = seg[i-1][1];
			seg[i][2] = seg[i-1][2];
		}
		// Moving the head.
		seg[0][0] += diruse[dir][0];
		seg[0][1] += diruse[dir][1];
		// Determines to which face the head goes next if it changes the face.
		// TODO: Simplify this and make it easier to understand.
		if(seg[0][2] < 4) {
			if(seg[0][1] < 0) {
				seg[0][1] = size;
				seg[0][2]--;
				if(seg[0][2] < 0) {
					seg[0][2] = 3;
				}
			}
			else if(seg[0][1] > size) {
				seg[0][1] = 0;
				seg[0][2]++;
				if(seg[0][2] > 3) {
					seg[0][2] = 0;
				}
			}
			if(seg[0][0] > size) {
				switch(seg[0][2]) {
				case 0:
					seg[0][0] = size;
					seg[0][1] = size-seg[0][1];
					dir = 3;
					break;
				case 1:
					seg[0][0] = size-seg[0][1];
					seg[0][1] = 0;
					dir = 2;
					break;
				case 2:
					seg[0][0] = 0;
					break;
				case 3:
					seg[0][0] = seg[0][1];
					seg[0][1] = size;
					dir = 0;
				}
				seg[0][2] = 4;
			}
			else if(seg[0][0] < 0) {
				switch(seg[0][2]) {
				case 0:
					seg[0][0] = size;
					seg[0][1] = seg[0][1];
					dir = 3;
					break;
				case 1:
					seg[0][0] = size-seg[0][1];
					seg[0][1] = size;
					dir = 0;
					break;
				case 2:
					seg[0][0] = 0;
					seg[0][1] = size-seg[0][1];
					dir = 1;
					break;
				case 3:
					seg[0][0] = seg[0][1];
					seg[0][1] = 0;
					dir = 2;
				}
				seg[0][2] = 5;
			}
		}
		else if(seg[0][2] == 4) {
			if(seg[0][0] < 0) {
				seg[0][2] = 2;
				seg[0][0] = size;
			}
			else if(seg[0][0] > size) {
				seg[0][2] = 0;
				seg[0][0] = size;
				seg[0][1] = size-seg[0][1];
				dir = 3;
			}
			if(seg[0][1] < 0) {
				seg[0][2] = 1;
				seg[0][1] = size-seg[0][0];
				seg[0][0] = size;
				dir = 3;
			}
			else if(seg[0][1] > size) {
				seg[0][2] = 3;
				seg[0][1] = seg[0][0];
				seg[0][0] = size;
				dir = 3;
			}
		}
		else {
			if(seg[0][0] < 0) {
				seg[0][2] = 2;
				seg[0][0] = 0;
				seg[0][1] = size-seg[0][1];
				dir = 1;
			}
			else if(seg[0][0] > size) {
				seg[0][2] = 0;
				seg[0][0] = 0;
			}
			if(seg[0][1] < 0) {
				seg[0][2] = 3;
				seg[0][1] = seg[0][0];
				seg[0][0] = 0;
				dir = 1;
			}
			else if(seg[0][1] > size) {
				seg[0][2] = 1;
				seg[0][1] = size-seg[0][0];
				seg[0][0] = 0;
				dir = 1;
			}
		}
		// Tell g3d where to paint the new head.
		Assets.g3d.setColor(seg[0][0], seg[0][1], seg[0][2], Color.GREEN);
	}
	
	// Determines if the object is eaten.
	public boolean eat(Object o) {
		if(o.isEaten(seg[0][0], seg[0][1], seg[0][2])) {
			length++;
			return true;
		}
		return false;
	}
	
	// Determines if the snake bites its own tail.
	public boolean eatSelf() {
		for(int i = 1; i < length; i++) {
			if(seg[0][0] == seg[i][0] && seg[0][1] == seg[i][1] && seg[0][2] == seg[i][2]) {
				return true;
			}
		}
		return false;
	}
	
	// Determines if an object from the list(currently only used by borders) is eaten.
	public boolean eatBorder(List<Object> o) {
		for(int i = 0; i < o.size(); i++) {
			if(o.get(i).isEaten(seg[0][0], seg[0][1], seg[0][2])) {
				return true;
			}
		}
		return false;
	}
}
