package fluff.lgs.gui.elements;

import org.newdawn.slick.Graphics;

import fluff.lgs.gui.Element;
import fluff.lgs.resources.Align;
import fluff.lgs.utils.Colors;

public class Spacer extends Element {
	
	public static final int HEIGHT = 2;
	
	private final Align align;
	
	public Spacer(int x, int y, int width, int height, Align align) {
		super(x, y, width, height);
		this.align = align;
	}
	
	@Override
	public void render(Graphics g, int mouseX, int mouseY) {
		if (align == null) return;
		
		g.setColor(Colors.spacer);
		switch (align) {
			case START:
				g.fillRect(0, 0, width, HEIGHT);
				break;
				
			case CENTER:
				g.fillRect(0, height / 2 - HEIGHT / 2, width, HEIGHT);
				break;
				
			case END:
				g.fillRect(0, height - HEIGHT, width, HEIGHT);
				break;
		}
	}
}
