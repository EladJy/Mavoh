package boot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import model.MyModel;
import presenter.Presenter;
import view.MyView;
import view.ServerGUI;

public class Run {
	public static void main(String[] args) {
		MyModel model = new MyModel();
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
			ServerGUI view = new ServerGUI("Game",500,500);
			Presenter presenter = new Presenter(model, view);
			model.addObserver(presenter);
			view.addObserver(presenter);
			view.start();
		}
	}
}
