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
		commands.put("generate_3d_maze", new GenerateMazeCommand());
		commands.put("display" , new getMaze());
		commands.put("error" , new displayError());
		commands.put("display_cross_section" , new getCrossSection());
		commands.put("displayMessage" , new displayMessage());
		commands.put("dir" , new dirPath());
		commands.put("save_maze" , new saveMaze());
		commands.put("load_maze" , new loadMaze());
		commands.put("solve" , new getSolutionReady());
		commands.put("display_solution" , new getSolution());
		commands.put("save_properties" , new saveProperties());
		commands.put("exit" , new exitCommand());

		return commands;
	}

	/**
	 * Command to generate maze
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class GenerateMazeCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			model.generate3dMaze(args);			
		}

	}

	/**
	 * Command to get maze and display it
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class getMaze implements Command {
		String mazeName;
		@Override
		public void doCommand(String[] args) {
			if(args == null) {
				byte[] byteArr = model.getMazeFromHashMap(mazeName);
				try {
					view.displayMaze(byteArr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				mazeName = args[0];
				model.getMaze(args);
			}
		}

	}

	/**
	 * Command to get cross section
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class getCrossSection implements Command {
		String mazeName;
		@Override
		public void doCommand(String[] args) {
			if (args == null) {
				int[][] crossSection = model.getCrossSection();
				//byte[] byteArr = model.getMazeFromHashMap(mazeName);
				view.displayCrossSection(crossSection);
			} else {
				mazeName = args[2];
				model.crossSection(args);			
			}
		}

	}

	/**
	 * Command to get all the files / directories from specific path
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class dirPath implements Command {
		String[] fileList;
		@Override
		public void doCommand(String[] args) {
			if(args == null) {
				view.displayDirPath(model.getList());	
			} else {
				model.dirPath(args);			
			}
		}

	}

	/**
	 * Command to save the maze
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class saveMaze implements Command {

		@Override
		public void doCommand(String[] args) {
			model.saveMaze(args);			
		}

	}

	/**
	 * Command to load the maze
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class loadMaze implements Command {

		@Override
		public void doCommand(String[] args) {
			model.loadMaze(args);			
		}

	}

	/**
	 * Command to get solution ready
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class getSolutionReady implements Command {

		@Override
		public void doCommand(String[] args) {
			model.getSolutionReady(args);			
		}

	}

	/**
	 * Command to get solution
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class getSolution implements Command {
		String mazeName;
		@Override
		public void doCommand(String[] args) {
			if(args == null) {
				view.displaySolution(model.getSolutionFromHashMap(mazeName));
			} else {
				mazeName = args[0];
				model.getSolution(args);
			}
		}

	}

	/**
	 * Command to exit
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class exitCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			model.exitCommand();			
		}

	}

	/**
	 * Command to save properties
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class saveProperties implements Command {

		@Override
		public void doCommand(String[] args) {
			model.saveProperties(args);
		}

	}

	/**
	 * Command to display a message
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class displayMessage implements Command {

		@Override
		public void doCommand(String[] args) {
			byte[] byteArr = null;
			if(model.getMazeName() != null && model.getProperties().getViewSetup().equals("gui")) {
				byteArr = model.getMazeFromHashMap(model.getMazeName());
				view.displayMessageWithMaze(byteArr , model.getMessage());
				return;
			}
			view.displayMessage(model.getMessage());		
		}
	}

	/**
	 * Command to display an error
	 * @author Elad Jarby
	 * @version 1.0
	 * @since 13.09.2016
	 */
	class displayError implements Command {

		@Override
		public void doCommand(String[] args) {
			view.displayError(model.getMessage());
		}
	}


}
