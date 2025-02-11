package fluff.lgs.gui.elements;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import fluff.lgs.gui.Element;

public class Icon extends Element {
	
	public Image image;
	
	public Icon(Image image, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.image = image;
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		if (image != null) {
			final int iconX = (width - image.getWidth()) / 2;
			final int iconY = (height - image.getHeight()) / 2;
			
			g.drawImage(image, iconX, iconY, Color.white);
		}
	}
}
