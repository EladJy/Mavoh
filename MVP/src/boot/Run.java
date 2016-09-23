package boot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import model.MyModel;
import presenter.Presenter;
import view.MazeWindow;
import view.MyView;

/**
 * Run the program
 * @author Elad Jarby
 * @version 1.0
 * @since 13.09.2016
 */
public class Run {
	public static void main(String[] args) {
		MyModel model = new MyModel(args);
		String viewSetup = model.getProperties().getViewSetup();
		if(viewSetup.equals("cli")) {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter out = new PrintWriter(System.out);	
			MyView view = new MyView(in, out);
			Presenter presenter = new Presenter(model, view);
			model.addObserver(presenter);
			view.addObserver(presenter);
			view.start();
		} else if(viewSetup.equals("gui")) {
			MazeWindow view = new MazeWindow("Game",850,800);
			Presenter presenter = new Presenter(model, view);
			model.addObserver(presenter);
			view.addObserver(presenter);
			view.start();
		}
	}
}
