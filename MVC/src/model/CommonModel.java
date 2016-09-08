package model;

import controller.Controller;

/**
 * Abstract class which implement Model.
 * @author Elad Jarby
 * @version 1.0
 * @since 05.09.2016
 */
public abstract class CommonModel implements Model {
	Controller controller;
	
	/**
	 * Constructor to initialize controller.
	 * @param controller - The controller that this Model use, 
	 * All the solutions for problems passing through this controller.
	 */
	public CommonModel(Controller controller) {
		super();
		this.controller = controller;
	}
}
