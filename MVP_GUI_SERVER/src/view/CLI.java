package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;

/**
 * The class is responsible to get correct user input</br>
 * And to send this information to the presenter.
 * @author Elad Jarby
 * @version 1.0
 * @since 19.10.2016
 */
public class CLI extends Observable {
	private BufferedReader in;
	private PrintWriter out;
	
	/**
	 * Constructor to initialize the CLI.
	 * @param in - Input reader.
	 * @param out - Output writer.
	 */
	public CLI(BufferedReader in , PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	/**
	 * Starting the CLI.
	 */
	public void start(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				printInstructions();
				while (true) {
					try {
						String commandLine = in.readLine();
						if(commandLine.equals("help")) {
							printInstructions();
							continue;
						}
						setChanged();
						notifyObservers(commandLine);
						if(commandLine.equals("close_server"))
							break;
					} catch (IOException e) {
						e.printStackTrace();
					}				
				}
				
				out.println("==========================");
				out.println("Bye bye , have a nice day!");
				out.println("==========================");
				out.flush();
			}
		});
		thread.start();
	}
	
	/**
	 * Print all the instructions in CLI.
	 */
	private void printInstructions() {
		System.out.println("=======================================================================================================");
		System.out.println("======================================= Hello , Welcome to my server! =================================");
		System.out.println("=======================================================================================================");
		System.out.println(">> 1)  open_server                                                                                   <<");
		System.out.println(">> 2)  close_server                                                                                  <<");
		System.out.println(">> 3)  save_properties <port> <number of clients> <time out> <gui or cli>                            <<");
		System.out.println("=======================================================================================================");
	}
}
