package view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;

/**
 * Define the game character in GUI
 * @author Elad Jarby
 * @version 1.0
 * @since 19.09.2016
 */
public class GameCharacter {
	int x;
	int y;
	Image image;
	
	/**
	 * Default constructor to initialize parameters
	 */
	public GameCharacter() {
		x = 0;
		y = 0;
	}
	
	/**
	 * Constructor , Initialize the character with the given parameters
	 * @param x - Position of the character according to X axis
	 * @param y - Position of the character according to Y axis
	 */
	public GameCharacter(int x , int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Paint function , drawing the character 
	 * @param e - Paint event to draw the picture of character
	 * @param length - Length of the character
	 * @param width - Width of the character
	 */
	public void paint(PaintEvent e , int length , int width) {
		image = new Image(e.display, "./resources/character.png");
		e.gc.drawImage(image, 0, 0 , image.getBounds().width , image.getBounds().height , x , y , length , width);
	}
}
