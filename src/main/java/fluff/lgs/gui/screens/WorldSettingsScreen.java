package fluff.lgs.gui.screens;

import fluff.lgs.gui.Element;
import fluff.lgs.gui.Screen;
import fluff.lgs.gui.elements.Button;
import fluff.lgs.gui.elements.Label;
import fluff.lgs.gui.elements.NicePanel;
import fluff.lgs.gui.elements.TextBox;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.storage.Worlds;
import fluff.lgs.storage.impl.WorldStorage;

public class WorldSettingsScreen extends ScrollMenuScreen {
	
	private final WorldStorage ws;
	
	private TextBox nameBox;
	
	public WorldSettingsScreen(Screen parentScreen, WorldStorage ws) {
		super("World Settings");
		this.parentScreen = parentScreen;
		this.ws = ws;
		
		NicePanel namePanel = new NicePanel(0, 0, scroll.getContentWidth(), 50, false);
		namePanel.elements.add(new Label("Name", 10, 0, 0, namePanel.height, Fonts.NORMAL, Align.START, Align.CENTER));
		namePanel.elements.add(nameBox = new TextBox(namePanel.width - 270, 10, 200, 30, this::setWorldName));
		nameBox.setPlaceHolder("Name");
		nameBox.setText(ws.getWorld().name);
		nameBox.setCursorPosAtEnd();
		nameBox.setMaxLength(Worlds.NAME_MAX_LENGTH);
		namePanel.elements.add(new Button("Set", namePanel.width - 60, 10, 50, 30, this::setWorldName));
		scroll.add(namePanel);
	}
	
	protected void setWorldName(Element e) {
		if (nameBox.getText().isBlank()) return;
		
		ws.getWorld().name = nameBox.getText();
	}
}
