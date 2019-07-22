import java.util.List;

public class Snake {
	LinkedVector head = null; // Use similar structure to a deque.
	LinkedVector tail = null;
	int curLength; // Actual length of the snake.
	int length = 3; // Start with a length of 3. Desired length of the snake.
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
		head = tail = new LinkedVector(x, y, l);
		curLength = 1;
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
			Assets.g3d.setColor(tail.x, tail.y, tail.z, Assets.bgColor);
		}catch(Exception ignored){}
		// Remove the tail if the desired length is reached:
		if(curLength == length) {
			tail = tail.previous;
			tail.unlink();
		} else {
			curLength++;
		}
		// 'Moving' the head.
		head = new LinkedVector(head.x+diruse[dir][0], head.y+diruse[dir][1], head.z, head);
		// Determines to which face the head goes next if it changes the face.
		// TODO: Simplify this and make it easier to understand.
		if(head.z < 4) {
			if(head.y < 0) {
				head.y = size;
				head.z--;
				if(head.z < 0) {
					head.z = 3;
				}
			}
			else if(head.y > size) {
				head.y = 0;
				head.z++;
				if(head.z > 3) {
					head.z = 0;
				}
			}
			if(head.x > size) {
				switch(head.z) {
				case 0:
					head.x = size;
					head.y = size-head.y;
					dir = 3;
					break;
				case 1:
					head.x = size-head.y;
					head.y = 0;
					dir = 2;
					break;
				case 2:
					head.x = 0;
					break;
				case 3:
					head.x = head.y;
					head.y = size;
					dir = 0;
				}
				head.z = 4;
			}
			else if(head.x < 0) {
				switch(head.z) {
				case 0:
					head.x = size;
					head.y = head.y;
					dir = 3;
					break;
				case 1:
					head.x = size-head.y;
					head.y = size;
					dir = 0;
					break;
				case 2:
					head.x = 0;
					head.y = size-head.y;
					dir = 1;
					break;
				case 3:
					head.x = head.y;
					head.y = 0;
					dir = 2;
				}
				head.z = 5;
			}
		}
		else if(head.z == 4) {
			if(head.x < 0) {
				head.z = 2;
				head.x = size;
			}
			else if(head.x > size) {
				head.z = 0;
				head.x = size;
				head.y = size-head.y;
				dir = 3;
			}
			if(head.y < 0) {
				head.z = 1;
				head.y = size-head.x;
				head.x = size;
				dir = 3;
			}
			else if(head.y > size) {
				head.z = 3;
				head.y = head.x;
				head.x = size;
				dir = 3;
			}
		}
		else {
			if(head.x < 0) {
				head.z = 2;
				head.x = 0;
				head.y = size-head.y;
				dir = 1;
			}
			else if(head.x > size) {
				head.z = 0;
				head.x = 0;
			}
			if(head.y < 0) {
				head.z = 3;
				head.y = head.x;
				head.x = 0;
				dir = 1;
			}
			else if(head.y > size) {
				head.z = 1;
				head.y = size-head.x;
				head.x = 0;
				dir = 1;
			}
		}
		// Tell g3d where to paint the new head.
		Assets.g3d.setColor(head.x, head.y, head.z, Assets.snakeColor);
	}
	
	// Determines if the object is eaten.
	public boolean eat(Object o) {
		if(o.isEaten(head.x, head.y, head.z)) {
			length++;
			return true;
		}
		return false;
	}
	
	// Determines if the snake bites its own tail.
	public boolean eatSelf() {
		if(curLength >= 4) { // There is no possibility the snake can kill itself with a length less than 4.
			for(LinkedVector seg = head.next; seg.next != null; seg = seg.next) {
				if(head.x == seg.x && head.y == seg.y && head.z == seg.z) {
					return true;
				}
			}
		}
		return false;
	}
	
	// Determines if an object from the list(currently only used by borders) is eaten.
	public boolean eatBorder(List<Object> o) {
		for(int i = 0; i < o.size(); i++) {
			if(o.get(i).isEaten(head.x, head.y, head.z)) {
				return true;
			}
		}
		return false;
	}
}
