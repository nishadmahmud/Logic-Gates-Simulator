package fluff.lgs.gui.screens;

import fluff.lgs.gui.elements.ScrollPanel;

public abstract class ScrollMenuScreen extends MenuScreen {
	
	protected final ScrollPanel scroll = new ScrollPanel(0, 0, panel.width, panel.height);
	
	public ScrollMenuScreen(String title) {
		super(title);
		
		panel.elements.add(scroll);
	}
}
