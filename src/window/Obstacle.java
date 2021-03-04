/*The window package contains everything related to the controls/GUI.*/
package window;

//Import the necessities to create the obstacle on a canvas.
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import drawing.Canvas;

public class Obstacle {

	//Create variable to store the canvas.
	private Canvas canvas;

	/*
	 * Constructor for the obstacle object.
	 * @param canvas, a canvas to draw the obstacle on.
	 */
	public Obstacle(Canvas canvas) {

		//Sets canvas to local canvas.
		this.canvas = canvas;

	}

	/*
	 * Draws the obstacle. 
	 */
	public void draw() {

		//Create a new image icon, using the obstacle picture from src.
		ImageIcon obstacle = new ImageIcon("Obstacle.png"); 
		
		//Assign the image icon to a JLabel.
		JLabel obstacleLabel = new JLabel(obstacle); 
		
		//Add the image to the canvas.
		canvas.add(obstacleLabel); 
		
		//Force a redraw of the canvas to show the changes.
		canvas.revalidate();

	}

}
