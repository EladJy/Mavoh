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
		commands.put("display" , new DisplayMazeCommand());
		commands.put("displayMessage" , new displayMazeMessage());
		commands.put("error" , new displayError());
		
		return commands;
	}
	
	class GenerateMazeCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			model.generate3dMaze(args);			
		}
		
	}
	
	class DisplayMazeCommand implements Command {

		@Override
		public void doCommand(String[] args) {
			byte[] byteArr = model.getMazeFromHashMap(args[0]);
			try {
				view.displayMaze(byteArr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class displayMazeMessage implements Command {

		@Override
		public void doCommand(String[] args) {
			view.displayMessage(model.getMessage());
		}
	}
	
	class displayError implements Command {

		@Override
		public void doCommand(String[] args) {
			String msg = "";
			for (String s : args) {
				msg = msg + s + " ";
			}	
			view.displayError(msg);
		}
	}
}
