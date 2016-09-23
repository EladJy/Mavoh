package presenter;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

/**
 * Define the functionality of the presenter class
 * @author Elad Jarby
 * @version 1.0
 * @since 13.09.2016
 */
public class Presenter implements Observer {
	private Model model;
	private View view;
	private HashMap<String, Command> commands;
	private CommandsManager commandsManager;

	/**
	 * Constructor to initialize presenter
	 * @param model - Model of the presenter
	 * @param view - View of the presenter
	 */
	public Presenter(Model model , View view) {
		this.model = model;
		this.view = view;
		commandsManager = new CommandsManager(this.model , this.view);
		commands = commandsManager.getCommandsMap();
	}

	/**
	 * Override function , when the observable had changed,</br>
	 * It calls to update function to update the observer (the presenter).
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o == view) {
			String commandLine = (String)arg;
			String arr[] = commandLine.split(" ");
			String command = arr[0];

			if(!commands.containsKey(command)) {
				view.displayError("Command doesn't exist!");
			} else {
				String[] args = null;
				if(arr.length > 1) {
					String commandArgs = commandLine.substring(commandLine.indexOf(" ") +1);
					args = commandArgs.split(" ");
				}
				Command cmd = commands.get(command);
				cmd.doCommand(args);
			}
		} else if(o == model) {
			String commandLine = (String)arg;
			Command cmd = commands.get(commandLine);
			cmd.doCommand(null);
		}

	}
}
