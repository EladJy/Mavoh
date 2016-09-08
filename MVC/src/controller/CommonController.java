package controller;
import java.util.HashMap;
import model.Model;
import view.View;

/**
 * Abstract class which implement Controller.</br>
 * 3 Data members - set up a connection between Model and View</br>
 * *Model model - Get a problem and generates a solution</br>
 * *View view - Display the solution and get commands from the user</br>
 * *HashMap<String, Command> stringToCommand - String: Save the the command name , Command: Generation of the command.
 * @author Elad Jarby
 * @version 1.0
 * @since 04.09.2016
 */
public abstract class CommonController implements Controller {
	Model model;
	View view;
	HashMap<String, Command> stringToCommand;
	
	/**
	 * Override to initialize the commands by the implementation of Common Controller.
	 */
	protected abstract void initCommands();
	/**
	 * Constructor to initialize the common controller
	 */
	public CommonController() {
		super();
		stringToCommand = new HashMap<String, Command>();
		initCommands();
	}
	
	/**
	 * Sets the model of the controller , to pass the command and generate solution.
	 * @param model - Model of the controller.
	 */
	public void setModel(Model model) {
		this.model = model;
	}
	
	/**
	 * Sets the view of the controller , to pass the command to be displayed.
	 * @param view - View of the controller.
	 */
	public void setView(View view) {
		this.view = view;
		view.setStringToCommand(stringToCommand);
	}

}
