package presenter;

/**
 * Command interface which represents communicator between View and Model
 * @author Elad Jarby
 * @version 1.0
 * @since 13.09.2016
 */
public interface Command {
	
	/**
	 * Define with command need to do
	 * @param args - Array of string , defines all the parameters of the command
	 */
	void doCommand(String[]args);
}
