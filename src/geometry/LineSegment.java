/*The geometry package contains anything related 
to the positioning system implemented in this program.*/
package geometry;

/*
 * A line segment is two Cartesian coordinates, representing the start and
 * end points of a line.
 */
public class LineSegment {
	
	/*
	 * Create two local variables (of type Cartesian coordinate) to store 
	 * the start and end points of a line.
	*/
	private CartesianCoordinate ccStart;
	private CartesianCoordinate ccEnd;
	
	/*
	 * Constructor for a specific line, requiring the desired start and end points.
	 * @param ccStart, the start point of the line, required as type CartesianCoordinate.
	 * @param ccEnd, the end point of the line, required as type CartesianCoordinate.
	 */
	public LineSegment(CartesianCoordinate ccStart, CartesianCoordinate ccEnd) {
		this.ccStart = ccStart;
		this.ccEnd = ccEnd;
	}
	
	/*
	 * Constructor for a specific line, but takes doubles instead of Cartesian coordinates.
	 * @param startX, the x coordinate of the start point of the line.
	 * @param startY, the y coordinate of the start point of the line.
	 * @param endX, the x coordinate of the end point of the line.
	 * @param endY, the y coordinate of the end point of the line.
	 */
	public LineSegment(double startX, double startY, double endX, double endY) {
		this.ccStart = new CartesianCoordinate(startX, startY);
		this.ccEnd = new CartesianCoordinate(endX, endY);
	}
	
	/*
	 * Setter for the start point of the line.
	 * @param ccStart, the desired start point, required as type CartesianCoordinate.
	 */
	public void setStartPoint (CartesianCoordinate ccStart) {
		this.ccStart = ccStart;
	}
	
	/*
	 * Setter for the end point of the line.
	 * @param ccEnd, the desired end point, required as type CartesianCoordinate.
	 */
	public void setEndPoint (CartesianCoordinate ccEnd) {
		this.ccEnd = ccEnd;
	}
	
	/*
	 * Getter for the line's start point.
	 * @return ccStart, the lines start point, as the type CartesianCoordinate.
	 */
	public CartesianCoordinate getStartPoint() {
		return ccStart;
	}
	
	/*
	 * Getter for the line's end point.
	 * @return ccEnd, the lines end point, as the type CartesianCoordinate.
	 */
	public CartesianCoordinate getEndPoint() {
		return ccEnd;
	}
	
	/*
	 * Converts the desired line segment to a string format suitable for display.
	 * @return LineSegment.toString, the current LineSegment as a string.
	 */
	public String toString() {
		return (ccStart.toString() +" "+ ccEnd.toString());
		
	}
}
