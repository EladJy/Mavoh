package model;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for handling a client
 * @author Elad Jarby
 * @version 1.0
 * @since 19.10.2016
 */
public interface ClientHandler {
	
	/**
	 * Handle client when he connected to the server
	 * @param inFromClient - Data that come from client
	 * @param outToClient - Data that server send to the client
	 */
	void handleClient(InputStream inFromClient, OutputStream outToClient);
	
	/**
	 * Function that save the solution when server is shutting down
	 */
	public void close();
	
	/**
	 * Message that need to print on server status
	 * @return String message
	 */
	public String getMessage();
}
