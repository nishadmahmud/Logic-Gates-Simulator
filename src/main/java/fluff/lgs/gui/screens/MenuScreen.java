package fluff.lgs.gui.screens;

import fluff.lgs.LGS;
import fluff.lgs.gui.Element;
import fluff.lgs.gui.Screen;
import fluff.lgs.gui.elements.CenterPanel;
import fluff.lgs.gui.elements.IconButton;
import fluff.lgs.gui.elements.Label;
import fluff.lgs.gui.elements.Panel;
import fluff.lgs.gui.elements.ScrollPanel;
import fluff.lgs.gui.elements.Spacer;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.resources.Icons;

public abstract class MenuScreen extends Screen {
	
	public static final int WIDTH = 500;
	public static final int HEIGHT = 550;
	
	protected final CenterPanel center;
	protected final Panel panel;
	
	public MenuScreen(String title, int width, int height) {
		center = new CenterPanel(width, height);
		panel = new Panel(10, 60, width - 20, height - 70);
		
		center.elements.add(new Label(title, 0, 0, width, 50, Fonts.HEADER, Align.CENTER, Align.CENTER));
		center.elements.add(new IconButton(Icons.BACK, 0, 0, 48, 48, false, e -> LGS.setScreen(parentScreen)));
		center.elements.add(new Spacer(0, 48, center.width, 12, Align.START));
		center.elements.add(panel);
		
		elements.add(center);
	}
	
	public MenuScreen(String title) {
		this(title, WIDTH, HEIGHT);
	}
	
	protected void horizontal(Panel p, int width, Element... array) {
		final int xOff = 10;
		final int yOff = 10;
		final int gap = 10;
		final int w = (width - xOff * 2 - gap * (array.length - 1)) / array.length;
		final int h = p.height - yOff * 2;
		
		for (int i = 0; i < array.length; i++) {
			Element e = array[i];
			
			e.x = xOff + i * gap + w * i;
			e.y = yOff;
			e.width = w;
			e.height = h;
			
			p.elements.add(e);
		}
	}
}
