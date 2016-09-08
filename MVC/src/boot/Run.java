package boot;

import controller.Controller;
import controller.MyController;
import model.Model;
import model.MyModel;
import view.MyView;
import view.View;

/**
 * Run the program!
 * @author Elad Jarby
 * @version 1.0
 * @since 06.09.2016
 *
 */
public class Run {
	public static void main(String[] args) {
		Controller controller = new MyController();
		Model model = new MyModel(controller);
		View view = new MyView(controller);
		controller.setModel(model);
		controller.setView(view);
		view.start();

	}
}
