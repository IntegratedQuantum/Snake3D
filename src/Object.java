import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

public class Object extends JFrame {
	int [] pos = new int [3];
	boolean eatable;
	Color color;

	// Create an object at a random position.
	public Object(int size, Snake snake, List<Object> o) {
		eatable = true;
		do {
			pos[0] = (int)(Math.random()*size);
			pos[1] = (int)(Math.random()*size);
			pos[2] = (int)(Math.random()*5);
		}while(onSnake(snake) || onBlock(o)); // Don't put on object on another object/on the snake.
		// Tell g3d to paint the object with a random color(not matching background or border color).
		Assets.g3d.setColor(pos[0], pos[1], pos[2], new Color(55+(int)(Math.random()*150), 55+(int)(Math.random()*150), 55+(int)(Math.random()*150)));
	}
	
	// Create an object at a certain position. Only used for level creation → no check if onSnake/onBlock.
	public Object(int x, int y, int l) {
		pos[0] = x;
		pos[1] = y;
		pos[2] = l;
		color = new Color(250, 250, 250); // color for borders.
		eatable = false;
		// Tell g3d to paint the object.
		Assets.g3d.setColor(pos[0], pos[1], pos[2], color);
	}
	
	// Determines if this object shares the location with an object from the list.
	public boolean onBlock(List<Object> o) {
		boolean on = false;
		for(int i = 0; i < o.size(); i++) {
			if(pos[0] == o.get(i).pos[0] && pos[1] == o.get(i).pos[1] && pos[2] == o.get(i).pos[2]) {
				on = true;
			}
		}
		return on;
	}
	
	// Determines if the object shares the location with any segment of the snake.
	public boolean onSnake(Snake snake) {
		boolean on = false;
		for(int i = snake.length; i >= 0; i--) {
			if(snake.seg[i][0] == pos[0] && snake.seg[i][1] == pos[1]) {
				on = true;
			}
		}
		return on;
	}
	
	// Checks if the object is on the location specified by x,y,l.
	public boolean isEaten(int x, int y, int l) {
		if(x == pos[0] && y == pos[1] && l == pos[2]) {
			return true;
		}
		return false;
	}
	
	// Make this object become a border object at a random position on the opposite face of its current face.
	public void turnToBorder() {
		int [] opposite = {2, 3, 0, 1, 5, 4};
		eatable = false;
		color = new Color(250, 250, 250); // color for borders.
		pos[2] = opposite[pos[2]];
		Assets.g3d.setColor(pos[0], pos[1], pos[2], color);
	}
}
