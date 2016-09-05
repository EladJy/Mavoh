package model;

import controller.Controller;

public abstract class CommonModel implements Model {
	Controller controller;
	
	public CommonModel(Controller controller) {
		super();
		this.controller = controller;
	}
}
