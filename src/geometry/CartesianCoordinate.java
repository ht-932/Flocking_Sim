/*The geometry package contains anything related 
to the positioning system implemented in this program.*/
package geometry;

/*
 * The Cartesian coordinate system is a 2-Dimensional positioning system.
 * These objects will be used to locate just about any graphical part of the program,
 * including any entities present.
 */
public class CartesianCoordinate {
	
	//Create two local variables to store the Cartesian coordinate's x and y values.
	private double xPosition;
	private double yPosition;
	
	/**
	 * Constructor for a specific Cartesian coordinate, requires desired location.
	 * @param x, the desired x coordinate.
	 * @param y, he desired y coordinate.
	 */
	public CartesianCoordinate(double x, double y) {
		xPosition = x;
		yPosition = y; 
	}
	
	/**
	 * Constructor for a blank Cartesian coordinate, with the value (0,0).
	 */
	public CartesianCoordinate() {
		xPosition = 0;
		yPosition = 0;
	}

	/**
	 * Getter for the x position.
	 * @return xPosition, the current x position
	 */
	public double getX() {
		return xPosition;
	}
	
	/**
	 * Getter for the y position.
	 * @return yPosition, the current y position
	 */
	public double getY() {
		return yPosition;
	}
	
	/*
	 * Setter for the x position.
	 * @param xPosition, the desired x position.
	 */
	public void setX(double xPosition) {
		this.xPosition = xPosition;
	}

	/*
	 * Setter for the y position
	 * @param yPosition, the desired y position.
	 */
	public void setY(double yPosition) {
		this.yPosition = yPosition;
	}

	/*
	 * Provides a string version of the desired coordinate suitable for display.
	 * @return CartesianCoordinate.toString, the current CartesianCoordinate as a string
	 */
	public String toString() {
		return ("X: " + (Double.toString(xPosition)) + " Y: " + (Double.toString(yPosition)));
	}
	
}
