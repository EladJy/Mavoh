package model;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import presenter.PropertiesLoader;
import presenter.ServerProperties;

/**
 * Make all the calculations for specific command.
 * @author Elad Jarby
 * @version 1.0
 * @since 18.10.2016
 */
public class MyModel extends Observable implements Model {

	int port;
	ServerSocket server;
	ClientHandler clinetHandler;
	int numOfClients;
	ExecutorService threadPool;
	volatile boolean stopServer;
	Thread mainServerThread;
	String message;
	ServerProperties serverProperties;

	/**
	 * Constructor to initialize all the fields.
	 */
	public MyModel() 
	{
		this.clinetHandler=new MyClientHandler();
		checkServerProperties();
		serverProperties = PropertiesLoader.getInstance().getProperties();
	}

	private void checkServerProperties() {
		File f=new File("serverProperties.xml");
		if(!f.exists())	{
			makeProperties();
		}	
	}


	/**
	 * Private function , if there is no file of server properties, </br>
	 * The function will make one.
	 */
	private void makeProperties() {
		XMLEncoder xmlEncoder;
		try {
			xmlEncoder = new XMLEncoder(new FileOutputStream("serverProperties.xml"));
			xmlEncoder.writeObject(new ServerProperties(1234, 5,10000,"gui"));
			xmlEncoder.close();
			System.out.println("XML File create successfuly!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/**
	 * Starting the server
	 */
	@Override
	public void openServer() {
		try 
		{
			this.numOfClients = serverProperties.getNumberOfClients();
			this.threadPool = Executors.newFixedThreadPool(numOfClients);
			this.port=serverProperties.getPort();
			this.server=new ServerSocket(this.port);
			this.stopServer=false;
			server.setSoTimeout(serverProperties.getTimeOut());
		}
		catch (SocketException e1) 
		{
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		mainServerThread=new Thread(new Runnable() {			
			@Override
			public void run() {
				while(!stopServer)
				{
					try {
						
						// Listen for connections from clients and accept them.
						final Socket client = server.accept();
						message = client.toString();
						setChanged();
						notifyObservers("displayMessage");

						if(client != null)
						{
							// Thread pool for getting amount of clients in the same time
							threadPool.execute(new Runnable() {									
								@Override
								public void run() {
									try{	

										// Counting the clients that the server handle on same time
										String clientAddr = (client.getInetAddress()).getHostAddress() +":" + client.getPort();
										message = "\thandling client: " + clientAddr ;
										setChanged();
										notifyObservers("displayMessage");
										
										// Sending the task to handleClient
										clinetHandler.handleClient(client.getInputStream(), client.getOutputStream());
										message = clinetHandler.getMessage();
										setChanged();
										notifyObservers("displayMessage");
										client.close();
										message = "\tdone handling client: " + clientAddr;
										setChanged();
										notifyObservers("displayMessage");

									}catch(IOException e){
										e.printStackTrace();
									}									
								}
							});								
						}
					}
					catch (SocketTimeoutException e){						
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}


			}
		});

		mainServerThread.start();
		try {
			message="server is opened\n" +
					"server address: "+InetAddress.getLocalHost().getHostAddress()+ "\n"+
					"server port is: "+port+"\n"
					+"number of clients to handle on the same time is: "+numOfClients+"\n";
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setChanged();
		notifyObservers("displayMessage");

	}

	/**
	 * Close the server
	 */
	@Override
	public void closeServer() {
		stopServer = true;

		clinetHandler.close();
		try 
		{

			// Waiting for all threads to terminate , check all threads each 10 seconds
			if(threadPool != null) {
				threadPool.shutdown();
				boolean terminated = false;
				while(!terminated)
				{
					try {
						terminated = (threadPool.awaitTermination(10, TimeUnit.SECONDS));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			// Closing the main server thread
			if(mainServerThread!=null)
			{
				mainServerThread.join();		
			}

			// If server is not null , close server
			if(server != null) {
				server.close();
			}
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}


	}

	/**
	 * Get message
	 * @return String Message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * Save properties to file
	 * @param arr  - array of string with the parameters
	 */
	@Override
	public void saveProperties(String[] arr) {
		if (arr.length != 4) {
			setChanged();
			message = "Invalid number of parameters";
			notifyObservers("error");
			return;
		}
		if(!isInteger(arr[0]) || !isInteger(arr[1]) || !isInteger(arr[2])) {
			setChanged();
			message = "Server port or number of clients or time out must be integar!";
			notifyObservers("error");
			return;
		}
		int serverPort = Integer.parseInt(arr[0]);
		int numberOfClients = Integer.parseInt(arr[1]);
		int timeOut = Integer.parseInt(arr[2]);
		String viewSetup = arr[3];

		if( serverPort < 1000) {
			setChanged();
			message = "Minimum port number need to be 1000";
			notifyObservers("error");
			return;
		}
		
		if( serverPort > 9999) {
			setChanged();
			message = "Maximum port number need to be 9999";
			notifyObservers("error");
			return;
		}


		if( numberOfClients > 100 ) {
			setChanged();
			message = "Maximum number of client can be 100";
			notifyObservers("error");
			return;
		}

		if( numberOfClients < 5 ) {
			setChanged();
			message = "Minimum number of client can be 5";
			notifyObservers("error");
			return;
		}
		try 
		{
			XMLEncoder xmlE = new XMLEncoder(new FileOutputStream("serverProperties.xml"));
			xmlE.writeObject(new ServerProperties(serverPort ,numberOfClients,timeOut, viewSetup));
			xmlE.close();
			serverProperties.setPort(serverPort);
			serverProperties.setNumberOfClients(numberOfClients);
			serverProperties.setTimeOut(timeOut);
			serverProperties.setViewSetup(viewSetup);
			message = "You must restart the program before\nthe new setting will take effect.";
			setChanged();
			notifyObservers("displayMessage");

		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}


	}
	
	/**
	 * Getter to get properties
	 * @return Properties
	 */
	public ServerProperties getProperties() {
		return serverProperties;
	}
	
	/**
	 * Function that check if the string is int
	 * @param s - String that need to check
	 * @return True if is int , otherwise false
	 */
	public static boolean isInteger(String s) {
		return isInteger(s,10);
	}

	public static boolean isInteger(String s, int radix) {
		if(s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-') {
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i),radix) < 0) return false;
		}
		return true;
	}

}
