import java.util.List;

public class Snake {
	Vector [] seg = new Vector [8]; // Uses the same functionality as a List(see Snake.growArray()), but can be handled like an array.
	int length = 3; // Start with a length of 3.
	private int size;
	private int [][] diruse = { // direction is encoded as number from 0 to 3. This is the 'encoder'.
		{0, -1},
		{1, 0},
		{0, 1},
		{-1, 0}
	};
	private int dir;

	public Snake(int size, int x, int y, int l) {
		this.size = size-1; // Size of the cube.
		for(int i = 0; i < seg.length; i++) {
			seg[i] = new Vector(1000, 1000, 1000);// Give not-yet-existent segments an arbitrary high default value.
		}
		seg[0].x = x;
		seg[0].y = y;
		seg[0].z = l; // l is a value between 0 and 5 representing the face this segment is on.
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
			Assets.g3d.setColor(seg[length].x, seg[length].y, seg[length].z, Assets.bgColor);
		}catch(Exception ignored){}
		// Moving the tail.
		for(int i = length; i > 0; i--) {
			seg[i].x = seg[i-1].x;
			seg[i].y = seg[i-1].y;
			seg[i].z = seg[i-1].z;
		}
		// Moving the head.
		seg[0].x += diruse[dir][0];
		seg[0].y += diruse[dir][1];
		// Determines to which face the head goes next if it changes the face.
		// TODO: Simplify this and make it easier to understand.
		if(seg[0].z < 4) {
			if(seg[0].y < 0) {
				seg[0].y = size;
				seg[0].z--;
				if(seg[0].z < 0) {
					seg[0].z = 3;
				}
			}
			else if(seg[0].y > size) {
				seg[0].y = 0;
				seg[0].z++;
				if(seg[0].z > 3) {
					seg[0].z = 0;
				}
			}
			if(seg[0].x > size) {
				switch(seg[0].z) {
				case 0:
					seg[0].x = size;
					seg[0].y = size-seg[0].y;
					dir = 3;
					break;
				case 1:
					seg[0].x = size-seg[0].y;
					seg[0].y = 0;
					dir = 2;
					break;
				case 2:
					seg[0].x = 0;
					break;
				case 3:
					seg[0].x = seg[0].y;
					seg[0].y = size;
					dir = 0;
				}
				seg[0].z = 4;
			}
			else if(seg[0].x < 0) {
				switch(seg[0].z) {
				case 0:
					seg[0].x = size;
					seg[0].y = seg[0].y;
					dir = 3;
					break;
				case 1:
					seg[0].x = size-seg[0].y;
					seg[0].y = size;
					dir = 0;
					break;
				case 2:
					seg[0].x = 0;
					seg[0].y = size-seg[0].y;
					dir = 1;
					break;
				case 3:
					seg[0].x = seg[0].y;
					seg[0].y = 0;
					dir = 2;
				}
				seg[0].z = 5;
			}
		}
		else if(seg[0].z == 4) {
			if(seg[0].x < 0) {
				seg[0].z = 2;
				seg[0].x = size;
			}
			else if(seg[0].x > size) {
				seg[0].z = 0;
				seg[0].x = size;
				seg[0].y = size-seg[0].y;
				dir = 3;
			}
			if(seg[0].y < 0) {
				seg[0].z = 1;
				seg[0].y = size-seg[0].x;
				seg[0].x = size;
				dir = 3;
			}
			else if(seg[0].y > size) {
				seg[0].z = 3;
				seg[0].y = seg[0].x;
				seg[0].x = size;
				dir = 3;
			}
		}
		else {
			if(seg[0].x < 0) {
				seg[0].z = 2;
				seg[0].x = 0;
				seg[0].y = size-seg[0].y;
				dir = 1;
			}
			else if(seg[0].x > size) {
				seg[0].z = 0;
				seg[0].x = 0;
			}
			if(seg[0].y < 0) {
				seg[0].z = 3;
				seg[0].y = seg[0].x;
				seg[0].x = 0;
				dir = 1;
			}
			else if(seg[0].y > size) {
				seg[0].z = 1;
				seg[0].y = size-seg[0].x;
				seg[0].x = 0;
				dir = 1;
			}
		}
		// Tell g3d where to paint the new head.
		Assets.g3d.setColor(seg[0].x, seg[0].y, seg[0].z, Assets.snakeColor);
	}
	
	// Determines if the object is eaten.
	public boolean eat(Object o) {
		if(o.isEaten(seg[0].x, seg[0].y, seg[0].z)) {
			length++;
			if(length == seg.length) {
				growArray();
			}
			return true;
		}
		return false;
	}
	
	// Determines if the snake bites its own tail.
	public boolean eatSelf() {
		for(int i = 1; i < length; i++) {
			if(seg[0].x == seg[i].x && seg[0].y == seg[i].y && seg[0].z == seg[i].z) {
				return true;
			}
		}
		return false;
	}
	
	// Determines if an object from the list(currently only used by borders) is eaten.
	public boolean eatBorder(List<Object> o) {
		for(int i = 0; i < o.size(); i++) {
			if(o.get(i).isEaten(seg[0].x, seg[0].y, seg[0].z)) {
				return true;
			}
		}
		return false;
	}

	// Increases the size of the seg array by the factor 2 and fills the empty places with new Vectors of arbitrary high value.
	// Decreasing isn't necessary, because Snake gets newly instantiated on the start of each game.
	public void growArray() {
		Vector[] old = seg;
		seg = new Vector[old.length*2];
		System.arraycopy(old, 0, seg, 0, old.length);
		for(int i = old.length; i < seg.length; i++) {
			seg[i] = new Vector(1000, 1000, 1000);
		}
	}
}
