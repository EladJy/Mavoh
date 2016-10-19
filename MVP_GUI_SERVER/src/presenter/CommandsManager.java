package presenter;

import java.util.HashMap;

import model.Model;
import view.View;

/**
 * Command manager which get commands from View or Model and pass it
 * @author Elad Jarby
 * @version 1.0
 * @since 13.09.2016
 */
public class CommandsManager {
	private Model model;
	private View view;

	/**
	 * Constructor to initialize View and Model
	 * @param model - Model of the presenter
	 * @param view - View of the presenter
	 */
	public CommandsManager(Model model , View view) {
		this.model = model;
		this.view = view;
	}

	/**
	 * Get all commands that possible in the presenter
	 * @return Hash map of all commands
	 */
	public HashMap<String, Command> getCommandsMap() {
		HashMap<String, Command> commands = new HashMap<String , Command>();
		commands.put("open_server", new openServer());
		commands.put("close_server" , new closeServer());
		commands.put("error", new displayError());
		commands.put("displayMessage" , new displayMessage());
		commands.put("save_properties" , new saveProperties());
		return commands;
	}
	
	/**
	 * Command to open the server
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 18.10.2016
	 */
	class openServer implements Command {
		String mazeName;
		@Override
		public void doCommand(String[] args) {
			if(args == null) {
				view.displayMessage(model.getMessage());
			} else {
				model.openServer();
			}
		}

	}
	
	/**
	 * Command to close the server
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 18.10.2016
	 */
	class closeServer implements Command {
		String mazeName;
		@Override
		public void doCommand(String[] args) {
			if(args == null) {
				view.displayMessage(model.getMessage());
			} else {
				model.closeServer();
			}
		}
	}
	
	/**
	 * Command to display message
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 18.10.2016
	 */
	class displayMessage implements Command {

		@Override
		public void doCommand(String[] args) {
			view.displayMessage(model.getMessage());		
		}
	}

	/**
	 * Command to display an error
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 18.10.2016
	 */
	class displayError implements Command {

		@Override
		public void doCommand(String[] args) {
			view.displayError(model.getMessage());
		}
	}
	
	/**
	 * Command to save properties
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 18.10.2016
	 */
	class saveProperties implements Command {

		@Override
		public void doCommand(String[] args) {
			model.saveProperties(args);
		}

	}
	


}
