package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import controller.Command;

public class CLI extends Thread {

	BufferedReader in;
	PrintWriter out;
	HashMap<String,Command> stringToCommand;

	public CLI(BufferedReader in , PrintWriter out) {
		super();
		this.in= in;
		this.out = out;

	}

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

					while(!(userCommand = in.readLine()).equals("exit")) {
						ArrayList<String> paramArray = new ArrayList<String>();

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
					out.print("Bye bye , have a nice day!");
					out.flush();	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void setStringToCommand(HashMap<String, Command> stringToCommand) {
		this.stringToCommand = stringToCommand;
	}

	private void printInstructions() {
		System.out.println("================================================================================");
		System.out.println("=========================== Hello , Welcome to my CLI! =========================");
		System.out.println("================================================================================");
		System.out.println(">> 1)  dir <path>                                                             <<");
		System.out.println(">> 2)  generate_3d_maze <maze name> <floors> <width> <length> <algorithm>     <<");
		System.out.println(">> 3)  display <maze name>                                                    <<");
		System.out.println(">> 4)  display_cross_section <axis> <index> <maze name>                       <<");
		System.out.println(">> 5)  save_maze <maze name> <file name>                                      <<");
		System.out.println(">> 6)  oad_maze <file name> <maze name>                                       <<");
		System.out.println(">> 7)  solve <maze name> <algorithm>                                          <<");
		System.out.println(">> 8)  display_solution <maze name>                                           <<");
		System.out.println(">> 9)  exit                                                                   <<");
		System.out.println(">> 10) help                                                                   <<");
		System.out.println("================================================================================");
	}

}
