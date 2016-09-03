package view;

import controller.Controller;

public abstract class CommonView implements View {
	Controller controller;
	
	public CommonView(Controller controller) {
		super();
		this.controller = controller;
	}
}
