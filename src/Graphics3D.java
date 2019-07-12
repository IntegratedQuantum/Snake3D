import java.awt.Graphics2D;
import java.awt.Color;

// This class is responsible for 3d-projection thus allowing the project to only depend on the java standard library.
public class Graphics3D {
	private double ax, ay; // Some representative for the angle, to compare to, but not the real angle, which isn't stored anywhere.
	private double bz = 1000; // "Distance between center of screen and eyes of the player in pixels." 1000 seems realistic to me.
	
	// Coordinates of the center of the cube in pixel. (0, 0, 0) is the center of YOUR screen.
	private int x = 0;
	private int y = 0;
	private int z = 1500;
	// Corners of the cube
	private double [][] points = {
	//	{x, y, z, xProjection, yProjection},
		{-300, -300, -300, 0, 0},
		{-300, -300, 300, 0, 0},
		{-300, 300, -300, 0, 0},
		{-300, 300, 300, 0, 0},
		{300, -300, -300, 0, 0},
		{300, -300, 300, 0, 0},
		{300, 300, -300, 0, 0},
		{300, 300, 300, 0, 0},
	};
	// Index of the corners of every one of the 6 areas of the cube.
	private int [][] a = {
		{0, 2, 4, 6},
		{4, 6, 5, 7},
		{5, 7, 1, 3},
		{1, 3, 0, 2},
		{4, 5, 0, 1},
		{2, 3, 6, 7},
	};
	private Color [][][] texture = new Color[6][8][8]; // Stores a color value for each tile on the surface of the cube.
	private int [] highest = new int[3];
	private boolean [] vis = new boolean[6];
	
	// Set each tile to black(the background color).
	public Graphics3D() {
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 8; j++) {
				for(int k = 0; k < 8; k++) {
					texture[i][j][k] = Color.BLACK;
				}
			}
		}
	}
	
	// Rotate the cube to a certain angle representative. Uses only the deltas.
	void reload(double ax, double ay) {
		update(ax-this.ax, ay-this.ay);
		this.ax = ax;
		this.ay = ay;
	}
	
	// Rotate the cube and do all the projection work.
	private void update(double ax, double ay) {
		// Rotate the corners.
		rotateX(ax);
		rotateY(ay);
		
		// Save the projection for each corner.
		for(int i = 0; i < 8; i++) {
			double x2 = points[i][0]+x;
			double y2 = points[i][1]+y;
			double z2 = points[i][2]+z;
			points[i][3] = get3DX(x2, z2);
			points[i][4] = get3DY(y2, z2);
		}

		// Find the closest point to the screen.
		int closest = 0;
		for(int i = 1; i < 8; i++) {
			if(points[i][2] < points[closest][2])
				closest = i;
		}
		// Find all three areas that that touch the closest point and store their index.
		int k = 0;
		int areas[] = new int[3];
		for(int i = 0; i < 6 && k < 3; i++) {
			for(int j = 0; j < 4; j++) {
				if(a[i][j] == closest) {
					areas[k] = i;
					k++;
					break;
				}
			}
		}
		// Store the average z-distance between the area and the middle-point of the cube. Used to decide which areas to paint and in which order.
		// In a cube this value is r*cos(α) where α is the angle between z-axis and the normal through the area and r is here half the side length.
		double [] val = new double[3];
		for(int i = 0; i < 3; i++) {
			val[i] = 0;
			for(int j = 0; j < 4; j++)
				val[i] += points[a[areas[i]][j]][2];
		}
		// Sort the areas by their distance to the screen:
		highest[0] = highest[1] = highest[2] = 0;
		for(int i = 1; i < 3; i++) {
			if(val[i] < val[highest[0]])
				highest[0] = i;
		}
		if(highest[0] == highest[1]) {
			highest[1]++;
			highest[2]++;
		}
		for(int i = highest[1]+1; i < 3; i++) {
			if(val[i] < val[highest[1]] && i != highest[0])
				highest[1] = i;
		}
		if(highest[1] == highest[2])
			highest[2]++;
		for(int i = highest[2]; i < 3; i++) {
			if(i != highest[0] && i != highest[1]) {
				highest[2] = i;
				break;
			}
		}
		// put the corresponding area index into the array.
		for(int i = 0; i < 3; i++)
			highest[i] = areas[highest[i]];
	}
	
	// From now on paint a new color at that tile until changed..
	public void setColor(int x, int y, int l, Color c) {
		if(texture[l][x][y] != new Color(250, 250, 250)) { // Don't change the color, if there is a border tile(Used to make the border not disappear after the snake hit onto it.).
			texture[l][x][y] = c;
		}
	}
	
	// Paint the cube and its tiles based on the colors stored in texture.
	// TODO: simplify.
	public void drawCube(Graphics2D g) {
		// Only paint three faces of the cube.
		for(int m = 2; m >= 0; m--) {
			int k = highest[m];
			// Store the corners of the current face to paint.
			int [] x = {(int)points[a[k][1]][3], (int)points[a[k][0]][3], (int)points[a[k][2]][3], (int)points[a[k][3]][3]};
			int [] y = {(int)points[a[k][1]][4], (int)points[a[k][0]][4], (int)points[a[k][2]][4], (int)points[a[k][3]][4]};
			
			// Divide the polygon created by the array directly above into an 8*8 field of tiles:
			double nx = (x[1]-x[0])/8.0; // x-size of a tile at one side
			double ny = (y[1]-y[0])/8.0; // y-size of a tile at one side
			double fx = nx+(x[3]-x[2])/8.0; // x-size of a tile at the other side
			double fy = ny+(y[3]-y[2])/8.0; // y-size of a tile at the other side
			for(int i = 0; i < 8; i++) {
				// Use a linear transition between n and f to determine respective size and position:
				double nx1 = x[0]+400+i*nx;
				double nx2 = x[0]+400+(i+1)*nx;
				double ny1 = y[0]+400+i*ny;
				double ny2 = y[0]+400+(i+1)*ny;
				double fx1 = (x[3]-x[0]-i*fx)/8;
				double fx2 = (x[3]-x[0]-(i+1)*fx)/8;
				double fy1 = (y[3]-y[0]-i*fy)/8;
				double fy2 = (y[3]-y[0]-(i+1)*fy)/8;
				// Finally draw the 8 polygons of each row or column(depending on the rotation of the cube).
				for(int j = 0; j < 8; j++) {
					int [] xx = {(int)(nx1+j*fx1), (int)(nx1+(j+1)*fx1), (int)(nx2+(j+1)*fx2), (int)(nx2+j*fx2)};
					int [] yy = {(int)(ny1+j*fy1), (int)(ny1+(j+1)*fy1), (int)(ny2+(j+1)*fy2), (int)(ny2+j*fy2)};
					g.setColor(texture[k][i][j]);
					g.fillPolygon(xx, yy, 4);
					g.setColor(Color.WHITE);
					g.drawPolygon(xx, yy, 4);
				}
			}
		}
	}
	
	// project the x-coordinate.
	private double get3DX(double x, double z) {
		x = Math.asin(x/Math.sqrt(x*x+z*z));
		x = Math.tan(x)*bz;
		return x;
	}
	
	// project the y-coordinate.
	private double get3DY(double y, double z) {
		y = Math.asin(y/Math.sqrt(y*y+z*z));
		y = Math.tan(y)*bz;
		return y;
	}
	
	// rotate around the y-axis.
	private void rotateY(double a) {
		double sin = Math.sin(a);
	    double cos = Math.cos(a);
	    for (int i = 0; i < 8; i++) {
	    	double y = points[i][1];
	    	double z = points[i][2];
	        points[i][1] = y*cos-z*sin;
	        points[i][2] = z*cos+y*sin;
	    }
	}
	
	// rotate around the x-axis.
	private void rotateX(double a) {
		double sin = Math.sin(a);
	    double cos = Math.cos(a);
	    for (int i = 0; i < 8; i++) {
	    	double x = points[i][0];
	    	double z = points[i][2];
	        points[i][0] = x*cos-z*sin;
	        points[i][2] = z*cos+x*sin;
	    }
	}
}
