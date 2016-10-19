package view;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

/**
 * Display all the generated information for specific command.
 * @author Elad Jarby
 * @version 1.0
 * @since 18.10.2016
 */
public class MyView extends Observable implements View,Observer {

	private BufferedReader in;
	private PrintWriter out;
	private CLI cli;
	 
	/**
	 * Constructor to initialize all the fields.
	 * @param in - Input reader.
	 * @param out - Output writer.
	 */
	public MyView(BufferedReader in , PrintWriter out) {
		this.in = in;
		this.out = out;
		cli = new CLI(this.in,this.out);
		cli.addObserver(this);
	}
	
	/**
	 * Update the observer function
	 * @param o - Observable
	 * @param arg - Object
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o == cli) {
			this.setChanged();
			this.notifyObservers(arg);
		}
	}
	
	/**
	 * Starts CLI Loop
	 */
	@Override
	public void start() {
		cli.start();
	}

	/**
	 * Displays an error message.
	 * @param error - Error message.
	 */
	@Override
	public void displayError(String error) {
		out.println(error);
		out.flush();		
	}

	/**
	 * Displays a message.
	 * @param msg - Message.
	 */
	@Override
	public void displayMessage(String msg) {
		out.println(msg);
		out.flush();		
	}

	@Override
	public void display(Object obj) {}

}
