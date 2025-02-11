package fluff.lgs.gui.screens;

import fluff.lgs.LGS;
import fluff.lgs.gate.connection.Link;
import fluff.lgs.gui.Element;
import fluff.lgs.gui.elements.Button;
import fluff.lgs.gui.elements.Label;
import fluff.lgs.gui.elements.NicePanel;
import fluff.lgs.gui.elements.TextBox;
import fluff.lgs.gui.elements.gate.ButtonConnection;
import fluff.lgs.gui.elements.gate.GateWindow;
import fluff.lgs.resources.Align;
import fluff.lgs.resources.Fonts;
import fluff.lgs.storage.Worlds;

public class GateSettingsScreen extends ScrollMenuScreen {
	
	private final GateWindow gw;
	
	private TextBox nameBox;
	
	public GateSettingsScreen(GateWindow gw) {
		super("Gate Settings");
		this.gw = gw;
		
		NicePanel typePanel = new NicePanel(0, 0, scroll.getContentWidth(), 50, false);
		typePanel.elements.add(new Label("Type", 10, 0, 0, typePanel.height, Fonts.NORMAL, Align.START, Align.CENTER));
		typePanel.elements.add(new Label(gw.gate.type.getID(), typePanel.width - 10, 0, 0, typePanel.height, Fonts.NORMAL, Align.END, Align.CENTER));
		scroll.add(typePanel);
		
		NicePanel namePanel = new NicePanel(0, 0, scroll.getContentWidth(), 50, false);
		namePanel.elements.add(new Label("Name", 10, 0, 0, namePanel.height, Fonts.NORMAL, Align.START, Align.CENTER));
		namePanel.elements.add(nameBox = new TextBox(namePanel.width - 270, 10, 200, 30, this::setWindowName));
		nameBox.setPlaceHolder("Name");
		nameBox.setText(gw.title);
		nameBox.setCursorPosAtEnd();
		nameBox.setMaxLength(Worlds.NAME_MAX_LENGTH);
		namePanel.elements.add(new Button("Set", namePanel.width - 60, 10, 50, 30, this::setWindowName));
		scroll.add(namePanel);
		
		NicePanel removePanel = new NicePanel(0, 0, scroll.getContentWidth(), 50, false);
		horizontal(removePanel, removePanel.width,
				new Button("Remove Gate", 0, 0, 0, 0, this::removeGate),
				new Button("Remove Connections", 0, 0, 0, 0, this::removeConnections)
				);
		scroll.add(removePanel);
	}
	
	protected void setWindowName(Element e) {
		if (nameBox.getText().isBlank()) return;
		
		gw.title = nameBox.getText();
	}
	
	protected void removeGate(Element e) {
		removeConnections(e);
		
		LGS.world().gates.remove(gw);
		LGS.setScreen(null);
	}
	
	protected void removeConnections(Element e) {
		for (ButtonConnection to : gw.gate.inputs) {
			Link link = LGS.world().connections.findTo(to);
			LGS.world().connections.remove(link);
		}
		for (ButtonConnection from : gw.gate.outputs) {
			for (Link link : LGS.world().connections.findFrom(from)) {
				LGS.world().connections.remove(link);
			}
		}
	}
}
