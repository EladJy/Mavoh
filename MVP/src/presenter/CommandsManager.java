package presenter;

import java.util.HashMap;

import model.Model;
import view.View;

public class CommandsManager {
	private Model model;
	private View view;

	public CommandsManager(Model model , View view) {
		this.model = model;
		this.view = view;
	}

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
		commands.put("exit" , new exitCommand());
		
		return commands;
	}

	class GenerateMazeCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			model.generate3dMaze(args);			
		}

	}

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

	class getCrossSection implements Command {

		@Override
		public void doCommand(String[] args) {
			if (args == null) {
				int[][] crossSection = model.getCrossSection();
				view.displayCrossSection(crossSection);
			} else {
				model.crossSection(args);			
			}
		}

	}

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

	class saveMaze implements Command {

		@Override
		public void doCommand(String[] args) {
			model.saveMaze(args);			
		}

	}

	class loadMaze implements Command {

		@Override
		public void doCommand(String[] args) {
			model.loadMaze(args);			
		}

	}

	class getSolutionReady implements Command {

		@Override
		public void doCommand(String[] args) {
			model.getSolutionReady(args);			
		}

	}

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

	class exitCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			model.exitCommand();			
		}

	}

	class displayMessage implements Command {

		@Override
		public void doCommand(String[] args) {
			view.displayMessage(model.getMessage());
		}
	}

	class displayError implements Command {

		@Override
		public void doCommand(String[] args) {
			view.displayError(model.getMessage());
		}
	}
}
