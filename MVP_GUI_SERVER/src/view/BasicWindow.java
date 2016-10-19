package view;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Generate all the features for basic window
 * @author Elad Jarby
 * @version 1.0
 * @since 13.09.2016
 */
public abstract class BasicWindow extends Observable implements Runnable {
	Display display;
	Shell shell;
	
	/**
	 * Constructor to initialize all the parameters
	 * @param title - Title of the window
	 * @param width - Width of the window
	 * @param height - Height of the window
	 */
	public BasicWindow(String title , int width , int height) {
		display = new Display();
		shell = new Shell(display);
		shell.setSize(width,height);
		shell.setText(title);
	}
	
	/**
	 * Initialize all the widgets of the window
	 */
	abstract void initWidgets();
	
	/**
	 * Running the window
	 */
	@Override
	public void run() {
		initWidgets();
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}

