package boot;

import controller.Controller;
import controller.MyController;
import model.Model;
import model.MyModel;
import view.MyView;
import view.View;

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
