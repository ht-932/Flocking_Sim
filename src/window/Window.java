/*The window package contains everything related to the controls/GUI.*/
package window;

//import the border layouts used
import java.awt.GridLayout;
import java.awt.BorderLayout;

//Import the ability to set colours.
import java.awt.Color;

//Import the canvas, JPanel and swing GUI package.
import javax.swing.JFrame;
import javax.swing.JPanel;
import drawing.Canvas;

public class Window {
	
	//Create variables to store the frame, panel and canvas.
	private JFrame frame;
	private JPanel sidePanel;
	private Canvas canvas;
	
	/*
	 * Constructor for the window, it simply assigns the frame, panel and canvas 
	 * that make up the GUI.
	 */
	public Window(JFrame frame, JPanel sidePanel, Canvas canvas) {

		//Assign variables to their local equivalents.
		this.frame = frame;
		this.sidePanel = sidePanel;
		this.canvas = canvas;
		
	}
	
	/*
	 * Call to create the frames/basic GUI.
	 */
	public void createFrames() {

		//Set the title of the frame.
		frame.setTitle("Flocking Simulator");
		
		//Set the default sizes of the window, which are tailored for a 700x500 canvas.
		frame.setSize(914, 537);
		
		//Set the cross on the frame t terminate the program.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Set the visibility to true.
		frame.setVisible(true);

		//Set the canvas and panel sizes.
		canvas.setSize(700, 500);
		sidePanel.setSize(200, 500);

		//Add the frame and sidePanel to the window.
		frame.add(canvas);
		frame.add(sidePanel, BorderLayout.EAST);

		//Set up the grid layout for the side panel.
		sidePanel.setLayout(new GridLayout(21,1));

		//Set the background colour of the side panel.
		sidePanel.setBackground(Color.lightGray);
		
	}

}
