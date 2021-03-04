/*Tools contains generic tools for the manipulation of the program.
 * 
 */
package tools;

/*
 * EXAMINER PLEASE NOTE 
 * 
 * Utils.java was provided by Dr Stuart Porter as a part of Laboratory 4.
 * As stated in the assignment I have made this clear in both my report and program.
 * I have added comments and understand the running of the class fully. 
 * However, I have not altered any actual code. 
 * 
 * Any implementation of a try/catch statement to make my main simulation loop
 * wait would have been incredibly similar to this.
 * */

public class Utils {
	
	/*
	 * Pauses the running of the program for the desired amount of time.
	 * Should an interrupt occur it will be ignored as an InteruptedException 
	 * is caused by the program requesting/needing to continue. Therefore, it 
	 * should and will be allowed to do so by not intercepting the throw.
	 * @param time, the time to pause for in milliseconds.
	 */
	public static void pause(int time) {
		
		//Try/Catch statement declaration.
		try {
			
			//Call sleep from the Thread class.
			Thread.sleep(time);
			
		//Should there be an exception, do nothing as explained above.
		} catch (InterruptedException e) {
			//By doing nothing, the program is allowed to continue.
			
		}
	}
}
