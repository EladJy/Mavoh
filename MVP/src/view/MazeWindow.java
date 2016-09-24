package view;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import presenter.Properties;
import presenter.PropertiesLoader;

/**
 * Generating the main window
 * @author Elad Jarby
 * @version 1.0
 * @since 18.09.2016
 */
public class MazeWindow extends BasicWindow implements View {
	boolean closeWindow;
	Combo setPerspective;
	Combo setFloors;
	Combo setWidth;
	Combo setLength;
	Label labelDisplayPosition;
	Label labelEmpty;
	Button btnStartGame;
	Button btnGenerateMaze;
	Button btnSolveMaze;
	Button btnDisplaySolution;
	Button btnGetHints;
	Button btnSaveMazeName;
	String mazeName = "elad";
	String choosenAxis = "";
	int maxSize;
	boolean canCloseGame = true;
	String[] floors = {};
	String[] widths = {};
	String[] lengths = {};
	String[] axis = {"x","y","z"};
	String[] algorithms = {"simple","growing-last","growing-random"};
	String[] searchAlgorithms = {"bfs","dfs"};
	String[] views = {"cli","gui"};
	Color labelColor = new Color(null,0,0,0,0);
	Color fontColor = new Color(null,255,255,255);
	Image buttonImage , buttonDisable;
	Image icon , iconProperties , iconSave , iconLoad , iconExit;
	MazeDisplay mazeDisplay;
	Maze3d maze;
	private Properties properties;
	ArrayList<Thread> threads;

	Menu menuBar , fileMenu;
	MenuItem fileMenuHeader;
	MenuItem filePropertiesItem , fileSaveMazeItem , fileLoadMazeItem , fileExitItem;

	/**
	 * Constructor to initialize all the parameters
	 * @param title - Title of the window
	 * @param width - Width of the window
	 * @param height - Height of the window
	 */
	public MazeWindow(String title, int width, int height) {
		super(title, width, height);
		closeWindow = false;
		properties = PropertiesLoader.getInstance().getProperties();
		threads = new ArrayList<Thread>(properties.getNumberOfThreads());
		buttonImage = new Image(display, "resources/button.jpg");
		buttonDisable = new Image(display, "resources/buttonDisable.jpg");
		icon = new Image(display, "icons/icon.png");
		iconProperties = new Image(display, "icons/properties.png");
		iconSave = new Image(display, "icons/save.png");
		iconLoad = new Image(display, "icons/load.png");
		iconExit = new Image(display, "icons/exit.png");
		setProperties();		
	}

	/**
	 * Set all the properties from the properties.xml file
	 */
	public void setProperties() {
		String size;
		maxSize = properties.getMaxMazeSize();
		floors = new String[maxSize-2];
		widths = new String[maxSize-2];
		lengths = new String[maxSize-2];

		for(int i = 0 ; i < maxSize-2 ; i++) {
			size = Integer.toString(i+3);
			floors[i] = size;
			widths[i] = size;
			lengths[i] = size;
		}
	}

	/**
	 * Initialize all the widgets of the window
	 */
	@Override
	void initWidgets() {

		// Create new grid layout with 3 columns
		GridLayout grid = new GridLayout(3, false);
		shell.setBackground(new Color(null, 168,26,25));
		shell.setLayout(grid);
		shell.setImage(icon);
		GridData gd;
		
		// Create a label for perspective settings
		Label labelPerspective = new Label(shell, SWT.NONE);
		labelPerspective.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
		labelPerspective.setText("Perspective: ");
		labelPerspective.setForeground(fontColor);
		labelPerspective.setBackground(labelColor);

		setPerspective = new Combo(shell, SWT.BORDER | SWT.READ_ONLY); 
		setPerspective.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
		setPerspective.setItems(axis);
		gd = new GridData();
		gd.widthHint = 25;
		setPerspective.setLayoutData(gd);

		// Create maze display
		mazeDisplay = new MazeDisplay(shell,SWT.BORDER | SWT.DOUBLE_BUFFERED);
		mazeDisplay.setLayoutData(new GridData(SWT.FILL , SWT.FILL , true , true , 1 ,14));

		// Create floors settings for maze
		Label labelFloor = new Label(shell, SWT.NONE);
		labelFloor.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
		labelFloor.setText("Floors: ");
		labelFloor.setForeground(fontColor);
		labelFloor.setBackground(labelColor);

		setFloors = new Combo(shell, SWT.BORDER | SWT.READ_ONLY); 
		setFloors.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
		setFloors.setItems(floors);
		gd = new GridData();
		gd.widthHint = 25;
		setFloors.setLayoutData(gd);

		// Create width settings for maze
		Label labelWidth = new Label(shell, SWT.NONE);
		labelWidth.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
		labelWidth.setText("Width: ");
		labelWidth.setForeground(fontColor);
		labelWidth.setBackground(labelColor);

		setWidth = new Combo(shell, SWT.BORDER | SWT.READ_ONLY); 
		setWidth.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
		setWidth.setItems(widths);
		gd = new GridData();
		gd.widthHint = 25;
		setWidth.setLayoutData(gd);

		// Create length settings for maze
		Label labelLength = new Label(shell, SWT.NONE);
		labelLength.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
		labelLength.setText("Length: ");
		labelLength.setForeground(fontColor);
		labelLength.setBackground(labelColor);

		setLength = new Combo(shell, SWT.BORDER | SWT.READ_ONLY); 
		setLength.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
		setLength.setItems(lengths);
		gd = new GridData();
		gd.widthHint = 25;
		setLength.setLayoutData(gd);

		// Create position settings for character
		Label labelPosition = new Label(shell, SWT.NONE);
		labelPosition.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
		labelPosition.setText("Position: ");
		labelPosition.setForeground(fontColor);
		labelPosition.setBackground(labelColor);

		labelDisplayPosition = new Label(shell, SWT.BORDER);
		labelDisplayPosition.setLayoutData(new GridData(SWT.CENTER , SWT.None , false , false , 1 , 1));
		labelDisplayPosition.setText("None");
		gd = new GridData();
		gd.widthHint = 49;
		gd.heightHint = 20;
		FontData[] fd = labelDisplayPosition.getFont().getFontData();
		fd[0].setHeight(10);
		labelDisplayPosition.setFont(new Font(display, fd[0]));
		labelDisplayPosition.setLayoutData(gd);

		// Create a empty label for skipping 
		labelEmpty = new Label(shell, SWT.None);
		labelEmpty.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 2 ,1));
		
		// Create a label for maze name settings
		Label labelMazeName = new Label(shell, SWT.NONE);
		labelMazeName.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
		labelMazeName.setText("Maze name: ");
		labelMazeName.setForeground(fontColor);
		labelMazeName.setBackground(labelColor);
		Text txtName = new Text(shell , SWT.BORDER);
		txtName.setText(mazeName);
		txtName.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 ,1));
		gd = new GridData();
		gd.widthHint = 40;
		txtName.setLayoutData(gd);
		
		btnSaveMazeName = new Button(shell, SWT.NONE);
		gd = new GridData(SWT.CENTER , SWT.NONE , false , false , 2 , 1);
		gd.widthHint = 95;
		btnSaveMazeName.setLayoutData(gd);
		btnSaveMazeName.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent arg0) {
				if(btnSaveMazeName.getEnabled()) 
					arg0.gc.drawImage(buttonImage, 0, 0);	
				else 
					arg0.gc.drawImage(buttonDisable, 0, 0);	
				arg0.gc.setForeground(fontColor);
				arg0.gc.drawText("Save maze name", 2, 5 , SWT.DRAW_TRANSPARENT);

			}
		});
		
		// Create a empty label for skipping 
		labelEmpty = new Label(shell, SWT.None);
		labelEmpty.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 2 ,1));
		
		// Create button for start new game
		btnStartGame = new Button(shell, SWT.NONE);

		gd = new GridData(SWT.CENTER , SWT.NONE , false , false , 2 , 1);
		gd.widthHint = 95;
		btnStartGame.setLayoutData(gd);
		btnStartGame.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent arg0) {
				if(btnStartGame.getEnabled()) 
					arg0.gc.drawImage(buttonImage, 0, 0);	
				else 
					arg0.gc.drawImage(buttonDisable, 0, 0);	
				arg0.gc.setForeground(fontColor);
				arg0.gc.drawText("Start new game", 5, 5 , SWT.DRAW_TRANSPARENT);

			}
		});

		// Create button for generate maze
		btnGenerateMaze = new Button(shell, SWT.NONE);
		btnGenerateMaze.setLayoutData(gd);
		btnGenerateMaze.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent arg0) {
				if(btnGenerateMaze.getEnabled()) 
					arg0.gc.drawImage(buttonImage, 0, 0);		
				else 
					arg0.gc.drawImage(buttonDisable, 0, 0);		
				arg0.gc.setForeground(fontColor);
				arg0.gc.drawText("Generate maze", 7, 5 , SWT.DRAW_TRANSPARENT);
			}
		});

		// Create button for solving maze
		btnSolveMaze = new Button(shell, SWT.NONE);
		btnSolveMaze.setLayoutData(gd);
		btnSolveMaze.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent arg0) {
				if(btnSolveMaze.getEnabled())
					arg0.gc.drawImage(buttonImage, 0, 0);		
				else 
					arg0.gc.drawImage(buttonDisable, 0, 0);	
				arg0.gc.setForeground(fontColor);
				arg0.gc.drawText("Solve maze", 18, 5 , SWT.DRAW_TRANSPARENT);

			}
		});

		// Create button for display solution
		btnDisplaySolution = new Button(shell, SWT.NONE);
		btnDisplaySolution.setLayoutData(gd);
		btnDisplaySolution.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent arg0) {
				if(btnDisplaySolution.getEnabled())
					arg0.gc.drawImage(buttonImage, 0, 0);		
				else 
					arg0.gc.drawImage(buttonDisable, 0, 0);	
				arg0.gc.setForeground(fontColor);
				arg0.gc.drawText("Display solution", 5, 5 , SWT.DRAW_TRANSPARENT);

			}
		});

		// Create button for get hints
		btnGetHints = new Button(shell, SWT.NONE);
		btnGetHints.setLayoutData(gd);
		btnGetHints.addPaintListener(new PaintListener() {

			@Override
			public void paintControl(PaintEvent arg0) {
				if(btnGetHints.isEnabled())
					arg0.gc.drawImage(buttonImage, 0, 0);		
				else 
					arg0.gc.drawImage(buttonDisable, 0, 0);	
				arg0.gc.setForeground(fontColor);
				arg0.gc.drawText("Get hints", 22 , 5 , SWT.DRAW_TRANSPARENT);

			}
		});

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
		
		// Create save maze item
		fileSaveMazeItem = new MenuItem(fileMenu, SWT.PUSH);
		fileSaveMazeItem.setText("Save maze");
		fileSaveMazeItem.setImage(iconSave);
		
		// Create load maze item
		fileLoadMazeItem = new MenuItem(fileMenu, SWT.PUSH);
		fileLoadMazeItem.setText("Load maze");
		fileLoadMazeItem.setImage(iconLoad);
		
		// Create exit item
		fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("Exit");
		fileExitItem.setImage(iconExit);

		shell.setMenuBar(menuBar);

		//Shell Listener - Message box when trying to close the window
		shell.addListener(SWT.Close , new Listener() {

			@Override
			public void handleEvent(Event e) {
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox msgBox = new MessageBox(shell,style);
				msgBox.setText("Exit");
				msgBox.setMessage("Are you sure you want to quit?");
				if(msgBox.open() == SWT.YES) {
					if(canCloseGame) {
						mazeDisplay.stopDisplaySolution();
						setChanged();
						notifyObservers("exit");
						interruptThreads();
						e.doit = true;
						closeWindow = true;
					} else {
						displayError("You need to close all windows!");
						e.doit = false;
					}
				} else {
					e.doit = false;
				}

			}
		});

		// Listener for properties in the menu.
		filePropertiesItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						GridData gd;
						//Create new shell and display for new menu
						Display displayProperties = new Display();
						Shell shellProperties = new Shell(displayProperties);

						//Set new grid with 2 columns
						GridLayout grid = new GridLayout(2 , false);
						shellProperties.setLayout(grid);

						//Create settings for choose how many threads
						Label labelNumberOfThreads = new Label(shellProperties,SWT.NONE);
						labelNumberOfThreads.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
						labelNumberOfThreads.setText("Threads: ");
						Spinner setMaxThreads = new Spinner(shellProperties, SWT.BORDER | SWT.READ_ONLY);
						setMaxThreads.setMinimum(5);
						setMaxThreads.setMaximum(100);
						setMaxThreads.setIncrement(1);
						setMaxThreads.pack();
						setMaxThreads.setSelection(properties.getNumberOfThreads());
						gd = new GridData();
						gd.widthHint = 22;
						setMaxThreads.setLayoutData(gd);

						//Create settings for choose what is the max size for the maze
						Label labelMaxSize = new Label(shellProperties,SWT.NONE);
						labelMaxSize.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
						labelMaxSize.setText("Max size: ");
						Spinner setMaxSize = new Spinner(shellProperties, SWT.BORDER | SWT.READ_ONLY);
						setMaxSize.setMinimum(3);
						setMaxSize.setMaximum(60);
						setMaxSize.setIncrement(1);
						setMaxSize.pack();
						setMaxSize.setSelection(properties.getMaxMazeSize());
						gd = new GridData();
						gd.widthHint = 22;
						setMaxSize.setLayoutData(gd);

						//Create settings for choose search algorithm
						Label labelSearchAlgorithm = new Label(shellProperties, SWT.NONE);
						labelSearchAlgorithm.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
						labelSearchAlgorithm.setText("Search Algorithm: ");

						Combo setSearchAlgorithm = new Combo(shellProperties, SWT.BORDER | SWT.READ_ONLY);
						setSearchAlgorithm.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
						setSearchAlgorithm.setItems(searchAlgorithms);
						setSearchAlgorithm.setText(properties.getSearchAlgorithm());
						gd = new GridData();
						gd.widthHint = 25;
						setSearchAlgorithm.setLayoutData(gd);

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

						//Create settings for choose algorithm
						Label labelAlgorithm = new Label(shellProperties, SWT.NONE);
						labelAlgorithm.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
						labelAlgorithm.setText("Algorithm: ");

						Combo setAlgorithm = new Combo(shellProperties, SWT.BORDER | SWT.READ_ONLY);
						setAlgorithm.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
						setAlgorithm.setItems(algorithms);
						setAlgorithm.setText(properties.getDefaultAlgorithm());
						gd = new GridData();
						gd.widthHint = 90;
						setAlgorithm.setLayoutData(gd);

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
								displayProperties.close();								
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent arg0){}
						});

						//Listener for save button
						btnSave.addSelectionListener(new SelectionListener() {

							@Override
							public void widgetSelected(SelectionEvent arg0) {
								String command = "save_properties " + setMaxThreads.getText() + " " + setAlgorithm.getText() +
										" " + setSearchAlgorithm.getText() + " " + setMaxSize.getText() + " " + setView.getText();
								setChanged();
								notifyObservers(command);
								shellProperties.close();
								displayProperties.close();
							}

							@Override
							public void widgetDefaultSelected(SelectionEvent arg0){}
						});

						//Display properties menu
						shellProperties.setSize(250,210);
						shellProperties.open();

						while(!shellProperties.isDisposed() && !closeWindow) {
							if(!displayProperties.readAndDispatch())
								displayProperties.sleep();
							if(closeWindow) {
								shellProperties.close();
								displayProperties.dispose();
							}
						}
					}
				});
				thread.start();
				threads.add(thread);
			}
		});

		// Listener for save maze in the menu.
		fileSaveMazeItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell,SWT.SAVE);
				fd.setText("Save");
				String selected = fd.open();
				if(selected != null && maze != null) {
					selected = selected.substring(selected.lastIndexOf("\\") +1);
					String command = "save_maze " + mazeName + " " + selected;
					setChanged();
					notifyObservers(command);
				}else if(selected != null){
					displayError("You need to genenrate maze!");
				}
				//				Thread thread = new Thread(new Runnable() {
				//					@Override
				//					public void run() {
				//						//Create new shell and display for new menu
				//						Display displaySave = new Display();
				//						Shell shellSave = new Shell();
				//
				//						//Set new grid with 2 columns
				//						GridLayout grid = new GridLayout(2 , false);
				//						shellSave.setLayout(grid);
				//
				//						//Create settings for maze name
				//						Label labelMazeName = new Label(shellSave , SWT.NONE);
				//						labelMazeName.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
				//						labelMazeName.setText("Maze name: ");
				//
				//						Text textMazeName = new Text(shellSave , SWT.SINGLE | SWT.BORDER);
				//
				//						//Create close button in the save menu
				//						Button btnClose = new Button(shellSave, SWT.NONE);
				//						btnClose.setText("Close");
				//						btnClose.setLayoutData(new GridData(SWT.CENTER , SWT.None , false , false , 1 , 1));
				//
				//						//Create save button in the save menu
				//						Button btnSave = new Button(shellSave, SWT.NONE);
				//						btnSave.setText("Save");
				//						btnSave.setLayoutData(new GridData(SWT.CENTER , SWT.None , false , false , 1 , 1));
				//
				//						//Listener for close button
				//						btnClose.addSelectionListener(new SelectionListener() {
				//
				//							@Override
				//							public void widgetSelected(SelectionEvent arg0) {
				//								shellSave.close();
				//								displaySave.close();								
				//							}
				//
				//							@Override
				//							public void widgetDefaultSelected(SelectionEvent arg0){}
				//						});
				//
				//						//Listener for save button
				//						btnSave.addSelectionListener(new SelectionListener() {
				//
				//							@Override
				//							public void widgetSelected(SelectionEvent arg0) {
				//								if(maze != null) {
				//									String command = "save_maze " + mazeName + " " + textMazeName.getText();
				//									setChanged();
				//									notifyObservers(command);
				//									shellSave.close();
				//									displaySave.close();
				//								} else {
				//									displayMessage("You need to genenrate maze!");
				//								}
				//							}
				//
				//							@Override
				//							public void widgetDefaultSelected(SelectionEvent arg0){}
				//						});
				//
				//						//Display save menu
				//						shellSave.setSize(175,110);
				//						shellSave.open();
				//
				//						while(!shellSave.isDisposed() && !closeWindow) {
				//							if(!displaySave.readAndDispatch())
				//								displaySave.sleep();
				//							if(closeWindow) {
				//								shellSave.close();
				//								displaySave.close();
				//							}
				//						}
				//						
				//
				//					}
				//				});
				//				thread.start();
				//				threads.add(thread);
			}
		});

		// Listener for load maze in the menu.
		fileLoadMazeItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell,SWT.OPEN);
				fd.setText("Load");
				fd.setFilterExtensions(new String[] { "*.maz" });
				String selected = fd.open();
				if(selected != null) {
					mazeName = selected.substring(selected.lastIndexOf("\\") +1 , selected.lastIndexOf("."));
					txtName.setText(mazeName);
					String command = "load_maze " + mazeName + " " + mazeName;
					btnSaveMazeName.setEnabled(false);
					setChanged();
					notifyObservers(command);
				}
				//				Thread thread = new Thread(new Runnable() {
				//					@Override
				//					public void run() {
				//						//Create new shell and display for new menu
				//						Display displayLoad = new Display();
				//						Shell shellLoad = new Shell();
				//
				//						//Set new grid with 2 columns
				//						GridLayout grid = new GridLayout(2 , false);
				//						shellLoad.setLayout(grid);
				//
				//						//Create settings for loading maze with specific name
				//						Label labelLoadMazeName = new Label(shellLoad , SWT.NONE);
				//						labelLoadMazeName.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
				//						labelLoadMazeName.setText("Maze to load: ");
				//
				//						Text textLoadMazeName = new Text(shellLoad , SWT.SINGLE | SWT.BORDER);
				//
				//						//Create settings for new maze name
				//						Label labelNewMazeName = new Label(shellLoad , SWT.NONE);
				//						labelNewMazeName.setLayoutData(new GridData(SWT.None , SWT.None , false , false , 1 , 1));
				//						labelNewMazeName.setText("New name: ");
				//
				//						Text textNewMazeName = new Text(shellLoad , SWT.SINGLE | SWT.BORDER);
				//
				//						//Create close button in the save menu
				//						Button btnClose = new Button(shellLoad, SWT.NONE);
				//						btnClose.setText("Close");
				//						btnClose.setLayoutData(new GridData(SWT.CENTER , SWT.None , false , false , 1 , 1));
				//
				//						//Create save button in the save menu
				//						Button btnSave = new Button(shellLoad, SWT.NONE);
				//						btnSave.setText("Load");
				//						btnSave.setLayoutData(new GridData(SWT.CENTER , SWT.None , false , false , 1 , 1));
				//
				//						//Listener for close button
				//						btnClose.addSelectionListener(new SelectionListener() {
				//
				//							@Override
				//							public void widgetSelected(SelectionEvent arg0) {
				//								shellLoad.close();
				//								displayLoad.close();								
				//							}
				//
				//							@Override
				//							public void widgetDefaultSelected(SelectionEvent arg0){}
				//						});
				//
				//						//Listener for save button
				//						btnSave.addSelectionListener(new SelectionListener() {
				//
				//							@Override
				//							public void widgetSelected(SelectionEvent arg0) {
				//								mazeName = textNewMazeName.getText();
				//								String command = "load_maze " + textLoadMazeName.getText() + " " + mazeName;
				//								setChanged();
				//								notifyObservers(command);
				//								shellLoad.close();
				//								displayLoad.close();		
				//							}
				//
				//							@Override
				//							public void widgetDefaultSelected(SelectionEvent arg0){}
				//						});
				//
				//						//Display load menu
				//						shellLoad.setSize(190,140);
				//						shellLoad.open();
				//
				//						while(!shellLoad.isDisposed() && !closeWindow) {
				//							if(!displayLoad.readAndDispatch())
				//								displayLoad.sleep();
				//							if(closeWindow) {
				//								shellLoad.close();
				//								displayLoad.close();		
				//							}
				//						}
				//					}
				//				});
				//				thread.start();
				//				threads.add(thread);
			}
		});

		// Listener for exit in the menu.
		fileExitItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent o) {
				shell.close();
			}
		});

		//Disable the buttons in the GUI
		setPerspective.setEnabled(false);
		setFloors.setEnabled(false);
		setWidth.setEnabled(false);
		setLength.setEnabled(false);
		btnGenerateMaze.setEnabled(false);
		btnSolveMaze.setEnabled(false);
		btnDisplaySolution.setEnabled(false);
		btnGetHints.setEnabled(false);

		// Listener for perspective box.
		setPerspective.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int index = 0;
				String axis = setPerspective.getText();
				if(maze !=null) {
					if(axis.equals("z")) {
						index = mazeDisplay.getCurrentPosition().getZ();
					} else if(axis.equals("y")) {
						index = mazeDisplay.getCurrentPosition().getY();
					} else if(axis.equals("x")) {
						index = mazeDisplay.getCurrentPosition().getX();
					}
					String command = "display_cross_section " + setPerspective.getText() + " "  + String.valueOf(index) + " " + mazeName;
					mazeDisplay.setAxis(setPerspective.getText());
					choosenAxis = setPerspective.getText();
					setChanged();
					notifyObservers(command);
				} else {
					displayError("Choose how many floors / width / length\nthat you want!");
				}
			}
		});

		//Listener for save maze name box
		btnSaveMazeName.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(txtName.getText() != "") {
					mazeName = txtName.getText();
					String msg = "Maze name set to: " + mazeName;
					displayMessage(msg);
				} else {
					String msg = "You need to enter at least 1 letter";
					displayError(msg);
				}

			}
		});
		
		// Listener for start new game box.
		btnStartGame.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				//Enable GUI buttons
				setPerspective.setEnabled(false);
				setFloors.setEnabled(true);
				setWidth.setEnabled(true);
				setLength.setEnabled(true);
				btnGenerateMaze.setEnabled(true);
				btnGetHints.setEnabled(false);
				String msg = "Hello,\n1. Choose size of floors , width , length for maze.\n2.Press ''Generate Maze''\n3. Choose axis - z / y / x";
				displayMessage(msg);
			}
		});

		// Listener for generate box
		btnGenerateMaze.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String floors = setFloors.getText();
				String width = setWidth.getText();
				String length = setLength.getText();
				String command = "generate_3d_maze " + mazeName + " " + floors + " " + width + " " + length;
				setChanged();
				notifyObservers(command);
				shell.redraw();
				if(!(floors.equals("") || width.equals("") || length.equals(""))) {
					btnGenerateMaze.setEnabled(false);
					btnSaveMazeName.setEnabled(false);
					setPerspective.setEnabled(true);
					btnSolveMaze.setEnabled(true);
					btnGetHints.setEnabled(true);
					setPerspective.setText("z");
				}
			}
		});

		// Listener for solve box
		btnSolveMaze.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(mazeDisplay.inGame) {
					String command = "solve " + mazeName;
					setChanged();
					notifyObservers(command);

					btnSolveMaze.setEnabled(false);
					btnDisplaySolution.setEnabled(true);
				} else {
					displayError("You need to generate maze!");
				}
			}
		});

		// Listener for display solution box
		btnDisplaySolution.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				btnGetHints.setEnabled(false);
				if(mazeDisplay.inGame) {
					if(!choosenAxis.equals("")) {
						String command = "display_solution " + mazeName;
						setChanged();
						notifyObservers(command);
						btnDisplaySolution.setEnabled(false);
						setPerspective.setEnabled(false);
					} else {
						displayError("You need to choose perspective first!");
					}
				} else {
					displayError("You need to generate maze!");
				}
			}
		});

		// Listener for display hints box
		btnGetHints.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Random r = new Random();
				int position;
				if(maze != null){
					position = r.nextInt(3);
					if(position == 0)
						displayMessage("Goal floor is: " + maze.getGoalPosition().getZ());
					else if(position == 1) {
						displayMessage("Goal width is: " + maze.getGoalPosition().getY());
					} else if(position == 2) {
						displayMessage("Goal length is: " + maze.getGoalPosition().getX());
					}
				}
				else {
					displayError("You need to generate maze!");
				}
			}
		});

		// Listener for maze display , to move the character in the maze
		mazeDisplay.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(mazeDisplay.isInGame()) {
					switch(e.keyCode) {
					case SWT.ARROW_LEFT:
						mazeDisplay.moveLeft();
						break;
					case SWT.ARROW_RIGHT:
						mazeDisplay.moveRight();
						break;
					case SWT.ARROW_UP:
						mazeDisplay.moveBackward();
						break;
					case SWT.ARROW_DOWN:
						mazeDisplay.moveForward();
						break;
					case SWT.PAGE_UP:
						mazeDisplay.moveUp();
						break;
					case SWT.PAGE_DOWN:
						mazeDisplay.moveDown();
						break;
					}
					if(mazeDisplay.isInGoalPosition()) {
						mazeDisplay.displayWinningMsg();
						btnGetHints.setEnabled(false);
						btnSolveMaze.setEnabled(false);
						btnDisplaySolution.setEnabled(false);
						btnSaveMazeName.setEnabled(true);
						mazeDisplay.stopGame();
						labelDisplayPosition.setText("None");
						return;
					}
					labelDisplayPosition.setText(mazeDisplay.getCurrentPosition().toString());
				} else {
					displayError("Please generate a maze to play!");
				}
			}
		});

		// Listener for maze display for zoom in or zoom out with ctrl + mouse wheel
		mazeDisplay.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseScrolled(MouseEvent g) {
				if((g.stateMask & SWT.CONTROL) == SWT.CONTROL) {
					mazeDisplay.performZoom(g.count);
				}
			}
		});
	}

	/**
	 * Interrupt all the threads
	 */
	public void interruptThreads() {
		for (int i = 0; i < threads.size(); i++) {
			threads.get(i).interrupt();
		}
	}

	/**
	 * Starting the GUI
	 */
	@Override
	public void start() {
		run();
	}


	/**
	 * Display a list of files/directories for specific path.
	 * @param dirArray - Array of string , containing  files/directories.
	 */
	@Override
	public void displayDirPath(String[] dirArray) {	}

	/**
	 * Displays an error message.
	 * @param error - Error message.
	 */
	@Override
	public void displayError(String error) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				canCloseGame = false;
				Display display = new Display();
				Shell shell = new Shell(display);

				int style = SWT.ICON_ERROR | SWT.OK;

				MessageBox msgBox = new MessageBox(shell,style);
				msgBox.setMessage(error);
				msgBox.open();
				display.dispose();
				canCloseGame = true;
			}
		});
		thread.start();
		threads.add(thread);		
	}

	/**
	 * Displays a message.
	 * @param msg - Message.
	 */
	@Override
	public void displayMessage(String msg) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				canCloseGame = false;
				Display display = new Display();
				Shell shell = new Shell(display);

				int style = SWT.ICON_INFORMATION | SWT.OK;

				MessageBox msgBox = new MessageBox(shell,style);
				msgBox.setMessage(msg);
				msgBox.open();
				display.dispose();
				canCloseGame = true;
			}
		});
		thread.start();
		threads.add(thread);
	}

	/**
	 * Displays a message with maze with array of bytes.
	 * @param arr - Maze with array of bytes
	 * @param msg - Message
	 */
	public void displayMessageWithMaze(byte[] byteArr , String msg) {

		Maze3d maze;
		try {
			maze = new Maze3d(byteArr);
			int maze3DArray[][][] = maze.getMaze3d();
			this.maze = maze;
			String searchMaze = "Maze";
			String searchIsReady = "is ready";
			if(msg.indexOf(searchMaze) != -1 && msg.indexOf(searchIsReady) != -1) {
				mazeDisplay.startGame();

				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						setPerspective.setText("z");
						mazeDisplay.set3DMaze(maze3DArray);
						String command = "display_cross_section " + "z" + " " + maze.getStartPosition().getZ() + " " + mazeName;
						choosenAxis = setPerspective.getText();
						setChanged();
						notifyObservers(command);
					}
				});
			}

			String stringLoaded = "loaded from file";
			if(msg.indexOf(stringLoaded) != -1) {
				mazeDisplay.startGame();

				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						setPerspective.setText("z");
						mazeDisplay.set3DMaze(maze3DArray);
						String command = "display_cross_section " + "z" + " " + maze.getStartPosition().getZ() + " " + mazeName;
						choosenAxis = setPerspective.getText();
						setChanged();
						notifyObservers(command);
						setPerspective.setEnabled(true);
						btnSolveMaze.setEnabled(true);
					}
				});
			}
			displayMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Displays a 3d maze that generated.
	 * @param byteArray - Byte array representing the maze 3d.
	 * @throws Exception exception
	 */
	@Override
	public void displayMaze(byte[] byteArray) throws Exception {}

	/**
	 * Display he cross section that the client asked for.
	 * @param crossSection - Cross section , a matrix (2d array).
	 */
	@Override
	public void displayCrossSection(int[][] crossSection) {
		mazeDisplay.setGoalPosition(maze.getGoalPosition());
		mazeDisplay.setCurrentPosition(maze.getStartPosition());
		mazeDisplay.setAxis(setPerspective.getText());
		mazeDisplay.setMazeData(crossSection);
		String axis = setPerspective.getText();
		if(axis.equals("z"))
			mazeDisplay.setLevel(maze.getStartPosition().getZ());
		else if(axis.equals("y"))
			mazeDisplay.setLevel(maze.getStartPosition().getY());
		else if(axis.equals("x"))
			mazeDisplay.setLevel(maze.getStartPosition().getX());
	}

	/**
	 * Display the solution of the maze.
	 * @param solution - Solution of the maze.
	 */
	@Override
	public void displaySolution(Solution<String> solution) {
		mazeDisplay.setSolution(solution);
		String command = "display_cross_section " + "z" + " " + maze.getStartPosition().getZ() + " " + mazeName;
		setPerspective.setText("z");
		choosenAxis = setPerspective.getText();
		setChanged();
		notifyObservers(command);
		mazeDisplay.start();
		labelDisplayPosition.setText("None");
		btnSaveMazeName.setEnabled(true);
	}

	/**
	 * Setter to set current position
	 * @param pos - Position that need to be set
	 */
	public void setCurrentPosition (Position pos) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				labelDisplayPosition.setText(pos.toString());
			}
		});
	}

}
