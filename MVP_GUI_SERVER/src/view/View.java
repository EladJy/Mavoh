package view;

/**
 * View interface which display all the generated information for specific command.
 * @author Elad Jarby
 * @version 1.0
 * @since 18.10.2016
 */
public interface View {
	
	/**
	 * Starting the GUI
	 */
	public void start();
	
	/**
	 * Function to display all the status in the server </br>
	 * and update each time server get a message/error
	 * @param obj - object
	 */
	 void display(Object obj);
	 
	/**
	* Displays an error message.
	* @param error - Error message.
	*/
	public void displayError(String error);
	
	/**
	 * Displays a message.
	 * @param msg - Message.
	 */
	public void displayMessage(String msg);
}
