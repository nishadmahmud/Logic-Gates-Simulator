package fluff.lgs.gui.elements;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import fluff.lgs.gui.ActionListener;
import fluff.lgs.utils.Colors;

public class IconButton extends Button {
	
	protected final boolean background;
	public Image image;
	
	public IconButton(Image image, int x, int y, int width, int height, boolean background, ActionListener<? extends IconButton> action) {
		super(null, x, y, width, height, action);
		this.image = image;
		this.background = background;
	}
	
	public IconButton(Image image, int x, int y, int width, int height, boolean background) {
		this(image, x, y, width, height, background, null);
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		if (background) {
			renderBackground(g, mouseX, mouseY);
			renderIcon(g, mouseX, mouseY, Color.white);
		} else {
			renderIcon(g, mouseX, mouseY, hovered ? Color.white : Colors.icon_button);
		}
	}
	
	protected void renderIcon(Graphics g, int mouseX, int mouseY, Color color) {
		if (image != null) {
			final int iconX = (width - image.getWidth()) / 2;
			final int iconY = (height - image.getHeight()) / 2;
			
			g.drawImage(image, iconX, iconY, color);
		}
	}
}
