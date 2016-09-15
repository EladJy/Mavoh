package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Observable;

public class CLI extends Observable {
	private BufferedReader in;
	private PrintWriter out;
	
	public CLI(BufferedReader in , PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	public void start(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				printInstructions();
				while (true) {
					try {
						String commandLine = in.readLine();
						setChanged();
						notifyObservers(commandLine);
						if(commandLine.equals("exit"))
							break;
					} catch (IOException e) {
						// TODO Auto-generated catch block
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
		System.out.println("=======================================================================================================");
	}
}