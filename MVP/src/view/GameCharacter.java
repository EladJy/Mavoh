package view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Image;

public class GameCharacter {
	int x;
	int y;
	Image image;
	
	public GameCharacter(int x , int y) {
		this.x = x;
		this.y = y;
	}
	public void paint(PaintEvent e , int length , int width) {
		image = new Image(e.display, "./resources/character.png");
		e.gc.drawImage(image, 0, 0 , image.getBounds().width , image.getBounds().height , x , y , length , width);
	}
}
