package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import presenter.PropertiesLoader;
import presenter.ServerProperties;

/**
 * Generating the main server window
 * @author Elad Jarby
 * @version 1.0
 * @since 19.10.2016
 */
public class ServerGUI extends BasicWindow implements View {
	Button StartServer;
	Button closeServer;
	StyledText ClientStatus;
	Image icon , iconProperties , iconExit;
	String port,numberOfClients,timeOut;
	String[] views = {"cli","gui"};
	private ServerProperties properties;
	private Menu menuBar, fileMenu;
	private MenuItem fileMenuHeader , filePropertiesItem , fileExitItem;
	Color labelColor = new Color(null,0,0,0,0);
	Color fontColor = new Color(null,255,255,255);
	protected Text txtPort;
	protected Text txtNumOfClients;
	protected Text txtTimeOut;
	
	/**
	 * Constructor to initialize all the parameters
	 * @param title - Title of the window
	 * @param width - Width of the window
	 * @param height - Height of the window
	 */
	public ServerGUI(String title, int width, int height) {
		super(title, width, height);
		properties = PropertiesLoader.getInstance().getProperties();
		icon = new Image(display, "resources/icons/icon.png");
		iconProperties = new Image(display, "resources/icons/properties.png");
		iconExit = new Image(display, "resources/icons/exit.png");
		setProperties();
	}
	
	/**
	 * Set all the properties from the serverProperties.xml file
	 */
	private void setProperties() {
		port =  Integer.toString(properties.getPort());
		numberOfClients = Integer.toString(properties.getNumberOfClients());
		timeOut = Integer.toString(properties.getTimeOut());	
		
	}
	
	/**
	 * Initialize all the widgets of the window
	 */
	@Override
	void initWidgets() {
		shell.setLayout(new GridLayout(2, false));
		shell.setBackground(new Color(null, 168,26,25));
		shell.setText("Server");
		shell.setImage(icon);

		// Create start server button
		StartServer = new Button(shell, SWT.PUSH);
		StartServer.setText("Start server  ");
		StartServer.setLayoutData(new GridData(SWT.NONE, SWT.None, false, false, 1, 1));

		// Create client status
		ClientStatus=new StyledText(shell, SWT.MULTI | SWT.BORDER);
		ClientStatus.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,2));
		ClientStatus.setEnabled(false);
		ClientStatus.setForeground(new Color(display,168,26,25));

		// Create close server button
		closeServer = new Button(shell, SWT.PUSH);
		closeServer.setText("Close server");
		closeServer.setLayoutData(new GridData(SWT.NONE, SWT.None, false, false, 1, 1));
		closeServer.setEnabled(false);
		
		// Create menu bar
		menuBar = new Menu(shell,SWT.BAR);
		fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("File");

		// Create file menu
		fileMenu = new Menu(shell,SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);

		// Create properties item
		filePropertiesItem = new MenuItem(fileMenu, SWT.PUSH);
		filePropertiesItem.setText("Properties");
		filePropertiesItem.setImage(iconProperties);
		
		// Create exit item
		fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("Exit");
		fileExitItem.setImage(iconExit);

		shell.setMenuBar(menuBar);
		
		// Listener for properties in the menu.
		filePropertiesItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
						GridData gd;
						//Create new shell and display for new menu
						Shell shellProperties = new Shell(display,SWT.TITLE|SWT.SYSTEM_MODAL| SWT.CLOSE | SWT.MAX);
						shellProperties.setText("Properties");
						//Set new grid with 2 columns
						GridLayout grid = new GridLayout(2 , false);
						shellProperties.setLayout(grid);


						// Create a label for maze name settings
						Label serverPort = new Label(shellProperties, SWT.NONE);
						serverPort.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
						serverPort.setText("Server port: ");
						serverPort.setForeground(labelColor);
						serverPort.setBackground(fontColor);
						txtPort = new Text(shellProperties , SWT.BORDER);
						txtPort.setText(port);
						txtPort.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
						gd = new GridData();
						gd.widthHint = 40;
						txtPort.setLayoutData(gd);
						
						Label numOfClients = new Label(shellProperties, SWT.NONE);
						numOfClients.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
						numOfClients.setText("Clients: ");
						numOfClients.setForeground(labelColor);
						numOfClients.setBackground(fontColor);
						txtNumOfClients = new Text(shellProperties , SWT.BORDER);
						txtNumOfClients.setText(numberOfClients);
						txtNumOfClients.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
						gd = new GridData();
						gd.widthHint = 40;
						txtNumOfClients.setLayoutData(gd);
						
						Label timeOutLabel = new Label(shellProperties, SWT.NONE);
						timeOutLabel.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
						timeOutLabel.setText("Time out: ");
						timeOutLabel.setForeground(labelColor);
						timeOutLabel.setBackground(fontColor);
						txtTimeOut = new Text(shellProperties , SWT.BORDER);
						txtTimeOut.setText(timeOut);
						txtTimeOut.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
						gd = new GridData();
						gd.widthHint = 40;
						txtTimeOut.setLayoutData(gd);
						
						//Create settings for choose view - CLI or GUI
						Label labelView = new Label(shellProperties, SWT.NONE);
						labelView.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
						labelView.setText("View: ");

						Combo setView = new Combo(shellProperties, SWT.BORDER | SWT.READ_ONLY);
						setView.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
						setView.setItems(views);
						setView.setText(properties.getViewSetup());
						gd = new GridData();
						gd.widthHint = 25;
						setView.setLayoutData(gd);
						
						//Create close button in the properties menu
						Button btnClose = new Button(shellProperties, SWT.NONE);
						btnClose.setText("Close");
						btnClose.setLayoutData(new GridData(SWT.CENTER , SWT.None , false , false , 1 , 1));

						//Create save button in the properties menu
						Button btnSave = new Button(shellProperties, SWT.NONE);
						btnSave.setText("Save");
						btnSave.setLayoutData(new GridData(SWT.CENTER , SWT.None , false , false , 1 , 1));

						//Listener for close button
						btnClose.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent arg0) {
								shellProperties.close();						
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent arg0){}
						});

						//Listener for save button
						btnSave.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent arg0) {
								String command = "save_properties " + txtPort.getText() + " " + txtNumOfClients.getText() +
										" " + txtTimeOut.getText() + " " + setView.getText();
								setChanged();
								notifyObservers(command);
								shellProperties.close();
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent arg0){}
						});

						//Display properties menu
						shellProperties.setSize(150,175);
						shellProperties.open();

					}
		});
		
		// Listener for exit menu button
		fileExitItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		
		// Listener for start server
		StartServer.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				StartServer.setEnabled(false);
				closeServer.setEnabled(true);
				setChanged();
				notifyObservers("open_server");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		// Listener for close server
		closeServer.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				StartServer.setEnabled(true);
				closeServer.setEnabled(false);
				setChanged();
				notifyObservers("close_server");
				display("Server shutdown successfuly!");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		// Listener for server close window
		shell.addListener(SWT.Close , new Listener() {

			@Override
			public void handleEvent(Event e) {
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox msgBox = new MessageBox(shell,style);
				msgBox.setText("Exit");
				msgBox.setMessage("Are you sure you want to quit?");
				if(msgBox.open() == SWT.YES) {
					setChanged();
					notifyObservers("close_server");
					e.doit = true;
				} else {
					e.doit = false;
				}
			}
		});

	}
	
	/**
	 * Starting the GUI
	 */
	@Override
	public void start() {
		run();

	}
	
	/**
	 * Function to display all the status in the server </br>
	 * and update each time server get a message/error
	 * @param obj - object
	 */
	@Override
	public void display(Object obj) {
		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				if (obj != null) {
					switch (obj.getClass().getSimpleName()) {
					case "String":
						String message = (String) obj;
						if(message.startsWith("\tSolution is") || message.startsWith("server is opened")) {
							greenFont(message);
						} else {
							ClientStatus.append(message + "\n");
							ClientStatus.setTopIndex(ClientStatus.getLineCount() - 1);
						}
					}
				}

			}});

	}
	
	/**
	 * Function that print the text in green
	 * @param message - Message that need to change
	 */
	private void greenFont(String message) {
		StyleRange style = new StyleRange();
		style.start = ClientStatus.getText().length();
		ClientStatus.append(message + "\n");
		style.length = message.length();
		style.foreground = new Color(null,34,139,34);
		style.font = new Font(display,"Arial", 9, SWT.BOLD );
		ClientStatus.setStyleRange(style);
		ClientStatus.setTopIndex(ClientStatus.getLineCount() - 1);
	}
	
	/**
	 * Displays an error message.
	 * @param error - Error message.
	 */
	@Override
	public void displayError(String error) {
		display(error);

	}
	
	/**
	 * Displays a message.
	 * @param msg - Message.
	 */
	@Override
	public void displayMessage(String msg) {
		display(msg);

	}
}
