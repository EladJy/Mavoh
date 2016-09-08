package controller;

/**
 * Define the command interface.</br>
 * Each command need implement doCommand function.
 * @author Elad Jarby
 * @version 1.0
 * @since 04.09.2016
 *
 */
public interface Command {
	/**
	 * Define what each command do.
	 * @param strings - Array of parameters.
	 */
	void doCommand(String[] strings);
}
