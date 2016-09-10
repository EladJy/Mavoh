package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import controller.Command;

/**
 * The class is responsible to get correct user input</br>
 * And to sent this information to the controller.
 * @author Elad Jarby
 * @version 1.0
 * @since 06.09.2016
 */
public class CLI extends Thread {

	BufferedReader in;
	PrintWriter out;
	HashMap<String,Command> stringToCommand;

	/**
	 * Constructor to initialize the CLI.
	 * @param in - Input reader.
	 * @param out - Output writer.
	 */
	public CLI(BufferedReader in , PrintWriter out) {
		super();
		this.in= in;
		this.out = out;

	}

	/**
	 * Starting the CLI.
	 */
	public void start() {
		printInstructions();
		out.print("Please enter command: ");
		out.flush();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String userCommand;
					Command command = null;

					while(true) {
						ArrayList<String> paramArray = new ArrayList<String>();
						userCommand = in.readLine();
						if(userCommand.equals("exit")) {
							break;
						} else if(userCommand.equals("help")) {
							printInstructions();
							continue;
						}
						while(!(userCommand.isEmpty())) {

							if((command = stringToCommand.get(userCommand)) != null) {
								Collections.reverse(paramArray);
								command.doCommand(paramArray.toArray(new String[paramArray.size()]));
								break;
							}
							if(userCommand.indexOf(" ") == -1) {
								break;
							}

							paramArray.add(userCommand.substring(userCommand.lastIndexOf(" ") + 1));
							userCommand = userCommand.substring(0, userCommand.lastIndexOf(" "));
						}
						if(command == null) {
							out.println("Error with command");
							out.flush();
						}
					}
					command = stringToCommand.get("exit");
					command.doCommand(null);
					out.println("==========================");
					out.println("Bye bye , have a nice day!");
					out.println("==========================");
					out.flush();	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Set the list of commands the is supported by the controller.
	 * @param stringToCommand - Hash map with the key-name of commands and the value of command.
	 */
	public void setStringToCommand(HashMap<String, Command> stringToCommand) {
		this.stringToCommand = stringToCommand;
	}

	/**
	 * Print all the instructions in CLI.
	 */
	private void printInstructions() {
		System.out.println("=======================================================================================================");
		System.out.println("======================================= Hello , Welcome to my CLI! ====================================");
		System.out.println("=======================================================================================================");
		System.out.println(">> 1)  dir <path>                                                                                    <<");
		System.out.println(">> 2)  generate_3d_maze <maze name> <z> <y> <x> <algorithm> (simple,growing-last,growing-random)     <<");
		System.out.println(">> 4)  display_cross_section <axis> <index> <maze name>                                              <<");
		System.out.println(">> 3)  display <maze name>                                                                           <<");
		System.out.println(">> 5)  save_maze <maze name> <file name>                                                             <<");
		System.out.println(">> 6)  load_maze <file name> <maze name>                                                             <<");
		System.out.println(">> 7)  solve <maze name> <algorithm>                                                                 <<");
		System.out.println(">> 8)  display_solution <maze name>                                                                  <<");
		System.out.println(">> 9)  exit                                                                                          <<");
		System.out.println(">> 10) help                                                                                          <<");
		System.out.println("=======================================================================================================");
	}

}
