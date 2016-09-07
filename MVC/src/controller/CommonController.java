package controller;

import java.util.HashMap;

import model.Model;
import view.View;

public abstract class CommonController implements Controller {
	Model model;
	View view;
	HashMap<String, Command> stringToCommand;
	
	protected abstract void initCommands();
	public CommonController() {
		super();
		stringToCommand = new HashMap<String, Command>();
		initCommands();
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public void setView(View view) {
		this.view = view;
		view.setStringToCommand(stringToCommand);
	}

}
