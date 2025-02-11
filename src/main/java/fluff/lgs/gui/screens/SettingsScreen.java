package fluff.lgs.gui.screens;

import fluff.lgs.LGS;
import fluff.lgs.gui.elements.Button;
import fluff.lgs.gui.elements.CenterPanel;
import fluff.lgs.gui.elements.Label;
import fluff.lgs.gui.elements.NicePanel;
import fluff.lgs.gui.elements.TextBox;
import fluff.lgs.gui.elements.ToggleButton;
import fluff.lgs.gui.elements.Window;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.storage.impl.Config;

public class SettingsScreen extends ScrollMenuScreen {
	
	public SettingsScreen() {
		super("Settings");
		
		NicePanel infoPanel = new NicePanel(0, 0, scroll.getContentWidth(), 50, false);
		infoPanel.elements.add(new Label("Show Info Panel", 10, 0, 0, infoPanel.height, Fonts.NORMAL, Align.START, Align.CENTER));
		ToggleButton infoButton = new ToggleButton(0, 0, 20, Config.SHOW_WORLD_INFO);
		infoButton.x = infoPanel.width - infoButton.width - 10;
		infoButton.y = infoPanel.height / 2 - infoButton.height / 2;
		infoPanel.elements.add(infoButton);
		scroll.add(infoPanel);
	}
}
