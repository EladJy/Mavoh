package view;

import controller.Controller;

/**
 * Abstract class which implement View.
 * @author Elad Jarby
 * @version 1.0
 * @since 06.09.2016
 */
public abstract class CommonView implements View {
	Controller controller;
	
	/**
	 * Constructor to initialize the controller.
	 * @param controller - The controller that this View use.
	 */
	public CommonView(Controller controller) {
		super();
		this.controller = controller;
	}
}
