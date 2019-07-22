import java.awt.Color;
import java.util.List;
import javax.swing.JFrame;


public class Object extends JFrame {
	Vector pos = new Vector();
	boolean eatable;
	Color color;

	// Create an object at a random position.
	public Object(int size, Snake snake, List<Object> o) {
		eatable = true;
		do {
			pos.x = (int)(Math.random()*size);
			pos.y = (int)(Math.random()*size);
			pos.z = (int)(Math.random()*5);
		}while(onSnake(snake) || onBlock(o)); // Don't put on object on another object/on the snake.
		// Tell g3d to paint the object with a random color(not matching background or border color).
		Assets.g3d.setColor(pos.x, pos.y, pos.z, new Color(55+(int)(Math.random()*150), 55+(int)(Math.random()*150), 55+(int)(Math.random()*150)));
	}
	
	// Create an object at a certain position. Only used for level creation → no check if onSnake/onBlock.
	public Object(int x, int y, int l) {
		pos.x = x;
		pos.y = y;
		pos.z = l;
		color = Assets.borderColor; // color for borders.
		eatable = false;
		// Tell g3d to paint the object.
		Assets.g3d.setColor(pos.x, pos.y, pos.z, color);
	}
	
	// Determines if this object shares the location with an object from the list.
	public boolean onBlock(List<Object> o) {
		boolean on = false;
		for(int i = 0; i < o.size(); i++) {
			if(pos.x == o.get(i).pos.x && pos.y == o.get(i).pos.y && pos.z == o.get(i).pos.z) {
				on = true;
			}
		}
		return on;
	}
	
	// Determines if the object shares the location with any segment of the snake.
	public boolean onSnake(Snake snake) {
		for(LinkedVector seg = snake.head; seg.next != null; seg = seg.next) {
			if(pos.x == seg.x && pos.y == seg.y && pos.z == seg.z) {
				return true;
			}
		}
		return false;
	}
	
	// Checks if the object is on the location specified by x,y,l.
	public boolean isEaten(int x, int y, int l) {
		if(x == pos.x && y == pos.y && l == pos.z) {
			return true;
		}
		return false;
	}
	
	// Make this object become a border object at a random position on the opposite face of its current face.
	public void turnToBorder() {
		int [] opposite = {2, 3, 0, 1, 5, 4};
		eatable = false;
		color = Assets.borderColor; // color for borders.
		pos.z = opposite[pos.z];
		Assets.g3d.setColor(pos.x, pos.y, pos.z, color);
	}
}
