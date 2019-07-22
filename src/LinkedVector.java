// Vector that is connected to other Vectors to be used in a deque

public class LinkedVector extends Vector {
	public LinkedVector next;
	public LinkedVector previous;

	public LinkedVector(int x, int y, int z) {
		super(x, y, z);
	}
	public LinkedVector(int x, int y, int z, LinkedVector next) {
		super(x, y, z);
		link(next);
		previous = null;
	}

	// Assume this is the end of the queue and add a new element to the back.
	public void link(LinkedVector next) {
		this.next = next;
		next.previous = this;
	}

	// Remove the link to the next Vector.
	public void unlink() {
		next.previous = null;
		next = null;
	}
}